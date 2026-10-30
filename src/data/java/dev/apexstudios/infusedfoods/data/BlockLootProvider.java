package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.infusedfoods.api.InfusedFoods;
import net.minecraft.world.level.block.Blocks;

final class BlockLootProvider extends dev.apexstudios.apexcore.api.data.BlockLootProvider {
    BlockLootProvider(Context output) {
        super(output, InfusedFoods.REGISTREE);
    }

    @Override
    protected void generate() {
        dropOther(InfusedFoods.POTION_CAULDRON_BLOCK.value(), Blocks.CAULDRON);
    }
}
