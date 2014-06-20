package org.netf.garyou.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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

	public Sprite bullet;

	public Sprite back1;
	public Sprite back2;
	public Sprite back3;

	public Sprite grass1;
	public Sprite grass2;
	public Sprite grass3;
	public Sprite grass4;

	public Animation player;

	public ParticleEffect bulletEffect;
	public ParticleEffect hitEffect;

	public Sprite eye;
	private Texture eyeTexture;

	public Sprite clear;
	public Sprite notClear;
	public Sprite shout;

	public Sprite white;
	private Texture whiteTexture;

	public Sprite logo5jcup;

	public Sprite next;
	public Sprite menu;
	public Sprite retry;
	public Sprite finalStage;

	private Assets() {
	}

	public void init(AssetManager assetManager) {

		this.assetManager = assetManager;

		assetManager.setErrorListener(this);

		assetManager.load("data/fonts/aoyagi.fnt", BitmapFont.class);
		assetManager.load("data/images/garyou.pack", TextureAtlas.class);
		assetManager.load("data/images/background.pack", TextureAtlas.class);

		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);

		for (String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);
		}

		bitmapFont = assetManager.get("data/fonts/aoyagi.fnt");

		TextureAtlas atlas = assetManager.get("data/images/garyou.pack");
		TextureAtlas background = assetManager.get("data/images/background.pack");

		dragonTitle = atlas.createSprite("dragon_game");

		moon = atlas.createSprite("moon");
		ga = atlas.createSprite("ga");
		ryou = atlas.createSprite("ryou");
		ten = atlas.createSprite("ten");
		sei = atlas.createSprite("sei");

		easy = atlas.createSprite("easy");
		normal = atlas.createSprite("normal");
		hard = atlas.createSprite("hard");

		dragonGame = atlas.createSprite("dragon_game");

		Pixmap circlePixmap = new Pixmap(128, 128, Format.RGBA4444);
		circlePixmap.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		circlePixmap.fillCircle(63, 63, 63);
		circleTexture = new Texture(circlePixmap);
		circle = new Sprite(circleTexture);
		circlePixmap.dispose();

		bullet = atlas.createSprite("bullet");

		back1 = background.createSprite("back1");
		back2 = background.createSprite("back2");
		back3 = background.createSprite("back3");

		player = new Animation(0.04f, atlas.createSprites("run"));

		grass1 = atlas.createSprite("grass");
		grass2 = atlas.createSprite("grass");
		grass3 = atlas.createSprite("grass");
		grass4 = atlas.createSprite("grass");

		bulletEffect = new ParticleEffect();
		bulletEffect.load(Gdx.files.internal("data/particle/bullet.p") //
				, Gdx.files.internal("data/particle"));

		hitEffect = new ParticleEffect();
		hitEffect.load(Gdx.files.internal("data/particle/hit.p") //
				, Gdx.files.internal("data/particle"));

		Pixmap eyePixmap = new Pixmap(128, 128, Format.RGBA4444);
		eyePixmap.setColor(1.0f, 0.0f, 0.0f, 1.0f);
		eyePixmap.fillCircle(63, 63, 63);
		eyeTexture = new Texture(eyePixmap);
		eye = new Sprite(eyeTexture);
		eyePixmap.dispose();

		clear = atlas.createSprite("clear");
		notClear = atlas.createSprite("notclear");
		shout = atlas.createSprite("shout");

		Pixmap whitePixmap = new Pixmap(64, 64, Format.RGBA8888);
		whitePixmap.setColor(0xe7 / 255.0f, 0xe3 / 255.0f, 0xc8 / 255.0f, 1.0f);
		whitePixmap.fillRectangle(0, 0, 64, 64);
		whiteTexture = new Texture(whitePixmap);
		white = new Sprite(whiteTexture);
		whitePixmap.dispose();

		logo5jcup = atlas.createSprite("logo_5jcup");
		next = atlas.createSprite("next");
		menu = atlas.createSprite("return");
		retry = atlas.createSprite("retry");
		finalStage = atlas.createSprite("final");

	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset + "'", (Exception) throwable);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		circleTexture.dispose();
		eyeTexture.dispose();
		whiteTexture.dispose();
	}

}
