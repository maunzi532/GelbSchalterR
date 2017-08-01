package area;

import java.awt.*;
import java.util.*;
import shift.*;
import sun.reflect.generics.reflectiveObjects.*;
import tex.*;

public abstract class Area
{
	public boolean gewonnen;
	public boolean mapview;
	public int xw;
	public int yw;
	public int xp;
	public int yp;
	public int tick;

	public abstract boolean start(String input, String texOrdnerName, boolean chm, boolean chs, int tem);

	public abstract Feld feld(int y, int x);

	public abstract void checkFields();

	public abstract ArrayList<Ziel> anzielbar();

	public abstract boolean moveX();

	public abstract void rahmen(Graphics2D gd, Texturen tex, int w1, int h);

	public abstract void noMovement();

	public abstract void reset();

	public void victoryTick(){}

	public void speichern()
	{
		throw new NotImplementedException();
	}

	public double realX()
	{
		return xp;
	}

	public double realY()
	{
		return yp;
	}

	public abstract D3C d3c();

	public int ycp;
	public int xcp;
	public int sh;
	public ArrayList<Render> renders;
	public ArrayList<Render>[][] renders2;

	@SuppressWarnings("unchecked")
	public void render(Ziel auswahl)
	{
		ArrayList<Ziel> eintrag = anzielbar();
		renders2 = new ArrayList[yw][xw];
		for(ycp = 0; ycp < yw; ycp++)
			for(xcp = 0; xcp < xw; xcp++)
			{
				renders = new ArrayList<>();
				feld(ycp, xcp).addToRender(this, xcp == xp && ycp == yp, xcp, ycp);
				mapAdd();
				if(auswahl != null && auswahl.x == xcp && auswahl.y == ycp)
					addm("Auswahl", auswahl.h);
				for(Ziel z : eintrag)
					if(z.x == xcp && z.y == ycp && z.von != null)
					{
						PreItem von = z.von;
						String marker = von.marker();
						String symbol = von.symbol(z.taste);
						addm("Möglich" + marker, z.h);
						if(symbol != null)
							addm("Symbol" + symbol + marker, z.h);
					}
				renders2[ycp][xcp] = renders;
			}
	}

	protected void mapAdd(){}

	public void addw(String name)
	{
		renders.add(new Render(name, sh));
	}

	public void addw(String name, String text)
	{
		renders.add(new Render(name, text, sh));
	}

	public void addm(String name, int h)
	{
		renders.add(new Render(name, h));
	}

	public void addgz(String name, int h)
	{
		if(h > 0)
			renders.add(new Render(name, h));
	}

	public void add3(Render3 r3)
	{
		renders.add(r3);
	}
}