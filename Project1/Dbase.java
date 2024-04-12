package Project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

class info {
	int pno;
	String pname;
	String type;
	String brand;
	int quantity;
	int price;
	public info(int pno, String pname, String type, String brand, int quantity, int price) {
		this.pno = pno;
		this.pname = pname;
		this.type = type;
		this.brand = brand;
		this.quantity = quantity;
		this.price = price;
		
	}
}

public class Dbase {
	Connection con; // 디비 연결
	String query; // 쿼리문 내용
	Statement stmt; // 워크시트 (쿼리문 입력 공간)
	ResultSet rs; // 결과
	ArrayList<info> storeInfos = new ArrayList<>(); // 결과를 어레이리스트에 저장
	//데이터연결
	void openCon() throws Exception{
		// 오라클 주소, 아이디 비밀번호
		String url = "jdbc:oracle:thin:@localhost:1521:XE";
		String user = "ADAM";
		String pw = "1234";

		// 자바 라이브러리 파일 로드
		Class.forName("oracle.jdbc.driver.OracleDriver");
		System.out.println("드라이버 로드 성공");

		// 네트워크 입출력 객체 생성
		System.out.println("데이터베이스 연결 준비...");
		con = DriverManager.getConnection(url, user, pw);
		System.out.println("데이터베이스 연결 성공");
				
		// 워크시트 생성
		stmt = con.createStatement();
		System.out.println("워크시트 생성");
	}
	//데이터종료
	void closeCon() throws Exception{
		con.close();
		System.out.println("데이터베이스 연결 해제");
	}
	//데이터입력
	void insertCon(int pno, String pname, String type, String brand, int quantity, int price) 
	throws Exception{
		query = "INSERT INTO PROD VALUES('" + pno + "','" + pname + "', '" + type + "','" + 
		brand + "','" + quantity + "','" + price + "')";
		stmt.executeUpdate(query); // 워크시트에 쿼리문 입력하기
		System.out.println(query);
	}
	//함수 불러오기
	void selectCon() throws Exception {
	    query = "SELECT pno, pname, type, brand, quantity, price FROM prod";
	    System.out.println(query);
	    rs = stmt.executeQuery(query); // 워크시트에 쿼리문 입력하고 데이터 받기
	    
	    while (rs.next()) {
	        // ResultSet에서 가져온 값으로 info 객체 생성
	        info temp = new info(rs.getInt("pno"), rs.getString("pname"), rs.getString("type"), 
	        rs.getString("brand"), rs.getInt("quantity"), rs.getInt("price"));
	        storeInfos.add(temp); // ArrayList에 객체 추가
	    }
	    
	    for (info i : storeInfos) { 
	        System.out.println("번호:"+i.pno + " , " + "품명:"+i.pname +" , " + "종류:"+i.type + " , "+ 
	        			"상표:"+i.brand+ " , " + "재고수:"+i.quantity+ " , " + "가격:"+i.price+"만원"); 
	    }
	}
	// 번호에 해당하는 함수 삭제
	void deleteCon(int pno) throws Exception{
		query = "DELETE prod WHERE pno like '" + pno + "'";
		stmt.executeUpdate(query);
		System.out.println(query);
	}
	
	public static void main(String[] args) {
		Dbase db = new Dbase();
		try {
			db.openCon();
			db.closeCon();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}