package block;

import java.awt.*;
import java.awt.image.*;
import tex.*;

class FTex extends Texturen
{
	static final Color[] farben = new Color[]
			{
					new Color(191, 31, 31, 127),
					new Color(31, 191, 31, 127),
					new Color(31, 31, 191, 127),
					new Color(255, 191, 63, 127),
					new Color(159, 159, 159, 127),
					new Color(191, 31, 191, 127)
			};

	private static final Color[] symbol = new Color[]
			{
					new Color(255, 191, 191, 127)
			};

	private static final Color[] marker = new Color[]
			{
					new Color(191, 31, 31),
					new Color(31, 191, 31),
					new Color(31, 31, 191),
					new Color(255, 191, 63),
					new Color(159, 159, 159),
					new Color(191, 31, 191)
			};

	FTex(String pack, String texOrdnerName)
	{
		super(pack, texOrdnerName);
		for(int i = 0; i < 10; i++)
			farbTex1("Höhe" + String.valueOf(i), false, 'A', farben);
		farbTex1("HöheI", true, 'A', farben);
		farbTex1("Schalter", true, 'A', farben);
		farbTex1("Schalter1", true, 'A', farben);
		farbTex1("Würfel", true, 'A', marker);
		farbTex1("Würfel1", true, 'A', marker);
		farbTex2("Würfel", true, 'A', marker);
		farbTex2("Würfel1", true, 'A', marker);
		farbTex1("Möglich", true, 'A', marker);
		for(int i = 0; i < 4; i++)
			farbTex1("Symbol" + i, true, 'A', symbol);
		farbTex1("SymbolL", true, 'A', symbol);
	}

	private void farbTex1(String vonname, boolean in, char start, Color[] farben1)
	{
		if(!lk2.containsKey(vonname))
			return;
		Textur von = lk2.get(vonname);
		for(int farb = 0; farb < farben1.length; farb++)
		{
			String toname = vonname + (char)(start + farb);
			if(!lk2.containsKey(toname))
				lk2.put(toname, new Textur(von.h_up, von.h_down, farbTex(von.look, in, farben1[farb])));
		}
	}

	private void farbTex2(String vonname, boolean in, char start, Color[] farben1)
	{
		if(!bilder2D.containsKey(vonname))
			return;
		BufferedImage von = bilder2D.get(vonname);
		for(int farb = 0; farb < farben1.length; farb++)
		{
			String toname = vonname + (char)(start + farb);
			if(!bilder2D.containsKey(toname))
				bilder2D.put(toname, farbTex(new BufferedImage[]{von}, in, farben1[farb])[0]);
		}
	}

	private BufferedImage[] farbTex(BufferedImage[] std, boolean trans, Color farbe)
	{
		BufferedImage[] toR = new BufferedImage[std.length];
		for(int j = 0; j < std.length; j++)
		{
			BufferedImage td = std[j];
			BufferedImage n = new BufferedImage(td.getWidth(), td.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
			if(trans)
			{
				WritableRaster ar = td.getRaster();
				int max = 0;
				for(int ix = 0; ix < n.getWidth(); ix++)
					for(int iy = 0; iy < n.getHeight(); iy++)
					{
						int[] pix = ar.getPixel(ix, iy, (int[]) null);
						if(pix[3] > 0 && pix[0] > max)
							max = pix[0];
					}
				for(int ix = 0; ix < n.getWidth(); ix++)
					for(int iy = 0; iy < n.getHeight(); iy++)
						if(ar.getPixel(ix, iy, (int[])null)[3] > 0)
							n.setRGB(ix, iy, combine(max, ar.getPixel(ix, iy, (int[])null), farbe).getRGB());
			}
			else
			{
				Graphics2D gd = n.createGraphics();
				gd.drawImage(td, 0, 0, null);
				gd.setColor(farbe);
				gd.fillRect(0, 0, td.getWidth(), td.getHeight());
			}
			toR[j] = n;
		}
		return toR;
	}

	private Color combine(int max, int[] von, Color clr)
	{
		int[] clr2 = new int[3];
		for(int i = 0; i < 3; i++)
		{
			if(max <= 0)
				clr2[i] = 255;
			else
				clr2[i] = von[i] * 255 / max;
		}
		int[] mid = new int[3];
		for(int i = 0; i < 3; i++)
		{
			mid[i] = clr2[i] * ((clr.getRGB() >> (8 * (2 - i))) & 255) / 255;
			if(mid[i] > 255)
				mid[i] = 255;
		}
		return new Color(mid[0], mid[1], mid[2], von[3] * clr.getAlpha() / 255);
	}
}