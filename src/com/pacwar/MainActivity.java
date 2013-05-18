package com.pacwar;

import java.io.IOException;
import java.util.ArrayList;

import android.util.DisplayMetrics;
import android.view.MotionEvent;

import com.e3roid.E3Activity;
import com.e3roid.E3Engine;
import com.e3roid.E3Scene;
import com.e3roid.drawable.Sprite;
import com.e3roid.drawable.sprite.AnimatedSprite;
import com.e3roid.drawable.texture.TiledTexture;

public class MainActivity extends E3Activity {
	public E3Scene scene;
	public static GameState model;
	private TiledTexture pacman_texture;
	private AnimatedSprite pacManSprite[];
	private TiledTexture ghosts_texture;
	private AnimatedSprite ghostSprite[];
	private static int pacmans_no = Global.PAC_INIT;
	private static int ghosts_no = Global.GHOST_INIT;

	private void initializeGame() throws IOException {
		int bgwidth = 164, bgheight = 212;
		Sprite bg = new Sprite(new TiledTexture("bg.png", bgwidth, bgheight,
				this), getWidth() / 2 - bgwidth / 2, getHeight() / 2 - bgheight
				/ 2);
		bg.scale((float) (getWidth() * 1.0 / bgwidth),
				(float) (getHeight() * 1.0 / bgheight));
		scene.getTopLayer().add(bg);

		int centerX = (getWidth() - pacman_texture.getTileWidth()) / 2;
		int centerY = (getHeight() - pacman_texture.getTileHeight()) / 2;

		// Add animation frames from tile.
		ArrayList<AnimatedSprite.Frame> pacman_frames = new ArrayList<AnimatedSprite.Frame>();

		pacman_frames = new ArrayList<AnimatedSprite.Frame>();
		pacman_frames.add(new AnimatedSprite.Frame(2, 0));
		pacman_frames.add(new AnimatedSprite.Frame(1, 0));
		pacman_frames.add(new AnimatedSprite.Frame(0, 0));

		pacManSprite = new AnimatedSprite[pacmans_no];
		for (int i = 0; i < pacmans_no; i++) {

			pacManSprite[i] = new AnimatedSprite(pacman_texture, 0, 0);
			// pacManSprite[i].scale(1, 1);
			// Start animation with 500msec, infinite loop.
			pacManSprite[i].animate(500, pacman_frames);
			scene.getTopLayer().add(pacManSprite[i]);
			pacManSprite[i].setVisible(false);
		}

		ArrayList<AnimatedSprite.Frame> frames2 = new ArrayList<AnimatedSprite.Frame>();
		frames2.add(new AnimatedSprite.Frame(0, 4));
		frames2.add(new AnimatedSprite.Frame(1, 4));
		frames2.add(new AnimatedSprite.Frame(2, 4));
		frames2.add(new AnimatedSprite.Frame(3, 4));

		ghostSprite = new AnimatedSprite[ghosts_no];
		for (int i = 0; i < ghosts_no; i++) {
			ghostSprite[i] = new AnimatedSprite(ghosts_texture, centerX,
					centerY);
			// ghostSprite[i].scale(2, 2);
			ghostSprite[i].animate(500, frames2);
			scene.getTopLayer().add(ghostSprite[i]);
			ghostSprite[i].setVisible(false);
		}

		scene.setBackgroundColor(0.94f, 1.00f, 0.94f, 1);
		// Toast.makeText(this, "Touch screen to move the sprite.",
		// Toast.LENGTH_LONG).show();

		model = new GameState(this);
	}

	@Override
	public E3Engine onLoadEngine() {

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

		Global.SCREEN_WIDTH = displaymetrics.widthPixels;
		Global.SCREEN_HEIGHT = displaymetrics.heightPixels;

		E3Engine engine = new E3Engine(this, (int) Global.SCREEN_WIDTH,
				(int) Global.SCREEN_HEIGHT);
		engine.requestFullScreen();
		engine.requestPortrait();
		return engine;
	}

	@Override
	public E3Scene onLoadScene() {
		scene = new E3Scene();
		scene.addEventListener(this);
		try {
			initializeGame();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scene;
	}

	private int pacman_shift_x, pacman_shift_y;
	private int ghost_shift_x, ghost_shift_y;

	public void show_pacman(int color, int x, int y, int w, int h) {
		pacman_shift_x = (int) Math.round(w / 2.0 - pac_w / 2.0);
		pacman_shift_y = (int) Math.round(h / 2.0 - pac_h / 2.0);

		pacManSprite[color].scale((float) (w * 1.0 / pac_w),
				(float) (h * 1.0 / pac_h));
		pacManSprite[color].move(100, 100);
		pacManSprite[color].setVisible(true);
	}

	public void hide_pacman(int color) {
		pacManSprite[color].setVisible(false);
	}

	public void move_pacman(int color, int x, int y) {
		pacManSprite[color].move(pacman_shift_x + x, pacman_shift_y + y);
	}

	public void show_ghost(int color, int x, int h, int w, int y) {
		ghost_shift_x = (int) Math.round(w / 2.0 - ghost_w / 2.0);
		ghost_shift_y = (int) Math.round(h / 2.0 - ghost_h / 2.0);

		ghostSprite[color].scale((float) (w * 1.0 / ghost_w),
				(float) (h * 1.0 / ghost_h));
		ghostSprite[color].move(x, y);
		ghostSprite[color].setVisible(true);
	}

	public void hide_ghost(int color) {
		ghostSprite[color].setVisible(false);
	}

	public void move_ghost(int color, int x, int y) {
		ghostSprite[color].move(ghost_shift_x + x, ghost_shift_y + y);
	}

	int pac_w = 36, pac_h = 37;
	int ghost_w = 17, ghost_h = 18;

	@Override
	public void onLoadResources() {
		// 31x49 pixel sprite with 1px border and (0,0) tile.
		// texture = new TiledTexture("king.png", 31, 49, 0, 0, 3, 2, this);
		// texture = new TiledTexture(name, width, height, xindex, yindex,
		// border, margin, context)
		ghosts_texture = new TiledTexture("ghosts.png", ghost_w, ghost_h, 0, 0,
				0, 0, this);
		pacman_texture = new TiledTexture("pacman.png", pac_w, pac_h, 0, 0, 0,
				0, this);
	}

	@Override
	public boolean onSceneTouchEvent(E3Scene scene, MotionEvent motionEvent) {
		if (pacManSprite != null && model != null) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				// x and y must be adjusted to scene's screen position by
				// scene.getSceneEventX,Y
				// because MotionEvent's getX() and getY() returns pixel
				// position of the actual device.
				int x = getTouchEventX(scene, motionEvent);
				int y = getTouchEventY(scene, motionEvent);
				model.SceneTouch(x, y, GameState.curPlayer);
				// pacManSprite.move(x, y);

				// In order to stop the animation, just call stop() method like
				// below.
				// if (sprite.isAnimated()) sprite.stop();
			}
		}

		return false;
	}

}
