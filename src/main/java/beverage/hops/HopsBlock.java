package beverage.hops;

import beverage.registry.Blocks;
import beverage.registry.Items;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class HopsBlock extends CropsBlock
{
    public HopsBlock(Properties props)
    {
        super(props);
    }

    @Override
    protected IItemProvider getSeedsItem()
    {
        return Items.HOPS_SEEDS.get();
    }

    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos)
    {
        return Blocks.HOPS_PLANT.get().getDefaultState();
    }
}
