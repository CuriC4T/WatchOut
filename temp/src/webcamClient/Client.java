/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamClient;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author imran
 */
public class Client {
	private DatagramSocket ds;
	private byte[] init;
	private InetAddress addr;
	private DatagramPacket dp;
	private DatagramPacket rcv;
	private InetAddress inetAddress;
	private Socket clientSocket;
	private DataOutputStream outToServer;

	private BufferedReader inFromServer;
	private VidThread vd;

	public Client() {

		try {
			ds = new DatagramSocket();
			//buffer and setting init message//
			init = new byte[62000];
			init = "givedata".getBytes();

			// server IP//
			addr = InetAddress.getByName("172.30.1.7");

			//send messgae//
			dp = new DatagramPacket(init, init.length, addr, 4321);
			ds.send(dp);

			//setting receive and receive message//
			rcv = new DatagramPacket(init, init.length);
			ds.receive(rcv);
			System.out.println(new String(rcv.getData()));

			//soketport//
			System.out.println(ds.getPort());
			vd = new VidThread(this);
			Thread thread = new Thread(vd);
			thread.start();


			inetAddress = InetAddress.getLocalHost();
			System.out.println(inetAddress);

			clientSocket = new Socket(addr, 6782);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());

			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer.writeBytes("Thanks man\n");

			
			clientSocket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public DatagramSocket getDs() {
		return ds;
	}

	public void setDs(DatagramSocket ds) {
		this.ds = ds;
	}

}
