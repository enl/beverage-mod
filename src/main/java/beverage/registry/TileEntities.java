package beverage.registry;

import beverage.BeverageMod;
import beverage.yeast.fermenter.YeastFermenterTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntities
{
    public static void init(IEventBus eventBus)
    {
        TILE_ENTITIES.register(eventBus);
    }

    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BeverageMod.MOD_ID);

    public static final RegistryObject<TileEntityType<?>> YEAST_FERMENTER = TILE_ENTITIES.register(
        "yeast_fermenter",
        () -> TileEntityType.Builder.create(YeastFermenterTileEntity::new, Blocks.YEAST_FERMENTER.get()).build(null)
    );
}
