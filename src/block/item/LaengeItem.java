package block.item;

import area.*;
import laderLC.*;

public abstract class LaengeItem extends Item
{
	int laenge;

	public LaengeItem(){}

	public LaengeItem(int level, int laenge)
	{
		super(level);
		this.laenge = laenge;
	}

	@Override
	public boolean benutze(int num, boolean cl, boolean main, boolean lvm)
	{
		//noinspection SimplifiableIfStatement
		if(!cl && !main && TA.take[16] <= 0 && level >= 0)
			return false;
		return super.benutze(num, cl, main, true);
	}

	public void lies2(String value, Integer errStart, Integer errEnd, ErrorVial vial, String textKey)
	{
		try
		{
			if(textKey.toLowerCase().equals("level"))
				level = Integer.parseInt(value);
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
		speichernZ(sb, "länge", String.valueOf(laenge));
	}
}