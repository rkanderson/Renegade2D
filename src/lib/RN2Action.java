package lib;

abstract public class RN2Action {
	/**
	 * Actions are magical. They allow you to make your nodes have behaviors that you would
	 * have otherwise needed to write a bunch more code in all sorts of places to have happen.
	 */
	
	protected RN2Node node;
	public RN2Action() {

	}
	
	/**
	 * Should be called ONCE and BEFORE any action blocks are called. It's kinda like
	 * getting the action to commit to being part of its node. I do this cuz I don't
	 * want to pass in the node as a constructor parameter.
	 * @param n the node that the action should affect.
	 */
	public void activate(RN2Node n) {
		node = n;
	}
	
	/**
	 * Should reset the action to its original, starter state.
	 */
	abstract public void reset();
	
	/**
	 * @return if the action is completed or not. If it is completed, the action will be
	 * removed from whatever is using it cuz it's useless now.
	 */
	abstract public boolean actionFinished();
	
	public void runTheActionBlock(double deltaTime) throws IllegalStateException {
		if(node == null) {
			throw new IllegalStateException("You need to call activate() on this action before you can call runTheActionBlock()");
		} else {
			runActionBlock(deltaTime);
		}
	}
	abstract protected void runActionBlock(double deltaTime);
	
	abstract public static class ActionBlock {
		abstract public void run(RN2Node node, double timeElapsed);
	}
	
	abstract public static class CompletionBlock {
		abstract public void run();
	}
	
	/**
	 * This method is a shortcut way to create custom actions.
	 * @param duration is how long the action will be active.
	 * @param actionBlock is an object that contains the method with the code
	 * that will be run each step of the way as the action progresses.
	 * @return your very own custom action!
	 */
	public static RN2Action customActionWithDuration(double duration, ActionBlock actionBlock) {
		return new RN2Action() {
			private double timeElapsed = 0;
			@Override
			public boolean actionFinished() {
				return timeElapsed >= duration;
			}

			@Override
			public void runActionBlock(double deltaTime) {
				timeElapsed += deltaTime;
				if(timeElapsed > duration) {
					timeElapsed = duration;
				}
				actionBlock.run(node, timeElapsed);
			}
			
			@Override
			public void reset() {
				timeElapsed = 0;
			}
			
		};
	}
	
	/**
	 * This allows one to take multiple actions and line them up together, treating them
	 * as a single action. They happen one after another, so this ain't much of an action
	 * pudding as it is an action sandwich!
	 * @param subActions the array of actions that will occur sequentially.
	 * @return the new single sequence action.
	 */
	public static RN2Action sequence(RN2Action[] subActions) {
		return new RN2Action() {
			private int indexImOn = 0;
			private boolean finished = false;
			
			@Override
			public void activate(RN2Node n) {
				super.activate(n);
				for(RN2Action a: subActions) {
					a.activate(n);
				}
			}
			
			@Override
			public boolean actionFinished() {
				return finished;
			}
			
			@Override
			public void reset() {
				indexImOn = 0;
				finished = false;
				for(RN2Action a: subActions) {
					a.reset();
				}
			}

			@Override
			protected void runActionBlock(double deltaTime) {
				if(finished) {return;}
				RN2Action action = subActions[indexImOn];
				action.runTheActionBlock(deltaTime);
				if(action.actionFinished()) {
					indexImOn++;
					if(indexImOn == subActions.length) {
						finished = true;
					}
				}
			}
		};
	}
	
	/**
	 * Makes an action that causes a passed in action to repeat indefinitely.
	 * @param action the base action from which the forever repeating one will be created.
	 * @return the new repeating action.
	 */
	public static RN2Action repeatForever(RN2Action action) {
		return new RN2Action() {

			@Override
			public void activate(RN2Node n) {
				super.activate(n);
				action.reset(); //<-Just in case the action was some kinda thing that was already being used.
				action.activate(n);
			}
			
			@Override
			public boolean actionFinished() {
				return false;
			}

			@Override
			protected void runActionBlock(double deltaTime) {
				action.runActionBlock(deltaTime);
				if(action.actionFinished()) {
					action.reset();
				}
			}

			@Override
			public void reset() {
				action.reset();
			}
		};
	}
	
	/**
	 * Makes an action that waits and does nothing......Ya....That's al it's good for.....Hurrr durr durr.
	 * Sometimes, you just gotta put this in a sequence.
	 * @param duration how long it waits.
	 * @return the wait action
	 */
	public static RN2Action waitForDuration(double duration)  {
		return new RN2Action(){
			private double timeElapsed;
			@Override
			public boolean actionFinished() {
				return timeElapsed >= duration;
			}

			@Override
			protected void runActionBlock(double deltaTime) {
				timeElapsed += deltaTime;
			}
			
			@Override
			public void reset() {
				timeElapsed = 0;
			}
			
		};
	}
	
	/**
	 * Makes an action that makes the node linearly shift its opacity from its initial value to the target value.
	 * @param targetOpacity the opacity the node should ultimately be at.
	 * @param duration how long it takes to get to the new opacity.
	 * @return the fade action
	 */
	public static RN2Action fadeToOpacityWithDuration(double targetOpacity, double duration) {
		return new RN2Action() {
			private double timeElapsed = 0, initialOpacity;
			@Override
			public boolean actionFinished() {
				return timeElapsed >= duration;
			}
			
			@Override
			public void reset() {
				timeElapsed = 0;
			}
			
			@Override
			public void activate(RN2Node n) {
				super.activate(n);
				initialOpacity = n.getAbsoluteOpacity();
			}

			@Override
			protected void runActionBlock(double deltaTime) {
				timeElapsed += deltaTime;
				if(timeElapsed > duration) {
					timeElapsed = duration;
				}
				node.setOpacity(initialOpacity + (targetOpacity - initialOpacity)*timeElapsed/duration);

			}
			
		};
	}
	
	/**
	 * Makes an action tha makes its node fade in over the given time duration. It doesn't matter what its
	 * original opacity was; it'll start from zero no matter what.
	 * @param duration 
	 * @return fade-in action
	 */
	public static RN2Action fadeInWithDuration(double duration) {
		return customActionWithDuration(duration, new ActionBlock(){
			@Override
			public void run(RN2Node node, double timeElapsed) {
				if(timeElapsed > duration) {
					node.opacity = 1;
				} else {
					node.opacity = timeElapsed / duration;
				}
			}
		});
	}
	
	/**
	 * Makes an action that makes its node disappear over the given time period. Its original opacity doesn't
	 * matter; the opacity will first be set to 0 and end up at 1.
	 * @param duration
	 * @return fade-out action
	 */
	public static RN2Action fadeOutWithDuration(double duration) {
		return customActionWithDuration(duration, new ActionBlock(){
			@Override
			public void run(RN2Node node, double timeElapsed) {
				if(timeElapsed > duration) {
					node.opacity = 0;
				} else {
					node.opacity = (duration-timeElapsed) / duration;
				}
			}
		});
	}
	
	/**
	 * Makes an action that moves the node from its current position to another given target position
	 * over the given time duration. NOTE:If while the action is running, the node's
	 * position is modified elsewhere, this action will change it back to maintain
	 * its original course no matter what.
	 * @param target the target point for the node to travel to.
	 * @param duration the duration of time the node will take to travel to the target.
	 * @return the o-mazing  move-to-position-action
	 */
	public static RN2Action moveToPositionOverDuration(RN2Point target, double duration) {
		return new RN2Action() {
			
			RN2Point fromPt;
			double timeElapsed = 0;

			@Override
			public void activate(RN2Node n) {
				super.activate(n);
				fromPt = new RN2Point(n.position);
			}
			
			@Override
			public void reset() {
				timeElapsed = 0;
			}

			@Override
			public boolean actionFinished() {
				return timeElapsed >= duration;
			}

			@Override
			protected void runActionBlock(double deltaTime) {
				timeElapsed += deltaTime;
				if(timeElapsed > duration) {
					timeElapsed = duration;
				}
				node.position.x = fromPt.x + (target.x - fromPt.x)*timeElapsed/duration;
				node.position.y = fromPt.y + (target.y - fromPt.y)*timeElapsed/duration;
			}
			
		};
	}
	
	/**
	 * Makes an action that moves the node by the distances contained inside the vector over the time duration.
	 * That's all there is to it.
	 * @param v vector to move by
	 * @param duration time duration
	 * @return move action
	 */
	public static RN2Action moveByVectorOverDuration(RN2Vector v, double duration) {
		return new RN2Action(){

			double timeElapsed = 0;
			
			@Override
			public void reset() {
				timeElapsed = 0;
			}

			@Override
			public boolean actionFinished() {
				return timeElapsed >= duration;
			}

			@Override
			protected void runActionBlock(double deltaTime) {
				timeElapsed += deltaTime;
				if(timeElapsed > duration) {
					timeElapsed = duration;
				}
				node.position.x += v.dx / duration * deltaTime;
				node.position.y += v.dy / duration * deltaTime;
			}
			
		};
	}
	
	
	/**
	 * Makes an action that causes the node to rotate from its initial zPosition
	 * to the target z position over the given time duration;
	 * @param targetZRotation ultimate z rotation
	 * @param duration time duration
	 * @return the action
	 */
	public static RN2Action rotateToZRotationOverDuration(double targetZRotation, double duration) {
		return new RN2Action() {

			double startZRotation;
			double timeElapsed = 0;
			
			@Override
			public void activate(RN2Node n) {
				super.activate(n);
				startZRotation = n.zRotation;
			}
			
			@Override
			public void reset() {
				timeElapsed = 0;
			}

			@Override
			public boolean actionFinished() {
				return timeElapsed >= duration;
			}

			@Override
			protected void runActionBlock(double deltaTime) {
				timeElapsed += deltaTime;
				if(timeElapsed > duration) {
					timeElapsed = duration;
				}
				node.zRotation = startZRotation + 
						(targetZRotation - startZRotation)*timeElapsed/duration;
			}
			
		};
	}
	
	/**
	 * Makes an action that causes its node to rotate by a given value over a given duration
	 * @param dzRot delta z-rotation
	 * @param duration time duration
	 * @return the action
	 */
	public static RN2Action rotateByValueOverDuration(double dzRot, double duration) {
		return new RN2Action() {

			double timeElapsed = 0;
			
			@Override
			public void reset() {
				timeElapsed = 0;
			}

			@Override
			public boolean actionFinished() {
				return timeElapsed >= duration;
			}

			@Override
			protected void runActionBlock(double deltaTime) {
				timeElapsed += deltaTime;
				if(timeElapsed > duration) {
					timeElapsed = duration;
				}
				node.zRotation += dzRot / duration * deltaTime;
			}
			
		};
	}
	
	/**
	 * Makes an action that changes the scale of its node to to the given target value
	 * over the time duration. NOTE: If the scale is modified elsewhere while the action
	 * is running, then the action will change it back to what it thinks it should be.
	 * It doesn't give a shit.
	 * @param newScaleX the target xScale
	 * @param newScaleY the target yScale
	 * @param duration time duration
	 * @return the action
	 */
	public static RN2Action scaleToValueOverDuration(double newScaleX, double newScaleY, double duration) {
		return new RN2Action() {

			double orgScaleX, orgScaleY;
			double timeElapsed = 0;
			
			@Override
			public void activate(RN2Node n) {
				super.activate(n);
				orgScaleX = n.xScale;
				orgScaleY = n.yScale;
			}
			
			@Override
			public void reset() {
				timeElapsed = 0;
			}

			@Override
			public boolean actionFinished() {
				return timeElapsed >= duration;
			}

			@Override
			protected void runActionBlock(double deltaTime) {
				timeElapsed += deltaTime;
				if(timeElapsed > duration) {
					timeElapsed = duration;
				}
				node.xScale = orgScaleX + (newScaleX - orgScaleX)*timeElapsed/duration;
				node.yScale = orgScaleY + (newScaleY - orgScaleY)*timeElapsed/duration;
			}
			
		};
	}
	
	public static RN2Action addScaleValueOverDuration(double dxs, double dys, double duration) {
		return new RN2Action() {
			
			double timeElapsed = 0;
			
			@Override
			public void reset() {
				timeElapsed = 0;
			}

			@Override
			public boolean actionFinished() {
				return timeElapsed >= duration;
			}

			@Override
			protected void runActionBlock(double deltaTime) {
				timeElapsed += deltaTime;
				if(timeElapsed > duration) {
					timeElapsed = duration;
				}
				node.xScale += dxs / duration * deltaTime;
				node.yScale += dys / duration * deltaTime;
			}
			
		};
	}
}

