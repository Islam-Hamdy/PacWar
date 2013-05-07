package com.pacwar;

import android.graphics.Point;

public class Man {
	int xMap, yMap, color, type, index;

	// Position.
	public float x;
	public float y;
	public float z;
	public float destX;
	public float destY;
	public float cenX;
	public float cenY;
	public Point[] next;
	public int current_point_to_go;
	// Velocity.
	public float velocityX = 1;
	public float velocityY = 1;
	public float velocityZ = 1;

	// Size.
	public float width;
	public float height;

	public void updateMe() {
		boolean xb = false;
		boolean yb = false;
		if (Math.abs(x - destX) < velocityX)
			xb = true;
		if (Math.abs(y - destY) < velocityY)
			yb = true;
		if (!xb) {
			if (x < destX){
				x += velocityX;
				cenX+=velocityX;
			}
			else if (x > destX){
				x -= velocityX;
				cenY-=velocityX;
			}
		}
		if (!yb) {
			if (y < destY) {
				y += velocityY;
				cenY += velocityY;
			} else if (y > destY) {
				y -= velocityY;
				cenY -= velocityY;
			}
		}
		x = Math.max(x, 0);
		y = Math.max(y, 0);
		if (next != null && xb && yb && current_point_to_go < next.length) {
			if (current_point_to_go < next.length - 1) {
				current_point_to_go++;
				destX = next[current_point_to_go].y * Global.CELL_WIDTH;
				destY = next[current_point_to_go].x * Global.CELL_HEIGHT;
			}
		}
	}
}