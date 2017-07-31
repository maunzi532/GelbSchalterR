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
import shift.*;
import tex.*;

public class SchalterR extends Area
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
	private static final int limit = 6;

	public BFeld[][] feld;
	private SchalterLies sl;

	public char farbeAktuell = 'A';
	public int dias;
	public int hp;

	public int akItem;
	public final ArrayList<Item> items = new ArrayList<>();

	private int lrm;
	private int oum;
	public SRD srd;

	public int richtung;
	public Cheatmode cheatmode;

	private Stack<BState> states = new Stack<>();

	@Override
	public boolean start(String input, String texOrdnerName, boolean cheatmode, boolean changesize, int testmode)
	{
		readFL(input, changesize);
		srd = new SRD(this);
		reset();
		Texturen tex = new FTex("BlockLab", texOrdnerName);
		if(cheatmode)
			this.cheatmode = new Cheatmode(this, sl);
		return SIN.start(this, tex, cheatmode, testmode);
	}

	@Override
	public void readFL(String c1, boolean se2n)
	{
		sl = new SchalterLies();
		if(c1.charAt(0) == '{')
		{
			ErrorVial vial = sl.lies(c1, se2n);
			if(vial.errors())
				System.out.println(vial.errors);
		}
		else try
		{
			sl.liesA(c1);
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
		xw = sl.se[0][0];
		yw = sl.se[0][1];
		xp = sl.se[1][0];
		yp = sl.se[1][1];
		feld = new BFeld[yw][xw];
		for(int yi = 0; yi < yw; yi++)
			for(int xi = 0; xi < xw; xi++)
				feld[yi][xi] = BFeld.copy(sl.feld[yi][xi], this);
		hp = feld[yp][xp].hoehe;
		richtung = 3;
		srd.reset(this);
	}

	@Override
	public Feld feld(int y, int x)
	{
		return feld[y][x];
	}

	public void setRichtung(int r1)
	{
		richtung = r1;
		srd.setRichtung(r1);
	}

	@Override
	public void checkFields()
	{
		for(int i = 0; i < items.size(); i++)
			items.get(i).setzeOptionen1(xp, yp, hp, xw, yw, akvorne(i));
	}

	private int akvorne(int i)
	{
		return i == 0 ? akItem : i <= akItem ? i - 1 : i;
	}

	@Override
	public ArrayList<Ziel> anzielbar()
	{
		ArrayList<Ziel> eintrag = new ArrayList<>();
		for(int i = 0; i < items.size(); i++)
			eintrag.addAll(items.get(akvorne(i)).g1);
		Collections.sort(eintrag);
		return eintrag;
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
				Shift.localReset(new D3C(xp, yp, hp));
			}
		if(nichtMap && SIN.mfokusX >= 1 && TA.take[201] == 2)
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
		if(TA.take[32] == 2)
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
			for(int i = 0; i < items.size(); i++)
				if(items.get(akvorne(i)).benutze(code, i == 0, false))
					return true;
			return false;
		}
		else if(TA.take[201] == 2)
		{
			if(SIN.auswahl != null && SIN.auswahl.von instanceof Item)
			{
				Ziel<Item> ziel = (Ziel<Item>) SIN.auswahl;
				return ziel.von.benutze(ziel.key, false);
			}
			/*for(int i = 0; i < items.size(); i++)
				if(items.get(akvorne(i)).benutze(SIN.fokusX, SIN.fokusY, i == 0, false))
					return true;*/
			return false;
		}
		else if(cheatmode != null)
			cheatmode.move();
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
		sl.feld[yp][xp].hoehe = hp;
		feld[yp][xp] = BFeld.copy(sl.feld[yp][xp], this);
	}

	public void speichern()
	{
		JFileChooser fc = new JFileChooser(new File("saves"));
		boolean sh = TA.take[16] > 0;
		if(fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			String s;
			if(sh)
				s = sl.speichern(xp, yp);
			else
				s = sl.speichern();
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
	public D3C d3c()
	{
		return new D3C(xp, yp, hp);
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
		gd.setColor(new Color(SchalterR.farben[farbeAktuell - 'A'].getRGB()));
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
		if(cheatmode != null)
			cheatmode.rahmen(gd, tex, w1, ht);
		srd.tick(this);
	}
}