package com.bosaa.xpcrafting.menu;

import com.bosaa.xpcrafting.XPCrafting;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, XPCrafting.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<CraftingMenu>> CRAFTING_MENU =
            MENU_TYPES.register(
                    "crafting_menu",
                    () -> IMenuTypeExtension.create(CraftingMenu::new)
            );

    public static void register(IEventBus modEventBus) {
        MENU_TYPES.register(modEventBus);
    }
}