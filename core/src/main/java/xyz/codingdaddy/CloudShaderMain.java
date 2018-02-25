package xyz.codingdaddy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class CloudShaderMain extends Game {

	@Override
	public void create() {
		setScreen(new CloudShaderScreen());
	}
	
}