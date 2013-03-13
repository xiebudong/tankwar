package com.fangaocheng.www.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/*
 * 普通的墙体 
 */

public class CommonWall {
	
	// 位置
	int x;
	int y;
	
	// 生死
	boolean live;
	
	// 图片
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



	// 绘制方法
	public void drawCommonWall(Graphics g)
	{
		if (!live)
		{
			return;
		}
		g.drawImage(CommonWallImage, x, y, null);
	}
	
	// 获得当前位置的一个矩形对象
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
