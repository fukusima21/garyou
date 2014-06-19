package org.netf.garyou.game.objects.base;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class GameObject {

	private Sprite sprite;
	public boolean focused;

	/**
	 * コンストラクタ
	 *
	 * @param x 中心X座標
	 * @param y 中心Y座標
	 * @param width 幅
	 * @param height 高さ
	 */
	public GameObject(Sprite sprite, float x, float y, float width, float height) {
		this(sprite, x, y, width, height, 1.0f);
	}

	/**
	 * コンストラクタ
	 *
	 * @param x 中心X座標
	 * @param y 中心Y座標
	 * @param width 幅
	 * @param height 高さ
	 * @param a 透過
	 */
	public GameObject(Sprite sprite, float x, float y, float width, float height, float a) {
		this.sprite = sprite;
		sprite.setBounds(x - width / 2.0f, y - height / 2.0f, width, height);
		sprite.setAlpha(a);
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {

		Rectangle bounds = this.sprite.getBoundingRectangle();
		sprite.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
		sprite.setColor(this.sprite.getColor());

		this.sprite = sprite;
	}

	/**
	 * サイズ設定
	 *
	 * @param width 幅
	 * @param height 高さ
	 */
	public void setSize(float targetWidth, float targetHeight) {

		float x = sprite.getX();
		float y = sprite.getY();
		float width = sprite.getWidth();
		float height = sprite.getHeight();

		sprite.setPosition(x + width / 2.0f - targetWidth / 2.0f, y + height / 2.0f - targetHeight / 2.0f);
		sprite.setSize(targetWidth, targetHeight);

	}

	/**
	 * 当たり判定
	 *
	 * @return
	 */
	public boolean isHit(float x1, float y1) {

		Rectangle rectangle = sprite.getBoundingRectangle();

		float x = rectangle.x + rectangle.width / 2.0f;
		float y = rectangle.y + rectangle.height / 2.0f;

		return (x1 - x) * (x1 - x) + (y1 - y) * (y1 - y) < (rectangle.width / 2.0) * (rectangle.width / 2.0);

	}
}
