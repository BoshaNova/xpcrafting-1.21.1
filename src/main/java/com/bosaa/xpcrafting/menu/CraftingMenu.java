package com.bosaa.xpcrafting.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class CraftingMenu extends AbstractContainerMenu {

    private final Player player;

    /**
     * Client-side constructor — called by the MenuType factory via IMenuTypeExtension.
     * The FriendlyByteBuf can carry extra data from the server; we don't need any yet.
     */
    public CraftingMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory);
    }

    /**
     * Server-side constructor — called by XPCraftingTableBlock when the player
     * right-clicks and we do player.openMenu(...).
     */
    public CraftingMenu(int containerId, Inventory playerInventory) {
        super(ModMenuTypes.CRAFTING_MENU.get(), containerId);
        this.player = playerInventory.player;
    }

    /** Keep the menu open as long as the player is alive. */
    @Override
    public boolean stillValid(Player player) {
        return player.isAlive();
    }

    /**
     * No slots, so shift-clicking never moves anything.
     * Required by AbstractContainerMenu but unused here.
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    public Player getPlayer() {
        return player;
    }
}