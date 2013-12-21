package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.data.Bed;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BedDesignHandler;
import carpentersblocks.util.registry.IconRegistry;

public class BlockHandlerCarpentersBed extends BlockDeterminantRender {

	@Override
	public boolean shouldRender3DInInventory()
	{
		return false;
	}

	@Override
	/**
	 * Renders bed
	 */
	protected boolean renderCarpentersBlock(int x, int y, int z)
	{
		Block block = BlockProperties.getCoverBlock(TE, 6);
		renderNormalBed(block, x, y, z);
		return true;
	}

	private void renderNormalBed(Block block, int x, int y, int z)
	{
		ForgeDirection dir = Bed.getDirection(TE);

		disableAO = true;

		boolean isHead = Bed.isHeadOfBed(TE);

		TEBase TE_opp = Bed.getOppositeTE(TE);

		boolean isOccupied = Bed.isOccupied(TE);
		int	blanketColor = 0;
		int frameColor = 0;

		/*
		 * The bed piece will render before it's companion
		 * is created, and just after it is destroyed.
		 * Therefore, we must null check it.
		 */
		if (TE_opp != null)
		{
			isOccupied |= Bed.isOccupied(TE_opp);

			/*
			 * Get bed dye colors.
			 * 
			 * Blanket color is foot dye color.
			 * Frame color is head dye color.
			 */
			blanketColor = BlockProperties.getDyeColor(isHead ? TE_opp : TE, 6);
			frameColor = BlockProperties.getDyeColor(isHead ? TE : TE_opp, 6);
		}

		int design = Bed.getDesign(TE);

		boolean hasCustomBlanket = design > 0 && BedDesignHandler.hasBlanket[design];

		Icon icon_pillow = hasCustomBlanket && BedDesignHandler.hasPillow[design] ? IconRegistry.icon_bed_pillow_custom[design] : IconRegistry.icon_bed_pillow;

		/*
		 * Check for adjacent bed pieces that can connect.
		 */
		boolean	bedParallelPos = false;
		boolean bedParallelNeg = false;

		if (dir.equals(ForgeDirection.NORTH) || dir.equals(ForgeDirection.SOUTH))
		{
			if (renderBlocks.blockAccess.getBlockId(x + 1, y, z) == srcBlock.blockID) {
				TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getBlockTileEntity(x + 1,  y,  z);
				bedParallelPos = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
			}
			if (renderBlocks.blockAccess.getBlockId(x - 1, y, z) == srcBlock.blockID) {
				TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getBlockTileEntity(x - 1,  y,  z);
				bedParallelNeg = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
			}

		} else {

			if (renderBlocks.blockAccess.getBlockId(x, y, z + 1) == srcBlock.blockID) {
				TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getBlockTileEntity(x,  y, z + 1);
				bedParallelPos = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
			}
			if (renderBlocks.blockAccess.getBlockId(x, y, z - 1) == srcBlock.blockID) {
				TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getBlockTileEntity(x,  y, z - 1);
				bedParallelNeg = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
			}

		}

		switch (dir)
		{
		case NORTH: // -Z
		{
			if (isHead) {

				if (shouldRenderCover(block))
				{
					// Render headboard
					renderBlocks.setRenderBounds(0.125D, 0.1875D, 0.875D, 0.875D, 0.875D, 1.0D);
					renderBlock(block, x, y, z);

					// Render legs
					renderBlocks.setRenderBounds(0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.875D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 1.0D);
					renderBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.875D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 1.0D);
					renderBlock(block, x, y, z);

					// Render support board
					renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 0.875D);
					renderBlock(block, x, y, z);
				}

				if (shouldRenderOpaque())
				{
					suppressDyeColor = true;
					suppressOverlay = true;
					suppressPattern = true;

					disableAO = false;

					// Render mattress
					setMetadataOverride(0);
					renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.0D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 0.875D);
					renderBlock(Block.cloth, x, y, z);
					clearMetadataOverride();

					// Render pillow
					renderBlocks.uvRotateTop = 2;
					setIconOverride(6, icon_pillow);
					renderBlocks.setRenderBounds(0.125D, 0.5625D, 0.4375D, 0.875D, 0.6875D, 0.8125D);
					renderBlock(Block.cloth, x, y, z);
					clearIconOverride(6);
					renderBlocks.uvRotateTop = 0;

					// Render blanket
					if (!hasCustomBlanket) {
						setMetadataOverride(blanketColor);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 0.5D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 0.5D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.5D);
						renderBlock(Block.cloth, x, y, z);
						clearMetadataOverride();
					}

					disableAO = true;

					suppressDyeColor = false;
					suppressOverlay = false;
					suppressPattern = false;
				}

			} else {

				if (shouldRenderCover(block))
				{
					setDyeColorOverride(frameColor);

					// Render legs
					if (!bedParallelNeg) {
						renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.125D, 0.1875D, 0.125D);
						renderBlock(block, x, y, z);
					}
					if (!bedParallelPos) {
						renderBlocks.setRenderBounds(0.875D, 0.0D, 0.0D, 1.0D, 0.1875D, 0.125D);
						renderBlock(block, x, y, z);
					}

					// Render support board
					renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
					renderBlock(block, x, y, z);

					clearDyeColorOverride();
				}

				if (shouldRenderOpaque())
				{
					suppressDyeColor = true;
					suppressOverlay = true;
					suppressPattern = true;

					disableAO = false;

					// Render mattress
					setMetadataOverride(0);
					renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.0625D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 1.0D);
					renderBlock(Block.cloth, x, y, z);
					clearMetadataOverride();

					// Render blanket
					if (!hasCustomBlanket) {
						setMetadataOverride(blanketColor);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.0625D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.9375D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
						renderBlock(Block.cloth, x, y, z);
						clearMetadataOverride();
					}

					disableAO = true;

					suppressDyeColor = false;
					suppressOverlay = false;
					suppressPattern = false;
				}

			}
			break;
		}
		case SOUTH: // +Z
		{
			if (isHead) {

				if (shouldRenderCover(block))
				{
					// Render headboard
					renderBlocks.setRenderBounds(0.125D, 0.1875D, 0.0D, 0.875D, 0.875D, 0.125D);
					renderBlock(block, x, y, z);

					// Render legs
					renderBlocks.setRenderBounds(0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);
					renderBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.0D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 0.125D);
					renderBlock(block, x, y, z);

					// Render support board
					renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.125D, 1.0D, 0.3125D, 1.0D);
					renderBlock(block, x, y, z);
				}

				if (shouldRenderOpaque())
				{
					suppressDyeColor = true;
					suppressOverlay = true;
					suppressPattern = true;

					disableAO = false;

					// Render mattress
					setMetadataOverride(0);
					renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.125D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 1.0D);
					renderBlock(Block.cloth, x, y, z);
					clearMetadataOverride();

					// Render pillow
					setIconOverride(6, icon_pillow);
					renderBlocks.setRenderBounds(0.125D, 0.5625D, 0.1875D, 0.875D, 0.6875D, 0.5625D);
					renderBlock(Block.cloth, x, y, z);
					clearIconOverride(6);
					renderBlocks.uvRotateTop = 0;

					// Render blanket
					if (!hasCustomBlanket) {
						setMetadataOverride(blanketColor);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.5D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.5D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.5D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						clearMetadataOverride();
					}

					disableAO = true;

					suppressDyeColor = false;
					suppressOverlay = false;
					suppressPattern = false;
				}

			} else {

				if (shouldRenderCover(block))
				{
					setDyeColorOverride(frameColor);

					// Render legs
					if (!bedParallelNeg) {
						renderBlocks.setRenderBounds(0.0D, 0.0D, 0.875D, 0.125D, 0.1875D, 1.0D);
						renderBlock(block, x, y, z);
					}
					if (!bedParallelPos) {
						renderBlocks.setRenderBounds(0.875D, 0.0D, 0.875D, 1.0D, 0.1875D, 1.0D);
						renderBlock(block, x, y, z);
					}

					// Render support board
					renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
					renderBlock(block, x, y, z);

					clearDyeColorOverride();
				}

				if (shouldRenderOpaque())
				{
					suppressDyeColor = true;
					suppressOverlay = true;
					suppressPattern = true;

					disableAO = false;

					// Render mattress
					setMetadataOverride(0);
					renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.0D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 0.9375D);
					renderBlock(Block.cloth, x, y, z);
					clearMetadataOverride();

					// Render blanket
					if (!hasCustomBlanket) {
						setMetadataOverride(blanketColor);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.0625D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 0.9375D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						clearMetadataOverride();
					}

					disableAO = true;

					suppressDyeColor = false;
					suppressOverlay = false;
					suppressPattern = false;
				}

			}
			break;
		}
		case WEST: // -X
		{
			if (isHead) {

				if (shouldRenderCover(block))
				{
					// Render headboard
					renderBlocks.setRenderBounds(0.875D, 0.1875D, 0.125D, 1.0D, 0.875D, 0.875D);
					renderBlock(block, x, y, z);

					// Render legs
					renderBlocks.setRenderBounds(0.875D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 1.0D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);
					renderBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.875D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 1.0D);
					renderBlock(block, x, y, z);

					// Render support board
					renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 0.875D, 0.3125D, 1.0D);
					renderBlock(block, x, y, z);
				}

				if (shouldRenderOpaque())
				{
					suppressDyeColor = true;
					suppressOverlay = true;
					suppressPattern = true;

					disableAO = false;

					// Render mattress
					setMetadataOverride(0);
					renderBlocks.setRenderBounds(0.0D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 0.875D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
					renderBlock(Block.cloth, x, y, z);
					clearMetadataOverride();

					// Render pillow
					renderBlocks.uvRotateTop = 1;
					setIconOverride(6, icon_pillow);
					renderBlocks.setRenderBounds(0.4375D, 0.5625D, 0.125D, 0.8125D, 0.6875D, 0.875D);
					renderBlock(Block.cloth, x, y, z);
					clearIconOverride(6);
					renderBlocks.uvRotateTop = 0;

					// Render blanket
					if (!hasCustomBlanket) {
						setMetadataOverride(blanketColor);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.5D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 0.5D, isOccupied ? 0.875D : 0.625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 0.5D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						clearMetadataOverride();
					}

					disableAO = true;

					suppressDyeColor = false;
					suppressOverlay = false;
					suppressPattern = false;
				}

			} else {

				if (shouldRenderCover(block))
				{
					setDyeColorOverride(frameColor);

					// Render legs
					if (!bedParallelNeg) {
						renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.125D, 0.1875D, 0.125D);
						renderBlock(block, x, y, z);
					}
					if (!bedParallelPos) {
						renderBlocks.setRenderBounds(0.0D, 0.0D, 0.875D, 0.125D, 0.1875D, 1.0D);
						renderBlock(block, x, y, z);
					}

					// Render support board
					renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
					renderBlock(block, x, y, z);

					clearDyeColorOverride();
				}

				if (shouldRenderOpaque())
				{
					suppressDyeColor = true;
					suppressOverlay = true;
					suppressPattern = true;

					disableAO = false;

					// Render mattress
					setMetadataOverride(0);
					renderBlocks.setRenderBounds(0.0625D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 1.0D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
					renderBlock(Block.cloth, x, y, z);
					clearMetadataOverride();

					// Render blanket
					if (!hasCustomBlanket) {
						setMetadataOverride(blanketColor);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0625D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 0.9375D);
						renderBlock(Block.cloth, x, y, z);
						clearMetadataOverride();
					}

					disableAO = true;

					suppressDyeColor = false;
					suppressOverlay = false;
					suppressPattern = false;
				}

			}
			break;
		}
		default: // EAST +X
		{
			if (isHead) {

				if (shouldRenderCover(block))
				{
					// Render headboard
					renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.125D, 0.125D, 0.875D, 0.875D);
					renderBlock(block, x, y, z);

					// Render legs
					renderBlocks.setRenderBounds(0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);
					renderBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.0D, bedParallelPos ? 0.1875D : 0.0D, 0.875D, 0.125D, bedParallelPos ? 0.875D : 1.0D, 1.0D);
					renderBlock(block, x, y, z);

					// Render support board
					renderBlocks.setRenderBounds(0.125D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
					renderBlock(block, x, y, z);
				}

				if (shouldRenderOpaque())
				{
					suppressDyeColor = true;
					suppressOverlay = true;
					suppressPattern = true;

					disableAO = false;

					// Render mattress
					setMetadataOverride(0);
					renderBlocks.setRenderBounds(0.125D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 1.0D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
					renderBlock(Block.cloth, x, y, z);
					clearMetadataOverride();

					// Render pillow
					renderBlocks.uvRotateTop = 3;
					setIconOverride(6, icon_pillow);
					renderBlocks.setRenderBounds(0.1875D, 0.5625D, 0.125D, 0.5625D, 0.6875D, 0.875D);
					renderBlock(Block.cloth, x, y, z);
					clearIconOverride(6);
					renderBlocks.uvRotateTop = 0;

					// Render blanket
					if (!hasCustomBlanket) {
						setMetadataOverride(blanketColor);
						renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						clearMetadataOverride();
					}

					disableAO = true;

					suppressDyeColor = false;
					suppressOverlay = false;
					suppressPattern = false;
				}

			} else {

				if (shouldRenderCover(block))
				{
					setDyeColorOverride(frameColor);

					// Render legs
					if (!bedParallelNeg) {
						renderBlocks.setRenderBounds(0.875D, 0.0D, 0.0D, 1.0D, 0.1875D, 0.125D);
						renderBlock(block, x, y, z);
					}
					if (!bedParallelPos) {
						renderBlocks.setRenderBounds(0.875D, 0.0D, 0.875D, 1.0D, 0.1875D, 1.0D);
						renderBlock(block, x, y, z);
					}

					// Render support board
					renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
					renderBlock(block, x, y, z);

					clearDyeColorOverride();
				}

				if (shouldRenderOpaque())
				{
					suppressDyeColor = true;
					suppressOverlay = true;
					suppressPattern = true;

					disableAO = false;

					// Render mattress
					setMetadataOverride(0);
					renderBlocks.setRenderBounds(0.0D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 0.9375D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
					renderBlock(Block.cloth, x, y, z);
					clearMetadataOverride();

					// Render blanket
					if (!hasCustomBlanket) {
						setMetadataOverride(blanketColor);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
						renderBlock(Block.cloth, x, y, z);
						renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0625D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.9375D);
						renderBlock(Block.cloth, x, y, z);
						clearMetadataOverride();
					}

					disableAO = true;

					suppressDyeColor = false;
					suppressOverlay = false;
					suppressPattern = false;
				}

			}
			break;
		}
		}

		disableAO = false;

		if (shouldRenderOpaque())
		{
			/*
			 * If this bed has a blanket design, we'll render part of the blanket
			 * here to fill in the gaps (face at head of bed, bottom side).
			 */
			if (hasCustomBlanket)
			{
				setMetadataOverride(blanketColor);
				suppressDyeColor = true;
				suppressOverlay = true;
				suppressPattern = true;

				if (isHead)
				{
					switch (dir)
					{
					case NORTH: // -Z
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 0.5D);
						lightingHelper.setLightness(0.8F);
						delegateSideRender(Block.cloth, x, y, z, SOUTH);
						break;
					case SOUTH: // +Z
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.5D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
						lightingHelper.setLightness(0.8F);
						delegateSideRender(Block.cloth, x, y, z, NORTH);
						break;
					case WEST: 	// -X
						renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.5D, isOccupied ? 0.875D : 0.625D, 1.0D);
						lightingHelper.setLightness(0.6F);
						delegateSideRender(Block.cloth, x, y, z, EAST);
						break;
					default: 	// EAST +X
						renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
						lightingHelper.setLightness(0.6F);
						delegateSideRender(Block.cloth, x, y, z, WEST);
						break;
					}

					lightingHelper.setLightness(0.5F);
					delegateSideRender(Block.cloth, x, y, z, DOWN);

				} else {

					lightingHelper.setLightness(0.5F);
					delegateSideRender(Block.cloth, x, y, z, DOWN);

				}

				suppressDyeColor = false;
				suppressOverlay = false;
				suppressPattern = false;
				clearMetadataOverride();
			}
		}
	}

}