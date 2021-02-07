package beverage.registry;

import beverage.BeverageMod;
import beverage.hops.HopsBlock;
import beverage.utils.Utils;
import beverage.yeast.YeastFermenterBlock;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Blocks
{
    private Blocks() {}

    public static void init(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BeverageMod.MOD_ID);

    public static final RegistryObject<Block> YEAST_FERMENTER = BLOCKS.register("yeast_fermenter", YeastFermenterBlock::new);

    public static final RegistryObject<Block> HOPS_PLANT = BLOCKS.register("hops_plant", () -> new HopsBlock(
        Properties.from(ForgeRegistries.BLOCKS.getValue(Utils.getVanillaResource("wheat")))
    ));
}
