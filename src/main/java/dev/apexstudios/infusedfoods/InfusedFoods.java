package dev.apexstudios.infusedfoods;

import dev.apexstudios.apexcore.lib.fluid.ItemOnlyFluid;
import dev.apexstudios.apexcore.lib.registree.Registree;
import dev.apexstudios.apexcore.lib.registree.holder.DeferredFluid;
import dev.apexstudios.apexcore.lib.registree.holder.DeferredFluidType;
import dev.apexstudios.apexcore.lib.tooltip.RegisterTooltipEvent;
import dev.apexstudios.apexcore.lib.tooltip.TooltipPosition;
import dev.apexstudios.infusedfoods.cauldron.ClientboundSyncPotionHandler;
import dev.apexstudios.infusedfoods.cauldron.PotionCauldronSetup;
import dev.apexstudios.infusedfoods.recipe.RecipeSetup;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.Nullable;

@Mod(InfusedFoods.ID)
public final class InfusedFoods {
    public static final String ID = "infusedfoods";
    public static final Registree REGISTREE = new Registree(ID);

    public static final TagKey<Item> ITEM_BLACKLIST = InfusedFoods.REGISTREE.tag(Registries.ITEM, "infusion_blacklist");
    public static final TagKey<Potion> POTION_BLACKLIST = InfusedFoods.REGISTREE.tag(Registries.POTION, "infusion_blacklist");

    public static final ResourceKey<CreativeModeTab> INFUSED_FOODS = InfusedFoods.REGISTREE.registerCreativeModeTab("foods", builder -> builder
            .displayItems((parameters, output) -> parameters.holders()
                    .lookup(Registries.ITEM)
                    .stream()
                    .map(items -> items.filterFeatures(parameters.enabledFeatures()))
                    .flatMap(HolderLookup::listElements)
                    .map(Holder::value)
                    .map(Item::getDefaultInstance)
                    .filter(InfusedFoods::isValidFood)
                    .flatMap(stack -> createInfusedItems(stack, parameters))
                    .forEach(output::accept)
            )
            .icon(Items.APPLE::getDefaultInstance)
            .withSearchBar()
    );

    public static final DeferredFluidType<FluidType> POTION_FLUID_TYPE = InfusedFoods.REGISTREE.registerSimpleFluidType("potion");
    public static final DeferredFluid<Fluid> POTION_FLUID = InfusedFoods.REGISTREE.registerFluid("potion", ItemOnlyFluid.simpleFactory(POTION_FLUID_TYPE, Items.POTION));

    public InfusedFoods(IEventBus modBus) {
        REGISTREE.registerEvents(modBus);
        PotionCauldronSetup.register(modBus);
        RecipeSetup.register();

        modBus.addListener(RegisterPayloadHandlersEvent.class, event -> event.registrar("1").playToClient(ClientboundSyncPotionHandler.TYPE, ClientboundSyncPotionHandler.STREAM_CODEC, ClientboundSyncPotionHandler::handle));
        modBus.addListener(RegisterTooltipEvent.class, event -> event.registerAfter(TooltipPosition.COMPONENT, this::appendPotionEffects));
        modBus.addListener(RegisterClientExtensionsEvent.class, event -> event.registerFluidType(new PotionFluidTypeClientExtension(), POTION_FLUID_TYPE.value()));
    }

    private void appendPotionEffects(ItemStack stack, Item.TooltipContext context, Consumer<Component> adder, @Nullable Player player, TooltipFlag flag) {
        if(!stack.has(DataComponents.FOOD) || stack.has(RecipeSetup.HIDE_EFFECTS_COMPONENT))
            return;

        var potion = stack.get(DataComponents.POTION_CONTENTS);

        if(potion != null)
            potion.addToTooltip(context, adder, flag, stack);
    }

    public static ResourceLocation identifier(String identifier) {
        return REGISTREE.registryName(identifier);
    }

    public static String id(String identifier) {
        return ID + ResourceLocation.NAMESPACE_SEPARATOR + identifier;
    }

    public static boolean isValidFood(ItemStack stack) {
        return stack.has(DataComponents.FOOD) && !stack.is(ITEM_BLACKLIST);
    }

    public static boolean isValidPotion(Holder<Potion> potion) {
        return !potion.is(POTION_BLACKLIST);
    }

    public static boolean isValidPotion(PotionContents contents) {
        return contents != PotionContents.EMPTY && contents.potion().map(InfusedFoods::isValidPotion).orElse(false);
    }

    public static Stream<ItemStack> createInfusedItems(ItemStack food, CreativeModeTab.ItemDisplayParameters parameters) {
        return parameters.holders()
                .lookup(Registries.POTION)
                .stream()
                .map(potions -> potions.filterFeatures(parameters.enabledFeatures()))
                .flatMap(HolderLookup::listElements)
                .filter(InfusedFoods::isValidPotion)
                .map(potion -> {
                    var result = food.copy(); // copy to not mutate the base stack
                    result.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
                    return result;
                })
                .filter(infused -> infused.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).hasEffects());
    }
}
