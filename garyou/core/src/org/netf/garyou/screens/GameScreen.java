package org.netf.garyou.screens;

import org.netf.garyou.game.GameController;
import org.netf.garyou.game.GameRenderer;
import org.netf.garyou.util.Constants;

import com.badlogic.gdx.Game;
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

	public GameScreen(Game game, MODE mode) {
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

		Gdx.gl.glClearColor(0xe7 / 255.0f, 0xe3 / 255.0f, 0xc8 / 255.0f, 0xff / 255.0f);
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

}
