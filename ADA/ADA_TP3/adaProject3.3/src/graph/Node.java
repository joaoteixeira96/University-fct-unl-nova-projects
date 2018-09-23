package graph;

public class Node {

	//valor da lanterna
	private int lamp;
	//valor do no, os nos sao numerados da esquerda para a direita 
	//e de cima para baixo na matriz
	private int value;
	
	public Node(int lamp, int value) {
		this.lamp = lamp;
		this.value = value;
	}
	
	public int getLamp() {
		return lamp;
	}
	
	public int getValue() {
		return value;
	}
	
	/**
	 * Devolve a linha do no na matriz
	 * @param width - largura da matriz
	 * @return - linha do no
	 */
	public int getLine(int width) {
		return value/width;
	}
	
	/**
	 * Devolve a coluna do no na matriz
	 * @param width - largura da matriz
	 * @return - coluna do no
	 */
	public int getCol(int width) {
		return value%width;
	}
	
	public void setLamp(int lamp) {
		this.lamp = lamp;
	}
    
    public boolean isLamp() {
        return lamp ==-1;
    }
}
