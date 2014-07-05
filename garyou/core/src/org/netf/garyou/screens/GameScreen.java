package org.netf.garyou.screens;

import org.netf.garyou.garyouMain;
import org.netf.garyou.game.GameController;
import org.netf.garyou.game.GameController.STATE;
import org.netf.garyou.game.GameRenderer;
import org.netf.garyou.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class GameScreen extends AbstractGameScreen {

	private static final String TAG = MenuScreen.class.getName();

	private GameRenderer gameRenderer;
	private GameController gameController;
	private OrthographicCamera camera;
	private Vector3 touchPoint;

	public static enum MODE {
		EASY, NORMAL, HARD
	};

	private MODE mode;

	public GameScreen(garyouMain game, MODE mode) {
		super(game);

		this.mode = mode;

		touchPoint = new Vector3();

		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2, 0);
		camera.update();
	}

	@Override
	public void render(float deltaTime) {

		gameController.update(deltaTime);

		Gdx.gl.glClearColor(0xe7 / 255.0f, 0xe3 / 255.0f, 0xc8 / 255.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gameRenderer.render();

	}

	@Override
	public void show() {
		gameController = new GameController(mode);
		gameRenderer = new GameRenderer(gameController);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void hide() {
		gameRenderer.dispose();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		if (gameController.getState() == STATE.MAIN) {
			touchPoint.set(screenX, screenY, 0);
			camera.unproject(touchPoint);
			gameController.onFire(touchPoint);
		}

		if (gameController.getState() == STATE.CLEAR2) {
			focus(screenX, screenY);
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		if (gameController.getState() == STATE.CLEAR2) {
			focus(screenX, screenY);

			if (gameController.menu.focused) {
				game.setScreen(new MenuScreen(game));
			} else if (gameController.next.focused) {
				switch (mode) {
				case EASY:
					game.setScreen(new GameScreen(game, MODE.NORMAL));
					break;
				case NORMAL:
					game.setScreen(new GameScreen(game, MODE.HARD));
					break;
				default:
					break;
				}
			} else if (gameController.finalStage.focused) {
				game.setScreen(new FinalScreen(game));
			}
		} else if (gameController.getState() == STATE.NOT_CLEAR2) {
			focus(screenX, screenY);
			if (gameController.menu.focused) {
				game.setScreen(new MenuScreen(game));
			} else if (gameController.retry.focused) {
				gameController.init();
			}

		}

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (gameController.getState() == STATE.CLEAR2) {
			focus(screenX, screenY);
		} else if (gameController.getState() == STATE.NOT_CLEAR2) {
			focus(screenX, screenY);
		}
		return true;
	}

	private void focus(int screenX, int screenY) {

		if (gameController.getState() == STATE.CLEAR2) {

			touchPoint.set(screenX, screenY, 0);
			camera.unproject(touchPoint);

			if (gameController.menu.isHit(touchPoint.x, touchPoint.y)) {
				gameController.menu.focused = true;
			} else {
				gameController.menu.focused = false;
			}

			if (gameController.next.isHit(touchPoint.x, touchPoint.y)) {
				gameController.next.focused = true;
			} else {
				gameController.next.focused = false;
			}

			if (gameController.finalStage.isHit(touchPoint.x, touchPoint.y)) {
				gameController.finalStage.focused = true;
			} else {
				gameController.finalStage.focused = false;
			}

		}

		if (gameController.getState() == STATE.NOT_CLEAR2) {

			touchPoint.set(screenX, screenY, 0);
			camera.unproject(touchPoint);

			if (gameController.menu.isHit(touchPoint.x, touchPoint.y)) {
				gameController.menu.focused = true;
			} else {
				gameController.menu.focused = false;
			}

			if (gameController.retry.isHit(touchPoint.x, touchPoint.y)) {
				gameController.retry.focused = true;
			} else {
				gameController.retry.focused = false;
			}

		}

	}

}
