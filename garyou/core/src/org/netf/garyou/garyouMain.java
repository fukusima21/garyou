package org.netf.garyou;

import org.netf.garyou.game.Assets;
import org.netf.garyou.screens.MenuScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class garyouMain extends Game {

	public WebRtcResolver webRtcResolver;

	public garyouMain() {
		webRtcResolver = null;
	}

	public garyouMain(WebRtcResolver webRtcResolver) {
		this.webRtcResolver = webRtcResolver;
	}

	@Override
	public void create() {
		Assets.instance.init(new AssetManager());
		setScreen(new MenuScreen(this));
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
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
