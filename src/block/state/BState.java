package block.state;

import block.*;
import block.item.*;
import java.util.*;
import java.util.stream.*;

public class BState
{
	boolean gewonnen;
	int x, y, z;
	char farbe;
	int dias;
	List<BlockD> bd;
	List<ItemD> items;

	public BState(BlockLab blockLab)
	{
		gewonnen = blockLab.gewonnen;
		x = blockLab.xp;
		y = blockLab.yp;
		z = blockLab.hoeheA;
		farbe = blockLab.farbeAktuell;
		dias = blockLab.dias;
		bd = new ArrayList<>();
		for(int iy = 0; iy < blockLab.yw; iy++)
			for(int ix = 0; ix < blockLab.xw; ix++)
				if(blockLab.feld[iy][ix].benutzt)
					bd.add(new BlockD(ix, iy));
		items = blockLab.items.stream().map(Item::saveState).sorted().collect(Collectors.toList());
	}

	public void charge(BlockLab blockLab)
	{
		blockLab.xp = x;
		blockLab.yp = y;
		blockLab.hoeheA = z;
		blockLab.farbeAktuell = farbe;
		blockLab.dias = dias;
		int caret = 0;
		for(int iy = 0; iy < blockLab.yw; iy++)
			for(int ix = 0; ix < blockLab.xw; ix++)
			{
				blockLab.feld[iy][ix].reset();
				if(caret < bd.size() && ix == bd.get(caret).x && iy == bd.get(caret).y)
				{
					blockLab.feld[iy][ix].benutzt = true;
					caret++;
				}
			}
		blockLab.items.clear();
		items.forEach(itemD -> blockLab.items.add(itemD.toItem().kopie(blockLab)));
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
				", z=" + z +
				", farbe=" + farbe +
				", dias=" + dias +
				", bd=" + bd +
				", items=" + items +
				'}';
	}
}