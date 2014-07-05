package org.netf.garyou.screens;

import org.netf.garyou.WebRtcEvent;
import org.netf.garyou.garyouMain;
import org.netf.garyou.game.VsController;
import org.netf.garyou.game.VsController.STATE;
import org.netf.garyou.game.VsRenderer;
import org.netf.garyou.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class VsScreen extends AbstractGameScreen implements WebRtcEvent {

	private static final String TAG = MenuScreen.class.getName();

	private VsRenderer finalRenderer;
	private VsController finalController;
	private OrthographicCamera camera;
	private Vector3 touchPoint;

	public VsScreen(garyouMain game) {
		super(game);

		touchPoint = new Vector3();

		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2, 0);
		camera.update();
	}

	@Override
	public void render(float deltaTime) {

		finalController.update(deltaTime);

		Gdx.gl.glClearColor(0xe7 / 255.0f, 0xe3 / 255.0f, 0xc8 / 255.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		finalRenderer.render();

	}

	@Override
	public void show() {
		finalController = new VsController(game.webRtcResolver);
		finalRenderer = new VsRenderer(finalController);
		if (game.webRtcResolver != null) {
			game.webRtcResolver.setWebRtcEvent(this);
		}
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void hide() {
		finalRenderer.dispose();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		if (finalController.getState() == STATE.MAIN) {
			touchPoint.set(screenX, screenY, 0);
			camera.unproject(touchPoint);
			finalController.onFire(touchPoint);
		}

		if (finalController.getState() == STATE.NOT_CLEAR3 //
				|| finalController.getState() == STATE.CLEAR3) {
			focus(screenX, screenY);
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		if (finalController.getState() == STATE.NOT_CLEAR3 //
				|| finalController.getState() == STATE.CLEAR3) {
			focus(screenX, screenY);
			if (finalController.retry.focused) {
				finalController.startRetry();
			}
		}

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		if (finalController.getState() == STATE.NOT_CLEAR3 //
				|| finalController.getState() == STATE.CLEAR3) {
			focus(screenX, screenY);
		}

		return true;
	}

	private void focus(int screenX, int screenY) {

		if (finalController.getState() == STATE.NOT_CLEAR3 //
				|| finalController.getState() == STATE.CLEAR3) {

			touchPoint.set(screenX, screenY, 0);
			camera.unproject(touchPoint);

			if (finalController.retry.isHit(touchPoint.x, touchPoint.y)) {
				finalController.retry.focused = true;
			} else {
				finalController.retry.focused = false;
			}

		}

	}

	@Override
	public void onRecv(String s) {

		if (s == null || s.length() == 0) {
			return;
		}

		if (s.equals("start")) {
			finalController.startSync();
		}

		if (s.startsWith("fire:")) {
			String[] split = s.split(":");
			float time = Float.valueOf(split[1]);
			float rad = Float.valueOf(split[2]);
			finalController.startPlayer2Fire(time, rad);
		}

		if (s.startsWith("hit:")) {
			String[] split = s.split(":");
			float time = Float.valueOf(split[1]);
			finalController.startPlayer2Hit(time);
		}

		if (s.startsWith("nohit")) {
			finalController.startPlayer2NoHit();
		}

		if (s.equals("retry")) {
			finalController.init(true);
		}

	}
}
