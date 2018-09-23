import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import graph.Solver;
import graph.Node;

public class Main {

	private static final String SPACE = " ";

	public static void main(String[] args) throws IOException {

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		// processa a primeira linha do input
		String[] firstLine = input.readLine().split(SPACE);
		int width = Integer.parseInt(firstLine[0]);
		int height = Integer.parseInt(firstLine[1]);
		Node[][] matrix = new Node[height][width];;

		readInputToMatrix(width, height, input, matrix);
		Solver g = new Solver(width, height, matrix);
		
		System.out.println(g.findBestPath());

	}
	/**
	 * Processa o input e guarda-o numa matriz
	 * @param width - largura da matriz
	 * @param height - altura da matriz
	 * @param input - utilizado para ler o input
	 * @param matrix - matriz onde e guardado o input
	 * @throws IOException - excecao lancada quando ocorre um erro na leitura do input
	 */
	private static void readInputToMatrix(int width, int height, BufferedReader input, Node [][] matrix) throws IOException {
		int index = 0;
		for (int i = 0; i < height; i++) {			
			String[] line = input.readLine().split(SPACE);
			for (int j = 0; j < width; j++) {
				String position = line[j];
				
				// No criado inicialmente -1, significa que n existe nem lampada nem abobura.
				if (position.equals("*")) {
					// Node em que muda o valor pois foi encontrado um lampada
					matrix[i][j] = new Node(-1 , index++ );
				} else if (Integer.parseInt(position) > 0) {
					// Node em que muda o valor pois foi encontrado abobora
					matrix[i][j] = new Node(Integer.parseInt(position) ,index++ );
				} else {
					// Node default
					matrix[i][j] = new Node(-2,index++);
				}
			}
		}		
	}
}
