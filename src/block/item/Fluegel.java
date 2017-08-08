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

	public Fluegel(int level, int priority, int laenge)
	{
		super(level, priority, laenge);
	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		Fluegel i1 = new Fluegel(level, priority, laenge);
		i1.schalterR = schalterR;
		return i1;
	}

	@Override
	public void setzeOptionen(int xp, int yp, int hp, int xw, int yw, BFeld fp)
	{
		if(!fp.aufBoden())
			return;
		for(int r = 0; r <= 3; r++)
		{
			int xm = r != 3 ? r - 1 : 0;
			int ym = r != 0 ? r - 2 : 0;
			for(int i = 1; i <= laenge; i++)
			{
				int xf = xp + i * xm;
				int yf = yp + i * ym;
				if(xf < 0 || yf < 0 || xf >= xw || yf >= yw)
					break;
				BFeld f = schalterR.feld[yf][xf];
				if(f.getBlockH() > hp)
					break;
				if(f.aufBoden())
				{
					if(i > 1)
						option(xf, yf, hp, r + 1);
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
		return new ItemD(5, level, priority, laenge);
	}
}