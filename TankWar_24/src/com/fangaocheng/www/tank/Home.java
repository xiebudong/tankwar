package com.fangaocheng.www.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/*
 * ����
 */
public class Home {

	// ����ͼƬ
	private static Image homeImage;
	
	// λ��
	private int x;
	private int y;
	
	// ����
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



	// ���Ƶķ���
	public void drawHome(Graphics g)
	{
		if (!live)
		{
			return;
		}
		g.drawImage(homeImage, x, y, null);	
	}
	
	// ��õ�ǰλ�þ��ζ���
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






















