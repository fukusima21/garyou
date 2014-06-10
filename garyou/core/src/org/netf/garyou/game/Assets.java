package org.netf.garyou.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
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
	public Sprite one;
	public Sprite two;
	public Sprite three;
	public Sprite dragonGame;
	public Sprite eye;
	private Texture eyeTexture;
	public Sprite circle;
	private Texture circleTxture;

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

		three = atlas.createSprite("three");
		two = atlas.createSprite("two");
		one = atlas.createSprite("one");

		dragonGame = atlas.createSprite("dragon_game");

		Pixmap eyePixmap = new Pixmap(30, 30, Format.RGBA8888);
		eyePixmap.setColor(1.0f, 0.0f, 0.0f, 1.0f);
		eyePixmap.fillCircle(15, 15, 14);
		eyeTexture = new Texture(eyePixmap);
		eye = new Sprite(eyeTexture);
		eyePixmap.dispose();

		Pixmap circlePixmap = new Pixmap(128, 128, Format.RGBA8888);
		circlePixmap.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		circlePixmap.fillCircle(63, 63, 63);
		circleTxture = new Texture(circlePixmap);
		circle = new Sprite(circleTxture);
		circlePixmap.dispose();

	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset + "'", (Exception) throwable);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		eyeTexture.dispose();
		circleTxture.dispose();
	}

}
