package gelb;

import java.awt.*;
import java.awt.image.*;
import tex.*;

public class GTex extends Texturen
{
	public GTex(String pack, String texOrdnerName)
	{
		super(pack, texOrdnerName);
		for(int i = 0; i < 10; i++)
			if(lk2.containsKey("Höhe" + i))
				for(int j = 0; j < 4; j++)
					treppentex1(i, j % 2 == 1, j / 2 > 0);
		drehTex1("Symbol");
	}

	private void treppentex1(int i, boolean r, boolean gelb)
	{
		String vonname = "Höhe" + i + (gelb ? "G" : "") + (r ? "R" : "T");
		if(!lk2.containsKey(vonname))
			return;
		Textur von = lk2.get(vonname);
		String t = (gelb ? "G" : "") + (r ? "R" : "T");
		for(int j = 1; j <= 4; j++)
		{
			String toname = "Höhe" + i + t + j;
			if(!lk2.containsKey(toname))
				lk2.put(toname, new Textur(von.h_up, von.h_down - accuracy, treppentex(von.look, j)));
		}
	}

	private BufferedImage[] treppentex(BufferedImage[] std, int j1)
	{
		BufferedImage[] toR = new BufferedImage[std.length + accuracy];
		System.arraycopy(std, 0, toR, 0, std.length);
		BufferedImage td = std[std.length - 1];
		int w = td.getWidth(null);
		int h = td.getHeight(null);
		for(int j = 0; j < accuracy; j++)
		{
			BufferedImage ti = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
			Graphics2D gt = ti.createGraphics();
			int tw = w * (accuracy - j) / accuracy;
			int th = h * (accuracy - j) / accuracy;
			int e1 = j1 == 2 ? w - tw : 0;
			int e2 = j1 == 3 ? h - th : 0;
			int e3 = j1 == 4 ? tw : w;
			int e4 = j1 == 1 ? th : h;
			gt.drawImage(td, e1, e2, e3, e4, e1, e2, e3, e4, null);
			toR[std.length + j] = ti;
		}
		return toR;
	}
}