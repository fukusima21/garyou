package org.netf.garyou.game;

import org.netf.garyou.game.MenuController.STATE;
import org.netf.garyou.util.Constants;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class MenuRenderer implements Disposable {

	private static final String TAG = MenuRenderer.class.getName();

	private MenuController menuController;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	public MenuRenderer(MenuController menuController) {
		this.menuController = menuController;
		init();
	}

	private void init() {
		batch = new SpriteBatch();

		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2, 0);

	}

	public void render() {

		camera.update();

		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		menuController.moon.getSprite().draw(batch);
		menuController.logo5jcup.getSprite().draw(batch);
		menuController.ga.getSprite().draw(batch);
		menuController.ryou.getSprite().draw(batch);
		menuController.ten.getSprite().draw(batch);
		menuController.sei.getSprite().draw(batch);
		menuController.dragon.getSprite().draw(batch);

		if (menuController.easy.focused) {
			Rectangle bounds = menuController.easy.getSprite().getBoundingRectangle();
			menuController.circle.getSprite().setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
			menuController.circle.getSprite().setAlpha(0.5f);
			menuController.circle.getSprite().draw(batch);
		}
		menuController.easy.getSprite().draw(batch);

		if (menuController.normal.focused) {
			Rectangle bounds = menuController.normal.getSprite().getBoundingRectangle();
			menuController.circle.getSprite().setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
			menuController.circle.getSprite().setAlpha(0.5f);
			menuController.circle.getSprite().draw(batch);
		}
		menuController.normal.getSprite().draw(batch);

		if (menuController.hard.focused) {
			Rectangle bounds = menuController.hard.getSprite().getBoundingRectangle();
			menuController.circle.getSprite().setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
			menuController.circle.getSprite().setAlpha(0.5f);
			menuController.circle.getSprite().draw(batch);
		}
		menuController.hard.getSprite().draw(batch);

		if (menuController.asobikataBtn.focused) {
			Rectangle bounds = menuController.asobikataBtn.getSprite().getBoundingRectangle();
			menuController.circle.getSprite().setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
			menuController.circle.getSprite().setAlpha(0.5f);
			menuController.circle.getSprite().draw(batch);
		}
		menuController.asobikataBtn.getSprite().draw(batch);

		if (menuController.taiketsu.focused) {
			Rectangle bounds = menuController.taiketsu.getSprite().getBoundingRectangle();
			menuController.circle.getSprite().setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
			menuController.circle.getSprite().setAlpha(0.5f);
			menuController.circle.getSprite().draw(batch);
		}
		menuController.taiketsu.getSprite().draw(batch);

		if (menuController.getState() == STATE.READY_A //
				|| menuController.getState() == STATE.MAIN_A) {
			menuController.asobikata.getSprite().draw(batch);
		}

		if (menuController.getState() == STATE.MAIN_A) {
			menuController.bullet.getSprite().draw(batch);
		}

		batch.end();

	}

	@Override
	public void dispose() {
		batch.dispose();
	}

}
