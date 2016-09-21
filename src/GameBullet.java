
public class GameBullet extends GameObject{

	int degree = 0;
	int index = 0;
	int speed = 0;
	
	public GameBullet(GameResource resource, GameScene scene, int type, int degree, int index, int speed){
		super(resource, scene, type);
		
		this.index = index;
		this.degree = degree;
		this.speed = speed;
		this.width = resource.getImage(type).getWidth(scene);
		this.height = resource.getImage(type).getHeight(scene);
	}
	
	public void move(){
		x -= (speed*Math.sin(Math.toRadians(degree)));
		y -= (speed*Math.cos(Math.toRadians(degree)));
	}
}
