package com.fangaocheng.www.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Grass {
	// ͼƬ
	static Image grassImage;
	
	// λ��
	int x;
	int y;
	
	
	public Grass(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	static
	{
		grassImage=Toolkit.getDefaultToolkit().getImage("images\\tree_wall.png");
	}
	
	public void drawGrass(Graphics g)
	{
		g.drawImage(grassImage, x, y, null);
	}
	
	// ���һ�����ζ���
	public Rectangle getRec()
	{
		return new Rectangle(x, y, 20, 20);
	}
}
