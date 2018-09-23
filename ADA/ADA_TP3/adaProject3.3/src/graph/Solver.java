package graph;

import java.util.SortedMap;
import java.util.TreeMap;

public class Solver {

	private int width;
	private int height;
	private Node[][] matrix;
	//vetor de inteiros que para cada no guarda o maximo de lanterna com que esse no foi percorrido
	private int[] visited;
	private SortedMap<Integer, Node> queue2;

	public Solver(int width, int height, Node[][] matrix) {
		this.width = width;
		this.height = height;
		this.matrix = matrix;
		visited = new int[width * height];
		queue2 = new TreeMap<>();
		fillVisited();
	}

	private void fillVisited() {
		for (int i = 0; i < visited.length; i++) {
			visited[i] = -1;
		}
	}

	/**
	 * Encontra o caminho mais curto para a solucao
	 * @return - caminho mais curto para a solucao
	 */
	public int findBestPath() {
		int track = 0;
		SortedMap<Integer, Node> queue = new TreeMap<>();
		queue.put(0, new Node(Math.max(matrix[0][0].getLamp(), 0), 0));
		while (true) {
			while (!queue.isEmpty()) {
				Node n = queue.remove(queue.firstKey());
				if (n.getLine(width) == height - 1 && n.getCol(width) == width - 1)
					return track;
				getSuccessors(n);
			}
			track++;

			queue = new TreeMap<>(queue2);
			queue2 = new TreeMap<>();
		}
	}

	/**
	 * Tenta inserir o node na queue2. So insere se o node nao estiver na queue2 ou 
	 * se o valor de lanterna com que la esta for inferior ao do node que se tenta inserir
	 * @param node - node que se tenta inserir
	 */
	private void addSuccessorToQueue(Node node) {
		if (!queue2.containsKey(node.getValue()) || queue2.get(node.getValue()).getLamp() < node.getLamp()) {
			queue2.put(node.getValue(), node);
		}
	}

	/**
	 * Verifica se o no current pode prosseguir para o no na linha nextLine e coluna nextCol
	 * @param current- no atual
	 * @param nextLine - linha do no para o qual se tenta prosseguir
	 * @param nextCol - coluna do no para o qual se tenta prosseguir
	 * @param nextValue - indice do no para o qual se tenta prosseguir
	 * @return -1 se nao pode prosseguir para o no, ou devolve o valor da lanterna
	 *         se conseguiu prosseguir para o no. O valor da lanterna e no novo no.
	 */
	private int canProceed(Node current, int nextLine, int nextCol, int nextValue) {
		try {
			if (matrix[nextLine][nextCol].isLamp() || matrix[current.getLine(width)][current.getCol(width)].isLamp()) {
				if (current.getLamp() > visited[nextValue]) {
					return Math.max(current.getLamp(), matrix[nextLine][nextCol].getLamp());
				}
			} else if (current.getLamp() > 0 && current.getLamp() - 1 > visited[nextValue]) {
				return Math.max(current.getLamp() - 1, matrix[nextLine][nextCol].getLamp());
			}
			return -1;
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}

	/**
	 * Coloca os sucessores do no atual na queue2
	 * @param current - no atual
	 */
	private void getSuccessors(Node current) {
		visited[current.getValue()] = current.getLamp();

		// Tenta inserir o no a direita do current
		int lamp = canProceed(current, current.getLine(width), current.getCol(width) + 1, current.getValue() + 1);
		if (lamp != -1) {
			addSuccessorToQueue(new Node(lamp, current.getValue() + 1));
		}

		// Tenta inserir o no a esquerda do current
		lamp = canProceed(current, current.getLine(width), current.getCol(width) - 1, current.getValue() - 1);
		if (lamp != -1) {
			addSuccessorToQueue(new Node(lamp, current.getValue() - 1));
		}

		// Tenta inserir o no em cima do current
		lamp = canProceed(current, current.getLine(width) - 1, current.getCol(width), current.getValue() - width);
		if (lamp != -1) {
			addSuccessorToQueue(new Node(lamp, current.getValue() - width));
		}

		// Tenta inserir o no em baixo do current
		lamp = canProceed(current, current.getLine(width) + 1, current.getCol(width), current.getValue() + width);
		if (lamp != -1) {
			addSuccessorToQueue(new Node(lamp, current.getValue() + width));		
		}
	}
}