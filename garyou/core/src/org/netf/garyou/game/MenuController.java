package org.netf.garyou.game;

import org.netf.garyou.accessors.GameObjectAccessor;
import org.netf.garyou.game.objects.base.GameObject;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Cubic;

public class MenuController {

	public GameObject moon;
	public GameObject dragon;
	public GameObject ga;
	public GameObject ryou;
	public GameObject ten;
	public GameObject sei;
	public GameObject easy;
	public GameObject normal;
	public GameObject hard;
	public GameObject circle;
	public GameObject logo5jcup;

	private TweenManager tweenManager;

	public static enum STATE {
		READY, MAIN
	}

	public STATE state;

	public MenuController() {
		init();
	}

	private void init() {

		state = STATE.READY;

		moon = new GameObject(Assets.instance.moon, 7.0f, 11.5f, 4.0f, 4.0f);
		ga = new GameObject(Assets.instance.ga, 2.0f, 1.5f, 2.0f, 2.0f, 0.0f);
		ryou = new GameObject(Assets.instance.ryou, 4.0f, 1.5f, 2.0f, 2.0f, 0.0f);
		ten = new GameObject(Assets.instance.ten, 6.0f, 1.5f, 2.0f, 2.0f, 0.0f);
		sei = new GameObject(Assets.instance.sei, 8.0f, 1.5f, 2.0f, 2.0f, 0.0f);
		dragon = new GameObject(Assets.instance.dragonTitle, 5.0f, 7.5f, 5.0f, 7.5f, 0.0f);

		easy = new GameObject(Assets.instance.easy, 2.5f, 10.0f, 4.0f, 4.0f, 0.0f);
		normal = new GameObject(Assets.instance.normal, 5.0f, 13.5f, 4.0f, 4.0f, 0.0f);
		hard = new GameObject(Assets.instance.hard, 7.5f, 10.0f, 4.0f, 4.0f, 0.0f);

		circle = new GameObject(Assets.instance.circle, 2.0f, 2.0f, 4.0f, 4.0f, 0.5f);

		logo5jcup = new GameObject(Assets.instance.logo5jcup, 1.5f, 16.5f, 3.0f, 3.0f);

		tweenManager = new TweenManager();

		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
		Tween.setCombinedAttributesLimit(5);

		Timeline ready = Timeline.createSequence() //
				.push(Tween.set(ga, GameObjectAccessor.SIZE_ALPHA).target(16.0f, 16.0f, 0.3f)) //
				.push(Tween.set(ryou, GameObjectAccessor.SIZE_ALPHA).target(16.0f, 16.0f, 0.3f)) //
				.push(Tween.set(ten, GameObjectAccessor.SIZE_ALPHA).target(16.0f, 16.0f, 0.3f)) //
				.push(Tween.set(sei, GameObjectAccessor.SIZE_ALPHA).target(16.0f, 16.0f, 0.3f)) //
				.push(Tween.set(dragon, GameObjectAccessor.MOVE_SIZE_ALPHA).target(5.0f, -6.0f, 7.5f, 11.25f, 0.0f)) //
				.push(Tween.set(easy, GameObjectAccessor.SIZE_ALPHA).target(8.0f, 8.0f, 0.0f)) //
				.push(Tween.set(normal, GameObjectAccessor.SIZE_ALPHA).target(8.0f, 8.0f, 0.0f)) //
				.push(Tween.set(hard, GameObjectAccessor.SIZE_ALPHA).target(8.0f, 8.0f, 0.0f)) //
				.push(Tween.set(logo5jcup, GameObjectAccessor.MOVE).target(1.5f, 16.5f)) //
				.push(Tween.to(ga, GameObjectAccessor.SIZE_ALPHA, 0.1f).target(2.0f, 2.0f, 1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(ryou, GameObjectAccessor.SIZE_ALPHA, 0.1f).target(2.0f, 2.0f, 1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(ten, GameObjectAccessor.SIZE_ALPHA, 0.1f).target(2.0f, 2.0f, 1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(sei, GameObjectAccessor.SIZE_ALPHA, 0.1f).target(2.0f, 2.0f, 1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(dragon, GameObjectAccessor.MOVE_SIZE_ALPHA, 1.0f).target(7.0f, 7.5f, 7.5f, 11.25f, 0.5f).ease(Cubic.INOUT)) //
				.push(Tween.to(easy, GameObjectAccessor.SIZE_ALPHA, 0.2f).target(4.0f, 4.0f, 0.8f).ease(Cubic.INOUT)) //
				.push(Tween.to(normal, GameObjectAccessor.SIZE_ALPHA, 0.2f).target(4.0f, 4.0f, 0.8f).ease(Cubic.INOUT)) //
				.push(Tween.to(hard, GameObjectAccessor.SIZE_ALPHA, 0.2f).target(4.0f, 4.0f, 0.8f).ease(Cubic.INOUT)) //
				.push(Tween.to(logo5jcup, GameObjectAccessor.MOVE, 0.5f).target(1.5f, 13.5f).ease(Bounce.OUT)) //
				.delay(0.5f).repeat(-1, 60.0f) //
				.start(tweenManager);

		ready.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				if (type == TweenCallback.START) {
					state = STATE.READY;
				} else if (type == TweenCallback.END) {
					state = STATE.MAIN;
				}
			}
		}).setCallbackTriggers(TweenCallback.END | TweenCallback.START);

	}

	public void update(float deltaTime) {
		tweenManager.update(deltaTime);
	}
}
