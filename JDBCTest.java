import java.sql.*;
import java.io.*;

public class JDBCTest {
	public static void main(String[] args){
		
		try{
			//�ȼ���jar�ļ�sqljdbc4.jar
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			//����windows�˺ŵ�½����ָ���˺�����
			String dbURL = "jdbc:sqlserver://0.0.0.0:1433;DatabaseName=test;integratedSecurity=true;";
			Connection con = DriverManager.getConnection(dbURL);
			System.out.println("���ݿ����ӳɹ�");
			
			
			Statement stmt = con.createStatement(); 
			String query = "select * from tb;";
			ResultSet rs = stmt.executeQuery(query); 
			System.out.println("�������");
			
			
			
			while(rs.next()){
				System.out.println(rs.getString("id")+" "+rs.getString("name"));
			}
			con.close();
		}catch(Exception e){
			System.out.println("��������");
			StringWriter sw = new StringWriter();   
            PrintWriter pw = new PrintWriter(sw);   
            e.printStackTrace(pw);   

			System.out.println("\r\n");
			System.out.println(sw.toString());
			System.out.println("\r\n");
		}
		
	}
}