package adaProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
	static List <String> list = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException {

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		char[] str1 = input.readLine().toCharArray();
		char[] str2 = input.readLine().toCharArray();
		
		LCS(str1,str2);
		
		for(String s: list)
			System.out.println(s);
	}

	static void LCS(char[] seqX, char[] seqY) {

		String[][] maxLength = new String[seqX.length+1][seqY.length+1];
		compMaxLength(seqX, seqY, maxLength);

		System.out.println(maxLength[seqX.length][seqY.length].length());
		System.out.println(maxLength[seqX.length][seqY.length]);
	}
	
	private static String bestSequence(String a, String b) {
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

	static void compMaxLength(char[] seqX, char[] seqY, String[][] maxLength) {
		// Row 0 — seqX is empty.
		for (int j = 0; j <= seqY.length; j++) {
			maxLength[0][j] = "";
		}
		// Column 0 — seqY is empty.
		for (int i = 0; i <= seqX.length; i++) {
			maxLength[i][0] = "";
		}
		// Remaining cells, filled by rows
		for (int i = 0; i < seqX.length; i++) {
			for (int j =0; j < seqY.length; j++) {
				if (seqX[i] == seqY[j]) {
					maxLength[i+1][j+1] = maxLength[i][j] + seqX[i];
				} else {
					maxLength[i+1][j+1] = bestSequence(maxLength[i][j+1], maxLength[i+1][j]);
				}
			}
		}
	}
}	