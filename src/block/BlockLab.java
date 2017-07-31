package block;

import area.*;
import block.item.*;
import block.state.*;
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
	BlockLies bl;

	public char farbeAktuell = 'A';
	public int dias;
	public int hoeheA;

	int akItem;
	public final ArrayList<Item> items = new ArrayList<>();

	int lrm;
	int oum;
	public SRD srd;
	public int richtung;
	public boolean pfadmodus;
	public int enhkey;

	public Stack<BState> states = new Stack<>();

	@Override
	public void start(String input, String texOrdnerName, boolean chm, boolean chs, int tem)
	{
		readFL(input, chs);
		srd = new SRD(this);
		reset();
		Texturen tex = new FTex("BlockLab", texOrdnerName);
		SIN.start(this, tex, chm, tem);
	}

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
		tick = 0;
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
				feld[yi][xi] = BFeld.copy(bl.feld[yi][xi], this);
		hoeheA = feld[yp][xp].hoehe;
		richtung = 3;
		srd.reset(this);
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
		srd.setRichtung(r1);
	}

	@Override
	public void checkFields()
	{
		geht = new int[yw][xw];
		int disable = 0;
		for(Item item : items)
			if(item.disable > disable)
				disable = item.disable;
		for(int i = 0; i < items.size(); i++)
			if(items.get(i).enabled(disable))
			{
				items.get(i).setzeOptionen1(xp, yp, hoeheA, xw, yw);
				for(int iy = 0; iy < geht.length; iy++)
					for(int ix = 0; ix < geht[iy].length; ix++)
						if(items.get(i).g2[iy][ix] > 0)
							geht[iy][ix] = 1;
			}
	}

	@Override
	public boolean moveX(boolean nichtMap)
	{
		if(TA.take[120] == 2)
			states.add(new BState(this));
		if(TA.take[121] == 2)
			if(!states.empty())
			{
				states.pop().charge(this);
				srd.reset2(this);
				SIN.mapview = 0;
				Shift.selectTarget(xp, yp, hoeheA, SIN.kamZoom);
				Shift.instant();
			}
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
				if(items.get(i2).benutze(code, i2 == akItem, false))
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
				if(items.get(i2).benutze(SIN.fokusX, SIN.fokusY, i2 == akItem, false))
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
					feld[SIN.fokusY][SIN.fokusX] = BFeld.copy(nf, this);
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
						LFeld f1 = bl.feld[SIN.fokusY][SIN.fokusX];
						f1.hoehe += p;
						if(f1.hoehe < 0)
							f1.hoehe = 0;
						feld[SIN.fokusY][SIN.fokusX] = BFeld.copy(f1, this);
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
				LFeld f1 = bl.feld[SIN.fokusY][SIN.fokusX];
				if(f1.schalter != 'n')
				{
					f1.schalter = plusfarbe(f1.schalter);
					feld[SIN.fokusY][SIN.fokusX] = BFeld.copy(f1, this);
				}
				else if(f1.blockFarbe != 'n')
				{
					f1.blockFarbe = plusfarbe(f1.blockFarbe);
					feld[SIN.fokusY][SIN.fokusX] = BFeld.copy(f1, this);
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
				LFeld f1 = bl.feld[SIN.fokusY][SIN.fokusX];
				if(enhkey == 0)
				{
					f1 = new BFeld(f1.hoehe);
					bl.feld[SIN.fokusY][SIN.fokusX] = f1;
				}
				f1.enhance(this, enhkey);
				feld[SIN.fokusY][SIN.fokusX] = BFeld.copy(f1, this);
			}
		}
		return false;
	}

	public boolean moveR(int caret2)
	{
		int k = 0;
		for(int i = 0; i < items.size(); i++)
		{
			for(int j = 0; j < items.get(i).g1.size(); j++)
			{
				if(k == caret2)
				{
					items.get(i).benutze(j, false);
					return true;
				}
				k++;
			}
		}
		return false;
	}

	public void angleichen()
	{
		bl.feld[yp][xp].hoehe = hoeheA;
		feld[yp][xp] = BFeld.copy(bl.feld[yp][xp], this);
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
	public double realX()
	{
		return srd.x;
	}

	@Override
	public double realY()
	{
		return srd.y;
	}

	@Override
	public void victoryTick()
	{
		srd.deep += 0.1;
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
		gd.fillRect(w1, h - ht * dias, ht, ht * dias);
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
				renders.add(new Render("Löscher", 1));
			else if(SIN.fokusX >= 0)
			{
				xcp = SIN.fokusX;
				ycp = SIN.fokusY;
				BFeld fn = BFeld.copy(bl.feld[SIN.fokusY][SIN.fokusX], this);
				fn.enhance(this, enhkey);
				fn.addToRender(this, false, -1, -1);
			}
			gd.drawImage(tex.placeThese(renders).img, w1 + ht, ht * 2 * 4, ht * 2, ht * 2, null);
		}
		srd.tick(this);
	}
}