package tex;

import area.*;
import java.awt.image.*;
import java.io.*;

class TBild
{
	final BufferedImage b;
	final int min;
	final int max;

	TBild(File f, int min, int max)
	{
		b = U.readImage(f);
		this.min = min;
		this.max = max;
	}
}