package dev.apexstudios.infusedfoods.core;

import dev.apexstudios.infusedfoods.api.InfusedFoods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.model.data.ModelData;

public final class InfusionCauldronBlockEntity extends BlockEntity {
    public static final String TAG_POTION = "potion";

    private PotionContents potionContents = PotionContents.EMPTY;

    public InfusionCauldronBlockEntity(BlockPos pos, BlockState blockState) {
        super(InfusedFoods.INFUSION_CAULDRON_BLOCK_ENTITY.value(), pos, blockState);
    }

    public void setPotionContents(PotionContents potionContents) {
        this.potionContents = potionContents;
        markDirty();
    }

    public PotionContents getPotionContents() {
        return potionContents;
    }

    public void markDirty() {
        setChanged();
        requestModelDataUpdate();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store(TAG_POTION, PotionContents.CODEC, potionContents);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read(TAG_POTION, PotionContents.CODEC).ifPresent(contents -> this.potionContents = contents);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        potionContents = components.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.POTION_CONTENTS, potionContents);
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard(TAG_POTION);
    }

    @Override
    public ModelData getModelData() {
        return ModelData.of(InfusedFoods.MODEL_PROPERTY_POTION, potionContents);
    }
}
