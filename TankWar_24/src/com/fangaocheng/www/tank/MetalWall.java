package com.fangaocheng.www.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/*
 * 金刚墙 
 */

public class MetalWall {

	// 图片
	static Image metalWallImage;
	
	int x;
	int y;
	
	static
	{
//		metalWallImage=Toolkit.getDefaultToolkit().getImage("images\\metal_wall.png");    // 20*20的小块儿
		metalWallImage=Toolkit.getDefaultToolkit().getImage("images\\metal.png");    // 一大块儿
	}
	
	
	
	public MetalWall(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public void drawMetalWall(Graphics g)
	{
		g.drawImage(metalWallImage, x, y, null);
	}
	
	// 获得当前位置的矩形对象
	public Rectangle getRect()
	{
//		return new Rectangle(x, y, 20, 20);
		return new Rectangle(x, y, 186, 35);
	}
}












