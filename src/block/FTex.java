package block;

import java.awt.*;
import java.awt.image.*;
import tex.*;

public class FTex extends Texturen
{
	public FTex(String pack, String texOrdnerName)
	{
		super(pack, texOrdnerName);
		for(int i = 0; i < 10; i++)
		{
			farbTex1(i, false);
			farbTex1(i, true);
		}
		farbTexS(false);
		farbTexS(true);
		drehTex1("Pfeil");
		drehTex1("Einhauwand");
		drehTex1("EinhauwandB");
	}

	private void drehTex1(String vonname)
	{
		if(!lk2.containsKey(vonname))
			return;
		Textur von = lk2.get(vonname);
		for(int r = 0; r < 4; r++)
		{
			String toname = vonname + r;
			if(!lk2.containsKey(toname))
				lk2.put(toname, new Textur(von.h_up, von.h_down, drehTex(von.look, r)));
		}
	}

	private void farbTex1(int i, boolean in)
	{
		String vonname = "Höhe" + i + (in ? "I" : "");
		if(!lk2.containsKey(vonname))
			return;
		Textur von = lk2.get(vonname);
		for(char farb = 'A'; farb <= 'F'; farb++)
		{
			String toname = vonname + farb;
			if(!lk2.containsKey(toname))
				lk2.put(toname, new Textur(von.h_up, von.h_down, farbTex(von.look, farb, in)));
		}
	}

	private void farbTexS(boolean g)
	{
		String vonname = "Schalter" + (g ? "1" : "");
		if(!lk2.containsKey(vonname))
			return;
		Textur von = lk2.get(vonname);
		for(char farb = 'A'; farb <= 'F'; farb++)
		{
			String toname = vonname + farb;
			if(!lk2.containsKey(toname))
				lk2.put(toname, new Textur(von.h_up, von.h_down, farbTex(von.look, farb, true)));
		}
	}

	private BufferedImage[] drehTex(BufferedImage[] std, int r)
	{
		BufferedImage[] toR = new BufferedImage[std.length];
		for(int j = 0; j < std.length; j++)
		{
			BufferedImage td = std[j];
			BufferedImage n = new BufferedImage(td.getWidth(), td.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
			Graphics2D gd = n.createGraphics();
			gd.rotate(r * Math.PI / 2, td.getWidth() / 2, td.getHeight() / 2);
			gd.drawRenderedImage(td, null);
			toR[j] = n;
		}
		return toR;
	}

	private BufferedImage[] farbTex(BufferedImage[] std, char farb, boolean trans)
	{
		BufferedImage[] toR = new BufferedImage[std.length];
		for(int j = 0; j < std.length; j++)
		{
			BufferedImage td = std[j];
			BufferedImage n = new BufferedImage(td.getWidth(), td.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
			if(trans)
			{
				WritableRaster ar = td.getRaster();
				for(int ix = 0; ix < n.getWidth(); ix++)
					for(int iy = 0; iy < n.getHeight(); iy++)
						if(ar.getPixel(ix, iy, (int[])null)[3] > 0)
							n.setRGB(ix, iy, BlockLab.farben[farb - 'A'].getRGB());
			}
			else
			{
				Graphics2D gd = n.createGraphics();
				gd.drawImage(td, 0, 0, null);
				gd.setColor(BlockLab.farben[farb - 'A']);
				gd.fillRect(0, 0, td.getWidth(), td.getHeight());
			}
			toR[j] = n;
		}
		return toR;
	}
}