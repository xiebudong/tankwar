package com.fangaocheng.www.tank;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 *  登陆界面
 */

public class InputIp extends JFrame implements MouseListener{

	public static final int SCREEN_WIDTH=Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int SCREEN_HEIGHT=Toolkit.getDefaultToolkit().getScreenSize().height;
	// 登陆界面的大小
	public static final int LOGIN_WIDTH=194;
	public static final int LOGIN_HEIGHT=161;
	JLabel bgImage;
	ImageIcon image;
	Icon imageIcon;
	JPanel panel;
	JTextField ipInput;
	String ip;
	TankWar tankWar;
	
	private static Image bg;
	
	int x;
	int y;
	
	public InputIp(String s)
	{
		super(s);
		setLayout(null);
		this.setLocation((SCREEN_WIDTH-LOGIN_WIDTH)/2, (SCREEN_HEIGHT-LOGIN_HEIGHT)/2);
		this.setSize(LOGIN_WIDTH, LOGIN_HEIGHT);
		this.setUndecorated(true); //隐藏windows边框
	    image=new ImageIcon("images/input_ip.png");
	    imageIcon=image;
		bgImage=new JLabel(imageIcon);		
		//设置图标显示的位置
		bgImage.setBounds(0,0,194,161);
		
		ipInput = new JTextField(20);
		ipInput.setBackground(new Color(85,85,85));
		ipInput.setForeground(Color.white);
		ipInput.setBorder(null);
		ipInput.setBounds(29, 78, 133, 23);
		
		// 注册对鼠标的监听器
		this.addMouseListener(this);
		add(bgImage);
	    add(ipInput);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
	    this.setResizable(false);
    	this.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		// 获取鼠标点击的坐标位置
		x = e.getX();
		y = e.getY();
//		System.out.println("x="+x+"   "+"y="+y);
		
		if(x>=61 && x<=127 && y>=118 && y<=137)
		{
			ip=ipInput.getText();
			if (ip != null)
			{
				this.setVisible(false);
				tankWar=new TankWar(ip);
				tankWar.keyF6();
			}
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
