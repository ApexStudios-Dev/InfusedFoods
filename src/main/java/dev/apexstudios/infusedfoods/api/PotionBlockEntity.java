package dev.apexstudios.infusedfoods.api;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;

public final class PotionBlockEntity extends BlockEntity {
    public static final String TAG_POTION = "potion";

    public static final ModelProperty<PotionContents> MODEL_PROPERTY = new ModelProperty<>();

    private PotionContents potionContents = PotionContents.EMPTY;

    PotionBlockEntity(BlockPos pos, BlockState blockState) {
        super(InfusedFoods.POTION_BLOCK_ENTITY.value(), pos, blockState);
    }

    public void setPotionContents(PotionContents potionContents) {
        this.potionContents = potionContents;
        setChanged();
    }

    public PotionContents getPotionContents() {
        return potionContents;
    }

    public boolean isSamePotion(DataComponentGetter components) {
        return Objects.equals(potionContents, components.get(DataComponents.POTION_CONTENTS));
    }

    public void applyTo(Entity entity) {
        if(!(entity instanceof LivingEntity living)) {
            return;
        }

        if(!living.isAffectedByPotions()) {
            return;
        }

        potionContents.applyToLivingEntity(living, 1F);
    }

    public void addParticles(Level level, RandomSource random, int fluidLevel) {
        var particle = ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, potionContents.getColor());

        var x = worldPosition.getX() + .5D;
        var y = worldPosition.getY() + .5D + (.25D * fluidLevel);
        var z = worldPosition.getZ() + .5D;

        for(var i = 0; i < 2; i ++) {
            var rand = random.nextInt(0, 4);

            if(rand == 0) {
                level.addParticle(particle, x + .25D, y, z, 0D, 0D, 0D);
            } else if(rand == 1) {
                level.addParticle(particle, x - .25D, y, z, 0D, 0D, 0D);
            } else if(rand == 2) {
                level.addParticle(particle, x, y, z + .25D, 0D, 0D, 0D);
            } else {
                level.addParticle(particle, x, y, z - .25D, 0D, 0D, 0D);
            }
        }
    }

    @Override
    public void onDataPacket(Connection net, ValueInput valueInput) {
        super.onDataPacket(net, valueInput);
        requestModelDataUpdate();
    }

    @Override
    public ModelData getModelData() {
        return ModelData.of(MODEL_PROPERTY, potionContents);
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
        setChanged();
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
}
