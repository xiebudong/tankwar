package com.fangaocheng.www.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/*
 * 河流
 */

public class River {
	
	// 图片
	static Image riverImage;
	
	// 位置
	int x;
	int y;
	
	
	public River(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	static
	{
		riverImage=Toolkit.getDefaultToolkit().getImage("images\\river_wall.png");
	}
	
	public void drawRiver(Graphics g)
	{
		g.drawImage(riverImage, x, y, null);
	}
	
	// 获得一个矩形对象
	public Rectangle getRec()
	{
		return new Rectangle(x, y, 20, 20);
	}
}
