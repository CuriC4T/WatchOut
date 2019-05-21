package webcamServer;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Server extends JFrame {

	private JPanel panel;

	private InetAddress[] inet;
	private ServerSocket welcomeSocket;
	private Socket connectionSocket[];
	private DatagramSocket serv;
	private DatagramPacket dp;
	private DatagramPacket dsend;

	private VidThread sendvid;

	private int[] port;
	private int i;
	private BufferedReader[] inFromClient;
	private DataOutputStream[] outToClient;

	public Server() throws Exception {
		frameSetting();
		serverSetting();
	}

	public void frameSetting() {

		// Creating a panel that while contains the canvas//
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// frame location and size//
		setLocation(200, 0);
		setSize(640, 900);
		// setAlwaysOnTop(true);

		add(panel);
		setVisible(true);

	}

	public void serverSetting() {
		// setting part//
		// maximun of client is 3//

		try {
			inet = new InetAddress[3];
			port = new int[3];

			//server port//
			welcomeSocket = new ServerSocket(6782);

			System.out.println("Is server clised?: " + welcomeSocket.isClosed());
			connectionSocket = new Socket[3];

			// set DatagramSocket and DatagramPacket//
			serv = new DatagramSocket(4321);
			byte[] buf = new byte[62000];
			dp = new DatagramPacket(buf, buf.length);

			// reader writer//
			inFromClient = new BufferedReader[3];
			outToClient = new DataOutputStream[3];

			i = 0;

			System.out.println("set colmplete");

			while (true) {

				System.out.println("port connected"+serv.getPort());
				serv.receive(dp);
				System.out.println("recevied: "+new String(dp.getData()));
				buf = "starts".getBytes();

				//get client ip and port//
				inet[i] = dp.getAddress();
				port[i] = dp.getPort();

				dsend = new DatagramPacket(buf, buf.length, inet[i], port[i]);
				serv.send(dsend);

				// streaming init//
				sendvid = new VidThread(this);

				// client 대기 및 연결//
				System.out.println("waiting\n ");
				connectionSocket[i] = welcomeSocket.accept();
				System.out.println("connected " + i);

				// client마다 writer reader setting//
				inFromClient[i] = new BufferedReader(new InputStreamReader(connectionSocket[i].getInputStream()));
				outToClient[i] = new DataOutputStream(connectionSocket[i].getOutputStream());
				outToClient[i].writeBytes("Connected: from Server\n");

				System.out.println("ip: " + inet[i]);

				// streaming start//
				sendvid.start();

				i++;

				if (i == 3) {
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// getter setter part//
	public DatagramSocket getDs() {
		return serv;
	}

	public int getPort(int i) {
		return port[i];
	}

	public int getI() {
		return i;
	}

	public InetAddress getInet(int i) {
		return inet[i];
	}

	public JPanel getPanel() {
		return panel;
	}

}
