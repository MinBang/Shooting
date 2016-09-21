import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;


public class GameScene extends Canvas{
	
	// Frame
	GameFrame frame = null;
	GameResource resource;
	
	// 더블 버퍼링
	Image dblBuf;
	Graphics gc;
	
	public GameScene(GameFrame frame, GameResource resouce){
		this.frame = frame;
		this.resource = resouce;
	}
	
	public void paint(Graphics g){
		if(gc == null){
			dblBuf = createImage(GameResource.gScreenWidth, GameResource.gScreenHeight);
			if(dblBuf == null){
				System.out.println("오프 스크린 버퍼 생성 실패");
			}else{
				gc = dblBuf.getGraphics();
			}
			
			return;
		}
		
		update(g);
	}
	
	public void update(Graphics g){
		if(gc == null){
			return;
		}
		
		dblPaint();
		g.drawImage(dblBuf, 0, 0, this);
	}
	
	public void dblPaint(){
		
		switch(frame.gameStatus){
		case GameResource.STATUS_TITLE:
			drawTitle();
			break;
		case GameResource.STATUS_STARTGAME:
			drawBG(); 
			drawClouds();
			drawPlayers();
			break;
		case GameResource.STATUS_RUNNINGAME:
			drawBG();
			drawClouds();
			drawEnemy();
			drawItem();
			drawBullet();
			drawExplode();
			drawPlayers();
			break;
		case GameResource.STATUS_STOPGAME:
			
			break;
		case GameResource.STATUS_GAMEOVER:
			drawBG();
			drawClouds();
			drawItem();
			drawEnemy();
			drawBullet();
			drawOver();
			
			break;
		default:
			break;
		}
		
		gc.drawString("gameCnt : " + frame.cnt + " life : " + frame.players[0].life, 10, 10);
	}
	
	public void drawTitle(){
		gc.drawImage(resource.getImage(GameResource.TITLE), 0, 0, this);
		
		if((frame.cnt % 20) < 15){
			gc.drawImage(resource.getImage(GameResource.PUSH_TITLE), 120, 320, this);
		}
	}
	
	public void drawOver(){
		gc.drawImage(resource.getImage(GameResource.GAMEOVER), 150, 200, this);
	}
	
	public void drawBG(){
		frame.bg.draw(gc);
	}
	
	public void drawPlayers(){
		for(int i=0;i < frame.players.length;i++){
			
			GamePlayer tPlayer = frame.players[i];
			
			if(tPlayer.isUse == true && tPlayer.state == GameResource.STATE_LOAD){
				if((frame.cnt % 20) < 10){
					//gc.drawImage(player.getmImage(player.getmMotion()), player.getmX(), player.getmY(), this);
					tPlayer.draw(gc);
				}
			}else if(tPlayer.isUse == true && tPlayer.state == GameResource.STATE_RUN){
				tPlayer.draw(gc);
			}else if(tPlayer.isUse == true && tPlayer.state == GameResource.STATE_ILL){
				if((frame.cnt % 20) < 10){
					tPlayer.draw(gc);
				}
			}else if(tPlayer.isUse == true && tPlayer.state == GameResource.STATE_INVINCIBLE){
				if((frame.cnt % 20) < 10){
					tPlayer.draw(gc);
				}
			}
		}
	}
	
	public void drawClouds(){
		for(int i=0;i < frame.vClouds.size();i++){
			
			GameCloud tCloud = frame.vClouds.get(i);
			
			tCloud.draw(gc);
		}
	}
	
	public void drawBullet(){
		for(int i=0;i < frame.vBullets.size();i++){
			frame.vBullets.get(i).draw(gc);
		}
	}
	
	public void drawEnemy(){
		for(int i=0;i < frame.vEnemys.size();i++){
			frame.vEnemys.get(i).draw(gc);			
		}
	}
	
	public void drawItem(){
		for(int i=0;i < frame.vItems.size();i++){
			frame.vItems.get(i).draw(gc);			
		}
	}
	
	public void drawExplode(){
		for(int i=0;i < frame.vExplodes.size();i++){
			frame.vExplodes.get(i).draw(gc);
		}
	}
}
