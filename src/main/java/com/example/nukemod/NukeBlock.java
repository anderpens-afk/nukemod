package com.example.nukemod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class NukeMod implements ModInitializer {
    public static final String MOD_ID = "nukemod";

    public static final Block NUKE_BLOCK = new NukeBlock(
            AbstractBlock.Settings.create().strength(1.0f));

    public static final Block PULSAR_BLOCK = new PulsarBlock(
            AbstractBlock.Settings.create().strength(5.0f).luminance(s -> 15));

    public static BlockEntityType<PulsarBlockEntity> PULSAR_BE;

    @Override
    public void onInitialize() {
        registerBlock("nuke_block", NUKE_BLOCK);
        registerBlock("pulsar", PULSAR_BLOCK);

        PULSAR_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MOD_ID, "pulsar"),
                BlockEntityType.Builder.create(PulsarBlockEntity::new, PULSAR_BLOCK).build(null));
    }

    private static void registerBlock(String name, Block block) {
        Identifier id = new Identifier(MOD_ID, name);
        Registry.register(Registries.BLOCK, id, block);
        Item item = Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(e -> e.add(item));
    }
}
