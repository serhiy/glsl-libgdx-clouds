package xyz.codingdaddy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;

public class Utils {
	public static Texture createTexture(int width, int height, Color color) {
		Pixmap pixmap = new Pixmap(width, height, Format.RGB888);
		pixmap.setColor(color);
		pixmap.fillRectangle(0, 0, width, height);
		Texture texture = new Texture(pixmap);
		pixmap.dispose();
		return texture;
	}
}
