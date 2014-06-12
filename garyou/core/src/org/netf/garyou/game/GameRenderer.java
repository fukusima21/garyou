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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class GameRenderer implements Disposable {

	private static final String TAG = GameRenderer.class.getName();

	private OrthographicCamera camera;
	private OrthographicCamera guiCamera;
	private SpriteBatch batch;
	private ShaderProgram shader;
	private Mesh mesh;
	float[] vertices;

	private GameController gameController;

	public GameRenderer(GameController gameController) {
		this.gameController = gameController;
		init();
	}

	private void init() {

		batch = new SpriteBatch();
		shader = createShader();

		vertices = new float[18];
		mesh = new Mesh(true, vertices.length, 0 //
				, new VertexAttributes( //
						new VertexAttribute(Usage.Position, 2, "a_position") //
						, new VertexAttribute(Usage.Color, 4, "a_color") //
				));

		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2, 0);

		guiCamera = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		guiCamera.position.set(Constants.VIEWPORT_GUI_WIDTH / 2, Constants.VIEWPORT_GUI_HEIGHT / 2, 0);
	}

	private ShaderProgram createShader() {
		// this shader tells opengl where to put things
		String vertexShader = "attribute vec4 a_position; \n" //
				+ "attribute vec4 a_color;                \n" //
				+ "uniform mat4 u_projTrans;              \n" //
				+ "varying vec4 vColor;                   \n" //
				+ "void main()                            \n" //
				+ "{                                      \n" //
				+ "    vColor = a_color;                  \n" //
				+ "    gl_Position = u_projTrans * vec4(a_position.xy, 0.0, 1.0);\n" //
				+ "}\n"; //

		// this one tells it what goes in between the points (i.e
		// colour/texture)
		String fragmentShader = "#ifdef GL_ES             \n" //
				+ "precision mediump float;               \n" //
				+ "#endif                                 \n" //
				+ "varying vec4 vColor;                   \n" //
				+ "void main()                            \n" //
				+ "{                                      \n" //
				+ "gl_FragColor = vColor;                 \n" //
				+ "}";

		// make an actual shader from our strings
		ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);

		// check there's no shader compile errors
		if (!shader.isCompiled()) {
			throw new IllegalStateException(shader.getLog());
		}

		return shader;
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

		Gdx.gl.glDepthMask(false);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		shader.begin();
		shader.setUniformMatrix("u_projTrans", camera.combined);
		renderLine();
		shader.end();

		Gdx.gl.glDepthMask(true);
	}

	@Override
	public void dispose() {
		batch.dispose();
		shader.dispose();
		mesh.dispose();
	}

	private void renderReady() {
		gameController.back.getSprite().draw(batch);
		gameController.moon.getSprite().draw(batch);
		gameController.dragonGame.getSprite().draw(batch);
	}

	private void renderMain() {
		gameController.back.getSprite().draw(batch);
		gameController.moon.getSprite().draw(batch);
		gameController.dragonGame.getSprite().draw(batch);

	}

	private void renderLine() {

		if (gameController.touch == null) {
			return;
		}

		Rectangle bounds = gameController.moon.getSprite().getBoundingRectangle();

		float x1 = gameController.touch.x;
		float y1 = gameController.touch.y;

		float x2 = bounds.x + bounds.width / 2.0f;
		float y2 = bounds.y + bounds.height / 2.0f;

		float rad1 = MathUtils.PI / 2 - MathUtils.atan2(y2 - y1, x2 - x1);

		vertices[0] = x1 - MathUtils.cos(rad1);
		vertices[1] = y1 + MathUtils.sin(rad1);
		vertices[2] = 1.0f;
		vertices[3] = 1.0f;
		vertices[4] = 0.0f;
		vertices[5] = 0.5f;

		vertices[6] = x1 + MathUtils.cos(rad1);
		vertices[7] = y1 - MathUtils.sin(rad1);
		vertices[8] = 1.0f;
		vertices[9] = 1.0f;
		vertices[10] = 0.0f;
		vertices[11] = 0.5f;

		vertices[12] = x2;
		vertices[13] = y2;
		vertices[14] = 1.0f;
		vertices[15] = 1.0f;
		vertices[16] = 1.0f;
		vertices[17] = 0.5f;

		mesh.setVertices(vertices);
		mesh.render(shader, GL20.GL_TRIANGLES);

	}

	private void renderTimer() {

		String time = String.valueOf(gameController.timer);

		Assets.instance.bitmapFont.setScale(1.0f);
		Assets.instance.bitmapFont.setColor(0.0f, 0.0f, 1.0f, 1.0f);
		Assets.instance.bitmapFont.draw(batch, time, 8.0f, 48.0f);

	}

}
