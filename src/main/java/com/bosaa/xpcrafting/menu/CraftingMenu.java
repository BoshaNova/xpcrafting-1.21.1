package com.bosaa.xpcrafting.menu;

import com.bosaa.xpcrafting.crafting.CraftingRecipe;
import com.bosaa.xpcrafting.crafting.RecipeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class CraftingMenu extends AbstractContainerMenu {

    private final Player player;

    public CraftingMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(containerId, playerInventory);
    }

    public CraftingMenu(int containerId, Inventory playerInventory) {
        super(ModMenuTypes.CRAFTING_MENU.get(), containerId);
        this.player = playerInventory.player;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.isAlive();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    public Player getPlayer() {
        return player;
    }

    public void tryCraft(String recipeId) {
        // Find the recipe
        CraftingRecipe recipe = RecipeRegistry.INSTANCE.getRecipes().stream()
                .filter(r -> r.getId().equals(recipeId))
                .findFirst()
                .orElse(null);

        if (recipe == null) return;

        // Check XP
        if (player.experienceLevel < recipe.getXpCost()) return;

        // Check ingredients
        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(entry.getKey()));
            if (item == null) return;
            if (countItem(item) < entry.getValue()) return;
        }

        // Consume ingredients
        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(entry.getKey()));
            consumeItem(item, entry.getValue());
        }

        // Consume XP
        player.giveExperienceLevels(-recipe.getXpCost());

        // Give result
        Item resultItem = BuiltInRegistries.ITEM.get(ResourceLocation.parse(recipe.getResultItem()));
        if (resultItem == null) return;
        ItemStack result = new ItemStack(resultItem, recipe.getResultCount());
        player.getInventory().add(result);
    }

    private int countItem(Item item) {
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == item) count += stack.getCount();
        }
        return count;
    }

    private void consumeItem(Item item, int amount) {
        int remaining = amount;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == item) {
                int take = Math.min(remaining, stack.getCount());
                stack.shrink(take);
                remaining -= take;
                if (remaining <= 0) break;
            }
        }
    }
}