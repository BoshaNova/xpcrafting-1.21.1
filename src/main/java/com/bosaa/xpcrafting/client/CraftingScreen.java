package com.bosaa.xpcrafting.client;

import com.bosaa.xpcrafting.menu.CraftingMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class CraftingScreen extends AbstractContainerScreen<CraftingMenu> {

    private static final int GUI_WIDTH  = 300;
    private static final int GUI_HEIGHT = 200;

    private static final int LIST_WIDTH  = 140;
    private static final int LIST_HEIGHT = 180;

    private static final String[] PLACEHOLDER_RECIPES = {
            "Iron Sword",
            "Diamond Pickaxe",
            "Golden Apple",
            "Enchanted Book",
            "Netherite Ingot",
            "Ender Pearl",
            "Bow",
            "Arrow",
            "Shield",
            "Trident"
    };

    private RecipeSelectionList recipeList;
    private Button craftButton;

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

        for (String name : PLACEHOLDER_RECIPES) {
            recipeList.addEntry(new RecipeSelectionList.RecipeEntry(name));
        }

        recipeList.setX(this.leftPos + 5);
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
                this.leftPos,
                this.topPos,
                this.leftPos + GUI_WIDTH,
                this.topPos  + GUI_HEIGHT,
                0xCC000000
        );

        graphics.fill(this.leftPos,                     this.topPos,
                this.leftPos + GUI_WIDTH,         this.topPos + 1,          0xFF444444);
        graphics.fill(this.leftPos,                     this.topPos + GUI_HEIGHT - 1,
                this.leftPos + GUI_WIDTH,         this.topPos + GUI_HEIGHT, 0xFF444444);
        graphics.fill(this.leftPos,                     this.topPos,
                this.leftPos + 1,                 this.topPos + GUI_HEIGHT, 0xFF444444);
        graphics.fill(this.leftPos + GUI_WIDTH - 1,     this.topPos,
                this.leftPos + GUI_WIDTH,         this.topPos + GUI_HEIGHT, 0xFF444444);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        recipeList.render(graphics, mouseX, mouseY, partialTick);

        int detailX = this.leftPos + LIST_WIDTH + 15;
        int detailY = this.topPos  + 15;
        graphics.drawString(this.font, "Select a recipe", detailX, detailY, 0xFFFFFF);

        graphics.drawString(this.font, "XP Crafting", this.leftPos + 8, this.topPos - 10, 0xFFFFFF);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // Suppress default title and inventory labels
    }

    private void onCraftClicked() {
        // Stage 3 will send a packet to the server here
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



}