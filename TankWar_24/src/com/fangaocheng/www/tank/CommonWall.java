package com.fangaocheng.www.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/*
 * ��ͨ��ǽ�� 
 */

public class CommonWall {
	
	// λ��
	int x;
	int y;
	
	// ����
	boolean live;
	
	// ͼƬ
	static Image CommonWallImage;
	
	static
	{
		CommonWallImage=Toolkit.getDefaultToolkit().getImage("images\\ptwall.png");
	}
	
	
	
	public CommonWall(int x, int y, boolean live) {
		super();
		this.x = x;
		this.y = y;
		this.live = live;
	}



	// ���Ʒ���
	public void drawCommonWall(Graphics g)
	{
		if (!live)
		{
			return;
		}
		g.drawImage(CommonWallImage, x, y, null);
	}
	
	// ��õ�ǰλ�õ�һ�����ζ���
	public Rectangle getRec()
	{
		return new Rectangle(x, y, 20, 20);
	}



	public int getX() {
		return x;
	}



	public void setX(int x) {
		this.x = x;
	}



	public int getY() {
		return y;
	}



	public void setY(int y) {
		this.y = y;
	}
	
	
}
