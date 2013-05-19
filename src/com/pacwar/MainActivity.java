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

	private AnimatedSprite pacManSprite[][];
	private TiledTexture ghosts_texture;

	private TiledTexture pacman_texture[];
	private AnimatedSprite ghostSprite[][];

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

		// Add animation frames from tile.
		ArrayList<AnimatedSprite.Frame> pacman_frames = new ArrayList<AnimatedSprite.Frame>();

		pacman_frames = new ArrayList<AnimatedSprite.Frame>();
		pacman_frames.add(new AnimatedSprite.Frame(2, 0));
		pacman_frames.add(new AnimatedSprite.Frame(1, 0));
		pacman_frames.add(new AnimatedSprite.Frame(0, 0));

		pacManSprite = new AnimatedSprite[pacmans_no][2];
		for (int i = 0; i < pacmans_no; i++) {
			pacManSprite[i][0] = new AnimatedSprite(pacman_texture[0], 0, 0);
			pacManSprite[i][0].animate(500, pacman_frames);
			scene.getTopLayer().add(pacManSprite[i][0]);
			pacManSprite[i][0].setVisible(false);

			pacManSprite[i][1] = new AnimatedSprite(pacman_texture[1], 0, 0);
			pacManSprite[i][1].animate(500, pacman_frames);
			scene.getTopLayer().add(pacManSprite[i][1]);
			pacManSprite[i][1].setVisible(false);
		}

		ArrayList<AnimatedSprite.Frame> frames2 = new ArrayList<AnimatedSprite.Frame>();
		frames2.add(new AnimatedSprite.Frame(0, 1));
		frames2.add(new AnimatedSprite.Frame(1, 1));
		frames2.add(new AnimatedSprite.Frame(2, 1));
		frames2.add(new AnimatedSprite.Frame(3, 1));

		ArrayList<AnimatedSprite.Frame> frames3 = new ArrayList<AnimatedSprite.Frame>();
		frames3.add(new AnimatedSprite.Frame(0, 2));
		frames3.add(new AnimatedSprite.Frame(1, 2));
		frames3.add(new AnimatedSprite.Frame(2, 2));
		frames3.add(new AnimatedSprite.Frame(3, 2));

		ghostSprite = new AnimatedSprite[ghosts_no][2];
		for (int i = 0; i < ghosts_no; i++) {
			ghostSprite[i][0] = new AnimatedSprite(ghosts_texture, 0, 0);
			ghostSprite[i][0].animate(500, frames2);
			scene.getTopLayer().add(ghostSprite[i][0]);
			ghostSprite[i][0].setVisible(false);

			ghostSprite[i][1] = new AnimatedSprite(ghosts_texture, 0, 0);
			ghostSprite[i][1].animate(500, frames3);
			scene.getTopLayer().add(ghostSprite[i][1]);
			ghostSprite[i][1].setVisible(false);
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

	public void show_pacman(int id, int player, int x, int y, int w, int h) {
		pacman_shift_x = (int) Math.round(w / 2.0 - pac_w / 2.0);
		pacman_shift_y = (int) Math.round(h / 2.0 - pac_h / 2.0);

		pacManSprite[id][player].scale((float) (w * 1.0 / pac_w),
				(float) (h * 1.0 / pac_h));
		pacManSprite[id][player].move(pacman_shift_x + x, pacman_shift_y + y);
		pacManSprite[id][player].setVisible(true);
	}

	public void hide_pacman(int id, int player) {
		pacManSprite[id][player].setVisible(false);
	}

	public void move_pacman(int id, int player, int x, int y) {
		pacManSprite[id][player].move(pacman_shift_x + x, pacman_shift_y + y);
	}

	public void show_ghost(int id, int player, int x, int y, int w, int h) {
		ghost_shift_x = (int) Math.round(w / 2.0 - ghost_w / 2.0);
		ghost_shift_y = (int) Math.round(h / 2.0 - ghost_h / 2.0);

		ghostSprite[id][player].scale((float) (w * 1.0 / ghost_w),
				(float) (h * 1.0 / ghost_h));
		ghostSprite[id][player].move(ghost_shift_x + x, ghost_shift_y + y);
		ghostSprite[id][player].setVisible(true);
	}

	public void hide_ghost(int id, int player) {
		ghostSprite[id][player].setVisible(false);
	}

	public void move_ghost(int id, int player, int x, int y) {
		ghostSprite[id][player].move(ghost_shift_x + x, ghost_shift_y + y);
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

		pacman_texture = new TiledTexture[2];

		pacman_texture[0] = new TiledTexture("pacman.png", pac_w, pac_h, 0, 0,
				0, 0, this);
		pacman_texture[1] = new TiledTexture("pacman2.png", pac_w, pac_h, 0, 0,
				0, 0, this);
	}

	private int prevX = -1, prevY = -1;

	@Override
	public boolean onSceneTouchEvent(E3Scene scene, MotionEvent motionEvent) {
		if (pacManSprite != null && model != null) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				if (prevX == getTouchEventX(scene, motionEvent)
						&& prevY == getTouchEventY(scene, motionEvent))
					return true;
				// x and y must be adjusted to scene's screen position by
				// scene.getSceneEventX,Y
				// because MotionEvent's getX() and getY() returns pixel
				// position of the actual device.
				int x = prevX = getTouchEventX(scene, motionEvent);
				int y = prevY = getTouchEventY(scene, motionEvent);
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
