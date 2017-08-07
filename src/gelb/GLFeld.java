package gelb;

class GLFeld
{
	int hoehe;
	boolean gelb;
	boolean lift;
	final boolean[] darauf = new boolean[4];
	int treppe;
	boolean nTyp;

	GLFeld(){}

	@SuppressWarnings("ConstantConditions")
	GLFeld(String f)
	{
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
						nTyp = true;
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
}