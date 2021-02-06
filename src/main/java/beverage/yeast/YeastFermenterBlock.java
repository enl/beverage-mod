package beverage.yeast;

import beverage.BeverageMod;
import beverage.registry.TileEntities;
import beverage.yeast.fermenter.YeastFermenterTileEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.INameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class YeastFermenterBlock extends Block
{
    private static final Properties initProperties = Block.Properties.create(Material.IRON)
        .setRequiresTool()
        .harvestTool(ToolType.PICKAXE)
        .hardnessAndResistance(2.0F, 6.0F)
        .harvestLevel(1)
        .tickRandomly()
        .sound(SoundType.LANTERN)
    ;


    public YeastFermenterBlock()
    {
        super(initProperties);
    }

    @Override
    public boolean canSpawnInBlock()
    {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return TileEntities.yeastFermenter.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (world.isRemote) {
            return ActionResultType.SUCCESS;
        }

        interactWith(world, pos, player);

        return ActionResultType.CONSUME;
    }

    private void interactWith(World world, BlockPos pos, PlayerEntity player)
    {
        if (world.isRemote) {
            return;
        }

        TileEntity tileEntity = world.getTileEntity(pos);

        player.openContainer((YeastFermenterTileEntity) tileEntity);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof YeastFermenterTileEntity) {
            ((YeastFermenterTileEntity) tileEntity).growYeast(random);
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.isIn(newState.getBlock())) {
            return;
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof YeastFermenterTileEntity) {
            YeastFermenterTileEntity fermenterEntity = (YeastFermenterTileEntity) tileEntity;
            InventoryHelper.dropInventoryItems(worldIn, pos, fermenterEntity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if (!stack.hasDisplayName()) {
            return;
        }

        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof INameable) {
            ((YeastFermenterTileEntity) tileEntity).setCustomName(stack.getDisplayName());
        }
    }
}
