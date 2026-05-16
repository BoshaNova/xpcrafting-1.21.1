package com.bosaa.xpcrafting;

import com.bosaa.xpcrafting.block.XPCraftingTableBlock;
import com.mojang.logging.LogUtils;

import net.minecraft.world.level.block.state.BlockBehaviour;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(XPCrafting.MODID)
public class XPCrafting {

    public static final String MODID = "xpcrafting";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredBlock<XPCraftingTableBlock> XP_CRAFTING_TABLE =
            BLOCKS.registerBlock(
                    "xp_crafting_table",
                    XPCraftingTableBlock::new,
                    BlockBehaviour.Properties.of()
                            .strength(2.5f)
            );

    public XPCrafting(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
    }
}