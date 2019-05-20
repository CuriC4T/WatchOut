package server.copy;

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

	

	public ServerSetting(WebCamServer temp) throws Exception {

		ServerSetting.inet = new InetAddress[30];
		port = new int[30];


		ServerSocket welcomeSocket = new ServerSocket(6782);
		System.out.println(welcomeSocket.isClosed());
		Socket connectionSocket[] = new Socket[30];
		inFromClient = new BufferedReader[30];
		outToClient = new DataOutputStream[30];

		DatagramSocket serv = new DatagramSocket(4321);

		byte[] buf = new byte[62000];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);

		
		System.out.println("set");

		i = 0;

		

		while (true) {
			System.out.println("asdasddasd");

			System.out.println(serv.getPort());
			serv.receive(dp);
			System.out.println(new String(dp.getData()));
			buf = "starts".getBytes();

			inet[i] = dp.getAddress();
			port[i] = dp.getPort();

			DatagramPacket dsend = new DatagramPacket(buf, buf.length, inet[i], port[i]);
			serv.send(dsend);

			Vidthread sendvid = new Vidthread(temp,serv);

			System.out.println("waiting\n ");
			connectionSocket[i] = welcomeSocket.accept();
			System.out.println("connected " + i);

			inFromClient[i] = new BufferedReader(new InputStreamReader(connectionSocket[i].getInputStream()));
			outToClient[i] = new DataOutputStream(connectionSocket[i].getOutputStream());
			outToClient[i].writeBytes("Connected: from Server\n");

			

			

			System.out.println(inet[i]);
			sendvid.start();

			i++;

			if (i == 30) {
				break;
			}
		}
	}
}

class Vidthread extends Thread {

	int clientno;

	DatagramSocket soc;

	Robot rb = new Robot();
	

	byte[] outbuff = new byte[62000];

	BufferedImage mybuf;
	ImageIcon img;
	Rectangle rc;
	WebCamServer frame;
	int bord;
	
	public Vidthread(WebCamServer frame,DatagramSocket ds) throws Exception {
		this.frame=frame;
		soc = ds;
		bord = frame.panel.getY() - frame.getY();
		System.out.println(soc.getPort());
		
	}

	public void run() {
		while (true) {
			try {

				int num = ServerSetting.i;

				rc = new Rectangle(new Point(frame.getX() + 8, frame.getY() + 27),
						new Dimension(frame.panel.getWidth(), frame.getHeight() / 2));


				mybuf = rb.createScreenCapture(rc);

				img = new ImageIcon(mybuf);

				
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
public class WebCamServer extends JFrame {

	

	public static JPanel panel;
	public static int xpos = 0, ypos = 0;
	// Constructor
	public WebCamServer() {
		
		// Creating a panel that while contains the canvas
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(200, 0);
		setSize(640, 900);
		//setAlwaysOnTop(true);

		add(panel);
		setVisible(true);
		xpos = getX();
		ypos = getY();

		// Playing the video

		

		try {
			new ServerSetting(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);

		}
	}
}


