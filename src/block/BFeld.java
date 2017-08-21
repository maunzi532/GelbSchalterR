package block;

import area.*;
import block.item.*;
import java.awt.*;
import java.util.*;
import tex.*;

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
		copy.eSchalter = von.eSchalter;
		copy.pfeil = von.pfeil;
		copy.einhauwand = von.einhauwand;
		copy.dia = von.dia;
		copy.diaTuer = von.diaTuer;
		copy.eis = von.eis;
		copy.loescher = von.loescher;
		copy.enterstange = von.enterstange;
		copy.enterpfeil = von.enterpfeil;
		copy.fahrebene = von.fahrebene;
		copy.lift = von.lift;
		copy.item = von.item;
		return copy;
	}

	public void reset()
	{
		benutzt = false;
	}

	public boolean betretengeht(int h, int side)
	{
		if(h != bodenH())
			return true;
		if(einhauwand >= 0 && !benutzt && einhauwand != side)
			return false;
		if(pfeil >= 0 && pfeil == side)
			return false;
		if(diaTuer > schalterR.dias)
			return false;
		if(eis && !benutzt && schalterR.items[2] == null)
			return false;
		return true;
	}

	public boolean weggehengeht(int h, int side)
	{
		return h != bodenH() || !(pfeil >= 0 && pfeil != side);
	}

	public boolean aufEben()
	{
		return schalterR.hp == ebenH();
	}

	/*public int getBlockLaserH()
	{
		return Math.max((einhauwand >= 0 && !benutzt) || diaTuer > 0 || (eis && !benutzt) ? bodenH() + 1 : bodenH(), existEbene());
	}*/

	public int getBlockH()
	{
		return Math.max((einhauwand >= 0 && !benutzt) || diaTuer > schalterR.dias || (eis && !benutzt) ? bodenH() + 1 : bodenH(), existEbene());
	}

	public boolean farbeAktiv()
	{
		return blockFarbe == schalterR.farbeAktuell;
	}

	private int existEbene()
	{
		return fahrebene;
		/*if(fahrebene < 0)
			return -1;
		int idx = schalterR.items.indexOf(new FahrendeEbene());
		if(idx < 0)
			return fahrebene;
		FahrendeEbene fe = (FahrendeEbene) schalterR.items.get(idx);
		return fe.start.x == schalterR.xp && fe.start.y == schalterR.yp ? -1 : fahrebene;*/
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

	public int ebenH()
	{
		return Math.max(bodenH(), existEbene());
	}

	@Override
	public int markH()
	{
		return ebenH();
	}

	public void weggehen()
	{

	}

	public void gehenL()
	{
		if(schalterR.hp == bodenH() && loescher)
			for(int i = 0; i < SchalterR.itemtypes; i++)
				if(schalterR.items[i] != null)
					schalterR.items[i].loescher();
	}

	public void gehenItem(Item[] items)
	{
		if(item != null && (items[item.id] == null || item.priority >= items[item.id].priority))
			items[item.id] = item.kopie(schalterR);
		if(schalterR.hp == enterstange && enterpfeil >= 0)
			items[7] = new FahrenderPfeil(enterpfeil, schalterR.d3c()).kopie(schalterR);
		if(schalterR.hp == existEbene() && bodenH() < fahrebene)
			items[8] = new FahrendeEbene(schalterR.d3c()).kopie(schalterR);
	}

	public void gehenFeld()
	{
		if(schalterR.hp == bodenH())
		{
			if(schalter != 'n')
				schalterR.farbeAktuell = schalter;
			if(eSchalter >= 0)
				schalterR.ebeneRichtung = eSchalter;
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
	}

	@Override
	public ArrayList<Render> addToRender(RenderCreater rc, boolean darauf, boolean preview)
	{
		rc.sh = bodenH();
		if(blockFarbe == 'n')
			rc.addgz("Höhe" + hoehe, hoehe);
		else if(schalterR.farbeAktuell == blockFarbe)
			rc.addgz("Höhe" + hoehe + blockFarbe, hoehe);
		else
		{
			rc.addgz("HöheI" + blockFarbe, hoehe);
			rc.addgz("Höhe" + sonstH, sonstH);
		}
		if(ziel)
		{
			rc.addw("Ziel2");
			rc.addw("Ziel1");
		}
		if(schalter != 'n')
			rc.addw("Schalter" + (schalterR.farbeAktuell == schalter ? "1" : "") + schalter);
		if(eSchalter >= 0)
			rc.addw("RSchalter" + (schalterR.ebeneRichtung == eSchalter ? "B" : "") + eSchalter);
		if(pfeil >= 0)
			rc.addw("Pfeil" + pfeil);
		if(einhauwand >= 0)
			if(benutzt)
				rc.addw("EinhauwandB" + einhauwand);
			else
				rc.addw("Einhauwand" + einhauwand);
		if(dia)
			rc.add(DiaRender.gib(0.1, 0.9, 4, (schalterR.tick % 100) / 100d, 0.8, new Color(0, 0, benutzt ? 0 : 200, 127), bodenH()));
		if(diaTuer > 0)
			if(diaTuer > schalterR.dias || preview)
			{
				rc.addw("DiaTür");
				rc.addt("  " + diaTuer, bodenH() + 1);
			}
			else
				rc.addw("DiaTürOffen");
		if(eis)
			rc.addw(benutzt ? "EisB" : "Eis");
		if(loescher)
			rc.addw("Löscher");
		if(enterstange >= 0)
		{
			rc.addm("Stange" + (darauf && (schalterR.hp == enterstange || schalterR.hp + 1 == enterstange) ? "B" : "") + enterstange, 0);
			if(enterpfeil >= 0)
				rc.addm("Enterpfeil" + enterpfeil, enterstange);
		}
		if(existEbene() >= 0)
			rc.addm("Fahrebene", fahrebene);
		if(lift)
			if(liftOben())
				rc.addm("LiftOben", steinH());
			else
				rc.addm("LiftUnten", steinH());
		if(item != null && !darauf)
			rc.addw(item.bildname());
		return rc.renders;
	}
}