import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseManager {
	private static Connection conn;
	private static DataBaseManager instance = new DataBaseManager();
	
	private DataBaseManager(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/USER?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT";
			String user = "root";
			String password = "HITAsugo48";
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("成功建立连接：" + conn.toString());
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean NameQuery(String name) {
		try {
			PreparedStatement preparedstatement = conn.prepareStatement("SELECT * FROM USERS WHERE BINARY NAME = ?");
			preparedstatement.setString(1, name);
			ResultSet resultSet = preparedstatement.executeQuery();
			if(resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	public void Insert(String name, String hash1) {
		try {
			PreparedStatement preparedstatement = conn.prepareStatement("INSERT INTO USERS VALUES(?, ?)");
			preparedstatement.setString(1, name);
			preparedstatement.setString(2, hash1);
			preparedstatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	public String QueryHash1byName(String name) {
		try {
			PreparedStatement preparedstatement = conn.prepareStatement("SELECT HASH1 FROM USERS WHERE BINARY NAME = ?");
			preparedstatement.setString(1, name);
			ResultSet resultSet = preparedstatement.executeQuery();
			if(resultSet.next()) {
				return resultSet.getString("HASH1");
			} else {
				throw new RuntimeException();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	public static DataBaseManager getInstance() {
		return instance;
	}
}
