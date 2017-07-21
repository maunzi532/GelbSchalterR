package tex;

import area.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class Texturen
{
	protected int accuracy;
	protected final HashMap<String, Textur> lk2 = new HashMap<>();
	public final HashMap<String, BufferedImage> bilder2D = new HashMap<>();
	private final HashMap<String, TexturR> lk3 = new HashMap<>();

	public Texturen(String pack, String texOrdnerName)
	{
		String txtDir = texOrdnerName + File.separator + pack;
		try
		{
			String settings = new String(Files.readAllBytes(new File(txtDir + File.separator + "desc").toPath()), Charset.forName("UTF-8"));
			settings = settings.replaceAll("\\s", "");
			accuracy = Integer.parseInt(settings);
		}catch(Exception e)
		{
			System.out.println("Fehler bei Texturen: Textdatei fehlt bzw fehlerhaft");
			e.printStackTrace();
			System.exit(1);
		}
		File[] ordner = new File(txtDir).listFiles();
		if(ordner == null)
		{
			System.out.println("Fehler beim lesen des Texturenordners");
			System.exit(3);
		}
		for(int i = 0; i < ordner.length; i++)
			if(ordner[i].isDirectory())
				neueTextur(ordner[i].listFiles(), ordner[i]);
			else if(ordner[i].getName().endsWith(".png"))
				bilder2D.put(ordner[i].getName().substring(0, ordner[i].getName().lastIndexOf('.')), U.readImage(ordner[i]));
	}

	protected void neueTextur(File[] files, File dir)
	{
		int kmin = Integer.MAX_VALUE;
		int kmax = Integer.MIN_VALUE;
		if(files != null)
			for(int j = 0; j < files.length; j++)
			{
				String filename = files[j].getName();
				int noSuffix = Integer.parseInt(filename.substring(0, filename.length() - 4));
				if(kmin > noSuffix)
					kmin = noSuffix;
				if(kmax < noSuffix)
					kmax = noSuffix;
			}
		BufferedImage[] im = new BufferedImage[kmax - kmin + 1];
		boolean rauf = false;
		BufferedImage last = null;
		for(int q = 0; !rauf || q <= kmax; q += rauf ? 1 : -1)
		{
			if(!rauf && q < kmin)
			{
				q = 0;
				rauf = true;
				last = null;
			}
			BufferedImage ov = U.readImage(new File(dir.getPath() + File.separator + q + ".png"));
			if(ov != null)
				last = ov;
			im[q - kmin] = last;
		}
		lk2.put(dir.getName(), new Textur(kmax, kmin, im));
	}

	public void placeAll2(Graphics2D gd, ArrayList<Render>[][] renders, int xw, int yw)
	{
		if(Shift.tile <= 0 || Shift.th <= 0)
			return;
		for(int iy = 0; iy < yw; iy++)
			for(int ix = 0; ix < xw; ix++)
			{
				if(renders[iy][ix].isEmpty())
					continue;
				StringBuilder sb = new StringBuilder();
				sb.append(Shift.tile).append('-').append(Shift.th);
				boolean rerender = false;
				for(Render re : renders[iy][ix])
				{
					if(re.rerender)
						rerender = true;
					sb.append('-').append(re.what).append(re.text).append(re.height);
				}
				String s1 = sb.toString();
				if(rerender)
					Shift.place4(gd, placeThese(renders[iy][ix]), ix, iy);
				else
				{
					if(!lk3.containsKey(s1))
						lk3.put(s1, placeThese(renders[iy][ix]));
					Shift.place4(gd, lk3.get(s1), ix, iy);
				}
			}
	}

	public TexturR placeThese(ArrayList<Render> renders)
	{
		int maxH = Integer.MIN_VALUE;
		int minH = Integer.MAX_VALUE;
		for(Render re : renders)
		{
			if(re instanceof Render3)
			{
				Render3 re3 = (Render3) re;
				maxH = Math.max(maxH, (int)((re3.maxh + re3.height + 1) * Shift.th));
				minH = Math.min(minH, (int)((re3.minh + re3.height) * Shift.th));
			}
			else
			{
				Textur tex = gettex(re);
				if(tex != null)
				{
					maxH = Math.max(maxH, ((tex.h_up + (re.height + 1) * accuracy) * Shift.th) / accuracy);
					minH = Math.min(minH, ((tex.h_down + re.height * accuracy) * Shift.th) / accuracy);
				}
			}
		}
		int iw = maxH - minH + Shift.tile;
		BufferedImage vor = new BufferedImage(iw, iw, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D gd = vor.createGraphics();
		TexturR tR = new TexturR(vor, maxH);
		for(int i = minH; i < maxH; i++)
			for(Render re : renders)
			{
				if(re instanceof Render3)
				{
					Render3 re3 = (Render3) re;
					re3.teile(gd, i, Shift.th, maxH, Shift.tile);
				}
				else
				{
					String key = re.what;
					Textur tex = gettex(re);
					int texH = i * accuracy / Shift.th - re.height * accuracy;
					if(tex != null && tex.h_up >= texH && tex.h_down <= texH)
					{
						int heightNo = i * accuracy / Shift.th - re.height * accuracy;
						if(heightNo - tex.h_down < tex.look.length)
						{
							Shift.place3Vor(gd, tex.look[heightNo - tex.h_down], maxH, i);
							if(tex.h_up == texH && re.text != null)
								Shift.placeErsatzTextVor(gd, re.text, maxH, i);
						}
					}
					if(!lk2.containsKey(key) && re.height * accuracy == i * accuracy / Shift.th)
						Shift.placeErsatzTextVor(gd, key, maxH, i);
				}
			}
		return tR;
	}

	private Textur gettex(Render re)
	{
		String key = re.what;
		Textur tex = lk2.get(key);
		if(tex == null && key.length() >= 5 && key.substring(0, 4).equals("Höhe"))
			tex = lk2.get(key.substring(0, 5));
		return tex;
	}
}