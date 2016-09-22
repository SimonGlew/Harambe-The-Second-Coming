package gameobjects;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tree extends GameObject{

	public Tree(){
		try {
			image = ImageIO.read(new File("src/tree.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}