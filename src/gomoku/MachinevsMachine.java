package gomoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class MachinevsMachine {

	String nameWhite;
	String nameBlack;
	int currentPlayer;
	Scanner sc;
	int x, y;
	Map<String, Integer> map = new HashMap<String, Integer>();// les positions et les poidss
	int size = 17;
	int[][] table = new int[size][size];
	int[][] positions = new int[size][size];
	int[][] pointWeight = new int[17][17];// 255 cases
	int scoreComputer = 0;
	int scorePlayer = 0;
	int sum = 0;
	int INFINITY = 10000000;
	int bestValue = 0;
	int win = 0;
	ArrayList<Point> bestMoves = new ArrayList();
	Map<String, Integer> hm = new HashMap<String, Integer>();

	public MachinevsMachine() {
		// pion noir: 1 player
		// pion blanc: 2
		// evaluate les situations
		
		//minmax_AI map
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

		//sempic_AI map
		hm.put("1", 10);
		hm.put("12", 50);
		hm.put("11", 150);
		hm.put("112", 200);
		hm.put("111", 1400);
		hm.put("1112", 900);
		hm.put("1111", 1700);

		hm.put("2", 10);
		hm.put("21", 50);
		hm.put("22", 100);
	    hm.put("221", 150);
		hm.put("222", 1000);
		hm.put("2221", 800);
		hm.put("2222", 1500);
		
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
		
		nameBlack = "sempic_AI";
		nameWhite = "minmax_AI";

		// print plateau

		table = new int[size][size];
		printTable(table, size);

		// play

		int currentPlayer = 0;
		do {

			calculTour(currentPlayer);

			// coordonnées

			// déposer les pions
			// si currentPlayer est 0, 1 pour le pion noir et 2 pour pion blanc
			// table[y][x] = currentPlayer == 0 ? 1 : 2; 下子
			if (currentPlayer == 0) {
				
				System.out.println("Sempic_AI plays...");
				// AI play
				pointWeight = sempic_evaluate(table);
				// AI plays;
				Sempic_AI(pointWeight, table);

				currentPlayer++;

			} else {

				System.out.println("Minmax_AI plays...");
				AI();
				currentPlayer++;
			}

			
			printTable(table, size);
			currentPlayer %= 2;

			// print plateau

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
	public void Sempic_AI(int[][] pointWeight, int[][]table) {

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

	}

	
	public int[][] sempic_evaluate(int [][]table) 
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
					Integer value = hm.get(code);
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
					value = hm.get(code);
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
					value = hm.get(code);
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
					value = hm.get(code);
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
					value = hm.get(code);
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
					value = hm.get(code);
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
					value = hm.get(code);
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
					value = hm.get(code);
					if (value != null) {
						pointWeight[i][j] += value;
					}

				}
			}
		}

		return pointWeight;
	}


	public int minmax_evaluate(int[][] positions) {
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
		Point p = minimax_evaluation(positions, 1);
		x = p.x;
		y = p.y;

		table[y][x] = 2;
		positions[y][x] = 2;
	}

	public Point minimax_evaluation(int[][] postions, int depth) {
		

		int index = 0;

		int bestValue = -INFINITY;
		for (int i = 1; i < size - 1; i++) {
			for (int j = 1; j < size - 1; j++) {
				if (positions[i][j] == 0) {
					positions[i][j] = 2;

					int value = min(positions, depth);
					if (value > bestValue) {
						bestValue = value;
						index = 0;
						Point p = new Point(i, j);
						bestMoves.add(index, p);
					} else if (value == bestValue) {
						index++;
						Point p = new Point(i, j);
						bestMoves.add(index, p);
					}

					positions[i][j] = 0;
				}
			}

		}

		
		return bestMoves.get(index);

	}

	/**
	 * machine
	 */
	public int max(int[][] positions, int depth) {

		int evalValue = minmax_evaluate(positions);

		boolean isGameOver = (win != 0);
		if (depth == 0 || isGameOver) {
			return evalValue;
		}

		int bestValue = -INFINITY;
		for (int i = 1; i < size - 1; i++) {

			for (int j = 1; j < size - 1; j++) {
				if (positions[i][j] == 0) {
					// essayer
					positions[i][j] = 2;

					// maximin
					bestValue = Math.max(bestValue, min(positions, depth - 1));

					// vide
					positions[i][j] = 0;
				}
			}

		}

		return evalValue;

	}

	/**
	 * player
	 */
	public int min(int[][] positions, int depth) {

		int value = minmax_evaluate(positions);

		boolean isGameOver = (win != 0);
		if (depth == 0 || isGameOver) {
			return value;
		}

		int meilleureValue = +INFINITY;
		for (int i = 1; i < size - 1; i++) {
			for (int j = 1; j < size - 1; j++) {

				if (positions[i][j] == 0) {
					// essayer
					positions[i][j] = 1;

					// minimax
					meilleureValue = Math.min(meilleureValue, max(positions, depth - 1));

					// vide
					positions[i][j] = 0;
				}
			}

		}

		return value;

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
		
		
			test = test || plusCinq(table, x, y, 0, -1);// 上
			test = test || plusCinq(table, x, y, 1, -1);// 右上
			test = test || plusCinq(table, x, y, 1, 0); // 右
			test = test || plusCinq(table, x, y, 1, 1); // 右下
		
		

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
		int count = 0;
		
			int originValue = table[originY][originX];// ce que l'on dépose
			

			int value;

			do {
				count++;

				originX += deplaceX; // déplacer dans 4 directions (abstraction)
				originY += deplaceY;

				value = table[originY][originX]; 
			} while (value == originValue);
		
		

		return count;
	}

	public static void main(String[] args) {

		new MachinevsMachine().run();

	}

}
