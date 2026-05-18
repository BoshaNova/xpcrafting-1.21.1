package com.bosaa.xpcrafting.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeRegistry extends SimplePreparableReloadListener<JsonArray> {

    public static final RecipeRegistry INSTANCE = new RecipeRegistry();

    private List<CraftingRecipe> recipes = Collections.emptyList();

    private RecipeRegistry() {}

    public static void init() {
        NeoForge.EVENT_BUS.register(INSTANCE);
    }

    @SubscribeEvent
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(this);
    }

    // ── Phase 1: load raw JSON off the main thread ───────────────────────────

    @Override
    protected JsonArray prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        try {
            InputStream stream = resourceManager
                    .getResourceOrThrow(
                            net.minecraft.resources.ResourceLocation.parse("xpcrafting:crafting_recipes.json")
                    )
                    .open();
            return JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonArray();
        } catch (Exception e) {
            return new JsonArray();
        }
    }

    // ── Phase 2: parse into recipe objects on the main thread ────────────────

    @Override
    protected void apply(JsonArray data, ResourceManager resourceManager, ProfilerFiller profiler) {
        List<CraftingRecipe> parsed = new ArrayList<>();

        for (JsonElement element : data) {
            try {
                JsonObject obj = element.getAsJsonObject();

                String id          = obj.get("id").getAsString();
                String displayName = obj.get("displayName").getAsString();
                String resultItem  = obj.get("resultItem").getAsString();
                int    resultCount = obj.get("resultCount").getAsInt();
                int    xpCost      = obj.get("xpCost").getAsInt();

                Map<String, Integer> ingredients = new HashMap<>();
                JsonObject ingObj = obj.getAsJsonObject("ingredients");
                for (Map.Entry<String, JsonElement> entry : ingObj.entrySet()) {
                    ingredients.put(entry.getKey(), entry.getValue().getAsInt());
                }

                parsed.add(new CraftingRecipe(id, displayName, ingredients, resultItem, resultCount, xpCost));
            } catch (Exception e) {
                // Skip malformed recipes and continue
            }
        }

        this.recipes = Collections.unmodifiableList(parsed);
    }

    public List<CraftingRecipe> getRecipes() {
        return recipes;
    }
}