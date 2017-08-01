package block.item;

import block.*;
import block.state.*;

public class CheatMovement extends Item
{
	private final Cheatmode cheatmode;

	public CheatMovement(Cheatmode cheatmode)
	{
		this.cheatmode = cheatmode;
		level = -1;
	}

	@Override
	public Item kopie(SchalterR schalterR)
	{
		CheatMovement i1 = new CheatMovement(cheatmode);
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
		option(xp, yp, hp + (f.bodenH() < hp ? -1 : 1), 0);
		for(int r = 0; r <= 3; r++)
		{
			int xm = r != 3 ? r - 1 : 0;
			int ym = r != 0 ? r - 2 : 0;
			if(xp + xm < 0 || yp + ym < 0 || xp + xm >= xw || yp + ym >= yw)
				continue;
			option(xp + xm, yp + ym, hp, r + 1);
		}
	}

	@Override
	public boolean benutze(int num, boolean cl, boolean main, boolean lvm)
	{
		if(cheatmode.pfadmodus)
			schalterR.angleichen();
		return super.benutze(num, cl, main, lvm);
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