package block.state;

import block.*;
import block.item.*;
import java.util.*;
import java.util.stream.*;

public class BState
{
	final boolean gewonnen;
	private final int x, y, z;
	private final char farbe;
	private final int dias;
	private final List<BlockD> bd;
	private final List<ItemD> items;

	public BState(SchalterR schalterR)
	{
		gewonnen = schalterR.gewonnen;
		x = schalterR.xp;
		y = schalterR.yp;
		z = schalterR.hp;
		farbe = schalterR.farbeAktuell;
		dias = schalterR.dias;
		bd = new ArrayList<>();
		for(int iy = 0; iy < schalterR.yw; iy++)
			for(int ix = 0; ix < schalterR.xw; ix++)
				if(schalterR.feld[iy][ix].benutzt)
					bd.add(new BlockD(ix, iy));
		items = schalterR.items.stream().map(Item::saveState).sorted().collect(Collectors.toList());
	}

	public void charge(SchalterR schalterR)
	{
		schalterR.xp = x;
		schalterR.yp = y;
		schalterR.hp = z;
		schalterR.farbeAktuell = farbe;
		schalterR.dias = dias;
		int caret = 0;
		for(int iy = 0; iy < schalterR.yw; iy++)
			for(int ix = 0; ix < schalterR.xw; ix++)
			{
				schalterR.feld[iy][ix].reset();
				if(caret < bd.size() && ix == bd.get(caret).x && iy == bd.get(caret).y)
				{
					schalterR.feld[iy][ix].benutzt = true;
					caret++;
				}
			}
		schalterR.items.clear();
		items.forEach(itemD -> schalterR.items.add(itemD.toItem().kopie(schalterR)));
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof BState)) return false;
		BState bState = (BState) o;
		if(x != bState.x || y != bState.y || z != bState.z || farbe != bState.farbe || dias != bState.dias)
			return false;
		if(bd.size() != bState.bd.size() || items.size() != bState.items.size())
			return false;
		for(int i = 0; i < bd.size(); i++)
			if(!bd.get(i).equals(bState.bd.get(i)))
				return false;
		for(int i = 0; i < items.size(); i++)
			if(!items.get(i).equals(bState.items.get(i)))
				return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		int result = x;
		result = 31 * result + y;
		result = 31 * result + z;
		result = 31 * result + (int) farbe;
		result = 31 * result + dias;
		result = 31 * result + bd.hashCode();
		result = 31 * result + items.hashCode();
		return result;
	}

	@Override
	public String toString()
	{
		return "BState{" +
				"gewonnen=" + gewonnen +
				", x=" + x +
				", y=" + y +
				", h=" + z +
				", farbe=" + farbe +
				", dias=" + dias +
				", bd=" + bd +
				", items=" + items +
				'}';
	}
}