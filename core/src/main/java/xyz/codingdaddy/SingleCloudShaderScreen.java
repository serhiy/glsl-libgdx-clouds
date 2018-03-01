package xyz.codingdaddy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SingleCloudShaderScreen implements Screen {

	private static final Color SKY_COLOR = new Color(0.05882f, 0.7843f, 0.9176f, 1.0f);
	
	private final InputProcessor inputProcessor;
	
	private Stage stage;
	private SpriteBatch spriteBatch;
	private Texture backgroundTexture;
	private BitmapFont font;
	private OrthographicCamera camera;
	private ShaderProgram shaderProgram;
	private float delta;
	
	public SingleCloudShaderScreen(InputProcessor inputProcessor) {
		this.inputProcessor = inputProcessor;
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.translate(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		camera.update();
		
		ShaderProgram.pedantic = false;
		shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/cloud.frag"));
		
		if (!this.shaderProgram.getLog().isEmpty()) {
			Gdx.app.error("cloud-shader", this.shaderProgram.getLog().toString());
		}
		
		backgroundTexture = Utils.createTexture(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), SKY_COLOR);
		
		spriteBatch = new SpriteBatch();
		
		stage = new Stage();
		stage.getViewport().setCamera(camera);

		Cloud cloud1 = new Cloud(400, 200);
		cloud1.setPosition(280, 50);
		
		Cloud cloud2 = new Cloud(300, 100);
		cloud2.setPosition(100, 10);
		
		stage.addActor(cloud1);
		stage.addActor(cloud2);

		font = new BitmapFont();
		font.setColor(Color.RED);
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputProcessor);
	}
	
	@Override
	public void render(float delta) {
		this.delta += delta;
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		spriteBatch.draw(backgroundTexture, 0, 0);
		font.draw(spriteBatch, "Click '2' to change the ShaderProgram.", 10, Gdx.graphics.getHeight() - 20);
		font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 40);		
		spriteBatch.end();
		
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		shaderProgram.dispose();
		backgroundTexture.dispose();
		font.dispose();
		spriteBatch.dispose();
		stage.dispose();
	}
	
	class Cloud extends Image {
		
		public Cloud(int width, int height) {
			setSize(width, height);
			setDrawable(new TextureRegionDrawable(new TextureRegion(Utils.createTexture(width, height, SKY_COLOR))));
		}
		
		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.setShader(shaderProgram);

			batch.getShader().setUniformf("u_time", delta);
			batch.getShader().setUniform2fv("u_size", new float[] { getWidth(), getHeight() }, 0, 2);
			batch.getShader().setUniform2fv("u_position", new float[] { getX(), getY() }, 0, 2);

			super.draw(batch, parentAlpha);

			batch.setShader(null);
		}
	}

}
