package dev.apexstudios.infusedfoods.client;

import dev.apexstudios.infusedfoods.api.InfusedFoods;
import java.util.List;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@Mod(value = InfusedFoods.ID, dist = Dist.CLIENT)
public final class InfusedFoodsClientEntryPoint {
    public InfusedFoodsClientEntryPoint(IEventBus modBus) {
        modBus.addListener(RegisterColorHandlersEvent.BlockTintSources.class, event -> event.register(
                List.of(new BlockTintSource() {
                    @Override
                    public int color(BlockState blockState) {
                        return PotionContents.BASE_POTION_COLOR;
                    }

                    @Override
                    public int colorInWorld(BlockState blockState, BlockAndTintGetter level, BlockPos pos) {
                        var potionContents = level.getModelData(pos).get(InfusedFoods.MODEL_PROPERTY_POTION);
                        return potionContents == null ? color(blockState) : potionContents.getColor();
                    }
                }),
                InfusedFoods.INFUSION_CAULDRON.value()
        ));
    }
}
