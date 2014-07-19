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
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;

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
	public GameObject bullet;

	public GameObject asobikata;
	public GameObject asobikataBtn;

	public GameObject taiketsu;

	private TweenManager tweenManager;

	private Timeline bulletT;

	public static enum STATE {
		READY, MAIN, READY_A, MAIN_A
	}

	private STATE state;

	public STATE getState() {
		return state;
	}

	public MenuController() {
		init();
	}

	private void init() {

		state = STATE.READY;

		moon = new GameObject(Assets.instance.moon, 7.0f, 11.5f, 4.0f, 4.0f);
		ga = new GameObject(Assets.instance.ga, 2.0f, 1.25f, 2.0f, 2.0f, 0.0f);
		ryou = new GameObject(Assets.instance.ryou, 4.0f, 1.25f, 2.0f, 2.0f, 0.0f);
		ten = new GameObject(Assets.instance.ten, 6.0f, 1.25f, 2.0f, 2.0f, 0.0f);
		sei = new GameObject(Assets.instance.sei, 8.0f, 1.25f, 2.0f, 2.0f, 0.0f);
		dragon = new GameObject(Assets.instance.dragonTitle, 5.0f, 7.5f, 5.0f, 7.5f, 0.0f);

		easy = new GameObject(Assets.instance.easy, 2.5f, 10.0f, 4.0f, 4.0f, 0.0f);
		normal = new GameObject(Assets.instance.normal, 5.0f, 13.5f, 4.0f, 4.0f, 0.0f);
		hard = new GameObject(Assets.instance.hard, 7.5f, 10.0f, 4.0f, 4.0f, 0.0f);
		taiketsu = new GameObject(Assets.instance.taiketsu, 5.0f, 6.5f, 4.0f, 4.0f, 0.0f);

		circle = new GameObject(Assets.instance.circle, 2.0f, 2.0f, 4.0f, 4.0f, 0.5f);

		asobikata = new GameObject(Assets.instance.asobikata, 5.0f, 19.5f, 6.0f, 9.0f, 1.0f);
		asobikataBtn = new GameObject(Assets.instance.asobikataBtn, 8.0f, 3.0f, 3.0f, 1.5f);
		bullet = new GameObject(Assets.instance.bullet, 2.0f + 1.25f, 3.0f + 3.25f, 0.5f, 0.5f, 0.5f);

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
				.push(Tween.set(taiketsu, GameObjectAccessor.SIZE_ALPHA).target(8.0f, 8.0f, 0.0f)) //
				.push(Tween.to(ga, GameObjectAccessor.SIZE_ALPHA, 0.1f).target(2.0f, 2.0f, 1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(ryou, GameObjectAccessor.SIZE_ALPHA, 0.1f).target(2.0f, 2.0f, 1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(ten, GameObjectAccessor.SIZE_ALPHA, 0.1f).target(2.0f, 2.0f, 1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(sei, GameObjectAccessor.SIZE_ALPHA, 0.1f).target(2.0f, 2.0f, 1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(dragon, GameObjectAccessor.MOVE_SIZE_ALPHA, 1.0f).target(6.5f, 7.5f, 7.5f, 11.25f, 0.3f).ease(Cubic.INOUT)) //
				.push(Tween.to(easy, GameObjectAccessor.SIZE_ALPHA, 0.2f).target(4.0f, 4.0f, 1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(normal, GameObjectAccessor.SIZE_ALPHA, 0.2f).target(4.0f, 4.0f, 1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(hard, GameObjectAccessor.SIZE_ALPHA, 0.2f).target(4.0f, 4.0f, 1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(taiketsu, GameObjectAccessor.SIZE_ALPHA, 0.2f).target(4.0f, 2.0f, 1.0f).ease(Cubic.INOUT)) //
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

	public void startAsobikata() {

		state = STATE.READY_A;

		bulletT = Timeline.createSequence() //
				.push(Tween.set(bullet, GameObjectAccessor.MOVE_ALPHA).target(2.0f + 1.25f, 3.0f + 3.25f, 0.8f)) //
				.push(Tween.to(bullet, GameObjectAccessor.MOVE_ALPHA, 1.0f).target(2.0f + 1.25f + 1.9f, 3.0f + 3.25f + 2.9f, 0.8f).ease(Linear.INOUT)) //
				.push(Tween.to(bullet, GameObjectAccessor.ALPHA, 0.5f).target(0.0f).ease(Linear.INOUT)) //
				.delay(0.5f).repeat(-1, 2.0f) //
				.start(tweenManager);

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(asobikata, GameObjectAccessor.MOVE).target(5.0f, 19.5f)) //
				.push(Tween.to(asobikata, GameObjectAccessor.MOVE, 0.5f).target(5.0f, 7.5f).ease(Quad.OUT)) //
				.repeat(0, 0) //
				.start(tweenManager);

		timeline.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				state = STATE.MAIN_A;
				bulletT.start(tweenManager);
			}
		}).setCallbackTriggers(TweenCallback.END);

	}

	public void backMenu() {
		state = STATE.MAIN;
		bulletT.kill();
	}
}
