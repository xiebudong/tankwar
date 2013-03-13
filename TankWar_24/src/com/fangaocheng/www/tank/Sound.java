package com.fangaocheng.www.tank;



import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.net.URLClassLoader;

//��Ϸ�����е���Ƶ������
public class Sound {
	
	//���һ��URL��Դ��λ��
	URLClassLoader urlClassLoader = (URLClassLoader)Sound.class.getClassLoader();
	//���һ��URL����
	URL sendMissileURL = urlClassLoader.getResource("sendMissile.WAV");
	//���һ����Ƶ����
	AudioClip sendMissile = Applet.newAudioClip(sendMissileURL);
	
	//�������ڵ���Ч����
	URL  SendRocketURL=urlClassLoader.getResource("sendRocket.WAV");
    //���һ����Ƶ����
	AudioClip   sendRocket = Applet.newAudioClip(SendRocketURL);
	
	URL  explodeURL=urlClassLoader.getResource("explode.WAV");
    //���һ����Ƶ����
	AudioClip   explode = Applet.newAudioClip(explodeURL);
	
	//����һ�����ŷ����ڵ��ķ���
	public void sendMissileMusic()
	{
		sendMissile.play();
	}
	//����һ�����ŷ������ڵķ���
	public void sendRocketMusic()
	{
		sendRocket.play();
	}
	//����һ�����ű�ը�ķ���
	public void explodeMusic()
	{
		explode.play();
	}
	

}
