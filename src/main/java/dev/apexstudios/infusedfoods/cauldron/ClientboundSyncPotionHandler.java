package dev.apexstudios.infusedfoods.cauldron;

import dev.apexstudios.infusedfoods.InfusedFoods;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundSyncPotionHandler(CauldronPotionHandler handler) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncPotionHandler> STREAM_CODEC = StreamCodec.composite(
            CauldronPotionHandler.STREAM_CODEC, ClientboundSyncPotionHandler::handler,
            ClientboundSyncPotionHandler::new
    );

    public static final Type<ClientboundSyncPotionHandler> TYPE = new Type<>(InfusedFoods.identifier("sync_potion_handler"));

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> context.player().level().setData(PotionCauldronSetup.ATTACHMENT, handler));
    }

    @Override
    public Type<ClientboundSyncPotionHandler> type() {
        return TYPE;
    }
}
