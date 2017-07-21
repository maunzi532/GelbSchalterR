package block;

import area.*;
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
	public static Color[] farben = new Color[]
			{
					new Color(191, 31, 31, 127),
					new Color(31, 191, 31, 127),
					new Color(31, 31, 191, 127),
					new Color(255, 191, 63, 127),
					new Color(255, 191, 63, 127),
					new Color(255, 191, 63, 127)
			};

	BFeld[][] feld;
	ArrayList<int[][]> geht2 = new ArrayList<>();
	ArrayList<int[][]> gehtTasten = new ArrayList<>();
	BlockLies bl;
	char farbeAktuell = 'A';
	int dias;
	int hoeheA;
	int akItem;
	ArrayList<Item> items = new ArrayList<>();
	boolean ntou;

	public void readFL(String c1)
	{
		bl = new BlockLies();
		if(c1.charAt(0) == '{')
		{
			ErrorVial vial = bl.lies(c1);
			if(!vial.worked())
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

	public void speichern()
	{
		JFileChooser fc = new JFileChooser(new File("saves"));
		if(fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
			try
			{
				Files.write(fc.getSelectedFile().toPath(), bl.speichern().getBytes(Charset.forName("UTF-8")));
			}catch(IOException e)
			{
				throw new RuntimeException(e);
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
	}

	@Override
	public Feld feld(int y, int x)
	{
		return feld[y][x];
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
			if(SIN.mfokusX >= 1)
			{
				int y1 = SIN.mfokusY / 2;
				if(y1 < items.size())
					akItem = y1;
			}
			return false;
		}
		int mlr = (TA.take[39] == 2 ? 1 : 0) - (TA.take[37] == 2 ? 1 : 0);
		int mou = (TA.take[40] == 2 ? 1 : 0) - (TA.take[38] == 2 ? 1 : 0);
		if(mlr != 0 || mou != 0)
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
				int code;
				if(ntou)
				{
					if(mou != 0)
						code = mou + 3;
					else
						code = mlr + 2;
				}
				else
				{
					if(mlr != 0)
						code = mlr + 2;
					else
						code = mou + 3;
				}
				if(items.get(i2).benutze(xp, yp, hoeheA, gehtTasten.get(i2), code))
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
				if(items.get(i2).benutze(xp, yp, hoeheA, geht2.get(i2), SIN.fokusX, SIN.fokusY))
					break;
			}
			return i < items.size();
		}
		else if(TA.take[69] == 2 && SIN.cheatmode)
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
		return false;
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
		gd.drawRect(w1 + ht, ht * 2 * akItem, ht * 2 - 1, ht * 2 - 1);
		for(int i = 0; i < items.size(); i++)
			gd.drawImage(tex.bilder2D.get(items.get(i).bildname()), w1 + ht, ht * 2 * i, ht * 2, ht * 2, null);
	}
}