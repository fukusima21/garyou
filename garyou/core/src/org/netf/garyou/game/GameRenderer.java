package org.netf.garyou.game;

import org.netf.garyou.game.GameController.STATE;
import org.netf.garyou.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

public class GameRenderer implements Disposable {

	private static final String TAG = GameRenderer.class.getName();

	private OrthographicCamera camera;
	private OrthographicCamera guiCamera;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private StringBuilder timer;

	private GameController gameController;

	public GameRenderer(GameController gameController) {
		this.gameController = gameController;
		init();
	}

	private void init() {

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2, 0);

		guiCamera = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		guiCamera.position.set(Constants.VIEWPORT_GUI_WIDTH / 2, Constants.VIEWPORT_GUI_HEIGHT / 2, 0);

		timer = new StringBuilder("00.00");
	}

	public void render() {
		camera.update();
		guiCamera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		renderBackground();

		switch (gameController.getState()) {
		case READY:
			renderReady();
			break;
		case MAIN:
		case CHARGE:
		case FIRE:
			renderMain();
			break;
		case FINISH:
			break;
		default:
			break;
		}
		batch.end();

		batch.setProjectionMatrix(guiCamera.combined);
		batch.begin();
		switch (gameController.getState()) {
		case READY:
			break;
		case MAIN:
			renderTimer();
			break;
		case CHARGE:
		case FIRE:
			renderPartcle();
			renderTimer();
			break;
		case FINISH:
			break;
		default:
			break;
		}
		batch.end();

		Gdx.gl.glDepthMask(false);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		switch (gameController.getState()) {
		case READY:
			break;
		case MAIN:
			break;
		case CHARGE:
		case FIRE:
			renderGuide();
			break;
		case FINISH:
			break;
		default:
			break;
		}
		shapeRenderer.end();

		Gdx.gl.glDepthMask(true);
	}

	@Override
	public void dispose() {
		batch.dispose();
		shapeRenderer.dispose();
	}

	private void renderBackground() {
		gameController.back.getSprite().draw(batch);
	}

	private void renderReady() {
		gameController.moon.getSprite().draw(batch);
		gameController.dragonGame.getSprite().draw(batch);
		gameController.player.getSprite().draw(batch);
		gameController.grass1.getSprite().draw(batch);
		gameController.grass2.getSprite().draw(batch);
	}

	private void renderMain() {
		gameController.eye.getSprite().draw(batch);
		gameController.dragonGame.getSprite().draw(batch);
		gameController.grass3.getSprite().draw(batch);
		gameController.grass4.getSprite().draw(batch);
		gameController.player.getSprite().draw(batch);
		gameController.grass1.getSprite().draw(batch);
		gameController.grass2.getSprite().draw(batch);

		if (gameController.getState() == STATE.FIRE) {
			gameController.bullet.getSprite().draw(batch);
		}
	}

	private void renderGuide() {

		float[] vertices = gameController.guide.getVertices();

		Gdx.gl.glLineWidth(3.0f);

		shapeRenderer.setColor(0.5f, 0.5f, 1.0f, 0.7f);
		shapeRenderer.line(vertices[0], vertices[1], vertices[2], vertices[3]);

	}

	private void renderTimer() {

		int time = MathUtils.floorPositive(gameController.timer * 100.0f);

		if (time < 0) {
			time = 0;
		}

		timer.setCharAt(0, time / 1000 == 0 ? ' ' : (char) ('0' + (time / 1000)));
		timer.setCharAt(1, (char) ('0' + (time % 1000) / 100));
		timer.setCharAt(3, (char) ('0' + (time % 100) / 10));
		timer.setCharAt(4, (char) ('0' + (time % 10)));

		Assets.instance.bitmapFont.setScale(1.0f);
		Assets.instance.bitmapFont.setColor(0.0f, 0.0f, 1.0f, 1.0f);
		Assets.instance.bitmapFont.draw(batch, timer, 8.0f, 464.0f);

	}

	private void renderPartcle() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		Assets.instance.bulletEffect.draw(batch, deltaTime);

	}

}
