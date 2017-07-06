package area;

import block.*;
import gelb.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import javax.swing.*;

class M
{
	private static boolean g = false;
	private static String texPack;
	private static String texOrdnerName = "Texturen2";

	public static void main(String[] args)
	{
		if(args.length == 0)
		{
			JFileChooser fc = new JFileChooser(new File("saves"));
			fc.showOpenDialog(null);
			start(fc.getSelectedFile());
		}
		else
			start(new File("saves" + File.separator + args[0] + ".txt"));
		System.exit(0);
	}

	private static void start(File lv)
	{
		String input = null;
		try
		{
			input = new String(Files.readAllBytes(lv.toPath()), Charset.forName("UTF-8"));
		}
		catch(Exception e)
		{
			System.out.println("Nicht gefunden");
			System.exit(1);
		}
		if(g)
		{
			texPack = "Default";
			Area area = new Gelb();
			area.readFL(input);
			area.reset();
			Texturen tex = new GTex(texPack, texOrdnerName);
			SIN.start(area, tex);
		}
		else
		{
			texPack = "BlockLab";
			Area area = new BlockLab();
			area.readFL(input);
			area.reset();
			Texturen tex = new FTex(texPack, texOrdnerName);
			SIN.start(area, tex);
		}
	}
}