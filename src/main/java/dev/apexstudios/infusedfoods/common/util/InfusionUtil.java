package dev.apexstudios.infusedfoods.common.util;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

public interface InfusionUtil {
    static boolean isValidFood(ItemStack stack) {
        return stack.has(DataComponents.FOOD) && !stack.is(InfusionTags.ITEM_BLACKLIST);
    }

    static boolean isValidPotion(Holder<Potion> potion) {
        return !potion.is(InfusionTags.POTION_BLACKLIST);
    }

    static boolean isValidPotion(PotionContents contents) {
        return contents != PotionContents.EMPTY && contents.potion().map(InfusionUtil::isValidPotion).orElse(false);
    }
}
