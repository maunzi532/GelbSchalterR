package gelb;

import area.*;
import java.util.*;
import tex.*;

class GFeld extends GLFeld implements Feld
{
	boolean aktiviert;

	public static GFeld copy(GLFeld von)
	{
		GFeld copy = new GFeld();
		copy.hoehe = von.hoehe;
		copy.gelb = von.gelb;
		copy.lift = von.lift;
		System.arraycopy(von.darauf, 0, copy.darauf, 0, von.darauf.length);
		copy.treppe = von.treppe;
		copy.nTyp = von.nTyp;
		if(copy.lift && copy.nTyp)
			copy.aktiviert = true;
		return copy;
	}

	public Integer getH(int side)
	{
		if(treppe == side)
			return hoehe + 1;
		return hoehe;
	}

	public int daraufH()
	{
		return getJH();
	}

	@Override
	public int markH()
	{
		return getJH();
	}

	public int getJH()
	{
		if(treppe > 0)
			return hoehe + 1;
		return hoehe;
	}

	@Override
	public ArrayList<Render> addToRender(RenderCreater rc, boolean hier, boolean preview)
	{
		rc.sh = daraufH();
		boolean[] da = darauf;
		String dire = "Höhe" + hoehe;
		if(gelb)
			dire = dire + "G";
		if(treppe > 0)
			if(nTyp)
				dire = dire + "R" + treppe;
			else
				dire = dire + "T" + treppe;
		if(lift)
			if(aktiviert)
				dire = dire + "F";
			else
				dire = dire + "L";
		rc.addw(dire);
		if(da[0])
			rc.addw("Gelbgeher");
		if(da[1])
			rc.addw("Springer");
		if(da[2])
			if(aktiviert)
				rc.addw("TeleA");
			else
				rc.addw("TeleI");
		if(da[3])
			rc.addw("Liftfahrer");
		return rc.renders;
	}
}