package beverage.registry;

import beverage.BeverageMod;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Items
{
    public static void init(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BeverageMod.MOD_ID);

    public static final RegistryObject<Item> WHEAT_MALT = ITEMS.register("wheat_malt", () -> new Item(
        (new Item.Properties()).group(BeverageMod.ITEM_GROUP)
    ));

    public static final RegistryObject<Item> WILD_YEAST = ITEMS.register("wild_yeast", () -> new Item(
        (new Item.Properties()).group(BeverageMod.ITEM_GROUP)
    ));

    public static final RegistryObject<Item> YEAST = ITEMS.register("yeast", () -> new Item(
        (new Item.Properties()).group(BeverageMod.ITEM_GROUP)
    ));

    public static final RegistryObject<Item> YEAST_FERMENTER = ITEMS.register("yeast_fermenter", () -> new BlockItem(
        Blocks.YEAST_FERMENTER.get(),
        (new Item.Properties()).group(BeverageMod.ITEM_GROUP)
    ));

    public static boolean isYeast(Item item)
    {
        return item.equals(WILD_YEAST.get()) || item.equals(YEAST.get());
    }
}
