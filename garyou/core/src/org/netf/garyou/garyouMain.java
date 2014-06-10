package org.netf.garyou;

import org.netf.garyou.game.Assets;
import org.netf.garyou.screens.MenuScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class garyouMain extends Game {

	@Override
	public void create() {
		Assets.instance.init(new AssetManager());
		setScreen(new MenuScreen(this));
	}

	@Override
	public void pause() {
		super.pause();
		Assets.instance.dispose();
	}

	@Override
	public void resume() {
		super.resume();
		Assets.instance.init(new AssetManager());
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		getScreen().dispose();
	}

}
