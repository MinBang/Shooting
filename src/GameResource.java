import java.awt.Image;
import java.util.HashMap;
import java.util.Map;


public class GameResource {
	
	// 게임 키 정의
	public final static int UP_PRESSED = 0x001;
	public final static int DOWN_PRESSED = 0x002;
	public final static int LEFT_PRESSED = 0x004;
	public final static int RIGHT_PRESSED = 0x008;
	public final static int FIRE_PRESSED = 0x010;

	// 게임 상태 정의
	public final static int STATUS_TITLE = 0;
	public final static int STATUS_STARTGAME = 1;
	public final static int STATUS_RUNNINGAME = 2;
	public final static int STATUS_STOPGAME = 3;
	public final static int STATUS_GAMEOVER = 4;
	
	// 화면 크기
	public final static int gScreenWidth = 640;// 게임 화면 너비
	public final static int gScreenHeight = 480;// 게임 화면 높이
	
	public final static int gDelay = 17;
	
	// 캐릭터 상태
	public final static int STATE_LOAD 	= 0;
	public final static int STATE_RUN 	= 1;
	public final static int STATE_ILL 	= 2;
	public final static int STATE_INVINCIBLE = 3;
	
	// 배경들
	public final static int BACKGROUND = 3;
	public final static int CLOUD1 = 4;
	public final static int CLOUD2 = 5;
	
	public final static int TITLE = 6;
	public final static int PUSH_TITLE = 7;
	public final static int GAMEOVER= 8;
	
	// player resource
	public final static int PLAYER_HOLDON = 11;
	public final static int PLAYER_FORWARD = 12;
	public final static int PLAYER_BACKWARD = 13;
	public final static int PLAYER_FIRE = 14;
	public final static int PLAYER_ILL = 15;

	// bullet
	public final static int BULLET0 = 21;
	public final static int BULLET1 = 22;
	public final static int BULLET2 = 23;
	
	// enemy
	public final static int ENEMY_MINI = 31;
	public final static int ENEMY_MIDDLE = 32;
	public final static int ENEMY_KING = 33;
	public final static int ENEMY_BOMB = 34;
	public final static int ENEMY_ITEM1 = 35; 
	
	//private Image imagePlayer;
	private Map<Integer, Image> imageMap;
		
	public GameResource(){
		imageMap = new HashMap<Integer, Image>();
	}
	
	public void setImage(int type, Image image){
		imageMap.put(type, image);
	}
	
	public Image getImage(int type){
		
		return imageMap.get(type);
	}	
}
