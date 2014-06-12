package org.netf.garyou.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	private AssetManager assetManager;

	public BitmapFont bitmapFont;
	public Texture background;
	public Sprite dragonTitle;
	public Sprite moon;
	public Sprite ga;
	public Sprite ryou;
	public Sprite ten;
	public Sprite sei;
	public Sprite easy;
	public Sprite normal;
	public Sprite hard;
	public Sprite dragonGame;

	public Sprite circle;
	private Texture circleTexture;

	public Sprite back1;
	private Texture back1Texture;

	public Sprite back2;
	private Texture back2Texture;

	public Sprite back3;
	private Texture back3Texture;

	private Assets() {
	}

	public void init(AssetManager assetManager) {

		this.assetManager = assetManager;

		assetManager.setErrorListener(this);

		assetManager.load("data/fonts/aoyagi.fnt", BitmapFont.class);
		assetManager.load("data/images/garyou.pack", TextureAtlas.class);

		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);

		for (String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);
		}

		bitmapFont = assetManager.get("data/fonts/aoyagi.fnt");

		TextureAtlas atlas = assetManager.get("data/images/garyou.pack");

		dragonTitle = atlas.createSprite("dragon_title");

		moon = atlas.createSprite("moon");
		ga = atlas.createSprite("ga");
		ryou = atlas.createSprite("ryou");
		ten = atlas.createSprite("ten");
		sei = atlas.createSprite("sei");

		easy = atlas.createSprite("easy");
		normal = atlas.createSprite("normal");
		hard = atlas.createSprite("hard");

		dragonGame = atlas.createSprite("dragon_game");

		Pixmap circlePixmap = new Pixmap(128, 128, Format.RGBA8888);
		circlePixmap.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		circlePixmap.fillCircle(63, 63, 63);
		circleTexture = new Texture(circlePixmap);
		circle = new Sprite(circleTexture);
		circlePixmap.dispose();

		Pixmap back1Pixmap = new Pixmap(16, 16, Format.RGB565);
		back1Pixmap.setColor(0x90 / 255.0f, 0xD7 / 255.0f, 0xEC / 255.0f, 0xff / 255.0f);
		back1Pixmap.fillRectangle(0, 0, 16, 16);
		back1Texture = new Texture(back1Pixmap);
		back1 = new Sprite(back1Texture);
		back1Pixmap.dispose();

		Pixmap back2Pixmap = new Pixmap(16, 16, Format.RGB565);
		back2Pixmap.setColor(128 / 255.0f, 133 / 255.0f, 152 / 255.0f, 0xff / 255.0f);
		back2Pixmap.fillRectangle(0, 0, 16, 16);
		back2Texture = new Texture(back2Pixmap);
		back2 = new Sprite(back2Texture);
		back2Pixmap.dispose();

		Pixmap back3Pixmap = new Pixmap(16, 16, Format.RGB565);
		back3Pixmap.setColor(86 / 255.0f, 0 / 255.0f, 125 / 255.0f, 0xff / 255.0f);
		back3Pixmap.fillRectangle(0, 0, 16, 16);
		back3Texture = new Texture(back3Pixmap);
		back3 = new Sprite(back3Texture);
		back3Pixmap.dispose();

	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset + "'", (Exception) throwable);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		circleTexture.dispose();
		back1Texture.dispose();
		back2Texture.dispose();
		back3Texture.dispose();
	}

}
