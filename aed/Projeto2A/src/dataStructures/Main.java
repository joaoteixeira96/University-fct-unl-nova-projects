package dataStructures;

public class Main {

	public static void main(String[] args) {
		
		Dictionary<Integer,Integer> listOreder = new OrderedDoubleList<Integer,Integer>();
		listOreder.insert(3, 45);
		listOreder.insert(-8, 3);
		listOreder.insert(1, 776);
		listOreder.insert(4, 2);
		
		
//		System.out.println(listOreder.find("bpp"));
		
		Iterator<Entry<Integer,Integer>> it;
		it = listOreder.iterator();
		while (it.hasNext()) {
			System.out.println(it.next().getValue());
		}
		
	}
}
