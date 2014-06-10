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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class MenuRenderer implements Disposable {

	private static final String TAG = MenuRenderer.class.getName();

	private MenuController menuController;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	public MenuRenderer(MenuController menuController) {
		this.menuController = menuController;
		init();
	}

	private void init() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2, 0);
	}

	public void render() {

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		menuController.moon.getSprite().draw(batch);
		menuController.ga.getSprite().draw(batch);
		menuController.ryou.getSprite().draw(batch);
		menuController.ten.getSprite().draw(batch);
		menuController.sei.getSprite().draw(batch);
		menuController.dragon.getSprite().draw(batch);

		if (menuController.easy.focused) {
			Rectangle bounds = menuController.easy.getSprite().getBoundingRectangle();
			menuController.circle.getSprite().setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
			menuController.circle.getSprite().setAlpha(0.5f);
			menuController.circle.getSprite().draw(batch);
		}
		menuController.easy.getSprite().draw(batch);

		if (menuController.normal.focused) {
			Rectangle bounds = menuController.normal.getSprite().getBoundingRectangle();
			menuController.circle.getSprite().setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
			menuController.circle.getSprite().setAlpha(0.5f);
			menuController.circle.getSprite().draw(batch);
		}
		menuController.normal.getSprite().draw(batch);

		if (menuController.hard.focused) {
			Rectangle bounds = menuController.hard.getSprite().getBoundingRectangle();
			menuController.circle.getSprite().setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
			menuController.circle.getSprite().setAlpha(0.5f);
			menuController.circle.getSprite().draw(batch);
		}
		menuController.hard.getSprite().draw(batch);
		batch.end();

	}

	@Override
	public void dispose() {
		batch.dispose();
	}

}
