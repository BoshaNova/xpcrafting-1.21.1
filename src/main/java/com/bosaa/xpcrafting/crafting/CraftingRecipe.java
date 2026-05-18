package com.bosaa.xpcrafting.crafting;

import java.util.Map;

public class CraftingRecipe {

    private final String id;
    private final String displayName;
    private final Map<String, Integer> ingredients;
    private final String resultItem;
    private final int resultCount;
    private final int xpCost;

    public CraftingRecipe(String id, String displayName, Map<String, Integer> ingredients,
                          String resultItem, int resultCount, int xpCost) {
        this.id = id;
        this.displayName = displayName;
        this.ingredients = ingredients;
        this.resultItem = resultItem;
        this.resultCount = resultCount;
        this.xpCost = xpCost;
    }

    public String getId()                        { return id; }
    public String getDisplayName()               { return displayName; }
    public Map<String, Integer> getIngredients() { return ingredients; }
    public String getResultItem()                { return resultItem; }
    public int getResultCount()                  { return resultCount; }
    public int getXpCost()                       { return xpCost; }
}