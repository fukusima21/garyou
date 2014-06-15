package org.netf.garyou.game;

import org.netf.garyou.accessors.GameObjectAccessor;
import org.netf.garyou.game.objects.base.GameObject;
import org.netf.garyou.screens.GameScreen.MODE;

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
import com.badlogic.gdx.math.Vector3;

public class GameController extends InputAdapter {

	public Vector3 touch;

	private TweenManager tweenManager;

	public GameObject dragonGame;
	public GameObject moon;
	public GameObject back;
	public GameObject player;
	public GameObject grass1;
	public GameObject grass2;

	public float timer;
	private float stateTime;

	public static enum STATE {
		READY, MAIN, FINISH
	}

	public STATE state;

	private Timeline ready;
	private Timeline main;
	private Timeline background;
	private Timeline foreground;
	private Timeline finish;

	public MODE mode;

	public GameController(MODE mode) {
		this.mode = mode;
		init();
	}

	private void init() {

		state = STATE.READY;

		touch = null;

		dragonGame = new GameObject(Assets.instance.dragonGame, 5.0f, 15.0f, 10.0f, 15.0f, 0.0f);
		moon = new GameObject(Assets.instance.moon, 7.0f, 11.5f, 4.0f, 4.0f, 1.0f);

		switch (mode) {
		case EASY:
			back = new GameObject(Assets.instance.back1, 30.0f, 7.5f, 40.0f, 15.0f, 0.5f);
			break;
		case NORMAL:
			back = new GameObject(Assets.instance.back2, 15.0f, 7.5f, 10.0f, 15.0f, 1.0f);
			break;
		case HARD:
			back = new GameObject(Assets.instance.back3, 15.0f, 7.5f, 10.0f, 15.0f, 1.0f);
			break;
		}

		player = new GameObject((Sprite) Assets.instance.player.getKeyFrame(0), 1.25f, 1.5f, 1.5f, 2.0f, 0.0f);

		grass1 = new GameObject(Assets.instance.grass1, 5.0f, 1.25f, 10.0f, 2.5f, 1.0f);
		grass2 = new GameObject(Assets.instance.grass2, 15.0f, 1.25f, 10.0f, 2.5f, 1.0f);

		tweenManager = new TweenManager();

		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
		Tween.setCombinedAttributesLimit(5);

		ready = Timeline.createSequence() //
				.push(Tween.set(grass1, GameObjectAccessor.MOVE).target(15.0f, 1.25f)) //
				.push(Tween.set(back, GameObjectAccessor.MOVE).target(30.0f, 7.5f)) //
				.push(Tween.set(moon, GameObjectAccessor.MOVE_SIZE).target(7.0f, 11.5f, 4.0f, 4.0f)) //
				.beginParallel() //
				.push(Tween.to(grass1, GameObjectAccessor.MOVE, 0.5f).target(5.0f, 1.25f).ease(Cubic.IN)) //
				.push(Tween.to(back, GameObjectAccessor.MOVE, 0.5f).target(20.0f, 7.5f).ease(Cubic.IN)) //
				.push(Tween.to(moon, GameObjectAccessor.MOVE_SIZE, 0.5f).target(7.3f, 12.4f, 0.5f, 0.5f).ease(Quad.IN)) //
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
				foreground.start(tweenManager);

			}
		}).setCallbackTriggers(TweenCallback.END);

		ready.start(tweenManager);

		stateTime = 0f;

	}

	public void update(float deltaTime) {

		tweenManager.update(deltaTime);

		if (state == STATE.MAIN) {
			timer = timer - deltaTime;

			switch (mode) {
			case EASY:
				// 出現後、停止
				break;
			case NORMAL:
				// 出現後、上下にゆれる
				break;
			case HARD:
				// 出現後、回転
				break;
			}

		}
		stateTime = stateTime + deltaTime;
		Sprite keyFrame = (Sprite) Assets.instance.player.getKeyFrame(stateTime, true);
		player.setSprite(keyFrame);

	}

	private Timeline createMainTimeline() {

		background = Timeline.createSequence() //
				.push(Tween.set(back, GameObjectAccessor.MOVE).target(20.0f, 7.5f)) //
				.push(Tween.to(back, GameObjectAccessor.MOVE, 30.0f).target(-10.0f, 7.5f).ease(Linear.INOUT)) //
				.repeat(0, 0.0f); //

		foreground = Timeline.createSequence() //
				.push(Tween.set(grass1, GameObjectAccessor.MOVE).target(5.0f, 1.25f)) //
				.push(Tween.set(grass2, GameObjectAccessor.MOVE).target(15.0f, 1.25f)) //
				.beginParallel() //
				.push(Tween.to(grass1, GameObjectAccessor.MOVE, 2.0f).target(-5.0f, 1.25f).ease(Linear.INOUT)) //
				.push(Tween.to(grass2, GameObjectAccessor.MOVE, 2.0f).target(5.0f, 1.25f).ease(Linear.INOUT)) //
				.end() //
				.repeat(-1, 0.0f);

		switch (mode) {
		case EASY:
			// 出現後、上下にゆれる
			main = Timeline.createSequence() //
					.push(Tween.set(dragonGame, GameObjectAccessor.MOVE_SIZE).target(7.0f, 9.0f, 7.0f, 10.5f)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE, 1.0f).target(7.0f, 5.0f, 7.0f, 10.5f).ease(Sine.INOUT)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE, 1.0f).target(7.0f, 15.0f, 7.0f, 10.5f).ease(Sine.INOUT)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE, 1.0f).target(7.0f, 9.0f, 7.0f, 10.5f).ease(Sine.INOUT)) //
					.repeat(-1, 0.0f); //
			break;
		case NORMAL:
			// 出現後、左右にゆれる
			main = Timeline.createSequence() //
					.push(Tween.set(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA).target(5.0f, 9.0f, 7.0f, 10.5f, 1.0f)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA, 0.7f).target(0.0f, 9.0f, 7.0f, 10.5f, 1.0f).ease(Sine.INOUT)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA, 0.7f).target(10.0f, 9.0f, 7.0f, 10.5f, 1.0f).ease(Sine.INOUT)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA, 0.7f).target(5.0f, 9.0f, 7.0f, 10.5f, 1.0f).ease(Sine.INOUT)) //
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
}
