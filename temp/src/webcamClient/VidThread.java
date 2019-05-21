package webcamClient;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class VidThread extends JFrame implements Runnable {

	private JPanel jp;
	private JPanel half;
	private JLabel jl;
	
	private byte[] rcvbyte;

	private DatagramPacket dp;
	private BufferedImage bf;
	private ImageIcon imc;
	private Client client;
	private ByteArrayInputStream bais;
	
	public VidThread(Client client) {
		// sc = mysoc;
		// sc.setTcpNoDelay(true);
		this.client=client;
		
		frameSetting();
		
	
	}

	@Override
	public void run() {
		rcvbyte = new byte[62000];
		dp = new DatagramPacket(rcvbyte, rcvbyte.length);
		try {
			System.out.println("got in");
			do {
				//getport//
				System.out.println("doing");
				System.out.println("port connected "+client.getDs().getPort());

				//receive//
				client.getDs().receive(dp);
				System.out.println("received");
				
				//stream//
				bais = new ByteArrayInputStream(rcvbyte);

				//read from stream//
				bf = ImageIO.read(bais);

				if (bf != null) {
					//setVisible(true);
					//make image and set//
					imc = new ImageIcon(bf);
					jl.setIcon(imc);
					Thread.sleep(15);
				}
				
				//refresh//
				revalidate();
				repaint();

			} while (true);

		} catch (Exception e) {
			System.out.println("couldnt do it");
		}
	}

	public void frameSetting() {
		jp = new JPanel(new GridLayout(2, 1));
		jl = new JLabel();
		
		
		setSize(640, 960);
		setTitle("Client Show");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		setLayout(new BorderLayout());
		setVisible(true);
		
		jp.add(jl);
		
		add(jp);

		
	}
}
