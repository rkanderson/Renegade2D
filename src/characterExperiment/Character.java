package characterExperiment;

import java.awt.Color;

import lib.*;

public class Character extends RN2Node {
	RN2Node ladder;
	public Character() {
		constructBody();
	}
	private void constructBody() {
		RN2PolygonNode mainBody = new RN2PolygonNode.RectBuilder()
				.withWidth(50).withHeight(50).build();
		mainBody.color = Color.BLUE;
		mainBody.zPosition = 2;
		addChild(mainBody);
		
		ladder = new RN2Node();
		ladder.position.y = -25;
		this.addChild(ladder);
		
		RN2LineNode modelMember = new RN2LineNode(0, 0, 0, 0);
		modelMember.thickness = 3;
		modelMember.color = new Color(100, 100, 255);
		
		for(int i=0; i<20; i++) {
			RN2LineNode member = modelMember.duplicate();
			member.pointA = new RN2Point(Math.pow(-1, i-1)*15, (i-1)*5 + 30);
			member.pointB =	new RN2Point(Math.pow(-1, i)*15,  i*5 + 30);
			ladder.addChild(member);
		}
		
	}
	
}
