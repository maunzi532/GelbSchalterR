package block.state;

import area.*;
import block.*;
import java.util.*;
import shift.*;

public class ASIN
{
	private static ArrayList<BState> states;
	private static int caret1;
	private static ArrayList<ArrayList<Integer>> pointers;
	private static SchalterR schalterR;

	private static int[] ws;

	public static void run()
	{
		states = new ArrayList<>();
		caret1 = 0;
		boolean nca = true;
		int caret2 = 0;
		pointers = new ArrayList<>();
		schalterR = (SchalterR) SIN.area;

		states.add(new BState(schalterR));
		pointers.add(new ArrayList<>());
		while(caret1 < states.size())
		{
			if(states.get(caret1).gewonnen)
			{
				nca = true;
				caret1++;
				caret2 = 0;
				pointers.add(new ArrayList<>());
				continue;
			}
			states.get(caret1).charge(schalterR);
			schalterR.srd.reset2(schalterR);
			Shift.localReset(schalterR.d3c());

			SIN.area.checkFields();
			if(nca)
			{
				nca = false;
				if(SIN.testmode == 2)
				{
					Shift.moveToTarget(schalterR.srd);
					SIN.drawX();
					do
					{
						U.warte(20);
						TA.bereit();
					}while(TA.take[16] != 2 && TA.take[65] <= 0);
				}
				else
					System.out.println(states.size());
			}
			if(schalterR.moveR(caret2))
			{
				caret2++;
				BState nst = new BState(schalterR);
				int ind = states.indexOf(nst);
				if(ind >= 0)
					pointers.get(caret1).add(ind);
				else
				{
					pointers.get(caret1).add(states.size());
					states.add(nst);
				}
			}
			else
			{
				nca = true;
				caret1++;
				caret2 = 0;
				pointers.add(new ArrayList<>());
			}
		}
		System.out.println(states.size());
		ws();
	}

	private static void ws()
	{
		ws = new int[states.size()];
		int tcl = ws.length;
		ArrayList<Integer> newws = new ArrayList<>();

		int weg = 0;
		for(int i = 0; i < ws.length; i++)
			if(states.get(i).gewonnen)
			{
				newws.add(i);
				tcl--;
			}
			else
				ws[i] = -1;
		while(tcl > 0 && !newws.isEmpty())
		{
			ArrayList<Integer> newws2 = new ArrayList<>();
			weg++;
			for(int i = 0; i < ws.length; i++)
				if(ws[i] < 0)
				{
					for(Integer p : pointers.get(i))
						if(newws.contains(p))
						{
							ws[i] = weg;
							newws2.add(i);
							tcl--;
							break;
						}
				}
			newws = newws2;
		}
		if(newws.isEmpty())
			System.out.println("rip " + tcl);
		else
			System.out.println("yay");
		System.out.println(ws[0]);
		if(newws.isEmpty())
			showDeadEnds();
		if(ws[0] >= 0)
			showFastPath();
	}

	private static void showDeadEnds()
	{
		for(int i = 0; i < ws.length; i++)
			if(ws[i] < 0)
			{
				states.get(i).charge(schalterR);
				schalterR.srd.reset2(schalterR);
				Shift.localReset(schalterR.d3c());
				Shift.moveToTarget(schalterR.srd);
				SIN.drawX();
				System.out.println(i);
				TA.bereit();
				while(TA.take[16] <= 0)
				{
					U.warte(20);
					TA.bereit();
				}
			}
	}

	private static void showFastPath()
	{
		schalterR.gewonnen = false;
		caret1 = 0;
		states.get(caret1).charge(schalterR);
		schalterR.srd.reset(schalterR);
		Shift.localReset(schalterR.d3c());
		for(int i = 0; i < 5; i++)
		{
			Shift.moveToTarget(schalterR.srd);
			SIN.drawX();
			U.warte(20);
		}
		schalterR.srd.dspeed = 0.25;
		schalterR.srd.mspeed = 6;
		int noch = ws[0];
		while(!states.get(caret1).gewonnen)
		{
			Shift.moveToTarget(schalterR.srd);
			SIN.drawX();
			/*do
			{
				U.warte(20);
				TA.bereit();
			}while(TA.take[16] != 2);*/
			U.warte(100);
			schalterR.checkFields();
			int c2 = 0;
			for(int i = 0; i < pointers.get(caret1).size(); i++)
				if(ws[pointers.get(caret1).get(i)] < noch)
				{
					c2 = i;
					break;
				}
			caret1 = pointers.get(caret1).get(c2);
			noch = ws[caret1];
			schalterR.moveR(c2);
			Shift.selectTarget(schalterR.d3c());
			BState check = new BState(schalterR);
			if(!states.get(caret1).equals(check))
			{
				System.out.println("Fehler");
				states.get(caret1).charge(schalterR);
			}
		}
		schalterR.noMovement();
		for(int i = 0; i < 20; i++)
		{
			if(schalterR.gewonnen)
				schalterR.victoryTick();
			Shift.moveToTarget(schalterR.srd);
			SIN.drawX();
			U.warte(20);
		}
	}
}