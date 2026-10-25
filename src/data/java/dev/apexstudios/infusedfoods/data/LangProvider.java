package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.apexcore.api.data.LanguageProvider;
import dev.apexstudios.infusedfoods.api.InfusedFoods;
import net.minecraft.data.PackOutput;

final class LangProvider extends LanguageProvider {
    LangProvider(PackOutput output) {
        super(output, InfusedFoods.ID);
    }

    @Override
    protected void addTranslations() {
        addTranslatableComponent(InfusedFoodsDataEntryPoint.PACK_DESC, "InfusedFoods resources");
        addBlock(InfusedFoods.INFUSION_CAULDRON, "Infusion Cauldron");
        add(InfusedFoods.CLEANSING_AGENTS, "Cleansing Agents");
        add(InfusedFoods.INFUSION_BLACKLIST, "Infusion Blacklist");
    }
}
