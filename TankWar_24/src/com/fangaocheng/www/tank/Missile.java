package com.fangaocheng.www.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;


/**
 * 描述炮弹的类
 * @author Administrator
 *
 */
public class Missile {
	
	private static Image  missileImage;
	//炮弹的大小
	public static final int  MISSILE_SISE=10;
	//位置
	private int  x;
	private int  y;
	// 方向
	private String direction;
	// 炮弹的移动速度
	private static final  int   MISSILE_SPEED=20;
	
	private TankWar tankWar;
	
	//定义一个表示炮弹生死的变量
	boolean  live=false;
	
	// 炮弹好坏
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
	
	//绘制的方法
	public void  drawMissile(Graphics  g){
		if(!live)
		{
			return;
		}
		g.drawImage(missileImage, x, y, null);
		this.missileMove();
	}
	
	//定义一个炮弹移动的方法
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
		
		//处理炮弹越界问题
		if(x<0||x>tankWar.GAME_WIDTH||y<0||y>tankWar.GAME_HEIGHT){
			live=false;
			tankWar.missiles.remove(this);
		}
		
		
	}
	
	
	// 获得当前炮弹的矩形对象
	public  Rectangle  getRec(){
		return new Rectangle(x, y, MISSILE_SISE, MISSILE_SISE);
	}
	
	// 打坦克方法
	public boolean  hitTank(Tank  tank){
		
		// 使用碰撞检测
		if(live && tank.live && getRec().intersects(tank.getRec()) && goodOrBadMissile != tank.goodOrBad){
			// 炮弹击中坦克,死掉了
			live=false;
			//从集合中移除
			tankWar.missiles.remove(this);
			// 坦克死了
//			tank.live=false;
			// 发生 爆炸
			if (tank.number == 0)   // 如果是好坦克爆炸的话  
			{
				// 这个tank.iflag比较特殊，他是为了保证号坦克被打一次只减一格血，并且只爆炸一次,相当于一个互斥量,
				// 在本函数和Tank类中的bomb函数中一次只执行一个
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
				
				if (tank.keyOrNet)     // 如果是按键控制的号坦克爆炸了，则只控制网络另一端的goodTank2爆炸
				{
					tankWar.startSend("B"+tank.number+"B");
				}
				else       // 如果是被网络控制的goodTank2爆炸了，则要标明是要控制网络另一端的按键控制的goodTank1爆炸
				{
					tankWar.startSend("B"+100+"B");       // 100是辨别要爆炸的坦克是本机按键控制方的坦克
				}
				if (tank.iflag%2 != 0)
				{
					tankWar.explodes.add(new Explode(tank.getX()-5, tank.getY()-5, tankWar, true));  // 确保爆炸发生在击中的坦克所在的位置
				}
			}
			else     // 如果是坏坦克爆炸的话
			{
				tank.live=false;
				tankWar.startSend("B"+tank.number+"B");
				tankWar.explodes.add(new Explode(tank.getX()-5, tank.getY()-5, tankWar, true));  // 确保爆炸发生在击中的坦克所在的位置
				tankWar.badTanks.remove(tank);       // 这个remove在Tank类的bomb函数里的坏坦克里也要有，否则的话两边的剩余坦克显式会不同步
			}
			
			return true;
		}
		return false;
	}
	

	// 打击批量坦克的方法
	public boolean hitTanks(List<Tank> tanks)
	{
		if (tanks.size() > 0)
		{
			for (int i=0 ; i<tanks.size(); i++)
			{
				Tank  tank = tanks.get(i);
				if (tank != null)
				{
					// 打击当前坦克
					if (hitTank(tank))
					{
						// 击中坦克
						return true;
					}
				}
			}
		}
		
		return false; 
	}
	
	// 炮打老窝
	public boolean hitHome(Home home)
	{
		if (this.live && home.live && this.getRec().intersects(home.getRec()))
		{
			// 炮弹击中老窝
			// 炮弹死掉
			this.live=false;
			tankWar.missiles.remove(this);
			// 老窝死掉
			home.live=false;
			// 爆炸
			tankWar.explodes.add(new Explode(home.getX()-45, home.getY()-45, tankWar, false));     
			return true;
		}
		return false;
	}
	
	// 定义一个炮弹打击普通墙体的方法
	public boolean hitWall(CommonWall w)
	{
		if (this.live && w.live && this.getRec().intersects(w.getRec()))
		{
			// 炮弹击中墙体
			this.live=false;
			tankWar.missiles.remove(this);
			//墙体死掉
			w.live=false;
			tankWar.commonWalls.remove(w);
			//产生一个爆炸
//			tankWar.explodes.add(new Explode(w.getX()-15, w.getY()-15, tankWar));         // 打墙还是不产生爆炸为好
			
			return true;
		}
		
		return false;
	}
	
	// 定义一个炮弹打击所有墙体的方法
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
	
	// 定义一个打击金刚墙的方法
	public  boolean hitMetalWall(MetalWall w)
	{
		if (this.live && this.getRec().intersects(w.getRect()))
		{
			// 炮弹击中金刚墙
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















