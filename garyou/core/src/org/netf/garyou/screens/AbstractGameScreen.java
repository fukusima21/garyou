package org.netf.garyou.screens;

import org.netf.garyou.game.Assets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public abstract class AbstractGameScreen implements Screen, InputProcessor {

	protected Game game;

	public AbstractGameScreen(Game game) {
		this.game = game;
	}

	public abstract void render(float deltaTime);

	public void resize(int width, int height) {
	}

	public abstract void show();

	public abstract void hide();

	public void resume() {
		Assets.instance.init(new AssetManager());
	}

	@Override
	public void pause() {
	}

	public void dispose() {
		Assets.instance.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

}
