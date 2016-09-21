

public class GamePlayer extends GameObject {
	
	int index = 0;
	int speed = 0;
	int life = 0;
	int bulCnt = 0;
	int state = 0;
	boolean isUse = false;
	
	public GamePlayer(GameResource resource, GameScene scene, int type, int index){
		super(resource, scene, type);
	
		this.index = index;
		this.width = resource.getImage(type).getWidth(scene);
		this.height = resource.getImage(type).getHeight(scene);
		
		init();		
	}
	
	public void init(){
		life = 5;
		speed = 6;	
		bulCnt = 5;
		state = GameResource.STATE_LOAD;
		type = GameResource.PLAYER_HOLDON;
	}	
	
	public void decreaseBulCnt(){
		if(5 < bulCnt){
			bulCnt--;
		}
	}
	
	public void decreaseLife(){		
		if(0 < life){
			life--;
		}
		
		bulCnt += 2;
		state = GameResource.STATE_ILL;
		type = GameResource.PLAYER_ILL;
	}
}
