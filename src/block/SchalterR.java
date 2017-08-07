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
	public BFeld[][] feld;
	private SchalterLies sl;

	public char farbeAktuell = 'A';
	public int dias;

	public int akItem;
	public final ArrayList<Item> items = new ArrayList<>();

	Cheatmode cheatmode;

	private final Stack<BState> states = new Stack<>();

	@Override
	public boolean start(String input, String texOrdnerName, boolean cheatmode, boolean changesize, int testmode)
	{
		readFL(input, changesize);
		if(cheatmode)
			this.cheatmode = new Cheatmode(this, sl);
		srd = new SRD(this);
		reset();
		Texturen tex = new FTex("SchalterR", texOrdnerName);
		return SIN.start(this, tex, testmode);
	}

	private void readFL(String c1, boolean se2n)
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
		mapview = false;
		farbeAktuell = 'A';
		dias = 0;
		items.clear();
		if(cheatmode != null)
			cheatmode.reset();
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

	@Override
	public void noMovement()
	{
		for(int i = 0; i < items.size(); i++)
			items.get(akvorne(i)).noMovement();
	}

	@Override
	public void checkFields()
	{
		boolean[] tasten = new boolean[5];
		for(int i = 0; i < items.size(); i++)
			if(items.get(akvorne(i)).disabled)
				items.get(akvorne(i)).noMovement();
			else
				items.get(akvorne(i)).setzeOptionen1(xp, yp, hp, xw, yw, akvorne(i), tasten);
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
	public boolean moveX()
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
		if(SIN.mfokusX >= 1 && itemauswahl(items.size() > 4 ? SIN.mfokusY * 2 + SIN.mfokusX - 1 : SIN.mfokusY / 2))
			return false;
		int code = slowerInput();
		if(code >= 0)
			benutze(SIN.tasten[code], false);
		else if(TA.take[201] == 2)
			return benutze(SIN.auswahl, true);
		else if(cheatmode != null)
			cheatmode.move();
		return false;
	}

	private boolean itemauswahl(int nummer)
	{
		if(nummer < items.size())
		{
			if(TA.take[201] == 2)
			{
				akItem = nummer;
				return true;
			}
			if(TA.take[203] == 2)
			{
				items.get(nummer).disabled = !items.get(nummer).disabled;
				return true;
			}
		}
		return false;
	}

	private boolean benutze(Ziel ziel, boolean cl)
	{
		return ziel != null && ziel.von instanceof Item && ((Item) ziel.von)
				.benutze(ziel.nummer, cl, items.get(akItem) == ziel.von, false);
	}

	public boolean moveR(int caret2)
	{
		int k = 0;
		for(int i = 0; i < items.size(); i++)
			for(int j = 0; j < items.get(i).g1.size(); j++)
			{
				if(k == caret2)
				{
					items.get(i).benutze(j, true, true, false);
					return true;
				}
				k++;
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
	public D3C d3c()
	{
		return new D3C(xp, yp, hp);
	}

	@Override
	protected void mapAdd(ArrayList<Render>[][] renders2, D3C feldAuswahl)
	{
		super.mapAdd(renders2, feldAuswahl);
		if(feldAuswahl != null && cheatmode != null)
			renders2[feldAuswahl.y][feldAuswahl.x].add(new Render("Auswahl", feld(feldAuswahl.y, feldAuswahl.x).markH()));
	}

	@Override
	public void rahmen(Graphics2D gd, Texturen tex, int w1, int h)
	{
		int ht = h / 10;
		gd.setColor(Color.BLACK);
		gd.fillRect(w1, 0, ht * 3, h);
		gd.setColor(new Color(FTex.farben[farbeAktuell - 'A'].getRGB()));
		gd.fillRect(w1, 0, ht, h);
		gd.setColor(Color.BLUE);
		gd.fillRect(w1, h - ht * dias, ht, ht * dias);
		gd.setColor(Color.WHITE);
		gd.drawRect(w1, 0, ht - 1, h - 1);
		gd.setColor(Color.RED);
		if(items.size() <= 4)
		{
			for(int i = 0; i < items.size(); i++)
			{
				gd.drawImage(tex.bilder2D.get(items.get(i).bildname()), w1 + ht, ht * 2 * i, ht * 2, ht * 2, null);
				if(items.get(i).disabled)
					gd.drawLine(w1 + ht, ht * 2 * i, w1 + ht * 3 - 1, ht * 2 * (i + 1) - 1);
			}
			gd.drawRect(w1 + ht, ht * 2 * akItem, ht * 2 - 1, ht * 2 - 1);
		}
		else
		{
			for(int i = 0; i < items.size(); i++)
			{
				gd.drawImage(tex.bilder2D.get(items.get(i).bildname()), w1 + ht + (i % 2) * ht, ht * (i / 2), ht, ht, null);
				if(items.get(i).disabled)
					gd.drawLine(w1 + ht + (i % 2) * ht, ht * (i / 2), w1 + ht + (i % 2 + 1) * ht - 1, ht * (i / 2 + 1) - 1);
			}
			gd.drawRect(w1 + ht + (akItem % 2) * ht, ht * (akItem / 2), ht - 1, ht - 1);
		}
		if(cheatmode != null)
			cheatmode.rahmen(gd, tex, w1, ht);
	}
}