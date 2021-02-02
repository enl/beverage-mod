package beverage;

import beverage.yeast.MushroomHarvestHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nonnull;

@Mod(BeverageMod.MOD_ID)
public class BeverageMod {
    public static final String MOD_ID = "beverage";
    public static final ItemGroup ITEM_GROUP = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(BeverageItems.wheatMalt);
        }
    };


    public BeverageMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.register(BeverageItems.class);
        MinecraftForge.EVENT_BUS.register(MushroomHarvestHandler.class);
    }

    @Nonnull
    public static ResourceLocation getId(String name) {
        return new ResourceLocation(MOD_ID, name);
    }
}
