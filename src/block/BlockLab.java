package block;

import area.*;
import block.item.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.*;
import laderLC.*;
import tex.*;

public class BlockLab extends Area
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
	public static final int limit = 6;

	public BFeld[][] feld;
	final ArrayList<int[][]> geht2 = new ArrayList<>();
	final ArrayList<int[][]> gehtTasten = new ArrayList<>();
	BlockLies bl;
	public char farbeAktuell = 'A';
	int dias;
	public int hoeheA;
	int akItem;
	final ArrayList<Item> items = new ArrayList<>();
	int lrm;
	int oum;
	public int richtung;
	public boolean pfadmodus;
	public int enhkey;

	@Override
	public void readFL(String c1, boolean se2n)
	{
		bl = new BlockLies();
		if(c1.charAt(0) == '{')
		{
			ErrorVial vial = bl.lies(c1, se2n);
			if(vial.errors())
				System.out.println(vial.errors);
		}
		else try
		{
			bl.liesA(c1);
		}catch(Exception e)
		{
			e.printStackTrace();
			System.exit(273);
		}
	}

	public void reset()
	{
		farbeAktuell = 'A';
		dias = 0;
		items.clear();
		items.add(new Movement().kopie(this));
		xw = bl.se[0][0];
		yw = bl.se[0][1];
		xp = bl.se[1][0];
		yp = bl.se[1][1];
		feld = new BFeld[yw][xw];
		for(int yi = 0; yi < yw; yi++)
			for(int xi = 0; xi < xw; xi++)
				feld[yi][xi] = bl.feld[yi][xi].copy(this);
		hoeheA = feld[yp][xp].hoehe;
		richtung = 3;
		SRD.reset(this);
	}

	@Override
	public Feld feld(int y, int x)
	{
		return feld[y][x];
	}

	@Override
	public int spielerHoehe()
	{
		return hoeheA;
	}

	public void setRichtung(int r1)
	{
		richtung = r1;
		SRD.setRichtung(r1);
	}

	@Override
	public void checkFields()
	{
		geht = new int[yw][xw];
		geht2.clear();
		gehtTasten.clear();
		int disable = 0;
		for(Item item : items)
			if(item.disable > disable)
				disable = item.disable;
		for(int i = 0; i < items.size(); i++)
		{
			int[][] geht3 = new int[yw][xw];
			int[][] gehtT3 = new int[5][3];
			if(items.get(i).enabled(disable))
				items.get(i).setzeOptionen(xp, yp, hoeheA, geht3, gehtT3);
			geht2.add(geht3);
			gehtTasten.add(gehtT3);
			for(int iy = 0; iy < geht.length; iy++)
				for(int ix = 0; ix < geht[iy].length; ix++)
					if(geht3[iy][ix] > 0)
						geht[iy][ix] = 1;
		}
	}

	@Override
	public boolean moveX(boolean nichtMap)
	{
		if(nichtMap)
		{
			if(SIN.mfokusX >= 1 && TA.take[201] == 2)
			{
				if(items.size() > 4)
				{
					int y1 = SIN.mfokusY * 2 + SIN.mfokusX - 1;
					if(y1 < items.size())
						akItem = y1;
				}
				else
				{
					int y1 = SIN.mfokusY / 2;
					if(y1 < items.size())
						akItem = y1;
				}
			}
			return false;
		}
		if(TA.take[37] <= 0 || TA.take[39] <= 0)
		{
			if(TA.take[37] > 0 && lrm > -limit)
				lrm--;
			if(TA.take[39] > 0 && lrm < limit)
				lrm++;
		}
		if(TA.take[37] == 2 && TA.take[39] == 2)
			lrm = 0;
		else if(TA.take[37] == 2)
			lrm = -limit;
		else if(TA.take[39] == 2)
			lrm = limit;
		if(TA.take[38] <= 0 || TA.take[40] <= 0)
		{
			if(TA.take[38] > 0 && oum > -limit)
				oum--;
			if(TA.take[40] > 0 && oum < limit)
				oum++;
		}
		if(TA.take[38] == 2 && TA.take[40] == 2)
			oum = 0;
		else if(TA.take[38] == 2)
			oum = -limit;
		else if(TA.take[40] == 2)
			oum = limit;
		int code = -1;
		if(TA.take[32] > 0)
			code = 0;
		else if(TA.take[37] > 0 && lrm <= -limit)
		{
			code = 1;
			lrm = -1;
		}
		else if(TA.take[38] > 0 && oum <= -limit)
		{
			code = 2;
			oum = -1;
		}
		else if(TA.take[39] > 0 && lrm >= limit)
		{
			code = 3;
			lrm = 1;
		}
		else if(TA.take[40] > 0 && oum >= limit)
		{
			code = 4;
			oum = 1;
		}
		if(code >= 0)
		{
			int i;
			for(i = 0; i < items.size(); i++)
			{
				int i2;
				if(i == 0)
					i2 = akItem;
				else if(i <= akItem)
					i2 = i - 1;
				else
					i2 = i;
				if(items.get(i2).benutze(gehtTasten.get(i2), code))
					break;
			}
			return i < items.size();
		}
		else if(TA.take[201] == 2)
		{
			int i;
			for(i = 0; i < items.size(); i++)
			{
				int i2;
				if(i == 0)
					i2 = akItem;
				else if(i <= akItem)
					i2 = i - 1;
				else
					i2 = i;
				if(items.get(i2).benutze(geht2.get(i2), SIN.fokusX, SIN.fokusY))
					break;
			}
			return i < items.size();
		}
		else if(SIN.cheatmode)
		{
			if(TA.take[112] == 2 && SIN.fokusX >= 0)
			{
				String alt = feld[SIN.fokusY][SIN.fokusX].speichern();
				Object neu = JOptionPane.showInputDialog(null, null, null, JOptionPane.QUESTION_MESSAGE, null, null, alt);
				if(neu instanceof String)
				{
					BFeld nf = new BFeld();
					nf.liesDirekt((String) neu);
					bl.feld[SIN.fokusY][SIN.fokusX] = nf;
					feld[SIN.fokusY][SIN.fokusX] = nf.copy(this);
				}
			}
			if(TA.take[113] == 2 || TA.take[114] == 2)
			{
				int p = 0;
				if(TA.take[113] == 2)
					p--;
				if(TA.take[114] == 2)
					p++;
				if(p != 0)
				{
					if(TA.take[16] > 0 || pfadmodus)
					{
						hoeheA += p;
						if(hoeheA < 0)
							hoeheA = 0;
						if(pfadmodus)
							angleichen();
					}
					else if(SIN.fokusX >= 0)
					{
						BFeld f1 = bl.feld[SIN.fokusY][SIN.fokusX];
						f1.hoehe += p;
						if(f1.hoehe < 0)
							f1.hoehe = 0;
						feld[SIN.fokusY][SIN.fokusX] = f1.copy(this);
					}
				}
			}
			if(TA.take[115] == 2)
			{
				pfadmodus = !pfadmodus;
				if(pfadmodus)
					angleichen();
			}
			if(TA.take[116] == 2 && SIN.fokusX >= 0)
			{
				BFeld f1 = bl.feld[SIN.fokusY][SIN.fokusX];
				if(f1.schalter != 'n')
				{
					f1.schalter = plusfarbe(f1.schalter);
					feld[SIN.fokusY][SIN.fokusX] = f1.copy(this);
				}
				else if(f1.blockFarbe != 'n')
				{
					f1.blockFarbe = plusfarbe(f1.blockFarbe);
					feld[SIN.fokusY][SIN.fokusX] = f1.copy(this);
				}
				else
					farbeAktuell = plusfarbe(farbeAktuell);
			}
			if(TA.take[117] == 2)
			{
				if(TA.take[16] > 0)
					dias--;
				else
				{
					enhkey--;
					if(enhkey < 0)
						enhkey = BFeld.maxenh;
				}
			}
			if(TA.take[118] == 2)
			{
				if(TA.take[16] > 0)
					dias++;
				else
				{
					enhkey++;
					if(enhkey > BFeld.maxenh)
						enhkey = 0;
				}
			}
			if(TA.take[119] == 2 && SIN.fokusX >= 0)
			{
				BFeld f1 = bl.feld[SIN.fokusY][SIN.fokusX];
				if(enhkey == 0)
				{
					f1 = new BFeld(f1.hoehe);
					bl.feld[SIN.fokusY][SIN.fokusX] = f1;
				}
				f1.enhance(this, enhkey);
				feld[SIN.fokusY][SIN.fokusX] = f1.copy(this);
			}
		}
		return false;
	}

	public void angleichen()
	{
		bl.feld[yp][xp].hoehe = hoeheA;
		feld[yp][xp] = bl.feld[yp][xp].copy(this);
	}

	public static char plusfarbe(char farbe)
	{
		if(farbe == 'F')
			return 'A';
		return (char)(farbe + 1);
	}

	public void speichern()
	{
		JFileChooser fc = new JFileChooser(new File("saves"));
		boolean sh = TA.take[16] > 0;
		if(fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			String s;
			if(sh)
				s = bl.speichern(xp, yp);
			else
				s = bl.speichern();
			try
			{
				Files.write(fc.getSelectedFile().toPath(), s.getBytes(Charset.forName("UTF-8")));
			}catch(IOException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void rahmen(Graphics2D gd, Texturen tex, int w1, int h)
	{
		int ht = h / 10;
		gd.setColor(Color.BLACK);
		gd.fillRect(w1, 0, ht * 3, h);
		gd.setColor(new Color(BlockLab.farben[farbeAktuell - 'A'].getRGB()));
		gd.fillRect(w1, 0, ht, h);
		gd.setColor(Color.BLUE);
		gd.fillRect(w1, ht * (10 - dias), ht, h);
		gd.setColor(Color.WHITE);
		gd.drawRect(w1, 0, ht - 1, h - 1);
		gd.setColor(Color.RED);
		if(items.size() <= 4)
		{
			for(int i = 0; i < items.size(); i++)
				gd.drawImage(tex.bilder2D.get(items.get(i).bildname()), w1 + ht, ht * 2 * i, ht * 2, ht * 2, null);
			gd.drawRect(w1 + ht, ht * 2 * akItem, ht * 2 - 1, ht * 2 - 1);
		}
		else
		{
			for(int i = 0; i < items.size(); i++)
				gd.drawImage(tex.bilder2D.get(items.get(i).bildname()), w1 + ht + (i % 2) * ht, ht * (i / 2), ht, ht, null);
			gd.drawRect(w1 + ht + (akItem % 2) * ht, ht * (akItem / 2), ht - 1, ht - 1);
		}
		if(SIN.cheatmode && Shift.tile > 0)
		{
			renders = new ArrayList<>();
			if(enhkey == 0)
			{
				xcp = 0;
				ycp = 0;
				addw("Löscher");
			}
			else if(SIN.fokusX >= 0)
			{
				xcp = SIN.fokusX;
				ycp = SIN.fokusY;
				BFeld fn = bl.feld[SIN.fokusY][SIN.fokusX].copy(this);
				fn.enhance(this, enhkey);
				fn.addToRender(this, false, -1, -1);
			}
			gd.drawImage(tex.placeThese(renders).img, w1 + ht, ht * 2 * 4, ht * 2, ht * 2, null);
		}
		SRD.tick(this);
	}
}