package dg.gui.animation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import dg.Agent;
import dg.Coordinates;
import dg.Direction;
import dg.event.EventHandler;
import dg.gui.GUIUtils;

public class AnimationQueue extends Thread {

	private static final long UPDATE_SLEEP_TIME = 30;
	private static final int UPDATE_PERCENT_STEP = 5;
	
	private static BlockingQueue<MoveAnimation> queue = new LinkedBlockingQueue<MoveAnimation>();
	
	private static MoveAnimation currentAnimation = null;
	private static int currentAnimationPercent = 0;
	
	private static AnimationQueue instance = null;
	
	private static List<EventHandler> animationEndEventHandlers = new ArrayList<EventHandler>();
	
	public static void init() {
		instance = new AnimationQueue();
		instance.start();
	}
	
	private AnimationQueue() {
	}
	
	/**
	 * Gibt die aktuelle (x,y)-Position des Agenten auf dem Bildschirm zurück.
	 */
	public static Point2D getHexOffset(Agent agent) {
		if (currentAnimation != null && agent.equals(currentAnimation.getAgent())) {
			Coordinates start = currentAnimation.getStart();
			Coordinates end = start.getAdjacentInDirection(currentAnimation.getMoveDirection());
			
			Point2D startPoint = GUIUtils.getHexOffset(start);
			Point2D endPoint = GUIUtils.getHexOffset(end);
			
			double dx = (endPoint.getX() - startPoint.getX()) / 100.;
			double dy = (endPoint.getY() - startPoint.getY()) / 100.;
			
			int percent = currentAnimationPercent;
			
			return new Point2D.Double(
					startPoint.getX() + percent * dx, 
					startPoint.getY() + percent * dy);
		}
		else {
			for (MoveAnimation animation : queue) {
				if (animation.getAgent().equals(agent)) {
					return GUIUtils.getHexOffset(animation.getStart());
				}
			}
			return GUIUtils.getHexOffset(agent.getPosition());
		}
	}
	
	public static Direction getDirectionOfView(Agent agent) {
		if (currentAnimation != null && agent.equals(currentAnimation.getAgent())) {
			return currentAnimation.getDirectionOfView();
		}
		for (MoveAnimation animation : queue) {
			if (animation.getAgent().equals(agent)) {
				return animation.getOldDirectionOfView();
			}
		}
		return agent.getDirectionOfView();
	}
	
	public static Coordinates getPosition(Agent agent) {
		if (currentAnimation != null && agent.equals(currentAnimation.getAgent())) {
			return currentAnimation.getStart();
		}
		for (MoveAnimation animation : queue) {
			if (animation.getAgent().equals(agent)) {
				return animation.getStart();
			}
		}
		return agent.getPosition();
	}
	
	public static void push(MoveAnimation animation) {
		System.out.println("pushed " + animation);
		queue.offer(animation);
	}
	
	public static void onAnimationEnd(EventHandler eventHandler) {
		if (currentAnimation == null && queue.size() == 0) {
			eventHandler.onEvent();
			return;
		}
		
		synchronized (animationEndEventHandlers) {
			animationEndEventHandlers.add(eventHandler);
		}
	}
	
	@Override
	public void run() {
		while (true) {
			if (currentAnimation != null) {
				if (currentAnimationPercent < 100) {
					currentAnimationPercent += UPDATE_PERCENT_STEP;
				}
				else {
					currentAnimation = null;
				}
			}
			
			if (currentAnimation == null) {
				if (queue.size() == 0) {
					synchronized (animationEndEventHandlers) {
						for (EventHandler handler : animationEndEventHandlers) {
							handler.onEvent();
						}
						animationEndEventHandlers.clear();
					}
				}
				
				try {
					currentAnimation = queue.take(); // blocking
				} catch (InterruptedException e) {
				}
				
				currentAnimationPercent = 0;
				System.out.println("start animating " + currentAnimation.getAgent());
			}
			
			try {
				Thread.sleep(UPDATE_SLEEP_TIME);
			} catch (InterruptedException e) {
			}
		}
	}

}
