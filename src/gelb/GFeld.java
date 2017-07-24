package gelb;

import area.*;

class GFeld implements Feld
{
	int hoehe;
	boolean gelb;
	boolean lift;
	final boolean[] darauf;
	boolean aktiviert;
	int treppe;
	boolean nTyp;

	public GFeld(String f)
	{
		darauf = new boolean[4];
		try
		{
			String zahl = "";
			for(int i = 0; i < f.length(); i++)
			{
				switch(f.charAt(i))
				{
					case 'g':
						darauf[0] = true;
						break;
					case 's':
						darauf[1] = true;
						break;
					case 't':
						darauf[2] = true;
						break;
					case 'l':
						darauf[3] = true;
						break;
					case 'G':
						gelb = true;
						break;
					case 'L':
						lift = true;
						break;
					case 'F':
						lift = true;
						aktiviert = true;
						break;
					case 'T':
						treppe = f.charAt(i + 1) - 48;
						i++;
						break;
					case 'R':
						treppe = f.charAt(i + 1) - 48;
						i++;
						nTyp = true;
						break;
					default:
						zahl = zahl + f.charAt(i);
				}
			}
			hoehe = Integer.parseInt(zahl);
		}
		catch(Exception e)
		{
			System.out.println("Fehler auf Feld: " + f);
			System.exit(3);
		}
	}

	public Integer getH(int side)
	{
		if(treppe == side)
			return hoehe + 1;
		return hoehe;
	}

	public int visualH()
	{
		return getJH();
	}

	public int getJH()
	{
		if(treppe > 0)
			return hoehe + 1;
		return hoehe;
	}

	public void addToRender(Area area, boolean hier, int xcp, int ycp)
	{
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
		if(hier)
			area.addw("Spieler");
	}
}