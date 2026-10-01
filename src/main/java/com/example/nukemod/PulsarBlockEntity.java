package com.example.nukemod;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class PulsarBlockEntity extends BlockEntity {
static final int LENGTH = 48;
static final boolean CUT_BLOCKS = true;
static final double CONE = 0.95;
static final double SPIN = 0.05;

private static final ParticleEffect CYAN =
new DustParticleEffect(new Vector3f(0.2f, 0.8f, 1.0f), 1.6f);
private static final ParticleEffect VIOLET =
new DustParticleEffect(new Vector3f(0.7f, 0.3f, 1.0f), 1.3f);
private static final ParticleEffect ORANGE =
new DustParticleEffect(new Vector3f(1.0f, 0.5f, 0.1f), 1.2f);

private int age = 0;
private int ring = -1;

public PulsarBlockEntity(BlockPos pos, BlockState state) {
super(NukeMod.PULSAR_BE, pos, state);
}

public static void tick(World world, BlockPos pos, BlockState state, PulsarBlockEntity be) {
if (!(world instanceof ServerWorld sw)) return;
if (sw.isReceivingRedstonePower(pos)) return;
be.serverTick(sw, pos);
}

private void serverTick(ServerWorld sw, BlockPos pos) {
age++;
double cx = pos.getX() + 0.5, cy = pos.getY() + 0.5, cz = pos.getZ() + 0.5;

double tilt = 0.5 + 0.3 * Math.sin(age * 0.012);
double ax = Math.sin(tilt), ay = Math.cos(tilt);
double ux = Math.cos(tilt), uy = -Math.sin(tilt);

double phi = age * SPIN;
double cp = Math.cos(phi), sp = Math.sin(phi);
double sc = Math.sin(CONE), cc = Math.cos(CONE);
double dx = ax * cc + (ux * cp) * sc;
double dy = ay * cc + (uy * cp) * sc;
double dz = (sp) * sc;

beam(sw, pos, cx, cy, cz, dx, dy, dz, CYAN);
beam(sw, pos, cx, cy, cz, -dx, -dy, -dz, VIOLET);

sw.spawnParticles(ParticleTypes.END_ROD, cx, cy, cz, 3, 0.4, 0.4, 0.4, 0.05);
sw.spawnParticles(ParticleTypes.PORTAL, cx, cy, cz, 4, 0.8, 0.8, 0.8, 0.5);

int n = 24;
for (int i = 0; i < n; i++) {
double th = age * 0.15 + i * (Math.PI * 2 / n);
double r = 2.2 + 0.6 * Math.sin(i * 3 + age * 0.1);
double c = Math.cos(th), s = Math.sin(th);
double px = cx + (ux * c) * r;
double py = cy + (uy * c) * r;
double pz = cz + s * r;
sw.spawnParticles(ORANGE, px, py, pz, 1, 0, 0, 0, 0);
}

if (age % 80 == 0) {
ring = 0;
sw.playSound(null, pos, SoundEvents.BLOCK_BEACON_POWER_SELECT,
SoundCategory.BLOCKS, 3.0f, 0.5f);
}
if (ring >= 0) {
double r = 1 + ring * 1.5;
int pts = 56;
for (int i = 0; i < pts; i++) {
double th = i * (Math.PI * 2 / pts);
double c = Math.cos(th), s = Math.sin(th);
sw.spawnParticles(ParticleTypes.END_ROD,
cx + ux * c * r, cy + uy * c * r, cz + s * r, 1, 0, 0, 0, 0);
}
if (++ring > 30) ring = -1;
}

if (age % 40 == 0) {
sw.playSound(null, pos, SoundEvents.BLOCK_BEACON_AMBIENT,
SoundCategory.BLOCKS, 2.0f, 0.6f);
}
}

private void beam(ServerWorld sw, BlockPos origin, double cx, double cy, double cz,
double dx, double dy, double dz, ParticleEffect color) {
for (double t = 1.5; t <= LENGTH; t += 1.0) {
double x = cx + dx * t, y = cy + dy * t, z = cz + dz * t;
BlockPos bp = BlockPos.ofFloored(x, y, z);

if (((int) t) % 2 == 0) {
sw.spawnParticles(color, x, y, z, 1, 0.05, 0.05, 0.05, 0);
} else {
sw.spawnParticles(ParticleTypes.END_ROD, x, y, z, 1, 0, 0, 0, 0);
}

if (CUT_BLOCKS) {
carve(sw, origin, bp);
for (net.minecraft.util.math.Direction d : net.minecraft.util.math.Direction.values()) {
carve(sw, origin, bp.offset(d));
}
}
}
sw.spawnParticles(ParticleTypes.FLASH,
cx + dx * LENGTH, cy + dy * LENGTH, cz + dz * LENGTH, 1, 0, 0, 0, 0);
}

private void carve(ServerWorld sw, BlockPos origin, BlockPos bp) {
if (bp.equals(origin)) return;
if (!sw.isInBuildLimit(bp)) return;
if (!sw.getChunkManager().isChunkLoaded(bp.getX() >> 4, bp.getZ() >> 4)) return;
BlockState st = sw.getBlockState(bp);
if (st.isAir() || st.getHardness(sw, bp) < 0) return;
sw.setBlockState(bp, Blocks.AIR.getDefaultState(), 2);
if (sw.random.nextInt(4) == 0) {
sw.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
bp.getX() + 0.5, bp.getY() + 0.5, bp.getZ() + 0.5, 2, 0.3, 0.3, 0.3, 0.1);
}
}
}
