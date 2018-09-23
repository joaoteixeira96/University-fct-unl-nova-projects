package adaProject;

public class Parades {


	/*
	 * Returns the longest subsequence between the two char arrays
	 */
	public String LCS(char[] seqX, char[] seqY) {

		String[][] matrix = new String[seqX.length+1][seqY.length+1];
		compMaxLength(seqX, seqY, matrix);

		return matrix[seqX.length][seqY.length];
	}
	
	/*
	 * Returns the longest String between the two and the lexicographically
	 * smaller in case of a tie 
	 */
	private String bestSequence(String a, String b) {
		if(a != null && b != null) {
			if(a.length() > b.length())
				return a;
			else if(a.length() < b.length())
				return b;
			else {
				if(a.compareTo(b) < 0)
					return a;
				else return b;
			}
		}
		return null;
	}

	/*
	 * Fills the matrix with the common subsequences leaving the
	 * longest common subsequence bottom right position of the matrix
	 */
	private void compMaxLength(char[] seqX, char[] seqY, String[][] matrix) {
		// Row 0 seqX is empty.
		for (int j = 0; j <= seqY.length; j++) {
			matrix[0][j] = "";
		}
		// Column 0 seqY is empty.
		for (int i = 0; i <= seqX.length; i++) {
			matrix[i][0] = "";
		}
		// Remaining cells, filled by rows
		for (int i = 0; i < seqX.length; i++) {
			for (int j =0; j < seqY.length; j++) {
				if (seqX[i] == seqY[j]) {
					matrix[i+1][j+1] = matrix[i][j] + seqX[i];
				} else {
					matrix[i+1][j+1] = bestSequence(matrix[i][j+1], matrix[i+1][j]);
				}
			}
		}
	}
}