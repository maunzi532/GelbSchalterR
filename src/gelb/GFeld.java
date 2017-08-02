package gelb;

import area.*;

class GFeld extends GLFeld implements Feld<Gelbgeher>
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

	@Override
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
	public void addToRender(Gelbgeher area, boolean hier, int xcp, int ycp)
	{
		area.sh = daraufH();
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
		area.addw(dire);
		if(da[0])
			area.addw("Gelbgeher");
		if(da[1])
			area.addw("Springer");
		if(da[2])
			if(aktiviert)
				area.addw("TeleA");
			else
				area.addw("TeleI");
		if(da[3])
			area.addw("Liftfahrer");
	}
}