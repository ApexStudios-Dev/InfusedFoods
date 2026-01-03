package dev.apexstudios.infusedfoods.common.cauldron;

import dev.apexstudios.infusedfoods.common.util.InfusionEntries;
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

public final class PotionCauldronBlockEntity extends BlockEntity {
    private static final String TAG_POTION = "PotionContents";

    private PotionContents potionContents = PotionContents.EMPTY;

    public PotionCauldronBlockEntity(BlockPos pos, BlockState blockState) {
        super(InfusionEntries.CAULDRON_BLOCK_ENTITY.value(), pos, blockState);
    }

    public PotionContents getPotionContents() {
        return potionContents;
    }

    public void setPotionContents(PotionContents potionContents) {
        this.potionContents = potionContents;
        setChanged();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.storeNullable(TAG_POTION, PotionContents.CODEC, potionContents);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        potionContents = input.read(TAG_POTION, PotionContents.CODEC).orElse(PotionContents.EMPTY);
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
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
