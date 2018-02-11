package block;

import area.*;
import block.item.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.*;
import shift.*;
import tex.*;

public class ShowItems
{
	SchalterR schalterR;
	public List<Item> showItems;
	int chosen1;
	boolean locked;

	public ShowItems(SchalterR schalterR)
	{
		this.schalterR = schalterR;
		reset();
	}

	public void reset()
	{
		chosen1 = -1;
		locked = false;
		generateShowItems();
	}

	void generateShowItems()
	{
		showItems = Arrays.stream(schalterR.items).filter(Objects::nonNull).collect(Collectors.toList());
	}

	boolean itemauswahl()
	{
		for(int i = 0; i <= 9; i++)
			if(TA.take[48 + i] == 2)
			{
				if(i == 9)
				{
					int start = 8;
					if(chosen1 >= 9 && chosen1 < 15)
						start = chosen1;
					for(int j = 1; j <= 6; j++)
					{
						int idx = start + j;
						if(idx >= 15)
							idx -= 6;
						if(schalterR.items[idx] != null)
						{
							chosen1 = idx;
							locked = false;
							return true;
						}
					}
				}
				else if(schalterR.items[i] != null)
				{
					if(chosen1 == i)
						locked = !locked;
					else
					{
						chosen1 = i;
						locked = false;
					}
					return true;
				}
			}
		return false;
	}

	boolean itemauswahl(int nummer)
	{
		if(nummer < showItems.size())
		{
			if(TA.take[201] == 2)
			{
				int id = showItems.get(nummer).id;
				if(chosen1 == id)
				{
					if(locked)
						chosen1 = -1;
					locked = !locked;
				}
				else
				{
					chosen1 = id;
					locked = false;
				}
				return true;
			}
			if(TA.take[203] == 2)
			{
				showItems.get(nummer).disabled = !showItems.get(nummer).disabled;
				return true;
			}
		}
		if(TA.take[201] == 2)
		{
			chosen1 = -1;
			locked = false;
		}
		return false;
	}

	public ArrayList<Ziel> anzielbar()
	{
		ArrayList<Ziel> eintrag = new ArrayList<>();
		if(chosen1 >= 0)
			eintrag.addAll(schalterR.items[chosen1].g1);
		else
			for(int i = 0; i < SchalterR.itemtypes; i++)
				if(schalterR.items[i] != null)
					eintrag.addAll(schalterR.items[i].g1);
		Collections.sort(eintrag);
		return eintrag;
	}

	public void actionTaken()
	{
		if(!locked)
			chosen1 = -1;
	}

	public void removeUnused()
	{
		if(chosen1 >= 0 && schalterR.items[chosen1] == null)
		{
			chosen1 = -1;
			locked = false;
		}
	}

	public void rahmen(Graphics2D gd, Texturen tex, int w1, int ht)
	{
		if(showItems.size() <= 4)
		{
			for(int i = 0; i < showItems.size(); i++)
			{
				gd.drawImage(tex.bilder2D.get(showItems.get(i).bildname()), w1 + ht, ht * 2 * i, ht * 2, ht * 2, null);
				if(showItems.get(i).disabled || (locked && !showItems.get(i).equals(schalterR.items[chosen1])))
					gd.drawLine(w1 + ht, ht * 2 * i, w1 + ht * 3 - 1, ht * 2 * (i + 1) - 1);
			}
			if(chosen1 >= 0)
			{
				int akp = showItems.indexOf(schalterR.items[chosen1]);
				if(akp >= 0)
					gd.drawRect(w1 + ht, ht * 2 * akp, ht * 2 - 1, ht * 2 - 1);
			}
		}
		else
		{
			for(int i = 0; i < showItems.size(); i++)
			{
				gd.drawImage(tex.bilder2D.get(showItems.get(i).bildname()), w1 + ht + (i % 2) * ht, ht * (i / 2), ht, ht, null);
				if(showItems.get(i).disabled || (locked && !showItems.get(i).equals(schalterR.items[chosen1])))
					gd.drawLine(w1 + ht + (i % 2) * ht, ht * (i / 2), w1 + ht + (i % 2 + 1) * ht - 1, ht * (i / 2 + 1) - 1);
			}
			if(chosen1 >= 0)
			{
				int akp = showItems.indexOf(schalterR.items[chosen1]);
				if(akp >= 0)
					gd.drawRect(w1 + ht + (akp % 2) * ht, ht * (akp / 2), ht - 1, ht - 1);
			}
		}
	}
}