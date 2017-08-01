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
		{
			treppentex1("Höhe" + i);
			treppentex1("Höhe" + i + 'G');
			lifttex(i, "");
			lifttex(i, "G");
		}
	}

	private void treppentex1(String vonname)
	{
		if(!lk2.containsKey(vonname))
			return;
		Textur von = lk2.get(vonname);
		for(int j = 1; j <= 4; j++)
			lk2.put(vonname + 'T' + j, new Textur(von.h_up, von.h_down - accuracy, treppentex(von.look, j)));
		BufferedImage[] look2 = new BufferedImage[von.look.length];
		System.arraycopy(von.look, 0, look2, 0, von.look.length);
		BufferedImage td = von.look[von.look.length - 1];
		if(td != null)
		{
			BufferedImage td2 = new BufferedImage(td.getWidth(), td.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
			td2.getGraphics().drawImage(td, 0, 0, null);
			td2.getGraphics().drawImage(bilder2D.get("R"), 0, 0, td.getWidth(), td.getHeight(), null);
			look2[look2.length - 1] = td2;
		}
		for(int j = 1; j <= 4; j++)
			lk2.put(vonname + 'R' + j, new Textur(von.h_up, von.h_down - accuracy, treppentex(look2, j)));
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

	private void lifttex(int h, String g)
	{
		String vonname = "Höhe" + h + g;
		if(!lk2.containsKey(vonname))
			return;
		Textur von = lk2.get(vonname);
		BufferedImage[] look2 = new BufferedImage[von.look.length];
		BufferedImage[] look3 = new BufferedImage[von.look.length + accuracy];
		System.arraycopy(von.look, 0, look2, 0, von.look.length);
		System.arraycopy(von.look, 0, look3, 0, von.look.length);
		BufferedImage td = von.look[von.look.length - 1];
		if(td != null)
		{
			BufferedImage td2 = new BufferedImage(td.getWidth(), td.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
			td2.getGraphics().drawImage(td, 0, 0, null);
			td2.getGraphics().drawImage(bilder2D.get("L"), 0, 0, td.getWidth(), td.getHeight(), null);
			look2[look2.length - 1] = td2;
			for(int i = 0; i <= accuracy; i++)
				look3[look3.length - 1 - i] = td2;
		}
		lk2.put(vonname + 'L', new Textur(von.h_up, von.h_down, look2));
		lk2.put("Höhe" + (h + 1) + g + 'F', new Textur(von.h_up, von.h_down - accuracy, look3));
	}
}