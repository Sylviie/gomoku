package gomoku;

import java.util.Scanner;

/**
 * 
 * @author Cheng Xinyun
 * function: jeu de gomuko
 * modes: homme vs homme, homme vs machine, machine vs machine
 *
 */
public class Gomoku {
	
	Scanner scanner;
	int value;
	
	public void choisirModes()
	{
		//conditions
		scanner = new Scanner(System.in);
		System.out.println("1.Homme vs Homme");
		System.out.println("2.Machine vs Homme");
		System.out.println("3.Machine vs Machine");
		System.out.print("Choisissez une mode de jeu: ");
		value=scanner.nextInt();
		switch(value)
		{
		case 1: new HommevsHomme().run();
		        break;
		case 2: new HommevsMachine_v1().run();
		        break;
		case 3: new HommevsMachine_v1().run();
		        break;
		    
		}
		
	}

	public static void main(String[] args) {
		new Gomoku().choisirModes();

	}

}
