package org.netf.garyou.util;

import com.badlogic.gdx.math.MathUtils;

public class GaryouUtil {

	private static StringBuilder sb = new StringBuilder("00.00");

	public static StringBuilder floatToString(float timer) {

		int time = MathUtils.floorPositive(timer * 100.0f);

		if (time < 0) {
			time = 0;
		}

		sb.setCharAt(0, time / 1000 == 0 ? ' ' : (char) ('0' + (time / 1000)));
		sb.setCharAt(1, (char) ('0' + (time % 1000) / 100));
		sb.setCharAt(3, (char) ('0' + (time % 100) / 10));
		sb.setCharAt(4, (char) ('0' + (time % 10)));

		return sb;
	}
}
