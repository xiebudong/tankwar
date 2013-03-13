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
 * ̹�˴�ս��Ϸ
 */

public class TankWar extends JFrame {

	private static TankWar tankWar;
	
	// ������Ļ�߶ȺͿ��
	public static final int SCREEN_WIDTH=Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int SCREEN_HEIGHT=Toolkit.getDefaultToolkit().getScreenSize().height;
	// ��Ϸ�����С
	public static final int GAME_WIDTH=800;
	public static final int GAME_HEIGHT=600;
	
	// ��������ˢ��Ƶ�ʣ�ֵԽС��ˢ��Խ�죬�������Ԫ�ض���Խ��
	public static final int FLUSH_RATE=50;
	
	// ��ʼ��Ϸ��־
	private boolean startGameFlag=false;
	
	private String ip;
	
	// ����ͼƬ
	private static Image bgImage;
	
	// ̹����
	// ������̹��
	private static Tank goodTank1;
	private static Tank goodTank2;
	// һ����̹��
	public static List<Tank> badTanks = new ArrayList<Tank>();
	
	public Sound  sound=new Sound();
	
	//�ڵ�
	List<Missile>  missiles=new ArrayList<Missile>();
	
	// ��ը
	List<Explode>  explodes=new ArrayList<Explode>();
	
	// ����
	public Home home=new Home(379, 560, true);
	
	// ����������ͨǽ��
	public List<CommonWall> commonWalls = new ArrayList<CommonWall>();
	
	// ���ǽ
	List<MetalWall> metalWalls = new ArrayList<MetalWall>();
	
	// ����
	List<River> riverWalls = new ArrayList<River>();
	
	// �ݴ�
	List<Grass> grassWalls=new ArrayList<Grass>();
	
	// ����˻�ͻ����߳�
	private NetThread netThread=null;
	// ���������
	public  PrintWriter output=null;

	// ָʾ�ǿͻ��˻��Ƿ�����
	public static String clientOrServer="none";
	
	// ����ͼƬ
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
		
		goodTank1 = new Tank(true, 'S', 'U', 469, 550, this, true, 0, clientOrServer);  // ��������
		goodTank2 = new Tank(true, 'S', 'U', 291, 550, this, false, 0, clientOrServer);   // �������
		
		// ���뻵̹��
		badTanks.add(new Tank(false, 'D', 'D', 0, 0, this, true, 1, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 100, 0, this, true, 2, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 200, 0, this, true, 3, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 300, 0, this, true, 4, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 400, 0, this, true, 5, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 500, 0, this, true, 6, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 600, 0, this, true, 7, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 700, 0, this, true, 8, clientOrServer));
		badTanks.add(new Tank(false, 'D', 'D', 800, 0, this, true, 9, clientOrServer));
		
		// ������ͨǽ��(����)
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
		
		// ǽ
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
		
		//��ӽ��ǽ
//		for (int i = 0; i < 20; i++) 
//		{
//			metalWalls.add(new MetalWall(2+i*20, 300));
//			metalWalls.add(new MetalWall(2+i*20, 320));
//		}
		
		metalWalls.add(new MetalWall(2, 270));
		metalWalls.add(new MetalWall(612, 270));
		
		// ��Ӻ���
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
		
		// ��Ӳݵ�
		for (int i = 0; i < 20; i++) 
		{
			grassWalls.add(new Grass(202+i*20, 390));
			grassWalls.add(new Grass(202+i*20, 410));
		}
		
		new FlushThread().start();
	}

	@Override
	public void paint(Graphics g2) {
		// ˫���弼��
		// ����һ�����л������Ļ���
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
		
		// ��ʼ��Ϸ��־λtrue��Ż���ֻ�̹��
		if (startGameFlag == true)
		{
			// �������ɻ�̹��
			for (int i = 0; i < badTanks.size(); i++) {
				Tank badTank = badTanks.get(i);
				if (badTank != null)
				{
					badTank.drawTank(g);
					if (clientOrServer.equals("server"))            // ����ǿͻ��˵Ļ���Ҳ����˵��̹���� ��������Ƶģ�
					{                                               // ���ܽ�����ײ��⣬��Ϊ������Ѿ�������,
						badTank.ramTanks(badTanks);                 // ����Ļ�����ֲ���ͬ��������ò���ǳ������ٽ�����
						// ��̹��ײǽ
						badTank.ramWalls(commonWalls);
						// ײ�����ǽ
						badTank.ramMetalWalls(metalWalls);
						// ײ����
						badTank.ramRivers(riverWalls);
					}
				}
			}
		}
		
		// ���ƺ���
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
		
		//�����ڵ�
		if(missiles.size()>0){
			for (int i = 0; i < missiles.size(); i++) {
				Missile  missile=missiles.get(i);
				if(missile!=null)
				{
					missile.drawMissile(g);
					
					if (startGameFlag == true)
					{
						// ��̹��
						missile.hitTanks(badTanks);
						missile.hitTank(goodTank1);
						missile.hitTank(goodTank2);
						// �ڵ��������
						missile.hitHome(home);
						// �ڵ��������ǽ��
						missile.hitWalls(commonWalls);
						// �ڵ�������еĽ��ǽ
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
		
		// ��������
		home.drawHome(g);
		
		// ����������ͨǽ��
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
		
		// ���ƽ��ǽ
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
		
		// ���Ʋݵ�
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
		
		//��ʾ��Ϸ��Ϣ
		// ʣ��Ѫ��
		g.drawString("�ҵ�ʣ��Ѫ��:", 4, 50);
		g.drawString("ս��ʣ��Ѫ��:", 4, 68);
		g.setColor(Color.RED);
		g.drawRect(85, 40, 200, 10);
		g.fillRect(85, 40, (int)((goodTank1.life/100.0)*200), 10);
		g.drawRect(85, 58, 200, 10);
		g.fillRect(85, 58, (int)((goodTank2.life/100.0)*200), 10);
		g.setColor(Color.WHITE);
//		g.drawString("�ڵ�������:"+missiles.size(), 4, 42);
		//ʣ��о�����
		g.drawString("�о�̹��:           "+badTanks.size()+"��", 4, 87);
		
		// ��ԭ���Ļ���ͳһ����
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
			// ��ü���
			int  keyCode = e.getKeyCode();
			
			
			if (keyCode == KeyEvent.VK_F5)
			{	
				if (clientOrServer.equals("server"))
				{
					startSend("badstart");     // ʹ��������һ�˵Ļ�̹��
					startGameFlag=true;
				}
			}
/*			
			// ��F5������Ϊ�ͻ���
			if (keyCode == KeyEvent.VK_F5)
			{
				if (netThread == null)
				{
	//				goodTank1.setX(480);
	//				goodTank2.setX(280);
					// �����̵߳ĵ��ñ��밴���������˳����ִ��
					netThread=new NetThread(tankWar);
					netThread.setClient(true);   // ����Ϊ�ͻ���
					System.out.println("����ѡ����Ϊ�ͻ���!");
					clientOrServer="client";
					goodTank1.setClientOrServer(clientOrServer);
					goodTank2.setClientOrServer(clientOrServer);
					for (int i = 0; i < badTanks.size(); i++)
					{
						badTanks.get(i).setClientOrServer(clientOrServer);
					}
					
					netThread.start();
				}
			
				// ��F6������Ϊ�����
			}
			
			if (keyCode == KeyEvent.VK_F6)
			{
				
				if (netThread == null)
				{
					goodTank1.setX(291);
					goodTank2.setX(469);
					netThread=new NetThread(tankWar);
					netThread.setClient(false);                 // ����Ϊ�����
					System.out.println("����ѡ����Ϊ�����!");
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
	
	// ����������������ʵ������İ���������F5��F6�Ĺ���
	// ���౻�������Ҫ�����ŵ�������������֮һ��ѡ������ģʽ���ǿͻ���ģʽ
	public void keyF6()
	{
		if (netThread == null)
		{
//				goodTank1.setX(480);
//				goodTank2.setX(280);
			// �����̵߳ĵ��ñ��밴���������˳����ִ��
			netThread=new NetThread(this, ip);
			netThread.setClient(true);   // ����Ϊ�ͻ���
			System.out.println("����ѡ����Ϊ�ͻ���!");
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
			netThread.setClient(false);                 // ����Ϊ�����
			System.out.println("����ѡ����Ϊ�����!");
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
	
	//�ڲ���(�߳�)����ɶ���Ļ��ˢ��
	class  FlushThread  extends  Thread{

		@Override
		public void run() {
			
			while(true){
				
				try {
					Thread.sleep(FLUSH_RATE);                       // ��������ˢ��Ƶ��
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//ˢ��Ļ
				repaint();
			}
		}
		
	}
	

	// �ú�����̹�������
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
	
	// �ú�����NetThread�����
	public void netReceiveProcess(String direc)
	{	
//		System.out.println(direc);
		
		// ����ж�����ǶԳƵģ�Ҳ����˵�ڷ���˺Ϳͻ��������ӵ�������һ����
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
					// ������ƺ�̹�˷���
					goodTank2.updateMissile();
					
				}
				else
				{
					// ������ƻ�̹�˷���
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
					// ������ƺ�̹�˱�ը
					goodTank2.bomb();
				}
				else if (x == 100)
				{
					goodTank1.bomb();
				}
				else
				{
					// ������ƻ�̹�˱�ը
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




















