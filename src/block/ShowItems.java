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
					{
						if(locked)
							chosen1 = -1;
						locked = !locked;
					}
					else
					{
						chosen1 = i;
						locked = false;
					}
					return true;
				}
				else
				{
					chosen1 = -1;
					locked = false;
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
		int akp = -1;
		if(chosen1 >= 0)
			akp = showItems.indexOf(schalterR.items[chosen1]);
		if(showItems.size() <= 4)
		{
			gd.setFont(new Font(null, Font.PLAIN, ht / 2));
			for(int i = 0; i < showItems.size(); i++)
				drawItem(gd, tex, w1 + ht, ht * 2 * i, ht * 2, showItems.get(i), akp == i);
		}
		else
		{
			gd.setFont(new Font(null, Font.PLAIN, ht / 4));
			for(int i = 0; i < showItems.size(); i++)
				drawItem(gd, tex, w1 + ht + (i % 2) * ht, ht * (i / 2), ht, showItems.get(i), akp == i);
		}
	}

	private static final Color[] edgc = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.WHITE};
	private static final Color[] edgc1 = new Color[]{Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK};

	public void drawItem(Graphics2D gd, Texturen tex, int xc, int yc, int w, Item item, boolean chosen)
	{
		gd.drawImage(tex.bilder2D.get(item.bildname()), xc, yc, w - 1, w - 1, null);
		FontMetrics fm = gd.getFontMetrics();
		int in = w / 8;
		for(int i = 0; i < 4; i++)
		{

			String edge = item.edgeText(i);
			if(edge == null)
				continue;
			int xp;
			if(i % 2 == 0)
				xp = xc + in;
			else
				xp = xc + w - 1 - in - fm.stringWidth(edge);
			int yp;
			if(i >= 2)
				yp = yc + w - 1 - in;
			else
				yp = yc + in + fm.getHeight();
			gd.setColor(edgc1[i]);
			gd.drawString(edge, xp - 1, yp - 1);
			gd.drawString(edge, xp + 1, yp - 1);
			gd.drawString(edge, xp - 1, yp + 1);
			gd.drawString(edge, xp + 1, yp + 1);
			gd.setColor(edgc[i]);
			gd.drawString(edge, xp, yp);
		}
		gd.setColor(Color.RED);
		if(item.disabled || (locked && !item.equals(schalterR.items[chosen1])))
			gd.drawLine(xc, yc, xc + w - 1, yc + w - 1);
		if(chosen)
			gd.drawRect(xc, yc, w - 1, w - 1);
	}
}