package org.netf.garyou.game;

import org.netf.garyou.accessors.GameObjectAccessor;
import org.netf.garyou.game.objects.base.GameObject;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Cubic;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.equations.Sine;

public class VsTimelines {

	private VsController controller;

	public VsTimelines(VsController controller) {
		this.controller = controller;
	}

	public Timeline createReady() {
		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.moon, GameObjectAccessor.MOVE_SIZE).target(7.0f, 11.5f, 4.0f, 4.0f)) //
				.push(Tween.set(controller.grass1, GameObjectAccessor.MOVE).target(15.0f, 1.25f)) //
				.push(Tween.set(controller.grass3, GameObjectAccessor.MOVE).target(15.0f, 1.25f)) //

				.beginParallel() //
				.push(Tween.to(controller.moon, GameObjectAccessor.MOVE_SIZE, 0.5f).target(7.3f, 12.4f, 0.5f, 0.5f).ease(Quad.IN)) //
				.push(Tween.to(controller.grass1, GameObjectAccessor.MOVE, 0.5f).target(5.0f, 1.25f).ease(Cubic.IN)) //
				.push(Tween.to(controller.grass3, GameObjectAccessor.MOVE, 0.5f).target(5.0f, 1.25f).ease(Cubic.IN)) //
				.end() //

				.push(Tween.set(controller.whiteBoard, GameObjectAccessor.MOVE).target(15.0f, 7.5f)) //
				.push(Tween.to(controller.whiteBoard, GameObjectAccessor.MOVE, 0.5f).target(5.0f, 7.5f).ease(Quad.IN)) //

				.push(Tween.set(controller.player1, GameObjectAccessor.MOVE_SIZE_ALPHA).target(-1.25f, 1.5f, 1.5f, 2.0f, 0.3f)) //
				.push(Tween.to(controller.player1, GameObjectAccessor.MOVE_SIZE_ALPHA, 1.0f).target(1.25f, 1.5f, 1.5f, 2.0f, 0.8f).ease(Sine.INOUT)) //
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

	public Timeline createPlayer2Fire(float x1, float y1, float x2, float y2) {

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.bulletPlayer2, GameObjectAccessor.MOVE).target(x1, y1)) //
				.push(Tween.to(controller.bulletPlayer2, GameObjectAccessor.MOVE, 1.0f).target(x2, y2).ease(Quad.OUT)) //
				.repeat(0, 0.0f); //

		return timeline;
	}

	public Timeline createNotClear1() {

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.dragonGame, GameObjectAccessor.ALPHA).target(1.0f)) //
				.push(Tween.set(controller.eye, GameObjectAccessor.ALPHA).target(1.0f)) //
				.push(Tween.set(controller.player1, GameObjectAccessor.MOVE_SIZE_ALPHA).target(1.25f, 1.5f, 1.5f, 2.0f, 0.8f)) //
				.beginParallel() //
				.push(Tween.to(controller.dragonGame, GameObjectAccessor.ALPHA, 2.0f).target(0.3f).ease(Linear.INOUT)) //
				.push(Tween.to(controller.eye, GameObjectAccessor.ALPHA, 2.0f).target(0.3f).ease(Linear.INOUT)) //
				.push(Tween.to(controller.player1, GameObjectAccessor.MOVE_SIZE_ALPHA, 2.0f).target(11.5f, 1.5f, 1.5f, 2.0f, 0.8f).ease(Linear.INOUT)) //
				.end() //
				.repeat(0, 0.0f).delay(0.2f);

		return timeline;
	}

	public Timeline createNotClear3(GameObject result, float w1, float h1, float w2, float h2) {

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.whiteBoard, GameObjectAccessor.MOVE).target(15.0f, 7.5f)) //
				.push(Tween.to(controller.whiteBoard, GameObjectAccessor.MOVE, 0.5f).target(5.0f, 7.5f).ease(Quad.IN)) //
				.push(Tween.set(controller.player1, GameObjectAccessor.MOVE_SIZE).target(5.0f, 5.0f, 1.5f, 2.0f)) //
				.push(Tween.to(controller.player1, GameObjectAccessor.MOVE_SIZE, 0.5f).target(5.0f, 5.0f, 1.5f, 2.0f).ease(Quad.IN)) //
				.beginParallel() //
				.push(Tween.set(controller.retry, GameObjectAccessor.MOVE_SIZE).target(7.75f, 6.5f, 1.0f, 1.0f)) //
				.push(Tween.set(result, GameObjectAccessor.MOVE_SIZE).target(5.0f, 8.0f, w1, h1)) //
				.push(Tween.to(controller.retry, GameObjectAccessor.MOVE_SIZE, 0.5f).target(7.75f, 6.5f, 4.0f, 1.75f).ease(Quad.IN)) //
				.push(Tween.to(result, GameObjectAccessor.MOVE_SIZE, 0.5f).target(5.0f, 9.5f, w2, h2).ease(Quad.IN)) //
				.end().repeat(0, 0.0f); //

		return timeline;
	}

	public Timeline createClear1(float x1, float y1) {

		Timeline timeline = Timeline.createSequence() //
				.beginParallel() //
				.push(Tween.set(controller.eye, GameObjectAccessor.COLOR).target(1.0f, 0.0f, 0.0f, 1.0f)) //
				.push(Tween.set(controller.dragonGame, GameObjectAccessor.MOVE).target(x1, y1)) //
				.push(Tween.set(controller.player1, GameObjectAccessor.MOVE_SIZE_ALPHA).target(1.25f, 1.5f, 1.5f, 2.0f, 0.8f)) //
				.push(Tween.to(controller.eye, GameObjectAccessor.COLOR, 0.5f).target(0.0f, 0.0f, 0.0f, 0.6f).ease(Quad.IN)) //
				.push(Tween.to(controller.dragonGame, GameObjectAccessor.MOVE, 1.5f).target(x1, 15.0f + 5.25f).ease(Back.INOUT)) //
				.push(Tween.to(controller.player1, GameObjectAccessor.MOVE_SIZE_ALPHA, 2.0f).target(11.5f, 1.5f, 1.5f, 2.0f, 0.8f).ease(Linear.INOUT)) //
				.end() //
				.repeat(0, 0.0f); //

		return timeline;
	}

	public Timeline createClear3(GameObject result, float w1, float h1, float w2, float h2) {

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.whiteBoard, GameObjectAccessor.MOVE).target(15.0f, 7.5f)) //
				.push(Tween.to(controller.whiteBoard, GameObjectAccessor.MOVE, 0.5f).target(5.0f, 7.5f).ease(Quad.IN)) //
				.push(Tween.set(controller.player1, GameObjectAccessor.MOVE_SIZE).target(5.0f, 5.0f, 1.5f, 2.0f)) //
				.push(Tween.to(controller.player1, GameObjectAccessor.MOVE_SIZE, 0.5f).target(5.0f, 5.0f, 1.5f, 2.0f).ease(Quad.IN)) //
				.beginParallel() //
				.push(Tween.set(controller.retry, GameObjectAccessor.MOVE_SIZE).target(7.75f, 6.5f, 1.0f, 1.0f)) //
				.push(Tween.set(result, GameObjectAccessor.MOVE_SIZE).target(5.0f, 8.0f, w1, h1)) //
				.push(Tween.to(controller.retry, GameObjectAccessor.MOVE_SIZE, 0.5f).target(7.75f, 6.5f, 4.0f, 1.75f).ease(Quad.IN)) //
				.push(Tween.to(result, GameObjectAccessor.MOVE_SIZE, 0.5f).target(5.0f, 9.5f, w2, h2).ease(Quad.IN)) //
				.end().repeat(0, 0.0f); //

		return timeline;
	}

	public Timeline createPlayer2Hit() {

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.player2, GameObjectAccessor.MOVE_SIZE_ALPHA).target(1.75f, 1.5f, 1.5f, 2.0f, 0.3f)) //
				.push(Tween.to(controller.player2, GameObjectAccessor.MOVE_SIZE_ALPHA, 2.0f).target(11.5f, 1.5f, 1.5f, 2.0f, 0.3f).ease(Linear.INOUT)) //
				.repeat(0, 0.0f); //

		return timeline;
	}

	public BaseTween<Timeline> createSync() {

		Timeline timeline = Timeline.createSequence() //
				.push(Tween.set(controller.three, GameObjectAccessor.MOVE_ALPHA).target(5.0f, 7.5f, 1.0f)) //
				.push(Tween.to(controller.three, GameObjectAccessor.MOVE_ALPHA, 1.0f).target(5.0f, 7.5f, 0.0f).ease(Linear.INOUT)) //
				.push(Tween.set(controller.two, GameObjectAccessor.MOVE_ALPHA).target(5.0f, 7.5f, 1.0f)) //
				.push(Tween.to(controller.two, GameObjectAccessor.MOVE_ALPHA, 1.0f).target(5.0f, 7.5f, 0.0f).ease(Linear.INOUT)) //
				.push(Tween.set(controller.one, GameObjectAccessor.MOVE_ALPHA).target(5.0f, 7.5f, 1.0f)) //
				.push(Tween.to(controller.one, GameObjectAccessor.MOVE_ALPHA, 1.0f).target(5.0f, 7.5f, 0.0f).ease(Linear.INOUT)) //
				.push(Tween.set(controller.start, GameObjectAccessor.MOVE_ALPHA).target(5.0f, 7.5f, 1.0f)) //
				.push(Tween.to(controller.start, GameObjectAccessor.MOVE_ALPHA, 1.0f).target(5.0f, 7.5f, 0.0f).ease(Linear.INOUT)) //
				.repeat(0, 0.0f); //

		return timeline;
	}

}
