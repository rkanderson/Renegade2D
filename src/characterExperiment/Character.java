package characterExperiment;

import java.awt.Color;

import lib.*;

public class Character extends RN2Node {
	RN2Node ladder;
	int numOfJoints = 5;
	public Character() {
		constructBody();
	}
	private void constructBody() {
		RN2PolygonNode mainBody = new RN2PolygonNode.RectBuilder()
				.withWidth(50).withHeight(50).build();
		mainBody.color = Color.BLUE;
		addChild(mainBody);
		
//		ladder = new RN2Node();
//		ladder.position.y = -25;
//		this.addChild(ladder);
//		RN2Point prevPoint = new RN2Point(0, 0);
//		for(int i=1; i<numOfJoints; i++) {
//			if(i % 2 == 0) {
//				RN2Point nextPoint = new RN2Point(0, i*10);
//				RN2LineNode
//
//			} else 
//				RN2Point nextPoint = new RN2Point(-25, i*10);
//				
//			}
//		}
		
		
	}
	
}
