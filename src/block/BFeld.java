package block;

import area.*;
import block.item.*;
import java.awt.*;

public class BFeld extends LFeld implements Feld
{
	SchalterR schalterR;

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

	public Integer getH(int side, boolean enter)
	{
		if(enter)
		{
			if(einhauwand >= 0 && !benutzt && einhauwand != side)
				return null;
			if(pfeil >= 0 && pfeil == side)
				return null;
			if(diaTuer > schalterR.dias)
				return null;
			if(eis && !benutzt && !schalterR.items.contains(new Feuer()))
				return null;
		}
		else
		{
			if(pfeil >= 0 && pfeil != side)
				return null;
		}
		if(blockFarbe != 'n' && blockFarbe != schalterR.farbeAktuell)
			return sonstH >= 0 ? sonstH : null;
		return lift(hoehe);
	}

	public int getAH()
	{
		int h1 = hoehe;
		if(blockFarbe != 'n' && blockFarbe != schalterR.farbeAktuell)
			h1 = sonstH >= 0 ? sonstH : 0;
		if((einhauwand >= 0 && !benutzt) || diaTuer > 0 || (eis && !benutzt))
			return h1 + 1;
		if(h1 != hoehe)
			return h1;
		return lift(hoehe);
	}

	public boolean liftOben()
	{
		return lift && schalterR.hp > hoehe;
	}

	private int lift(int hv)
	{
		return liftOben() ? hv + 1 : hv;
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

	public int bodenH()
	{
		if(blockFarbe != 'n' && blockFarbe != schalterR.farbeAktuell)
			return sonstH >= 0 ? sonstH : -1;
		return lift(hoehe);
	}

	@Override
	public int visualH()
	{
		return hoehe;
	}

	@Override
	public int texZero()
	{
		return hoehe;
	}

	@Override
	public int markH()
	{
		if(blockFarbe != 'n' && blockFarbe != schalterR.farbeAktuell)
			return sonstH >= 0 ? sonstH : -1;
		return lift(hoehe);
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
			area.add3(DiaRender.gib(0.1, 0.9, 4, (area.tick % 100) / 100d, 0.8, new Color(0, 0, benutzt ? 0 : 200, 127), visualH()));
		if(diaTuer > 0)
			if(diaTuer > schalterR.dias || xcp < 0)
				area.addw("DiaTür", "  " + diaTuer);
			else
				area.addw("DiaTürOffen");
		if(eis)
			if(benutzt)
				area.addw("EisB");
			else
				area.addw("Eis");
		if(loescher)
			area.addw("Löscher");
		if(enterstange >= 0)
			area.addm("Stange" + (darauf ? "B" : ""), enterstange);
		if(lift)
			if(lift(hoehe) > hoehe)
				area.addw("LiftOben");
			else
				area.addw("LiftUnten");
		if(item != null && !darauf)
			area.addw(item.bildname());
		schalterR.srd.addSpieler(area, xcp, ycp);
	}
}