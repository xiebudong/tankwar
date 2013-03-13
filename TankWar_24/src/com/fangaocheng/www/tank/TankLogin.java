package com.fangaocheng.www.tank;

import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 *  登陆界面
 */

public class TankLogin extends JFrame implements MouseListener{

	public static final int SCREEN_WIDTH=Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int SCREEN_HEIGHT=Toolkit.getDefaultToolkit().getScreenSize().height;
	// 登陆界面的大小
	public static final int LOGIN_WIDTH=194;
	public static final int LOGIN_HEIGHT=161;
	JLabel bgImage;
	ImageIcon image;
	Icon imageIcon;
	
	int x;
	int y;
	
	MouseEvent eBackup;
	
	public TankLogin(String s)
	{
		super(s);
		setLayout(null);
		this.setLocation((SCREEN_WIDTH-LOGIN_WIDTH)/2, (SCREEN_HEIGHT-LOGIN_HEIGHT)/2);
		this.setSize(LOGIN_WIDTH, LOGIN_HEIGHT);
		
		this.setUndecorated(true); //隐藏windows边框
	    
	    image=new ImageIcon("images/login.png");
	    imageIcon=image;
		bgImage=new JLabel(imageIcon);
		bgImage.setBounds(0,0,194,161);
		this.addMouseListener(this);
	    this.add(bgImage);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
	    this.setResizable(false);
    	this.setVisible(true);
	}


	public static void main(String[] args) 
	{
		new TankLogin("坦克大战");
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
//		eBackup=e;
		// 获取鼠标点击的坐标位置
		x = e.getX();
		y = e.getY();
//		System.out.println("x="+x+"   "+"y="+y);
		
		if(x>=48 && x<=143 && y>=55 && y<=82)
		{
			new ServerLogin("服务端");
			this.setVisible(false);
		}
		if(x>=48 && x<=143 && y>=102 && y<=127)//48,55,143,82
		{
			new InputIp("客户端");
			this.setVisible(false);
		}		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub	
	}
}
