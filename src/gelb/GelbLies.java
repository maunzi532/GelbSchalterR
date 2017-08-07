package gelb;

class GelbLies
{
	GLFeld[][] feld;
	int xw, yw, xs, ys, xz, yz;

	void liesA(String c1)
	{
		try
		{
			c1 = c1.replaceAll("\\s", "");
			char[] c = c1.toCharArray();
			String loaded = "";
			int no1 = 0;
			int no2 = 0;
			int x1 = 0;
			int x2;
			for(int i = 0; i < c.length; i++)
			{
				if(c[i] == ';')
				{
					no1++;
					if(no1 <= 3)
					{
						x2 = Integer.parseInt(loaded);
						loaded = "";
						switch(no1)
						{
							case 1:
								xw = x1;
								yw = x2;
								break;
							case 2:
								xs = x1;
								ys = x2;
								break;
							case 3:
								xz = x1;
								yz = x2;
								break;
						}
					}
					else if(no1 == 4)
					{
						feld = new GLFeld[yw][xw];
						loaded = "";
					}
				}
				else if(c[i] == 'x')
				{
					x1 = Integer.parseInt(loaded);
					loaded = "";
				}
				else if(c[i] == ',')
				{
					feld[no2 / xw][no2 % xw] = new GLFeld(loaded);
					loaded = "";
					no2++;
				}
				else
					loaded = loaded + c[i];
			}
			feld[no2 / xw][no2 % xw] = new GLFeld(loaded);
		}catch(Exception e)
		{
			System.out.println("Fehler beim Lesen der Datei");
			e.printStackTrace();
			System.exit(2);
		}
	}
}