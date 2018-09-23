import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Graph {

	private int[] inDegree;
	private List<Integer>[] outEdges;
	private int suspects;

	@SuppressWarnings("unchecked")
	public Graph(int suspects) {
		inDegree = new int[suspects];
		this.suspects = suspects;
		outEdges = new List[suspects];
		buildDataStructure();
	}

	private void buildDataStructure() {
		for (int i = 0; i < suspects; i++) {
			outEdges[i] = new LinkedList<Integer>();
		}
	}

	// x must leave the gallery before y enters
	public void addDependency(int x, int y) {
		inDegree[y]++;
		outEdges[x].add(y);
	}

	// Returns true is there is no cycle in the graph
	public boolean isAcyclic() {
		int numProcNodes = 0;
		Queue<Integer> ready = new LinkedList<Integer>();
		for (int i = 0; i < suspects; i++) {
			if (inDegree[i] == 0) {
				ready.add(i);
			}
		}
		while (!ready.isEmpty()) {
			int node = ready.remove();
			numProcNodes++;
			for (int v : outEdges[node]) {
				inDegree[v]--;
				if (inDegree[v] == 0)
					ready.add(v);
			}
		}
		return numProcNodes == suspects;
	}
}
