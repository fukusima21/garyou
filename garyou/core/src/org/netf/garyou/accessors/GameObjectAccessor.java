package org.netf.garyou.accessors;

import org.netf.garyou.game.objects.base.GameObject;

import com.badlogic.gdx.math.Rectangle;

import aurelienribon.tweenengine.TweenAccessor;

public class GameObjectAccessor implements TweenAccessor<GameObject> {

	public static final int MOVE = 0;
	public static final int SIZE = 1;
	public static final int MOVE_SIZE = 2;
	public static final int ALPHA = 3;
	public static final int SIZE_ALPHA = 4;
	public static final int MOVE_SIZE_ALPHA = 5;
	public static final int ROTATE = 6;
	public static final int MOVE_ALPHA = 7;
	public static final int COLOR = 8;

	@Override
	public int getValues(GameObject target, int tweenType, float[] returnValues) {

		Rectangle bounds = target.getSprite().getBoundingRectangle();

		switch (tweenType) {
		case MOVE:
			returnValues[0] = bounds.x + bounds.width / 2.0f;
			returnValues[1] = bounds.y + bounds.height / 2.0f;
			return 2;
		case SIZE:
			returnValues[0] = bounds.width;
			returnValues[1] = bounds.height;
			return 2;
		case MOVE_SIZE:
			returnValues[0] = bounds.x + bounds.width / 2.0f;
			returnValues[1] = bounds.y + bounds.height / 2.0f;
			returnValues[2] = bounds.width;
			returnValues[3] = bounds.height;
			return 4;
		case ALPHA:
			returnValues[0] = target.getSprite().getColor().a;
			return 1;
		case SIZE_ALPHA:
			returnValues[0] = bounds.width;
			returnValues[1] = bounds.height;
			returnValues[2] = target.getSprite().getColor().a;
			return 3;
		case MOVE_SIZE_ALPHA:
			returnValues[0] = bounds.x + bounds.width / 2.0f;
			returnValues[1] = bounds.y + bounds.height / 2.0f;
			returnValues[2] = bounds.width;
			returnValues[3] = bounds.height;
			returnValues[4] = target.getSprite().getColor().a;
			return 5;
		case ROTATE:
			returnValues[0] = target.getSprite().getRotation();
			return 1;
		case MOVE_ALPHA:
			returnValues[0] = bounds.x + bounds.width / 2.0f;
			returnValues[1] = bounds.y + bounds.height / 2.0f;
			returnValues[2] = target.getSprite().getColor().a;
			return 3;
		case COLOR:
			returnValues[0] = target.getSprite().getColor().r;
			returnValues[1] = target.getSprite().getColor().g;
			returnValues[2] = target.getSprite().getColor().b;
			return 3;
		default:
			break;
		}
		return 0;

	}

	@Override
	public void setValues(GameObject target, int tweenType, float[] newValues) {

		switch (tweenType) {
		case MOVE:
			target.getSprite().setCenter(newValues[0], newValues[1]);
			break;
		case SIZE:
			target.setSize(newValues[0], newValues[1]);
			break;
		case MOVE_SIZE:
			target.getSprite().setCenter(newValues[0], newValues[1]);
			target.setSize(newValues[2], newValues[3]);
			break;
		case ALPHA:
			target.getSprite().setAlpha(newValues[0]);
			break;
		case SIZE_ALPHA:
			target.setSize(newValues[0], newValues[1]);
			target.getSprite().setAlpha(newValues[2]);
			break;
		case MOVE_SIZE_ALPHA:
			target.getSprite().setCenter(newValues[0], newValues[1]);
			target.setSize(newValues[2], newValues[3]);
			target.getSprite().setAlpha(newValues[4]);
			break;
		case ROTATE:
			target.getSprite().setRotation(newValues[0]);
			break;
		case MOVE_ALPHA:
			target.getSprite().setCenter(newValues[0], newValues[1]);
			target.getSprite().setAlpha(newValues[2]);
			break;
		case COLOR:
			target.getSprite().setColor(newValues[0], newValues[1], newValues[2], target.getSprite().getColor().a);
			break;
		default:
			break;
		}
	}

}
