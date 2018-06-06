package area;

public class S
{

	public static void warte(int m)
	{
		try
		{
			Thread.sleep(m);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}