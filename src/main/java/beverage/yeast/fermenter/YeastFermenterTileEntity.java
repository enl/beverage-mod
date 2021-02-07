package beverage.yeast.fermenter;

import beverage.registry.Items;
import beverage.registry.TileEntities;
import beverage.utils.Utils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class YeastFermenterTileEntity extends LockableLootTileEntity implements INameable, ISidedInventory
{
    private NonNullList<ItemStack> items = NonNullList.withSize(13, ItemStack.EMPTY);

    public YeastFermenterTileEntity()
    {
        this(TileEntities.YEAST_FERMENTER.get());
    }

    public YeastFermenterTileEntity(TileEntityType<?> tileEntityType)
    {
        super(tileEntityType);
    }

    @Override
    protected NonNullList<ItemStack> getItems()
    {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn)
    {
        items = itemsIn;
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return Utils.getTranslation("container.yeast_fermenter");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory)
    {
        return new YeastFermenterContainer(id, playerInventory, this);
    }

    @Override
    public int getSizeInventory()
    {
        return this.items.size();
    }

    //Region yeast growth handler
    public void growYeast(Random rnd)
    {
        growthHandler.grow(rnd);
    }

    private final YeastGrowthHandler growthHandler = new YeastGrowthHandler();

    private class YeastGrowthHandler
    {
        private final int NUTRIENT_AMOUNT = 3;
        private final int OUTPUT_AMOUNT = 1;

        public void grow(Random rnd)
        {
            if (!hasParentYeast()) {
                return;
            }

            if (!hasNutrients()) {
                return;
            }

            if (!hasOutputSpace()) {
                return;
            }

            decreaseNutrients();
            increaseOutput();
            markDirty();
        }

        private boolean hasParentYeast()
        {
            ItemStack stack = getStackInSlot(YeastFermenterContainer.YEAST_SLOT_IDX);

            return !stack.isEmpty();
        }

        private boolean hasNutrients()
        {
            int totals = 0;

            for (int index: YeastFermenterContainer.NUTRIENT_SLOTS) {
                totals += getStackInSlot(index).getCount();
            }

            return totals >= NUTRIENT_AMOUNT;
        }

        private boolean hasOutputSpace()
        {
            for (int index: YeastFermenterContainer.OUTPUT_SLOTS) {
                if (getStackInSlot(index).getCount() < getInventoryStackLimit()) {
                    return true;
                }
            }

            return false;
        }

        private void decreaseNutrients()
        {
            int leftovers = OUTPUT_AMOUNT;
            for (int index: YeastFermenterContainer.NUTRIENT_SLOTS) {
                int existingCount = getStackInSlot(index).getCount();
                int decreasedCount = existingCount - leftovers > 0 ? leftovers : existingCount;

                decrStackSize(index, decreasedCount);
                leftovers -= decreasedCount;

                if (leftovers == 0) {
                    return;
                }
            }
        }

        private void increaseOutput()
        {
            int leftovers = OUTPUT_AMOUNT;
            for (int index: YeastFermenterContainer.OUTPUT_SLOTS) {
                ItemStack stack = getStackInSlot(index);
                if (stack.isEmpty()) {
                    setInventorySlotContents(index, new ItemStack(Items.YEAST.get(), OUTPUT_AMOUNT));
                    return;
                }

                int increasedAmount = Math.min(leftovers, getInventoryStackLimit() - stack.getCount());
                stack.setCount(stack.getCount() + increasedAmount);
                leftovers -= increasedAmount;

                if (leftovers == 0) {
                    return;
                }
            }
        }
    }


    //Region: World save
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        this.loadFromNbt(nbt);
    }

    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        return this.saveToNbt(compound);
    }

    public void loadFromNbt(CompoundNBT compound)
    {
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(compound) && compound.contains("Items", 9)) {
            ItemStackHelper.loadAllItems(compound, this.items);
        }
    }

    public CompoundNBT saveToNbt(CompoundNBT compound)
    {
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.items, false);
        }

        return compound;
    }
    //Endregion

    //Region: capabilities
    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    protected void invalidateCaps()
    {
        super.invalidateCaps();
        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side)
    {
        if (!this.removed && side != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.UP) {
                return handlers[0].cast();
            }

            if (side == Direction.DOWN) {
                return handlers[1].cast();
            }

            else {
                return handlers[2].cast();
            }
        }

        return super.getCapability(capability, side);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        if (side == Direction.DOWN) {
            return YeastFermenterContainer.OUTPUT_SLOTS;
        }

        return YeastFermenterContainer.NUTRIENT_SLOTS;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction)
    {
        if (index == YeastFermenterContainer.YEAST_SLOT_IDX) {
            return Items.isYeast(itemStackIn.getItem());
        }

        if (Arrays.stream(YeastFermenterContainer.NUTRIENT_SLOTS).anyMatch(i -> i == index)) {
            return itemStackIn.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("sugar"));
        }

        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction)
    {
        return Arrays.stream(YeastFermenterContainer.OUTPUT_SLOTS).anyMatch(i -> i == index);
    }
}
