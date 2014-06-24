package org.netf.garyou.screens;

import org.netf.garyou.garyouMain;
import org.netf.garyou.game.FinalController;
import org.netf.garyou.game.FinalController.STATE;
import org.netf.garyou.game.FinalRenderer;
import org.netf.garyou.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class FinalScreen extends AbstractGameScreen {

	private static final String TAG = MenuScreen.class.getName();

	private FinalRenderer finalRenderer;
	private FinalController finalController;
	private OrthographicCamera camera;
	private Vector3 touchPoint;

	public FinalScreen(garyouMain game) {
		super(game);

		touchPoint = new Vector3();

		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2, 0);
		camera.update();
	}

	@Override
	public void render(float deltaTime) {

		finalController.update(deltaTime);

		Gdx.gl.glClearColor(0xe7 / 255.0f, 0xe3 / 255.0f, 0xc8 / 255.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		finalRenderer.render();

	}

	@Override
	public void show() {
		finalController = new FinalController();
		finalRenderer = new FinalRenderer(finalController);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void hide() {
		finalRenderer.dispose();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		if (finalController.getState() == STATE.MAIN) {
			touchPoint.set(screenX, screenY, 0);
			camera.unproject(touchPoint);
			finalController.onFire(touchPoint);
		}

		if (finalController.getState() == STATE.CLEAR2) {
			focus(screenX, screenY);
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		if (finalController.getState() == STATE.CLEAR2) {
			focus(screenX, screenY);

		} else if (finalController.getState() == STATE.NOT_CLEAR2) {
			focus(screenX, screenY);

			if (finalController.menu.focused) {
				game.setScreen(new MenuScreen(game));
			} else if (finalController.retry.focused) {
				finalController.init();
			}

		}

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		if (finalController.getState() == STATE.NOT_CLEAR2) {
			focus(screenX, screenY);
		}

		return true;
	}

	private void focus(int screenX, int screenY) {

		if (finalController.getState() == STATE.NOT_CLEAR2) {

			touchPoint.set(screenX, screenY, 0);
			camera.unproject(touchPoint);

			if (finalController.menu.isHit(touchPoint.x, touchPoint.y)) {
				finalController.menu.focused = true;
			} else {
				finalController.menu.focused = false;
			}

			if (finalController.retry.isHit(touchPoint.x, touchPoint.y)) {
				finalController.retry.focused = true;
			} else {
				finalController.retry.focused = false;
			}

		}

	}

}
