package org.netf.garyou.game;

import org.netf.garyou.WebRtcResolver;
import org.netf.garyou.accessors.GameObjectAccessor;
import org.netf.garyou.game.objects.base.GameObject;
import org.netf.garyou.util.Constants;
import org.netf.garyou.util.GaryouUtil;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class VsController {

	private TweenManager tweenManager;

	public GameObject dragonGame;
	public GameObject moon;
	public GameObject player1;
	public GameObject player2;
	public GameObject grass1;
	public GameObject grass2;
	public GameObject grass3;
	public GameObject grass4;
	public GameObject eye;
	public GameObject bullet;
	public GameObject bulletPlayer2;
	public GameObject circle;
	public GameObject center;

	public GameObject whiteBoard;
	public GameObject retry;
	public GameObject win;
	public GameObject lose;
	public GameObject draw;

	public GameObject[] cloud;

	public GameObject one;
	public GameObject two;
	public GameObject three;
	public GameObject start;
	public GameObject connect;

	public Polyline guide;

	public static enum STATE {
		NULL, READY, WAIT, SYNC, MAIN, FIRE, CLEAR1, CLEAR2, CLEAR3, NOT_CLEAR1, NOT_CLEAR2, NOT_CLEAR3
	}

	private STATE state;

	public static enum STATE_PLAYER2 {
		NULL, PLAYING, CLEAR, NOTCLEAR
	}

	private STATE_PLAYER2 statePlayer2;

	public float timer;
	public float timerPlayer2;

	private float stateTime;

	private VsTimelines timelines;

	private Timeline ready;
	private Timeline main;
	private Timeline foreground1;
	private Timeline foreground2;
	private Timeline fire;
	private Timeline firePlayer2;
	private Timeline clear1;
	private Timeline notClear1;

	private WebRtcResolver webRtcResolver;

	public STATE getState() {
		if (state == null) {
			return STATE.NULL;
		}
		return state;
	}

	public STATE_PLAYER2 getStatePlayer2() {
		if (statePlayer2 == null) {
			return STATE_PLAYER2.NULL;
		}
		return statePlayer2;
	}

	public VsController(WebRtcResolver webRtcResolver) {
		this.webRtcResolver = webRtcResolver;
		init(false);
	}

	public void init(final boolean isRetry) {

		state = STATE.READY;
		timer = 30.0f;
		stateTime = 0f;
		timerPlayer2 = -1.0f;

		tweenManager = new TweenManager();

		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
		Tween.setCombinedAttributesLimit(5);

		dragonGame = new GameObject(Assets.instance.dragonGame, Constants.OUT_OF_X, Constants.OUT_OF_Y, 10.0f, 15.0f);
		moon = new GameObject(Assets.instance.moon, 7.0f, 11.5f, 4.0f, 4.0f);

		center = new GameObject(new Sprite(Assets.instance.guard), 5.0f, 7.5f, 0.4f, 0.4f);

		player1 = new GameObject((Sprite) Assets.instance.player1.getKeyFrame(0), -1.25f, 1.5f, 1.5f, 2.0f);
		player2 = new GameObject((Sprite) Assets.instance.player2.getKeyFrame(0), 1.75f, 1.5f, 1.5f, 2.0f, 0.3f);

		grass1 = new GameObject(Assets.instance.grass1, 5.0f, 1.25f, 10.0f, 2.5f);
		grass2 = new GameObject(Assets.instance.grass2, 15.0f, 1.25f, 10.0f, 2.5f);
		grass3 = new GameObject(Assets.instance.grass3, 5.0f, 1.25f, -10.0f, 2.5f);
		grass4 = new GameObject(Assets.instance.grass4, 15.0f, 1.25f, -10.0f, 2.5f);
		grass3.getSprite().setColor(0.5f, 0.5f, 0.5f, 1.0f);
		grass4.getSprite().setColor(0.5f, 0.5f, 0.5f, 1.0f);

		bullet = new GameObject(Assets.instance.bullet, Constants.FIRST_BULLET_X, Constants.FIRST_BULLET_Y, 0.5f, 0.5f);
		bulletPlayer2 = new GameObject(new Sprite(Assets.instance.bullet), Constants.FIRST_BULLET_X, Constants.FIRST_BULLET_Y, 0.5f, 0.5f, 0.4f);

		eye = new GameObject(Assets.instance.eye, 5.0f, 7.5f, 0.4f, 0.4f);

		whiteBoard = new GameObject(Assets.instance.white, 15.0f, 7.5f, 10.0f, 8.0f, 0.5f);
		retry = new GameObject(Assets.instance.retry, 15.0f, 7.5f, 3.5f, 1.75f);

		win = new GameObject(Assets.instance.win, 5.0f, 19.0f, 6.0f, 3.0f);
		lose = new GameObject(Assets.instance.lose, 5.0f, 19.0f, 6.0f, 3.0f);
		draw = new GameObject(Assets.instance.draw, 5.0f, 19.0f, 9.0f, 3.0f);

		one = new GameObject(Assets.instance.one, 5.0f, 19.0f, 1.0f, 1.0f);
		two = new GameObject(Assets.instance.two, 5.0f, 19.0f, 1.0f, 1.0f);
		three = new GameObject(Assets.instance.three, 5.0f, 19.0f, 1.0f, 1.0f);
		start = new GameObject(Assets.instance.start, 5.0f, 19.0f, 2.0f, 1.0f);
		connect = new GameObject(Assets.instance.connect, 5.0f, 6.5f, 2.0f, 0.5f);

		circle = new GameObject(Assets.instance.circle, 2.0f, 2.0f, 4.0f, 4.0f, 0.5f);

		guide = new Polyline(new float[4]);

		timelines = new VsTimelines(this);

		ready = timelines.createReady();
		ready.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {

				if (isRetry) {
					startSync();
				} else {
					state = STATE.WAIT;
					if (webRtcResolver.isPlayer1()) {
						webRtcResolver.init();
					} else {
						String id = webRtcResolver.getConnectionIdFromUrl();
						webRtcResolver.connect(id);
					}
				}

				foreground1.start(tweenManager);
				foreground2.start(tweenManager);
			}
		}).setCallbackTriggers(TweenCallback.END); //
		ready.start(tweenManager);

		foreground1 = timelines.createForeground1();
		foreground2 = timelines.createForeground2();

		initCloud();

	}

	private void initCloud() {

		cloud = new GameObject[10];

		for (int i = 0; i < 5; i++) {
			float x = MathUtils.random(10.0f);
			float y = MathUtils.random(5.0f, 15.0f);
			cloud[i] = new GameObject(new Sprite(Assets.instance.cloud1), x, y, 6.0f, 2.0f);
		}

		for (int i = 5; i < 10; i++) {
			float x = MathUtils.random(10.0f);
			float y = MathUtils.random(5.0f, 15.0f);
			cloud[i] = new GameObject(new Sprite(Assets.instance.cloud2), x, y, 6.0f, 2.0f);
		}

	}

	public void update(float deltaTime) {

		if (state == STATE.FIRE) {
			checkHit(deltaTime);
		} else {
			tweenManager.update(deltaTime);
			updateEyePosition();
		}

		updateCloudPosition();

		if (state == STATE.MAIN || state == STATE.FIRE) {
			timer = timer - deltaTime;
			if (timer < 0.0f || timer < timerPlayer2) {
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

		if (state == STATE.CLEAR2) {

			if (statePlayer2 == STATE_PLAYER2.PLAYING) {
				;
			} else {
				state = STATE.CLEAR3;
				Timeline timeline = null;
				if (timer > timerPlayer2) {
					// 勝ち
					timeline = timelines.createClear3(win, 1.0f, 1.0f, 6.0f, 3.0f).start(tweenManager);
				} else if (timer == timerPlayer2) {
					// 引き分け
					timeline = timelines.createClear3(draw, 1.0f, 1.0f, 6.0f, 3.0f).start(tweenManager);
				} else {
					// 負け
					timeline = timelines.createClear3(lose, 1.0f, 1.0f, 6.0f, 3.0f).start(tweenManager);
				}
				timeline.start(tweenManager);
			}
		}

		if (state == STATE.NOT_CLEAR2) {

			if (statePlayer2 == STATE_PLAYER2.PLAYING) {
				;
			} else {
				state = STATE.NOT_CLEAR3;
				Timeline timeline = null;
				if (statePlayer2 == STATE_PLAYER2.CLEAR) {
					// 負け
					timeline = timelines.createNotClear3(lose, 1.0f, 1.0f, 6.0f, 3.0f);
				} else {
					// 引き分け
					timeline = timelines.createNotClear3(draw, 1.0f, 1.0f, 9.0f, 3.0f);
				}
				timeline.start(tweenManager);
			}
		}

		stateTime = stateTime + deltaTime;
		Sprite keyFrame;
		keyFrame = (Sprite) Assets.instance.player1.getKeyFrame(stateTime, true);
		player1.setSprite(keyFrame);
		keyFrame = (Sprite) Assets.instance.player2.getKeyFrame(stateTime, true);
		player2.setSprite(keyFrame);

	}

	private void updateCloudPosition() {

		float time = Gdx.graphics.getDeltaTime();

		for (int i = 0; i < cloud.length; i++) {

			float x = cloud[i].getSprite().getX();

			if (i < 5) {
				x = x - 10.0f * time;
			} else {
				x = x - 5.0f * time;
			}

			cloud[i].getSprite().setX(x);

			if (x < -6.0f) {
				float y = MathUtils.random(5.0f, 15.0f);
				cloud[i].getSprite().setCenter(13.0f, y);
			}
		}

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

	private void startClear(float x, float y) {

		state = STATE.CLEAR1;

		Assets.instance.bulletEffect.allowCompletion();

		Assets.instance.hitEffect.setPosition(x * Constants.VIEWPORT_GUI_WIDTH / Constants.VIEWPORT_WIDTH //
		, y * Constants.VIEWPORT_GUI_HEIGHT / Constants.VIEWPORT_HEIGHT);
		Assets.instance.hitEffect.start();

		float x1 = dragonGame.getSprite().getX() + dragonGame.getSprite().getWidth() / 2.0f;
		float y1 = dragonGame.getSprite().getY() + dragonGame.getSprite().getHeight() / 2.0f;

		clear1 = timelines.createClear1(x1, y1);
		clear1.setCallbackTriggers(TweenCallback.END);
		clear1.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				if (state == STATE.CLEAR1) {
					state = STATE.CLEAR2;
				}
			}
		});
		clear1.start(tweenManager);

		main.kill();
		fire.kill();

		String message = "hit:" + GaryouUtil.floatToString(timer);
		webRtcResolver.sendMessage(message);

	}

	private void startNotClear() {

		state = STATE.NOT_CLEAR1;

		Assets.instance.bulletEffect.allowCompletion();

		notClear1 = timelines.createNotClear1();
		notClear1.setCallbackTriggers(TweenCallback.END);
		notClear1.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				if (state == STATE.NOT_CLEAR1) {
					state = STATE.NOT_CLEAR2;
				}
			}
		});
		notClear1.start(tweenManager);

		String message = "nohit";
		webRtcResolver.sendMessage(message);

	}

	private void updateEyePosition() {

		float x = dragonGame.getSprite().getX();
		float y = dragonGame.getSprite().getY();
		float width = dragonGame.getSprite().getWidth();
		float height = dragonGame.getSprite().getHeight();
		eye.getSprite().setCenter(x + width / 2.0f - 1.44f, y + height / 2.0f + 3.5f);

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

		fire = timelines.createFire(x1, y1, x2, y2);

		fire.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				startNotClear();
			}
		}).setCallbackTriggers(TweenCallback.END);

		fire.start(tweenManager);

		String message = "fire:" + timer + ":" + rad1;
		webRtcResolver.sendMessage(message);

	}

	public void startGame() {

		state = STATE.MAIN;

		timer = 30.0f;
		main = timelines.createMain();
		main.start(tweenManager);

		timerPlayer2 = -1.0f;
		statePlayer2 = STATE_PLAYER2.PLAYING;

	}

	public void startPlayer2Fire(float time, float rad) {

		float x1 = Constants.FIRST_BULLET_X;
		float y1 = Constants.FIRST_BULLET_Y;

		float x2 = 15.0f * MathUtils.cos(rad) + x1;
		float y2 = 15.0f * MathUtils.sin(rad) + y1;

		firePlayer2 = timelines.createPlayer2Fire(x1, y1, x2, y2);
		firePlayer2.update(timer - time);
		firePlayer2.start(tweenManager);

	}

	public void startPlayer2Hit(float time) {

		if (firePlayer2 != null) {
			firePlayer2.kill();
		}

		timerPlayer2 = time;
		Timeline timeline = timelines.createPlayer2Hit();
		timeline.start(tweenManager);
		statePlayer2 = STATE_PLAYER2.CLEAR;
	}

	public void startPlayer2NoHit() {

		if (firePlayer2 != null) {
			firePlayer2.kill();
		}

		timelines.createPlayer2Hit().start(tweenManager);
		statePlayer2 = STATE_PLAYER2.NOTCLEAR;
	}

	public void startRetry() {
		webRtcResolver.sendMessage("retry");
		init(true);
	}

	public void startSync() {

		state = STATE.SYNC;

		timelines.createSync() //
				.setCallbackTriggers(TweenCallback.END) //
				.setCallback(new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						startGame();
					}
				}) //
				.start(tweenManager);

	}
}
