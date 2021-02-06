package beverage.client.gui;

import beverage.utils.Utils;
import beverage.yeast.fermenter.YeastFermenterContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class YeastFermenterScreen extends ContainerScreen<YeastFermenterContainer>
{
    private static final ResourceLocation BACKGROUND = Utils.getResource("textures/gui/yeast_fermenter.png");

    public YeastFermenterScreen(YeastFermenterContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        // Render UI background
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.minecraft == null)
            return;

        this.minecraft.getTextureManager().bindTexture(BACKGROUND);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.blit(matrixStack, x, y, 0, 0, this.xSize, this.ySize);
    }
}
