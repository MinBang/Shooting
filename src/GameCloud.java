
public class GameCloud extends GameObject {
	
	int index;
	int speed;
	
	public GameCloud(GameResource resource, GameScene scene, int type, int index, int speed){
		super(resource, scene, type);
		
		this.index = index;
		this.speed = speed;
		this.width = resource.getImage(type).getWidth(scene);
		this.height = resource.getImage(type).getHeight(scene);
	}
}
