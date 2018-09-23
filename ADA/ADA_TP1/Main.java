import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import adaProject.Parades;

public class Main {
	
	public static void main(String[] args) throws IOException {

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		char[] str1 = input.readLine().toCharArray();
		char[] str2 = input.readLine().toCharArray();
		
		Parades p = new Parades();
		String result = p.LCS(str1,str2);
		System.out.println(result.length());
		if(result.length() > 0 )
			System.out.println(result);
	}
}	