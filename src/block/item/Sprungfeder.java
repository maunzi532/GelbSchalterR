package block.item;

import block.*;
import block.state.*;

public class Sprungfeder extends LaengeItem
{
	public Sprungfeder()
	{
		level = 1;
		laenge = 2;
	}

	public Sprungfeder(int level, int laenge)
	{
		super(level, laenge);
	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		Sprungfeder i1 = new Sprungfeder(level, laenge);
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
				if(xp + i * xm < 0 || yp + i * ym < 0 || xp + i * xm >= schalterR.xw || yp + i * ym >= schalterR.yw)
					break;
				BFeld f = schalterR.feld[yp + i * ym][xp + i * xm];
				if(f.getAH() > hp)
					break;
				if(i == laenge && f.bodenH() == hp)
					option(xp + i * xm, yp + i * ym, hp, r + 1);
			}
		}
	}

	@Override
	public String bildname()
	{
		return "Sprungfeder";
	}

	@Override
	public ItemD saveState()
	{
		return new ItemD(4, level, laenge);
	}
}