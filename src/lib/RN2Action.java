package lib;

abstract public class RN2Action {
	public double duration = 0, timeElapsed = 0;
	public RN2Node node;
	public RN2Action(RN2Node onNode, double withDuration) {
		node = onNode;
		duration = withDuration;
	}
	
	abstract public void runActionBlock();
	
	public static class FadeInAction extends RN2Action {
		
		public FadeInAction(RN2Node onNode, double withDuration) {
			super(onNode, withDuration);
		}

		@Override
		public void runActionBlock() {
			node.opacity = timeElapsed / duration;
		}
		
	}
	
	public static class FadeOutAction extends RN2Action {
		
		public FadeOutAction(RN2Node onNode, double withDuration) {
			super(onNode, withDuration);
		}

		@Override
		public void runActionBlock() {
			node.opacity = (duration - timeElapsed) / duration;
		}
		
	}
	
	public static class MoveToPositionAction extends RN2Action {
		RN2Point startPosition, targetPosition;
		double xMovePerSecond, yMovePerSecond;
		public MoveToPositionAction(RN2Node onNode, double withDuration, RN2Point toPosition) {
			super(onNode, withDuration);
			startPosition = onNode.position;
			targetPosition = toPosition;
			xMovePerSecond = (toPosition.x - onNode.position.x) / duration;
			yMovePerSecond = (toPosition.y - onNode.position.y) / duration;
		}

		@Override
		public void runActionBlock() {
			node.position.x = startPosition.x + 
					(timeElapsed / duration)*(targetPosition.x - startPosition.x);
			node.position.y = startPosition.y + 
					(timeElapsed / duration)*(targetPosition.y - startPosition.y);
		}
		
	}
			
}
	
	
	
//	public RN2Action(double duration) {
//		this.duration = duration;
//	}
//	abstract public void actionBlock(RN2Node node, double timeElapsed);
//	
//	public static RN2Action fadeInWithDuration(double d) {
//		RN2Action a = new RN2Action(d){
//			@Override
//			public void actionBlock(RN2Node node, double timeElapsed) {
//				node.opacity = timeElapsed/duration;
//			}
//			
//		};
//		return a;	
//	}
//	
//	public static RN2Action fadeOutWithDuration(double d) {
//		RN2Action a = new RN2Action(d){
//			@Override
//			public void actionBlock(RN2Node node, double timeElapsed) {
//				node.opacity = (duration-timeElapsed)/duration;
//			}
//			
//		};
//		return a;	
//	}

