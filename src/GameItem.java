import java.awt.Graphics;


public class GameItem extends GameObject {

	int rx;
	int motion = 0;
	int speed = 0;
	int state = 0;
	
	public GameItem(GameResource resource, GameScene scene, int type) {
		super(resource, scene, type);
		
		this.width = resource.getImage(type).getWidth(scene) / 7;
		this.height = resource.getImage(type).getHeight(scene);
	}
	
	public void move(int x, int y){
		this.x = x;
		this.y = y;
		rx = x + 100;
		if(GameResource.gScreenWidth - width < rx){
			rx = GameResource.gScreenWidth - width;
		}
		
		speed = 4;
	}
	
	public void moveEX(){
		if(x < rx && state == 0){
			x += speed;
			if(rx <= x){
				state = 1;
			}
		}else{
			x -= speed;
		}
	}
	
	public void changeMotion(){
		motion = (motion+1)%7;
	}
	
	@Override
	public void draw(Graphics gc){		
		int sx = x - (motion * width);
		int sy = y;
		
		gc.setClip(x, y, width, height);
		gc.drawImage(resource.getImage(type), sx, sy, scene);
		
		gc.setClip(0 , 0, GameResource.gScreenWidth, GameResource.gScreenHeight);
	}
}
