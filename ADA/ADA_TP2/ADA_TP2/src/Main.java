import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	private static final String Inconsistent_conjectures = "Inconsistent conjectures";
	private static final String Consistent_conjectures = "Consistent conjectures";
	private static final String SPACE = " ";
	private static int S = 0;
	private static int P = 0;
	private static int C = 0;

	public static void main(String[] args) throws IOException {

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		// first input read
		String[] init = input.readLine().split(SPACE);
		S = Integer.parseInt(init[0]);
		P = Integer.parseInt(init[1]);
		C = Integer.parseInt(init[2]);
		
		//Graph with double size because each suspect as nodes arrived and left
		Graph g = new Graph(S * 2);

		//Create dependency (arrived -> left) for each suspect
		for (int i = 0; i < 2 * S; i = i + 2) {
			g.addDependency(i, i + 1);
		}
		//Create dependency for the preceding conjectures
		for (int j = 0; j < P; j++) {
			String[] pairs = input.readLine().split(SPACE);
			int x = Integer.parseInt(pairs[0]);
			int y = Integer.parseInt(pairs[1]);
			g.addDependency(x * 2 + 1, y * 2);
		}
		//Create dependecy for the concurrent conjectures 
		for (int k = 0; k < C; k++) {
			String[] pairs = input.readLine().split(SPACE);
			int x = Integer.parseInt(pairs[0]);
			int y = Integer.parseInt(pairs[1]);
			g.addDependency(y * 2, x * 2 + 1);
			g.addDependency(x * 2, y * 2 + 1);
		}

		if (g.isAcyclic()) {
			System.out.println(Consistent_conjectures);
		} else
			System.out.println(Inconsistent_conjectures);
	}
}
