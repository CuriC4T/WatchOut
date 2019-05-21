package webcamServer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VidThread extends Thread {

	private DatagramSocket soc;

	private Robot rb;

	private byte[] outbuff;

	private BufferedImage mybuf;
	private Rectangle rc;
	private Server server;
	private ByteArrayOutputStream baos;
	private DatagramPacket dp;

	public VidThread(Server server) throws Exception {

		this.server = server;

		soc = server.getDs();
		rb = new Robot();
		outbuff = new byte[62000];
		System.out.println(soc.getPort());

	}

	public void run() {
		while (true) {
			try {

				int num = server.getI();
				baos = new ByteArrayOutputStream();

				// capture area//
				rc = new Rectangle(new Point(server.getX() + 8, server.getY() + 27),
						new Dimension(server.getPanel().getWidth(), server.getHeight() / 2));

				// capture//
				mybuf = rb.createScreenCapture(rc);

				// write iamge to stream//
				ImageIO.write(mybuf, "jpg", baos);

				// set buffer by stream//
				outbuff = baos.toByteArray();

				for (int i = 0; i < num; i++) {
					// set DP//
					dp = new DatagramPacket(outbuff, outbuff.length, server.getInet(i), server.getPort(i));

					// send and flush//
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
