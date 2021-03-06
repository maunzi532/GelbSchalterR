package tex;

import area.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import shift.*;

public class Texturen
{
	protected int accuracy;
	protected final HashMap<String, Textur> lk2 = new HashMap<>();
	public final HashMap<String, BufferedImage> bilder2D = new HashMap<>();
	private final HashMap<String, TexturR> lk3 = new HashMap<>();

	protected Texturen(String pack, String texOrdnerName)
	{
		String txtDir = texOrdnerName + File.separator + pack;
		try
		{
			String settings = Lader7.textSavedata(txtDir + File.separator + "desc");
			settings = settings.replaceAll("\\s", "");
			accuracy = Integer.parseInt(settings);
		}catch(Exception e)
		{
			System.out.println("Fehler bei Texturen: Textdatei fehlt bzw fehlerhaft");
			e.printStackTrace();
			System.exit(1);
		}
		File[] ordner = Lader7.jarLocation.toPath().resolve(txtDir).toFile().listFiles();
		if(ordner == null)
		{
			System.out.println("Fehler beim lesen des Texturenordners");
			System.exit(3);
		}
		for(int i = 0; i < ordner.length; i++)
			if(ordner[i].isDirectory())
				neueTextur(ordner[i].listFiles(), ordner[i]);
			else if(ordner[i].getName().endsWith(".png"))
				bilder2D.put(ordner[i].getName().substring(0, ordner[i].getName().lastIndexOf('.')),
						Lader7.imageSavedata(ordner[i].getPath()));
	}

	private void neueTextur(File[] files, File dir)
	{
		try
		{
			boolean dreh = false;
			int autoh = -1;
			int kmin = Integer.MAX_VALUE;
			int kmax = Integer.MIN_VALUE;
			ArrayList<TBild> tb = new ArrayList<>();
			for(int j = 0; j < files.length; j++)
			{
				String filename = files[j].getName();
				if(filename.equals("dreh"))
					dreh = true;
				if(filename.startsWith("autoh"))
					autoh = Integer.parseInt(filename.substring(5));
				if(!filename.endsWith(".png"))
					continue;
				String[] ft = filename.substring(0, filename.lastIndexOf('.')).split("_");
				int min = Integer.parseInt(ft[0]);
				int max = Integer.parseInt(ft[ft.length > 1 ? 1 : 0]);
				tb.add(new TBild(files[j], min, max));
				if(kmin > min)
					kmin = min;
				if(kmax < max)
					kmax = max;
			}
			BufferedImage[] im = new BufferedImage[kmax - kmin + 1];
			for(TBild tb1 : tb)
				for(int i = tb1.min - kmin; i <= tb1.max - kmin; i++)
					im[i] = tb1.b;
			lk2.put(dir.getName(), new Textur(kmax, kmin, im));
			if(dreh)
				drehTex1(dir.getName());
			if(autoh >= 0)
				autoh1(dir.getName(), autoh);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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

	private void autoh1(String vonname, int keep)
	{
		if(!lk2.containsKey(vonname))
			return;
		Textur von = lk2.get(vonname);
		for(int h = 0; h < 10; h++)
		{
			String toname = vonname + h;
			if(!lk2.containsKey(toname))
			{
				BufferedImage[] auto = new BufferedImage[accuracy * h + keep];
				if(auto.length <= von.look.length)
					System.arraycopy(von.look, von.look.length - auto.length, auto, 0, auto.length);
				else
				{
					int vll2 = 0 - von.h_down;
					int vll3 = von.h_up;
					System.arraycopy(von.look, 0, auto, 0, vll2);
					System.arraycopy(von.look, von.look.length - vll3, auto, auto.length - vll3, vll3);
					for(int i = vll2; i < auto.length - vll3; i++)
						auto[i] = von.look[vll2];
				}
				lk2.put(toname, new Textur(accuracy * h + keep, 0, auto));
			}
		}
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
					sb.append(re.sth ? '-' : '_').append(re.what).append(re.height);
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
		{
			gd.setClip(maxH - i, maxH - i, maxH - i + Shift.tile, maxH - i + Shift.tile);
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
							if(re.sth)
								place3Vor(gd, tex.look[heightNo - tex.h_down], maxH, i);
							else if(tex.h_up == texH)
								placeTextVor(gd, re.what, maxH, i);
						}
					}
					if(!lk2.containsKey(key) && re.height * accuracy == i * accuracy / Shift.th)
						placeTextVor(gd, key, maxH, i);
				}
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

	private static void place3Vor(Graphics2D gd, Image im, int sh, int h)
	{
		gd.drawImage(im, sh - h, sh - h, Shift.tile, Shift.tile, null);
	}

	private static void placeTextVor(Graphics2D gd, String s, int sh, int h)
	{
		gd.setFont(new Font("Consolas", Font.PLAIN, (int)(Shift.tile * 1.5f / s.length())));
		int fh2 = gd.getFontMetrics().getHeight() / 2;
		gd.setColor(Color.WHITE);
		gd.drawString(s, sh - h, sh - h + Shift.tile / 2 + fh2);
	}
}