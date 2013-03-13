package com.fangaocheng.www.tank;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class NetThread extends Thread {

	private TankWar tankWar;

	private static boolean clientOrServer = false;

	private String ip;
	private ServerSocket s;
	private Socket socket;
	InputStream inStream;
	OutputStream outStream;
	private Scanner in;
	private PrintWriter out;

	public NetThread() {

	}

	public NetThread(TankWar tankWar, String ip) {
		this.tankWar = tankWar;
		this.ip=ip;
	}

	public void setClient(boolean b) // b Ϊ true����������Ϊ�ͻ��ˣ�����Ϊ�����		
	{
		clientOrServer = b;
	}

	public Scanner getNetInputStream() {
		return in;

	}

	public PrintWriter getNetOutputStream() {
		return out;

	}
	
	@Override
	public void run() {
		try {
			if (clientOrServer) {
				socket = new Socket(ip, 9527);
			} else {
				// ��������������
				s = new ServerSocket(9527);
				// �ȴ��ͻ�������
				socket = s.accept();
			}
			try {
				inStream = socket.getInputStream();
				outStream = socket.getOutputStream();
				in = new Scanner(inStream);
				out = new PrintWriter(outStream, true /* �Զ�ˢ�·��� */);

				if (clientOrServer) {
					out.println("Hello Server!");
				}

				while (in.hasNextLine()) {
					// �����յ����������
					String line = in.nextLine();
//					System.out.println(" "+line);

					// ���ݽ��յ����ִ����ı䷽��
					if (line != null) {
						tankWar.netReceiveProcess(line);
					}

//					try {
//						Thread.sleep(20);                // Ŀǰ����������ӳ�20msΪ���, ���ֱ��ע�͵���Ч��Ҳ����
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
				}
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// �����������ӵ��ӳ�
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
