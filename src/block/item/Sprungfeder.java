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
				int xf = xp + i * xm;
				int yf = yp + i * ym;
				if(xf < 0 || yf < 0 || xf >= xw || yf >= yw)
					break;
				BFeld f = schalterR.feld[yf][xf];
				if(f.getBlockH() > hp)
					break;
				if(i == laenge && f.bodenH() == hp)
					option(xf, yf, hp, r + 1);
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