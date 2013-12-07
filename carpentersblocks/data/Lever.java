package carpentersblocks.data;

import net.minecraft.block.material.Material;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;

public class Lever {

	/**
	 * 16-bit data components:
	 *
	 *	[0000000]	[0]		[0]		[0]			[0]		[000]
	 *  Unused		Axis	Ready	Polarity	State	Facing
	 */

	public enum Axis
	{
		X,
		Z
	}

	/*
	 * Polarity (inverts default state).
	 */
	public final static byte POLARITY_POSITIVE = 0;
	public final static byte POLARITY_NEGATIVE = 1;

	/*
	 * State (on/off).
	 */
	public final static byte STATE_OFF = 0;
	public final static byte STATE_ON = 1;

	/**
	 * Returns facing.
	 */
	public static ForgeDirection getFacing(TEBase TE)
	{
		return ForgeDirection.getOrientation(BlockProperties.getData(TE) & 0x7);
	}

	/**
	 * Sets facing.
	 */
	public static void setFacing(TEBase TE, int side)
	{
		int temp = BlockProperties.getData(TE) & 0xfff8;
		temp |= side;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns state.
	 */
	public static int getState(TEBase TE)
	{
		int temp = BlockProperties.getData(TE) & 0x8;
		return temp >> 3;
	}

	/**
	 * Sets state.
	 */
	public static void setState(TEBase TE, int state, boolean playSound)
	{
		int temp = BlockProperties.getData(TE) & 0xfff7;
		temp |= state << 3;

		if (
				!TE.worldObj.isRemote &&
				BlockProperties.getCoverBlock(TE, 6).blockMaterial != Material.cloth &&
				playSound &&
				getState(TE) != state
				) {
			TE.worldObj.playSoundEffect(TE.xCoord + 0.5D, TE.yCoord + 0.5D, TE.zCoord + 0.5D, "random.click", 0.3F, getState(TE) == STATE_ON ? 0.5F : 0.6F);
		}

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns polarity.
	 */
	public static int getPolarity(TEBase TE)
	{
		int temp = BlockProperties.getData(TE) & 0x10;
		return temp >> 4;
	}

	/**
	 * Sets polarity.
	 */
	public static void setPolarity(TEBase TE, int polarity)
	{
		int temp = BlockProperties.getData(TE) & 0xffef;
		temp |= polarity << 4;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns rotation axis.
	 */
	public static Axis getAxis(TEBase TE)
	{
		int temp = BlockProperties.getData(TE) & 0x40;
		return temp > 1 ? Axis.Z : Axis.X;
	}

	/**
	 * Sets rotation axis.
	 */
	public static void setAxis(TEBase TE, Axis axis)
	{
		int temp = BlockProperties.getData(TE) & 0xffbf;
		temp |= axis.ordinal() << 6;

		BlockProperties.setData(TE, temp);
	}

	/**
	 * Returns whether block is capable of handling logic functions.
	 * This is implemented because for buttons and levers the SERVER
	 * lags behind the client and will cause the block to pop of walls
	 * before it has a chance to set the correct facing.
	 */
	public static boolean isReady(TEBase TE)
	{
		return (BlockProperties.getData(TE) & 0x20) > 1;
	}

	/**
	 * Sets block as ready.
	 */
	public static void setReady(TEBase TE)
	{
		int temp = BlockProperties.getData(TE) & 0xffdf;
		temp |= 1 << 5;

		BlockProperties.setData(TE, temp);
	}

}
