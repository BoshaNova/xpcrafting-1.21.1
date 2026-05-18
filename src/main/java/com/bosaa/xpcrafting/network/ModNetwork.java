package com.bosaa.xpcrafting.network;

import com.bosaa.xpcrafting.XPCrafting;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = XPCrafting.MODID)
public class ModNetwork {

    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                CraftRequestPacket.TYPE,
                CraftRequestPacket.STREAM_CODEC,
                CraftRequestPacket::handle
        );
        registrar.playToClient(
                CraftSuccessPacket.TYPE,
                CraftSuccessPacket.STREAM_CODEC,
                CraftSuccessPacket::handle
        );
    }
}