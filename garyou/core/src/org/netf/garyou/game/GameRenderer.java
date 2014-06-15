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
import com.badlogic.gdx.math.MathUtils;
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

		vertices = new float[4];
		mesh = new Mesh(true, vertices.length, 0 //
				, new VertexAttributes( //
						new VertexAttribute(Usage.Position, 2, "a_position") //
				));

		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2, 0);

		guiCamera = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		guiCamera.position.set(Constants.VIEWPORT_GUI_WIDTH / 2, Constants.VIEWPORT_GUI_HEIGHT / 2, 0);

	}

	private ShaderProgram createShader() {
		// this shader tells opengl where to put things
		String vertexShader = "                           \n" //
				+ "attribute vec4 a_position;             \n" //
				+ "uniform mat4 u_projTrans;              \n" //
				+ "                                       \n" //
				+ "void main()                            \n" //
				+ "{                                      \n" //
				+ "    gl_Position = u_projTrans * vec4(a_position.xy, 0.0 ,1.0);\n" //
				+ "}\n"; //

		// this one tells it what goes in between the points (i.e
		// colour/texture)
		String fragmentShader = "                         \n" //
				+ "#ifdef GL_ES                           \n" //
				+ "precision mediump float;               \n" //
				+ "#endif                                 \n" //
				+ "                                       \n" //
				+ "uniform vec2 resolution;               \n" //
				+ "varying vec2 vPosition;                \n" //
				+ "                                       \n" //
				+ "void main()                            \n" //
				+ "{                                      \n" //
				+ "    vec2 position = (gl_FragCoord.xy / resolution ) - vec2(0.5);   \n" //
				+ "    float len = length(position);      \n" //
				+ "    float vignette = smoothstep(0.75, 0.3, len); \n" //
				+ "    gl_FragColor = vec4(vignette, 0.0, 0.0, 1.0);  \n" //
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
		shader.setUniformf("resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
		gameController.moon.getSprite().draw(batch);
		gameController.back.getSprite().draw(batch);
		gameController.dragonGame.getSprite().draw(batch);
		gameController.player.getSprite().draw(batch);
		gameController.grass1.getSprite().draw(batch);
		gameController.grass2.getSprite().draw(batch);
	}

	private void renderMain() {
		gameController.moon.getSprite().draw(batch);
		gameController.back.getSprite().draw(batch);
		gameController.dragonGame.getSprite().draw(batch);
		gameController.player.getSprite().draw(batch);
		gameController.grass1.getSprite().draw(batch);
		gameController.grass2.getSprite().draw(batch);
	}

	private void renderLine() {

		if (gameController.touch == null) {
			return;
		}

		float x1 = 1.5f;
		float y1 = 1.5f;

		float x2 = gameController.touch.x;
		float y2 = gameController.touch.y;

		float rad1 = MathUtils.atan2(y2 - y1, x2 - x1);

		vertices[0] = x1;
		vertices[1] = y1;
		vertices[2] = 15.0f * MathUtils.cos(rad1) + x1;
		vertices[3] = 15.0f * MathUtils.sin(rad1) + y1;

		Gdx.gl.glLineWidth(10.0f);
		mesh.setVertices(vertices);
		mesh.render(shader, GL20.GL_LINES);

	}

	private void renderTimer() {

		String time = String.valueOf(gameController.timer);

		Assets.instance.bitmapFont.setScale(1.0f);
		Assets.instance.bitmapFont.setColor(0.0f, 0.0f, 1.0f, 1.0f);
		Assets.instance.bitmapFont.draw(batch, time, 8.0f, 464.0f);

	}

}
