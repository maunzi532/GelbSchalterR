package block;

import java.awt.*;
import java.awt.image.*;
import tex.*;

public class FTex extends Texturen
{
	public static final Color[] farben = new Color[]
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
					new Color(31, 31, 255),
					new Color(255, 31, 31)
			};

	public FTex(String pack, String texOrdnerName)
	{
		super(pack, texOrdnerName);
		for(int i = 0; i < 10; i++)
			farbTex1("Höhe" + String.valueOf(i), false, 'A',  farben);
		farbTex1("HöheI", true, 'A',  farben);
		farbTex1("Schalter", true, 'A',  farben);
		farbTex1("Schalter1", true, 'A',  farben);
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
				for(int ix = 0; ix < n.getWidth(); ix++)
					for(int iy = 0; iy < n.getHeight(); iy++)
						if(ar.getPixel(ix, iy, (int[])null)[3] > 0)
							n.setRGB(ix, iy, farbe.getRGB());
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
}