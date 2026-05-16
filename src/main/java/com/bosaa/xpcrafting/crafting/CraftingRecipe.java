package com.bosaa.xpcrafting.crafting;

import java.util.Map;

/**
 * Represents a single XP crafting recipe loaded from JSON.
 *
 * Example JSON structure this maps to:
 * {
 *   "id": "iron_sword",
 *   "ingredients": {
 *     "minecraft:iron_ingot": 2,
 *     "minecraft:stick": 1
 *   },
 *   "result_item": "minecraft:iron_sword",
 *   "result_count": 1,
 *   "xp_cost": 5
 * }
 */
public class CraftingRecipe {

    private final String id;
    private final Map<String, Integer> ingredients; // item registry name -> count required
    private final String resultItem;                // item registry name of what you get
    private final int resultCount;
    private final int xpCost;

    public CraftingRecipe(String id, Map<String, Integer> ingredients,
                          String resultItem, int resultCount, int xpCost) {
        this.id = id;
        this.ingredients = ingredients;
        this.resultItem = resultItem;
        this.resultCount = resultCount;
        this.xpCost = xpCost;
    }

    public String getId()                       { return id; }
    public Map<String, Integer> getIngredients(){ return ingredients; }
    public String getResultItem()               { return resultItem; }
    public int getResultCount()                 { return resultCount; }
    public int getXpCost()                      { return xpCost; }
}