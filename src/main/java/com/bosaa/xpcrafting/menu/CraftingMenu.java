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

    public boolean tryCraft(String recipeId) {
        CraftingRecipe recipe = RecipeRegistry.INSTANCE.getRecipes().stream()
                .filter(r -> r.getId().equals(recipeId))
                .findFirst()
                .orElse(null);

        if (recipe == null) return false;
        if (player.experienceLevel < recipe.getXpCost()) return false;

        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(entry.getKey()));
            if (item == null) return false;
            if (countItem(item) < entry.getValue()) return false;
        }

        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(entry.getKey()));
            consumeItem(item, entry.getValue());
        }

        player.giveExperienceLevels(-recipe.getXpCost());

        Item resultItem = BuiltInRegistries.ITEM.get(ResourceLocation.parse(recipe.getResultItem()));
        if (resultItem == null) return false;

        ItemStack result = new ItemStack(resultItem, recipe.getResultCount());
        int countBefore = result.getCount();
        player.getInventory().add(result);

// Drop however many items didn't fit
        // BUG doesn't work in creative, but works in survival
        int notAdded = result.getCount();
        if (notAdded > 0) {
            player.drop(new ItemStack(resultItem, notAdded), false);
        }

        return true;
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