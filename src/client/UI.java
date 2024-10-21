package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

import server.BankingServiceInterface;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class UI {

	private JFrame frame;
	private JTextField textFieldUsername;
	private JLabel lblPass;
	private JTextField textFieldPassword;
	private JLabel lblSodu;
	private JTextField textField_Naptien;
	private JButton btnNap;
	private JTextField textField_Rut;
	private JButton btnRut;
	private BankingServiceInterface bankStub;
	private String username;
	private double amount;
	private JLabel lblUser;
	private JTextField textFieldPort;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public UI() {
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(204, 255, 255));
		frame.setBounds(100, 100, 671, 354);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(153, 255, 255));
		panel.setBounds(395, 10, 240, 297);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setBounds(22, 45, 133, 26);
		panel.add(textFieldUsername);
		textFieldUsername.setColumns(10);
		
		JLabel lblUserName = new JLabel("User name");
		lblUserName.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblUserName.setBounds(22, 10, 133, 25);
		panel.add(lblUserName);
		
		lblPass = new JLabel("Password");
		lblPass.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPass.setBounds(22, 81, 118, 22);
		panel.add(lblPass);
		
		textFieldPassword = new JTextField();
		textFieldPassword.setBounds(22, 113, 133, 26);
		panel.add(textFieldPassword);
		textFieldPassword.setColumns(10);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPort.setBounds(22, 159, 118, 22);
		panel.add(lblPort);
		
		textFieldPort = new JTextField();
		textFieldPort.setText("");
		textFieldPort.setColumns(10);
		textFieldPort.setBounds(22, 191, 133, 26);
		panel.add(textFieldPort);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnLogin.setBounds(32, 235, 94, 33);
		panel.add(btnLogin);
		
		btnLogin.addActionListener(e -> {
			try {
				
				Registry registry = LocateRegistry.getRegistry("localhost", Integer.parseInt(textFieldPort.getText()));						
				
				try {
					bankStub = (BankingServiceInterface)registry.lookup("bankingService");
				} catch (NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if( bankStub.login(textFieldUsername.getText(), textFieldPassword.getText()) )
				{
					JOptionPane.showMessageDialog(null, "Đăng nhập thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
					username = textFieldUsername.getText();
					amount =  bankStub.getBalance(textFieldUsername.getText());
					lblSodu.setText("Số dư "+ amount);
					lblUser.setText("Người dùng: " + username);
					
					textFieldPassword.setText("");
					textFieldUsername.setText("");
					btnLogin.setEnabled(false);					
					
				}
				else
					JOptionPane.showMessageDialog(null, "Đăng nhập thất bại", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		lblSodu = new JLabel("Số dư: ");
		lblSodu.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblSodu.setBounds(38, 72, 133, 24);
		frame.getContentPane().add(lblSodu);
		
		textField_Naptien = new JTextField();
		textField_Naptien.setBounds(38, 119, 170, 36);
		frame.getContentPane().add(textField_Naptien);
		textField_Naptien.setColumns(10);
		
		btnNap = new JButton("Nạp tiền");
		btnNap.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double num = Double.parseDouble(textField_Naptien.getText()) ;
				try {
					amount = bankStub.deposit( username, num );
					lblSodu.setText("Số dư "+ amount);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNap.setBounds(218, 119, 123, 36);
		frame.getContentPane().add(btnNap);
		
		textField_Rut = new JTextField();
		textField_Rut.setBounds(38, 165, 170, 41);
		frame.getContentPane().add(textField_Rut);
		textField_Rut.setColumns(10);
		
		btnRut = new JButton("Rút tiền");
		btnRut.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnRut.setBounds(218, 165, 123, 41);
		btnRut.addActionListener(e ->{
			double num = Double.parseDouble(textField_Rut.getText()) ;
			try {
				if(bankStub.withdraw(username, num))
				{
					amount -= num;
					lblSodu.setText("Số dư "+ amount);
				}
				else {
					JOptionPane.showMessageDialog(frame, "Rút tiền không thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		frame.getContentPane().add(btnRut);
		
		lblUser = new JLabel("");
		lblUser.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblUser.setBounds(38, 16, 303, 36);
		frame.getContentPane().add(lblUser);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					bankStub.logout(username);
					JOptionPane.showMessageDialog(frame, "Đăng xuất thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
					username = "";
					amount = 0;
					lblUser.setText("");
					lblSodu.setText("");
					btnLogin.setEnabled(true);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnLogout.setBounds(121, 216, 123, 47);
		frame.getContentPane().add(btnLogout);
		frame.setVisible(true);
	}
}
