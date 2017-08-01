package block.item;

import block.*;
import block.state.*;

public class Fluegel extends LaengeItem
{
	public Fluegel()
	{
		level = 2;
		laenge = 5;
	}

	public Fluegel(int level, int laenge)
	{
		super(level, laenge);
	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		Fluegel i1 = new Fluegel(level, laenge);
		i1.schalterR = schalterR;
		return i1;
	}

	@Override
	public void setzeOptionen(int xp, int yp, int hp, int xw, int yw)
	{
		for(int r = 0; r <= 3; r++)
		{
			int xm = r != 3 ? r - 1 : 0;
			int ym = r != 0 ? r - 2 : 0;
			for(int i = 1; i <= laenge; i++)
			{
				if(xp + i * xm < 0 || yp + i * ym < 0 || xp + i * xm >= xw || yp + i * ym >= yw)
					break;
				BFeld f = schalterR.feld[yp + i * ym][xp + i * xm];
				if(f.getBlockedH() > hp)
					break;
				if(f.bodenH() == hp)
				{
					if(i > 1)
						option(xp + i * xm, yp + i * ym, hp, r + 1);
					break;
				}
			}
		}
	}

	@Override
	public String speichername()
	{
		return "flügel";
	}

	@Override
	public String bildname()
	{
		return "Flügel";
	}

	@Override
	public ItemD saveState()
	{
		return new ItemD(5, level, laenge);
	}
}