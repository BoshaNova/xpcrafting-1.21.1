package com.bosaa.xpcrafting.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;

public class RecipeSelectionList extends ObjectSelectionList<RecipeSelectionList.RecipeEntry> {

    public RecipeSelectionList(Minecraft minecraft, int width, int height,
                               int y0, int itemHeight) {
        super(minecraft, width, height, y0, itemHeight);
    }

    @Override
    public int getRowWidth() {
        return this.width - 6;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getX() + this.width - 6;
    }

    // Expose addEntry publicly so CraftingScreen can call it
    @Override
    public int addEntry(RecipeEntry entry) {
        return super.addEntry(entry);
    }
    public static class RecipeEntry extends ObjectSelectionList.Entry<RecipeEntry> {

        private final String displayName;

        public RecipeEntry(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int top, int left,
                           int width, int height, int mouseX, int mouseY,
                           boolean isMouseOver, float partialTick) {
            graphics.drawString(
                    Minecraft.getInstance().font,
                    displayName,
                    left + 3,
                    top + (height - 8) / 2,
                    0xFFFFFF
            );
        }

        @Override
        public Component getNarration() {
            return Component.literal(displayName);
        }
    }
}