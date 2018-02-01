package gelb;

import area.*;
import java.awt.*;
import java.util.*;
import shift.*;
import tex.*;

public class Gelbgeher extends Area implements PreItem
{
	private int[][] geht;
	private GFeld[][] feld;
	private GelbLies gl;
	private int xz;
	private int yz;
	private static final int gelbmax = 10;
	private int gelbn;
	private static final int sprmax = 10;
	private int sprn;
	private static final int lifmax = 10;
	private int lif;
	private boolean teleport;

	@Override
	public boolean start(String input, String texOrdnerName, boolean chm, boolean chs, int tem)
	{
		gl = new GelbLies();
		gl.liesA(input);
		srd = new SRD(this);
		reset();
		Texturen tex = new GTex("Gelbgeher", texOrdnerName);
		return SIN.start(this, tex, tem);
	}

	@Override
	public void reset()
	{
		tick = 0;
		mapview = false;
		gelbn = 0;
		sprn = 0;
		lif = 0;
		teleport = false;
		xw = gl.xw;
		yw = gl.yw;
		xp = gl.xs;
		yp = gl.ys;
		xz = gl.xz;
		yz = gl.yz;
		feld = new GFeld[yw][xw];
		for(int yi = 0; yi < yw; yi++)
			for(int xi = 0; xi < xw; xi++)
				feld[yi][xi] = GFeld.copy(gl.feld[yi][xi]);
		hp = feld[yp][xp].hoehe;
		richtung = 3;
		srd.reset(this);
	}

	@Override
	public void noMovement()
	{
		geht = new int[yw][xw];
	}

	@Override
	public Feld feld(int y, int x)
	{
		return feld[y][x];
	}

	@Override
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

	private Integer treppencheck(GFeld f, int r, boolean start)
	{
		if(f.treppe == 0)
			return f.hoehe;
		if(f.treppe == r % 4 + 1 || f.treppe == (r + 2) % 4 + 1 || f.treppe == r && f.nTyp)
			return null;
		return f.hoehe + (f.treppe == (start ? r : (r + 1) % 4 + 1) ? 1 : 0);
	}

	@Override
	public ArrayList<Ziel> anzielbar()
	{
		ArrayList<Ziel> eintrag = new ArrayList<>();
		for(int iy = yw - 1; iy >= 0; iy--)
			for(int ix = xw - 1; ix >= 0; ix--)
				if(geht[iy][ix] > 0)
					eintrag.add(new Ziel(ix, iy, feld[iy][ix].markH(), this, geht[iy][ix], -1, -1));
		return eintrag;
	}

	@Override
	public String marker(boolean hier)
	{
		return "";
	}

	@Override
	public String symbol(int key)
	{
		return null;
	}

	private void gehen(int felder)
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
		hp = feld[yp][xp].daraufH();
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

	private boolean useItem()
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
			hp = feld[yp][xp].daraufH();
			return true;
		}
		return false;
	}

	@Override
	public boolean moveX()
	{
		int fokusY = -1;
		int fokusX = -1;
		if(SIN.auswahl != null)
		{
			fokusY = SIN.auswahl.y;
			fokusX = SIN.auswahl.x;
		}
		boolean moved = false;
		int xv = xp;
		int yv = yp;
		if(teleport)
		{
			if(TA.take[201] == 2 && SIN.auswahl != null && geht[fokusY][fokusX] != 0)
			{
				xp = fokusX;
				yp = fokusY;
				gehen(0);
				moved = true;
				teleport = false;
				mapview = false;
			}
		}
		else
		{
			int code = slowerInput();
			if(code == 0)
				moved = useItem();
			else if(code > 0)
			{
				int xe = code == 1 ? -1 : (code == 3 ? 1 : 0);
				int ye = code == 2 ? -1 : (code == 4 ? 1 : 0);
				moved = TA.take[16] > 0 ? tryDirection(xv + xe * 2, yv + ye * 2) : tryDirection(xv + xe, yv + ye);
			}
			else if(TA.take[201] == 2 && SIN.auswahl != null && geht[fokusY][fokusX] != 0)
			{
				if(fokusX == xp && fokusY == yp)
					moved = useItem();
				else
				{
					xp = fokusX;
					yp = fokusY;
					gehen(geht[yp][xp]);
					moved = true;
				}
			}
		}
		return moved;
	}

	private boolean tryDirection(int xt, int yt)
	{
		if(xt < 0 || yt < 0 || xt >= xw || yt >= yw || geht[yt][xt] == 0)
			return false;
		xp = xt;
		yp = yt;
		gehen(geht[yp][xp]);
		return true;
	}

	@Override
	public D3C d3c()
	{
		return new D3C(xp, yp, feld[yp][xp].getJH());
	}

	@Override
	protected void mapAdd(ArrayList<Render>[][] renders2, D3C feldAuswahl)
	{
		renders2[yz][xz].add(new Render("Ziel", feld[yz][xz].daraufH()));
		super.mapAdd(renders2, feldAuswahl);
	}

	@Override
	public void rahmen(Graphics2D gd, Texturen tex, int w1, int h)
	{
		int ht = h / 10;
		gd.setColor(Color.RED);
		gd.fillRect(w1, 0, ht * 3, h);
		gd.setColor(Color.YELLOW);
		gd.fillRect(w1, 0, ht, ht * gelbn);
		gd.setColor(Color.BLUE);
		gd.fillRect(w1 + ht, 0, ht, ht * sprn);
		gd.setColor(Color.DARK_GRAY);
		gd.fillRect(w1 + ht * 2, 0, ht, ht * lif);
		gd.setColor(Color.WHITE);
		gd.drawRect(w1, 0, ht - 1, h - 1);
		gd.drawRect(w1 + ht, 0, ht - 1, h - 1);
		gd.drawRect(w1 + ht * 2, 0, ht - 1, h - 1);
	}
}