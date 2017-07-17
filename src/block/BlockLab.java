package block;

import area.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.*;
import laderLC.*;

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
	public boolean moveX(int inputT, int input2, int input3)
	{
		if(inputT == -3 && input2 > 0)
		{
			int y1 = input3 / 2;
			if(y1 < items.size())
				akItem = y1;
			return false;
		}
		else if(inputT == -2 && input2 >= 37 && input2 <= 40)
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
				if(items.get(i2).benutze(xp, yp, hoeheA, gehtTasten.get(i2), input2 - 36))
					break;
			}
			return i < items.size();
		}
		else if(inputT == -4)
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
				if(items.get(i2).benutze(xp, yp, hoeheA, geht2.get(i2), input2, input3))
					break;
			}
			return i < items.size();
		}
		else if(inputT == -5)
		{
			String alt = feld[input3][input2].speichern();
			Object neu = JOptionPane.showInputDialog(null, null, null, JOptionPane.QUESTION_MESSAGE, null, null, alt);
			if(neu instanceof String)
			{
				BFeld nf = new BFeld();
				nf.liesDirekt((String) neu);
				bl.feld[input3][input2] = nf;
				feld[input3][input2] = nf.copy(this);
			}
		}
		return false;
	}

	@Override
	public void rahmen(Graphics2D gd, Texturen tex, int w, int h)
	{
		gd.setColor(Color.BLACK);
		gd.fillRect(w - h / 10 * 3, 0, h / 10 * 3, h);
		gd.setColor(new Color(BlockLab.farben[farbeAktuell - 'A'].getRGB()));
		gd.fillRect(w - h / 10 * 3, 0, h / 10, h);
		gd.setColor(Color.BLUE);
		gd.fillRect(w - h / 10 * 3, h * (10 - dias) / 10, h / 10, h);
		//gd.setColor(Color.BLUE);
		//gd.fillRect(w - h / 10, 0, h / 10, h / 10 * lab.dias);
		gd.setColor(Color.WHITE);
		gd.drawRect(w - h / 10 * 3, 0, h / 10 - 1, h - 1);
		//gd.drawRect(w - h / 10 * 2, 0, h / 10 - 1, h - 1);
		//gd.drawRect(w - h / 10, 0, h / 10 - 1, h - 1);
		gd.setColor(Color.RED);
		gd.drawRect(w - h / 10 * 2, h / 5 * akItem, h / 5 - 1, h / 5 - 1);
		for(int i = 0; i < items.size(); i++)
			gd.drawImage(tex.bilder2D.get(items.get(i).bildname()), w - h / 10 * 2, h / 5 * i, h / 5, h / 5, null);
	}
}