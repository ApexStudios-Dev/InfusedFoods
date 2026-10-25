package dev.apexstudios.infusedfoods.api;

import dev.apexstudios.infusedfoods.core.CleansingRecipe;
import dev.apexstudios.infusedfoods.core.InfusionCauldronBlock;
import dev.apexstudios.infusedfoods.core.InfusionCauldronBlockEntity;
import dev.apexstudios.registree.api.Registree;
import dev.apexstudios.registree.api.holders.DeferredBlockEntityType;
import dev.apexstudios.registree.api.holders.DeferredRecipeSerializer;
import dev.apexstudios.registree.api.util.BlockEntityRegistrations;
import dev.apexstudios.registree.api.util.BlockRegistrations;
import dev.apexstudios.registree.api.util.RecipeSerializerRegistrations;
import java.util.function.UnaryOperator;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.model.data.ModelProperty;
import net.neoforged.neoforge.registries.DeferredBlock;

public interface InfusedFoods {
    String ID = "infusedfoods";
    Registree REGISTREE = Registree.create(ID);

    TagKey<Item> CLEANSING_AGENTS = REGISTREE.tag(Registries.ITEM, "cleansing_agents");
    TagKey<Potion> INFUSION_BLACKLIST = REGISTREE.tag(Registries.POTION, "infusion_blacklist");

    DeferredBlock<InfusionCauldronBlock> INFUSION_CAULDRON = BlockRegistrations.register(
            REGISTREE,
            "infusion_cauldron",
            InfusionCauldronBlock::new,
            () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAULDRON),
            UnaryOperator.identity()
    );

    DeferredBlockEntityType<InfusionCauldronBlockEntity> INFUSION_CAULDRON_BLOCK_ENTITY = BlockEntityRegistrations.registerForBlock(REGISTREE, INFUSION_CAULDRON, InfusionCauldronBlockEntity::new);
    DeferredRecipeSerializer<CleansingRecipe> CLEANSING_RECIPE = RecipeSerializerRegistrations.register(REGISTREE, "cleansing", CleansingRecipe.MAP_CODEC, CleansingRecipe.STREAM_CODEC);
    ModelProperty<PotionContents> MODEL_PROPERTY_POTION = new ModelProperty<>();
}
