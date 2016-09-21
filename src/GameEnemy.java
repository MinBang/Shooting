import java.awt.Graphics;


public class GameEnemy extends GameObject{

	int speed = 0;
	int degree = 0;
	int motion = 0;
	int state = 0;
	int mode = 0;
	int life = 0;
	int stage = 0;
	
	public GameEnemy(GameResource resource, GameScene scene, int type, int degree, int stage) {
		super(resource, scene, type);
	
		this.degree = degree;
		this.stage = stage;
		this.height = resource.getImage(type).getHeight(scene);
		state = GameResource.STATE_LOAD;
		
		switch(type){
		case GameResource.ENEMY_MINI:
			speed = 3;
			life = 3 + (stage * 2);
			this.width = resource.getImage(type).getWidth(scene) / 7;
			
			break;
		case GameResource.ENEMY_MIDDLE:
			speed = 5;
			life = 5 + (stage * 8);
			this.width = resource.getImage(type).getWidth(scene) / 7;
			
			break;
		case GameResource.ENEMY_KING:
			speed = 4;
			life = 100 + (stage * 50);
			this.width = resource.getImage(type).getWidth(scene);
			
			break;
		}
	}
	
	public void moveEX(){
		
		if(GameResource.ENEMY_KING == type){
			if(400 < x){
				x--;
			}
		}else{
			if(0 == mode){
				x -= (speed*Math.sin(Math.toRadians(degree)));
			}
			else{
				x += (speed*Math.sin(Math.toRadians(degree)));
			}		
			y -= (speed*Math.cos(Math.toRadians(degree)));
		}		
	}
	
	@Override
	public void draw(Graphics gc){
		
		if(GameResource.ENEMY_KING == type){
			gc.drawImage(resource.getImage(type), x, y, scene);
		}else{
			int sx = x - (motion * width);
			int sy = y;
			
			gc.setClip(x, y, width, height);
			gc.drawImage(resource.getImage(type), sx, sy, scene);
			
			gc.setClip(0 , 0, GameResource.gScreenWidth, GameResource.gScreenHeight);
		}
	}
	
	public void changeMotion(){
		motion = (motion+1)%7;
	}
	
	public void decreaseLife(){		
		if(0 < life){
			life--;
		}
	}
}
