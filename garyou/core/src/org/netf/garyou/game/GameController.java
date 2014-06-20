package org.netf.garyou.game;

import org.netf.garyou.accessors.GameObjectAccessor;
import org.netf.garyou.game.objects.base.GameObject;
import org.netf.garyou.screens.GameScreen.MODE;
import org.netf.garyou.util.Constants;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class GameController extends InputAdapter {

	private TweenManager tweenManager;

	public GameObject dragonGame;
	public GameObject moon;
	public GameObject back;
	public GameObject player;
	public GameObject grass1;
	public GameObject grass2;
	public GameObject grass3;
	public GameObject grass4;
	public GameObject bullet;
	public GameObject eye;
	public GameObject clearMessage;
	public GameObject notClearMessage;
	public GameObject shout;
	public GameObject whiteBoard;
	public GameObject next;
	public GameObject menu;
	public GameObject retry;
	public GameObject finalStage;
	public GameObject circle;

	public Polyline guide;

	public float timer;
	private float stateTime;

	public static enum STATE {
		NULL, READY, MAIN, FIRE, CLEAR1, CLEAR2, NOT_CLEAR1, NOT_CLEAR2
	}

	private STATE state;

	private Timeline ready;
	private Timeline main;
	private Timeline background;
	private Timeline foreground1;
	private Timeline foreground2;
	private Timeline fire;

	private GameTimelines gameTimelines;

	public MODE mode;

	public STATE getState() {

		if (state == null) {
			return STATE.NULL;
		}
		return state;
	}

	public GameController(MODE mode) {
		this.mode = mode;
		init();
	}

	public void init() {

		state = STATE.READY;
		timer = 30.0f;
		stateTime = 0f;

		dragonGame = new GameObject(Assets.instance.dragonGame, 5.0f, 15.0f, 10.0f, 15.0f);
		moon = new GameObject(Assets.instance.moon, 7.0f, 11.5f, 4.0f, 4.0f);

		switch (mode) {
		case EASY:
			back = new GameObject(Assets.instance.back1, 30.0f, 7.5f, 40.0f, 15.0f, 1.0f);
			break;
		case NORMAL:
			back = new GameObject(Assets.instance.back2, 30.0f, 7.5f, 40.0f, 15.0f, 1.0f);
			break;
		case HARD:
			back = new GameObject(Assets.instance.back3, 30.0f, 7.5f, 40.0f, 15.0f, 1.0f);
			break;
		}

		player = new GameObject((Sprite) Assets.instance.player.getKeyFrame(0), -1.25f, 1.5f, 1.5f, 2.0f);
		grass1 = new GameObject(Assets.instance.grass1, 5.0f, 1.25f, 10.0f, 2.5f);
		grass2 = new GameObject(Assets.instance.grass2, 15.0f, 1.25f, 10.0f, 2.5f);
		grass3 = new GameObject(Assets.instance.grass3, 5.0f, 1.25f, -10.0f, 2.5f);
		grass4 = new GameObject(Assets.instance.grass4, 15.0f, 1.25f, -10.0f, 2.5f);
		grass3.getSprite().setColor(0.5f, 0.5f, 0.5f, 1.0f);
		grass4.getSprite().setColor(0.5f, 0.5f, 0.5f, 1.0f);

		bullet = new GameObject(Assets.instance.bullet, Constants.FIRST_BULLET_X, Constants.FIRST_BULLET_Y, 0.5f, 0.5f);

		eye = new GameObject(Assets.instance.eye, 5.0f, 7.5f, 0.4f, 0.4f);

		clearMessage = new GameObject(Assets.instance.clear, 5.0f, 19.0f, 8.0f, 4.0f);
		notClearMessage = new GameObject(Assets.instance.notClear, 5.0f, 19.0f, 8.0f, 4.0f);
		shout = new GameObject(Assets.instance.shout, 5.0f, 7.5f, 8.0f, 4.0f);

		whiteBoard = new GameObject(Assets.instance.white, 15.0f, 7.5f, 10.0f, 8.0f, 0.5f);

		menu = new GameObject(Assets.instance.menu, 15.0f, 7.5f, 3.5f, 1.75f);
		next = new GameObject(Assets.instance.next, 15.0f, 7.5f, 3.5f, 1.75f);
		finalStage = new GameObject(Assets.instance.finalStage, 15.0f, 7.5f, 3.5f, 1.75f);
		retry = new GameObject(Assets.instance.retry, 15.0f, 7.5f, 3.5f, 1.75f);

		circle = new GameObject(Assets.instance.circle, 2.0f, 2.0f, 4.0f, 4.0f, 0.5f);

		guide = new Polyline(new float[4]);

		tweenManager = new TweenManager();

		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
		Tween.setCombinedAttributesLimit(5);

		gameTimelines = new GameTimelines(this);

		ready = gameTimelines.createReady();

		ready.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				state = STATE.MAIN;
				timer = 30.0f;
				main.start(tweenManager);
				background.start(tweenManager);
				foreground1.start(tweenManager);
				foreground2.start(tweenManager);
			}
		}).setCallbackTriggers(TweenCallback.END) //
				.start(tweenManager);

		main = gameTimelines.createMain();

		foreground1 = gameTimelines.createForeground1();
		foreground2 = gameTimelines.createForeground2();

		background = gameTimelines.createBackground();
		background.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				foreground1.kill();
				foreground2.kill();
			}
		});

	}

	public void update(float deltaTime) {

		if (state == STATE.FIRE) {
			checkHit(deltaTime);
		} else {
			tweenManager.update(deltaTime);
			updateEyePosition();
		}

		if (state == STATE.MAIN || state == STATE.FIRE) {
			timer = timer - deltaTime;
			if (timer < 0.0f) {
				startNotClear();
			}

			if (state == STATE.FIRE) {

				Rectangle bounds = bullet.getSprite().getBoundingRectangle();
				float x = bounds.x + bounds.width / 2.0f;
				float y = bounds.y + bounds.height / 2.0f;

				Assets.instance.bulletEffect.setPosition(x * Constants.VIEWPORT_GUI_WIDTH / Constants.VIEWPORT_WIDTH //
				, y * Constants.VIEWPORT_GUI_HEIGHT / Constants.VIEWPORT_HEIGHT);

			}
		}

		stateTime = stateTime + deltaTime;
		Sprite keyFrame = (Sprite) Assets.instance.player.getKeyFrame(stateTime, true);
		player.setSprite(keyFrame);

	}

	// 当たり判定
	private void checkHit(float deltaTime) {

		float div = deltaTime / 30.0f;

		for (int i = 0; i < 30; i++) {

			tweenManager.update(div);
			updateEyePosition();

			float x1 = bullet.getSprite().getX() + bullet.getSprite().getWidth() / 2.0f;
			float y1 = bullet.getSprite().getY() + bullet.getSprite().getHeight() / 2.0f;
			float r1 = bullet.getSprite().getWidth() / 2.0f;

			float x2 = eye.getSprite().getX() + eye.getSprite().getWidth() / 2.0f;
			float y2 = eye.getSprite().getY() + eye.getSprite().getHeight() / 2.0f;
			float r2 = eye.getSprite().getWidth() / 2.0f;

			if ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) <= (r1 + r2) * (r1 + r2)) {
				startClear(x2, y2);
				break;
			}
		}

	}

	private void updateEyePosition() {

		float deg = dragonGame.getSprite().getRotation();

		if (mode == MODE.HARD) {
			deg = -deg - 22.363f;
			float length = 3.78465f;

			float x = dragonGame.getSprite().getX();
			float y = dragonGame.getSprite().getY();
			float width = dragonGame.getSprite().getWidth();
			float height = dragonGame.getSprite().getHeight();

			eye.getSprite().setCenter(x + width / 2.0f + length * MathUtils.sinDeg(deg) //
			, y + height / 2.0f + length * MathUtils.cosDeg(deg));

		} else {
			float x = dragonGame.getSprite().getX();
			float y = dragonGame.getSprite().getY();
			float width = dragonGame.getSprite().getWidth();
			float height = dragonGame.getSprite().getHeight();
			eye.getSprite().setCenter(x + width / 2.0f - 1.44f, y + height / 2.0f + 3.5f);
		}

	}

	public void onFire(Vector3 touchPoint) {

		state = STATE.FIRE;

		float x1 = Constants.FIRST_BULLET_X;
		float y1 = Constants.FIRST_BULLET_Y;

		float rad1 = MathUtils.atan2(touchPoint.y - y1, touchPoint.x - x1);

		float x2 = 15.0f * MathUtils.cos(rad1) + x1;
		float y2 = 15.0f * MathUtils.sin(rad1) + y1;

		Assets.instance.bulletEffect.setPosition( //
				x1 * Constants.VIEWPORT_GUI_WIDTH / Constants.VIEWPORT_WIDTH //
				, y1 * Constants.VIEWPORT_GUI_HEIGHT / Constants.VIEWPORT_HEIGHT);

		Assets.instance.bulletEffect.start();

		bullet.getSprite().setCenter(x1, y1);

		float[] vertices = guide.getVertices();

		vertices[0] = 1.0f * MathUtils.cos(rad1) + x1;
		vertices[1] = 1.0f * MathUtils.sin(rad1) + y1;
		vertices[2] = 15.0f * MathUtils.cos(rad1) + x1;
		vertices[3] = 15.0f * MathUtils.sin(rad1) + y1;

		fire = gameTimelines.createFire(x1, y1, x2, y2);

		fire.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				startNotClear();
			}
		}).setCallbackTriggers(TweenCallback.END);

		fire.start(tweenManager);

	}

	private void startClear(float x, float y) {

		state = STATE.CLEAR1;

		main.kill();
		fire.kill();

		Assets.instance.bulletEffect.allowCompletion();

		Assets.instance.hitEffect.setPosition(x * Constants.VIEWPORT_GUI_WIDTH / Constants.VIEWPORT_WIDTH //
		, y * Constants.VIEWPORT_GUI_HEIGHT / Constants.VIEWPORT_HEIGHT);
		Assets.instance.hitEffect.start();

		float x1 = dragonGame.getSprite().getX() + dragonGame.getSprite().getWidth() / 2.0f;
		float y1 = dragonGame.getSprite().getY() + dragonGame.getSprite().getHeight() / 2.0f;
		float a1 = dragonGame.getSprite().getColor().a;

		Timeline timeline = gameTimelines.createClear1(x1, y1, a1);
		timeline.setCallbackTriggers(TweenCallback.END);
		timeline.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				state = STATE.CLEAR2;
				gameTimelines.createClear2().start(tweenManager);
			}
		});

		timeline.start(tweenManager);

	}

	private void startNotClear() {

		state = STATE.NOT_CLEAR1;

		Assets.instance.bulletEffect.allowCompletion();

		Timeline timeline = gameTimelines.createNotClear1();

		timeline.setCallbackTriggers(TweenCallback.END);

		timeline.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				state = STATE.NOT_CLEAR2;
				gameTimelines.createNotClear2().start(tweenManager);
			}
		});

		timeline.start(tweenManager);

	}
}
