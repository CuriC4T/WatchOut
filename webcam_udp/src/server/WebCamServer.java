package server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;

import javax.swing.*;

import raspistill.Raspistill2;

import java.nio.file.Files;

//import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/**
 *
 * @author imran
 */
class ServerSetting {

	/**
	 * @param args the command line arguments
	 */
	public static InetAddress[] inet;
	public static int[] port;
	public static int i;
	static int count = 0;
	public static BufferedReader[] inFromClient;
	public static DataOutputStream[] outToClient;

	public ServerSetting() throws Exception {

		ServerSetting.inet = new InetAddress[3];
		port = new int[3];

		ServerSocket welcomeSocket = new ServerSocket(6782);
		System.out.println(welcomeSocket.isClosed());
		Socket connectionSocket[] = new Socket[3];
		inFromClient = new BufferedReader[3];
		outToClient = new DataOutputStream[3];

		DatagramSocket serv = new DatagramSocket(4321);

		byte[] buf = new byte[62000];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);

		System.out.println("set");

		i = 0;

		while (true) {

			System.out.println(serv.getPort());
			serv.receive(dp);
			System.out.println(new String(dp.getData()));
			buf = "starts".getBytes();

			inet[i] = dp.getAddress();
			port[i] = dp.getPort();

			DatagramPacket dsend = new DatagramPacket(buf, buf.length, inet[i], port[i]);
			serv.send(dsend);

			Vidthread sendvid = new Vidthread(serv);

			System.out.println("waiting\n ");
			connectionSocket[i] = welcomeSocket.accept();
			System.out.println("connected " + i);

			inFromClient[i] = new BufferedReader(new InputStreamReader(connectionSocket[i].getInputStream()));
			outToClient[i] = new DataOutputStream(connectionSocket[i].getOutputStream());
			outToClient[i].writeBytes("Connected: from Server\n");

			System.out.println(inet[i]);
			sendvid.start();

			i++;

			if (i == 3) {
				break;
			}
		}
	}
}

class Vidthread extends Thread {
	public Raspistill2 raspistill;

	int clientno;

	DatagramSocket soc;

	Robot rb = new Robot();

	byte[] outbuff = new byte[62000];

	BufferedImage mybuf;

	public Vidthread(DatagramSocket ds) throws Exception {
		
		soc = ds;
		System.out.println(soc.getPort());

	}

	public void run() {
		raspistill = new Raspistill2();
		while (true) {
			try {

				int num = ServerSetting.i;

				mybuf = raspistill.TakePicture();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				ImageIO.write(mybuf, "jpg", baos);

				outbuff = baos.toByteArray();

				for (int j = 0; j < num; j++) {
					DatagramPacket dp = new DatagramPacket(outbuff, outbuff.length, ServerSetting.inet[j],
							ServerSetting.port[j]);

					soc.send(dp);
					baos.flush();
				}
				Thread.sleep(15);

			} catch (Exception e) {
				System.out.println(e);
			}

		}

	}

}
