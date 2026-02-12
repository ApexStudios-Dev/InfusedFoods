package dev.apexstudios.infusedfoods.common.util;

import dev.apexstudios.infusedfoods.common.InfusedFoods;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;

public interface InfusionTags {
    TagKey<Item> ITEM_BLACKLIST = InfusedFoods.ITEMS.tag("infusion_blacklist");
    TagKey<Potion> POTION_BLACKLIST = InfusedFoods.POTIONS.tag("infusion_blacklist");

    TagKey<Item> CLEANSING_AGENT = InfusedFoods.ITEMS.tag("cleansing_agent");
}
