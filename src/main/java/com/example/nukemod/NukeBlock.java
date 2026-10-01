package com.example.nukemod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NukeBlock extends Block {
private static final float POWER = 35.0f;

public NukeBlock(Settings settings) {
super(settings);
}

private void detonate(World world, BlockPos pos) {
if (world.isClient) return;
world.removeBlock(pos, false);
world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE,
SoundCategory.BLOCKS, 4.0f, 0.5f);
world.createExplosion(null,
pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
POWER, true, World.ExplosionSourceType.TNT);
}

@Override
public void neighborUpdate(BlockState state, World world, BlockPos pos,
Block sourceBlock, BlockPos sourcePos, boolean notify) {
if (world.isReceivingRedstonePower(pos)) {
detonate(world, pos);
}
}

@Override
public ActionResult onUse(BlockState state, World world, BlockPos pos,
PlayerEntity player, Hand hand, BlockHitResult hit) {
ItemStack stack = player.getStackInHand(hand);
if (stack.isOf(Items.FLINT_AND_STEEL)) {
detonate(world, pos);
if (!player.isCreative()) stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
return ActionResult.success(world.isClient);
}
return super.onUse(state, world, pos, player, hand, hit);
}
}
