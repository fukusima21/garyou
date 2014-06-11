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
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.equations.Sine;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

public class GameController extends InputAdapter {

	public Vector3 pos;

	private TweenManager tweenManager;

	public GameObject dragonGame;
	public GameObject moon;
	public GameObject back;
	public float timer;

	public static enum STATE {
		READY, MAIN, FINISH
	}

	public STATE state;

	private Timeline ready;
	private Timeline main;
	private Timeline finish;

	public MODE mode;

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
			back = new GameObject(Assets.instance.back1, 15.0f, 7.5f, 10.0f, 15.0f, 1.0f);
			break;
		case NORMAL:
			back = new GameObject(Assets.instance.back2, 15.0f, 7.5f, 10.0f, 15.0f, 1.0f);
			break;
		case HARD:
			back = new GameObject(Assets.instance.back3, 15.0f, 7.5f, 10.0f, 15.0f, 1.0f);
			break;
		}

		tweenManager = new TweenManager();

		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
		Tween.setCombinedAttributesLimit(5);

		ready = Timeline.createSequence() //
				.push(Tween.set(back, GameObjectAccessor.MOVE).target(15.0f, 7.5f)) //
				.push(Tween.set(moon, GameObjectAccessor.MOVE_SIZE).target(7.0f, 11.5f, 4.0f, 4.0f)) //
				.beginParallel() //
				.push(Tween.to(back, GameObjectAccessor.MOVE, 0.5f).target(5.0f, 7.5f).ease(Cubic.IN)) //
				.push(Tween.to(moon, GameObjectAccessor.MOVE_SIZE, 0.5f).target(7.3f, 12.4f, 0.5f, 0.5f).ease(Quad.IN)) //
				.end() //
				.push(Tween.set(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA).target(5.0f, -30.0f, 20.0f, 30.0f, 0.5f)) //
				.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA, 1.0f).target(5.0f, 7.5f, 20.0f, 30.0f, 0.8f).ease(Sine.INOUT)) //
				.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA, 1.0f).target(5.0f, 7.5f, 10.0f, 15.0f, 1.0f).ease(Sine.INOUT)) //
				.repeat(0, 0.0f); //

		createTimeline();

		ready.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				state = STATE.MAIN;
				timer = 30.0f;

				if (main != null) {
					main.start(tweenManager);
				}

			}
		}).setCallbackTriggers(TweenCallback.END);

		ready.start(tweenManager);

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

	}

	private Timeline createTimeline() {

		switch (mode) {
		case EASY:
			// 出現後、上下にゆれる
			main = Timeline.createSequence() //
					.push(Tween.set(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA).target(5.0f, 7.5f, 10.0f, 15.0f, 1.0f)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA, 1.0f).target(5.0f, 2.5f, 10.0f, 15.0f, 1.0f).ease(Sine.INOUT)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA, 1.0f).target(5.0f, 12.5f, 10.0f, 15.0f, 1.0f).ease(Sine.INOUT)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA, 1.0f).target(5.0f, 7.5f, 10.0f, 15.0f, 1.0f).ease(Sine.INOUT)) //
					.repeat(-1, 0.0f); //
			break;
		case NORMAL:
			// 出現後、左右にゆれる
			main = Timeline.createSequence() //
					.push(Tween.set(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA).target(5.0f, 7.5f, 10.0f, 15.0f, 1.0f)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA, 0.7f).target(0.0f, 7.5f, 10.0f, 15.0f, 1.0f).ease(Sine.INOUT)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA, 0.7f).target(10.0f, 7.5f, 10.0f, 15.0f, 1.0f).ease(Sine.INOUT)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA, 0.7f).target(5.0f, 7.5f, 10.0f, 15.0f, 1.0f).ease(Sine.INOUT)) //
					.repeat(-1, 0.0f); //
			break;
		case HARD:
			// 出現後、回転
			dragonGame.getSprite().setOrigin(5.0f, 7.5f);
			main = Timeline.createSequence() //
					.push(Tween.set(dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA).target(5.0f, 7.5f, 10.0f, 15.0f, 1.0f)) //
					.push(Tween.set(dragonGame, GameObjectAccessor.ROTATE).target(360.0f)) //
					.push(Tween.to(dragonGame, GameObjectAccessor.ROTATE, 2.0f).target(0.0f).ease(Sine.INOUT)) //
					.repeatYoyo(-1, 0.0f); //
			break;
		}

		return null;
	}
}
