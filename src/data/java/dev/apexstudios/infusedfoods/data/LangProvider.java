package dev.apexstudios.infusedfoods.data;

import dev.apexstudios.apexcore.api.data.LanguageProvider;
import dev.apexstudios.infusedfoods.api.InfusedFoods;
import dev.apexstudios.registree.api.util.CreativeModeTabRegistrations;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import org.apache.commons.lang3.text.WordUtils;

final class LangProvider extends LanguageProvider {
    LangProvider(PackOutput output) {
        super(output, InfusedFoods.ID);
    }

    @Override
    protected void addTranslations() {
        addTranslatableComponent(InfusedFoodsDataEntryPoint.PACK_DESC, "InfusedFoods resources");
        addBlock(InfusedFoods.POTION_CAULDRON_BLOCK, "Potion Cauldron");
        addBlock(InfusedFoods.POTION_FLUID_BLOCK, "Potion");
        addFluidType(InfusedFoods.POTION_FLUID_TYPE, "Potion");
        addItem(InfusedFoods.POTION_BUCKET, "Potion Bucket");
        add(InfusedFoods.CLEANSING_AGENTS, "Cleansing Agents");
        add(InfusedFoods.INFUSION_BLACKLIST, "Infusion Blacklist");
        add(CreativeModeTabRegistrations.defaultTranslationKey(InfusedFoods.POTION_BUCKET_TAB), "Potion Buckets");

        var prefix = InfusedFoods.POTION_BUCKET.value().getDescriptionId();
        addEffect(prefix, Potions.AWKWARD, "Awkward Potion Bucket");
        addEffect(prefix, "empty", "Uncraftable Potion Bucket");
        addEffect(prefix, Potions.FIRE_RESISTANCE, LangProvider::bucketOf);
        addEffect(prefix, Potions.HARMING, LangProvider::bucketOf);
        addEffect(prefix, Potions.HEALING, LangProvider::bucketOf);
        addEffect(prefix, Potions.INFESTED, LangProvider::bucketOf);
        addEffect(prefix, Potions.INVISIBILITY, LangProvider::bucketOf);
        addEffect(prefix, Potions.LEAPING, LangProvider::bucketOf);
        // addEffect(prefix, Potions.LEVITATION, LangProvider::bucketOf);
        addEffect(prefix, "levitation", LangProvider::bucketOf);
        addEffect(prefix, Potions.LUCK, LangProvider::bucketOf);
        addEffect(prefix, Potions.MUNDANE, "Mundane Potion Bucket");
        addEffect(prefix, Potions.NIGHT_VISION, LangProvider::bucketOf);
        addEffect(prefix, Potions.OOZING, LangProvider::bucketOf);
        addEffect(prefix, Potions.POISON, LangProvider::bucketOf);
        addEffect(prefix, Potions.REGENERATION, LangProvider::bucketOf);
        addEffect(prefix, Potions.SLOW_FALLING, LangProvider::bucketOf);
        addEffect(prefix, Potions.SLOWNESS, LangProvider::bucketOf);
        addEffect(prefix, Potions.STRENGTH, LangProvider::bucketOf);
        addEffect(prefix, Potions.SWIFTNESS, LangProvider::bucketOf);
        addEffect(prefix, Potions.THICK, "Thick Potion Bucket");
        addEffect(prefix, Potions.TURTLE_MASTER, LangProvider::bucketOf);
        addEffect(prefix, Potions.WATER, "Water Bucket");
        addEffect(prefix, Potions.WATER_BREATHING, LangProvider::bucketOf);
        addEffect(prefix, Potions.WEAKNESS, LangProvider::bucketOf);
        addEffect(prefix, Potions.WEAVING, LangProvider::bucketOf);
        addEffect(prefix, Potions.WIND_CHARGED, LangProvider::bucketOf);
    }

    private void addEffect(String prefix, Holder<Potion> holder, Function<String, String> nameModifier) {
        addEffect(
                prefix,
                holder.value().name(),
                nameModifier
        );
    }

    private void addEffect(String prefix, Holder<Potion> holder, String value) {
        addEffect(prefix, holder, _ -> value);
    }

    private void addEffect(String prefix, String name, Function<String, String> nameModifier) {
        addEffect(
                prefix,
                name,
                nameModifier.apply(name.replace('_', ' '))
        );
    }

    private void addEffect(String prefix, String name, String value) {
        add(prefix + ".effect." + name, value);
    }

    private static String bucketOf(String name) {
        return "Bucket of " + WordUtils.capitalize(name);
    }
}
