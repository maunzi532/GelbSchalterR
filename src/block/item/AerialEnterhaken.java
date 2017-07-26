package block.item;

import block.*;
import block.state.*;
import laderLC.*;

public class AerialEnterhaken extends Item
{
	boolean doppelt;
	int laenge;

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
	public Item kopie(BlockLab blockLab)
	{
		AerialEnterhaken i1 = new AerialEnterhaken(level, laenge, doppelt);
		i1.blockLab = blockLab;
		return i1;
	}

	@Override
	public void setzeOptionen(int xp, int yp, int hoeheA)
	{
		int ix = xp - laenge;
		if(ix < 0)
			ix = 0;
		int iy = yp - laenge;
		if(iy < 0)
			iy = 0;
		for(; ix <= xp + laenge && ix < g2[0].length; ix++)
			for(int iy2 = iy; iy2 <= yp + laenge && iy2 < g2.length; iy2++)
			{
				//geht[iy2][ix] = erreichbar(xp, yp, hoeheA, ix, iy2);
				int err = erreichbar(xp, yp, hoeheA, ix, iy2);
				if(err >= 0)
					option(ix, iy2, err, -1);
			}
	}

	public int erreichbar(int xp, int yp, int hoeheA, int ix, int iy)
	{
		BFeld zf = blockLab.feld[iy][ix];
		if(zf.enterstange < 0)
			return -1;
		if(zf.enterstange != zf.bodenH() && !doppelt)
			return -1;
		int xd = (ix - xp) * (ix - xp);
		int yd = (iy - yp) * (iy - yp);
		int zd = (zf.enterstange - hoeheA) * (zf.enterstange - hoeheA);
		if(xd + yd + zd > laenge * laenge)
			return -1;
		int xn = ix > xp ? xp : ix;
		int xh = ix > xp ? ix : xp;
		int yn = iy > yp ? yp : iy;
		int yh = iy > yp ? iy : yp;
		for(; xn <= xh; xn++)
			for(int yn2 = yn; yn2 <= yh; yn2++)
				if(yn2 != yp && xn != xp && blockLab.feld[yn2][xn].getAH() > hoeheA)
					return -1;
		return zf.enterstange;
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
		return new ItemD(2, level, laenge, doppelt ? 2 : 1);
	}
}