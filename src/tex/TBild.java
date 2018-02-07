package tex;

import area.*;
import java.awt.image.*;
import java.io.*;
import java.util.zip.*;

class TBild
{
	final BufferedImage b;
	final int min;
	final int max;

	TBild(File f, int min, int max)
	{
		b = Lader5.bild(f);
		this.min = min;
		this.max = max;
	}

	TBild(ZipEntry f, int min, int max)
	{
		b = Lader5.bild(f);
		this.min = min;
		this.max = max;
	}
}