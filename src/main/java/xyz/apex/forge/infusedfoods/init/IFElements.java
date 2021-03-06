package xyz.apex.forge.infusedfoods.init;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.MenuEntry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import xyz.apex.forge.commonality.Mods;
import xyz.apex.forge.commonality.tags.BlockTags;
import xyz.apex.forge.commonality.tags.ItemTags;
import xyz.apex.forge.infusedfoods.block.InfusionStationBlock;
import xyz.apex.forge.infusedfoods.block.entity.InfusionStationBlockEntity;
import xyz.apex.forge.infusedfoods.client.renderer.InfusionStationBlockEntityRenderer;
import xyz.apex.forge.infusedfoods.client.screen.InfusionStationMenuScreen;
import xyz.apex.forge.infusedfoods.container.InfusionStationMenu;
import xyz.apex.forge.infusedfoods.item.InfusionStationBlockItem;

import static xyz.apex.forge.infusedfoods.block.entity.InfusionStationBlockEntity.*;

public final class IFElements
{
	public static final ResourceLocation INFUSION_STATION_CONTAINER_SCREEN_TEXTURE = new ResourceLocation(Mods.INFUSED_FOODS, "textures/gui/container/infusion_station.png");
	public static final ResourceLocation INFUSION_STATION_BLOCK_TEXTURE = new ResourceLocation(Mods.INFUSED_FOODS, "textures/models/infusion_station.png");
	public static final ResourceLocation INFUSION_STATION_BLOCK_TEXTURE_TINT = new ResourceLocation(Mods.INFUSED_FOODS, "textures/models/infusion_station_tint.png");

	public static final BlockEntry<InfusionStationBlock> INFUSION_STATION_BLOCK = IFRegistry.INSTANCE
			.object("infusion_station")
			.block(InfusionStationBlock::new)

			.lang("Infusion Station")

			.blockstate((ctx, provider) -> provider
							.getVariantBuilder(ctx.get())
							.forAllStates(blockState -> ConfiguredModel
									.builder()
										.modelFile(provider
												.models()
												.getBuilder(ctx.getName())
												.texture("particle", "minecraft:block/stone")
										)
									.build()
							)
			)
			.loot((lootTables, block) -> lootTables.add(block, LootTable
					.lootTable()
					.withPool(RegistrateBlockLootTables.applyExplosionCondition(block, LootPool
							.lootPool()
							.setRolls(ConstantValue.exactly(1))
							.add(LootItem.lootTableItem(block))
							.apply(CopyNbtFunction
									.copyData(ContextNbtProvider.BLOCK_ENTITY)
									.copy(buildNbtPath(NBT_APEX, NBT_INFUSION_FLUID), buildNbtPath(NBT_APEX, NBT_INFUSION_FLUID))
									.copy(buildNbtPath(NBT_APEX, NBT_BLAZE_FUEL), buildNbtPath(NBT_APEX, NBT_BLAZE_FUEL))
							)
					))
			))
			.recipe((ctx, provider) -> ShapedRecipeBuilder
					.shaped(ctx.get(), 1)
					.define('B', ItemTags.Forge.RODS_BLAZE)
					.define('#', ItemTags.Forge.STONE)
					.pattern(" B ")
					.pattern("###")
					.unlockedBy("has_blaze_rod", RegistrateRecipeProvider.has(ItemTags.Forge.RODS_BLAZE))
					.save(provider, ctx.getId())
			)

			.initialProperties(Material.METAL)
			.properties(properties -> properties
					.sound(SoundType.METAL)
					.noOcclusion()
					.requiresCorrectToolForDrops()
					.strength(.5F)
					.lightLevel(blockState -> 1)
			)

			.addLayer(() -> RenderType::cutout)

			.tag(BlockTags.Vanilla.MINEABLE_WITH_PICKAXE)

			.item(InfusionStationBlockItem::new)
				.model((ctx, provider) -> {
							var id = ctx.getId();
					var builtInEntity = new ModelFile.UncheckedModelFile("minecraft:builtin/entity");

							provider.getBuilder(id.getNamespace() + ":item/" + id.getPath())
						        .parent(builtInEntity)
								.transforms()
									.transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
										.rotation(0F, 0F, 0F)
										.translation(0F, 0F, 0F)
										.scale(1F, 1F, 1F)
									.end()
									.transform(ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
										.rotation(0F, 0F, 0F)
										.translation(0F, 0F, 0F)
										.scale(1F, 1F, 1F)
									.end()
									.transform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
										.rotation(0F, 0F, 0F)
										.translation(0F, 0F, 0F)
										.scale(1F, 1F, 1F)
									.end()
									.transform(ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
										.rotation(0F, 0F, 0F)
										.translation(0F, 0F, 0F)
										.scale(1F, 1F, 1F)
									.end()
									.transform(ItemTransforms.TransformType.HEAD)
										.rotation(0F, 0F, 0F)
										.translation(0F, 0F, 0F)
										.scale(1F, 1F, 1F)
									.end()
									.transform(ItemTransforms.TransformType.GROUND)
										.rotation(0F, 0F, 0F)
										.translation(0F, 0F, 0F)
										.scale(1F, 1F, 1F)
									.end()
									.transform(ItemTransforms.TransformType.FIXED)
										.rotation(0F, 0F, 0F)
										.translation(0F, 0F, 0F)
										.scale(1F, 1F, 1F)
									.end()
									.transform(ItemTransforms.TransformType.GUI)
										.rotation(0F, 0F, 0F)
										.translation(0F, 0F, 0F)
										.scale(1F, 1F, 1F)
									.end()
								.end();
				})
			.build()

			.blockEntity(InfusionStationBlockEntity::new)
				.renderer(() -> InfusionStationBlockEntityRenderer::new)
			.build()

	.register();

	public static final MenuEntry<InfusionStationMenu> INFUSION_STATION_MENU = IFRegistry.INSTANCE
			.object("infusion_station")
			.menu(InfusionStationMenu::new, () -> InfusionStationMenuScreen::new)
			.register();

	public static final ItemEntry<InfusionStationBlockItem> INFUSION_STATION_BLOCK_ITEM = ItemEntry.cast(INFUSION_STATION_BLOCK.getSibling(Registry.ITEM_REGISTRY));
	public static final BlockEntityEntry<InfusionStationBlockEntity> INFUSION_STATION_BLOCK_ENTITY = BlockEntityEntry.cast(INFUSION_STATION_BLOCK.getSibling(Registry.BLOCK_ENTITY_TYPE_REGISTRY));

	static void bootstrap()
	{
	}

	private static String buildNbtPath(String... paths)
	{
		return String.join(".", paths);
	}
}