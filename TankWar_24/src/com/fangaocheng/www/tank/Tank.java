package com.fangaocheng.www.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Tank {
	
	// TankWar���������
	private TankWar tankWar;
	
	// ̹��ͼƬ
	private Image   tankImg;
	
	// ��־���ҷ����ǵз�̹��
	public boolean goodOrBad=true;
	
	// ��־ѡ����Ƿ���˻��ǿͻ���
	private String clientOrServer;
	
	// ��̹�˱��, ��̹�˵Ļ����ֵΪ0
	public int number;
	
	public int life=100;
	
	// ���tank.iflag�Ƚ����⣬����Ϊ�˱�֤��̹�˱���һ��ֻ��һ��Ѫ������ֻ��ըһ��,�൱��һ��������,
	// �ڱ�������Tank���е�bomb������һ��ִֻ��һ��
	public int iflag=0;
	
	// ����һ�������������
	private Random random = new Random();
	
	// ��̹�˻��һ���������󣬳�������������ߵĴ���
	private int randomTemp = random.nextInt(5)+3;
	
	// �����꣬����������ײ֮ǰ��λ��
	int oldX;
	int oldY;
	
	// ��־�ǰ������ݻ�����������
	public boolean keyOrNet;
	
	//����̹�˵Ĵ�С
	private static final  int   TANK_SIZE=37;
	
	// ������4��״̬
	boolean bUp = false, bDown = false, bLeft = false, bRight = false;
	
	// ö�ٱ�ʾ̹�˿��ܵ����з���
	public enum Direction {
		L, U, R, D, STOP
	};
	
	// �ҷ�̹��ͼƬ����
	private static Map<String, Image> goodTank=new HashMap<String, Image>();
	
	// �з�̹�˱���
	private static Map<String, Image> badTank=new HashMap<String, Image>();
	
	// ��ǰ̹�˵�λ����Ϣ
	private int x;
	private int y; 
	
	// ̹���ٶ�
	public static final  int   TANK_SPEED=5;
	
	// ̹������
	public  boolean  live;
	
	// ��������
//	Sound  sound=new Sound();          // �������������һnew���ͻ���ִ��󣬲�֪Ϊ��,�ŵ�������ͺ���
	
	// ��ǰ̹�˵ķ���
	private Direction direction;
	// �����ɿ����а���ʱ����һ�������µķ�������˷�������Ϊ��Ͳ�ķ���
	private Direction ptDirection;
	// �˱���ȷ���ı䷽�����ֹͣʱֻ��������һ�˷���һ������
	private Direction historyDirection;
	
	// ������ַ�����ʾ���������緢��
	private String stringDirection;
	static
	{
		goodTank.put("up", Toolkit.getDefaultToolkit().getImage("images\\goodtankU.png"));
		goodTank.put("down", Toolkit.getDefaultToolkit().getImage("images\\goodtankD.png"));
		goodTank.put("left", Toolkit.getDefaultToolkit().getImage("images\\goodtankL.png"));
		goodTank.put("right", Toolkit.getDefaultToolkit().getImage("images\\goodtankR.png"));
		
		badTank.put("up", Toolkit.getDefaultToolkit().getImage("images\\errortankU.png"));
		badTank.put("down", Toolkit.getDefaultToolkit().getImage("images\\errortankD.png"));
		badTank.put("left", Toolkit.getDefaultToolkit().getImage("images\\errortankL.png"));
		badTank.put("right", Toolkit.getDefaultToolkit().getImage("images\\errortankR.png"));
	}
	
	public Tank()
	{
		tankImg=goodTank.get("up");
	}
	
	
	/*
	 * ���ã����ι�����
	 * ������
	 * 		 goodOrBad: ��־���ҷ�̹�˻��ǵз�̹��
	 * 		 direction: ֻ��4��char��ȡֵ���ֱ�Ϊ'U','D','L','R'���ֱ��ʾ�ϡ��¡�����
	 * 		 x: ��̹�˵�x����
	 *       y: ��̹�˵�y����
	 *       keyOrNet: ָʾ�������ƻ����������
	 */
	public Tank(boolean goodOrBad, char direction, char ptDirection, int x, int y, TankWar tankWar, boolean keyOrNet, int number, String clientOrServer)
	{
		this.goodOrBad=goodOrBad;
		this.x=x;
		this.y=y;
		this.tankWar=tankWar;
		this.keyOrNet=keyOrNet;
		this.number=number;
		this.clientOrServer=clientOrServer;
		this.live=true;
		
		switch (direction)
		{
		case 'U' :  this.direction=Direction.U; break;
		case 'D' :  this.direction=Direction.D; break;
		case 'L' :  this.direction=Direction.L; break;
		case 'R' :  this.direction=Direction.R; break;
		case 'S' :  this.direction=Direction.STOP; break;
		}

		switch (ptDirection)
		{
		case 'U' :  this.ptDirection=Direction.U; break;
		case 'D' :  this.ptDirection=Direction.D; break;
		case 'L' :  this.ptDirection=Direction.L; break;
		case 'R' :  this.ptDirection=Direction.R; break;
		default  :  this.ptDirection=Direction.U; break;
		}
		
		if (goodOrBad)
		{
			switch (ptDirection)
			{
			case 'U' : tankImg=goodTank.get("up");
			case 'D' : tankImg=goodTank.get("down");
			case 'L' : tankImg=goodTank.get("left");
			case 'R' : tankImg=goodTank.get("right");
			}
		}
		else
		{
			switch (ptDirection)
			{
			case 'U' : tankImg=badTank.get("up");
			case 'D' : tankImg=badTank.get("down");
			case 'L' : tankImg=badTank.get("left");
			case 'R' : tankImg=badTank.get("right");
			}
		}
	}
	
	/*
	 * ���ã����ݴ����direction�����л���ͬ�����̹��ͼƬ
	 */
	public void swapImage()
	{
		int kk=0;
		if (goodOrBad)
		{
			kk++;
			switch (ptDirection)
			{
			case U : tankImg=goodTank.get("up"); break;
			case D : tankImg=goodTank.get("down"); break;
			case L : tankImg=goodTank.get("left"); break;
			case R : tankImg=goodTank.get("right"); break;
			}
		}
		else
		{
			kk++;
			switch (ptDirection)
			{
			case U : tankImg=badTank.get("up"); break;
			case D : tankImg=badTank.get("down"); break;
			case L : tankImg=badTank.get("left"); break;
			case R : tankImg=badTank.get("right"); break;
			}
		}
	}
	
	public void  drawTank(Graphics  g)
	{
		
		if(!live)
		{			
			return;		
		}
		
		if (clientOrServer.equals("none") || clientOrServer.equals("server"))
		{
			if (keyOrNet)  // ��������
			{
				swapImage();
				g.drawImage(tankImg, x, y, null);
				// ̹���ƶ�
				move();
			}
			else
			{
				swapImage();
				// ƾ�����緢������������ƶ�
				g.drawImage(tankImg, x, y, null);
			}		
		}
		else
		{
			if (goodOrBad && keyOrNet)
			{
				swapImage();
				g.drawImage(tankImg, x, y, null);
				// ̹���ƶ�
				move();
			}
			else
			{
				swapImage();
				// ƾ�����緢������������ƶ�
				g.drawImage(tankImg, x, y, null);
			}			
		}
	}
	
	public void keyPressed(KeyEvent e)  // ͨ���û��İ�������λ��
	{ 
		keyOrNet=true;      // ������ȡ������
		// ��ü���
		int  keyCode = e.getKeyCode();

		switch(keyCode){
		
		case  KeyEvent.VK_W:
		case  KeyEvent.VK_UP:
			bUp=true;
			break;
		case  KeyEvent.VK_S:
		case  KeyEvent.VK_DOWN:
			bDown=true;
			break;
		case  KeyEvent.VK_A:
		case  KeyEvent.VK_LEFT:
			bLeft=true;
			break;		
		case  KeyEvent.VK_D:
		case  KeyEvent.VK_RIGHT:
			bRight=true;
			break;			
		}
		
		// �����ĸ�����Ĳ����ͱ���ȷ��̹�˵ķ���
		locationDirection();
	}
	
	public void locationDirection() {
		if (!bLeft && bUp && !bRight && !bDown)
		{
			direction = Direction.U;
		}
		if (!bLeft && !bUp && !bRight && bDown)
		{
			direction = Direction.D;
		}
		if (bLeft && !bUp && !bRight && !bDown)
		{
			direction = Direction.L;
		}
		if (!bLeft && !bUp && bRight && !bDown)
		{
			direction = Direction.R;
		}
		if (!bLeft && !bUp && !bRight && !bDown)
		{
			direction = Direction.STOP;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		//��ü���
		int keyCode=e.getKeyCode();

		switch(keyCode){
		
		case  KeyEvent.VK_SPACE:
		case  KeyEvent.VK_J:
			
			//����
			if (live)
			{
				tankWar.missiles.add(this.fire());
				tankWar.sound.sendMissileMusic();
				tankWar.startSend("M"+number+"M");         // ͨ�����緢�ͺ�̹�˷������
			}
			break;
		case  KeyEvent.VK_W:
		case  KeyEvent.VK_UP:
			bUp=false;
			break;
		case  KeyEvent.VK_S:
		case  KeyEvent.VK_DOWN:
			bDown=false;
			break;
		case  KeyEvent.VK_A:
		case  KeyEvent.VK_LEFT:
			bLeft=false;
			break;
		case  KeyEvent.VK_D:
		case  KeyEvent.VK_RIGHT:
			bRight=false;
			break;
		}
		
		locationDirection();
	}
	
	public void updateTank(String direc)   // ͨ�����緢�������ݸ���λ��
	{
		
		if (direc.equals("up"))
		{
			ptDirection=Direction.U;
		}
		else if (direc.equals("down"))
		{
			ptDirection=Direction.D;
		}
		else if (direc.equals("left"))
		{
			ptDirection=Direction.L;
		}
		else if (direc.equals("right"))
		{
			ptDirection=Direction.R;
		}
		else if (direc.equals("stop"))
		{
			
		}	
		else
		{
			
		}
		
	}
	
	public Missile  fire()
	{
		//��Ч
//		sound.sendMissileMusic();
		Missile missile=null;
		
		switch(ptDirection){
		case U:
			missile=new Missile(x+14, y-7, "up", tankWar, goodOrBad);
			break;
		case D:
			missile=new Missile(x+13, y+36, "down", tankWar, goodOrBad);
			break;
		case L:
			missile=new Missile(x-8, y+15, "left", tankWar, goodOrBad);
			break;
		case R:
			missile=new Missile(x+35, y+15, "right", tankWar, goodOrBad);
			break;
		}
		
		// �ӵ���һ��ǹ�žͼ��һ�£��Է�ֹ���Ŵ�
		missile.hitWalls(tankWar.commonWalls);
		missile.hitTanks(tankWar.badTanks);
		
		return missile;
	}
	
	public void updateMissile()
	{
		if (live)
		{
			tankWar.missiles.add(this.fire());
		}
	}
	
	// �������ݸ��»�̹��λ��
	public void updateBadTankPosition(int x, int y)
	{	
		oldX=x;
		oldY=y;
		
		this.x=x;
		this.y=y;
	}
	
	// �������ݸ��»�̹�˷���
	public void updateBadTankDirection(String direc)
	{
		if (direc.equals("up"))
		{
			ptDirection=Direction.U;
		}
		else if (direc.equals("down"))
		{
			ptDirection=Direction.D;
		}
		else if (direc.equals("left"))
		{
			ptDirection=Direction.L;
		}
		else if (direc.equals("right"))
		{
			ptDirection=Direction.R;
		}
		else if (direc.equals("stop"))
		{

		}	
		else
		{
			
		}
	}
	
	public void updateBadTankMissile()
	{
		if (live)
		{
			tankWar.missiles.add(this.fire());
		}
	}
	
	public void bomb()
	{

		if (goodOrBad)
		{
			iflag++;
			if (tankWar.output == null)
			{
				iflag=1;
			}
			if (iflag%2 != 0)
			{
				life=life-20;	
			}
			if (life == 0)
			{
				live=false;
			}
			if (iflag%2 != 0)
			{
				tankWar.explodes.add(new Explode(x-5, y-5, tankWar, true));
			}
		}
		else
		{
			live=false;
			tankWar.explodes.add(new Explode(x-5, y-5, tankWar, true));
			tankWar.badTanks.remove(this);   // ���remove��Missile���hitTank������Ļ�̹����ҲҪ�У�����Ļ����ߵ�ʣ��̹����ʽ�᲻ͬ��
		}	
	}
	
	public  void  move(){
	
	// ��������ı�֮ǰ��λ��
	oldX=x;
	oldY=y;
	
	switch (direction) {
	case U:
		y=y-TANK_SPEED;
		break;
	case D:
		y=y+TANK_SPEED;
		break;
	case L:
		x=x-TANK_SPEED;
		break;
	case R:
		x=x+TANK_SPEED;
		break;
	case STOP:
		break;
	}
	
	// ��ptDirection��¼�ɿ�����֮ǰ���Ǹ����򣬲���¼���ڰ��µķ������ptDirection�����Ż����Ǹ������ͼƬ
	if (direction != Direction.STOP) {
		ptDirection = direction;
	}

	
	//����̹��Խ������
	if(x<=2){
		x=2;
	}
	
	if(x>=tankWar.GAME_WIDTH-TANK_SIZE-3){
		x=tankWar.GAME_WIDTH-TANK_SIZE-3;
	}
	
	if(y<=29){
		y=29;
	}
	
	if(y>=tankWar.GAME_HEIGHT-TANK_SIZE-3){
		y=tankWar.GAME_HEIGHT-TANK_SIZE-3;
	}
	
	if (clientOrServer.equals("server"))
	{
		// �û�̹������ı任����
		if (!goodOrBad)
		{
			// ����ı䷽��
//			Direction[] direc = Direction.values();
			if (randomTemp == 0)
			{	int rd;
				rd=random.nextInt(100)+1;
				if (rd<=20)
				{
					direction = Direction.U;
				}
				else if (rd<=40)
				{
					direction = Direction.L;
				}
				else if (rd<=60)
				{
					direction = Direction.R;
				}
				else
				{
					direction = Direction.D;
				}
				
//				direction = direc[random.nextInt(direc.length-1)];
				randomTemp=random.nextInt(15)+4;       
			}
			randomTemp--;
		}	
	
		if (!goodOrBad)
		{
			// ��������Ʒ���Ƶ��
			if (ptDirection == Direction.D)
			{
				if (random.nextInt(50) > 47)                      
				{
					tankWar.startSend("M"+number+"M");         // ͨ�����緢�ͻ�̹�˷������
					tankWar.missiles.add(fire());
				}
			}
			else
			{
				if (random.nextInt(100) > 97)      
				{
					tankWar.startSend("M"+number+"M");         // ͨ�����緢�ͻ�̹�˷������
					tankWar.missiles.add(fire());
				}
			}
		}
		
		tankWar.startSend("#"+x+"#"+y+"#"+number+"#");
	}
	
	// ȷ��̹�˷���ֻ�ڸı��ʱ����һ��
	if (keyOrNet && historyDirection != direction)
	{
		historyDirection=direction;
		switch (direction) {
		case U:
			stringDirection="up";
			break;
		case D:
			stringDirection="down";
			break;
		case L:
			stringDirection="left";
			break;
		case R:
			stringDirection="right";
			break;
		case STOP:
			stringDirection="stop";
			break;
		}
		if (goodOrBad)
		{
			tankWar.startSend(stringDirection);         // ͨ�����緢�Ͱ������
		}
		else
		{
			if (TankWar.clientOrServer.equals("server"))
			{
				tankWar.startSend("D"+stringDirection+"D"+number+"D");
			}
		}
	}
	
	if (goodOrBad)
	{
		tankWar.startSend("#"+x+"#"+y+"#"+number+"#");
	}
	
	}

	// ���̹�˵ľ��ζ���
	public Rectangle  getRec(){
		return new Rectangle(x, y, TANK_SIZE, TANK_SIZE);
	}
	
	// ����̹�˸���̹��
	public boolean ramTank(Tank tank)
	{
		if (live && tank.live && getRec().intersects(tank.getRec()) && this != tank)
		{
			// ̹��ײ��̹����
			
			// ����ײ��������̹�˵�λ�ã��ָ�����ײ֮ǰ�ĸ��Ե�λ��
			goBack();
			tank.goBack();
			
			return true;
		}
		
		return false;
	}
	
	// ײ������̹��
	public boolean ramTanks(List<Tank> tanks)
	{
		if (tanks.size() > 0)
		{
			for (int i = 0; i < tanks.size(); i++) 
			{
				Tank tank = tanks.get(i);
				if (ramTank(tank))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	// ̹��ײǽ����
	public boolean ramWall(CommonWall w)
	{
		if (this.live && w.live && this.getRec().intersects(w.getRec()))
		{
			// ̹��ײǽ����
			this.goBack();
			
			return true;
		}
		
		return false;
	}
	
	// ̹������ײǽ
	public boolean ramWalls(List<CommonWall> ws)
	{
		if (ws.size()>0)
		{
			for (int i = 0; i < ws.size(); i++)
			{
				CommonWall w = ws.get(i);
				
				if (this.ramWall(w))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	// ̹��ײ�����ǽ�ķ���
	public boolean ramMetalWall(MetalWall w)
	{
		if (this.live && this.getRec().intersects(w.getRect()))
		{
			// ̹��ײ���˽��ǽ
			this.goBack();
			return true;
		}
		return false;
	}
	
    // ײ���������ǽ
	public boolean ramMetalWalls(List<MetalWall> ws)
	{
		if (ws.size() > 0)
		{
			for (int i = 0; i < ws.size(); i++) 
			{
				MetalWall w = ws.get(i);
				if (this.ramMetalWall(w))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	// ̹��ײ������
	public boolean ramRiver(River r)
	{
		if (this.live && this.getRec().intersects(r.getRec()))
		{
			// ̹��ײ��������
			this.goBack();
			return true;
		}
		
		return false;
	}
	
	// ̹��ײ����������
	public boolean ramRivers(List<River> rs)
	{
		if (rs.size() > 0)
		{
			for (int i = 0; i < rs.size(); i++) {
				River r = rs.get(i);
				if (this.ramRiver(r))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void goBack()
	{
		x=oldX;
		y=oldY;
		tankWar.startSend("#"+x+"#"+y+"#"+number+"#");            
	}
	
	public void setClientOrServer(String cs)
	{
		clientOrServer=cs;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int x)
	{
		this.x=x;
	}
	
	public void setY(int y)
	{
		this.y=y;
	}
	
}
