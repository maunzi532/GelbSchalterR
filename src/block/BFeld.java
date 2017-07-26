package block;

import area.*;
import block.item.*;
import java.awt.*;

public class BFeld extends LFeld implements Feld
{
	public static final int maxenh = 11;

	BlockLab blockLab;

	boolean benutzt = false;

	public BFeld(){}

	public BFeld(int hoehe)
	{
		this.hoehe = hoehe;
	}

	public static BFeld copy(LFeld von, BlockLab blockLab)
	{
		BFeld copy = new BFeld();
		copy.blockLab = blockLab;
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
		copy.item = von.item;
		return copy;
	}

	public Integer getH(int side, boolean enter)
	{
		if(enter)
		{
			if(einhauwand >= 0 && !benutzt && einhauwand != side)
				return null;
			if(pfeil >= 0 && pfeil == side)
				return null;
			if(diaTuer > blockLab.dias)
				return null;
			if(eis && !benutzt && !blockLab.items.contains(new Feuer()))
				return null;
		}
		else
		{
			if(pfeil >= 0 && pfeil != side)
				return null;
		}
		if(blockFarbe != 'n' && blockFarbe != blockLab.farbeAktuell)
			return sonstH >= 0 ? sonstH : null;
		return hoehe;
	}

	public int getAH()
	{
		int h1 = hoehe;
		if(blockFarbe != 'n' && blockFarbe != blockLab.farbeAktuell)
			h1 = sonstH >= 0 ? sonstH : 0;
		if((einhauwand >= 0 && !benutzt) || diaTuer > 0 || (eis && !benutzt))
			return h1 + 1;
		return h1;
	}

	public void gehen()
	{
		if(loescher)
			blockLab.items.forEach(Item::loescher);
		Item itemA = blockLab.items.get(blockLab.akItem);
		blockLab.items.removeIf(Item::weg);
		if(item != null)
		{
			blockLab.items.remove(item);
			blockLab.items.add(item.kopie(blockLab));
		}
		int nA = blockLab.items.indexOf(itemA);
		blockLab.akItem = nA >= 0 ? nA : 0;
		if(schalter != 'n')
			blockLab.farbeAktuell = schalter;
		if(dia && !benutzt)
		{
			benutzt = true;
			blockLab.dias++;
		}
		if(einhauwand >= 0)
			benutzt = true;
		if(eis)
			benutzt = true;
		if(ziel)
		{
			blockLab.setRichtung(3);
			blockLab.gewonnen = true;
		}
	}

	public int bodenH()
	{
		if(blockFarbe != 'n' && blockFarbe != blockLab.farbeAktuell)
			return sonstH >= 0 ? sonstH : -1;
		return hoehe;
	}

	@Override
	public int visualH()
	{
		return hoehe;
	}

	@Override
	public void addToRender(Area area, boolean darauf, int xcp, int ycp)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Höhe").append(hoehe);
		if(blockFarbe != 'n')
		{
			if(((BlockLab) area).farbeAktuell != blockFarbe)
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
			area.addw("Schalter" + (blockLab.farbeAktuell == schalter ? "1" : "") + schalter);
		if(pfeil >= 0)
			area.addw("Pfeil" + pfeil);
		if(einhauwand >= 0)
			if(benutzt)
				area.addw("EinhauwandB" + einhauwand);
			else
				area.addw("Einhauwand" + einhauwand);
		if(dia)
			area.add3(DiaRender.gib(0.1, 0.9, 4, (SIN.t % 100) / 100d, 0.8, new Color(0, 0, benutzt ? 0 : 200, 127), visualH()));
		if(diaTuer > 0)
			if(diaTuer > blockLab.dias || xcp < 0)
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
		if(item != null && !darauf)
			area.addw(item.bildname());
		((BlockLab) area).srd.addSpieler(area, xcp, ycp);
	}
}