package gelb;

import area.*;
import java.awt.*;

public class Gelb extends Area
{
	GFeld[][] feld;
	public int xz;
	public int yz;
	private final int gelbmax = 10;
	private int gelbn;
	private final int sprmax = 10;
	private int sprn;
	private final int lifmax = 10;
	private int lif;
	boolean teleport;

	public Feld feld(int y, int x)
	{
		return feld[y][x];
	}

	public void checkFields()
	{
		if(teleport)
		{
			for(int i = 0; i < feld.length; i++)
				for(int j = 0; j < feld[i].length; j++)
					geht[i][j] = (feld[i][j].darauf[2]
							&& feld[i][j].aktiviert) ? -1 : 0;
		}
		else
		{
			geht = new int[yw][xw];
			if(yp + 1 < yw)
				geht[yp + 1][xp] = feldBegehbar(yp + 1, xp, 3, 1);
			if(xp + 1 < xw)
				geht[yp][xp + 1] = feldBegehbar(yp, xp + 1, 2, 1);
			if(yp - 1 >= 0)
				geht[yp - 1][xp] = feldBegehbar(yp - 1, xp, 1, 1);
			if(xp - 1 >= 0)
				geht[yp][xp - 1] = feldBegehbar(yp, xp - 1, 4, 1);
			if(sprn >= 2)
			{
				if(yp + 2 < yw && feld[yp + 1][xp].getJH() <= feld[yp][xp].getH(3))
					geht[yp + 2][xp] = feldBegehbar(yp + 2, xp, 3, 2);
				if(xp + 2 < xw && feld[yp][xp + 1].getJH() <= feld[yp][xp].getH(2))
					geht[yp][xp + 2] = feldBegehbar(yp, xp + 2, 2, 2);
				if(yp - 2 >= 0 && feld[yp - 1][xp].getJH() <= feld[yp][xp].getH(1))
					geht[yp - 2][xp] = feldBegehbar(yp - 2, xp, 1, 2);
				if(xp - 2 >= 0 && feld[yp][xp - 1].getJH() <= feld[yp][xp].getH(4))
					geht[yp][xp - 2] = feldBegehbar(yp, xp - 2, 4, 2);
			}
			if(feld[yp][xp].darauf[2])
				geht[yp][xp] = -1;
			if(feld[yp][xp].lift && lif > 0)
				geht[yp][xp] = -1;
		}
	}

	private int feldBegehbar(int yf, int xf, int side, int weit)
	{
		Integer ph = treppencheck(feld[yp][xp], side, true);
		Integer fh = treppencheck(feld[yf][xf], side, false);
		if(ph == null || fh == null)
			return 0;
		if(!(feld[yf][xf].gelb && gelbn <= weit) && (fh.equals(ph)))
			return weit;
		return 0;
	}

	Integer treppencheck(GFeld f, int r, boolean start)
	{
		if(f.treppe == 0)
			return f.hoehe;
		if(f.treppe == r % 4 + 1 || f.treppe == (r + 2) % 4 + 1 || f.treppe == r && f.nTyp)
			return null;
		return f.hoehe + (f.treppe == (start ? r : (r + 1) % 4 + 1) ? 1 : 0);
	}

	public void gehen(int felder)
	{
		gelbn -= felder;
		if(gelbn < 0)
			gelbn = 0;
		sprn -= felder;
		if(sprn < 0)
			sprn = 0;
		lif -= felder;
		if(lif < 0)
			lif = 0;
		if(feld[yp][xp].darauf[0])
			gelbn = gelbmax;
		if(feld[yp][xp].darauf[1])
			sprn = sprmax;
		if(feld[yp][xp].darauf[2])
			feld[yp][xp].aktiviert = true;
		if(feld[yp][xp].darauf[3])
			lif = lifmax;
		if(xp == xz && yp == yz)
			gewonnen = true;
	}

	public boolean useItem()
	{
		if(feld[yp][xp].darauf[2])
		{
			teleport = true;
			mapview = true;
			return true;
		}
		if(feld[yp][xp].lift)
		{
			feld[yp][xp].hoehe += feld[yp][xp].aktiviert ? -1 : 1;
			feld[yp][xp].aktiviert = !feld[yp][xp].aktiviert;
			return true;
		}
		return false;
	}

	public void readFL(String c1)
	{
		try
		{
			c1 = c1.replaceAll("\\s", "");
			char[] c = c1.toCharArray();
			String loaded = "";
			int no1 = 0;
			int no2 = 0;
			int x1 = 0;
			int x2;
			for(int i = 0; i < c.length; i++)
			{
				if(c[i] == ';')
				{
					no1++;
					if(no1 <= 3)
					{
						x2 = Integer.parseInt(loaded);
						loaded = "";
						switch(no1)
						{
							case 1:
								xw = x1;
								yw = x2;
								break;
							case 2:
								xp = x1;
								yp = x2;
								break;
							case 3:
								xz = x1;
								yz = x2;
								break;
						}
					}
					else if(no1 == 4)
					{
						feld = new GFeld[yw][xw];
						loaded = "";
					}
				}
				else if(c[i] == 'x')
				{
					x1 = Integer.parseInt(loaded);
					loaded = "";
				}
				else if(c[i] == ',')
				{
					feld[no2 / xw][no2 % xw] = new GFeld(loaded);
					loaded = "";
					no2++;
				}
				else
					loaded = loaded + c[i];
			}
			feld[no2 / xw][no2 % xw] = new GFeld(loaded);
		}catch(Exception e)
		{
			System.out.println("Fehler beim Lesen der Datei");
			e.printStackTrace();
			System.exit(2);
		}
		noMovement();
	}

	@Override
	public void reset(){}

	public boolean moveX(int inputT, int clAtX, int clAtY)
	{
		boolean moved = false;
		int xv = xp;
		int yv = yp;
		if(teleport)
		{
			if(inputT == -4 && geht[clAtY][clAtX] != 0)
			{
				xp = clAtX;
				yp = clAtY;
				gehen(0);
				moved = true;
				teleport = false;
				mapview = false;
			}
		}
		else if(inputT == -2)
		{
			int xt = xv + (clAtX != 40 ? clAtX - 38 : 0);
			int yt = yv + (clAtX != 37 ? clAtX - 39 : 0);
			if(tryDirection(xt, yt))
				moved = !tryDirection(xt * 2 - xv, yt * 2 - yv);
			else
				moved = true;
		}
		else if(inputT == -4 && geht[clAtY][clAtX] != 0)
		{
			if(clAtX == xp && clAtY == yp)
				moved = useItem();
			else
			{
				xp = clAtX;
				yp = clAtY;
				gehen(geht[yp][xp]);
				moved = true;
			}
		}
		return moved;
	}

	private boolean tryDirection(int xt, int yt)
	{
		if(xt < 0 || yt < 0 || xt >= xw || yt >= yw || geht[yt][xt] == 0)
			return true;
		xp = xt;
		yp = yt;
		gehen(geht[yp][xp]);
		return false;
	}

	@Override
	public void rahmen(Graphics2D gd, Texturen tex, int w, int h)
	{
		gd.setColor(Color.RED);
		gd.fillRect(w - h / 10 * 3, 0, h / 10 * 3, h);
		gd.setColor(Color.YELLOW);
		gd.fillRect(w - h / 10 * 3, 0, h / 10, h / 10 * gelbn);
		gd.setColor(Color.BLUE);
		gd.fillRect(w - h / 10 * 2, 0, h / 10, h / 10 * sprn);
		gd.setColor(Color.DARK_GRAY);
		gd.fillRect(w - h / 10, 0, h / 10, h / 10 * lif);
		gd.setColor(Color.WHITE);
		gd.drawRect(w - h / 10 * 3, 0, h / 10 - 1, h - 1);
		gd.drawRect(w - h / 10 * 2, 0, h / 10 - 1, h - 1);
		gd.drawRect(w - h / 10, 0, h / 10 - 1, h - 1);
	}
}