package laderLC;

import java.awt.image.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import javax.imageio.*;

public class Lader4
{
	public static Map<String, Object> map = new HashMap<>();

	public static String bauName(String... teile)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < teile.length - 1; i++)
			sb.append(teile[i]).append(File.separatorChar);
		sb.append(teile[teile.length - 1]);
		return sb.toString();
	}

	public static String readR(String s)
	{
		try
		{
			return new String(Files.readAllBytes(Paths.get(s)), Charset.forName("UTF-8"));
		}
		catch(Exception e)
		{
			throw new RuntimeException();
		}
	}

	public static BufferedImage readR2(String s)
	{
		try
		{
			return ImageIO.read(new File(s));
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static String readText(String s, boolean save)
	{
		String toR;
		if(save)
		{
			String s2 = "Text " + s;
			if(map.containsKey(s2))
				toR = (String)map.get(s2);
			else
			{
				toR = readR(s);
				map.put(s2, toR);
			}
		}
		else
			toR = readR(s);
		return toR;
	}

	public static BufferedImage bild(String s, boolean save)
	{
		BufferedImage toR;
		if(save)
		{
			String s2 = "Bild " + s;
			if(map.containsKey(s2))
				toR = (BufferedImage) map.get(s2);
			else
			{
				toR = readR2(s);
				map.put(s2, toR);
			}
		}
		else
			toR = readR2(s);
		return toR;
	}
}