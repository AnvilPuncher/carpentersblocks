package carpentersblocks.renderer.helper;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import carpentersblocks.util.flowerpot.FlowerPotHandler;
import carpentersblocks.util.registry.FeatureRegistry;

public class RenderHelperFlowerPot extends RenderHelper {

	private static void setPlantColor(Block block, int x, int y, int z)
	{
		Tessellator tessellator = Tessellator.instance;
		LightingHelper lightingHelper = LightingHelper.instance;

		float[] rgb = lightingHelper.applyAnaglyphFilter(lightingHelper.getBlockRGB(block, x, y, z));

		tessellator.setColorOpaque_F(rgb[0], rgb[1], rgb[2]);

		if (FeatureRegistry.enablePlantColorOverride) {
			if (block.getBlockColor() != 16777215) {
				tessellator.setColorOpaque_F(0.45F, 0.80F, 0.30F);
			}
		}
	}

	/**
	 * Renders any block requiring crossed squares such as reeds, flowers, and mushrooms
	 */
	public static boolean renderPlantCrossedSquares(RenderBlocks renderBlocks, Block block, int metadata, int x, int y, int z, float scale, boolean flipped)
	{
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

		Icon icon = FlowerPotHandler.getPlantIcon(new ItemStack(block, 1, metadata));

		setPlantColor(block, x, y, z);

		double uMin = icon.getMinU();
		double vMin = icon.getMinV();
		double uMax = icon.getMaxU();
		double vMax = icon.getMaxV();
		double rotation = 0.45D * scale;
		double xMin = x + 0.5D - rotation;
		double xMax = x + 0.5D + rotation;
		double zMin = z + 0.5D - rotation;
		double zMax = z + 0.5D + rotation;

		if (flipped)
		{
			double temp = vMin;
			vMin = vMax;
			vMax = temp;
		}

		tessellator.addVertexWithUV(xMin, y + (double) scale, zMin, uMin, vMin);
		tessellator.addVertexWithUV(xMin, y + 0.0D, zMin, uMin, vMax);
		tessellator.addVertexWithUV(xMax, y + 0.0D, zMax, uMax, vMax);
		tessellator.addVertexWithUV(xMax, y + (double) scale, zMax, uMax, vMin);
		tessellator.addVertexWithUV(xMax, y + (double) scale, zMax, uMin, vMin);
		tessellator.addVertexWithUV(xMax, y + 0.0D, zMax, uMin, vMax);
		tessellator.addVertexWithUV(xMin, y + 0.0D, zMin, uMax, vMax);
		tessellator.addVertexWithUV(xMin, y + (double) scale, zMin, uMax, vMin);
		tessellator.addVertexWithUV(xMin, y + (double) scale, zMax, uMin, vMin);
		tessellator.addVertexWithUV(xMin, y + 0.0D, zMax, uMin, vMax);
		tessellator.addVertexWithUV(xMax, y + 0.0D, zMin, uMax, vMax);
		tessellator.addVertexWithUV(xMax, y + (double) scale, zMin, uMax, vMin);
		tessellator.addVertexWithUV(xMax, y + (double) scale, zMin, uMin, vMin);
		tessellator.addVertexWithUV(xMax, y + 0.0D, zMin, uMin, vMax);
		tessellator.addVertexWithUV(xMin, y + 0.0D, zMax, uMax, vMax);
		tessellator.addVertexWithUV(xMin, y + (double) scale, zMax, uMax, vMin);

		return true;
	}

	/**
	 * Renders thin-profile crossed squares (reeds, vine-type blocks).
	 */
	public static void renderPlantThinCrossedSquares(RenderBlocks renderBlocks, Block block, int metadata, int x, int y, int z, boolean flipped)
	{
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

		Icon icon = FlowerPotHandler.getPlantIcon(new ItemStack(block, 1, metadata));

		setPlantColor(block, x, y, z);

		double uMin = icon.getInterpolatedU(0.0D);
		double uMax = icon.getInterpolatedU(4.0D);
		double vMin = icon.getInterpolatedV(16.0D);
		double vMax = icon.getInterpolatedV(0.0D);
		double rotatedScaleFactor = 0.45D * 0.375F;
		double xMin = x + 0.5D - rotatedScaleFactor;
		double xMax = x + 0.5D + rotatedScaleFactor;
		double zMin = z + 0.5D - rotatedScaleFactor;
		double zMax = z + 0.5D + rotatedScaleFactor;

		if (flipped)
		{
			double temp = vMin;
			vMin = vMax;
			vMax = temp;
		}

		tessellator.addVertexWithUV(xMin, y + 0.75D, zMin, uMin, vMax);
		tessellator.addVertexWithUV(xMin, y + 0.0D, zMin, uMin, vMin);
		tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMax, vMin);
		tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMax, vMax);

		tessellator.addVertexWithUV(xMax, y + 0.75D, zMin, uMin, vMax);
		tessellator.addVertexWithUV(xMax, y + 0.0D, zMin, uMin, vMin);
		tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMax, vMin);
		tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMax, vMax);

		tessellator.addVertexWithUV(xMax, y + 0.75D, zMax, uMin, vMax);
		tessellator.addVertexWithUV(xMax, y + 0.0D, zMax, uMin, vMin);
		tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMax, vMin);
		tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMax, vMax);

		tessellator.addVertexWithUV(xMin, y + 0.75D, zMax, uMin, vMax);
		tessellator.addVertexWithUV(xMin, y + 0.0D, zMax, uMin, vMin);
		tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMax, vMin);
		tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMax, vMax);

		uMin = icon.getInterpolatedU(12.0D);
		uMax = icon.getInterpolatedU(16.0D);

		tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMin, vMax);
		tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMin, vMin);
		tessellator.addVertexWithUV(xMin, y + 0.0D, zMin, uMax, vMin);
		tessellator.addVertexWithUV(xMin, y + 0.75D, zMin, uMax, vMax);

		tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMin, vMax);
		tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMin, vMin);
		tessellator.addVertexWithUV(xMax, y + 0.0D, zMin, uMax, vMin);
		tessellator.addVertexWithUV(xMax, y + 0.75D, zMin, uMax, vMax);

		tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMin, vMax);
		tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMin, vMin);
		tessellator.addVertexWithUV(xMax, y + 0.0D, zMax, uMax, vMin);
		tessellator.addVertexWithUV(xMax, y + 0.75D, zMax, uMax, vMax);

		tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMin, vMax);
		tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMin, vMin);
		tessellator.addVertexWithUV(xMin, y + 0.0D, zMax, uMax, vMin);
		tessellator.addVertexWithUV(xMin, y + 0.75D, zMax, uMax, vMax);
	}

	/**
	 * Renders vanilla cactus using "prickly" method.
	 */
	public static void drawPlantCactus(RenderBlocks renderBlocks, Block block, int x, int y, int z)
	{
		Icon icon = block.getBlockTextureFromSide(2);

		LightingHelper lightingHelper = LightingHelper.instance;

		double uMinL = icon.getInterpolatedU(0.0D);
		double uMaxL = icon.getInterpolatedU(3.0D);
		double uMinR = icon.getInterpolatedU(13.0D);
		double uMaxR = icon.getInterpolatedU(16.0D);
		double vMin = icon.getInterpolatedV(16.0D);
		double vMax = icon.getInterpolatedV(0.0D);

		renderBlocks.enableAO = true;

		renderBlocks.setRenderBounds(0.375D, 0.25D, 0.375D, 0.6875D, 1.0D, 0.6875D);

		/* NORTH FACE */

		lightingHelper.setLightness(0.8F).setLightingZNeg(block, x, y, z);
		lightingHelper.colorSide(block, x, y, z, 2, icon);

		// LEFT
		setupVertex(renderBlocks, x + 0.6875F, y + 0.75F, z + 0.375F, uMinL, vMax, TOP_LEFT);
		setupVertex(renderBlocks, x + 0.6875F, y, z + 0.375F, uMinL, vMin, BOTTOM_LEFT);
		setupVertex(renderBlocks, x + 0.5F, y, z + 0.375F, uMaxL, vMin, BOTTOM_CENTER);
		setupVertex(renderBlocks, x + 0.5F, y + 0.75F, z + 0.375F, uMaxL, vMax, TOP_CENTER);

		// RIGHT
		setupVertex(renderBlocks, x + 0.5F, y + 0.75F, z + 0.375F, uMinR, vMax, TOP_CENTER);
		setupVertex(renderBlocks, x + 0.5F, y, z + 0.375F, uMinR, vMin, BOTTOM_CENTER);
		setupVertex(renderBlocks, x + 0.3125F, y, z + 0.375F, uMaxR, vMin, BOTTOM_RIGHT);
		setupVertex(renderBlocks, x + 0.3125F, y + 0.75F, z + 0.375F, uMaxR, vMax, TOP_RIGHT);

		/* SOUTH FACE */

		lightingHelper.setLightness(0.8F).setLightingZPos(block, x, y, z);
		lightingHelper.colorSide(block, x, y, z, 3, icon);

		// LEFT
		setupVertex(renderBlocks, x + 0.3125F, y + 0.75F, z + 0.625F, uMinL, vMax, TOP_LEFT);
		setupVertex(renderBlocks, x + 0.3125F, y, z + 0.625F, uMinL, vMin, BOTTOM_LEFT);
		setupVertex(renderBlocks, x + 0.5F, y, z + 0.625F, uMaxL, vMin, BOTTOM_CENTER);
		setupVertex(renderBlocks, x + 0.5F, y + 0.75F, z + 0.625F, uMaxL, vMax, TOP_CENTER);

		// RIGHT
		setupVertex(renderBlocks, x + 0.5F, y + 0.75F, z + 0.625F, uMinR, vMax, TOP_CENTER);
		setupVertex(renderBlocks, x + 0.5F, y, z + 0.625F, uMinR, vMin, BOTTOM_CENTER);
		setupVertex(renderBlocks, x + 0.6875F, y, z + 0.625F, uMaxR, vMin, BOTTOM_RIGHT);
		setupVertex(renderBlocks, x + 0.6875F, y + 0.75F, z + 0.625F, uMaxR, vMax, TOP_RIGHT);

		/* WEST FACE */

		lightingHelper.setLightness(0.6F).setLightingXNeg(block, x, y, z);
		lightingHelper.colorSide(block, x, y, z, 4, icon);

		// LEFT
		setupVertex(renderBlocks, x + 0.375F, y + 0.75F, z + 0.3125F, uMinL, vMax, TOP_LEFT);
		setupVertex(renderBlocks, x + 0.375F, y, z + 0.3125F, uMinL, vMin, BOTTOM_LEFT);
		setupVertex(renderBlocks, x + 0.375F, y, z + 0.5F, uMaxL, vMin, BOTTOM_CENTER);
		setupVertex(renderBlocks, x + 0.375F, y + 0.75F, z + 0.5F, uMaxL, vMax, TOP_CENTER);

		// RIGHT
		setupVertex(renderBlocks, x + 0.375F, y + 0.75F, z + 0.5F, uMinR, vMax, TOP_CENTER);
		setupVertex(renderBlocks, x + 0.375F, y, z + 0.5F, uMinR, vMin, BOTTOM_CENTER);
		setupVertex(renderBlocks, x + 0.375F, y, z + 0.6875F, uMaxR, vMin, BOTTOM_RIGHT);
		setupVertex(renderBlocks, x + 0.375F, y + 0.75F, z + 0.6875F, uMaxR, vMax, TOP_RIGHT);

		/* EAST FACE */

		lightingHelper.setLightness(0.6F).setLightingXPos(block, x, y, z);
		lightingHelper.colorSide(block, x, y, z, 5, icon);

		// LEFT
		setupVertex(renderBlocks, x + 0.625F, y + 0.75F, z + 0.6875F, uMinL, vMax, TOP_LEFT);
		setupVertex(renderBlocks, x + 0.625F, y, z + 0.6875F, uMinL, vMin, BOTTOM_LEFT);
		setupVertex(renderBlocks, x + 0.625F, y, z + 0.5F, uMaxL, vMin, BOTTOM_CENTER);
		setupVertex(renderBlocks, x + 0.625F, y + 0.75F, z + 0.5F, uMaxL, vMax, TOP_CENTER);

		// RIGHT
		setupVertex(renderBlocks, x + 0.625F, y + 0.75F, z + 0.5F, uMinR, vMax, TOP_CENTER);
		setupVertex(renderBlocks, x + 0.625F, y, z + 0.5F, uMinR, vMin, BOTTOM_CENTER);
		setupVertex(renderBlocks, x + 0.625F, y, z + 0.3125F, uMaxR, vMin, BOTTOM_RIGHT);
		setupVertex(renderBlocks, x + 0.625F, y + 0.75F, z + 0.3125F, uMaxR, vMax, TOP_RIGHT);

		/* UP */

		lightingHelper.setLightness(1.0F).setLightingYPos(block, x, y, z);
		lightingHelper.colorSide(block, x, y, z, 1, icon);

		icon = block.getBlockTextureFromSide(1);

		double uMin = icon.getInterpolatedU(6.0D);
		double uMax = icon.getInterpolatedU(10.0D);
		vMin = icon.getInterpolatedV(10.0D);
		vMax = icon.getInterpolatedV(6.0D);

		setupVertex(renderBlocks, x + 0.375F, y + 0.75F, z + 0.625F, uMin, vMin, TOP_LEFT);
		setupVertex(renderBlocks, x + 0.625F, y + 0.75F, z + 0.625F, uMin, vMax, BOTTOM_LEFT);
		setupVertex(renderBlocks, x + 0.625F, y + 0.75F, z + 0.375F, uMax, vMax, BOTTOM_RIGHT);
		setupVertex(renderBlocks, x + 0.375F, y + 0.75F, z + 0.375F, uMax, vMin, TOP_RIGHT);

		renderBlocks.enableAO = false;
	}

}
