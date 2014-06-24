package org.netf.garyou.game;

import org.netf.garyou.accessors.GameObjectAccessor;
import org.netf.garyou.game.GameController.STATE;
import org.netf.garyou.game.objects.base.GameObject;
import org.netf.garyou.util.Constants;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class FinalController {

	private TweenManager tweenManager;

	public GameObject dragonGame;
	public GameObject moon;
	public GameObject player;
	public GameObject grass1;
	public GameObject grass2;
	public GameObject grass3;
	public GameObject grass4;
	public GameObject guard;
	public GameObject[] guards;
	public GameObject eye;
	public GameObject bullet;
	public GameObject circle;

	public GameObject notClearMessage;
	public GameObject whiteBoard;
	public GameObject menu;
	public GameObject retry;

	public Polyline guide;

	public static enum STATE {
		NULL, READY, MAIN, FIRE, CLEAR1, CLEAR2, NOT_CLEAR1, NOT_CLEAR2
	}

	public float timer;
	private float stateTime;

	private FinalTimelines timelines;

	private Timeline ready;
	private Timeline main;
	private Timeline foreground1;
	private Timeline foreground2;
	private Timeline guardT;
	private Timeline fire;

	private STATE state;

	public STATE getState() {

		if (state == null) {
			return STATE.NULL;
		}
		return state;
	}

	public FinalController() {
		init();
	}

	public void init() {

		state = STATE.READY;
		timer = 30.0f;
		stateTime = 0f;

		tweenManager = new TweenManager();

		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
		Tween.setCombinedAttributesLimit(5);

		dragonGame = new GameObject(Assets.instance.dragonGame, Constants.OUT_OF_X, Constants.OUT_OF_Y, 10.0f, 15.0f);
		moon = new GameObject(Assets.instance.moon, 7.0f, 11.5f, 4.0f, 4.0f);

		guard = new GameObject(Assets.instance.guard, Constants.OUT_OF_X, Constants.OUT_OF_Y, 0.4f, 0.4f);
		guard.getSprite().setOrigin(0.2f, 0.2f);

		guards = new GameObject[8];
		for (int i = 0; i < guards.length; i++) {
			guards[i] = new GameObject(new Sprite(Assets.instance.guard), Constants.OUT_OF_X, Constants.OUT_OF_Y, 0.4f, 0.4f);
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

		notClearMessage = new GameObject(Assets.instance.notClear, 5.0f, 19.0f, 8.0f, 4.0f);
		whiteBoard = new GameObject(Assets.instance.white, 15.0f, 7.5f, 10.0f, 8.0f, 0.5f);
		menu = new GameObject(Assets.instance.menu, 15.0f, 7.5f, 3.5f, 1.75f);
		retry = new GameObject(Assets.instance.retry, 15.0f, 7.5f, 3.5f, 1.75f);

		circle = new GameObject(Assets.instance.circle, 2.0f, 2.0f, 4.0f, 4.0f, 0.5f);

		guide = new Polyline(new float[4]);

		timelines = new FinalTimelines(this);

		ready = timelines.createReady();
		ready.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				state = STATE.MAIN;
				timer = 30.0f;
				main.start(tweenManager);
				guardT.start(tweenManager);
				foreground1.start(tweenManager);
				foreground2.start(tweenManager);
			}
		}).setCallbackTriggers(TweenCallback.END); //
		ready.start(tweenManager);

		main = timelines.createMain();
		guardT = timelines.createGuardT();

		foreground1 = timelines.createForeground1();
		foreground2 = timelines.createForeground2();

	}

	public void update(float deltaTime) {

		if (state == STATE.FIRE) {
			checkHit(deltaTime);
		} else {
			tweenManager.update(deltaTime);
			updateGuardPosition();
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
			updateGuardPosition();
			updateEyePosition();

			float x1 = bullet.getSprite().getX() + bullet.getSprite().getWidth() / 2.0f;
			float y1 = bullet.getSprite().getY() + bullet.getSprite().getHeight() / 2.0f;
			float r1 = bullet.getSprite().getWidth() / 2.0f;

			if (isHitGuard(x1, y1, r1)) {
				startNotClear();
				break;
			}

			float x2 = eye.getSprite().getX() + eye.getSprite().getWidth() / 2.0f;
			float y2 = eye.getSprite().getY() + eye.getSprite().getHeight() / 2.0f;
			float r2 = eye.getSprite().getWidth() / 2.0f;

			if ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) <= (r1 + r2) * (r1 + r2)) {
				startClear(x2, y2);
				break;
			}
		}

	}

	private boolean isHitGuard(float x1, float y1, float r1) {

		for (int i = 0; i < guards.length; i++) {

			float x2 = guards[i].getSprite().getX() + guards[i].getSprite().getWidth() / 2.0f;
			float y2 = guards[i].getSprite().getY() + guards[i].getSprite().getHeight() / 2.0f;
			float r2 = guards[i].getSprite().getWidth() / 2.0f;

			if ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) <= (r1 + r2) * (r1 + r2)) {

				fire.kill();

				Assets.instance.bulletEffect.allowCompletion();
				Assets.instance.hitEffect.setPosition(x1 * Constants.VIEWPORT_GUI_WIDTH / Constants.VIEWPORT_WIDTH //
				, y1 * Constants.VIEWPORT_GUI_HEIGHT / Constants.VIEWPORT_HEIGHT);
				Assets.instance.hitEffect.start();

				return true;
			}

		}

		return false;
	}

	private void startClear(float x, float y) {

		state = STATE.CLEAR1;

		main.kill();
		fire.kill();

		Assets.instance.bulletEffect.allowCompletion();

		Assets.instance.hitEffect.setPosition(x * Constants.VIEWPORT_GUI_WIDTH / Constants.VIEWPORT_WIDTH //
		, y * Constants.VIEWPORT_GUI_HEIGHT / Constants.VIEWPORT_HEIGHT);
		Assets.instance.hitEffect.start();

		// float x1 = dragonGame.getSprite().getX() +
		// dragonGame.getSprite().getWidth() / 2.0f;
		// float y1 = dragonGame.getSprite().getY() +
		// dragonGame.getSprite().getHeight() / 2.0f;
		// float a1 = dragonGame.getSprite().getColor().a;
		//
		// Timeline timeline = timelines.createClear1(x1, y1, a1);
		// timeline.setCallbackTriggers(TweenCallback.END);
		// timeline.setCallback(new TweenCallback() {
		// @Override
		// public void onEvent(int type, BaseTween<?> source) {
		// state = STATE.CLEAR2;
		// timelines.createClear2().start(tweenManager);
		// }
		// });
		//
		// timeline.start(tweenManager);

	}

	private void startNotClear() {

		state = STATE.NOT_CLEAR1;

		Assets.instance.bulletEffect.allowCompletion();

		Timeline timeline = timelines.createNotClear1();

		timeline.setCallbackTriggers(TweenCallback.END);

		timeline.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				state = STATE.NOT_CLEAR2;
				timelines.createNotClear2().start(tweenManager);
			}
		});

		timeline.start(tweenManager);

	}

	private void updateGuardPosition() {

		float x = dragonGame.getSprite().getX();
		float y = dragonGame.getSprite().getY();
		float width = dragonGame.getSprite().getWidth();
		float height = dragonGame.getSprite().getHeight();
		guard.getSprite().setCenter(x + width / 2.0f - 1.44f, y + height / 2.0f + 3.5f);

		float a = eye.getSprite().getColor().a;

		float length = 3.5f;

		for (int i = 0; i < guards.length; i++) {

			float rad1 = guard.getSprite().getRotation() + (360 / guards.length * i);

			float x1 = length * MathUtils.cosDeg(rad1) + guard.getSprite().getX();
			float y1 = length * MathUtils.sinDeg(rad1) + guard.getSprite().getY();
			float width1 = guard.getSprite().getWidth();
			float height1 = guard.getSprite().getHeight();

			guards[i].getSprite().setCenter(x1 + width1 / 2.0f, y1 + height1 / 2.0f);
			guards[i].getSprite().setAlpha(a);

		}

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

	}

}
