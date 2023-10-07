import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.alibaba.fastjson.JSONObject;

public class Client {
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	private static final String sever_ip = "127.0.0.1";
	private static final int sever_port = 8888;
	
	private static boolean signup(String name, String psw) throws IOException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("request", "signup");
		jsonObject.put("name", name);
		jsonObject.put("hash1", MyHashUtil.MyHash(name + psw, "MD5"));
		Socket socket;
		OutputStream os;
		InputStream is;
		PrintWriter pw;
		BufferedReader br;
		try {
			socket = new Socket(sever_ip, sever_port);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e1);
		}
		try{
			os = socket.getOutputStream();
			is = socket.getInputStream();
			pw = new PrintWriter(os);
			br = new BufferedReader(new InputStreamReader(is));
			
			pw.write(jsonObject.toString());
			pw.flush();
			socket.shutdownOutput();
			
			String resultJsonStr = "", temp;
			while((temp = br.readLine())!=null){
				resultJsonStr += temp;
			}
			
			socket.shutdownInput();
			JSONObject resultJson = JSONObject.parseObject(resultJsonStr);
			String result = resultJson.getString("result");
			
			os.close();
			is.close();
			pw.close();
			br.close();
			socket.close();
			if(result == null) {
				throw new RuntimeException("通讯错误！");
			} else if(result.equals("true")){
				return true;
			} else if(result.equals("false")) {
				return false;
			} else {
				throw new RuntimeException("通讯错误！");
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static boolean login(String name, String psw) throws IOException {
		String hash1 = MyHashUtil.MyHash(name + psw, "MD5");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("request", "login");
		jsonObject.put("name", name);
		String verify = "";
		for(int i = 0; i < 8; i++) {
			verify += Integer.toString((new Random()).nextInt(9));
		}
		String hash2 = MyHashUtil.MyHash(hash1 + verify, "MD5");
		jsonObject.put("verify", verify);
		jsonObject.put("hash2", hash2);
		Socket socket;
		OutputStream os;
		InputStream is;
		PrintWriter pw;
		BufferedReader br;
		try{	
			socket = new Socket(sever_ip, sever_port);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e1);
		}
		try {	
			os = socket.getOutputStream();
			is = socket.getInputStream();
			pw = new PrintWriter(os);
			br = new BufferedReader(new InputStreamReader(is));
			
			pw.write(jsonObject.toString());
			pw.flush();
			socket.shutdownOutput();
			
			String resultJsonStr = "", temp;
			while((temp = br.readLine())!=null){
				resultJsonStr += temp;
			}
			
			socket.shutdownInput();
			JSONObject resultJson = JSONObject.parseObject(resultJsonStr);
			String result = resultJson.getString("result");
			
			os.close();
			is.close();
			pw.close();
			br.close();
			socket.close();
			if(result == null) {
				throw new RuntimeException("通讯错误！");
			} else if(result.equals("true")){
				byte[] iv = resultJson.getBytes("iv");
				String c = resultJson.getString("c");
				String m;
				try {
					m = MyDESUtil.decrypt(c, hash1, iv);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("服务器端不可信！。");
				}
				if(m != null && m.equals(verify)) {
					System.out.println("登录成功。");
					File file = new File("login.log");
					if(!file.exists()) {
						file.createNewFile();
					}
					FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write("[TIME]" + df.format(new Date()) + "\n[NAME]" + name + "\n[VERIFY]" + m + "\n\n");
					bw.close();
					return true;
				} else {
					System.out.println("服务器端不可信！。");
					File file = new File("login.log");
					if(!file.exists()) {
						file.createNewFile();
					}
					FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write("[TIME]" + df.format(new Date()) + "\n[NAME]" + name + "\n[VERIFY]" + m + "(BUT IT SHOULD BE " + verify + ")\n\n");
					bw.close();
					return true;
				}
			} else if(result.equals("false")) {
				System.out.println(resultJson.getString("error"));
				return false;
			} else {
				throw new RuntimeException("通讯错误！");
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void main(String[] args) {
		ClientUI ui = new ClientUI();
		ui.LoginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ui.Username.getText().length() >= 3 && ui.Username.getText().length() <= 8 && 
						ui.Psw.getText().length() >= 6 && ui.Psw.getText().length() <= 16 && !ui.Username.getText().contains(" ")){
					try {
						if(login(ui.Username.getText().toUpperCase(), ui.Psw.getText())) {
							JOptionPane.showMessageDialog(ui, "登录成功。");
//							ui.setVisible(false);
//							System.exit(0);
						} else {
							JOptionPane.showMessageDialog(ui, "登录失败。");
						}
					} catch (HeadlessException e1) {
						// TODO Auto-generated catch block
						throw new RuntimeException(e1);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(ui, "无法连接到服务器。");
					}
				} else {
					JOptionPane.showMessageDialog(ui, "用户名长度[3,8]且不能含空格，密码长度[6,16]。");
				}
			}
		});
		ui.SignupButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ui.Username.getText().length() >= 3 && ui.Username.getText().length() <= 8 && 
						ui.Psw.getText().length() >= 6 && ui.Psw.getText().length() <= 16 && !ui.Username.getText().contains(" ")){
					try {
						if(signup(ui.Username.getText().toUpperCase(), ui.Psw.getText()))
						{
							JOptionPane.showMessageDialog(ui, "注册成功。");
						} else {
							JOptionPane.showMessageDialog(ui, "注册失败。");
						}
					} catch (HeadlessException e1) {
						// TODO Auto-generated catch block
						throw new RuntimeException(e1);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(ui, "无法连接到服务器。");
					}
				} else {
					JOptionPane.showMessageDialog(ui, "用户名长度[3,8]且不能含空格，密码长度[6,16]。");
				}
			}
		});
		ui.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		ui.setVisible(true);
	}
}
