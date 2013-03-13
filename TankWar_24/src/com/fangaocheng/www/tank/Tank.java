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
	
	// TankWar对象的引用
	private TankWar tankWar;
	
	// 坦克图片
	private Image   tankImg;
	
	// 标志是我方还是敌方坦克
	public boolean goodOrBad=true;
	
	// 标志选择的是服务端还是客户端
	private String clientOrServer;
	
	// 坏坦克编号, 好坦克的话这个值为0
	public int number;
	
	public int life=100;
	
	// 这个tank.iflag比较特殊，他是为了保证号坦克被打一次只减一格血，并且只爆炸一次,相当于一个互斥量,
	// 在本函数和Tank类中的bomb函数中一次只执行一个
	public int iflag=0;
	
	// 定义一个随机数产生器
	private Random random = new Random();
	
	// 当坦克获得一个随机方向后，朝着这个方向所走的次数
	private int randomTemp = random.nextInt(5)+3;
	
	// 旧坐标，用来保存相撞之前的位置
	int oldX;
	int oldY;
	
	// 标志是按键数据还是网络数据
	public boolean keyOrNet;
	
	//定义坦克的大小
	private static final  int   TANK_SIZE=37;
	
	// 按键的4个状态
	boolean bUp = false, bDown = false, bLeft = false, bRight = false;
	
	// 枚举表示坦克可能的所有方向
	public enum Direction {
		L, U, R, D, STOP
	};
	
	// 我方坦克图片保存
	private static Map<String, Image> goodTank=new HashMap<String, Image>();
	
	// 敌方坦克保存
	private static Map<String, Image> badTank=new HashMap<String, Image>();
	
	// 当前坦克的位置信息
	private int x;
	private int y; 
	
	// 坦克速度
	public static final  int   TANK_SPEED=5;
	
	// 坦克生死
	public  boolean  live;
	
	// 发炮声音
//	Sound  sound=new Sound();          // 声音对象在这儿一new，就会出现错误，不知为何,放到主类里就好着
	
	// 当前坦克的方向
	private Direction direction;
	// 保存松开所有按键时的上一次所按下的方向键，此方向是作为炮筒的方向
	private Direction ptDirection;
	// 此变量确保改变方向或者停止时只向网络另一端发送一次命令
	private Direction historyDirection;
	
	// 方向的字符串表示，用于网络发送
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
	 * 作用：带参构造器
	 * 参数：
	 * 		 goodOrBad: 标志是我方坦克还是敌方坦克
	 * 		 direction: 只有4个char型取值，分别为'U','D','L','R'，分别表示上、下、左、右
	 * 		 x: 本坦克的x坐标
	 *       y: 本坦克的y坐标
	 *       keyOrNet: 指示按键控制还是网络控制
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
	 * 作用：根据传入的direction参数切换不同方向的坦克图片
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
			if (keyOrNet)  // 按键控制
			{
				swapImage();
				g.drawImage(tankImg, x, y, null);
				// 坦克移动
				move();
			}
			else
			{
				swapImage();
				// 凭借网络发来的坐标进行移动
				g.drawImage(tankImg, x, y, null);
			}		
		}
		else
		{
			if (goodOrBad && keyOrNet)
			{
				swapImage();
				g.drawImage(tankImg, x, y, null);
				// 坦克移动
				move();
			}
			else
			{
				swapImage();
				// 凭借网络发来的坐标进行移动
				g.drawImage(tankImg, x, y, null);
			}			
		}
	}
	
	public void keyPressed(KeyEvent e)  // 通过用户的按键更新位置
	{ 
		keyOrNet=true;      // 按键获取的数据
		// 获得键码
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
		
		// 根据四个方向的布尔型变量确定坦克的方向
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
		//获得键码
		int keyCode=e.getKeyCode();

		switch(keyCode){
		
		case  KeyEvent.VK_SPACE:
		case  KeyEvent.VK_J:
			
			//开火
			if (live)
			{
				tankWar.missiles.add(this.fire());
				tankWar.sound.sendMissileMusic();
				tankWar.startSend("M"+number+"M");         // 通过网络发送好坦克发炮情况
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
	
	public void updateTank(String direc)   // 通过网络发来的数据更新位置
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
		//音效
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
		
		// 子弹刚一出枪膛就检测一下，以防止隔着打
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
	
	// 网络数据更新坏坦克位置
	public void updateBadTankPosition(int x, int y)
	{	
		oldX=x;
		oldY=y;
		
		this.x=x;
		this.y=y;
	}
	
	// 网络数据更新坏坦克方向
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
			tankWar.badTanks.remove(this);   // 这个remove在Missile类的hitTank函数里的坏坦克里也要有，否则的话两边的剩余坦克显式会不同步
		}	
	}
	
	public  void  move(){
	
	// 保存坐标改变之前的位置
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
	
	// 用ptDirection记录松开按键之前的那个方向，并记录正在按下的方向，这个ptDirection决定着绘制那个方向的图片
	if (direction != Direction.STOP) {
		ptDirection = direction;
	}

	
	//处理坦克越界问题
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
		// 让坏坦克随机的变换方向
		if (!goodOrBad)
		{
			// 随机改变方向
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
			// 随机数限制发炮频率
			if (ptDirection == Direction.D)
			{
				if (random.nextInt(50) > 47)                      
				{
					tankWar.startSend("M"+number+"M");         // 通过网络发送坏坦克发炮情况
					tankWar.missiles.add(fire());
				}
			}
			else
			{
				if (random.nextInt(100) > 97)      
				{
					tankWar.startSend("M"+number+"M");         // 通过网络发送坏坦克发炮情况
					tankWar.missiles.add(fire());
				}
			}
		}
		
		tankWar.startSend("#"+x+"#"+y+"#"+number+"#");
	}
	
	// 确保坦克方向只在改变的时候发送一次
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
			tankWar.startSend(stringDirection);         // 通过网络发送按键情况
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

	// 获得坦克的矩形对象
	public Rectangle  getRec(){
		return new Rectangle(x, y, TANK_SIZE, TANK_SIZE);
	}
	
	// 避免坦克覆盖坦克
	public boolean ramTank(Tank tank)
	{
		if (live && tank.live && getRec().intersects(tank.getRec()) && this != tank)
		{
			// 坦克撞到坦克了
			
			// 将相撞的这两个坦克的位置，恢复到相撞之前的各自的位置
			goBack();
			tank.goBack();
			
			return true;
		}
		
		return false;
	}
	
	// 撞击批量坦克
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
	
	// 坦克撞墙方法
	public boolean ramWall(CommonWall w)
	{
		if (this.live && w.live && this.getRec().intersects(w.getRec()))
		{
			// 坦克撞墙上了
			this.goBack();
			
			return true;
		}
		
		return false;
	}
	
	// 坦克批量撞墙
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
	
	// 坦克撞击金刚墙的方法
	public boolean ramMetalWall(MetalWall w)
	{
		if (this.live && this.getRec().intersects(w.getRect()))
		{
			// 坦克撞到了金刚墙
			this.goBack();
			return true;
		}
		return false;
	}
	
    // 撞击批量金刚墙
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
	
	// 坦克撞击河流
	public boolean ramRiver(River r)
	{
		if (this.live && this.getRec().intersects(r.getRec()))
		{
			// 坦克撞到河流了
			this.goBack();
			return true;
		}
		
		return false;
	}
	
	// 坦克撞击批量河流
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
