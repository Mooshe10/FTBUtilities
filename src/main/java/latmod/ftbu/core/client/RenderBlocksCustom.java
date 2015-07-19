package latmod.ftbu.core.client;

import latmod.ftbu.core.CustomBlockAccess;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class RenderBlocksCustom extends RenderBlocks
{
	public AxisAlignedBB fullBlock = AxisAlignedBB.getBoundingBox(0D, 0D, 0D, 1D, 1D, 1D);
	
	public CustomBlockAccess customBlockAccess = null;
	public float customColRed = 1F;
	public float customColGreen = 1F;
	public float customColBlue = 1F;
	
	public void setCustomColor(Integer col)
	{
		if(col == null) customColRed = customColGreen = customColBlue = 1F;
		else
		{
			customColRed = ((col >> 16) & 0xFF) / 255F;
			customColGreen = ((col >> 8) & 0xFF) / 255F;
			customColBlue = ((col >> 0) & 0xFF) / 255F;
		}
	}
	
	public boolean renderStandardBlock(Block b, int x, int y, int z)
	{
		if(customColRed == 1F && customColGreen == 1F && customColBlue == 1F)
			return super.renderStandardBlock(b, x, y, z);
		return renderStandardBlockWithColorMultiplier(b, x, y, z, customColRed, customColGreen, customColBlue);
	}
	
	public void renderBlockSandFalling(Block b, World w, int x, int y, int z, int m)
	{
		super.renderBlockSandFalling(b, w, x, y, z, m);
	}
	
	public void renderStandardBlockIcons(Block b, int x, int y, int z, IIcon[] icons, boolean tileEntity)
	{
		Tessellator tessellator = Tessellator.instance;
		//GL11.glRotatef(90F, 0F, 1F, 0F);
		
		float f = 0.5F;
		float f1 = 1.0F;
		float f2 = 0.8F;
		float f3 = 0.6F;
		
		float[] cols = { f, f1, f2, f2, f3, f3 };
		cols = new float[] { 1F, 1F, 1F, 1F, 1F, 1F };
		
		if(tileEntity)
		{
			tessellator.startDrawingQuads();
			tessellator.setBrightness(b.getMixedBrightnessForBlock(blockAccess, x, y, z));
		}
		
		double off = -0.5D;
		
		for(int i = 0; i < 6; i++)
		{
			tessellator.setColorOpaque_F(cols[i], cols[i], cols[i]);
			renderFace(b, i, off, off, off, (icons == null || icons.length != 6) ? getBlockIcon(b, blockAccess, x, y, z, i) : icons[i]);
		}
		
		if(tileEntity) tessellator.draw();
	}
	
	public void renderBlockAsItem(Block block, int metadata, float f)
	{
		Tessellator tessellator = Tessellator.instance;
		
		GL11.glRotatef(90F, 0F, 1F, 0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		tessellator.setColorOpaque_F(customColRed, customColGreen, customColBlue);
		tessellator.setBrightness(0);
		
		GL11.glColor4f(customColRed, customColGreen, customColBlue, 1F);
		
		for(int i = 0; i < 6; i++)
		{
			ForgeDirection fd = ForgeDirection.VALID_DIRECTIONS[i];
			tessellator.startDrawingQuads();
			tessellator.setNormal(fd.offsetX, fd.offsetY, fd.offsetZ);
			renderFace(block, i, 0D, 0D, 0D, getBlockIconFromSideAndMetadata(block, i, metadata));
			tessellator.draw();
		}
		
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	
	public void renderFace(Block b, int f, double x, double y, double z, IIcon icon)
	{
		if(f < 0 || f > 5);
		else if(f == 0) renderFaceYNeg(b, x, y, z, icon);
		else if(f == 1) renderFaceYPos(b, x, y, z, icon);
		else if(f == 2) renderFaceZNeg(b, x, y, z, icon);
		else if(f == 3) renderFaceZPos(b, x, y, z, icon);
		else if(f == 4) renderFaceXNeg(b, x, y, z, icon);
		else if(f == 5) renderFaceXPos(b, x, y, z, icon);
	}

	public void renderBlockAsItemByRenderType(Block b, int metadata)
	{ super.renderBlockAsItem(b, metadata, 1F); }
	
	public void updateColor()
	{ Tessellator.instance.setColorOpaque_F(customColRed, customColGreen, customColBlue); }
	
	public void setRenderBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
	{ super.setRenderBounds(Math.max(0D, minX), Math.max(0D, minY), Math.max(0D, minZ), Math.min(1D, maxX), Math.min(1D, maxY), Math.min(1D, maxZ)); }
	
	public void setRenderBounds(AxisAlignedBB aabb)
	{ if(aabb != null) super.setRenderBounds(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ); }
	
	public void setFaceBounds(int side, AxisAlignedBB aabb)
	{ setFaceBounds(side, aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ); }
	
	public void setFaceBounds(int side, double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
	{
		if(side == 0) setRenderBounds(minX, minY, minZ, maxX, minY, maxZ);
		else if(side == 1) setRenderBounds(minX, maxY, minZ, maxX, maxY, maxZ);
		else if(side == 2) setRenderBounds(minX, minY, minZ, maxX, maxY, minZ);
		else if(side == 3) setRenderBounds(minX, minY, maxZ, maxX, maxY, maxZ);
		else if(side == 4) setRenderBounds(minX, minY, minZ, minX, maxY, maxZ);
		else if(side == 5) setRenderBounds(maxX, minY, minZ, maxX, maxY, maxZ);
	}
	
	public void expandBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
	{ setRenderBounds(renderMinX - minX, renderMinY - minY, renderMinZ - minZ, renderMaxX + maxX, renderMaxY + maxY, renderMaxZ + maxZ); }
	
	public void expandBounds(double d)
	{ if(d != 0D) expandBounds(d, d, d, d, d, d); }
}