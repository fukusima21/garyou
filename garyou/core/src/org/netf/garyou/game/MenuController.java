package org.netf.garyou.game;

import org.netf.garyou.accessors.GameObjectAccessor;
import org.netf.garyou.game.objects.base.GameObject;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Cubic;

import com.badlogic.gdx.Game;

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

	private Game game;

	private TweenManager tweenManager;

	public MenuController(Game game) {
		this.game = game;
		init();
	}

	private void init() {

		moon = new GameObject(Assets.instance.moon, 7.0f, 11.5f, 4.0f, 4.0f, 1.0f);
		ga = new GameObject(Assets.instance.ga, 2.0f, 1.5f, 2.0f, 2.0f, 0.0f);
		ryou = new GameObject(Assets.instance.ryou, 4.0f, 1.5f, 2.0f, 2.0f, 0.0f);
		ten = new GameObject(Assets.instance.ten, 6.0f, 1.5f, 2.0f, 2.0f, 0.0f);
		sei = new GameObject(Assets.instance.sei, 8.0f, 1.5f, 2.0f, 2.0f, 0.0f);
		dragon = new GameObject(Assets.instance.dragonTitle, 5.0f, 5.75f, 10.0f, 7.5f, 0.0f);

		easy = new GameObject(Assets.instance.easy, 2.5f, 10.0f, 4.0f, 4.0f, 0.0f);
		normal = new GameObject(Assets.instance.normal, 5.0f, 13.5f, 4.0f, 4.0f, 0.0f);
		hard = new GameObject(Assets.instance.hard, 7.5f, 10.0f, 4.0f, 4.0f, 0.0f);

		circle = new GameObject(Assets.instance.circle, 2.0f, 2.0f, 4.0f, 4.0f, 0.5f);

		tweenManager = new TweenManager();

		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());

		Timeline.createSequence() //
				.push(Tween.set(ga, GameObjectAccessor.SIZE_ALPHA).target(16.0f, 16.0f, 0.3f)) //
				.push(Tween.set(ryou, GameObjectAccessor.SIZE_ALPHA).target(16.0f, 16.0f, 0.3f)) //
				.push(Tween.set(ten, GameObjectAccessor.SIZE_ALPHA).target(16.0f, 16.0f, 0.3f)) //
				.push(Tween.set(sei, GameObjectAccessor.SIZE_ALPHA).target(16.0f, 16.0f, 0.3f)) //
				.push(Tween.set(dragon, GameObjectAccessor.ALPHA).target(0.0f)) //
				.push(Tween.set(easy, GameObjectAccessor.SIZE_ALPHA).target(8.0f, 8.0f, 0.0f)) //
				.push(Tween.set(normal, GameObjectAccessor.SIZE_ALPHA).target(8.0f, 8.0f, 0.0f)) //
				.push(Tween.set(hard, GameObjectAccessor.SIZE_ALPHA).target(8.0f, 8.0f, 0.0f)) //
				.push(Tween.to(ga, GameObjectAccessor.SIZE_ALPHA, 0.1f).target(2.0f, 2.0f, 1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(ryou, GameObjectAccessor.SIZE_ALPHA, 0.1f).target(2.0f, 2.0f, 1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(ten, GameObjectAccessor.SIZE_ALPHA, 0.1f).target(2.0f, 2.0f, 1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(sei, GameObjectAccessor.SIZE_ALPHA, 0.1f).target(2.0f, 2.0f, 1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(dragon, GameObjectAccessor.ALPHA, 3.5f).target(1.0f).ease(Cubic.INOUT)) //
				.push(Tween.to(easy, GameObjectAccessor.SIZE_ALPHA, 0.2f).target(4.0f, 4.0f, 0.8f).ease(Cubic.INOUT)) //
				.push(Tween.to(normal, GameObjectAccessor.SIZE_ALPHA, 0.2f).target(4.0f, 4.0f, 0.8f).ease(Cubic.INOUT)) //
				.push(Tween.to(hard, GameObjectAccessor.SIZE_ALPHA, 0.2f).target(4.0f, 4.0f, 0.8f).ease(Cubic.INOUT)) //
				.delay(0.5f).repeat(-1, 60.0f) //
				.start(tweenManager);

	}

	public void update(float deltaTime) {
		tweenManager.update(deltaTime);
	}
}
