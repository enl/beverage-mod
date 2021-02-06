package beverage.setup;

import beverage.BeverageMod;
import beverage.client.gui.YeastFermenterScreen;
import beverage.registry.Containers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value= Dist.CLIENT, modid = BeverageMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Client
{
    public static void init(FMLClientSetupEvent event)
    {
        ScreenManager.registerFactory(Containers.YEAST_FERMENTER.get(), YeastFermenterScreen::new);
    }
}
