package area;

import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.zip.*;
import javax.imageio.*;

public class Lader5
{
	public static File jarLocation;
	public static boolean jar;
	public static Path dataRoot1;
	public static ZipFile dataRoot2;
	public static Map<String, List<ZipEntry>> deep;
	public static Map<String, ZipEntry> notDeep;

	public static void inJarCheck()
	{
		try
		{
			URL url = Lader5.class.getResource("Lader5.class");
			jar = url.getProtocol().equals("jar");
			String dir = "";
			if(jar)
			{
				String jarname = url.getPath().substring(url.getPath().indexOf(":") + 1, url.getPath().indexOf("!"));
				jarLocation = new File(jarname).getParentFile();
				dataRoot2 = new ZipFile(jarname);
			}
			else
			{
				dataRoot1 = Paths.get(dir);
				jarLocation = new File(dir);
			}
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void prepareData(String dir)
	{
		if(!jar)
			return;
		List<ZipEntry> relevant = dataRoot2.stream()
				.filter(e -> e.getName().startsWith(dir) && !e.getName().endsWith("/")).collect(Collectors.toList());
		deep = relevant.stream().filter(e -> e.getName().split("/").length == 4)
				.collect(Collectors.groupingBy(e -> e.getName().substring(dir.length(), e.getName().lastIndexOf("/"))));
		notDeep = relevant.stream().filter(e -> e.getName().split("/").length == 3)
				.collect(Collectors.toMap(e -> e.getName().substring(dir.length()), Function.identity()));
	}

	public static String text(String location)
	{
		try
		{
			if(jar)
			{
				InputStream is = dataRoot2.getInputStream(dataRoot2.getEntry(location));
				ByteArrayOutputStream result = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int length;
				while((length = is.read(buffer)) != -1)
				{
					result.write(buffer, 0, length);
				}
				return result.toString(StandardCharsets.UTF_8.name());
			}
			else
			{
				return new String(Files.readAllBytes(dataRoot1.resolve(location)), Charset.forName("UTF-8"));
			}
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static BufferedImage bild(File location)
	{
		try
		{
			return ImageIO.read(dataRoot1.resolve(location.toPath()).toFile());
		}
		catch(IOException e)
		{
			return null;
		}
	}

	public static BufferedImage bild(ZipEntry location)
	{
		try
		{
			return ImageIO.read(dataRoot2.getInputStream(location));
		}
		catch(IOException e)
		{
			return null;
		}
	}

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