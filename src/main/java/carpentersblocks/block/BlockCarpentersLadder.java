package carpentersblocks.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Ladder;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;

public class BlockCarpentersLadder extends BlockCoverable {

    public BlockCarpentersLadder(int blockID)
    {
        super(blockID, Material.wood);
        setHardness(Block.ladder.blockHardness);
        setUnlocalizedName("blockCarpentersLadder");
        setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
        setStepSound(soundLadderFootstep);
        setTextureName("carpentersblocks:general/solid");
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);
        int data = BlockProperties.getData(TE);

        switch (data) {
            case Ladder.FACING_NORTH: // Ladder on +Z
                setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, 1.0F, 1.0F);
                break;
            case Ladder.FACING_SOUTH: // Ladder on -Z
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1875F);
                break;
            case Ladder.FACING_WEST: // Ladder on +X
                setBlockBounds(0.8125F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;
            case Ladder.FACING_EAST: // Ladder on -X
                setBlockBounds(0.0F, 0.0F, 0.0F, 0.1875F, 1.0F, 1.0F);
                break;
            case Ladder.FACING_ON_X:
                setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
                break;
            default: // Ladder.FACING_ON_Z
                setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
                break;
        }
    }

    @Override
    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity)
    {
        setBlockBoundsBasedOnState(world, x, y, z);
        super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        switch (ForgeDirection.getOrientation(side)) {
            case DOWN:
                return world.isBlockSolidOnSide(x, y + 1, z, ForgeDirection.UP) || world.getBlockId(x, y + 1, z) == blockID && Ladder.isFreestanding((TEBase)world.getBlockTileEntity(x, y + 1, z));
            case UP:
                return world.isBlockSolidOnSide(x, y - 1, z, ForgeDirection.DOWN) || world.getBlockId(x, y - 1, z) == blockID && Ladder.isFreestanding((TEBase)world.getBlockTileEntity(x, y - 1, z));
            case NORTH:
                return world.isBlockSolidOnSide(x, y, z + 1, ForgeDirection.SOUTH);
            case SOUTH:
                return world.isBlockSolidOnSide(x, y, z - 1, ForgeDirection.NORTH);
            case WEST:
                return world.isBlockSolidOnSide(x + 1, y, z, ForgeDirection.EAST);
            case EAST:
                return world.isBlockSolidOnSide(x - 1, y, z, ForgeDirection.WEST);
            default:
                return false;
        }
    }

    @Override
    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int initData)
    {
        return side;
    }

    @Override
    /**
     * Called when the block is placed in the world.
     * Uses cardinal direction to adjust metadata if player clicks top or bottom face of block.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);

        if (metadata < 2) {
            int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
            BlockProperties.setData(TE, facing % 2 == 0 ? Ladder.FACING_ON_X : Ladder.FACING_ON_Z);
        } else {
            BlockProperties.setData(TE, metadata);
        }

        if (!entityLiving.isSneaking())
        {
            TEBase TE_adj = null;

            if (world.getBlockId(x, y - 1, z) == blockID) {
                TE_adj = (TEBase) world.getBlockTileEntity(x, y - 1, z);
            } else if (world.getBlockId(x, y + 1, z) == blockID) {
                TE_adj = (TEBase) world.getBlockTileEntity(x, y + 1, z);
            }

            if (TE_adj != null) {
                BlockProperties.setData(TE, BlockProperties.getData(TE_adj));
            }
        }

        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
    }

    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {
        if (!world.isRemote)
        {
            TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

            if (TE != null)
            {
                int data = BlockProperties.getData(TE);

                if (data > 1)
                {
                    if (
                            data == 2 && !world.isBlockSolidOnSide(x, y, z + 1, ForgeDirection.NORTH) ||
                            data == 3 && !world.isBlockSolidOnSide(x, y, z - 1, ForgeDirection.SOUTH) ||
                            data == 4 && !world.isBlockSolidOnSide(x + 1, y, z, ForgeDirection.WEST) ||
                            data == 5 && !world.isBlockSolidOnSide(x - 1, y, z, ForgeDirection.EAST)
                            )
                    {
                        dropBlockAsItem(world, x, y, z, data, 0);
                        world.setBlockToAir(x, y, z);
                    }
                }
            }
        }

        super.onNeighborBlockChange(world, x, y, z, blockID);
    }

    @Override
    public boolean isLadder(World world, int x, int y, int z, EntityLivingBase entityLiving)
    {
        return true;
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersLadderRenderID;
    }

}
