package area;

import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.stream.*;
import javax.imageio.*;

public class Lader7
{
	public static File jarLocation;
	public static boolean jar;

	public static void inJarCheck()
	{
		{
			URL url = Lader7.class.getResource("Lader7.class");
			jar = url.getProtocol().equals("jar");
			String dir = "";
			if(jar)
			{
				String jarname = url.getPath().substring(url.getPath().indexOf(":") + 1, url.getPath().indexOf("!"));
				try
				{
					jarLocation = new File(URLDecoder.decode(jarname, StandardCharsets.UTF_8.name())).getParentFile().getAbsoluteFile();
				}catch(UnsupportedEncodingException e)
				{
					throw new RuntimeException(e);
				}
			}
			else
			{
				jarLocation = new File(dir).getAbsoluteFile();
			}
		}
	}

	public static String textSavedata(String location)
	{
		try
		{
			return new String(Files.readAllBytes(jarLocation.toPath().resolve(location)), StandardCharsets.UTF_8);
		}
		catch(IOException | NullPointerException e)
		{
			return null;
		}
	}

	public static void textStore(String location, String text)
	{
		try
		{
			Files.write(jarLocation.toPath().resolve(location), text.getBytes(StandardCharsets.UTF_8));
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public static BufferedImage imageSavedata(String location)
	{
		try
		{
			return ImageIO.read(jarLocation.toPath().resolve(location).toFile());
		}
		catch(IOException | NullPointerException e)
		{
			return null;
		}
	}

	public static void imageStore(String location, BufferedImage image)
	{
		try
		{
			ImageIO.write(image, "png", jarLocation.toPath().resolve(location).toFile());
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public static String textResource(String location)
	{
		try
		{
			return new BufferedReader(new InputStreamReader(getResourceAsStream(location), StandardCharsets.UTF_8))
					.lines().collect(Collectors.joining("\n"));
		}
		catch(NullPointerException e)
		{
			return null;
		}
	}

	public static BufferedImage imageResource(String location)
	{
		try
		{
			return ImageIO.read(getResourceAsStream(location));
		}
		catch(IOException | NullPointerException | IllegalArgumentException e)
		{
			return null;
		}
	}

	private static InputStream getResourceAsStream(String resource)
	{
		final InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
		return in == null ? Lader7.class.getClassLoader().getResourceAsStream(resource) : in;
	}

}