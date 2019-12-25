package gomoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * @author Cheng Xinyun min max
 *
 */

public class HommevsMachine_v2 {

	String nameWhite;
	String nameBlack;
	int currentPlayer;
	Scanner sc;
	int x, y;
	Map<String, Integer> map = new HashMap<String, Integer>();// les positions et les poidss
	int size = 10;
	int[][] table;
	int[][] positions = new int[size][size];
	int scoreComputer = 0;
	int scorePlayer = 0;
	int sum = 0;
	int INFINITY = 10000000;
	int bestValue = 0;
	int win = 0;
	ArrayList<Point> BestMove = new ArrayList();

	public HommevsMachine_v2() {
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
		map.put("222", 1000);
		map.put("2221", 800);
		map.put("2222", 1500);

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

		table = new int[size][size];
		printTable(table, size);

		// play

		int currentPlayer = 0;
		do {

			calculTour(currentPlayer);

			// si currentPlayer est 0, 1 pour le pion noir et 2 pour pion blanc
			// table[y][x] = currentPlayer == 0 ? 1 : 2; 
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
				positions[y][x] = 1;

				currentPlayer++;
				win = checkWin(table, x, y);
			} else {

				System.out.println("AI plays: ");
				AI();
				currentPlayer++;
				
			}

			printTable(table, size);
			currentPlayer %= 2;

			// print plateau

			// test win or lost
			

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

	public int evaluate(int[][] positions) {
		// trouver min value of positions
		for (int i = 1; i < size - 1; i++) {
			for (int j = 1; j < size - 1; j++) {
				String code = " ";
				if (positions[i][j] == 1)// player
				{
					code += positions[i][j];
					String right = code;
					// right
					for (int k = i + 1; k < size - 1; k++) {
						if (positions[k][j] == 1) {
							right += positions[k][j];
						} else {
							right += positions[k][j];
							break;
						}
					}
					Integer value = map.get(right);
					if (value != null) {
						scorePlayer += value;
					}

					// left
					String left = code;
					for (int k = i - 1; k >= 1; k--) {
						if (positions[k][j] == 1) {
							left += positions[k][j];
						} else {
							left += positions[k][j];
							break;
						}
					}
					value = map.get(left);
					if (value != null) {
						scorePlayer += value;
					}

					// up
					String up = code;
					for (int k = j - 1; k >= 1; k--) {
						if (positions[i][k] == 1) {
							up += positions[i][k];
						} else {
							up += positions[i][k];
							break;
						}
					}
					value = map.get(up);
					if (value != null) {
						scorePlayer += value;
					}

					// down
					String down = code;
					for (int k = j + 1; k < size - 1; k++) {
						if (positions[i][k] == 1) {
							down += positions[i][k];
						} else {
							down += positions[i][k];
							break;
						}
					}
					value = map.get(down);
					if (value != null) {
						scorePlayer += value;
					}

					// upRight
					String upRight = code;
					for (int k = i + 1, w = j + 1; k < size - 1 && w < size - 1; k++, w++) {
						if (positions[k][w] == 1) {
							upRight += positions[k][w];
						} else {
							upRight += positions[k][w];
							break;
						}
					}
					value = map.get(upRight);
					if (value != null) {
						scorePlayer += value;
					}

					// downRight
					String downRight = code;
					for (int k = i + 1, w = j - 1; k < size - 1 && w >= 1; k++, w--) {
						if (positions[k][w] == 1) {
							downRight += positions[k][w];
						} else {
							downRight += positions[k][w];
							break;
						}
					}
					value = map.get(downRight);
					if (value != null) {
						scorePlayer += value;
					}

					// leftUp
					String leftUp = code;
					for (int k = i - 1, w = j + 1; k >= 1 && w < size - 1; k--, w++) {
						if (positions[k][w] == 1) {
							leftUp += positions[k][w];
						} else {
							leftUp += positions[k][w];
							break;
						}
					}
					value = map.get(leftUp);
					if (value != null) {
						scorePlayer += value;
					}

					// leftDown
					String leftDown = code;
					for (int k = i - 1, w = j - 1; k >= 1 && w >= 1; k--, w--) {
						if (positions[k][w] == 1) {
							leftDown += positions[k][w];
						} else {
							leftDown += positions[k][w];
							break;
						}
					}
					value = map.get(leftDown);
					if (value != null) {
						scorePlayer += value;
					}

				} else {// AI
					code += positions[i][j];
					String right = code;
					// right
					for (int k = i + 1; k < size - 1; k++) {
						if (positions[k][j] == 1) {
							right += positions[k][j];
						} else {
							right += positions[k][j];
							break;
						}
					}
					Integer value = map.get(right);
					if (value != null) {
						scoreComputer += value;
					}

					// left
					String left = code;
					for (int k = i - 1; k >= 1; k--) {
						if (positions[k][j] == 1) {
							left += positions[k][j];
						} else {
							left += positions[k][j];
							break;
						}
					}
					value = map.get(left);
					if (value != null) {
						scoreComputer += value;
					}

					// up
					String up = code;
					for (int k = j - 1; k >= 1; k--) {
						if (positions[i][k] == 1) {
							up += positions[i][k];
						} else {
							up += positions[i][k];
							break;
						}
					}
					value = map.get(up);
					if (value != null) {
						scoreComputer += value;
					}

					// down
					String down = code;
					for (int k = j + 1; k < size - 1; k++) {
						if (positions[i][k] == 1) {
							down += positions[i][k];
						} else {
							down += positions[i][k];
							break;
						}
					}
					value = map.get(down);
					if (value != null) {
						scoreComputer += value;
					}

					// upRight
					String upRight = code;
					for (int k = i + 1, w = j + 1; k < size - 1 && w < size - 1; k++, w++) {
						if (positions[k][w] == 1) {
							upRight += positions[k][w];
						} else {
							upRight += positions[k][w];
							break;
						}
					}
					value = map.get(upRight);
					if (value != null) {
						scoreComputer += value;
					}

					// downRight
					String downRight = code;
					for (int k = i + 1, w = j - 1; k < size - 1 && w >= 1; k++, w--) {
						if (positions[k][w] == 1) {
							downRight += positions[k][w];
						} else {
							downRight += positions[k][w];
							break;
						}
					}
					value = map.get(downRight);
					if (value != null) {
						scoreComputer += value;
					}

					// leftUp
					String leftUp = code;
					for (int k = i - 1, w = j + 1; k >= 1 && w < size - 1; k--, w++) {
						if (positions[k][w] == 1) {
							leftUp += positions[k][w];
						} else {
							leftUp += positions[k][w];
							break;
						}
					}
					value = map.get(leftUp);
					if (value != null) {
						scoreComputer += value;
					}

					// leftDown
					String leftDown = code;
					for (int k = i - 1, w = j - 1; k >= 1 && w >= 1; k--, w--) {
						if (positions[k][w] == 1) {
							leftDown += positions[k][w];
						} else {
							leftDown += positions[k][w];
							break;
						}
					}
					value = map.get(leftDown);
					if (value != null) {
						scoreComputer += value;
					}
				}

			}
		}

		return sum = scoreComputer - scorePlayer;
	}

	public void AI() {
		
		Point p = getNextMove(positions, 2);
		x = p.x;
		y = p.y;

		table[y][x] = 2;
		positions[y][x] = 2;
	}
     
	//machine
	public Point getNextMove(int[][] postions, int depth) {
       
		int index = 0;
		int meilleureScore = -INFINITY;
		for (int i = 1; i < size - 1; i++) {
			for (int j = 1; j < size - 1; j++) {
				if (positions[i][j] == 0) {// vide
					positions[i][j] = 2;

					int value = min(positions, depth);
					if (value > meilleureScore) {
						meilleureScore = value;
						index = 0;
						Point p = new Point(i, j);
						BestMove.add(index, p);
					} else if (value == meilleureScore) {
						index++;
						Point p = new Point(i, j);
						BestMove.add(index, p);
					}

					positions[i][j] = 0;
				}
			}

		}

		return BestMove.get(index);// obtenir le point de bestvalue pour machine.

	}

	/**
	 * machine
	 */
	public int max(int[][] positions, int depth) {

		int valeur = evaluate(positions);

		boolean isGameOver = (win != 0);
		if (depth == 0 || isGameOver) {
			return valeur;
		}

		int meilleurScore = -INFINITY;
		for (int i = 1; i < size - 1; i++) {

			for (int j = 1; j < size - 1; j++) {
				if (positions[i][j] == 0) {
					// essayer
					positions[i][j] = 2;

					// maximin
					meilleurScore = Math.max(meilleurScore, min(positions, depth - 1));

					// rend vide
					positions[i][j] = 0;
				}
			}

		}

		return valeur;

	}

	/**
	 * joueur
	 */
	public int min(int[][] positions, int depth) {

		int valeur = evaluate(positions);

		boolean isGameOver = (win != 0);
		if (depth == 0 || isGameOver) {
			return valeur;
		}

		int meilleureScore = INFINITY;
		for (int i = 1; i < size - 1; i++) {
			for (int j = 1; j < size - 1; j++) {

				if (positions[i][j] == 0) {
					// essayer pour joueur
					positions[i][j] = 1;

					// minimax
					meilleureScore = Math.min(meilleureScore, max(positions, depth - 1));

					// rend vide
					positions[i][j] = 0;
				}
			}

		}

		return valeur;

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
		count += compte(table, x, y, -dx, -dy); // direction inverse
		count -= 1;

		return count >= 5;

	}

	// compter le nombre de pions
	public int compte(int[][] table, int originX, int originY, int deplaceX, int deplaceY) {
		int count = 0;

		int originValue = table[originY][originX];// ce que l'on dépose

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

		new HommevsMachine_v2().run();

	}

}
