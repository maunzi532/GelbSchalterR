package block.state;

import area.*;
import block.*;
import block.item.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class BState implements Serializable
{
	final boolean gewonnen;
	private final int x, y, z;
	private final char farbe;
	private final int eRichtung;
	final int dias;
	private final List<BlockD> bd;
	private final ItemD[] items;

	public BState(SchalterR schalterR)
	{
		gewonnen = schalterR.gewonnen;
		x = schalterR.xp;
		y = schalterR.yp;
		z = schalterR.hp;
		farbe = schalterR.farbeAktuell;
		eRichtung = schalterR.ebeneRichtung;
		dias = schalterR.dias;
		bd = new ArrayList<>();
		for(int iy = 0; iy < schalterR.yw; iy++)
			for(int ix = 0; ix < schalterR.xw; ix++)
				if(schalterR.feld[iy][ix].benutzt)
					bd.add(new BlockD(ix, iy));
		items = new ItemD[SchalterR.itemtypes];
		for(int i = 0; i < SchalterR.itemtypes; i++)
			if(schalterR.items[i] != null)
				items[i] = schalterR.items[i].saveState();
	}

	public void charge(SchalterR schalterR)
	{
		schalterR.xp = x;
		schalterR.yp = y;
		schalterR.hp = z;
		schalterR.farbeAktuell = farbe;
		schalterR.ebeneRichtung = eRichtung;
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
		for(int i = 1; i < SchalterR.itemtypes; i++)
			if(items[i] != null)
				schalterR.items[i] = items[i].toItem().kopie(schalterR);
			else
				schalterR.items[i] = null;
		if(schalterR.items[8] != null)
		{
			FahrendeEbene fe = (FahrendeEbene) schalterR.items[8];
			schalterR.feld[fe.start.y][fe.start.x].fahrebene1 = fe;
		}
		for(int i = 9; i < 15; i++)
			if(schalterR.items[i] != null)
			{
				Wuerfel wu = (Wuerfel) schalterR.items[i];
				if(wu.ort != null)
					schalterR.feld[wu.ort.y][wu.ort.x].wuerfel1 = wu;
			}
	}

	public void speichernState()
	{
		JFileChooser fc = new JFileChooser(Lader7.jarLocation.getAbsoluteFile());
		if(fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				ObjectOutputStream sv = new ObjectOutputStream(new FileOutputStream(fc.getSelectedFile()));
				sv.writeObject(this);
				sv.close();
			}catch(IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		TA.fix();
	}

	public static BState ladenState()
	{
		JFileChooser fc = new JFileChooser(Lader7.jarLocation.getAbsoluteFile());
		boolean sh = TA.take[16] > 0;
		if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				ObjectInputStream sv = new ObjectInputStream(new FileInputStream(fc.getSelectedFile()));
				BState re = (BState) sv.readObject();
				sv.close();
				TA.fix();
				return re;
			}catch(IOException | ClassNotFoundException e)
			{
				throw new RuntimeException(e);
			}
		}
		TA.fix();
		return null;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof BState)) return false;
		BState bState = (BState) o;
		if(x != bState.x || y != bState.y || z != bState.z || farbe != bState.farbe || eRichtung != bState.eRichtung || dias != bState.dias)
			return false;
		if(bd.size() != bState.bd.size())
			return false;
		for(int i = 0; i < bd.size(); i++)
			if(!bd.get(i).equals(bState.bd.get(i)))
				return false;
		for(int i = 0; i < SchalterR.itemtypes; i++)
			if(!Objects.equals(items[i], bState.items[i]))
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
		result = 31 * result + eRichtung;
		result = 31 * result + dias;
		result = 31 * result + bd.hashCode();
		result = 31 * result + Arrays.hashCode(items);
		return result;
	}

	public boolean strictlyBetter(BState bState)
	{
		if(x != bState.x || y != bState.y || z != bState.z || farbe != bState.farbe || eRichtung != bState.eRichtung || dias > bState.dias)
			return false;
		if(bd.size() > bState.bd.size())
			return false;
		for(int i = 0; i < bd.size(); i++)
			if(!bState.bd.contains(bd.get(i)))
				return false;
		for(int i = 0; i < SchalterR.itemtypes; i++)
			if((bState.items[i] == null || items[i] != null) && !Objects.equals(items[i], bState.items[i]))
				return false;
		return true;
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
				", eRichtung=" + eRichtung +
				", dias=" + dias +
				", bd=" + bd +
				", items=" + Arrays.toString(items) +
				'}';
	}
}