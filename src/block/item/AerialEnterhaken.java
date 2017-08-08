package block.item;

import block.*;
import block.state.*;
import laderLC.*;

public class AerialEnterhaken extends Item
{
	private boolean doppelt;
	private int laenge;

	public AerialEnterhaken()
	{
		laenge = 5;
	}

	public AerialEnterhaken(int level, int laenge, boolean doppelt)
	{
		super(level);
		this.doppelt = doppelt;
		this.laenge = laenge;
	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		AerialEnterhaken i1 = new AerialEnterhaken(level, laenge, doppelt);
		i1.schalterR = schalterR;
		return i1;
	}

	@Override
	public void setzeOptionen(int xp, int yp, int hp, int xw, int yw, BFeld fp)
	{
		for(int ix = Math.max(xp - laenge, 0); ix <= xp + laenge && ix < xw; ix++)
			for(int iy = Math.max(yp - laenge, 0); iy <= yp + laenge && iy < yw; iy++)
			{
				int zh = erreichbar(xp, yp, hp, ix, iy);
				if(zh >= 0)
					option(ix, iy, zh, -1);
			}
	}

	private int erreichbar(int xp, int yp, int hp, int xf, int yf)
	{
		BFeld f = schalterR.feld[yf][xf];
		int zh = f.enterstange;
		if(xf == xp && yf == yp && zh == hp)
			return -1;
		if(zh < f.bodenH() || (zh > f.bodenH() && !doppelt))
			return -1;
		int xd = (xf - xp) * (xf - xp);
		int yd = (yf - yp) * (yf - yp);
		int zd = (zh - hp) * (zh - hp);
		if(xd + yd + zd > laenge * laenge)
			return -1;
		int xn = Math.min(xp, xf);
		int xh = Math.max(xp, xf);
		int yn = Math.min(yp, yf);
		int yh = Math.max(yp, yf);
		int minh = Math.min(hp, zh);
		for(int xn2 = xn; xn2 <= xh; xn2++)
			for(int yn2 = yn; yn2 <= yh; yn2++)
				if((yn2 != yp || xn2 != xp) && (yn2 != yf || xn2 != xf) && schalterR.feld[yn2][xn2].getBlockH() > minh)
					return -1;
		return zh;
	}

	@Override
	public String speichername()
	{
		return "enterhaken";
	}

	@Override
	public String bildname()
	{
		return doppelt ? "Doppelhaken" : "Enterhaken";
	}

	public void lies2(String value, Integer errStart, Integer errEnd, ErrorVial vial, String textKey)
	{
		try
		{
			if(textKey.toLowerCase().equals("level"))
				level = Integer.parseInt(value);
			else if(textKey.toLowerCase().equals("doppelt"))
				doppelt = true;
			else if(textKey.toLowerCase().equals("länge"))
				laenge = Integer.parseInt(value);
			else
				vial.add(new CError("Unbekannter Wert: " + textKey, errStart, errEnd));
		}catch(Exception e)
		{
			vial.add(new CError("Invalides Setzen eines Werts", errStart, errEnd));
		}
	}

	public void speichern(StringBuilder sb)
	{
		super.speichern(sb);
		if(doppelt)
			speichernZ(sb, "doppelt", null);
		speichernZ(sb, "länge", String.valueOf(laenge));
	}

	@Override
	public ItemD saveState()
	{
		return new ItemD(2, level, priority, laenge, doppelt ? 2 : 1);
	}
}