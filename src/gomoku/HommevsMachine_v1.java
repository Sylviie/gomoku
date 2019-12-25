package gomoku;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/**
 * 
 * @author Cheng Xinyun
 * algorithme: calculer par rapport à la situation actuelle
 */

public class HommevsMachine_v1 {

	String nameWhite;
	String nameBlack;
	Scanner sc;
	int x, y;
	Map<String, Integer> map = new HashMap<String, Integer>();// les positions et les poidss
	int[][] pointWeight = new int[17][17];// 255 cases
	int size = 17;

	public HommevsMachine_v1() {
		// pion noir: 1 player
		// pion blanc: 2
		// evaluate les situations
		map.put("1", 10);
		map.put("12", 50);
		map.put("11", 150);
		map.put("112", 200);
		map.put("111", 1400);
		map.put("1112", 900);
		map.put("1111", 1700);

		map.put("2", 10);
		map.put("21", 50);
		map.put("22", 100);
		map.put("221", 150);
		map.put("222", 1300);
		map.put("2221", 800);
		map.put("2222", 2200);
		
	}

	public void calculTour(int currentPlayer) {
		// tester tour si current est 0, c'est le tour de pion noir sinon pion blanc
		String playerName = (currentPlayer == 0) ? nameBlack : nameWhite;
		System.out.println(playerName + " peut déposer le pion: ");
	}

	public void printTable(int[][] table, int size) {
		System.out.print("   ");
		// chiffres
		for (int i = 1; i < size - 1; i++) {
			int value = i + 1;
			System.out.print(value < 10 ? value + "  " : value + " ");
		}
		System.out.println();
		// lettres
		for (int i = 1; i < size - 1; i++) {
			char title = (char) ('A' + i);
			System.out.print(title + "  ");
			for (int j = 1; j < size - 1; j++) {
				int value = table[i][j]; // évite l'examen des limitess
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
		System.out.print("Entrez le nom du joueur : ");
		nameBlack = sc.nextLine();
		nameWhite = "AI";

		// print plateau

		int size = 17;
		int[][] table = new int[size][size];
		printTable(table, size);

		// play
		int winJoueur = 0;
		int winMachine = 0;
		int currentPlayer = 0;
		do {

			calculTour(currentPlayer);

			// coordonnées

			// déposer les pions
			// si currentPlayer est 0, 1 pour le pion noir et 2 pour pion blanc
			// table[y][x] = currentPlayer == 0 ? 1 : 2; 下子
			if (currentPlayer == 0) {
				// player play
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
					canPlace = canPlace && x >= 1 && x < size - 1;
					canPlace = canPlace && y >= 1 && y < size - 1;
					canPlace = canPlace && table[y][x] == 0;

				} while (!canPlace);

				table[y][x] = 1;
				pointWeight[y][x]=0;
				currentPlayer++;
			    winJoueur = checkWin(table, x, y);

			} else{
				
				System.out.println("AI plays... ");
				// AI play
				pointWeight = evaluate(table);
				// AI plays;
				Point p =AI(pointWeight, table);
				currentPlayer++;
				winMachine = checkWin(table, p.y, p.x);
			}
			
			currentPlayer %= 2;
			printTable(table, size);

			// print plateau

			// test win or lost
			

		} while (winJoueur == 0 && winMachine == 0);// to player win or lost

		// print result
		if (winJoueur == 1 || winMachine == 1) {
			System.out.println(nameBlack + " gagne !");
		} else if (winJoueur == 2 || winMachine == 2) {
			System.out.println(nameWhite + " gagne !");
		/*} else if (win == 3) {
			/*System.out.println("Personne gagne ! ");*/
		}

	}


	// evaluate function: get a pointWeight array
	public int[][] evaluate(int [][]table) 
	{
		for (int i = 1; i <size-1; i ++) {
			for (int j = 1; j < size-1; j ++) {
				if (table[i][j] == 0) {
					// right
					int color = 0;
					String code = "";
					for (int k = i + 1; k < size-1; k ++) {
						if (table[k][j] != 0) {
							if (color == 0) {
								color = table[k][j];
								code += table[k][j];
							} else {
								if (table[k][j] == color) {
									code += table[k][j];
								} else {
									code += table[k][j];
									break;
								}
							}
						} else {
							break;
						}
					}
					Integer value = map.get(code);
					if (value != null) {
						pointWeight[i][j] += value;
					}
					// down
					code = "";
					color = 0;
					for (int k = j + 1; k < size-1; k ++) {
						if (table[i][k] != 0) {
							if (color == 0) {
								color = table[i][k];
								code += table[i][k];
							} else {
								if (table[i][k] == color) {
									code += table[i][k];
								} else {
									code += table[i][k];
									break;
								}
							}
						} else {
							break;
						}
					}
					value = map.get(code);
					if (value != null) {
						pointWeight[i][j] += value;
					}
					// left
					code = "";
					color = 0;
					for (int k = i - 1; k >= 1; k --) {
						if (table[k][j] != 0) {
							if (color == 0) {
								color = table[k][j];
								code += table[k][j];
							} else {
								if (table[k][j] == color) {
									code += table[k][j];
								} else {
									code += table[k][j];
									break;
								}
							}
						} else {
							break;
						}
					}
					value = map.get(code);
					if (value != null) {
						pointWeight[i][j] += value;
					}
					// up
					code = "";
					color = 0;
					for (int k = j - 1; k >= 1; k --) {
						if (table[i][k] != 0) {
							if (color == 0) {
								color = table[i][k];
								code += table[i][k];
							} else {
								if (table[i][k] == color) {
									code += table[i][k];
								} else {
									code += table[i][k];
									break;
								}
							}
						} else {
							break;
						}
					}
					value = map.get(code);
					if (value != null) {
						pointWeight[i][j] += value;
					}
					// up right
					code = "";
					color = 0;
					for (int k = i +1, w = j +1; k < size-1
							&& w < size-1; k ++, w ++) {
						if (table[k][w] != 0) {
							if (color == 0) {
								color = table[k][w];
								code += table[k][w];
							} else {
								if (table[k][w] == color) {
									code += table[k][w];
								} else {
									code += table[k][w];
									break;
								}
							}
						} else {
							break;
						}
					}
					value = map.get(code);
					if (value != null) {
						pointWeight[i][j] += value;
					}

					//leftDown
					code = "";
					color = 0;
					for (int k = i - 1, w = j - 1; k >= 1 && w >= 1; k --, w --) {
						if (table[k][w] != 0) {
							if (color == 0) {
								color = table[k][w];
								code += table[k][w];
							} else {
								if (table[k][w] == color) {
									code += table[k][w];
								} else {
									code += table[k][w];
									break;
								}
							}
						} else {
							break;
						}
					}
					value = map.get(code);
					if (value != null) {
						pointWeight[i][j] += value;
					}

					//downRight
					code = "";
					color = 0;
					for (int k = i + 1, w = j - 1; k <size-1 && w >= 1; k ++, w --) {
						if (table[k][w] != 0) {
							if (color == 0) {
								color = table[k][w];
								code += table[k][w];
							} else {
								if (table[k][w] == color) {
									code += table[k][w];
								} else {
									code += table[k][w];
									break;
								}
							}
						} else {
							break;
						}
					}
					value = map.get(code);
					if (value != null) {
						pointWeight[i][j] += value;
					}

					//leftUp
					code = "";
					color = 0;
					for (int k = i - 1, w = j + 1; k >= 1 && w <size-1; k --, w ++) {
						if (table[k][w] != 0) {
							if (color == 0) {
								color = table[k][w];
								code += table[k][w];
							} else {
								if (table[k][w] == color) {
									code += table[k][w];
								} else {
									code += table[k][w];
									break;
								}
							}
						} else {
							break;
						}
					}
					value = map.get(code);
					if (value != null) {
						pointWeight[i][j] += value;
					}

				}
			}
		}

		return pointWeight;
	}

	public Point AI(int[][] pointWeight, int[][]table) {

		// Search function 
		
		int maxx = 1;
		int maxy = 1;
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (pointWeight[i][j] > pointWeight[maxx][maxy]) {
					maxx = i;
					maxy = j;

				}
					
			}
		}
		
        
		// get max point

		table[maxx][maxy] = 2;
		// AI play
		
		for (int i = 0; i < 15;  i++) {
			for (int j = 0; j < 15; j++) {
				pointWeight[i][j] = 0;
			}
		}
		
		Point p = new Point (maxx, maxy);
		return p;

	}

	public int checkWin(int[][] table, int x, int y) {
		// On n'a pas besoin de regarder tout le plateau
		// tester horizontalement
		// .
		// O O X X X X X
		// . <-
		// -> . : (droite + gauche)- 1
		// tester verticalment
		// tester diagonalement

		boolean test = false; // 4 directions
		test = test || plusCinq(table, x, y, 0, -1);// up
		test = test || plusCinq(table, x, y, 1, -1);// right up
		test = test || plusCinq(table, x, y, 1, 0); // right
		test = test || plusCinq(table, x, y, 1, 1); // right down

		if (test) {
			return table[y][x];
		}

		return 0;
	}

	// tester si on a cinq pions ou plus
	public boolean plusCinq(int[][] table, int x, int y, int dx, int dy) {

		int count = 0;

		count += compte(table, x, y, dx, dy); // abstraction 当前方向
		count += compte(table, x, y, -dx, -dy); // 反方向
		count -= 1;

		return count >= 5;

	}

	// compter le nombre de pions
	public int compte(int[][] table, int originX, int originY, int deplaceX, int deplaceY) {
		int originValue = table[originY][originX];// ce que l'on dépose
		int count = 0;
		

		int value;

		do {
			count++;

			originX += deplaceX; // déplacer dans 4 directions (abstraction)
			originY += deplaceY;

			value = table[originY][originX]; // pb
		} while (value == originValue);

		return count;
	}

	public static void main(String[] args) {

		new HommevsMachine_v1().run();
	}
}
