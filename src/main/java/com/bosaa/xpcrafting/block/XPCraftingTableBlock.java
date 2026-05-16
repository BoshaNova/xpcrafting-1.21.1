package com.bosaa.xpcrafting.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class XPCraftingTableBlock extends Block {

    public static final MapCodec<XPCraftingTableBlock> CODEC = simpleCodec(XPCraftingTableBlock::new);

    public XPCraftingTableBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    /**
     * Called when the player right-clicks the block.
     * For now we just print a message — we'll open the GUI here later.
     */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level,
                                               BlockPos pos, Player player,
                                               BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            player.sendSystemMessage(
                    net.minecraft.network.chat.Component.literal("XP Crafting Table clicked! GUI coming soon.")
            );
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}