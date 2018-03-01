package xyz.codingdaddy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class CloudShaderMain extends Game {

	@Override
	public void create() {
		InputProcessor inputProcessor = new UserInputProcessor(this);
		setScreen(new SingleCloudShaderScreen(inputProcessor));
	}
	
}