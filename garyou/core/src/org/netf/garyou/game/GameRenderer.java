/*******************************************************************************
 * Copyright 2013 Andreas Oehlke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.netf.garyou.game;

import org.netf.garyou.util.Constants;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class GameRenderer implements Disposable {

	private static final String TAG = GameRenderer.class.getName();

	private OrthographicCamera camera;
	private OrthographicCamera guiCamera;
	private SpriteBatch batch;
	private GameController gameController;

	public GameRenderer(GameController gameController) {
		this.gameController = gameController;
		init();
	}

	private void init() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2, 0);

		guiCamera = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		guiCamera.position.set(Constants.VIEWPORT_GUI_WIDTH / 2, Constants.VIEWPORT_GUI_HEIGHT / 2, 0);
	}

	public void render() {
		camera.update();
		guiCamera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		if (gameController.state != null) {
			switch (gameController.state) {
			case READY:
				renderReady();
				break;
			case MAIN:
				renderMain();
				break;
			case FINISH:
				break;
			}
		}
		batch.end();

		batch.setProjectionMatrix(guiCamera.combined);
		batch.begin();
		if (gameController.state != null) {
			switch (gameController.state) {
			case READY:
				break;
			case MAIN:
				renderTimer();
				break;
			case FINISH:
				break;
			}
		}
		batch.end();

	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	private void renderReady() {
		gameController.back.getSprite().draw(batch);
		gameController.moon.getSprite().draw(batch);
//		gameController.one.getSprite().draw(batch);
//		gameController.two.getSprite().draw(batch);
//		gameController.three.getSprite().draw(batch);
		gameController.dragonGame.getSprite().draw(batch);
	}

	private void renderMain() {
		gameController.back.getSprite().draw(batch);
		gameController.moon.getSprite().draw(batch);
		gameController.dragonGame.getSprite().draw(batch);

	}

	private void renderTimer() {

		String time = String.valueOf(gameController.timer);

		Assets.instance.bitmapFont.setScale(1.0f);
		Assets.instance.bitmapFont.setColor(0.0f, 0.0f, 1.0f, 1.0f);
		Assets.instance.bitmapFont.draw(batch, time, 8.0f, 48.0f);

	}

}
