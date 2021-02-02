package beverage;

import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class BeverageItems {
    private BeverageItems() {}

    public static Item wheatMalt;
    public static Item wildYeast;

    @SubscribeEvent
    public static void registerAllItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();

        wheatMalt = register(registry, "wheat_malt", new Item((new Properties()).group(BeverageMod.ITEM_GROUP)));
        wildYeast = register(registry, "wild_yeast", new Item((new Properties()).group(BeverageMod.ITEM_GROUP)));
    }

    private static <T extends Item> T register(IForgeRegistry<Item> registry, String name, T item) {
        item.setRegistryName(BeverageMod.getId(name));
        registry.register(item);

        return item;
    }
}
