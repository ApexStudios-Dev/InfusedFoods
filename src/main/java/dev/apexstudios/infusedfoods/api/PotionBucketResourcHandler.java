package dev.apexstudios.infusedfoods.api;

import java.util.Objects;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.ItemAccessResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.Nullable;

// BucketResourceHandler is final, Modified for our usage only
public final class PotionBucketResourcHandler extends ItemAccessResourceHandler<FluidResource> {
    public PotionBucketResourcHandler(ItemAccess itemAccess) {
        super(itemAccess, 1);
    }

    @Override
    protected FluidResource getResourceFrom(ItemResource accessResource, int index) {
        if(accessResource.is(InfusedFoods.POTION_BUCKET.value())) {
            return FluidResource.of(InfusedFoods.POTION_FLUID.value())
                    .with(DataComponents.POTION_CONTENTS, accessResource.get(DataComponents.POTION_CONTENTS));
        }

        return FluidResource.EMPTY;
    }

    @Override
    protected int getAmountFrom(ItemResource accessResource, int index) {
        var resource = getResourceFrom(accessResource, index);

        if(resource.isEmpty()) {
            return 0;
        }

        return getCapacity(index, resource);
    }

    @Override
    protected @Nullable ItemResource update(ItemResource accessResource, int index, FluidResource newResource, int newAmount) {
        if(newAmount == 0) {
            return ItemResource.of(Items.BUCKET);
        }

        if(newAmount != FluidType.BUCKET_VOLUME) {
            return ItemResource.EMPTY;
        }

        var newStack = newResource.toStack(newAmount);
        return ItemResource.of(newStack.getFluidType().getBucket(newStack));
    }

    @Override
    protected int getCapacity(int index, FluidResource resource) {
        Objects.checkIndex(index, size());
        return FluidType.BUCKET_VOLUME;
    }
}
