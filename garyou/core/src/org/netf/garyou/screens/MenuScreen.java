package org.netf.garyou.screens;

import org.netf.garyou.game.MenuController;
import org.netf.garyou.game.MenuController.STATE;
import org.netf.garyou.game.MenuRenderer;
import org.netf.garyou.util.Constants;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class MenuScreen extends AbstractGameScreen implements InputProcessor {

	private static final String TAG = MenuScreen.class.getName();

	private MenuRenderer menuRenderer;
	private MenuController menuController;
	private OrthographicCamera camera;
	private Vector3 touchPoint;

	public MenuScreen(Game game) {

		super(game);

		touchPoint = new Vector3();

		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2, 0);
		camera.update();

	}

	@Override
	public void render(float deltaTime) {

		menuController.update(deltaTime);

		Gdx.gl.glClearColor(0xe7 / 255.0f, 0xe3 / 255.0f, 0xc8 / 255.0f, 0xff / 255.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		menuRenderer.render();

	}

	@Override
	public void show() {
		menuController = new MenuController();
		menuRenderer = new MenuRenderer(menuController);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void hide() {
		menuRenderer.dispose();
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		if (menuController.state == STATE.MAIN) {

			touchPoint.set(screenX, screenY, 0);
			camera.unproject(touchPoint);

			if (menuController.easy.isHit(touchPoint.x, touchPoint.y)) {
				game.setScreen(new GameScreen(game, GameScreen.MODE.EASY));
			} else if (menuController.normal.isHit(touchPoint.x, touchPoint.y)) {
				game.setScreen(new GameScreen(game, GameScreen.MODE.NORMAL));
			} else if (menuController.hard.isHit(touchPoint.x, touchPoint.y)) {
				game.setScreen(new GameScreen(game, GameScreen.MODE.HARD));
			}
		}

		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		focus(screenX, screenY);
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		focus(screenX, screenY);
		return true;
	}

	private void focus(int screenX, int screenY) {

		if (menuController.state == STATE.MAIN) {

			touchPoint.set(screenX, screenY, 0);
			camera.unproject(touchPoint);

			if (menuController.easy.isHit(touchPoint.x, touchPoint.y)) {
				menuController.easy.focused = true;
			} else {
				menuController.easy.focused = false;
			}

			if (menuController.normal.isHit(touchPoint.x, touchPoint.y)) {
				menuController.normal.focused = true;
			} else {
				menuController.normal.focused = false;
			}

			if (menuController.hard.isHit(touchPoint.x, touchPoint.y)) {
				menuController.hard.focused = true;
			} else {
				menuController.hard.focused = false;
			}
		}

	}

}
