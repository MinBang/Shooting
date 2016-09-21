import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class GameFrame extends JFrame implements Runnable{

	/**
	 * 변수 선언
	 */
	GameResource resource;	
	GameScene scene;
	Thread gThread;
	GameObject bg;
	GamePlayer[] players;
	Vector<GameCloud> vClouds;
	Vector<GameBullet> vBullets;
	Vector<GameEnemy> vEnemys;
	Vector<GameItem> vItems;
	Vector<GameExplode> vExplodes;
	
	//GameBullet bullets;
	
	// 게임이 실행 체크
	public boolean isRun = true;
	public int gameStatus = GameResource.STATUS_TITLE;
	
	// 게임 frame 속도 관한 변수
	long preTime = 0;
	int delay = GameResource.gDelay;	
	int key = 0;
	int cnt = 0;
	int gameCnt = 0;
	int enemyCnt = 450;
	int enemyFire = 100;
	int stage = 0;
	
	////////////////////////////////////////
	
	private JPanel contentPane; 
	/**
	 * Create the frame.
	 */
	public GameFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		//////
		
		gThread = new Thread(this);
		resource = new GameResource();
		scene = new GameScene(this, resource);

		bg = new GameObject(resource, scene, GameResource.BACKGROUND);
		players = new GamePlayer[2];
		
		initGameFrame();
		initGameScene();
		initGameResource();
		
		for(int i=0;i<2;i++){
			players[i] = new GamePlayer(resource, scene, GameResource.PLAYER_HOLDON, i);
		}
		
		initGameObject();
		
		gThread.start();
	}
	
	/**
	 * Game Init
	 */
	
	public void initGameFrame(){
		
		setIconImage(makeImage("./src/rsc/icon.png"));
		setTitle("Bang Shooting Game");
		setBackground(new Color(0xffffff));
		getContentPane().setLayout(null);// 윈도우의 레이아웃을 프리로 설정
		setBounds(100, 100, GameResource.gScreenWidth, GameResource.gScreenHeight);// 윈도우의 시작 위치와 너비
																// 높이 지정
		setResizable(false);// 윈도우의 크기를 변경할 수 없음
		setVisible(true);// 윈도우 표시

		addKeyListener(new GameKeyListener());// 키 입력 이벤트 리스너 활성화
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				Window wnd = e.getWindow();
				wnd.setVisible(false);
				wnd.dispose();
				System.exit(0);
			}
		});// 윈도우의 닫기 버튼 활성화
		
		setVisible(true);
	}
	
	public void initGameScene(){
		scene.setBounds(0, 0, GameResource.gScreenWidth, GameResource.gScreenHeight);
		getContentPane().add(scene);
		
		scene.repaint();
	}
	
	public void initGameResource(){
		resource.setImage(GameResource.BULLET0, makeImage("./src/rsc_game/bullet_0.png"));
		resource.setImage(GameResource.BULLET2, makeImage("./src/rsc_game/bullet_2.png"));
		resource.setImage(GameResource.PLAYER_HOLDON, makeImage("./src/rsc_player/my_00.png"));
		resource.setImage(GameResource.PLAYER_FORWARD, makeImage("./src/rsc_player/my_02.png"));
		resource.setImage(GameResource.PLAYER_BACKWARD, makeImage("./src/rsc_player/my_04.png"));
		resource.setImage(GameResource.PLAYER_FIRE, makeImage("./src/rsc_player/my_06.png"));
		resource.setImage(GameResource.PLAYER_ILL, makeImage("./src/rsc_player/my_08.png"));
		resource.setImage(GameResource.BACKGROUND, makeImage("./src/rsc/구름.jpg"));
		resource.setImage(GameResource.CLOUD1, makeImage("./src/rsc/cloud0.png"));
		resource.setImage(GameResource.CLOUD2, makeImage("./src/rsc/bg_f.png"));
		resource.setImage(GameResource.CLOUD2, makeImage("./src/rsc/bg_f.png"));
		resource.setImage(GameResource.ENEMY_MINI, makeImage("./src/rsc_game/enemy0.png"));
		resource.setImage(GameResource.ENEMY_MIDDLE, makeImage("./src/rsc_game/enemy0.png"));
		resource.setImage(GameResource.ENEMY_KING, makeImage("./src/rsc_game/enemy1.png"));
		resource.setImage(GameResource.ENEMY_BOMB, makeImage("./src/rsc_game/explode.png"));
		resource.setImage(GameResource.ENEMY_ITEM1, makeImage("./src/rsc_game/item0.png"));
		resource.setImage(GameResource.TITLE, makeImage("./src/rsc/title1.png"));
		resource.setImage(GameResource.PUSH_TITLE, makeImage("./src/rsc/pushspace.png"));
		resource.setImage(GameResource.GAMEOVER, makeImage("./src/rsc_game/gameover.png"));
	}
	
	public void initGameObject(){
		
		cnt = 0;
		gameCnt = 0;
		enemyCnt = 350;
		enemyFire = 120;
		
		players[0].init();
		for(int i=0;i<2;i++){			
			players[i].move(0,  GameResource.gScreenHeight/2 - 50);
		}		
		players[0].isUse = true;
		
		
		//clouds		
		int cloudLen = 8;
		vClouds = new Vector<GameCloud>();
		
		for(int i=0;i<3;i++){
			for(int j=0;j<cloudLen;j++){
				GameCloud tCloud = new GameCloud(resource, scene, GameResource.CLOUD1, i, (i+1) * 7);
				tCloud.move((j * tCloud.width) - 60, 315 + (30 * i));
				
				vClouds.add(tCloud);				
			}
			
			if(i == 1){
				for(int j=0;j<3;j++){
					GameCloud tCloud = new GameCloud(resource, scene, GameResource.CLOUD2, 4, 13);
					tCloud.move(j * tCloud.width, 320);
					
					vClouds.add(tCloud);
				}
			}
		}
		
		// bullets
		vBullets = new Vector<GameBullet>();
		
		// enemys
		vEnemys = new Vector<GameEnemy>();
		
		// items
		vItems = new Vector<GameItem>();
		
		// explode
		vExplodes = new Vector<GameExplode>();
	}
	
	/**
	 * Game Process
	 */

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try{
			while(isRun){
				preTime = System.currentTimeMillis();
				
				scene.repaint();
				keyProcess_Player1();
				objectProcess();
				
				if(System.currentTimeMillis() - preTime < delay){
					Thread.sleep(delay + (System.currentTimeMillis() - preTime));
				}
				
				cnt++;
				if(cnt == 6001){					
					cnt = 0;
				}
			}
		}catch(Exception e){
			//e.printStackTrace();
		}	
	}
	
	public boolean checkMapRange(int x, int y, int width, int height){
		
		int range = 10;
		
		if(GameResource.gScreenWidth < (x + width) - range || x < 0 - range){
			return false;
		}else if(GameResource.gScreenHeight - (height + range) < y || y < 0 - range){
			return false;
		}
		
		return true;
	}
	
	public Image makeImage(String path) {
		Image tImage = null;
		Toolkit tk = Toolkit.getDefaultToolkit();

		tImage = tk.getImage(path);

		try {
			MediaTracker mt = new MediaTracker(this);
			mt.addImage(tImage, 0);
			mt.waitForID(0);
		} catch (Exception e) {
			e.printStackTrace();
			tImage = null;
		}

		return tImage;
	}
	
	public static int randomRange(int n1, int n2) {
	    return (int) (Math.random() * (n2 - n1 + 1)) + n1;
	}
	
	public int getAngle(int sx, int sy, int dx, int dy){
		int vx=dx-sx;
		int vy=dy-sy;
		double rad=Math.atan2(vx,vy);
		int degree=(int)((rad*180)/Math.PI);
		
		return (degree+180);
	}
	
	public boolean checkCollision(GameObject object1, GameObject object2){
		
		if(object1.x < object2.x && object2.x < (object1.x + object1.width) 
			&& object1.y < object2.y && object2.y < (object1.y + object1.height)){
			return true;
		}
		
		if(object1.x < (object2.x + object2.width) && (object2.x + object2.width) < (object1.x + object1.width) 
			&& object1.y < object2.y && object2.y < (object1.y + object1.height)){
			return true;
		}

		if (object1.x < object2.x && object2.x < (object1.x + object1.width)
				&& object1.y < (object2.y + object2.height) && (object2.y + object2.height) < (object1.y + object1.height)) {
			return true;
		}
		
		if (object1.x < (object2.x + object2.width) && (object2.x + object2.width) < (object1.x + object1.width)
				&& object1.y < (object2.y + object2.height) && (object2.y + object2.height) < (object1.y + object1.height)) {
			return true;
		}

		return false;
	}
	
	// object 처리
	
	public void objectProcess(){
		
		switch(gameStatus){
		case GameResource.STATUS_STARTGAME:
			cloudProcess();
			playerProcess();
			
			break;
		case GameResource.STATUS_RUNNINGAME:
			cloudProcess();
			playerProcess();
			itemProcess();
			bulletProcess();
			enemyProcess();
			explodeProcess();
			collisionProcess();

			break;
		case GameResource.STATUS_STOPGAME:
			
			break;
		case GameResource.STATUS_GAMEOVER:
			cloudProcess();
			bulletProcess();
			itemProcess();
			enemyProcess();
			
			break;
		default:
			break;
		}
	}
	
	public void playerProcess(){
		
		for(int i=0;i < players.length;i++){
			
			GamePlayer tPlayer = players[i];
			
			if(tPlayer.isUse == true && tPlayer.state == GameResource.STATE_LOAD){
				//tPlayer.x += 1;
				players[0].move(players[0].x + 2, players[0].y);
				
				if(150 <=tPlayer.x){
					tPlayer.state = GameResource.STATE_RUN;
					gameStatus = GameResource.STATUS_RUNNINGAME;
				}
			}else if(tPlayer.isUse == true && tPlayer.state == GameResource.STATE_ILL){
				if(checkMapRange(players[0].x - 1, players[0].y, players[0].width, players[0].height)){
					players[0].move(players[0].x - 1, players[0].y);
				}
				
				if(cnt == (gameCnt + 50)%6001){
					gameCnt = cnt;
					players[0].state = GameResource.STATE_INVINCIBLE;
				}
			}else if(tPlayer.isUse == true && tPlayer.state == GameResource.STATE_INVINCIBLE){
				if(cnt == (gameCnt + 70)%6001){
					players[0].state = GameResource.STATE_RUN;
				}
			}			
		}
	}
	
	public void cloudProcess(){
		
		for(int i=0;i<vClouds.size();i++){
			GameCloud tCloud = vClouds.get(i);
			int cloudWidth = resource.getImage(tCloud.type).getWidth(this);
			
			if(tCloud.x + cloudWidth <= 0){
				tCloud.move(GameResource.gScreenWidth - 30, tCloud.y);
			}
			else{
				tCloud.move(tCloud.x - tCloud.speed, tCloud.y);
			}
		}
	}
	
	public void bulletProcess(){
		
		for(int i=0;i<2;i++){
			if(true == players[i].isUse && GameResource.STATE_RUN == players[i].state || GameResource.STATE_INVINCIBLE == players[i].state){
				if(GameResource.PLAYER_FIRE == players[i].type && 0 == (cnt % players[i].bulCnt)){
					
					GameBullet tBullet = new GameBullet(resource, scene, GameResource.BULLET0, randomRange(245, 265), players[i].type, 8);
					tBullet.move(players[i].x + 50, players[i].y + 30);
					
					vBullets.add(tBullet);
					
					tBullet = new GameBullet(resource, scene, GameResource.BULLET0, randomRange(268, 272), players[i].type, 8);
					tBullet.move(players[i].x + 50, players[i].y + 30);
					
					vBullets.add(tBullet);

					tBullet = new GameBullet(resource, scene, GameResource.BULLET0, randomRange(275, 295), players[i].type, 8);
					tBullet.move(players[i].x + 50, players[i].y + 30);
					
					vBullets.add(tBullet);
				}
			}
		}
		
		for(int i=0;i<vBullets.size();i++){
			vBullets.get(i).move();
		}
		
		for(int i=0;i<vBullets.size();i++){
			GameBullet tBullet = vBullets.get(i);
			
			if(!checkMapRange(tBullet.x, tBullet.y, tBullet.width, tBullet.height)){
				vBullets.remove(i);
			}
		}
		
		//System.out.println("size : " + vBullets.size());
	}
	
	public void enemyProcess(){
		
		int changeIndex = 100;
		int enemyBulletSpeed = 3;
		
		// 적군 생성
		if(0 == (cnt % enemyCnt)){
			GameEnemy tEnemy = new GameEnemy(resource, scene, GameResource.ENEMY_MINI, randomRange(30, 60), stage);
			tEnemy.move(GameResource.gScreenWidth - 50, GameResource.gScreenHeight + tEnemy.height);
			
			vEnemys.add(tEnemy);
			
			tEnemy = new GameEnemy(resource, scene, GameResource.ENEMY_MIDDLE, randomRange(60, 120), stage);
			tEnemy.move(GameResource.gScreenWidth + 50, GameResource.gScreenHeight/2 - tEnemy.height);
			
			vEnemys.add(tEnemy);
			
			tEnemy = new GameEnemy(resource, scene, GameResource.ENEMY_MINI, randomRange(120, 150), stage);
			tEnemy.move(GameResource.gScreenWidth - 50, 0 - tEnemy.height);
			
			vEnemys.add(tEnemy);
			
			for(int i=0;i<stage;i++){
				tEnemy = new GameEnemy(resource, scene, GameResource.ENEMY_MINI, randomRange(120, 150), stage);
				tEnemy.move(GameResource.gScreenWidth - 70 + (i*10), 0 - tEnemy.height);
				
				vEnemys.add(tEnemy);
			}
		}
		
		if(0 == (cnt % (enemyCnt * 3))){
			
			int t = randomRange(0, 2);
			GameEnemy tEnemy = null;
			
			switch(t){
			case 0:
				tEnemy = new GameEnemy(resource, scene, GameResource.ENEMY_MIDDLE, randomRange(30, 60), stage);
				tEnemy.move(GameResource.gScreenWidth - 50, GameResource.gScreenHeight + tEnemy.height);
				
				break;
			case 1:
				tEnemy = new GameEnemy(resource, scene, GameResource.ENEMY_MIDDLE, randomRange(60, 120), stage);
				tEnemy.move(GameResource.gScreenWidth + 50, GameResource.gScreenHeight/2 - tEnemy.height);
				
				break;
			case 2:
				tEnemy = new GameEnemy(resource, scene, GameResource.ENEMY_MIDDLE, randomRange(120, 150), stage);
				tEnemy.move(GameResource.gScreenWidth - 50, 0 - tEnemy.height);
				
				break;
			default:
				break;
			}			
			
			vEnemys.add(tEnemy);
		}
		
		// 적들 모션 변경
		if(0 == (cnt % 10)){
			for(int i=0;i<vEnemys.size();i++){
				vEnemys.get(i).changeMotion();
			}
		}
		
		// 적들 나오는 시간 당기기, 미사일 발사 시간 당기기
		if(0 == (cnt % 500)){
			GameEnemy tEnemy = null;
			
			tEnemy = new GameEnemy(resource, scene, GameResource.ENEMY_KING, randomRange(60, 120), stage);
			tEnemy.move(GameResource.gScreenWidth + 50, GameResource.gScreenHeight/2 - tEnemy.height/2);
			
			vEnemys.add(tEnemy);
			
			if(150 < enemyCnt){
				enemyCnt -= 100;
				stage++;
			}
			
			if(40 < enemyFire){
				enemyFire -= 10;
			}
		}		
		
		// 적군 이동과 함께 반대로 튕겨져 나가게 하기
		for(int i=0;i<vEnemys.size();i++){
			GameEnemy tEnemy = vEnemys.get(i);
			
			tEnemy.moveEX();
			
			if(tEnemy.mode == 0 && tEnemy.x < changeIndex){
				tEnemy.mode++;
			}
		}

		// 적군 유도탄 발사
		if(0 == (cnt % enemyFire) || 0 == (cnt % (enemyFire + 3))){
			for (int i = 0; i < vEnemys.size(); i++) {
				GameEnemy tEnemy = vEnemys.get(i);
				GameBullet tBullet = null;
				
				if (tEnemy.state == GameResource.STATE_RUN && tEnemy.type == GameResource.ENEMY_MINI) {
					
					tBullet = new GameBullet(resource, scene, GameResource.BULLET2, getAngle(tEnemy.x, tEnemy.y, players[0].x + players[0].width, players[0].y + players[0].height), GameResource.BULLET2, enemyBulletSpeed);
					tBullet.move(tEnemy.x, tEnemy.y + 10);
					
					vBullets.add(tBullet);
				}
			}
		}
		
		if(0 == (cnt % enemyFire) || 0 == (cnt % (enemyFire + 3)) || 0 == (cnt % (enemyFire + 6 )) || 0 == (cnt % (enemyFire + 9))){
			for (int i = 0; i < vEnemys.size(); i++) {
				GameEnemy tEnemy = vEnemys.get(i);
				GameBullet tBullet = null;
				
				if (tEnemy.state == GameResource.STATE_RUN && tEnemy.type == GameResource.ENEMY_MIDDLE) {
					
					tBullet = new GameBullet(resource, scene, GameResource.BULLET2, getAngle(tEnemy.x, tEnemy.y, players[0].x + players[0].width, players[0].y + players[0].height), GameResource.BULLET2, enemyBulletSpeed);
					tBullet.move(tEnemy.x, tEnemy.y + 10);
					
					vBullets.add(tBullet);
				}
			}
		}
		
		if(0 == (cnt % enemyFire) || 0 == (cnt % (enemyFire + 3)) 
			|| 0 == (cnt % (enemyFire + 6 )) || 0 == (cnt % (enemyFire + 9))
			|| 0 == (cnt % (enemyFire + 12 )) || 0 == (cnt % (enemyFire + 15))
			|| 0 == (cnt % (enemyFire + 18 )) || 0 == (cnt % (enemyFire + 21))
			|| 0 == (cnt % (enemyFire + 24 )) || 0 == (cnt % (enemyFire + 27))){


			for (int i = 0; i < vEnemys.size(); i++) {
				GameEnemy tEnemy = vEnemys.get(i);
				GameBullet tBullet = null;
				
				if (tEnemy.state == GameResource.STATE_RUN && tEnemy.type == GameResource.ENEMY_KING) {
					
					for(int j=0;j<4;j++){
						tBullet = new GameBullet(resource, scene, GameResource.BULLET2, ((cnt%enemyFire) + 10) * 2 + (j * 5), GameResource.BULLET2, enemyBulletSpeed);
						tBullet.move(tEnemy.x, tEnemy.y + tEnemy.height/2);
						
						vBullets.add(tBullet);
					}					
				}
			}
		}

		// 화면 밖으로 나간 적 소거, 체력 없는 적도 소거
		for (int i = 0; i < vEnemys.size(); i++) {
			GameEnemy tEnemy = vEnemys.get(i);
			
			if(GameResource.STATE_LOAD == tEnemy.state){
				if(checkMapRange(tEnemy.x, tEnemy.y, tEnemy.width, tEnemy.height)){
					tEnemy.state = GameResource.STATE_RUN;
				}
			}
			else{
				if(!checkMapRange(tEnemy.x, tEnemy.y, tEnemy.width, tEnemy.height)){
					vEnemys.remove(i);
				}
				
				if(0 == tEnemy.life){
					
					int t = randomRange(0, 1);
					
					if(t == 1){
						GameItem tItem = new GameItem(resource, scene, GameResource.ENEMY_ITEM1);
						tItem.move(tEnemy.x, tEnemy.y);
						
						vItems.add(tItem);
					}					
					
					vEnemys.remove(i);
				}			
			}
		}
		
		//System.out.println("size : " + vEnemys.size());
	}
	
	public void collisionProcess(){
		
		for(int i=0;i < vBullets.size();i++){
			GameBullet tBullet = vBullets.get(i);
			
			if(GameResource.PLAYER_FIRE == tBullet.index){
				
				for(int j=0;j<vEnemys.size();j++){
					GameEnemy tEnemy = vEnemys.get(j);
					
					if(tEnemy.state == GameResource.STATE_RUN && checkCollision(tEnemy, tBullet)){
						
						GameExplode tExplode = new GameExplode(resource, scene, GameResource.ENEMY_BOMB);
						tExplode.move(tBullet.x + 10, tBullet.y + 10);
						
						vExplodes.add(tExplode);
						
						vBullets.remove(i);
						tEnemy.decreaseLife();
					}
				}				
			}else if(GameResource.BULLET2 == tBullet.index){
				for(int j=0;j<2;j++){
					if(players[j].isUse == true && players[j].state == GameResource.STATE_RUN){
						if(checkCollision(players[j], tBullet)){
							
							vBullets.remove(i);
							players[j].decreaseLife();
							gameCnt = cnt;
							
							if(0 == players[j].life){
								gameStatus = GameResource.STATUS_GAMEOVER;
							}
						}
					}
				}				
			}
		}
		
		for(int i=0;i<vEnemys.size();i++){
			GameEnemy tEnemy = vEnemys.get(i);
			
			for(int j=0;j<2;j++){
				if(players[j].isUse == true && players[j].state == GameResource.STATE_RUN){
					if(checkCollision(tEnemy, players[j])){
						players[j].decreaseLife();
						gameCnt = cnt;
						
						if(0 == players[j].life){
							gameStatus = GameResource.STATUS_GAMEOVER;
						}
					}
				}
			}
		}
		
		for(int i=0;i<vItems.size();i++){
			GameItem tItem = vItems.get(i);
			
			for(int j=0;j<2;j++){
				if(players[j].isUse == true && players[j].state == GameResource.STATE_RUN || players[j].state == GameResource.STATE_INVINCIBLE){
					if(checkCollision(players[j], tItem)){
						players[j].decreaseBulCnt();
						
						vItems.remove(i);
					}
				}
			}
		}
	}
	
	public void itemProcess(){
		
		if(0 == (cnt % 10)){
			for(int i=0;i<vItems.size();i++){
				vItems.get(i).changeMotion();
			}
		}
		
		for(int i=0;i<vItems.size();i++){
			vItems.get(i).moveEX();
		}
		
		for(int i=0;i<vItems.size();i++){
			GameItem tItem = vItems.get(i);
			
			if(!checkMapRange(tItem.x, tItem.y, tItem.width, tItem.height)){
				vItems.remove(i);
			}
		}
	}
	
	public void explodeProcess(){
		
		if(0 == (cnt % 2)){
			for(int i=0;i<vExplodes.size();i++){
				vExplodes.get(i).changeMotion();
			}
		}
		
		for(int i=0;i<vExplodes.size();i++){
			GameExplode tExplode = vExplodes.get(i);
			
			if(tExplode.motion == 15){
				
				vExplodes.remove(i);
			}
		}
	}
	
	
	
	// key처리	
	public void keyProcess_Player1(){
		
		//System.out.println("status : " + gameStatus);
	
		if(gameStatus == GameResource.STATUS_TITLE || gameStatus == GameResource.STATUS_GAMEOVER){
			
			switch(key){
			case GameResource.FIRE_PRESSED:
				if(gameStatus == GameResource.STATUS_GAMEOVER){					
					vBullets.clear();
					vEnemys.clear();
					vClouds.clear();
					vItems.clear();
					vExplodes.clear();
					
					initGameObject();
				}
				
				cnt = 0;
				gameStatus = GameResource.STATUS_STARTGAME;
				
				break;
			default:
				break;
			}
		}else if(gameStatus == GameResource.STATUS_RUNNINGAME && players[0].state == GameResource.STATE_RUN || players[0].state == GameResource.STATE_INVINCIBLE){
			
			switch (key) {
			case GameResource.UP_PRESSED:
				if(checkMapRange(players[0].x, players[0].y - players[0].speed, players[0].width, players[0].height)){
					//players[0].y -= players[0].speed;
					players[0].move(players[0].x, players[0].y - players[0].speed);
				}
				
				players[0].type = GameResource.PLAYER_FORWARD;

				break;
			case GameResource.DOWN_PRESSED:				
				if(checkMapRange(players[0].x, players[0].y + players[0].speed, players[0].width, players[0].height)){
					//players[0].y += players[0].speed;
					players[0].move(players[0].x, players[0].y + players[0].speed);
				}
				
				players[0].type = GameResource.PLAYER_FORWARD;
				
				break;
			case GameResource.RIGHT_PRESSED:
				if(checkMapRange(players[0].x + players[0].speed, players[0].y, players[0].width, players[0].height)){
					//players[0].x += players[0].speed;
					players[0].move(players[0].x + players[0].speed, players[0].y);
				}
				
				players[0].type = GameResource.PLAYER_FORWARD;
				
				break;
			case GameResource.LEFT_PRESSED:
				if(checkMapRange(players[0].x - players[0].speed , players[0].y, players[0].width, players[0].height)){
					//players[0].x -= players[0].speed;
					players[0].move(players[0].x - players[0].speed, players[0].y);
				}
				
				players[0].type = GameResource.PLAYER_BACKWARD;
				
				break;
			case GameResource.FIRE_PRESSED:
				players[0].type = GameResource.PLAYER_FIRE;
				
				break;
			case GameResource.UP_PRESSED | GameResource.LEFT_PRESSED:
				if(checkMapRange(players[0].x - players[0].speed, players[0].y - players[0].speed, players[0].width, players[0].height)){
					//players[0].x -= players[0].speed;
					//players[0].y -= players[0].speed;
					
					players[0].move(players[0].x - players[0].speed, players[0].y - players[0].speed);
				}
				
				players[0].type = GameResource.PLAYER_BACKWARD;
				
				break;
			case GameResource.UP_PRESSED | GameResource.RIGHT_PRESSED:
				if(checkMapRange(players[0].x + players[0].speed, players[0].y - players[0].speed, players[0].width, players[0].height)){
					//players[0].x += players[0].speed;
					//players[0].y -= players[0].speed;
					
					players[0].move(players[0].x + players[0].speed, players[0].y - players[0].speed);
				}
				
				players[0].type = GameResource.PLAYER_FORWARD;

				break;
			case GameResource.DOWN_PRESSED | GameResource.LEFT_PRESSED:
				if(checkMapRange(players[0].x - players[0].speed, players[0].y + players[0].speed, players[0].width, players[0].height)){
					//players[0].x -= players[0].speed;
					//players[0].y += players[0].speed;
					
					players[0].move(players[0].x - players[0].speed, players[0].y + players[0].speed);
				}
				
				players[0].type = GameResource.PLAYER_BACKWARD;
				
				break;
			case GameResource.DOWN_PRESSED | GameResource.RIGHT_PRESSED:
				if(checkMapRange(players[0].x + players[0].speed, players[0].y + players[0].speed, players[0].width, players[0].height)){
					//players[0].x += players[0].speed;
					//players[0].y += players[0].speed;
					
					players[0].move(players[0].x + players[0].speed, players[0].y + players[0].speed);
				}
				
				players[0].type = GameResource.PLAYER_FORWARD;
				
				break;
			case GameResource.UP_PRESSED | GameResource.FIRE_PRESSED:
				if(checkMapRange(players[0].x , players[0].y - players[0].speed, players[0].width, players[0].height)){
					//players[0].y -= players[0].speed;
					
					players[0].move(players[0].x, players[0].y - players[0].speed);
				}
				
				players[0].type = GameResource.PLAYER_FIRE;
				
				break;
			case GameResource.DOWN_PRESSED | GameResource.FIRE_PRESSED:
				if(checkMapRange(players[0].x, players[0].y + players[0].speed, players[0].width, players[0].height)){
					//players[0].y += players[0].speed;
					
					players[0].move(players[0].x, players[0].y + players[0].speed);
				}
				
				players[0].type = GameResource.PLAYER_FIRE;
				
				break;
			case GameResource.LEFT_PRESSED | GameResource.FIRE_PRESSED:
				if(checkMapRange(players[0].x - players[0].speed, players[0].y, players[0].width, players[0].height)){
					//players[0].x -= players[0].speed;
					
					players[0].move(players[0].x - players[0].speed, players[0].y);
				}
				
				players[0].type = GameResource.PLAYER_FIRE;
				
				break;
			case GameResource.RIGHT_PRESSED | GameResource.FIRE_PRESSED:
				if(checkMapRange(players[0].x + players[0].speed, players[0].y, players[0].width, players[0].height)){
					//players[0].x += players[0].speed;
					
					players[0].move(players[0].x + players[0].speed, players[0].y);
				}
				
				players[0].type = GameResource.PLAYER_FIRE;
				
				break;
			case GameResource.UP_PRESSED | GameResource.LEFT_PRESSED | GameResource.FIRE_PRESSED:
				if(checkMapRange(players[0].x - players[0].speed, players[0].y - players[0].speed, players[0].width, players[0].height)){
					//players[0].x -= players[0].speed;
					//players[0].y -= players[0].speed;
					
					players[0].move(players[0].x - players[0].speed, players[0].y - players[0].speed);
				}
				
				players[0].type = GameResource.PLAYER_FIRE;
				
				break;
			case GameResource.UP_PRESSED | GameResource.RIGHT_PRESSED | GameResource.FIRE_PRESSED:
				if(checkMapRange(players[0].x + players[0].speed, players[0].y - players[0].speed, players[0].width, players[0].height)){
					//players[0].x += players[0].speed;
					//players[0].y -= players[0].speed;
					
					players[0].move(players[0].x + players[0].speed, players[0].y - players[0].speed);
				}
				
				players[0].type = GameResource.PLAYER_FIRE;
				
				break;
			case GameResource.DOWN_PRESSED | GameResource.LEFT_PRESSED | GameResource.FIRE_PRESSED:
				if(checkMapRange(players[0].x - players[0].speed, players[0].y + players[0].speed, players[0].width, players[0].height)){
					//players[0].x -= players[0].speed;
					//players[0].y += players[0].speed;
					
					players[0].move(players[0].x - players[0].speed, players[0].y + players[0].speed);
				}
				
				players[0].type = GameResource.PLAYER_FIRE;
				
				break;
			case GameResource.DOWN_PRESSED | GameResource.RIGHT_PRESSED | GameResource.FIRE_PRESSED:
				if(checkMapRange(players[0].x + players[0].speed, players[0].y + players[0].speed, players[0].width, players[0].height)){
					//players[0].x += players[0].speed;
					//players[0].y += players[0].speed;
					
					players[0].move(players[0].x + players[0].speed, players[0].y + players[0].speed);
				}
				
				players[0].type = GameResource.PLAYER_FIRE;
				
				break;
			default:
				key = 0;
				players[0].type = GameResource.PLAYER_HOLDON;
				
				break;
			}
		}
	}
	
	class GameKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				// player.mY -= player.mSpeed;
				key |= GameResource.UP_PRESSED;
				break;
			case KeyEvent.VK_DOWN:
				// player.mY += player.mSpeed;
				key |= GameResource.DOWN_PRESSED;
				break;
			case KeyEvent.VK_RIGHT:
				// player.mX += player.mSpeed;
				key |= GameResource.RIGHT_PRESSED;
				break;
			case KeyEvent.VK_LEFT:
				// player.mX -= player.mSpeed;
				key |= GameResource.LEFT_PRESSED;
				break;
			case KeyEvent.VK_SPACE:
				key |= GameResource.FIRE_PRESSED;
				break;
			default:
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				// player.mY -= player.mSpeed;
				key &= ~GameResource.UP_PRESSED;
				break;
			case KeyEvent.VK_DOWN:
				// player.mY += player.mSpeed;
				key &= ~GameResource.DOWN_PRESSED;
				break;
			case KeyEvent.VK_RIGHT:
				// player.mX += player.mSpeed;
				key &= ~GameResource.RIGHT_PRESSED;
				break;
			case KeyEvent.VK_LEFT:
				// player.mX -= player.mSpeed;
				key &= ~GameResource.LEFT_PRESSED;
				break;
			case KeyEvent.VK_SPACE:
				key &= ~GameResource.FIRE_PRESSED;
				break;
			default:
				break;
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
		}
	}
}
