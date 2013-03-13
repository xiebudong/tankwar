package com.fangaocheng.www.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 *  ��ը��
 *
 */

public class Explode {
	
	// �������ɱ�ը��ͼƬ
	static Image[]  tankExplodes = new Image[4];
	static Image[]  homeExplodes = new Image[5];
	
	static Image  explode = Toolkit.getDefaultToolkit().getImage("images\\fire.png");
	
	
	//����һ����ʾ������±����ʼλ��
	int index1=0;
	int index2=0;
	
	//λ��
	int  x;
	int  y;
	
	// ��־��̹�˵ı�ը�������ѵı�ը
	boolean tankOrHomeExplode=true;
	
	// ��ը����
	boolean  live;
	
	// ���������
	TankWar  tankWar;
	
	static{
		
//		for (int i = 0; i < explodes.length; i++) {
//			explodes[i]=explode;
//		}
		tankExplodes[0]=Toolkit.getDefaultToolkit().getImage("images\\tank_bomb_1.png");
		tankExplodes[1]=Toolkit.getDefaultToolkit().getImage("images\\tank_bomb_2.png");
		tankExplodes[2]=Toolkit.getDefaultToolkit().getImage("images\\tank_bomb_3.png");
		tankExplodes[3]=Toolkit.getDefaultToolkit().getImage("images\\tank_bomb_4.png");
//		explodes[4]=Toolkit.getDefaultToolkit().getImage("images\\tank_bomb_5.png");
//		explodes[5]=Toolkit.getDefaultToolkit().getImage("images\\tank_bomb_6.png");
//		explodes[6]=Toolkit.getDefaultToolkit().getImage("images\\tank_bomb_7.png");
//		explodes[7]=Toolkit.getDefaultToolkit().getImage("images\\tank_bomb_8.png");
//		explodes[8]=Toolkit.getDefaultToolkit().getImage("images\\tank_bomb_9.png");
//		explodes[9]=Toolkit.getDefaultToolkit().getImage("images\\tank_bomb_10.png");
//		explodes[10]=Toolkit.getDefaultToolkit().getImage("images\\tank_bomb_11.png");
		
		homeExplodes[0]=Toolkit.getDefaultToolkit().getImage("images\\home_bomb_1.png");
		homeExplodes[1]=Toolkit.getDefaultToolkit().getImage("images\\home_bomb_1.png");
		homeExplodes[2]=Toolkit.getDefaultToolkit().getImage("images\\home_bomb_1.png");
		homeExplodes[3]=Toolkit.getDefaultToolkit().getImage("images\\home_bomb_1.png");
		homeExplodes[4]=Toolkit.getDefaultToolkit().getImage("images\\home_bomb_1.png");
//		homeExplodes[5]=Toolkit.getDefaultToolkit().getImage("images\\home_bomb_2.png");
		
	}
	
	public Explode()
	{
		
	}
	
	public Explode(int x, int y, TankWar tankWar, boolean tankOrHomeExplode) {
		super();
		this.x = x;
		this.y = y;
		this.live = true;
		this.tankWar=tankWar;
		this.tankOrHomeExplode=tankOrHomeExplode;
	}
	
	// ���Ʊ�ը
	public  void  drawExplode(Graphics  g){
		
		if(!live)
		{
			return;
		}
		if (tankOrHomeExplode)
		{
			if(index1 == tankExplodes.length)
			{
				//һ�α�ը����
				live=false;
				//���������Ƴ����ͷ��ڴ�
				tankWar.explodes.remove(this);
				index1=0;
			}
			g.drawImage(tankExplodes[index1], x, y, null);
			index1++;
		}
		else
		{
			if(index2 == homeExplodes.length)
			{
				//һ�α�ը����
				live=false;
				//���������Ƴ����ͷ��ڴ�
				tankWar.explodes.remove(this);
				index2=0;
			}
			g.drawImage(homeExplodes[index2], x, y, null);
			index2++;
		}
	}
}
