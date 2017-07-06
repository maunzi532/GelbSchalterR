package area;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class U
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

	public static BufferedImage readImage(File f)
	{
		try
		{
			return ImageIO.read(f);
		}
		catch(Exception e)
		{
			return null;
		}
	}
}