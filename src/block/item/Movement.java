package block.item;

import block.*;
import block.state.*;

public class Movement extends Item
{
	public Movement()
	{
		level = -1;
	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		Movement i1 = new Movement();
		i1.schalterR = schalterR;
		return i1;
	}

	@Override
	public boolean weg()
	{
		return false;
	}

	@Override
	public void setzeOptionen(int xp, int yp, int hp, int xw, int yw)
	{
		BFeld f = schalterR.feld[yp][xp];
		if(f.lift)
			option(xp, yp, hp + (f.liftOben() ? -1 : 1), 0);
		for(int r = 0; r <= 3; r++)
		{
			int xm = r != 3 ? r - 1 : 0;
			int ym = r != 0 ? r - 2 : 0;
			if(xp + xm < 0 || yp + ym < 0 || xp + xm >= xw || yp + ym >= yw)
				continue;
			if(schalterR.feld[yp][xp].weggehengeht(r, hp) && schalterR.feld[yp + ym][xp + xm].betretengeht((r + 2) % 4, hp))
				option(xp + xm, yp + ym, hp, r + 1);
		}
	}

	@Override
	public String bildname()
	{
		return "Spieler";
	}

	@Override
	public ItemD saveState()
	{
		return new ItemD(0);
	}
}