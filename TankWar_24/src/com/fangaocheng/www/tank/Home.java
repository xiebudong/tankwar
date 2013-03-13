package com.fangaocheng.www.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/*
 * 老窝
 */
public class Home {

	// 老窝图片
	private static Image homeImage;
	
	// 位置
	private int x;
	private int y;
	
	// 生死
	public boolean live;
	
	static
	{
		homeImage = Toolkit.getDefaultToolkit().getImage("images\\home.png");
	}
	
	
	public Home(int x, int y, boolean live) {
		super();
		this.x = x;
		this.y = y;
		this.live = live;
	}



	// 绘制的方法
	public void drawHome(Graphics g)
	{
		if (!live)
		{
			return;
		}
		g.drawImage(homeImage, x, y, null);	
	}
	
	// 获得当前位置矩形对象
	public Rectangle getRec()
	{
		return new Rectangle(x, y, 40, 40);
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






















