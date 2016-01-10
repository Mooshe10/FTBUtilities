package latmod.ftbu.badges;

import cpw.mods.fml.relauncher.*;
import ftb.lib.client.*;
import latmod.ftbu.mod.FTBU;
import latmod.lib.util.FinalIDObject;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Badge extends FinalIDObject
{
	public static final ResourceLocation defTex = FTBU.mod.getLocation("textures/failed_badge.png");
	
	public static final Badge emptyBadge = new Badge("-", null)
	{
		@SideOnly(Side.CLIENT)
		public ResourceLocation getTexture()
		{ return null; }
	};
	
	// -- //
	
	public final String imageURL;
	private ResourceLocation textureURL = null;
	
	public Badge(String id, String url)
	{
		super(id);
		imageURL = url;
	}
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture()
	{
		if(textureURL == null)
		{
			textureURL = FTBU.mod.getLocation("textures/badges/" + ID);
			FTBLibClient.getDownloadImage(textureURL, imageURL, defTex, null);
		}
		
		return textureURL;
	}
	
	@SideOnly(Side.CLIENT)
	public void onPlayerRender(EntityPlayer ep)
	{
		ResourceLocation texture = getTexture();
		if(texture == null) return;
		
		GlStateManager.pushAttrib();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.enableTexture();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		FTBLibClient.setTexture(texture);
		FTBLibClient.pushMaxBrightness();
		GlStateManager.pushMatrix();
		
		if(ep.isSneaking()) GlStateManager.rotate(25F, 1F, 0F, 0F);
		
		GlStateManager.translate(0.04F, 0.01F, 0.86F);
		
		ItemStack armor = ep.getEquipmentInSlot(3);
		
		if(armor != null && armor.getItem().isValidArmor(armor, 1, ep)) GlStateManager.translate(0F, 0F, -0.0625F);
		
		double s = 0.2D;
		GlStateManager.translate(0F, 0F, -1F);
		GlStateManager.color(1F, 1F, 1F, 1F);

		/*GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0F, 0F); GL11.glVertex3f(0F, 0F, 0F);
		GL11.glTexCoord2f(1F, 0F); GL11.glVertex3f(s, 0F, 0F);
		GL11.glTexCoord2f(1F, 1F); GL11.glVertex3f(s, s, 0F);
		GL11.glTexCoord2f(0F, 1F); GL11.glVertex3f(0F, s, 0F);
		GL11.glEnd();*/
		
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.addVertexWithUV(0D, 0D, 0D, 0D, 0D);
		t.addVertexWithUV(s, 0D, 0D, 1D, 0D);
		t.addVertexWithUV(s, s, 0D, 1D, 1D);
		t.addVertexWithUV(0D, s, 0D, 0D, 1D);
		t.draw();
		
		FTBLibClient.popMaxBrightness();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
}