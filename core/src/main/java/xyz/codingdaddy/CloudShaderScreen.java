package xyz.codingdaddy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CloudShaderScreen implements Screen {

	private OrthographicCamera camera;
	private SpriteBatch spriteBatch;
	private ShaderProgram shaderProgram;
	private Texture texture;
	private BitmapFont font;
	
	public CloudShaderScreen() {
		this.spriteBatch = new SpriteBatch();
		this.shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/clouds.vert"), Gdx.files.internal("shaders/clouds4.frag"));
		this.shaderProgram.pedantic = false;
		
		Gdx.app.log("meke", "Loading this crap.");
		if (!this.shaderProgram.getLog().isEmpty()) {
			Gdx.app.error("meke", this.shaderProgram.getLog().toString());
		}
		
		Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Format.RGB888);
		pixmap.setColor(Color.BLUE);
		pixmap.fillRectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		texture = new Texture(pixmap);
		pixmap.dispose();
		
		font = new BitmapFont();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.translate(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		camera.update();
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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
		spriteBatch.draw(texture, 0, 0);
		spriteBatch.setShader(this.shaderProgram);
		spriteBatch.getShader().setUniformf("u_time", this.delta);
		//spriteBatch.getShader().setUniform2fv("u_resolution", new float[] { Gdx.graphics.getWidth(), Gdx.graphics.getHeight() }, 0, 2);
		spriteBatch.getShader().setUniform2fv("u_resolution", new float[] { 400, 200 }, 0, 2);
		spriteBatch.draw(texture, 0, 0);
		
//		if (!spriteBatch.getShader().getLog().isEmpty()) {
//			Gdx.app.error("meke", spriteBatch.getShader().getLog().toString());
//		}
		
		spriteBatch.setShader(null);
		
		font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
		
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
		// TODO Auto-generated method stub
		
	}

}
