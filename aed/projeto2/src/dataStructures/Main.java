package dataStructures;


public class Main {

	public static void main(String[] args) {
		Dictionary<String,Integer> listOreder = new OrderedDoubleList<String,Integer>();
		listOreder.insert("ala", 45);
		listOreder.insert("bpp", 776);
		listOreder.insert("cpp", 2);
		listOreder.insert("rg", 3);
		System.out.println(listOreder.find("ala"));
		
//		Iterator<Entry<String,Integer>> it;
//		it = listOreder.iterator();
//		while (it.hasNext()) {
//			System.out.println(it.next().getValue());
//		}
	}

}
