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

	/**
	 * 座標設定
	 *
	 * @param x 中心X座標
	 * @param y 中心Y座標
	 */
	public void setCenterPosition(float x, float y) {
		Rectangle rectangle = sprite.getBoundingRectangle();
		sprite.setBounds(x - rectangle.width / 2.0f, y - rectangle.height / 2.0f, rectangle.width, rectangle.height);
	}

	/**
	 * サイズ設定
	 *
	 * @param width 幅
	 * @param height 高さ
	 */
	public void setSize(float width, float height) {
		Rectangle rectangle = sprite.getBoundingRectangle();
		sprite.setBounds(rectangle.x + rectangle.width / 2.0f - width / 2.0f, rectangle.y + rectangle.height / 2.0f - height / 2.0f, width, height);
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
