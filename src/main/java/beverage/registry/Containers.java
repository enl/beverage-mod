package beverage.registry;

import beverage.BeverageMod;
import beverage.yeast.fermenter.YeastFermenterContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Containers
{
    private Containers() {}

    public static void init(IEventBus eventBus)
    {
        CONTAINERS.register(eventBus);
    }

    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, BeverageMod.MOD_ID);

    public static RegistryObject<ContainerType<YeastFermenterContainer>> YEAST_FERMENTER = CONTAINERS.register(
        "yeast_fermenter",
        () -> IForgeContainerType.create(YeastFermenterContainer.factory)
    );
}
