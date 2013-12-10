package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import carpentersblocks.data.Safe;
import carpentersblocks.tileentity.TECarpentersSafe;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;

public class BlockHandlerCarpentersSafe extends BlockDeterminantRender {

	private final int numBoxes 		= 21;
	private final int numCapacity 	= 9;

	private final byte BLOCKTYPE_COVER 			= 0;
	private final byte BLOCKTYPE_PANEL 			= 1;
	private final byte BLOCKTYPE_HANDLE			= 2;
	private final byte BLOCKTYPE_GREEN_LIGHT	= 3;
	private final byte BLOCKTYPE_RED_LIGHT 		= 4;
	private final byte BLOCKTYPE_DOOR 			= 5;

	private final byte WOOD_XLOW_WALL 				= 0;
	private final byte WOOD_XMAX_WALL 				= 1;
	private final byte WOOD_BOTTOM_LEFT 			= 2;
	private final byte WOOD_BOTTOM_RIGHT 			= 3;
	private final byte WOOD_TOP_LEFT 				= 4;
	private final byte WOOD_TOP_RIGHT 				= 5;
	private final byte WOOD_BACK_LEFT 				= 6;
	private final byte WOOD_BACK_RIGHT 				= 7;
	private final byte WOOD_VERTICAL_CENTER_PIECE 	= 8;
	private final byte WOOD_SHELF_TOP 				= 9;
	private final byte WOOD_SHELF_BOTTOM 			= 10;
	private final byte SLIDING_DOOR 				= 11;
	private final byte IRON_PANEL_TOP 				= 12;
	private final byte IRON_PANEL_LEFT 				= 13;
	private final byte IRON_PANEL_RIGHT 			= 14;
	private final byte IRON_PANEL_CENTER 			= 15;
	private final byte IRON_PANEL_BOTTOM 			= 16;
	private final byte IRON_PANEL_BACK_PLATE 		= 17;
	private final byte HANDLE 						= 18;
	private final byte GREEN_LIGHT 					= 19;
	private final byte RED_LIGHT 					= 20;

	private final float LIGHT_MAX = 1.0F;
	private final float LIGHT_MIN = 0.15F;

	private final float[] LIGHT_RED_ACTIVE 		= { LIGHT_MAX, 0.0F, 0.0F };
	private final float[] LIGHT_RED_INACTIVE	= { LIGHT_MAX / 3.0F, LIGHT_MIN, LIGHT_MIN };
	private final float[] LIGHT_GREEN_ACTIVE	= { 0.0F, LIGHT_MAX, 0.0F };
	private final float[] LIGHT_GREEN_INACTIVE	= { LIGHT_MIN, LIGHT_MAX / 3.0F, LIGHT_MIN };
	private final float[] LIGHT_BLUE_ACTIVE		= { 0.0F, 0.0F, LIGHT_MAX };
	private final float[] LIGHT_BLUE_INACTIVE	= { LIGHT_MIN, LIGHT_MIN, LIGHT_MAX / 3.0F };

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
	{
		Tessellator tessellator = Tessellator.instance;
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		for (int box = 0; box < numBoxes; ++box)
		{
			/* Render the door closed. */
			if (box == SLIDING_DOOR) {
				renderBlocks.setRenderBounds(0.375D, 0.0625D, 0.875F, 0.9375D, 0.9375D, 0.9375D);
			} else {
				setBounds(renderBlocks, box);
			}

			rotateBounds(renderBlocks, ForgeDirection.EAST);
			int type = getBlockType(box);
			Block boxBlock;

			switch (type) {
			case BLOCKTYPE_COVER:
			case BLOCKTYPE_DOOR:
				boxBlock = BlockRegistry.blockCarpentersSafe;
				break;
			case BLOCKTYPE_PANEL:
			case BLOCKTYPE_HANDLE:
				boxBlock = Block.blockIron;
				break;
			default:
				boxBlock = Block.ice;
				renderBlocks.setOverrideBlockTexture(IconRegistry.icon_safe_light);
			}

			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderBlocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, boxBlock.getBlockTextureFromSide(0));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderBlocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, boxBlock.getBlockTextureFromSide(1));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderBlocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, boxBlock.getBlockTextureFromSide(2));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderBlocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, boxBlock.getBlockTextureFromSide(3));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderBlocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, boxBlock.getBlockTextureFromSide(4));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderBlocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, boxBlock.getBlockTextureFromSide(5));
			tessellator.draw();

			renderBlocks.clearOverrideBlockTexture();
		}

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	/**
	 * Rotates renderBounds if direction does not equal NORTH.
	 */
	private void rotateBounds(RenderBlocks renderBlocks, ForgeDirection dir)
	{
		switch (dir) {
		case SOUTH:
			renderBlocks.setRenderBounds(
					1.0D - renderBlocks.renderMaxX,
					renderBlocks.renderMinY,
					1.0D - renderBlocks.renderMaxZ,
					1.0D - renderBlocks.renderMinX,
					renderBlocks.renderMaxY,
					1.0D - renderBlocks.renderMinZ
					);
			break;
		case WEST:
			renderBlocks.setRenderBounds(
					renderBlocks.renderMinZ,
					renderBlocks.renderMinY,
					1.0D - renderBlocks.renderMaxX,
					renderBlocks.renderMaxZ,
					renderBlocks.renderMaxY,
					1.0D - renderBlocks.renderMinX
					);
			break;
		case EAST:
			renderBlocks.setRenderBounds(
					1.0D - renderBlocks.renderMaxZ,
					renderBlocks.renderMinY,
					renderBlocks.renderMinX,
					1.0D - renderBlocks.renderMinZ,
					renderBlocks.renderMaxY,
					renderBlocks.renderMaxX
					);
			break;
		default: {}
		}
	}

	/**
	 * Returns box block type.
	 */
	private int getBlockType(int box)
	{
		if (box < 11) {
			return BLOCKTYPE_COVER;
		} else if (box == 11) {
			return BLOCKTYPE_DOOR;
		} else if (box < 18) {
			return BLOCKTYPE_PANEL;
		} else if (box == 18) {
			return BLOCKTYPE_HANDLE;
		} else if (box == 19) {
			return BLOCKTYPE_GREEN_LIGHT;
		} else {
			return BLOCKTYPE_RED_LIGHT;
		}
	}

	/**
	 * All bounds are set relative to NORTH facing safe.
	 * Rotate them after this using rotateBounds().
	 */
	private void setBounds(RenderBlocks renderBlocks, int box)
	{
		boolean isOpen = TE == null ? false : Safe.getState(TE) == Safe.STATE_OPEN;

		switch (box) {
		case WOOD_XLOW_WALL:
			renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
			break;
		case WOOD_XMAX_WALL:
			renderBlocks.setRenderBounds(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			break;
		case WOOD_BOTTOM_LEFT:
			renderBlocks.setRenderBounds(0.0625D, 0.0D, 0.0625D, 0.3125D, 0.0625D, 1.0D);
			break;
		case WOOD_BOTTOM_RIGHT:
			renderBlocks.setRenderBounds(0.375D, 0.0D, 0.0625D, 0.9375D, 0.0625D, 1.0D);
			break;
		case WOOD_TOP_LEFT:
			renderBlocks.setRenderBounds(0.0625D, 0.9375D, 0.0625D, 0.3125D, 1.0D, 1.0D);
			break;
		case WOOD_TOP_RIGHT:
			renderBlocks.setRenderBounds(0.375D, 0.9375D, 0.0625D, 0.9375D, 1.0D, 1.0D);
			break;
		case WOOD_BACK_LEFT:
			renderBlocks.setRenderBounds(0.0625D, 0.0D, 0.0D, 0.3125D, 1.0D, 0.0625D);
			break;
		case WOOD_BACK_RIGHT:
			renderBlocks.setRenderBounds(0.375D, 0.0D, 0.0D, 0.9375D, 1.0D, 0.0625D);
			break;
		case WOOD_VERTICAL_CENTER_PIECE:
			renderBlocks.setRenderBounds(0.3125D, 0.0D, 0.0D, 0.375D, 1.0D, 1.0D);
			break;
		case WOOD_SHELF_TOP:
			renderBlocks.setRenderBounds(0.375D, 0.625D, 0.0625D, 0.9375D, 0.6875D, 0.875D);
			break;
		case WOOD_SHELF_BOTTOM:
			renderBlocks.setRenderBounds(0.375D, 0.3125D, 0.0625D, 0.9375D, 0.375D, 0.875D);
			break;
		case SLIDING_DOOR:
			renderBlocks.setRenderBounds(0.375D, 0.0625D, 0.875F, isOpen ? 0.5625D : 0.9375D, 0.9375D, 0.9375D);
			break;
		case IRON_PANEL_TOP:
			renderBlocks.setRenderBounds(0.125D, 0.875D, 0.9375D, 0.25D, 0.9375D, 1.0D);
			break;
		case IRON_PANEL_LEFT:
			renderBlocks.setRenderBounds(0.0625D, 0.0625D, 0.9375D, 0.125D, 0.9375D, 1.0D);
			break;
		case IRON_PANEL_RIGHT:
			renderBlocks.setRenderBounds(0.25D, 0.0625D, 0.9375D, 0.3125D, 0.9375D, 1.0D);
			break;
		case IRON_PANEL_CENTER:
			renderBlocks.setRenderBounds(0.125D, 0.6875D, 0.9375D, 0.25D, 0.75D, 1.0D);
			break;
		case IRON_PANEL_BOTTOM:
			renderBlocks.setRenderBounds(0.125D, 0.0625D, 0.9375D, 0.25D, 0.125D, 1.0D);
			break;
		case IRON_PANEL_BACK_PLATE:
			renderBlocks.setRenderBounds(0.0625D, 0.0625D, 0.875D, 0.3125D, 0.9375D, 0.9375D);
			break;
		case HANDLE:
			renderBlocks.setRenderBounds(isOpen ? 0.4375D : 0.8125D, 0.375D, 0.9375D, isOpen ? 0.5D : 0.875D, 0.625D, 1.0D);
			break;
		case GREEN_LIGHT:
			renderBlocks.setRenderBounds(0.125D, 0.8125D, 0.9375D, 0.25D, 0.875D, 1.0D);
			break;
		case RED_LIGHT:
			renderBlocks.setRenderBounds(0.125D, 0.75D, 0.9375D, 0.25D, 0.8125D, 1.0D);
			break;
		}
	}

	@Override
	/**
	 * Renders safe.
	 */
	protected boolean renderCarpentersBlock(int x, int y, int z)
	{
		renderBlocks.renderAllFaces = true;

		/* Begin drawing everything but the capacity light strip. */

		Block block = BlockProperties.getCoverBlock(TE, 6);
		ForgeDirection facing = Safe.getFacing(TE);

		for (int box = 0; box < numBoxes; ++box)
		{
			setBounds(renderBlocks, box);
			rotateBounds(renderBlocks, facing);
			drawBox(block, x, y, z, box);
		}

		/* Begin drawing the capacity light strip. */

		if (!shouldRenderOpaque())
		{
			setIconOverride(6, IconRegistry.icon_safe_light);

			double yMin = 0.125D;
			double yMax = 0.1875D;
			int capacity = getCapacityIlluminated();

			suppressDyeColor = true;
			suppressOverlay = true;
			suppressPattern = true;
			disableAO = true;

			for (int box = 0; box < numCapacity; ++box)
			{
				if (box + 1 <= capacity) {
					lightingHelper.setBrightnessOverride(lightingHelper.MAX_BRIGHTNESS);
					lightingHelper.setColorOverride(LIGHT_BLUE_ACTIVE);
				} else {
					lightingHelper.setColorOverride(LIGHT_BLUE_INACTIVE);
				}

				renderBlocks.setRenderBounds(0.125D, yMin, 0.9375D, 0.25D, yMax, 1.0D);
				rotateBounds(renderBlocks, facing);
				renderBlock(Block.ice, x, y, z);
				lightingHelper.clearColorOverride();
				lightingHelper.clearBrightnessOverride();

				yMin += 0.0625D;
				yMax += 0.0625D;
			}

			disableAO = false;
			suppressDyeColor = false;
			suppressOverlay = false;
			suppressPattern = false;

			clearIconOverride(6);
		}

		renderBlocks.renderAllFaces = false;

		return true;
	}

	private void drawBox(Block block, int x, int y, int z, int box)
	{
		boolean isLocked = Safe.isLocked(TE);
		int type = getBlockType(box);

		if (type != BLOCKTYPE_COVER && type != BLOCKTYPE_DOOR)
		{
			suppressDyeColor = true;
			suppressOverlay = true;
			suppressPattern = true;
		}

		switch (type) {
		case BLOCKTYPE_PANEL:
			if (shouldRenderOpaque()) {
				renderBlock(getMetalBlock(), x, y, z);
			}
			break;
		case BLOCKTYPE_HANDLE:
			if (shouldRenderOpaque()) {
				renderBlock(Block.blockIron, x, y, z);
			}
			break;
		case BLOCKTYPE_GREEN_LIGHT:
			if (!shouldRenderOpaque())
			{
				disableAO = true;
				setIconOverride(6, IconRegistry.icon_safe_light);

				if (isLocked) {
					lightingHelper.setColorOverride(LIGHT_GREEN_INACTIVE);
				} else {
					lightingHelper.setBrightnessOverride(lightingHelper.MAX_BRIGHTNESS);
					lightingHelper.setColorOverride(LIGHT_GREEN_ACTIVE);
				}

				renderBlock(Block.ice, x, y, z);
				lightingHelper.clearColorOverride();
				lightingHelper.clearBrightnessOverride();
				clearIconOverride(6);
				disableAO = false;
			}
			break;
		case BLOCKTYPE_RED_LIGHT:
			if (!shouldRenderOpaque())
			{
				disableAO = true;
				setIconOverride(6, IconRegistry.icon_safe_light);

				if (isLocked) {
					lightingHelper.setBrightnessOverride(lightingHelper.MAX_BRIGHTNESS);
					lightingHelper.setColorOverride(LIGHT_RED_ACTIVE);
				} else {
					lightingHelper.setColorOverride(LIGHT_RED_INACTIVE);
				}

				renderBlock(Block.ice, x, y, z);
				lightingHelper.clearColorOverride();
				lightingHelper.clearBrightnessOverride();
				clearIconOverride(6);
				disableAO = false;
			}
			break;
		case BLOCKTYPE_DOOR:
			lightingHelper.setLightnessOffset(REDUCED_OFFSET);
		default:
			if (shouldRenderCover(block)) {
				renderBlock(block, x, y, z);
			}
		}

		lightingHelper.clearLightnessOffset();
		suppressDyeColor = false;
		suppressOverlay = false;
		suppressPattern = false;
	}

	/**
	 * Returns metal block used for safe panel.
	 */
	private Block getMetalBlock()
	{
		TECarpentersSafe TE_safe = (TECarpentersSafe) TE;

		return TE_safe.hasUpgrade() ? Block.blockGold : Block.blockIron;
	}

	/**
	 * Returns how many capacity strips to illuminate.
	 * Value returned should be between 0-9.
	 */
	private int getCapacityIlluminated()
	{
		TECarpentersSafe TE_safe = (TECarpentersSafe) TE;
		int numSlotsFilled = 0;

		for (int slot = 0; slot < TE_safe.getSizeInventory(); ++slot)
		{
			if (TE_safe.getStackInSlot(slot) != null) {
				++numSlotsFilled;
			}
		}

		return numSlotsFilled / (3 * TE_safe.getSizeInventory() / 27);
	}

}