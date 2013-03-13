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

	public void setClient(boolean b) // b 为 true，则是设置为客户端，否则为服务端		
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
				// 建立服务器监听
				s = new ServerSocket(9527);
				// 等待客户端连接
				socket = s.accept();
			}
			try {
				inStream = socket.getInputStream();
				outStream = socket.getOutputStream();
				in = new Scanner(inStream);
				out = new PrintWriter(outStream, true /* 自动刷新发送 */);

				if (clientOrServer) {
					out.println("Hello Server!");
				}

				while (in.hasNextLine()) {
					// 将接收到的数据输出
					String line = in.nextLine();
//					System.out.println(" "+line);

					// 根据接收到的字串来改变方向
					if (line != null) {
						tankWar.netReceiveProcess(line);
					}

//					try {
//						Thread.sleep(20);                // 目前来看，这个延迟20ms为最好, 如果直接注释掉，效果也不错
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
		// 建立持续连接的延迟
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
