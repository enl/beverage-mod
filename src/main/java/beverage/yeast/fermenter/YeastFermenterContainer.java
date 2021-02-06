package beverage.yeast.fermenter;

import beverage.registry.Containers;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.IContainerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class YeastFermenterContainer extends Container
{
    public static final IContainerFactory<YeastFermenterContainer> factory = (id, inventory, packetBuffer) -> new YeastFermenterContainer(id, inventory);

    public static final int[] NUTRIENT_SLOTS = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    public static final int[] OUTPUT_SLOTS = {10, 11, 12};
    public static final int YEAST_SLOT_IDX = 0;

    private final IInventory inventory;

    public YeastFermenterContainer(int id, PlayerInventory playerInventory)
    {
        this(id, playerInventory, new Inventory(13));
    }

    public YeastFermenterContainer(int id, PlayerInventory playerInventory, IInventory inventory)
    {
        super(Containers.YEAST_FERMENTER.get(), id);
        this.inventory = inventory;
        int idx = 0;

        // yeast slot on the left
        this.addSlot(new YeastSlot(inventory, idx++, 8, 52));

        // nutrient slots
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new NutrientSlot(inventory, idx++, 44 + 18 * j, 16 + 18 * i));
            }
        }

        // result slots
        for (int i = 0; i < 3; ++i) {
            this.addSlot(new ResultSlot(inventory, idx++, 124, 16 + 18 * i));
        }

        // player inventory
        for(int i1 = 0; i1 < 3; ++i1) {
            for(int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 84 + i1 * 18));
            }
        }

        for(int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(playerInventory, j1, 8 + j1 * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity player)
    {
        return inventory.isUsableByPlayer(player);
    }

    @Override
    public void onContainerClosed(PlayerEntity player)
    {
        super.onContainerClosed(player);
        this.inventory.closeInventory(player);
    }

    //stack transfer
    //TODO: extract to separate class

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index)
    {
        if (index < inventory.getSizeInventory()) {
            return tryToTransferToPlayer(player, index);
        }

        return tryToTransferFromPlayer(player, index);
    }

    private ItemStack tryToTransferToPlayer(PlayerEntity player, int index)
    {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot == null || !slot.getHasStack()) {
            return itemStack;
        }

        ItemStack itemStack1 = slot.getStack();
        itemStack = itemStack1.copy();

        if (!mergeItemStack(itemStack1, inventory.getSizeInventory(), inventorySlots.size(), true)) {
            return ItemStack.EMPTY;
        }

        if (itemStack1.isEmpty()) {
            slot.putStack(ItemStack.EMPTY);
        } else {
            slot.onSlotChanged();
        }

        if (itemStack1.getCount() == itemStack.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, itemStack1);
        return itemStack;
    }

    private ItemStack tryToTransferFromPlayer(PlayerEntity player, int index)
    {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot == null || !slot.getHasStack()) {
            return itemStack;
        }

        ItemStack itemStack1 = slot.getStack();
        itemStack = itemStack1.copy();

        ValidIndexesRange indexes = findValidIndexesRange(inventory, itemStack);

        if (indexes.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (!mergeItemStack(itemStack1, indexes.getMin(), indexes.getMax(), false)) {
            return ItemStack.EMPTY;
        }

        if (itemStack1.isEmpty()) {
            slot.putStack(ItemStack.EMPTY);
        } else {
            slot.onSlotChanged();
        }

        if (itemStack1.getCount() == itemStack.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, itemStack1);
        return itemStack;
    }

    private ValidIndexesRange findValidIndexesRange(IInventory inventory, ItemStack itemStack)
    {
        int min = 0;
        int max = 0;
        int size = inventory.getSizeInventory();

        for (int i = 0; i < size; ++i) {
            Slot slot = this.inventorySlots.get(i);
            if (slot.isItemValid(itemStack)) {
                min = min > 0 ? min : i;
                max = i + 1;
            } else if (max > 0) {
                break;
            }
        }

        return new ValidIndexesRange(min, max);
    }

    private static class ValidIndexesRange
    {
        private final int min;
        private final int max;
        private final boolean empty;

        ValidIndexesRange(int min, int max)
        {
            this.min = min;
            this.max = max;
            empty = min == 0 && max == 0;
        }

        public boolean isEmpty()
        {
            return empty;
        }

        public int getMin()
        {
            return min;
        }

        public int getMax()
        {
            return max;
        }
    }

    // end region stack transfer
}
