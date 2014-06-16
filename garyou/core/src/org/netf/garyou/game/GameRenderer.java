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
	private StringBuilder timer;

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

		timer = new StringBuilder("00.00");
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
				+ "void main()                            \n" //
				+ "{                                      \n" //
				+ "    gl_FragColor = vec4(1.0f, 1.0f, 1.0f, 0.7);  \n" //
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
		renderBackground();
		batch.end();

		batch.setProjectionMatrix(guiCamera.combined);
		batch.begin();
		renderPartcle();
		batch.end();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		switch (gameController.getState()) {
		case READY:
			renderReady();
			break;
		case MAIN:
		case FIRE:
			renderMain();
			break;
		case FINISH:
			break;
		default:
			break;
		}
		batch.end();

		batch.setProjectionMatrix(guiCamera.combined);
		batch.begin();
		switch (gameController.getState()) {
		case READY:
			break;
		case MAIN:
		case FIRE:
			renderTimer();
			break;
		case FINISH:
			break;
		default:
			break;
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

	private void renderBackground() {
		gameController.back.getSprite().draw(batch);
	}

	private void renderReady() {
		gameController.moon.getSprite().draw(batch);
		gameController.dragonGame.getSprite().draw(batch);
		gameController.player.getSprite().draw(batch);
		gameController.grass1.getSprite().draw(batch);
		gameController.grass2.getSprite().draw(batch);
	}

	private void renderMain() {
		gameController.dragonGame.getSprite().draw(batch);
		gameController.grass3.getSprite().draw(batch);
		gameController.grass4.getSprite().draw(batch);
		gameController.player.getSprite().draw(batch);
		gameController.grass1.getSprite().draw(batch);
		gameController.grass2.getSprite().draw(batch);

		gameController.bullet.getSprite().draw(batch);

	}

	private void renderLine() {

		if (gameController.touch != null) {

			float x1 = Constants.FIRST_BULLET_X;
			float y1 = Constants.FIRST_BULLET_Y;

			float x2 = gameController.touch.x;
			float y2 = gameController.touch.y;

			float rad1 = MathUtils.atan2(y2 - y1, x2 - x1);

			vertices[0] = 1.0f * MathUtils.cos(rad1) + x1;
			vertices[1] = 1.0f * MathUtils.sin(rad1) + y1;
			vertices[2] = 15.0f * MathUtils.cos(rad1) + x1;
			vertices[3] = 15.0f * MathUtils.sin(rad1) + y1;

			Gdx.gl.glLineWidth(1.0f);
			mesh.setVertices(vertices);
			mesh.render(shader, GL20.GL_LINES);

		}

	}

	private void renderTimer() {

		int time = MathUtils.floorPositive(gameController.timer * 100.0f);

		if (time < 0) {
			time = 0;
		}

		timer.setCharAt(0, time / 1000 == 0 ? ' ' : (char) ('0' + (time / 1000)));
		timer.setCharAt(1, (char) ('0' + (time % 1000) / 100));
		timer.setCharAt(3, (char) ('0' + (time % 100) / 10));
		timer.setCharAt(4, (char) ('0' + (time % 10)));

		Assets.instance.bitmapFont.setScale(1.0f);
		Assets.instance.bitmapFont.setColor(0.0f, 0.0f, 1.0f, 1.0f);
		Assets.instance.bitmapFont.draw(batch, timer, 8.0f, 464.0f);

	}

	private void renderPartcle() {

		if (gameController.touch != null || //
				gameController.getState() == GameController.STATE.FIRE) {
			float deltaTime = Gdx.graphics.getDeltaTime();
			Assets.instance.bulletEffect.draw(batch, deltaTime);
		}

	}

}
