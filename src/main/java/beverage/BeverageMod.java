package beverage;

import beverage.registry.Blocks;
import beverage.registry.Containers;
import beverage.registry.Items;
import beverage.registry.TileEntities;
import beverage.setup.Client;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BeverageMod.MOD_ID)
public class BeverageMod
{
    public static final String MOD_ID = "beverage";
    public static final ItemGroup ITEM_GROUP = new BeverageItemGroup(
        MOD_ID,
        () -> new ItemStack(Items.WHEAT_MALT.get())
    );

    public BeverageMod()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(Client::init);

        Items.init(eventBus);
        Blocks.init(eventBus);
        TileEntities.init(eventBus);
        Containers.init(eventBus);
    }
}
