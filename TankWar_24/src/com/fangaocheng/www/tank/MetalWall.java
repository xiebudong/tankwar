package com.fangaocheng.www.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/*
 * ���ǽ 
 */

public class MetalWall {

	// ͼƬ
	static Image metalWallImage;
	
	int x;
	int y;
	
	static
	{
//		metalWallImage=Toolkit.getDefaultToolkit().getImage("images\\metal_wall.png");    // 20*20��С���
		metalWallImage=Toolkit.getDefaultToolkit().getImage("images\\metal.png");    // һ����
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
	
	// ��õ�ǰλ�õľ��ζ���
	public Rectangle getRect()
	{
//		return new Rectangle(x, y, 20, 20);
		return new Rectangle(x, y, 186, 35);
	}
}












