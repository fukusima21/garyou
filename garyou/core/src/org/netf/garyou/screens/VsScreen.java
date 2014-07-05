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

	private VsRenderer vsRenderer;
	private VsController vsController;
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

		vsController.update(deltaTime);

		Gdx.gl.glClearColor(0xe7 / 255.0f, 0xe3 / 255.0f, 0xc8 / 255.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		vsRenderer.render();

	}

	@Override
	public void show() {
		vsController = new VsController(game.webRtcResolver);
		vsRenderer = new VsRenderer(vsController);
		if (game.webRtcResolver != null) {
			game.webRtcResolver.setWebRtcEvent(this);
		}
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void hide() {
		vsRenderer.dispose();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		if (vsController.getState() == STATE.MAIN) {
			touchPoint.set(screenX, screenY, 0);
			camera.unproject(touchPoint);
			vsController.onFire(touchPoint);
		}

		if (vsController.getState() == STATE.NOT_CLEAR3 //
				|| vsController.getState() == STATE.CLEAR3) {
			focus(screenX, screenY);
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		if (vsController.getState() == STATE.NOT_CLEAR3 //
				|| vsController.getState() == STATE.CLEAR3) {
			focus(screenX, screenY);
			if (vsController.retry.focused) {
				vsController.startRetry();
			}
		}

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		if (vsController.getState() == STATE.NOT_CLEAR3 //
				|| vsController.getState() == STATE.CLEAR3) {
			focus(screenX, screenY);
		}

		return true;
	}

	private void focus(int screenX, int screenY) {

		if (vsController.getState() == STATE.NOT_CLEAR3 //
				|| vsController.getState() == STATE.CLEAR3) {

			touchPoint.set(screenX, screenY, 0);
			camera.unproject(touchPoint);

			if (vsController.retry.isHit(touchPoint.x, touchPoint.y)) {
				vsController.retry.focused = true;
			} else {
				vsController.retry.focused = false;
			}

		}

	}

	@Override
	public void onRecv(String s) {

		if (s == null || s.length() == 0) {
			return;
		}

		if (vsController.getState() == STATE.WAIT) {
			if (s.startsWith("start:")) {
				String[] split = s.split(":");
				int stage = Integer.valueOf(split[1]);
				vsController.startSync(stage);
			}
		}

		if (s.startsWith("fire:")) {
			String[] split = s.split(":");
			float time = Float.valueOf(split[1]);
			float rad = Float.valueOf(split[2]);
			vsController.startPlayer2Fire(time, rad);
		}

		if (s.startsWith("hit:")) {
			String[] split = s.split(":");
			float time = Float.valueOf(split[1]);
			vsController.startPlayer2Hit(time);
		}

		if (s.startsWith("nohit")) {
			vsController.startPlayer2NoHit();
		}

		if (s.startsWith("retry:")) {
			String[] split = s.split(":");
			int stage = Integer.valueOf(split[1]);
			vsController.init(true, stage);
		}

	}
}
