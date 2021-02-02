package beverage.yeast;

import beverage.BeverageMod;
import net.minecraft.block.Block;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

/**
 * It seems there must be better way of doing this, but I was not able to make proper loot modifier.
 * Might be the case that mushrooms are _special_ and do not have loot tables at all.
 *
 * TODO: https://mcforge.readthedocs.io/en/latest/items/globallootmodifiers/
 */
public class MushroomHarvestHandler {
    private static final ResourceLocation item = BeverageMod.getId("wild_yeast");
    private static final Double probability = 0.1;


    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event)
    {
        IWorld world = event.getWorld();
        if (world.isRemote()) {
            return;
        }

        Block brokenBlock = event.getState().getBlock();
        Block BrownMushroom = ForgeRegistries.BLOCKS.getValue(
            new ResourceLocation("minecraft:brown_mushroom")
        );

        if (BrownMushroom == null || !brokenBlock.matchesBlock(BrownMushroom)) {
            return;
        }

        PlayerEntity player = event.getPlayer();
        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        if (!(player.getHeldItemMainhand().getItem() instanceof HoeItem)) {
            return;
        }

        ItemStack stack = new ItemStack(
            ForgeRegistries.ITEMS.getValue(item),
            randomizeAmount(world.getRandom())
        );

        if (stack.isEmpty()) {
            return;
        }

        BlockPos pos = event.getPos();
        dropStackInWorld((World) world, pos, stack);
    }

    /**
     * Heavily customized Vanilla code:
     * @see Block#spawnAsEntity(World, BlockPos, ItemStack)
     */
    private static void dropStackInWorld(World world, BlockPos pos, ItemStack stack)
    {
        ItemEntity itementity = new ItemEntity(
            world,
            addRandomOffset(world.getRandom(), pos.getX()),
            addRandomOffset(world.getRandom(), pos.getY()),
            addRandomOffset(world.getRandom(), pos.getZ()),
            stack
        );
        itementity.setDefaultPickupDelay();
        world.addEntity(itementity);
    }

    private static int randomizeAmount(Random rnd)
    {
        return rnd.nextFloat() > (1 - probability) ? 1 : 0;
    }

    private static double addRandomOffset(Random rnd, int coord)
    {
        return rnd.nextFloat() * 0.5F + 0.25D + coord;
    }
}
