package carpentersblocks.entity.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.api.ICarpentersHammer;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.PlayerPermissions;
import carpentersblocks.util.handler.DyeHandler;
import carpentersblocks.util.handler.TileHandler;
import carpentersblocks.util.registry.IconRegistry;
import carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityCarpentersTile extends EntityBase {

    private int ticks;

    private final static byte ID_DIR  = 13;
    private final static byte ID_DYE  = 14;
    private final static byte ID_TILE = 15;
    private final static byte ID_ROT  = 16;
    
    private final static String TAG_TILE = "tile";
    private final static String TAG_DIR  = "dir";
    private final static String TAG_DYE  = "dye";
    private final static String TAG_ROT  = "rot";

    /** Depth of tile. */
    private final static double depth = 0.0625D;

    private final static double[][] bounds =
    {
            {         0.0D, 1.0D - depth,         0.0D,  1.0D,  1.0D,  1.0D },
            {         0.0D,         0.0D,         0.0D,  1.0D, depth,  1.0D },
            {         0.0D,         0.0D, 1.0D - depth,  1.0D,  1.0D,  1.0D },
            {         0.0D,         0.0D,         0.0D,  1.0D,  1.0D, depth },
            { 1.0D - depth,         0.0D,         0.0D,  1.0D,  1.0D,  1.0D },
            {         0.0D,         0.0D,         0.0D, depth,  1.0D,  1.0D }
    };

    public EntityCarpentersTile(World world)
    {
        super(world);
    }

    public EntityCarpentersTile(EntityPlayer entityPlayer, World world, int x, int y, int z, ForgeDirection dir, ForgeDirection offset_side, boolean ignoreNeighbors)
    {
        super(world, entityPlayer);
        posX = x;
        posY = y;
        posZ = z;
        setDirection(dir);
        this.setBoundingBox();

        if (!ignoreNeighbors) {
        
            List<EntityCarpentersTile> list = new ArrayList<EntityCarpentersTile>();
            double factor = 0.2D;
            
            boundingBox.contract(0.1D, 0.1D, 0.1D);

            switch (offset_side) {
                case DOWN:
                    list = world.getEntitiesWithinAABB(EntityCarpentersTile.class, boundingBox.offset(0.0D, -factor, 0.0D));
                    break;
                case UP:
                    list = world.getEntitiesWithinAABB(EntityCarpentersTile.class, boundingBox.offset(0.0D, factor, 0.0D));
                    break;
                case NORTH:
                    list = world.getEntitiesWithinAABB(EntityCarpentersTile.class, boundingBox.offset(0.0D, 0.0D, -factor));
                    break;
                case SOUTH:
                    list = world.getEntitiesWithinAABB(EntityCarpentersTile.class, boundingBox.offset(0.0D, 0.0D, factor));
                    break;
                case WEST:
                    list = world.getEntitiesWithinAABB(EntityCarpentersTile.class, boundingBox.offset(-factor, 0.0D, 0.0D));
                    break;
                case EAST:
                    list = world.getEntitiesWithinAABB(EntityCarpentersTile.class, boundingBox.offset(factor, 0.0D, 0.0D));
                    break;
                default:
                    
                    switch (dir) {
                        case DOWN:
                        case UP:
                            list = world.getEntitiesWithinAABB(EntityCarpentersTile.class, boundingBox.expand(factor, 0.0D, factor));
                            break;
                        case NORTH:
                        case SOUTH:
                            list = world.getEntitiesWithinAABB(EntityCarpentersTile.class, boundingBox.expand(factor, factor, 0.0D));
                            break;
                        case WEST:
                        case EAST:
                            list = world.getEntitiesWithinAABB(EntityCarpentersTile.class, boundingBox.expand(0.0D, factor, factor));
                            break;
                        default: {}
                    }
                    
            }

            for (EntityCarpentersTile tile : list)
            {
                /* Skip checking diagonal tiles when tile is placed in center. */
                
                if (offset_side.equals(ForgeDirection.UNKNOWN))
                {
                    switch (dir) {
                        case DOWN:
                        case UP:
                            if (!(tile.posX == this.posX || tile.posZ == this.posZ)) {
                                continue;
                            }
                            break;
                        case NORTH:
                        case SOUTH:
                            if (!(tile.posX == this.posX || tile.posY == this.posY)) {
                                continue;
                            }
                            break;
                        case WEST:
                        case EAST:
                            if (!(tile.posZ == this.posZ || tile.posY == this.posY)) {
                                continue;
                            }
                            break;
                        default: {}
                    }
                }

                /* Match up tile properties with neighbor. */
                
                if (!tile.getDye().equals(getDefaultDye())) {
                    setDye(tile.getDye());
                }
                if (tile.getRotation() != 0) {
                    setRotation(tile.getRotation());
                }
                if (!tile.getTile().equals(getDefaultTile())) {
                    setTile(tile.getTile());
                }
            }

        }
    }

    public String getDefaultTile()
    {
        return "blank";
    }
    
    public String getDefaultDye()
    {
        return "dyeWhite";
    }
    
    public void playTileSound()
    {
        BlockProperties.playBlockSound(worldObj, new ItemStack(Blocks.hardened_clay), (int) Math.floor(posX), (int) Math.floor(posY), (int) Math.floor(posZ));
    }
    
    public void playDyeSound()
    {
        BlockProperties.playBlockSound(worldObj, new ItemStack(Blocks.sand), (int) Math.floor(posX), (int) Math.floor(posY), (int) Math.floor(posZ));
    }
    
    public double[] getBounds()
    {
        return bounds[getDataWatcher().getWatchableObjectInt(ID_DIR)];
    }

    public void setBoundingBox()
    {
        double bounds[] = getBounds();
        boundingBox.setBounds(posX + bounds[0], posY + bounds[1], posZ + bounds[2], posX + bounds[3], posY + bounds[4], posZ + bounds[5]);
    }

    public ForgeDirection getDirection()
    {
        return ForgeDirection.getOrientation(getDataWatcher().getWatchableObjectInt(ID_DIR));
    }
    
    public void setDirection(ForgeDirection dir)
    {
        getDataWatcher().updateObject(ID_DIR, new Integer(dir.ordinal()));
    }

    public void setRotation(int rotation)
    {
        getDataWatcher().updateObject(ID_ROT, new Integer(rotation));
    }
    
    public void rotate()
    {
        int rotation = getRotation();
        setRotation(++rotation & 3);
    }
    
    public int getRotation()
    {
        return getDataWatcher().getWatchableObjectInt(ID_ROT);
    }
    
    public void setDye(String dye)
    {
        getDataWatcher().updateObject(ID_DYE, new String(dye));
    }
    
    public String getDye()
    {
        return getDataWatcher().getWatchableObjectString(ID_DYE);
    }

    public void setTile(String tile)
    {
        getDataWatcher().updateObject(ID_TILE, new String(tile));
    }
    
    public String getTile()
    {
        return getDataWatcher().getWatchableObjectString(ID_TILE);
    }
    
    /**
     * Sets next tile design.
     */
    private void setNextIcon()
    {
        setTile(TileHandler.getNext(getTile()));
    }
    
    /**
     * Sets previous tile design.
     */
    private void setPrevIcon()
    {
        setTile(TileHandler.getPrev(getTile()));
    }

    public IIcon getIcon()
    {
        if (getTile().equals(getDefaultTile())) {
            return IconRegistry.icon_blank_tile;
        } else {
            return IconRegistry.icon_tile.get(TileHandler.tileList.indexOf(getTile()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbtTagCompound)
    {
        getDataWatcher().updateObject(ID_TILE, String.valueOf(nbtTagCompound.getString(TAG_TILE)));
        getDataWatcher().updateObject(ID_DYE, String.valueOf(nbtTagCompound.getString(TAG_DYE)));
        getDataWatcher().updateObject(ID_DIR, Integer.valueOf(nbtTagCompound.getInteger(TAG_DIR)));
        getDataWatcher().updateObject(ID_ROT, Integer.valueOf(nbtTagCompound.getInteger(TAG_ROT)));
        super.readEntityFromNBT(nbtTagCompound);
    }
    
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setString(TAG_TILE, getDataWatcher().getWatchableObjectString(ID_TILE));
        nbtTagCompound.setString(TAG_DYE, getDataWatcher().getWatchableObjectString(ID_DYE));
        nbtTagCompound.setInteger(TAG_DIR, getDataWatcher().getWatchableObjectInt(ID_DIR));
        nbtTagCompound.setInteger(TAG_ROT, getDataWatcher().getWatchableObjectInt(ID_ROT));
        super.writeEntityToNBT(nbtTagCompound);
    }

    /**
     * Called when this entity is broken. Entity parameter may be null.
     */
    public void onBroken(Entity entity)
    {
        if (entity instanceof EntityPlayer) {

            EntityPlayer entityPlayer = (EntityPlayer) entity;
            ItemStack itemStack = entityPlayer.getHeldItem();
            
            boolean hasHammer = false;
            
            if (itemStack != null) {
                Item item = itemStack.getItem();
                
                if (item instanceof ICarpentersHammer) {
                    hasHammer = true;
                }
            }
            
            if (entityPlayer.capabilities.isCreativeMode && !hasHammer) {
                return;
            }

        }

        entityDropItem(getItemDrop(), 0.0F);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        if (!worldObj.isRemote) {
            
            if (ticks++ >= 20) {
                
                this.setBoundingBox();
                ticks = 0;
                
                if (!isDead && !onValidSurface())
                {
                    setDead();
                    onBroken((Entity)null);
                }
            }

        }
    }

    /**
     * Returns representative ItemStack for entity.
     */
    private ItemStack getItemDrop()
    {
        return new ItemStack(ItemRegistry.itemCarpentersTile);
    }

    /**
     * Called when a user uses the creative pick block button on this entity.
     *
     * @param target The full target the player is looking at
     * @return A ItemStack to add to the player's inventory, Null if nothing should be added.
     */
    @Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
        return getItemDrop();
    }

    @Override
    public boolean shouldRenderInPass(int pass)
    {
        // TODO: Switch to pass 1 when alpha rendering is fixed.
        return pass == 0;
    }

    /**
     * checks to make sure painting can be placed there
     */
    public boolean onValidSurface()
    {
        ForgeDirection dir = getDirection();

        int x_offset = MathHelper.floor_double(posX) - dir.offsetX;
        int y_offset = MathHelper.floor_double(posY) - dir.offsetY;
        int z_offset = MathHelper.floor_double(posZ) - dir.offsetZ;

        Block block = worldObj.getBlock(x_offset, y_offset, z_offset);

        return !(block != null && !block.isSideSolid(worldObj, x_offset, y_offset, z_offset, dir));
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float par2)
    {
        if (!worldObj.isRemote) {

            Entity entity = damageSource.getEntity();
    
            boolean dropItem = false;
            
            if (entity instanceof EntityPlayer) {
                            
                EntityPlayer entityPlayer = (EntityPlayer) entity;
                ItemStack itemStack = entityPlayer.getHeldItem();
    
                if (itemStack != null) {
    
                    if (itemStack.getItem() instanceof ICarpentersHammer) {
                        if (entity.isSneaking()) {
                            if (!this.isDead && !this.worldObj.isRemote) {
                                dropItem = true;
                            }                        
                        } else {
                            setNextIcon();
                        }              
                    } else {
                        if (!this.isDead && !this.worldObj.isRemote) {
                            dropItem = true;
                        }   
                    }
                    
                } else if (entityPlayer.capabilities.isCreativeMode) {
                    
                    dropItem = true; 
                    
                }
    
            }
            
            playTileSound();
            
            if (dropItem)
            {
                this.setDead();
                this.setBeenAttacked();
                this.onBroken(damageSource.getEntity());
                return true;
            }
        
        }
        
        return false;
    }
    
    @Override
    /**
     * First layer of player interaction.
     */
    public boolean interactFirst(EntityPlayer entityPlayer)
    {
        if (worldObj.isRemote) {
            
            return true;
            
        } else if (PlayerPermissions.canPlayerEdit(this, (int) Math.floor(posX), (int) Math.floor(posY), (int) Math.floor(posZ), entityPlayer)) {
            
            ItemStack itemStack = entityPlayer.getHeldItem();
            
            if (itemStack != null) {
                
                if (itemStack.getItem() instanceof ICarpentersHammer) {
                    
                    if (entityPlayer.isSneaking()) {
                        rotate();
                    } else {
                        setPrevIcon();
                    }
                    
                    playTileSound();
                    
                } else if (BlockProperties.isDye(itemStack, true)) {
                    
                    setDye(DyeHandler.getDyeName(itemStack));
                    playDyeSound();
                    
                }

                return true;
            }
       
        }
        
        return false;
    }

    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    @Override
    public void moveEntity(double x, double y, double z)
    {
        if (!worldObj.isRemote && !isDead && x * x + y * y + z * z > 0.0D)
        {
            setDead();
            onBroken((Entity)null);
        }
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    @Override
    public void addVelocity(double x, double y, double z)
    {
        if (!worldObj.isRemote && !isDead && x * x + y * y + z * z > 0.0D)
        {
            setDead();
            onBroken((Entity)null);
        }
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        getDataWatcher().addObject(ID_TILE, new String("blank"));
        getDataWatcher().addObject(ID_DYE, new String("dyeWhite"));
        getDataWatcher().addObject(ID_DIR, new Integer(0));
        getDataWatcher().addObject(ID_ROT, new Integer(0));
    }

    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double posX, double posY, double posZ, float yaw, float pitch, int par9)
    {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * returns the bounding box for this entity
     */
    @Override
    public AxisAlignedBB getBoundingBox()
    {
        setBoundingBox();
        return boundingBox;
    }

    @Override
    public float getCollisionBorderSize()
    {
        return 0.0F;
    }

    @Override
    protected boolean shouldSetPosAfterLoading()
    {
        return false;
    }
        
}