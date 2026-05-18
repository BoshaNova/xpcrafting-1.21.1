package com.bosaa.xpcrafting.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CraftSuccessPacket() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<CraftSuccessPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.parse("xpcrafting:craft_success"));

    public static final StreamCodec<FriendlyByteBuf, CraftSuccessPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> {},
                    buf -> new CraftSuccessPacket()
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CraftSuccessPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                // Force client inventory to sync with server state
                mc.player.inventoryMenu.broadcastChanges();
                mc.player.containerMenu.broadcastChanges();
            }
        });
    }
}