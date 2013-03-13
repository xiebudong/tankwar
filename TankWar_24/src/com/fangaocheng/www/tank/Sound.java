package com.fangaocheng.www.tank;



import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.net.URLClassLoader;

//游戏中所有的音频处理类
public class Sound {
	
	//获得一个URL资源定位器
	URLClassLoader urlClassLoader = (URLClassLoader)Sound.class.getClassLoader();
	//获得一个URL对象
	URL sendMissileURL = urlClassLoader.getResource("sendMissile.WAV");
	//获得一个音频对象
	AudioClip sendMissile = Applet.newAudioClip(sendMissileURL);
	
	//发射火箭炮的音效处理
	URL  SendRocketURL=urlClassLoader.getResource("sendRocket.WAV");
    //获得一个音频对象
	AudioClip   sendRocket = Applet.newAudioClip(SendRocketURL);
	
	URL  explodeURL=urlClassLoader.getResource("explode.WAV");
    //获得一个音频对象
	AudioClip   explode = Applet.newAudioClip(explodeURL);
	
	//定义一个播放发射炮弹的方法
	public void sendMissileMusic()
	{
		sendMissile.play();
	}
	//定义一个播放发射火箭炮的方法
	public void sendRocketMusic()
	{
		sendRocket.play();
	}
	//定义一个播放爆炸的方法
	public void explodeMusic()
	{
		explode.play();
	}
	

}
