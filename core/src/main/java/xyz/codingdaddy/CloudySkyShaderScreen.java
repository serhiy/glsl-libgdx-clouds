package xyz.codingdaddy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class CloudySkyShaderScreen implements Screen {

	private final InputProcessor inputProcessor;
	
	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	private ShaderProgram shaderProgram;
	private Texture backgroundTexture;
	private BitmapFont font;
	
	public CloudySkyShaderScreen(InputProcessor inputProcessor) {
		this.inputProcessor = inputProcessor;
		
		spriteBatch = new SpriteBatch();
		
		ShaderProgram.pedantic = false;
		shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/cloudysky.frag"));

		if (!shaderProgram.getLog().isEmpty()) {
			Gdx.app.error("cloud-shader", shaderProgram.getLog().toString());
		}
		
		backgroundTexture = Utils.createTexture(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.BLUE);
		
		font = new BitmapFont();
		font.setColor(Color.RED);
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.translate(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		camera.update();
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputProcessor);
	}
	
	private float delta = 0;

	@Override
	public void render(float delta) {
		this.delta += delta;
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		spriteBatch.setShader(this.shaderProgram);
		spriteBatch.getShader().setUniformf("u_time", this.delta);
		spriteBatch.getShader().setUniform2fv("u_resolution", new float[] { 400, 200 }, 0, 2);
		spriteBatch.draw(backgroundTexture, 0, 0);
		
		spriteBatch.setShader(null);
		
		font.draw(spriteBatch, "Click '1' to change the ShaderProgram.", 10, Gdx.graphics.getHeight() - 20);
		font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 40);		
		
		spriteBatch.end();
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
		
	}

}
