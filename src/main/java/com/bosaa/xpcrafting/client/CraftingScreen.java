package com.bosaa.xpcrafting.client;

import com.bosaa.xpcrafting.crafting.CraftingRecipe;
import com.bosaa.xpcrafting.crafting.RecipeRegistry;
import com.bosaa.xpcrafting.menu.CraftingMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public class CraftingScreen extends AbstractContainerScreen<CraftingMenu> {

    private static final int GUI_WIDTH  = 300;
    private static final int GUI_HEIGHT = 200;
    private static final int LIST_WIDTH  = 140;
    private static final int LIST_HEIGHT = 180;

    private RecipeSelectionList recipeList;
    private Button craftButton;
    private CraftingRecipe selectedRecipe = null;

    public CraftingScreen(CraftingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth  = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width  - GUI_WIDTH)  / 2;
        this.topPos  = (this.height - GUI_HEIGHT) / 2;

        recipeList = new RecipeSelectionList(
                this.minecraft,
                LIST_WIDTH,
                LIST_HEIGHT,
                this.topPos + 10,
                20
        );

        // Populate from RecipeRegistry instead of hardcoded placeholders
        List<CraftingRecipe> recipes = RecipeRegistry.INSTANCE.getRecipes();
        for (CraftingRecipe recipe : recipes) {
            recipeList.addEntry(new RecipeSelectionList.RecipeEntry(recipe.getDisplayName(), () -> {
                selectedRecipe = recipe;
                craftButton.active = true;
            }));
        }

        recipeList.setX(this.leftPos + 5);
        this.addWidget(recipeList);

        craftButton = Button.builder(
                        Component.literal("Craft"),
                        btn -> onCraftClicked()
                )
                .pos(this.leftPos + LIST_WIDTH + 30, this.topPos + GUI_HEIGHT - 30)
                .size(80, 20)
                .build();

        craftButton.active = false;
        this.addRenderableWidget(craftButton);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.fill(
                this.leftPos, this.topPos,
                this.leftPos + GUI_WIDTH, this.topPos + GUI_HEIGHT,
                0xCC000000
        );
        // Border
        graphics.fill(this.leftPos,                 this.topPos,                     this.leftPos + GUI_WIDTH, this.topPos + 1,              0xFF444444);
        graphics.fill(this.leftPos,                 this.topPos + GUI_HEIGHT - 1,    this.leftPos + GUI_WIDTH, this.topPos + GUI_HEIGHT,      0xFF444444);
        graphics.fill(this.leftPos,                 this.topPos,                     this.leftPos + 1,         this.topPos + GUI_HEIGHT,      0xFF444444);
        graphics.fill(this.leftPos + GUI_WIDTH - 1, this.topPos,                     this.leftPos + GUI_WIDTH, this.topPos + GUI_HEIGHT,      0xFF444444);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        recipeList.render(graphics, mouseX, mouseY, partialTick);

        // Title
        graphics.drawString(this.font, "XP Crafting", this.leftPos + 8, this.topPos - 10, 0xFFFFFF);

        // Detail panel
        int detailX = this.leftPos + LIST_WIDTH + 15;
        int detailY = this.topPos + 15;

        if (selectedRecipe == null) {
            graphics.drawString(this.font, "Select a recipe", detailX, detailY, 0xAAAAAA);
        } else {
            // Recipe name
            graphics.drawString(this.font, selectedRecipe.getDisplayName(), detailX, detailY, 0xFFFFFF);
            detailY += 14;

            // Ingredients
            graphics.drawString(this.font, "Ingredients:", detailX, detailY, 0xAAAAAA);
            detailY += 12;

            for (Map.Entry<String, Integer> entry : selectedRecipe.getIngredients().entrySet()) {
                String itemId = entry.getKey();
                int required  = entry.getValue();
                int held      = countItem(itemId);
                boolean met   = held >= required;

                String line = "- " + itemId.replace("minecraft:", "") + " x" + required + " (" + held + " held)";
                graphics.drawString(this.font, line, detailX, detailY, met ? 0x55FF55 : 0xFF5555);
                detailY += 11;
            }

            // XP cost
            detailY += 4;
            int playerLevels = this.menu.getPlayer().experienceLevel;
            boolean xpMet    = playerLevels >= selectedRecipe.getXpCost();
            String xpLine    = "XP: " + selectedRecipe.getXpCost() + " levels (" + playerLevels + " held)";
            graphics.drawString(this.font, xpLine, detailX, detailY, xpMet ? 0x55FF55 : 0xFF5555);

            // Enable/disable craft button based on whether all requirements are met
            craftButton.active = xpMet && canCraft(selectedRecipe);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // Suppress default labels
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (recipeList.isMouseOver(mouseX, mouseY)) {
            return recipeList.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (recipeList.isMouseOver(mouseX, mouseY)) {
            return recipeList.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private int countItem(String registryName) {
        var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(registryName));
        if (item == null) return 0;
        int count = 0;
        for (ItemStack stack : this.menu.getPlayer().getInventory().items) {
            if (stack.getItem() == item) count += stack.getCount();
        }
        return count;
    }

    private boolean canCraft(CraftingRecipe recipe) {
        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            if (countItem(entry.getKey()) < entry.getValue()) return false;
        }
        return true;
    }

    private void onCraftClicked() {
        // Stage 3 next: send CraftRequestPacket to server
    }
}