import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;

public class Server {
	
	private static final int port = 8888;
	private static final DataBaseManager dataBaseManager = DataBaseManager.getInstance();
	
	public static void main(String[] args) {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("服务器\n==============");
			while(true)
			{
				Socket socket = serverSocket.accept();
				OutputStream os = socket.getOutputStream();
				InputStream is = socket.getInputStream();
				PrintWriter pw = new PrintWriter(os);
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				
				String requestJsonStr = "", temp;
				while((temp = br.readLine()) != null) {
					requestJsonStr += temp;
				}
				socket.shutdownInput();
				
				JSONObject requestJson = JSONObject.parseObject(requestJsonStr);
				String request = requestJson.getString("request");
				
				if(request == null) {
					os.close();
					is.close();
					pw.close();
					br.close();
					socket.close();
					throw new RuntimeException("通讯错误！");
				} else if(request.equals("signup")) {
					String name, hash1;
					name = requestJson.getString("name");
					hash1 = requestJson.getString("hash1");
					JSONObject answer = new JSONObject();
					if(dataBaseManager.NameQuery(name)) {
						answer.put("result", "false");
					} else {
						dataBaseManager.Insert(name, hash1);
						answer.put("result", "true");
					}
					pw.write(answer.toJSONString());
					pw.flush();
					socket.shutdownOutput();
					os.close();
					is.close();
					pw.close();
					br.close();
					socket.close();
				} else if(request.equals("login")) {
					String name, verify, hash2;
					name = requestJson.getString("name");
					verify = requestJson.getString("verify");
					hash2 = requestJson.getString("hash2");
					JSONObject answer = new JSONObject();
					if(!dataBaseManager.NameQuery(name)) {
						answer.put("result", "false");
						answer.put("error", "invalidname");
					} else {
						String hash1 = dataBaseManager.QueryHash1byName(name);
						String hash2_toverify = MyHashUtil.MyHash(hash1 + verify, "MD5");
						if(hash2_toverify.equals(hash2)) {
							answer.put("result", "true");
							byte[] iv = new byte[8];
							new Random().nextBytes(iv);
							String c = MyDESUtil.encrypt(verify, hash1, iv);
							answer.put("iv", iv);
							answer.put("c", c);
						} else {
							answer.put("result", "false");
							answer.put("error", "wrongpsw");
						}
					}
					pw.write(answer.toJSONString());
					pw.flush();
					socket.shutdownOutput();
					os.close();
					is.close();
					pw.close();
					br.close();
					socket.close();
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
