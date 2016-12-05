package gameCode;

import java.util.ArrayList;

public class TestRandomStuff {

	public static void main(String[] args) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("a"); arr.add("b"); arr.add("c");
		arr.add(3, "hi");
		System.out.println(arr);
	}

}
