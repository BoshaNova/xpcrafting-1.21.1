package com.bosaa.xpcrafting.client;

import com.bosaa.xpcrafting.menu.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = "xpcrafting", value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.CRAFTING_MENU.get(), CraftingScreen::new);
    }
}