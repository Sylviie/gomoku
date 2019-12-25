package gomoku;

import java.util.Scanner;

/**
 * 2-15 B-O
 * @author Cheng Xinyun
 *  function: gomoku homme vs homme
 * x: black O: white
 */
public class HommevsHomme {

	String nameWhite;
	String nameBlack;
	Scanner sc;


	public void calculTour(int currentPlayer) {
		// tester tour si current est 0, c'est le tour de pion noir sinon pion blanc
		String playerName = (currentPlayer == 0) ? nameBlack : nameWhite;
		System.out.println(playerName + " peut déposer le pion: ");
	}

	public void printTable(int[][] table, int size) {
		System.out.print("   ");
        // chiffres
		for (int i = 1; i < size-1; i++) {
			int value = i+1;
			System.out.print(value < 10 ? value + "  " : value + " ");
		}
		System.out.println();
        // lettres
		for (int i = 1; i < size-1; i++) {
			char title = (char) ('A' + i);
			System.out.print(title + "  ");
			for (int j = 1; j < size-1; j++) {
				int value = table[i][j];   //éviter l'examen de limite
				char c = ' ';
				switch (value) {
				case 0:
					c = '.';
					break;
				case 1:
					c = 'X';
					break;
				case 2:
					c = 'O';
					break;
				}
				System.out.print(c + "  ");
			}
			System.out.println();
		}
	}
	
	public void run() {
		// input name of player
		sc = new Scanner(System.in);
		System.out.print("Entrez le nom du joueur 1: ");
		nameBlack = sc.nextLine();
		System.out.print("Entrez le nom du joueur 2: ");
		nameWhite = sc.nextLine();

		// print plateau

		int size = 17;
		int[][] table = new int[size][size];
		printTable(table, size);

		// play
		int win = 0;
		int currentPlayer = 0;
		do {

			calculTour(currentPlayer);

			// coordonnées
			int x, y;
			boolean canPlace = true;
			do {
				if (!canPlace) {
					System.out.println("Vous ne pouvez pas déposer votre pion ici.");
				}
				String code = sc.nextLine();
				y = code.charAt(0) - 'A';
				x = Integer.valueOf(code.substring(1)) - 1;

				canPlace = true;
				// tester coordonnées
				canPlace = canPlace && x >= 1 && x < size-1;
				canPlace = canPlace && y >= 1 && y < size-1;
				canPlace = canPlace && table[y][x] == 0;

			} while (!canPlace);

			// déposer les pions
			// si currentPlayer est 0, 1 pour le pion noir et 2 pour pion blanc
			table[y][x] = currentPlayer == 0 ? 1 : 2;
			currentPlayer++;
			currentPlayer %= 2;

			// print plateau
			printTable(table, size);

			// test win or lost
			win = checkWin(table, x, y);

		} while (win == 0);// to player win or lost

		// print result
		if (win == 1) {
			System.out.println(nameBlack + " gagne !");
		} else if (win == 2) {
			System.out.println(nameWhite + " gagne !");
		} else if (win == 3) {
			System.out.println("Personne gagne ! ");
		}

	}


	public int checkWin(int[][] table, int x, int y) 
	{
		// On n'a pas besoin de regarder tout le plateau
		// tester horizontalement
		// .
		// O O X X X X X
		// . <-
		// -> . : (droite + gauche)- 1
		// tester verticalment
		// tester diagonalement
		
		boolean test=false; //4 directions
		test = test || plusCinq(table, x, y, 0, -1);//up
		test = test || plusCinq(table, x, y, 1, -1);//right up
		test = test || plusCinq(table, x, y, 1, 0); //right
		test = test || plusCinq(table, x, y, 1, 1); //right down
		
		
		if (test)
		{
			return table[y][x];
		}
		
		return 0;
	}

	//tester si on a cinq pions ou plus
	public boolean plusCinq(int[][]table, int x, int y, int dx, int dy)
	{
		
		int count =0;
	
		count += compte(table, x, y, dx, dy);  //direction
		count += compte(table, x, y, -dx, -dy);  //direction inverse
		count -= 1;
		
		return count >=5;
		
	}
	
	//compter le nombre de pions
	public int compte(int [][] table, int originX, int originY, int deplaceX, int deplaceY) {
		int originValue = table[originY][originX];// ce que l'on dépose 
		int count = 0;
		int x = originX;
		int y = originY;
		
		int value;
		
			do {
				count++;
				
					originX += deplaceX; //déplacer dans 4 directions (abstraction)
					originY += deplaceY;
				
				value = table[originY][originX];  
			}while(value == originValue);
			
		return count;
	}
	public static void main(String[] args) {

		new HommevsHomme().run();
	}

}
