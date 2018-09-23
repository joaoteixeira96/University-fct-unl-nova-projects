package dataStructures;

public class Main {

	public static void main(String[] args) {
		Dictionary<String, Integer> tree;
		tree = new BinarySearchTree<String, Integer>();

		tree.insert("e", 5);
		tree.insert("d", 4);
		tree.insert("g", 7);
		tree.insert("c", 3);
		tree.insert("f", 6);
		tree.insert("z", 8);
		tree.insert("a", 1);
		tree.insert("b", 2);

		Iterator<Entry<String, Integer>> it;
		it = tree.iterator();

		while(it.hasNext()){
			System.out.println(it.next().getValue());
		}
	}

		//		Dictionary<Integer, String> ordem = new ChainedHashTable<Integer, String>();
		//		
		//		ordem.insert(35, "quarto");
		//		ordem.insert(2, "primeiro");
		//		ordem.insert(60, "quinto");
		//		ordem.insert(80, "oitavo");
		//		ordem.insert(17, "segundo");
		//		ordem.insert(60, "sexto");
		//		ordem.insert(18, "terceiro");
		//		ordem.insert(60, "setimo");
		//		ordem.insert(80, "nono");
		//		
		//		
		//		Iterator<Entry<Integer, String>> it;
		//		it = ordem.iterator();
		//		
		//		while(it.hasNext()){
		//			System.out.println(it.next().getValue());
		//		}
		//		
		//		
		//	}

}
