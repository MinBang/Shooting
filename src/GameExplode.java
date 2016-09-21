import java.awt.Graphics;


public class GameExplode extends GameObject {

	int motion = 0;
	
	public GameExplode(GameResource resource, GameScene scene, int type) {
		super(resource, scene, type);
		
		this.width = resource.getImage(type).getWidth(scene) / 4;
		this.height = resource.getImage(type).getHeight(scene) / 4;
	}
	
	@Override
	public void draw(Graphics gc){		
		int sx = x - ((motion/4) * width);
		int sy = y - ((motion%4) * height);
		
		gc.setClip(x, y, width , height);
		gc.drawImage(resource.getImage(type), sx, sy, scene);
		
		gc.setClip(0 , 0, GameResource.gScreenWidth, GameResource.gScreenHeight);
	}
	
	public void changeMotion(){
		System.out.println("motion : " + motion);
		
		motion = (motion+1)%16;
	}
}
