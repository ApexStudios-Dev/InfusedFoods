package dev.apexstudios.infusedfoods.cauldron;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.network.PacketDistributor;

public final class CauldronPotionHandler {
    public static final StreamCodec<RegistryFriendlyByteBuf, CauldronPotionHandler> STREAM_CODEC = ByteBufCodecs.<RegistryFriendlyByteBuf, Long, PotionContents, Map<Long, PotionContents>>map(
            Maps::newHashMapWithExpectedSize,
            ByteBufCodecs.VAR_LONG,
            PotionContents.STREAM_CODEC
    ).map(CauldronPotionHandler::new, handler -> handler.map);

    private final Long2ObjectMap<PotionContents> map = new Long2ObjectOpenHashMap<>();

    private CauldronPotionHandler(Map<Long, PotionContents> map) {
        this.map.putAll(map);
    }

    CauldronPotionHandler() {

    }

    public void set(BlockPos pos, PotionContents contents) {
        if(contents == PotionContents.EMPTY)
            map.remove(pos.asLong());
        else
            map.put(pos.asLong(), contents);
    }

    public PotionContents get(BlockPos pos) {
        return map.getOrDefault(pos.asLong(), PotionContents.EMPTY);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public CauldronPotionHandler copy() {
        return new CauldronPotionHandler(map);
    }

    public static PotionContents get(Level level, BlockPos pos) {
        return getInstance(level).get(pos);
    }

    public static void set(Level level, BlockPos pos, PotionContents contents) {
        var handler = getInstance(level);
        handler.set(pos, contents);

        if(!level.isClientSide)
            PacketDistributor.sendToAllPlayers(new ClientboundSyncPotionHandler(handler));
    }

    public static CauldronPotionHandler getInstance(Level level) {
        return level.getData(PotionCauldronSetup.ATTACHMENT);
    }

    public static final class Serializer implements IAttachmentSerializer<CompoundTag, CauldronPotionHandler> {
        @Override
        public CauldronPotionHandler read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider registries) {
            var map = new Long2ObjectOpenHashMap<PotionContents>();
            var ops = registries.createSerializationContext(NbtOps.INSTANCE);

            for(var key : tag.getAllKeys()) {
                long pos;

                try {
                    pos = Long.parseLong(key);
                } catch (NumberFormatException e) {
                    continue;
                }

                var potion = PotionContents.CODEC.parse(ops, tag.get(key)).getOrThrow();
                map.put(pos, potion);
            }

            return new CauldronPotionHandler(map);
        }

        @Override
        public CompoundTag write(CauldronPotionHandler handler, HolderLookup.Provider registries) {
            var tag = new CompoundTag();
            var ops = registries.createSerializationContext(NbtOps.INSTANCE);

            handler.map.forEach((key, contents) -> {
                var potionTag = PotionContents.CODEC.encodeStart(ops, contents).getOrThrow();
                tag.put(String.valueOf(key), potionTag);
            });

            return tag;
        }
    }
}
