package com.bosaa.xpcrafting.network;

import com.bosaa.xpcrafting.menu.CraftingMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CraftRequestPacket(String recipeId) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<CraftRequestPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.parse("xpcrafting:craft_request"));

    public static final StreamCodec<FriendlyByteBuf, CraftRequestPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> buf.writeUtf(packet.recipeId()),
                    buf -> new CraftRequestPacket(buf.readUtf())
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CraftRequestPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof CraftingMenu menu) {
                menu.tryCraft(packet.recipeId());
            }
        });
    }
}