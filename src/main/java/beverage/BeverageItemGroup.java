package beverage;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
public class BeverageItemGroup extends ItemGroup
{
    private final Supplier<ItemStack> iconSupplier;

    public BeverageItemGroup(String name, Supplier<ItemStack> iconSupplier)
    {
        super(name);
        this.iconSupplier = iconSupplier;
    }

    @Override
    public ItemStack createIcon()
    {
        return iconSupplier.get();
    }
}