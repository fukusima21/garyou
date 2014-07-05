package org.netf.garyou.game;

import org.netf.garyou.game.GameController.STATE;
import org.netf.garyou.util.Constants;
import org.netf.garyou.util.GaryouUtil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class GameRenderer implements Disposable {

	private static final String TAG = GameRenderer.class.getName();

	private OrthographicCamera camera;
	private OrthographicCamera guiCamera;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private StringBuilder timer;

	private GameController controller;

	public GameRenderer(GameController controller) {
		this.controller = controller;
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

		switch (controller.getState()) {
		case READY:
			renderReady();
			break;
		case MAIN:
		case FIRE:
			renderMain();
			break;
		case CLEAR1:
			renderClear1();
			break;
		case CLEAR2:
			renderClear2();
			break;
		case NOT_CLEAR1:
			renderNoClear1();
			break;
		case NOT_CLEAR2:
			renderNoClear2();
			break;
		default:
			break;
		}
		batch.end();

		batch.setProjectionMatrix(guiCamera.combined);
		batch.begin();
		switch (controller.getState()) {
		case READY:
		case MAIN:
			renderTimer();
			break;
		case FIRE:
			renderTimer();
			renderBulletPartcle();
			break;
		case CLEAR1:
			renderTimer();
			renderBulletPartcle();
			renderHitPartcle();
			break;
		case CLEAR2:
			renderTimer();
		case NOT_CLEAR1:
		case NOT_CLEAR2:
			renderTimer();
			renderBulletPartcle();
		default:
			break;
		}
		batch.end();

		Gdx.gl.glDepthMask(false);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		switch (controller.getState()) {
		case FIRE:
			renderGuide();
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
		controller.back.getSprite().draw(batch);
	}

	private void renderReady() {
		controller.moon.getSprite().draw(batch);
		controller.dragonGame.getSprite().draw(batch);
		controller.player.getSprite().draw(batch);
		controller.grass3.getSprite().draw(batch);
		controller.grass1.getSprite().draw(batch);
	}

	private void renderMain() {
		controller.eye.getSprite().draw(batch);
		controller.dragonGame.getSprite().draw(batch);
		controller.grass3.getSprite().draw(batch);
		controller.grass4.getSprite().draw(batch);
		controller.player.getSprite().draw(batch);
		controller.grass1.getSprite().draw(batch);
		controller.grass2.getSprite().draw(batch);

		if (controller.getState() == STATE.FIRE) {
			controller.bullet.getSprite().draw(batch);
		}
	}

	private void renderClear1() {
		controller.eye.getSprite().draw(batch);
		controller.dragonGame.getSprite().draw(batch);
		controller.grass3.getSprite().draw(batch);
		controller.grass4.getSprite().draw(batch);
		controller.player.getSprite().draw(batch);
		controller.grass1.getSprite().draw(batch);
		controller.grass2.getSprite().draw(batch);
	}

	private void renderClear2() {

		controller.whiteBoard.getSprite().draw(batch);

		controller.clearMessage.getSprite().draw(batch);
		controller.player.getSprite().draw(batch);

		if (controller.menu.focused) {
			Rectangle bounds = controller.menu.getSprite().getBoundingRectangle();
			controller.circle.getSprite().setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
			controller.circle.getSprite().setAlpha(0.5f);
			controller.circle.getSprite().draw(batch);
		}

		if (controller.next.focused) {
			Rectangle bounds = controller.next.getSprite().getBoundingRectangle();
			controller.circle.getSprite().setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
			controller.circle.getSprite().setAlpha(0.5f);
			controller.circle.getSprite().draw(batch);
		}

		if (controller.finalStage.focused) {
			Rectangle bounds = controller.finalStage.getSprite().getBoundingRectangle();
			controller.circle.getSprite().setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
			controller.circle.getSprite().setAlpha(0.5f);
			controller.circle.getSprite().draw(batch);
		}

		controller.menu.getSprite().draw(batch);
		controller.next.getSprite().draw(batch);
		controller.finalStage.getSprite().draw(batch);

		controller.grass3.getSprite().draw(batch);
		controller.grass4.getSprite().draw(batch);
		controller.grass1.getSprite().draw(batch);
		controller.grass2.getSprite().draw(batch);
	}

	private void renderNoClear1() {

		controller.eye.getSprite().draw(batch);
		controller.dragonGame.getSprite().draw(batch);
		controller.player.getSprite().draw(batch);

		controller.grass3.getSprite().draw(batch);
		controller.grass4.getSprite().draw(batch);
		controller.grass1.getSprite().draw(batch);
		controller.grass2.getSprite().draw(batch);

	}

	private void renderNoClear2() {

		controller.whiteBoard.getSprite().draw(batch);

		controller.eye.getSprite().draw(batch);
		controller.dragonGame.getSprite().draw(batch);

		controller.notClearMessage.getSprite().draw(batch);
		controller.player.getSprite().draw(batch);

		if (controller.menu.focused) {
			Rectangle bounds = controller.menu.getSprite().getBoundingRectangle();
			controller.circle.getSprite().setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
			controller.circle.getSprite().setAlpha(0.5f);
			controller.circle.getSprite().draw(batch);
		}

		if (controller.retry.focused) {
			Rectangle bounds = controller.retry.getSprite().getBoundingRectangle();
			controller.circle.getSprite().setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
			controller.circle.getSprite().setAlpha(0.5f);
			controller.circle.getSprite().draw(batch);
		}

		controller.menu.getSprite().draw(batch);
		controller.retry.getSprite().draw(batch);

		controller.grass3.getSprite().draw(batch);
		controller.grass4.getSprite().draw(batch);
		controller.grass1.getSprite().draw(batch);
		controller.grass2.getSprite().draw(batch);

	}

	private void renderGuide() {

		float[] vertices = controller.guide.getVertices();

		Gdx.gl.glLineWidth(3.0f);

		shapeRenderer.setColor(0.5f, 0.5f, 1.0f, 0.7f);
		shapeRenderer.line(vertices[0], vertices[1], vertices[2], vertices[3]);

	}

	private void renderTimer() {

		StringBuilder timer = GaryouUtil.floatToString(controller.timer);

		Assets.instance.bitmapFont.setScale(1.0f);
		Assets.instance.bitmapFont.setColor(0.0f, 0.0f, 1.0f, 1.0f);
		Assets.instance.bitmapFont.draw(batch, timer, 8.0f, 464.0f);

	}

	private void renderBulletPartcle() {

		if (!Assets.instance.bulletEffect.isComplete()) {
			float deltaTime = Gdx.graphics.getDeltaTime();
			Assets.instance.bulletEffect.draw(batch, deltaTime);
		}
	}

	private void renderHitPartcle() {

		if (!Assets.instance.hitEffect.isComplete()) {
			float deltaTime = Gdx.graphics.getDeltaTime();
			Assets.instance.hitEffect.draw(batch, deltaTime);
		}
	}

}
