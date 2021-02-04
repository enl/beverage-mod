package beverage;

import beverage.registry.Blocks;
import beverage.registry.Items;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BeverageMod.MOD_ID)
public class BeverageMod {
    public static final String MOD_ID = "beverage";
    public static final ItemGroup ITEM_GROUP = new BeverageItemGroup(
        MOD_ID,
        () -> new ItemStack(Items.wheatMalt.get())
    );

    public BeverageMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Items.init(eventBus);
        Blocks.init(eventBus);
    }

    public static ResourceLocation getId(String name) {
        return new ResourceLocation(MOD_ID, name);
    }
}
