package dev.apexstudios.infusedfoods.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public final class PotionBucketItem extends BucketItem {
    PotionBucketItem(Properties properties) {
        super(InfusedFoods.POTION_FLUID.value(), properties);
    }

    @Override
    public boolean emptyContents(@Nullable LivingEntity user, Level level, BlockPos pos, @Nullable BlockHitResult hitResult, @Nullable ItemStack containerItem) {
        var success = super.emptyContents(user, level, pos, hitResult, containerItem);

        if(success && containerItem != null && level.getBlockEntity(pos) instanceof PotionBlockEntity blockEntity) {
            blockEntity.setPotionContents(containerItem.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY));
        }

        return success;
    }

    @Override
    public Component getName(ItemStack stack) {
        var potion = stack.get(DataComponents.POTION_CONTENTS);

        if(potion == null) {
            return super.getName(stack);
        }

        return potion.getName(getDescriptionId() + ".effect.");
    }
}
