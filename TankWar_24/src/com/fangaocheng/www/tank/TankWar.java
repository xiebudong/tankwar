package com.fangaocheng.www.tank;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.JFrame;


import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

/*
 * 坦克大战游戏
 */

public class TankWar extends JFrame {

	private static TankWar tankWar;
	
	// 保存屏幕高度和宽度
	public static final int SCREEN_WIDTH=Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int SCREEN_HEIGHT=Toolkit.getDefaultToolkit().getScreenSize().height;
	// 游戏界面大小
	public static final int GAME_WIDTH=800;
	public static final int GAME_HEIGHT=600;
	
	// 整体界面的刷新频率，值越小，刷新越快，界面里的元素动的越快
	public static final int FLUSH_RATE=50;
	
	// 开始游戏标志
	private boolean startGameFlag=false;
	
	private String ip;
	
	// 背景图片
	private static Image bgImage;
	
	// 坦克类
	// 两辆好坦克
	private static Tank goodTank1;
	private static Tank goodTank2;
	// 一批坏坦克
	public static List<Tank> badTanks = new ArrayList<Tank>();
	
	public Sound  sound=new Sound();
	
	//炮弹
	List<Missile>  missiles=new ArrayList<Missile>();
	
	// 爆炸
	List<Explode>  explodes=new ArrayList<Explode>();
	
	// 老窝
	public Home home=new Home(379, 560, true);
	
	// 保存所有普通墙体
	public List<CommonWall> commonWalls = new ArrayList<CommonWall>();
	
	// 金刚墙
	List<MetalWall> metalWalls = new ArrayList<MetalWall>();
	
	// 河流
	List<River> riverWalls = new ArrayList<River>();
	
	// 草丛
	List<Grass> grassWalls=new ArrayList<Grass>();
	
	// 服务端或客户端线程
	private NetThread netThread=null;
	// 网络输出流
	public  PrintWriter output=null;

	// 指示是客户端还是服务器
	public static String clientOrServer="none";
	
	// 加载图片
	static
	{
		bgImage=Toolkit.getDefaultToolkit().getImage("images\\map.png");
	}
	
	public TankWar(String ip)
	{
		this.ip=ip;
		this.setTitle("TankWar by FGC");
		this.setLocation((SCREEN_WIDTH-GAME_WIDTH)/2, (SCREEN_HEIGHT-GAME_HEIGHT)/2);
		this.setSize(GAME_WIDTH,GAME_HEIGHT);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		this.addKeyListener(new MyKeyListener());
		
		goodTank1 = new Tank(true, 'S', 'U', 469, 550, this, true, 0, clientOrServer);  // 按键控制
		goodTank2 = new Tank(true, 'S', 'U', 291, 550, this, false, 0, clientOrServer);   // 网络控制
		
		// 加入坏坦克
		badTanks.add(new Tank(false, 'D', 'D', 0, 0, this, true, 1, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 100, 0, this, true, 2, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 200, 0, this, true, 3, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 300, 0, this, true, 4, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 400, 0, this, true, 5, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 500, 0, this, true, 6, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 600, 0, this, true, 7, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 700, 0, this, true, 8, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 800, 0, this, true, 9, clientOrServer));
		
		// 加入普通墙体(老窝)
		commonWalls.add(new CommonWall(359, 580, true));
		commonWalls.add(new CommonWall(339, 580, true));
		commonWalls.add(new CommonWall(359, 560, true));
		commonWalls.add(new CommonWall(339, 560, true));
		
		commonWalls.add(new CommonWall(359, 540, true));
		commonWalls.add(new CommonWall(339, 540, true));
		commonWalls.add(new CommonWall(379, 540, true));
		commonWalls.add(new CommonWall(399, 540, true));
		commonWalls.add(new CommonWall(419, 540, true));
		commonWalls.add(new CommonWall(439, 540, true));
		
		commonWalls.add(new CommonWall(419, 560, true));
		commonWalls.add(new CommonWall(439, 560, true));
		commonWalls.add(new CommonWall(419, 580, true));
		commonWalls.add(new CommonWall(439, 580, true));
		
		// 墙
		for (int i = 0; i < 40; i++) 
		{
			commonWalls.add(new CommonWall(2+i*20, 150, true));
			commonWalls.add(new CommonWall(2+i*20, 170, true));
		}
		
		for (int i = 0; i < 6; i++) 
		{
			commonWalls.add(new CommonWall(288, 190+i*20, true));
			commonWalls.add(new CommonWall(308, 190+i*20, true));
		}
		
		for (int i = 0; i < 6; i++) 
		{
			commonWalls.add(new CommonWall(472, 190+i*20, true));
			commonWalls.add(new CommonWall(492, 190+i*20, true));
		}
		
		//添加金刚墙
//		for (int i = 0; i < 20; i++) 
//		{
//			metalWalls.add(new MetalWall(2+i*20, 300));
//			metalWalls.add(new MetalWall(2+i*20, 320));
//		}
		
		metalWalls.add(new MetalWall(2, 270));
		metalWalls.add(new MetalWall(612, 270));
		
		// 添加河流
		for (int i = 0; i < 10; i++) 
		{
			riverWalls.add(new River(2+i*20, 390));
			riverWalls.add(new River(2+i*20, 410));
		}
		
		for (int i = 0; i < 10; i++) 
		{
			riverWalls.add(new River(600+i*20, 390));
			riverWalls.add(new River(600+i*20, 410));
		}
		
		// 添加草地
		for (int i = 0; i < 20; i++) 
		{
			grassWalls.add(new Grass(202+i*20, 390));
			grassWalls.add(new Grass(202+i*20, 410));
		}
		
		new FlushThread().start();
	}

	@Override
	public void paint(Graphics g2) {
		// 双缓冲技术
		// 创建一个带有缓冲区的画笔
		BufferedImage   bg=new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics  g=bg.createGraphics();
		g.drawImage(bgImage, 0, 0, this);
		
		goodTank1.drawTank(g);
		goodTank2.drawTank(g);
		
		if (startGameFlag == true)
		{
			goodTank1.ramTanks(badTanks);
		}
		
		goodTank1.ramWalls(commonWalls);
		goodTank1.ramMetalWalls(metalWalls);
		goodTank1.ramRivers(riverWalls);
		
		// 开始游戏标志位true后才会出现坏坦克
		if (startGameFlag == true)
		{
			// 绘制若干坏坦克
			for (int i = 0; i < badTanks.size(); i++) {
				Tank badTank = badTanks.get(i);
				if (badTank != null)
				{
					badTank.drawTank(g);
					if (clientOrServer.equals("server"))            // 如果是客户端的话，也就是说坏坦克是 被网络控制的，
					{                                               // 则不能进行碰撞检测，因为服务端已经检测过了,
						badTank.ramTanks(badTanks);                 // 否则的话会出现不能同步的现象（貌似是出现了临界现象）
						// 坏坦克撞墙
						badTank.ramWalls(commonWalls);
						// 撞击金刚墙
						badTank.ramMetalWalls(metalWalls);
						// 撞河流
						badTank.ramRivers(riverWalls);
					}
				}
			}
		}
		
		// 绘制河流
		if (riverWalls.size() > 0)
		{
			for (int i=0; i<riverWalls.size(); i++)
			{
				River r = riverWalls.get(i);
				if (r != null)
				{
					r.drawRiver(g);
				}
			}
		}
		
		//绘制炮弹
		if(missiles.size()>0){
			for (int i = 0; i < missiles.size(); i++) {
				Missile  missile=missiles.get(i);
				if(missile!=null)
				{
					missile.drawMissile(g);
					
					if (startGameFlag == true)
					{
						// 打坦克
						missile.hitTanks(badTanks);
						missile.hitTank(goodTank1);
						missile.hitTank(goodTank2);
						// 炮弹打击老窝
						missile.hitHome(home);
						// 炮弹打击所有墙体
						missile.hitWalls(commonWalls);
						// 炮弹打击所有的金刚墙
						missile.hitMetals(metalWalls);
					}
				}
			}
		}

		if(explodes.size()>0){
			
			for (int i = 0; i < explodes.size(); i++) {
				Explode  explode=explodes.get(i);
				if(explode!=null){
					explode.drawExplode(g);
				}
			}
		}
		
		// 绘制老窝
		home.drawHome(g);
		
		// 绘制所有普通墙体
		if (commonWalls.size()>0)
		{
			for (int i=0; i<commonWalls.size(); i++)
			{
				CommonWall cw=commonWalls.get(i);
				if (cw != null)
				{
					cw.drawCommonWall(g);
				}
			}
		}
		
		// 绘制金刚墙
		if (metalWalls.size()>0)
		{
			for (int i = 0; i < metalWalls.size(); i++)
			{
				MetalWall w=metalWalls.get(i);
				if (w != null)
				{
					w.drawMetalWall(g);
				}
			}
		}
		
		// 绘制草地
		if (grassWalls.size() > 0)
		{
			for (int i=0; i<grassWalls.size(); i++)
			{
				Grass grass=grassWalls.get(i);
				if (grass != null)
				{
					grass.drawGrass(g);
				}
			}
		}
		
		//显示游戏信息
		// 剩余血量
		g.drawString("我的剩余血量:", 4, 50);
		g.drawString("战友剩余血量:", 4, 68);
		g.setColor(Color.RED);
		g.drawRect(85, 40, 200, 10);
		g.fillRect(85, 40, (int)((goodTank1.life/100.0)*200), 10);
		g.drawRect(85, 58, 200, 10);
		g.fillRect(85, 58, (int)((goodTank2.life/100.0)*200), 10);
		g.setColor(Color.WHITE);
//		g.drawString("炮弹的数量:"+missiles.size(), 4, 42);
		//剩余敌军个数
		g.drawString("敌军坦克:           "+badTanks.size()+"个", 4, 87);
		
		// 用原来的画笔统一绘制
		g2.drawImage(bg, 0, 0, this);
	}
	
	class MyKeyListener extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e) {
			
			goodTank1.keyPressed(e);
		}
		
		public void keyReleased(KeyEvent e) {
			
			goodTank1.keyReleased(e);
			// 获得键码
			int  keyCode = e.getKeyCode();
			
			
			if (keyCode == KeyEvent.VK_F5)
			{	
				if (clientOrServer.equals("server"))
				{
					startSend("badstart");     // 使能网络另一端的坏坦克
					startGameFlag=true;
				}
			}
/*			
			// 按F5，则作为客户端
			if (keyCode == KeyEvent.VK_F5)
			{
				if (netThread == null)
				{
	//				goodTank1.setX(480);
	//				goodTank2.setX(280);
					// 网络线程的调用必须按照下面这个顺序来执行
					netThread=new NetThread(tankWar);
					netThread.setClient(true);   // 设置为客户端
					System.out.println("你已选择作为客户端!");
					clientOrServer="client";
					goodTank1.setClientOrServer(clientOrServer);
					goodTank2.setClientOrServer(clientOrServer);
					for (int i = 0; i < badTanks.size(); i++)
					{
						badTanks.get(i).setClientOrServer(clientOrServer);
					}
					
					netThread.start();
				}
			
				// 按F6，则作为服务端
			}
			
			if (keyCode == KeyEvent.VK_F6)
			{
				
				if (netThread == null)
				{
					goodTank1.setX(291);
					goodTank2.setX(469);
					netThread=new NetThread(tankWar);
					netThread.setClient(false);                 // 设置为服务端
					System.out.println("你已选择作为服务端!");
					clientOrServer="server";
					goodTank1.setClientOrServer(clientOrServer);
					goodTank2.setClientOrServer(clientOrServer);
					for (int i = 0; i < badTanks.size(); i++)
					{
						badTanks.get(i).setClientOrServer(clientOrServer);
					}
					
					netThread.start();
				}
			}
			*/
		}
	}
	
	// 下面这两个函数是实现上面的按键监听的F5和F6的功能
	// 本类被声明完后要紧接着调用这两个函数之一来选择服务端模式还是客户端模式
	public void keyF6()
	{
		if (netThread == null)
		{
//				goodTank1.setX(480);
//				goodTank2.setX(280);
			// 网络线程的调用必须按照下面这个顺序来执行
			netThread=new NetThread(this, ip);
			netThread.setClient(true);   // 设置为客户端
			System.out.println("你已选择作为客户端!");
			clientOrServer="client";
			goodTank1.setClientOrServer(clientOrServer);
			goodTank2.setClientOrServer(clientOrServer);
			for (int i = 0; i < badTanks.size(); i++)
			{
				badTanks.get(i).setClientOrServer(clientOrServer);
			}
			
			netThread.start();
		}
	}
	
	public void keyF7()
	{
		if (netThread == null)
		{
			goodTank1.setX(291);
			goodTank2.setX(469);
			netThread=new NetThread(this, ip);
			netThread.setClient(false);                 // 设置为服务端
			System.out.println("你已选择作为服务端!");
			clientOrServer="server";
			goodTank1.setClientOrServer(clientOrServer);
			goodTank2.setClientOrServer(clientOrServer);
			for (int i = 0; i < badTanks.size(); i++)
			{
				badTanks.get(i).setClientOrServer(clientOrServer);
			}
			
			netThread.start();
		}
	}
	
	//内部类(线程)，完成对屏幕的刷新
	class  FlushThread  extends  Thread{

		@Override
		public void run() {
			
			while(true){
				
				try {
					Thread.sleep(FLUSH_RATE);                       // 整体界面的刷新频率
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//刷屏幕
				repaint();
			}
		}
		
	}
	

	// 该函数被坦克类调用
	public void startSend(String direc)
	{
		if (netThread != null)
		{
			output=netThread.getNetOutputStream();
			if (output != null)
			{
				output.println(direc);
			}
		}
	}
	
	// 该函数被NetThread类调用
	public void netReceiveProcess(String direc)
	{	
//		System.out.println(direc);
		
		// 这个判断语句是对称的，也就是说在服务端和客户端他发挥的作用是一样的
		if (direc.equals("up") || direc.equals("down") || direc.equals("left") || direc.equals("right") || direc.equals("stop"))
		{
			goodTank2.updateTank(direc);
		}
		
		if (direc.startsWith("D") && direc.endsWith("D"))
		{
			String temp[] = direc.split("D");
			if (temp.length == 3)
			{
				int x = Integer.parseInt(temp[2]);
				
				for (int i = 0; i < badTanks.size(); i++) {
					Tank badTank = badTanks.get(i);
					if (badTank.number == x)
					{
						badTank.updateBadTankDirection(temp[1]);
					}
				}	
			}
		}
		
		if (direc.startsWith("#") && direc.endsWith("#"))
		{
			String temp[] = direc.split("#");
			if (temp.length == 4)
			{
				int x = Integer.parseInt(temp[1]);
				int y = Integer.parseInt(temp[2]);
				int n = Integer.parseInt(temp[3]);
				if (n == 0)
				{
					goodTank2.setX(x);
					goodTank2.setY(y);
				}
				else
				{
					for (int i = 0; i < badTanks.size(); i++) {
						Tank badTank = badTanks.get(i);
						if (badTank.number == n)
						{
							badTank.updateBadTankPosition(x, y);
						}
					}	
				}
			}
		}
		else if (direc.startsWith("M") && direc.endsWith("M"))
		{
			String temp[] = direc.split("M");
			if (temp.length == 2)
			{
				int x = Integer.parseInt(temp[1]);
				if (x == 0)
				{
					// 网络控制好坦克发弹
					goodTank2.updateMissile();
					
				}
				else
				{
					// 网络控制坏坦克发弹
					for (int i = 0; i < badTanks.size(); i++) {
						Tank badTank = badTanks.get(i);
						if (badTank.number == x)
						{
							badTank.updateBadTankMissile();
						}
					}					
				}
			}
		}
		else if (direc.startsWith("B") && direc.endsWith("B"))
		{
			String temp[] = direc.split("B");
			if (temp.length == 2)
			{
				int x = Integer.parseInt(temp[1]);
				if (x == 0)
				{
					// 网络控制好坦克爆炸
					goodTank2.bomb();
				}
				else if (x == 100)
				{
					goodTank1.bomb();
				}
				else
				{
					// 网络控制坏坦克爆炸
					for (int i = 0; i < badTanks.size(); i++) {
						Tank badTank = badTanks.get(i);
						if (badTank.number == x)
						{
							badTank.bomb();
						}
					}
				}
				
			}
		}
		else if (direc.startsWith("L1") && direc.endsWith("L1"))
		{
			String temp[] = direc.split("L1");
			if (temp.length == 2)
			{
				int x = Integer.parseInt(temp[1]);
				goodTank2.life=x;
				if (x == 0)
				{
					goodTank2.live=false;
				}
			}
		}
		else if (direc.startsWith("L2") && direc.endsWith("L2"))
		{
			String temp[] = direc.split("L2");
			if (temp.length == 2)
			{
				int x = Integer.parseInt(temp[1]);
				goodTank1.life=x;
				if (x == 0)
				{
					goodTank1.live=false;
				}
			}
		}
		else
		{
			
		}
		
		if (direc.equals("badstart"))
		{
			startGameFlag=true;
		}
	}
}




















