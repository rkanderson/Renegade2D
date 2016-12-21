package gameCode;

import java.util.ArrayList;

import lib.*;

public class TestRandomStuff {

	public static void main(String[] args) {
		RN2Node l1 = new RN2Node();
		System.out.println("l1: "+ intArrToString(l1.getAddress()));
		RN2Node l2a = new RN2Node();
		RN2Node l2b = new RN2Node();
		l1.addChild(l2a); l1.addChild(l2b);
		System.out.println("l2a: "+ intArrToString(l2a.getAddress()));
		System.out.println("l2b: "+ intArrToString(l2b.getAddress()));
		l1.removeChild(l2a);
		
		RN2Node l3a = new RN2Node();
		l2b.addChild(l3a);
		System.out.println("l3a "+intArrToString(l3a.getAddress()));
		



		
	}
	
	static String intArrToString(int[] arr) {
		String str = "[";
		for(int i: arr) {
			str += i+", ";
		}
		return str+"]";
	}

}
