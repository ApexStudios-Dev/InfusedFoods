package dev.apexstudios.infusedfoods.util;

import dev.apexstudios.infusedfoods.InfusedFoods;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;

public interface InfusionTags {
    TagKey<Item> ITEM_BLACKLIST = InfusedFoods.REGISTREE.tag(Registries.ITEM, "infusion_blacklist");
    TagKey<Potion> POTION_BLACKLIST = InfusedFoods.REGISTREE.tag(Registries.POTION, "infusion_blacklist");

    TagKey<Item> CLEANSING_AGENT = InfusedFoods.REGISTREE.tag(Registries.ITEM, "cleansing_agent");
}
