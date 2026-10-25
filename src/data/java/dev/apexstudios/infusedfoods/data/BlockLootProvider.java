package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.infusedfoods.api.InfusedFoods;
import java.util.Set;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

final class BlockLootProvider extends BlockLootSubProvider {
    BlockLootProvider(Context output) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, output);
    }

    @Override
    protected void generate() {
        dropOther(InfusedFoods.INFUSION_CAULDRON.value(), Blocks.CAULDRON);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return InfusedFoods.REGISTREE.registrar(Registries.BLOCK).values();
    }
}
