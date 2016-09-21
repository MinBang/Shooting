import java.awt.Graphics;


public class GameObject {
	int x = 0;
	int y = 0;
	int width = 0;
	int height = 0;
	int type = -1;
	
	GameResource resource;
	GameScene scene;
	
	public GameObject(GameResource resource, GameScene scene, int type){
		this.resource = resource;
		this.scene = scene;
		this.type = type;
	}
	
	public void move(int x, int y){
		 this.x = x;
		 this.y = y;
	};
	
	public void draw(Graphics gc){
		gc.drawImage(resource.getImage(type), x, y, scene);
	};
}
