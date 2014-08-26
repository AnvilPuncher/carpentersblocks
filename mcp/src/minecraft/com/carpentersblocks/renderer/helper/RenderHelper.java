package com.carpentersblocks.renderer.helper;

import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHelper extends VertexHelper {

    private static boolean  rotationOverride = false;
    private static int      rotation;
    private static double   uMin;
    private static double   uMax;
    private static double   vMin;
    private static double   vMax;
    protected static double xMin;
    protected static double xMax;
    protected static double yMin;
    protected static double yMax;
    protected static double zMin;
    protected static double zMax;
    protected static double uTL;
    protected static double vTL;
    protected static double uBL;
    protected static double vBL;
    protected static double uBR;
    protected static double vBR;
    protected static double uTR;
    protected static double vTR;

    public static double renderOffset;

    public static void setOffset(double offset)
    {
        renderOffset = offset;
    }

    public static void clearOffset()
    {
        renderOffset = 0.0D;
    }

    public static void setTextureRotationOverride(int in_rotation)
    {
        rotationOverride = true;
        rotation = in_rotation;
    }

    public static void clearTextureRotationOverride()
    {
        rotationOverride = false;
    }

    /**
     * Sets block bounds for rendering without a RenderBlocks object.
     */
    public static void setBounds(double in_xMin, double in_yMin, double in_zMin, double in_xMax, double in_yMax, double in_zMax)
    {
        xMin = in_xMin;
        yMin = in_yMin;
        zMin = in_zMin;
        xMax = in_xMax;
        yMax = in_yMax;
        zMax = in_zMax;
    }

    /**
     * Sets draw mode in tessellator.
     */
    public static void startDrawing(int drawMode)
    {
        Tessellator.instance.draw();
        Tessellator.instance.startDrawing(drawMode);
    }

    /**
     * Sets UV coordinates for each corner based on side rotation.
     */
    private static void setCornerUV(double t_uTL, double t_vTL, double t_uBL, double t_vBL, double t_uBR, double t_vBR, double t_uTR, double t_vTR)
    {
        uTL = t_uTL;
        vTL = t_vTL;
        uBL = t_uBL;
        vBL = t_vBL;
        uBR = t_uBR;
        vBR = t_vBR;
        uTR = t_uTR;
        vTR = t_vTR;
    }

    /**
     * Will populate render bounds and icon u, v translations.
     */
    protected static void prepareRender(RenderBlocks renderBlocks, ForgeDirection side, double x, double y, double z, Icon icon)
    {
        /* Enforce default floating icons */

        if (icon == BlockGrass.getIconSideOverlay() || icon.getIconName().contains("overlay/overlay_") && icon.getIconName().endsWith("_side")) {
            setFloatingIcon();
        }

        /* Set render bounds with offset. */

        xMin = x + renderBlocks.renderMinX - renderOffset;
        xMax = x + renderBlocks.renderMaxX + renderOffset;
        yMin = y + renderBlocks.renderMinY - renderOffset;
        yMax = y + renderBlocks.renderMaxY + renderOffset;
        zMin = z + renderBlocks.renderMinZ - renderOffset;
        zMax = z + renderBlocks.renderMaxZ + renderOffset;

        /* Set u, v for icon with rotation. */

        if (!rotationOverride) {
            switch (side) {
                case DOWN:
                    rotation = renderBlocks.uvRotateBottom;
                    break;
                case UP:
                    rotation = renderBlocks.uvRotateTop;
                    break;
                case NORTH:
                    rotation = renderBlocks.uvRotateNorth;
                    break;
                case SOUTH:
                    rotation = renderBlocks.uvRotateSouth;
                    break;
                case WEST:
                    rotation = renderBlocks.uvRotateWest;
                    break;
                case EAST:
                    rotation = renderBlocks.uvRotateEast;
                    break;
                default: {}
            }
        }

        switch (side) {

            case DOWN:

                switch (rotation) {

                    case 0:

                        uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
                        uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
                        vMin = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
                        vMax = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);

                        setCornerUV(uMax, vMax, uMax, vMin, uMin, vMin, uMin, vMax);

                        break;

                    case 1:

                        uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxZ * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinZ * 16.0D);
                        vMin = icon.getInterpolatedV(renderBlocks.renderMinX * 16.0D);
                        vMax = icon.getInterpolatedV(renderBlocks.renderMaxX * 16.0D);

                        setCornerUV(uMin, vMax, uMax, vMax, uMax, vMin, uMin, vMin);

                        break;

                    case 2:

                        uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMinX * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxX * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinZ * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxZ * 16.0D);

                        setCornerUV(uMax, vMax, uMax, vMin, uMin, vMin, uMin, vMax);

                        break;

                    case 3:

                        uMin = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
                        uMax = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinX * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxX * 16.0D);

                        setCornerUV(uMin, vMax, uMax, vMax, uMax, vMin, uMin, vMin);

                        break;

                }

                break;

            case UP:

                switch (rotation) {

                    case 0:

                        uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
                        uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
                        vMin = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
                        vMax = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);

                        setCornerUV(uMax, vMax, uMax, vMin, uMin, vMin, uMin, vMax);

                        break;

                    case 1:

                        uMin = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
                        uMax = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinX * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxX * 16.0D);

                        setCornerUV(uMin, vMax, uMax, vMax, uMax, vMin, uMin, vMin);

                        break;

                    case 2:

                        uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMinX * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxX * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinZ * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxZ * 16.0D);

                        setCornerUV(uMax, vMax, uMax, vMin, uMin, vMin, uMin, vMax);

                        break;

                    case 3:

                        uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxZ * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinZ * 16.0D);
                        vMin = icon.getInterpolatedV(renderBlocks.renderMinX * 16.0D);
                        vMax = icon.getInterpolatedV(renderBlocks.renderMaxX * 16.0D);

                        setCornerUV(uMin, vMax, uMax, vMax, uMax, vMin, uMin, vMin);

                        break;

                }

                break;

            case NORTH:

                switch (rotation) {

                    case 0:

                        uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxX * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinX * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - (floatingIcon ? 1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY) : renderBlocks.renderMinY) * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - (floatingIcon ? 1.0D : renderBlocks.renderMaxY) * 16.0D);

                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);

                        break;

                    case 1:

                        uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(renderBlocks.renderMaxX * 16.0D);
                        vMax = icon.getInterpolatedV(renderBlocks.renderMinX * 16.0D);

                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);

                        break;

                    case 2:

                        uMin = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
                        uMax = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
                        vMin = icon.getInterpolatedV(renderBlocks.renderMinY * 16.0D);
                        vMax = icon.getInterpolatedV(renderBlocks.renderMaxY * 16.0D);

                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);

                        break;

                    case 3:

                        uMin = icon.getInterpolatedU(renderBlocks.renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(renderBlocks.renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxX * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMinX * 16.0D);

                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);

                        break;

                }

                break;

            case SOUTH:

                switch (rotation) {

                    case 0:

                        uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
                        uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - (floatingIcon ? 1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY) : renderBlocks.renderMinY) * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - (floatingIcon ? 1.0D : renderBlocks.renderMaxY) * 16.0D);

                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);

                        break;

                    case 1:

                        uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinX * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxX * 16.0D);

                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);

                        break;

                    case 2:

                        uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMinX * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxX * 16.0D);
                        vMin = icon.getInterpolatedV(renderBlocks.renderMinY * 16.0D);
                        vMax = icon.getInterpolatedV(renderBlocks.renderMaxY * 16.0D);

                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);

                        break;

                    case 3:

                        uMin = icon.getInterpolatedU(renderBlocks.renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(renderBlocks.renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(renderBlocks.renderMinX * 16.0D);
                        vMax = icon.getInterpolatedV(renderBlocks.renderMaxX * 16.0D);

                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);

                        break;

                }

                break;

            case WEST:

                switch (rotation) {

                    case 0:

                        uMin = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
                        uMax = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - (floatingIcon ? 1.0D : renderBlocks.renderMaxY) * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - (floatingIcon ? 1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY) : renderBlocks.renderMinY) * 16.0D);

                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);

                        break;

                    case 1:

                        uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinZ * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxZ * 16.0D);

                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);

                        break;

                    case 2:

                        uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMinZ * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxZ * 16.0D);
                        vMin = icon.getInterpolatedV(renderBlocks.renderMinY * 16.0D);
                        vMax = icon.getInterpolatedV(renderBlocks.renderMaxY * 16.0D);

                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);

                        break;

                    case 3:

                        uMin = icon.getInterpolatedU(renderBlocks.renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(renderBlocks.renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
                        vMax = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);

                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);

                        break;

                }

                break;

            case EAST:

                switch (rotation) {

                    case 0:

                        uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxZ * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinZ * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - (floatingIcon ? 1.0D : renderBlocks.renderMaxY) * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - (floatingIcon ? 1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY) : renderBlocks.renderMinY) * 16.0D);

                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);

                        break;

                    case 1:

                        uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);
                        vMax = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);

                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);

                        break;

                    case 2:

                        uMin = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
                        uMax = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
                        vMin = icon.getInterpolatedV(renderBlocks.renderMinY * 16.0D);
                        vMax = icon.getInterpolatedV(renderBlocks.renderMaxY * 16.0D);

                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);

                        break;

                    case 3:

                        uMin = icon.getInterpolatedU(renderBlocks.renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(renderBlocks.renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxZ * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMinZ * 16.0D);

                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);

                        break;

                }

                break;

            default: {}

        }
    }

    /**
     * Renders the given texture to the bottom face of the block. Args: slope, x, y, z, texture
     */
    public static void renderFaceYNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.DOWN, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMin, zMax, uTR, vTR, SOUTHWEST);
        setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
        setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
        setupVertex(renderBlocks, xMax, yMin, zMax, uTL, vTL, SOUTHEAST);
    }

    /**
     * Renders the given texture to the top face of the block. Args: slope, x, y, z, texture
     */
    public static void renderFaceYPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.UP, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
        setupVertex(renderBlocks, xMax, yMax, zMin, uBL, vBL, NORTHEAST);
        setupVertex(renderBlocks, xMin, yMax, zMin, uBR, vBR, NORTHWEST);
        setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
    }

    /**
     * Renders the given texture to the North face of the block.  Args: slope, x, y, z, texture
     */
    public static void renderFaceZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.NORTH, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, TOP_RIGHT   );
    }

    /**
     * Renders the given texture to the South face of the block.  Args: slope, x, y, z, texture
     */
    public static void renderFaceZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.SOUTH, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, TOP_RIGHT   );
    }

    /**
     * Renders the given texture to the West face of the block.  Args: slope, x, y, z, texture
     */
    public static void renderFaceXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.WEST, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, TOP_RIGHT   );
    }

    /**
     * Renders the given texture to the East face of the block.  Args: slope, x, y, z, texture
     */
    public static void renderFaceXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.EAST, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, TOP_RIGHT   );
    }

}
