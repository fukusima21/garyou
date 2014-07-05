package org.netf.garyou.game;

import org.netf.garyou.accessors.GameObjectAccessor;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Cubic;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.equations.Sine;

public class FinalTimelines {

	private FinalController controller;

	public FinalTimelines(FinalController controller) {
		this.controller = controller;
	}

	public Timeline createReady() {
		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.dragonGame, GameObjectAccessor.ROTATE).target(0.0f)) //
				.push(Tween.set(controller.dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA).target(5.0f, -30.0f, 20.0f, 30.0f, 0.1f)) //
				.push(Tween.set(controller.moon, GameObjectAccessor.MOVE_SIZE).target(7.0f, 11.5f, 4.0f, 4.0f)) //
				.push(Tween.set(controller.guard, GameObjectAccessor.ROTATE).target(0.0f)) //
				.push(Tween.set(controller.grass1, GameObjectAccessor.MOVE).target(15.0f, 1.25f)) //
				.push(Tween.set(controller.grass3, GameObjectAccessor.MOVE).target(15.0f, 1.25f)) //

				.beginParallel() //
				.push(Tween.to(controller.dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA, 1.0f).target(5.0f, 7.5f, 20.0f, 30.0f, 0.5f).ease(Sine.INOUT)) //
				.push(Tween.to(controller.dragonGame, GameObjectAccessor.MOVE_SIZE_ALPHA, 1.0f).target(7.0f, 9.0f, 7.0f, 10.5f, 1.0f).ease(Sine.INOUT)) //
				.push(Tween.to(controller.moon, GameObjectAccessor.MOVE_SIZE, 0.5f).target(7.3f, 12.4f, 0.5f, 0.5f).ease(Quad.IN)) //
				.push(Tween.to(controller.guard, GameObjectAccessor.ROTATE, 1.0f).target(360.0f).ease(Sine.INOUT)) //
				.push(Tween.to(controller.grass1, GameObjectAccessor.MOVE, 0.5f).target(5.0f, 1.25f).ease(Cubic.IN)) //
				.push(Tween.to(controller.grass3, GameObjectAccessor.MOVE, 0.5f).target(5.0f, 1.25f).ease(Cubic.IN)) //
				.end() //

				.push(Tween.set(controller.player, GameObjectAccessor.MOVE_SIZE_ALPHA).target(-1.25f, 1.5f, 1.5f, 2.0f, 0.3f)) //
				.push(Tween.to(controller.player, GameObjectAccessor.MOVE_SIZE_ALPHA, 1.0f).target(1.5f, 1.5f, 1.5f, 2.0f, 0.8f).ease(Sine.INOUT)) //
				.repeat(0, 0.0f); //

		return timeline;
	}

	public Timeline createMain() {

		// 出現後、上下にゆれる
		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.eye, GameObjectAccessor.COLOR).target(1.0f, 0.0f, 0.0f, 1.0f)) //
				.push(Tween.set(controller.dragonGame, GameObjectAccessor.ROTATE).target(0.0f)) //
				.push(Tween.set(controller.dragonGame, GameObjectAccessor.MOVE_SIZE).target(7.0f, 9.0f, 7.0f, 10.5f)) //
				.push(Tween.to(controller.dragonGame, GameObjectAccessor.MOVE_SIZE, 1.0f).target(7.0f, 5.0f, 7.0f, 10.5f).ease(Quad.OUT)) //
				.push(Tween.to(controller.dragonGame, GameObjectAccessor.MOVE_SIZE, 1.0f).target(7.0f, 15.0f, 7.0f, 10.5f).ease(Quad.INOUT)) //
				.push(Tween.to(controller.dragonGame, GameObjectAccessor.MOVE_SIZE, 1.0f).target(7.0f, 9.0f, 7.0f, 10.5f).ease(Quad.OUT)) //
				.repeat(-1, 0.0f); //

		return timeline;
	}

	public Timeline createGuardT() {

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.guard, GameObjectAccessor.ROTATE).target(0.0f)) //
				.push(Tween.to(controller.guard, GameObjectAccessor.ROTATE, 1.5f).target(360.0f).ease(Sine.INOUT)) //
				.repeat(-1, 0.0f); //

		return timeline;

	}

	public Timeline createForeground1() {

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.grass1, GameObjectAccessor.MOVE).target(5.0f, 1.25f)) //
				.push(Tween.set(controller.grass2, GameObjectAccessor.MOVE).target(15.0f, 1.25f)) //
				.beginParallel() //
				.push(Tween.to(controller.grass1, GameObjectAccessor.MOVE, 1.0f).target(-5.0f, 1.25f).ease(Linear.INOUT)) //
				.push(Tween.to(controller.grass2, GameObjectAccessor.MOVE, 1.0f).target(5.0f, 1.25f).ease(Linear.INOUT)) //
				.end() //
				.repeat(-1, 0.0f);

		return timeline;
	}

	public Timeline createForeground2() {

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.grass3, GameObjectAccessor.MOVE).target(5.0f, 1.25f)) //
				.push(Tween.set(controller.grass4, GameObjectAccessor.MOVE).target(15.0f, 1.25f)) //
				.beginParallel() //
				.push(Tween.to(controller.grass3, GameObjectAccessor.MOVE, 2.0f).target(-5.0f, 1.25f).ease(Linear.INOUT)) //
				.push(Tween.to(controller.grass4, GameObjectAccessor.MOVE, 2.0f).target(5.0f, 1.25f).ease(Linear.INOUT)) //
				.end() //
				.repeat(-1, 0.0f);

		return timeline;

	}

	public Timeline createFire(float x1, float y1, float x2, float y2) {

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.bullet, GameObjectAccessor.MOVE).target(x1, y1)) //
				.push(Tween.to(controller.bullet, GameObjectAccessor.MOVE, 1.0f).target(x2, y2).ease(Quad.OUT)) //
				.repeat(0, 0.0f); //

		return timeline;
	}

	public Timeline createNotClear1() {

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.dragonGame, GameObjectAccessor.ALPHA).target(1.0f)) //
				.push(Tween.set(controller.eye, GameObjectAccessor.ALPHA).target(1.0f)) //
				.push(Tween.set(controller.player, GameObjectAccessor.MOVE_SIZE_ALPHA).target(1.5f, 1.5f, 1.5f, 2.0f, 0.8f)) //
				.beginParallel() //
				.push(Tween.to(controller.dragonGame, GameObjectAccessor.ALPHA, 2.0f).target(0.3f).ease(Linear.INOUT)) //
				.push(Tween.to(controller.eye, GameObjectAccessor.ALPHA, 2.0f).target(0.3f).ease(Linear.INOUT)) //
				.push(Tween.to(controller.player, GameObjectAccessor.MOVE_SIZE_ALPHA, 2.0f).target(11.5f, 1.5f, 1.5f, 2.0f, 0.8f).ease(Linear.INOUT)) //
				.end() //
				.repeat(0, 0.0f).delay(0.2f);

		return timeline;
	}

	public Timeline createNotClear2() {

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.whiteBoard, GameObjectAccessor.MOVE).target(15.0f, 7.5f)) //
				.push(Tween.to(controller.whiteBoard, GameObjectAccessor.MOVE, 0.5f).target(5.0f, 7.5f).ease(Quad.IN)) //
				.push(Tween.set(controller.player, GameObjectAccessor.MOVE_SIZE).target(5.0f, 5.0f, 1.5f, 2.0f)) //
				.push(Tween.set(controller.notClearMessage, GameObjectAccessor.MOVE_SIZE).target(5.0f, 8.0f, 2.0f, 1.0f)) //
				.push(Tween.to(controller.player, GameObjectAccessor.MOVE_SIZE, 0.5f).target(5.0f, 5.0f, 1.5f, 2.0f).ease(Quad.IN)) //
				.push(Tween.to(controller.notClearMessage, GameObjectAccessor.MOVE_SIZE, 0.5f).target(5.0f, 9.5f, 7.0f, 3.5f).ease(Quad.IN)) //
				.push(Tween.set(controller.menu, GameObjectAccessor.MOVE_SIZE).target(2.0f, 6.5f, 1.0f, 1.0f)) //
				.push(Tween.set(controller.retry, GameObjectAccessor.MOVE_SIZE).target(7.75f, 6.5f, 1.0f, 1.0f)) //
				.beginParallel() //
				.push(Tween.to(controller.menu, GameObjectAccessor.MOVE_SIZE, 0.5f).target(2.0f, 6.5f, 3.5f, 1.75f).ease(Quad.IN)) //
				.push(Tween.to(controller.retry, GameObjectAccessor.MOVE_SIZE, 0.5f).target(7.75f, 6.5f, 4.0f, 1.75f).ease(Quad.IN)) //
				.end().repeat(0, 0.0f); //

		return timeline;
	}

	public Timeline createClear1(float x1, float y1, float a1) {

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.eye, GameObjectAccessor.COLOR).target(1.0f, 0.0f, 0.0f, 1.0f)) //
				.push(Tween.set(controller.dragonGame, GameObjectAccessor.MOVE_ALPHA).target(x1, y1, a1)) //
				.push(Tween.set(controller.player, GameObjectAccessor.MOVE_SIZE_ALPHA).target(1.5f, 1.5f, 1.5f, 2.0f, 0.8f)) //
				.beginParallel() //
				.push(Tween.to(controller.eye, GameObjectAccessor.COLOR, 0.5f).target(0.0f, 0.0f, 0.0f, 0.6f).ease(Quad.IN)) //
				.push(Tween.to(controller.dragonGame, GameObjectAccessor.MOVE_ALPHA, 1.5f).target(x1, 15.0f + 5.25f, a1).ease(Back.INOUT)) //
				.push(Tween.to(controller.player, GameObjectAccessor.MOVE_SIZE_ALPHA, 2.0f).target(11.5f, 1.5f, 1.5f, 2.0f, 0.8f).ease(Linear.INOUT)) //
				.end() //
				.repeat(0, 0.0f); //

		return timeline;
	}

	public Timeline createCameraT() {

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.center, GameObjectAccessor.MOVE).target(5.0f, 7.3f)) //
				.push(Tween.to(controller.center, GameObjectAccessor.MOVE, 0.1f).target(5.0f, 7.7f).ease(Bounce.INOUT)) //
				.repeatYoyo(30, 0.0f) //
		; //

		return timeline;

	}

	public Timeline createClear2() {

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.whiteBoard, GameObjectAccessor.MOVE).target(15.0f, 7.5f)) //
				.push(Tween.set(controller.kan, GameObjectAccessor.MOVE_SIZE_ALPHA).target(5.0f, 7.5f, 8.0f, 8.0f, 0.3f)) //
				.push(Tween.to(controller.whiteBoard, GameObjectAccessor.MOVE, 0.5f).target(5.0f, 7.5f).ease(Quad.IN)) //
				.push(Tween.to(controller.kan, GameObjectAccessor.MOVE_SIZE_ALPHA, 0.5f).target(5.0f, 7.5f, 2.0f, 2.0f, 1.0f).ease(Quad.IN)) //
				.delay(1.0f).repeat(0, 0.0f); //

		return timeline;
	}

}
