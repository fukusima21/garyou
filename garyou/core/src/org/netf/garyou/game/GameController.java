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
import aurelienribon.tweenengine.equations.Cubic;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.equations.Sine;

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

	public Polyline guide;

	public float timer;
	private float stateTime;

	public static enum STATE {
		NULL, READY, MAIN, CHARGE, FIRE, FINISH
	}

	private STATE state;

	private Timeline ready;
	private Timeline main;
	private Timeline background;
	private Timeline foreground1;
	private Timeline foreground2;
	private Timeline fire;
	private Timeline finish;

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

	private void init() {

		state = STATE.READY;

		dragonGame = new GameObject(Assets.instance.dragonGame, 5.0f, 15.0f, 10.0f, 15.0f, 0.0f);
		moon = new GameObject(Assets.instance.moon, 7.0f, 11.5f, 4.0f, 4.0f, 1.0f);

		switch (mode) {
		case EASY:
			back = new GameObject(Assets.instance.back1, 30.0f, 7.5f, 40.0f, 15.0f, 0.7f);
			break;
		case NORMAL:
			back = new GameObject(Assets.instance.back2, 30.0f, 7.5f, 40.0f, 15.0f, 0.7f);
			break;
		case HARD:
			back = new GameObject(Assets.instance.back3, 30.0f, 7.5f, 40.0f, 15.0f, 0.7f);
			break;
		}

		player = new GameObject((Sprite) Assets.instance.player.getKeyFrame(0), 1.25f, 1.5f, 1.5f, 2.0f, 0.0f);

		grass1 = new GameObject(Assets.instance.grass1, 5.0f, 1.25f, 10.0f, 2.5f, 0.8f);
		grass2 = new GameObject(Assets.instance.grass2, 15.0f, 1.25f, 10.0f, 2.5f, 0.8f);
		grass3 = new GameObject(Assets.instance.grass3, 5.0f, 1.25f, 10.0f, 2.5f, 0.4f);
		grass4 = new GameObject(Assets.instance.grass4, 15.0f, 1.25f, 10.0f, 2.5f, 0.4f);

		bullet = new GameObject(Assets.instance.bullet, Constants.FIRST_BULLET_X, Constants.FIRST_BULLET_Y, 0.5f, 0.5f, 1.0f);

		eye = new GameObject(Assets.instance.eye, 5.0f, 7.5f, 0.4f, 0.4f, 1.0f);

		guide = new Polyline(new float[4]);

		tweenManager = new TweenManager();

		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
		Tween.setCombinedAttributesLimit(5);

		ready = Timeline.createSequence() //
				.push(Tween.set(grass1, GameObjectAccessor.MOVE).target(15.0f, 1.25f)) //
				.push(Tween.set(back, GameObjectAccessor.MOVE).target(30.0f, 7.5f)) //
				.push(Tween.set(moon, GameObjectAccessor.MOVE_SIZE_ALPHA).target(7.0f, 11.5f, 4.0f, 4.0f, 1.0f)) //
				.beginParallel() //
				.push(Tween.to(grass1, GameObjectAccessor.MOVE, 0.5f).target(5.0f, 1.25f).ease(Cubic.IN)) //
				.push(Tween.to(back, GameObjectAccessor.MOVE, 0.5f).target(20.0f, 7.5f).ease(Cubic.IN)) //
				.push(Tween.to(moon, GameObjectAccessor.MOVE_SIZE_ALPHA, 0.5f).target(7.3f, 12.4f, 0.5f, 0.5f, 0.0f).ease(Quad.IN)) //
				.end() //
				.push(Tween.set(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA).target(5.0f, -30.0f, 20.0f, 30.0f, 0.1f)) //
				.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA, 1.0f).target(5.0f, 7.5f, 20.0f, 30.0f, 0.5f).ease(Sine.INOUT)) //
				.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA, 1.0f).target(7.0f, 9.0f, 7.0f, 10.5f, 1.0f).ease(Sine.INOUT)) //
				.push(Tween.set(player, GameObjectAccessor.MOVE_SIZE_ALPHA).target(-1.25f, 1.5f, 1.5f, 2.0f, 0.3f)) //
				.push(Tween.to(player, GameObjectAccessor.MOVE_SIZE_ALPHA, 1.0f).target(1.5f, 1.5f, 1.5f, 2.0f, 0.8f).ease(Sine.INOUT)) //
				.repeat(0, 0.0f); //

		createMainTimeline();

		ready.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				state = STATE.MAIN;
				timer = 30.0f;

				if (main != null) {
					main.start(tweenManager);
				}
				background.start(tweenManager);
				foreground1.start(tweenManager);
				foreground2.start(tweenManager);

			}
		}).setCallbackTriggers(TweenCallback.END);

		ready.start(tweenManager);

		stateTime = 0f;

	}

	public void update(float deltaTime) {

		tweenManager.update(deltaTime);

		if (state == STATE.MAIN || state == STATE.CHARGE || state == STATE.FIRE) {
			timer = timer - deltaTime;
		}

		if (state == STATE.FIRE) {

			Rectangle bounds = bullet.getSprite().getBoundingRectangle();
			float x = bounds.x + bounds.width / 2.0f;
			float y = bounds.y + bounds.height / 2.0f;

			Assets.instance.bulletEffect.setPosition(x * Constants.VIEWPORT_GUI_WIDTH / Constants.VIEWPORT_WIDTH //
			, y * Constants.VIEWPORT_GUI_HEIGHT / Constants.VIEWPORT_HEIGHT);

			if (Assets.instance.bulletEffect.isComplete()) {
				// TODO 終了判定へ
				state = STATE.MAIN;
			}

		}

		stateTime = stateTime + deltaTime;
		Sprite keyFrame = (Sprite) Assets.instance.player.getKeyFrame(stateTime, true);
		player.setSprite(keyFrame);

		{
			float x = dragonGame.getSprite().getX();
			float y = dragonGame.getSprite().getY();
			float width = dragonGame.getSprite().getWidth();
			float height = dragonGame.getSprite().getHeight();

			eye.getSprite().setCenter(x + width / 2.0f - 1.44f, y + height / 2.0f + 3.5f);
		}

		if (state == STATE.FIRE) {
			checkHit();
		}

	}

	// 当たり判定
	private void checkHit() {

		float x1 = bullet.getSprite().getX();
		float y1 = bullet.getSprite().getY();
		float r1 = bullet.getSprite().getWidth() / 2.0f;

		float x2 = eye.getSprite().getX();
		float y2 = eye.getSprite().getY();
		float r2 = eye.getSprite().getWidth() / 2.0f;

		if ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) <= (r1 + r2) * (r1 + r2)) {
			// TODO HIT＆終了
			fire.kill();
			main.kill();
			bullet.getSprite().setAlpha(0.0f);
			Assets.instance.bulletEffect.allowCompletion();
		}

	}

	private Timeline createMainTimeline() {

		background = Timeline.createSequence() //
				.push(Tween.set(back, GameObjectAccessor.MOVE).target(20.0f, 7.5f)) //
				.push(Tween.to(back, GameObjectAccessor.MOVE, 30.0f).target(-10.0f, 7.5f).ease(Linear.INOUT)) //
				.repeat(0, 0.0f); //

		foreground1 = Timeline.createSequence() //
				.push(Tween.set(grass1, GameObjectAccessor.MOVE).target(5.0f, 1.25f)) //
				.push(Tween.set(grass2, GameObjectAccessor.MOVE).target(15.0f, 1.25f)) //
				.beginParallel() //
				.push(Tween.to(grass1, GameObjectAccessor.MOVE, 2.0f).target(-5.0f, 1.25f).ease(Linear.INOUT)) //
				.push(Tween.to(grass2, GameObjectAccessor.MOVE, 2.0f).target(5.0f, 1.25f).ease(Linear.INOUT)) //
				.end() //
				.repeat(-1, 0.0f);

		foreground2 = Timeline.createSequence() //
				.push(Tween.set(grass3, GameObjectAccessor.MOVE).target(5.0f, 1.25f)) //
				.push(Tween.set(grass4, GameObjectAccessor.MOVE).target(15.0f, 1.25f)) //
				.beginParallel() //
				.push(Tween.to(grass3, GameObjectAccessor.MOVE, 4.0f).target(-5.0f, 1.25f).ease(Linear.INOUT)) //
				.push(Tween.to(grass4, GameObjectAccessor.MOVE, 4.0f).target(5.0f, 1.25f).ease(Linear.INOUT)) //
				.end() //
				.repeat(-1, 0.0f);

		switch (mode) {
		case EASY:
			// 出現後、左右にゆれる
			main = Timeline.createSequence() //
					.push(Tween.set(dragonGame, GameObjectAccessor.MOVE_SIZE).target(7.0f, 9.0f, 7.0f, 10.5f)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE, 3.0f).target(10.0f, 9.0f, 7.0f, 10.5f).ease(Sine.INOUT)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE, 6.0f).target(4.0f, 9.0f, 7.0f, 10.5f).ease(Sine.INOUT)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE, 3.0f).target(7.0f, 9.0f, 7.0f, 10.5f).ease(Sine.INOUT)) //
					.repeat(-1, 0.0f); //
			break;
		case NORMAL:
			// 出現後、上下にゆれる
			main = Timeline.createSequence() //
					.push(Tween.set(dragonGame, GameObjectAccessor.MOVE_SIZE).target(7.0f, 9.0f, 7.0f, 10.5f)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE, 1.0f).target(7.0f, 5.0f, 7.0f, 10.5f).ease(Sine.INOUT)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE, 1.0f).target(7.0f, 15.0f, 7.0f, 10.5f).ease(Sine.INOUT)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE, 1.0f).target(7.0f, 9.0f, 7.0f, 10.5f).ease(Sine.INOUT)) //
					.repeat(-1, 0.0f); //
			break;
		case HARD:
			// 出現後、回転
			dragonGame.getSprite().setOrigin(5.0f, 7.5f);
			main = Timeline.createSequence() //
					.push(Tween.set(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA).target(5.0f, 7.5f, 7.0f, 10.5f, 1.0f)) //
					.push(Tween.set(dragonGame, GameObjectAccessor.ROTATE).target(360.0f)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.ROTATE, 2.0f).target(0.0f).ease(Sine.INOUT)) //
					.repeatYoyo(-1, 0.0f); //
			break;
		}

		return null;
	}

	public void onCharge(Vector3 touchPoint) {

		state = STATE.CHARGE;

		float x1 = Constants.FIRST_BULLET_X;
		float y1 = Constants.FIRST_BULLET_Y;

		float x2 = touchPoint.x;
		float y2 = touchPoint.y;

		float rad1 = MathUtils.atan2(y2 - y1, x2 - x1);

		float[] vertices = guide.getVertices();

		vertices[0] = 1.0f * MathUtils.cos(rad1) + x1;
		vertices[1] = 1.0f * MathUtils.sin(rad1) + y1;
		vertices[2] = 15.0f * MathUtils.cos(rad1) + x1;
		vertices[3] = 15.0f * MathUtils.sin(rad1) + y1;

		bullet.getSprite().setAlpha(1.0f);
		bullet.getSprite().setCenter(x1, y1);

		Assets.instance.bulletEffect.setPosition( //
				x1 * Constants.VIEWPORT_GUI_WIDTH / Constants.VIEWPORT_WIDTH //
				, y1 * Constants.VIEWPORT_GUI_HEIGHT / Constants.VIEWPORT_HEIGHT);

		Assets.instance.bulletEffect.start();

	}

	public void onFire(Vector3 touchPoint) {

		state = STATE.FIRE;

		float x1 = Constants.FIRST_BULLET_X;
		float y1 = Constants.FIRST_BULLET_Y;

		float x2 = touchPoint.x;
		float y2 = touchPoint.y;

		float rad1 = MathUtils.atan2(y2 - y1, x2 - x1);

		float x3 = 15.0f * MathUtils.cos(rad1) + x1;
		float y3 = 15.0f * MathUtils.sin(rad1) + y1;

		fire = Timeline.createSequence() //
				.push(Tween.set(bullet, GameObjectAccessor.MOVE_ALPHA).target(x1, y1, 1.0f)) //
				.push(Tween.to(bullet, GameObjectAccessor.MOVE_ALPHA, 1.0f).target(x3, y3, 1.0f)) //
				.repeat(0, 0.0f); //

		fire.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				Assets.instance.bulletEffect.allowCompletion();
			}
		}).setCallbackTriggers(TweenCallback.END);

		fire.start(tweenManager);

	}

	public void onMove(Vector3 touchPoint) {

		float x1 = Constants.FIRST_BULLET_X;
		float y1 = Constants.FIRST_BULLET_Y;

		float x2 = touchPoint.x;
		float y2 = touchPoint.y;

		float rad1 = MathUtils.atan2(y2 - y1, x2 - x1);

		float[] vertices = guide.getVertices();

		vertices[0] = 1.0f * MathUtils.cos(rad1) + x1;
		vertices[1] = 1.0f * MathUtils.sin(rad1) + y1;
		vertices[2] = 15.0f * MathUtils.cos(rad1) + x1;
		vertices[3] = 15.0f * MathUtils.sin(rad1) + y1;

	}
}
