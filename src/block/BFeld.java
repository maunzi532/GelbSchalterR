package block;

import area.*;
import block.item.*;
import java.awt.*;

public class BFeld extends LFeld implements Feld
{
	private SchalterR schalterR;

	public boolean benutzt = false;

	public BFeld(){}

	public BFeld(int hoehe)
	{
		this.hoehe = hoehe;
	}

	public static BFeld copy(LFeld von, SchalterR schalterR)
	{
		BFeld copy = new BFeld();
		copy.schalterR = schalterR;
		copy.ziel = von.ziel;
		copy.hoehe = von.hoehe;
		copy.blockFarbe = von.blockFarbe;
		copy.sonstH = von.sonstH;
		copy.schalter = von.schalter;
		copy.pfeil = von.pfeil;
		copy.einhauwand = von.einhauwand;
		copy.dia = von.dia;
		copy.diaTuer = von.diaTuer;
		copy.eis = von.eis;
		copy.loescher = von.loescher;
		copy.enterstange = von.enterstange;
		copy.lift = von.lift;
		copy.item = von.item;
		return copy;
	}

	public void reset()
	{
		benutzt = false;
	}

	public boolean betretengeht(int side, int hp)
	{
		if(hp != bodenH())
			return false;
		if(einhauwand >= 0 && !benutzt && einhauwand != side)
			return false;
		if(pfeil >= 0 && pfeil == side)
			return false;
		if(diaTuer > schalterR.dias)
			return false;
		if(eis && !benutzt && !schalterR.items.contains(new Feuer()))
			return false;
		return true;
	}

	public boolean weggehengeht(int side, int hp)
	{
		if(hp != bodenH())
			return true;
		if(pfeil >= 0 && pfeil != side)
			return false;
		return true;
	}

	public int getBlockedH()
	{
		return (einhauwand >= 0 && !benutzt) || diaTuer > 0 || (eis && !benutzt) ? bodenH() + 1 : bodenH();
	}

	public int getBlockedH2()
	{
		return (einhauwand >= 0 && !benutzt) || diaTuer > schalterR.dias || (eis && !benutzt) ? bodenH() + 1 : bodenH();
	}

	private int steinH()
	{
		return blockFarbe != 'n' && blockFarbe != schalterR.farbeAktuell ? sonstH : hoehe;
	}

	public boolean liftOben()
	{
		return lift && schalterR.hp > steinH();
	}

	public int bodenH()
	{
		return liftOben() ? steinH() + 1 : steinH();
	}

	@Override
	public int daraufH()
	{
		return hoehe;
	}

	@Override
	public int markH()
	{
		return bodenH();
	}

	public void gehen()
	{
		if(loescher)
			schalterR.items.forEach(Item::loescher);
		Item itemA = schalterR.items.get(schalterR.akItem);
		schalterR.items.removeIf(Item::weg);
		if(item != null)
		{
			schalterR.items.remove(item);
			schalterR.items.add(item.kopie(schalterR));
		}
		int nA = schalterR.items.indexOf(itemA);
		schalterR.akItem = nA >= 0 ? nA : 0;
		if(schalter != 'n')
			schalterR.farbeAktuell = schalter;
		if(dia && !benutzt)
		{
			benutzt = true;
			schalterR.dias++;
		}
		if(einhauwand >= 0)
			benutzt = true;
		if(eis)
			benutzt = true;
		if(ziel)
		{
			schalterR.setRichtung(3);
			schalterR.gewonnen = true;
		}
	}

	@Override
	public void addToRender(Area area, boolean darauf, int xcp, int ycp)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Höhe").append(hoehe);
		if(blockFarbe != 'n')
		{
			if(((SchalterR) area).farbeAktuell != blockFarbe)
			{
				sb = new StringBuilder("HöheI");
				if(sonstH >= 0)
					area.addm("Höhe" + sonstH, sonstH);
			}
			sb.append(blockFarbe);
		}
		if(hoehe > 0)
			area.addw(sb.toString());
		if(ziel)
		{
			area.addw("Ziel2");
			area.addw("Ziel1");
		}
		if(schalter != 'n')
			area.addw("Schalter" + (schalterR.farbeAktuell == schalter ? "1" : "") + schalter);
		if(pfeil >= 0)
			area.addw("Pfeil" + pfeil);
		if(einhauwand >= 0)
			if(benutzt)
				area.addw("EinhauwandB" + einhauwand);
			else
				area.addw("Einhauwand" + einhauwand);
		if(dia)
			area.add3(DiaRender.gib(0.1, 0.9, 4, (area.tick % 100) / 100d, 0.8, new Color(0, 0, benutzt ? 0 : 200, 127), daraufH()));
		if(diaTuer > 0)
			if(diaTuer > schalterR.dias || xcp < 0)
				area.addw("DiaTür", "  " + diaTuer);
			else
				area.addw("DiaTürOffen");
		if(eis)
			area.addw(benutzt ? "EisB" : "Eis");
		if(loescher)
			area.addw("Löscher");
		if(enterstange >= 0)
			area.addm("Stange" + (darauf ? "B" : ""), enterstange);
		if(lift)
			area.addw(liftOben() ? "LiftOben" : "LiftUnten");
		if(item != null && !darauf)
			area.addw(item.bildname());
		schalterR.srd.addSpieler(area, xcp, ycp);
	}
}