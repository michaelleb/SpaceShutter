/**
 * 
 */
package com.andrapp.spaceshutter.util;

/**
 * @author Misha
 *
 */
public interface EventDispatcher {
	
	public void dispatchTouchDown(int posX, int posY);
	public void dispatchTouchUP();
	public void dispatchFling(int simpleDirection);
	public void dispatchDrag(int posX, int posY);
	public void addOnTouchDownListener(TouchDownListener listener);
	public void addOnTouchUpListener(TouchUpListener listener);
	public void addOnFlingListener(FlingListener listener);
	public void addOnDragListener(DragListener listener);
	public void removeOnTouchDownListener(TouchDownListener listener);
	public void removeOnTouchUpListener(TouchDownListener listener);
	public void removeOnFlingListener(FlingListener listener);
	public void removeOnDragListener(DragListener listener);
	
}
