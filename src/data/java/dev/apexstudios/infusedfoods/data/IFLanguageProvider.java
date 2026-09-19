package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.infusedfoods.common.InfusedFoods;
import dev.apexstudios.infusedfoods.common.util.InfusionEntries;
import dev.apexstudios.infusedfoods.common.util.InfusionTags;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

final class IFLanguageProvider extends LanguageProvider {
    IFLanguageProvider(PackOutput output) {
        super(output, InfusedFoods.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(InfusionTags.ITEM_BLACKLIST, "Infusion Blacklist (Items)");
        add(InfusionTags.POTION_BLACKLIST, "Infusion Blacklist (Potions)");
        addBlock(InfusionEntries.CAULDRON_BLOCK, "Potion Cauldron");
        addFluidType(InfusionEntries.POTION_FLUID_TYPE, "Potion");
        add(InfusionTags.CLEANSING_AGENT, "Cleansing Agents");
    }
}
