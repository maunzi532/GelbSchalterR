package block;

import area.*;
import block.item.*;
import block.state.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.stream.*;
import javax.swing.*;
import laderLC.*;
import shift.*;
import tex.*;

public class SchalterR extends Area
{
	public static final int itemtypes = 9;

	public BFeld[][] feld;
	private SchalterLies sl;

	public char farbeAktuell = 'A';
	public int ebeneRichtung;
	public int dias;

	int akItem;
	public Item[] items;
	public List<Item> showItems;

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
		generateShowItems();
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

	private int akvorne(int i)
	{
		return akItem < 0 ? i : i == 0 ? akItem : i <= akItem ? i - 1 : i;
	}

	@Override
	public ArrayList<Ziel> anzielbar()
	{
		ArrayList<Ziel> eintrag = new ArrayList<>();
		for(int i = 0; i < itemtypes; i++)
			if(items[akvorne(i)] != null)
				eintrag.addAll(items[akvorne(i)].g1);
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
		if(SIN.mfokusX >= 1 && itemauswahl(showItems.size() > 4 ? SIN.mfokusY * 2 + SIN.mfokusX - 1 : SIN.mfokusY / 2))
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
		if(nummer < showItems.size())
		{
			if(TA.take[201] == 2)
			{
				akItem = showItems.get(nummer).id;
				return true;
			}
			if(TA.take[203] == 2)
			{
				showItems.get(nummer).disabled = !showItems.get(nummer).disabled;
				return true;
			}
		}
		return false;
	}

	private boolean benutze(Ziel ziel, boolean cl)
	{
		return ziel != null && ziel.von instanceof Item && ((Item) ziel.von)
				.benutze(ziel.nummer, cl, items[akItem] == ziel.von, false);
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
		if(ziel.x != xp && ziel.y != yp)
			feld[yp][xp].weggehen();
		xp = ziel.x;
		yp = ziel.y;
		hp = ziel.h;
		feld[ziel.y][ziel.x].gehenL();
		Item itemA = items[akItem];
		for(int i = 0; i < itemtypes; i++)
			if(items[i] != null && items[i].weg())
				items[i] = null;
		feld[ziel.y][ziel.x].gehenItem(items);
		int nA = Arrays.asList(items).indexOf(itemA);
		akItem = nA >= 0 ? nA : 0;
		generateShowItems();
		feld[ziel.y][ziel.x].gehenFeld();
	}

	private void generateShowItems()
	{
		showItems = Arrays.stream(items).filter(Objects::nonNull).collect(Collectors.toList());
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
		if(showItems.size() <= 4)
		{
			for(int i = 0; i < showItems.size(); i++)
			{
				gd.drawImage(tex.bilder2D.get(showItems.get(i).bildname()), w1 + ht, ht * 2 * i, ht * 2, ht * 2, null);
				if(showItems.get(i).disabled)
					gd.drawLine(w1 + ht, ht * 2 * i, w1 + ht * 3 - 1, ht * 2 * (i + 1) - 1);
			}
			gd.drawRect(w1 + ht, ht * 2 * akItem, ht * 2 - 1, ht * 2 - 1);
		}
		else
		{
			for(int i = 0; i < showItems.size(); i++)
			{
				gd.drawImage(tex.bilder2D.get(showItems.get(i).bildname()), w1 + ht + (i % 2) * ht, ht * (i / 2), ht, ht, null);
				if(showItems.get(i).disabled)
					gd.drawLine(w1 + ht + (i % 2) * ht, ht * (i / 2), w1 + ht + (i % 2 + 1) * ht - 1, ht * (i / 2 + 1) - 1);
			}
			gd.drawRect(w1 + ht + (akItem % 2) * ht, ht * (akItem / 2), ht - 1, ht - 1);
		}
		if(cheatmode != null)
			cheatmode.rahmen(gd, tex, w1, ht);
	}
}