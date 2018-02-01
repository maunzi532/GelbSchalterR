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
	public static final int itemtypes = 15;

	public BFeld[][] feld;
	private SchalterLies sl;

	public char farbeAktuell = 'A';
	public int ebeneRichtung;
	public int dias;

	ShowItems showItems;
	public Item[] items;

	private Cheatmode cheatmode;

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
		ebeneRichtung = 4;
		dias = 0;
		items = new Item[itemtypes];
		if(cheatmode != null)
			cheatmode.reset();
		items[1] = new Movement().kopie(this);
		showItems = new ShowItems(this);
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
		for(int i = 0; i < itemtypes; i++)
			if(items[i] != null)
				items[i].noMovement();
	}

	@Override
	public void checkFields()
	{
		boolean[] tasten = new boolean[5];
		for(int i = 0; i < itemtypes; i++)
			if(items[i] != null)
				if(items[i].disabled)
					items[i].noMovement();
				else
					items[i].setzeOptionen1(xp, yp, hp, xw, yw, tasten);
	}

	@Override
	public ArrayList<Ziel> anzielbar()
	{
		return showItems.anzielbar();
	}

	@Override
	public boolean moveX()
	{
		if(TA.take[120] == 2)
		{
			if(TA.take[16] > 0)
				new BState(this).speichernState();
			else
				states.add(new BState(this));
		}
		if(TA.take[121] == 2)
		{
			BState lade = null;
			if(TA.take[16] > 0)
				lade = BState.ladenState();
			else if(!states.empty())
				lade = states.pop();
			if(lade != null)
			{
				lade.charge(this);
				srd.reset2(this);
				Shift.localReset(new D3C(xp, yp, hp));
				showItems.generateShowItems();
			}
		}
		if(SIN.mfokusX >= 1 && showItems.itemauswahl(showItems.showItems.size() > 4 ? SIN.mfokusY * 2 + SIN.mfokusX - 1 : SIN.mfokusY / 2))
			return false;
		if(showItems.itemauswahl())
			return false;
		int[] code = slowerInput();
		if(code[0] >= 0)
		{
			if(benutze(SIN.tasten[code[0]], false, code[1] > 0))
				discharge(code[0]);
		}
		else if(TA.take[201] == 2)
			return benutze(SIN.auswahl, true, true);
		else if(cheatmode != null)
			cheatmode.move();
		return false;
	}

	private boolean benutze(Ziel ziel, boolean cl, boolean charge)
	{
		return ziel != null && ziel.von instanceof Item && ((Item) ziel.von)
				.benutze(ziel.nummer, cl, charge, false);
	}

	public boolean moveR(int caret2)
	{
		int k = 0;
		for(int i = 0; i < itemtypes; i++)
			if(items[i] != null)
				for(int j = 0; j < items[i].g1.size(); j++)
				{
					if(k == caret2)
					{
						items[i].benutze(j, true, true, false);
						return true;
					}
					k++;
				}
		return false;
	}

	public boolean aufEben()
	{
		return hp == feld[yp][xp].bodenH() || items[8] != null;
	}

	public void angleichen()
	{
		sl.feld[yp][xp].hoehe = hp;
		feld[yp][xp] = BFeld.copy(sl.feld[yp][xp], this);
	}

	public void gehen(D3C ziel)
	{
		xp = ziel.x;
		yp = ziel.y;
		hp = ziel.h;
		feld[ziel.y][ziel.x].gehenL();
		for(int i = 0; i < itemtypes; i++)
			if(items[i] != null && items[i].weg())
				items[i] = null;
		feld[ziel.y][ziel.x].gehenItem(items);
		showItems.removeUnused();
		showItems.generateShowItems();
		feld[ziel.y][ziel.x].gehenFeld();
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
		TA.fix();
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
			renders2[feldAuswahl.y][feldAuswahl.x].add(new Render("Auswahl" + feld(feldAuswahl.y, feldAuswahl.x).markH(), 0));
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
		showItems.rahmen(gd, tex, w1, ht);
		if(cheatmode != null)
			cheatmode.rahmen(gd, tex, w1, ht);
	}
}