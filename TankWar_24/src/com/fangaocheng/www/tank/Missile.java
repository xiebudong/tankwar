package com.fangaocheng.www.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;


/**
 * �����ڵ�����
 * @author Administrator
 *
 */
public class Missile {
	
	private static Image  missileImage;
	//�ڵ��Ĵ�С
	public static final int  MISSILE_SISE=10;
	//λ��
	private int  x;
	private int  y;
	// ����
	private String direction;
	// �ڵ����ƶ��ٶ�
	private static final  int   MISSILE_SPEED=20;
	
	private TankWar tankWar;
	
	//����һ����ʾ�ڵ������ı���
	boolean  live=false;
	
	// �ڵ��û�
	private boolean goodOrBadMissile;
	
	static{
		missileImage=Toolkit.getDefaultToolkit().getImage("images\\missile.png");
	}
	
	public Missile()
	{
		
	}
	
	public Missile(int x, int y, String direction, TankWar tankWar, boolean goodOrBadMissile)
	{
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.tankWar=tankWar;
		this.live=true;
		this.goodOrBadMissile=goodOrBadMissile;
	}
	
	//���Ƶķ���
	public void  drawMissile(Graphics  g){
		if(!live)
		{
			return;
		}
		g.drawImage(missileImage, x, y, null);
		this.missileMove();
	}
	
	//����һ���ڵ��ƶ��ķ���
	public void  missileMove(){
		
		if (direction.equals("up"))
		{
			y-=MISSILE_SPEED;
		}
		else if (direction.equals("down"))
		{
			y+=MISSILE_SPEED;
		}
		else if (direction.equals("left"))
		{
			x-=MISSILE_SPEED;
		}
		else if (direction.equals("right"))
		{
			x+=MISSILE_SPEED;
		}
		else
		{
			
		}
		
		//�����ڵ�Խ������
		if(x<0||x>tankWar.GAME_WIDTH||y<0||y>tankWar.GAME_HEIGHT){
			live=false;
			tankWar.missiles.remove(this);
		}
		
		
	}
	
	
	// ��õ�ǰ�ڵ��ľ��ζ���
	public  Rectangle  getRec(){
		return new Rectangle(x, y, MISSILE_SISE, MISSILE_SISE);
	}
	
	// ��̹�˷���
	public boolean  hitTank(Tank  tank){
		
		// ʹ����ײ���
		if(live && tank.live && getRec().intersects(tank.getRec()) && goodOrBadMissile != tank.goodOrBad){
			// �ڵ�����̹��,������
			live=false;
			//�Ӽ������Ƴ�
			tankWar.missiles.remove(this);
			// ̹������
//			tank.live=false;
			// ���� ��ը
			if (tank.number == 0)   // ����Ǻ�̹�˱�ը�Ļ�  
			{
				// ���tank.iflag�Ƚ����⣬����Ϊ�˱�֤��̹�˱���һ��ֻ��һ��Ѫ������ֻ��ըһ��,�൱��һ��������,
				// �ڱ�������Tank���е�bomb������һ��ִֻ��һ��
				tank.iflag++;
				if (tankWar.output == null)
				{
					tank.iflag=1;
				}
				if (tank.iflag%2 != 0)
				{
					tank.life=tank.life-20;	
				}
				
				if (tank.life == 0)
				{
					tank.live=false;
				}
				
				if (tank.keyOrNet)     // ����ǰ������Ƶĺ�̹�˱�ը�ˣ���ֻ����������һ�˵�goodTank2��ը
				{
					tankWar.startSend("B"+tank.number+"B");
				}
				else       // ����Ǳ�������Ƶ�goodTank2��ը�ˣ���Ҫ������Ҫ����������һ�˵İ������Ƶ�goodTank1��ը
				{
					tankWar.startSend("B"+100+"B");       // 100�Ǳ��Ҫ��ը��̹���Ǳ����������Ʒ���̹��
				}
				if (tank.iflag%2 != 0)
				{
					tankWar.explodes.add(new Explode(tank.getX()-5, tank.getY()-5, tankWar, true));  // ȷ����ը�����ڻ��е�̹�����ڵ�λ��
				}
			}
			else     // ����ǻ�̹�˱�ը�Ļ�
			{
				tank.live=false;
				tankWar.startSend("B"+tank.number+"B");
				tankWar.explodes.add(new Explode(tank.getX()-5, tank.getY()-5, tankWar, true));  // ȷ����ը�����ڻ��е�̹�����ڵ�λ��
				tankWar.badTanks.remove(tank);       // ���remove��Tank���bomb������Ļ�̹����ҲҪ�У�����Ļ����ߵ�ʣ��̹����ʽ�᲻ͬ��
			}
			
			return true;
		}
		return false;
	}
	

	// �������̹�˵ķ���
	public boolean hitTanks(List<Tank> tanks)
	{
		if (tanks.size() > 0)
		{
			for (int i=0 ; i<tanks.size(); i++)
			{
				Tank  tank = tanks.get(i);
				if (tank != null)
				{
					// �����ǰ̹��
					if (hitTank(tank))
					{
						// ����̹��
						return true;
					}
				}
			}
		}
		
		return false; 
	}
	
	// �ڴ�����
	public boolean hitHome(Home home)
	{
		if (this.live && home.live && this.getRec().intersects(home.getRec()))
		{
			// �ڵ���������
			// �ڵ�����
			this.live=false;
			tankWar.missiles.remove(this);
			// ��������
			home.live=false;
			// ��ը
			tankWar.explodes.add(new Explode(home.getX()-45, home.getY()-45, tankWar, false));     
			return true;
		}
		return false;
	}
	
	// ����һ���ڵ������ͨǽ��ķ���
	public boolean hitWall(CommonWall w)
	{
		if (this.live && w.live && this.getRec().intersects(w.getRec()))
		{
			// �ڵ�����ǽ��
			this.live=false;
			tankWar.missiles.remove(this);
			//ǽ������
			w.live=false;
			tankWar.commonWalls.remove(w);
			//����һ����ը
//			tankWar.explodes.add(new Explode(w.getX()-15, w.getY()-15, tankWar));         // ��ǽ���ǲ�������ըΪ��
			
			return true;
		}
		
		return false;
	}
	
	// ����һ���ڵ��������ǽ��ķ���
	public  boolean hitWalls(List<CommonWall> ws)
	{
		if (ws.size() > 0)
		{
			for (int i = 0; i < ws.size(); i++) 
			{
				CommonWall w=ws.get(i);
				if (this.hitWall(w))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	// ����һ��������ǽ�ķ���
	public  boolean hitMetalWall(MetalWall w)
	{
		if (this.live && this.getRec().intersects(w.getRect()))
		{
			// �ڵ����н��ǽ
			this.live=false;
			tankWar.missiles.remove(this);
			tankWar.explodes.add(new Explode(x-23, y-23, tankWar, true));
			return true;
		}
		return false;
	}
	
	public boolean hitMetals(List<MetalWall> ws)
	{
		if (ws.size() > 0)
		{
			for (int i = 0; i < ws.size(); i++)
			{
				MetalWall w = ws.get(i);
				if (this.hitMetalWall(w))
				{
					return true;
				}
			}
		}
		return false;
	}
}















