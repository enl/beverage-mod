package beverage.utils;

import beverage.BeverageMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;

public final class Utils
{
    public static ResourceLocation getResource(String key)
    {
        return new ResourceLocation(BeverageMod.MOD_ID, key);
    }

    public static ResourceLocation getVanillaResource(String key)
    {
        return new ResourceLocation("minecraft", key);
    }

    public static ITextComponent getTranslation(String key, Object ...args)
    {
        return new TranslationTextComponent(
            BeverageMod.MOD_ID + "." + key,
            args
        );
    }

    public static void log(String message)
    {
        LogManager.getLogger(BeverageMod.MOD_ID).debug(message);
    }
}
