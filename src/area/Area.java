package area;

import java.awt.*;
import java.util.*;
import shift.*;
import sun.reflect.generics.reflectiveObjects.*;
import tex.*;

public abstract class Area
{
	private static final int slow = 6;

	public boolean gewonnen;
	public boolean mapview;
	public int xw;
	public int yw;
	public int xp;
	public int yp;
	public int hp;
	public int tick;

	public SRD srd;
	public int richtung;

	private int lrm;
	private int oum;

	public abstract boolean start(String input, String texOrdnerName, boolean chm, boolean chs, int tem);

	public abstract Feld feld(int y, int x);

	public abstract void checkFields();

	public abstract ArrayList<Ziel> anzielbar();

	public abstract boolean moveX();

	public abstract void rahmen(Graphics2D gd, Texturen tex, int w1, int h);

	public abstract void noMovement();

	public abstract void reset();

	public void victoryTick()
	{
		srd.deep += 0.1;
	}

	public void speichern()
	{
		throw new NotImplementedException();
	}

	public int slowerInput()
	{
		if(TA.take[37] <= 0 || TA.take[39] <= 0)
		{
			if(TA.take[37] > 0 && lrm > -slow)
				lrm--;
			if(TA.take[39] > 0 && lrm < slow)
				lrm++;
		}
		if(TA.take[37] == 2 && TA.take[39] == 2)
			lrm = 0;
		else if(TA.take[37] == 2)
			lrm = -slow;
		else if(TA.take[39] == 2)
			lrm = slow;
		if(TA.take[38] <= 0 || TA.take[40] <= 0)
		{
			if(TA.take[38] > 0 && oum > -slow)
				oum--;
			if(TA.take[40] > 0 && oum < slow)
				oum++;
		}
		if(TA.take[38] == 2 && TA.take[40] == 2)
			oum = 0;
		else if(TA.take[38] == 2)
			oum = -slow;
		else if(TA.take[40] == 2)
			oum = slow;
		if(TA.take[32] == 2)
			return 0;
		if(TA.take[37] > 0 && lrm <= -slow)
		{
			lrm = -1;
			return 1;
		}
		if(TA.take[38] > 0 && oum <= -slow)
		{
			oum = -1;
			return 2;
		}
		if(TA.take[39] > 0 && lrm >= slow)
		{
			lrm = 1;
			return 3;
		}
		if(TA.take[40] > 0 && oum >= slow)
		{
			oum = 1;
			return 4;
		}
		return -1;
	}

	public void setRichtung(int r1)
	{
		richtung = r1;
		srd.setRichtung(r1);
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
		srd.tick(this);
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

	protected void mapAdd()
	{
		srd.addSpieler(this, xcp, ycp);
	}

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