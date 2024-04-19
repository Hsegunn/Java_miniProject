//최종
package Project1;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

class info {
	int pno;		//번호
	String pname;	//이름
	String type;	//종류
	String brand;	//브랜드
	int quantity;	//재고
	int price;		//가격
	Date PDate; 	//일자
	//생성자
	public info(int pno, String pname, String type, String brand, int quantity, 
	int price, Date PDate) {
		this.pno = pno;
		this.pname = pname;
		this.type = type;
		this.brand = brand;
		this.quantity = quantity;
		this.price = price;
		this.PDate = PDate;
		
	}
}

public class Dbase5 {
	Connection con; // 디비 연결
	String query1; // 쿼리문 내용1
	String query2; // 쿼리문 내용2
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
	//입고데이터입력
	void insertCon(int pno, String pname, String type, String brand, int quantity, int price) 
	throws Exception {
		// LocalDate함수(날짜 정보만 출력)를 사용해서 오늘 날짜 가져오기
		LocalDate today = LocalDate.now();
		// LocalDate를 java.sql.Date(SQL Date 값으로 식별)로 변환
		java.sql.Date sqlDate = java.sql.Date.valueOf(today); 

		String sqlInProd = "INSERT INTO INPROD(pno,pname,type,brand,quantity,price,PDate) "
						  +"VALUES(?, ?, ?, ?, ?, ?, ?)";
		String sqlProd = "INSERT INTO PROD(pno,pname,type,brand,quantity,price,PDate) "
				        +"VALUES(?, ?, ?, ?, ?, ?, ?)";
			try {
				con.setAutoCommit(false); // 트랜잭션 시작
			        
			    	try (PreparedStatement pstmt = con.prepareStatement(sqlInProd)) {
			            pstmt.setInt(1, pno);		// 번호 설정	
			            pstmt.setString(2, pname);  // 이름 설정
			            pstmt.setString(3, type);   // 종류 설정
			            pstmt.setString(4, brand);  // 브랜드 설정
			            pstmt.setInt(5, quantity);  // 재고 수 설정
			            pstmt.setInt(6, price);     // 가격 설정
			            pstmt.setDate(7, sqlDate);  // 날짜 설정
			            pstmt.executeUpdate();
			        }

			        try (PreparedStatement pstmt = con.prepareStatement(sqlProd)) {
			            pstmt.setInt(1, pno);
			            pstmt.setString(2, pname);
			            pstmt.setString(3, type);
			            pstmt.setString(4, brand);
			            pstmt.setInt(5, quantity);
			            pstmt.setInt(6, price);
			            pstmt.setDate(7, sqlDate);  
			            pstmt.executeUpdate();
			        }

			        con.commit(); // 트랜잭션 커밋
			        System.out.println("물품이 정상적으로 입고되었습니다.");
			    } catch (SQLException e) {
			        con.rollback(); // 오류 발생 시 롤백
			        System.out.println("입고 처리 중 오류가 발생했습니다: " + e.getMessage());
			        throw e;
			    } finally {
			        con.setAutoCommit(true); // 자동 커밋으로 복구
			    }
			}
	//출고데이터입력
	void outInsertCon(int pno, String pname, String type, String brand, int quantity, int price) 
			throws Exception {
			    LocalDate today = LocalDate.now(); // 오늘 날짜를 가져옵니다.
			    String sql = "INSERT INTO OUTPROD(pno,pname,type,brand,quantity,price,PDate) "
			    		   + "VALUES(?, ?, ?, ?, ?, ?, ?)";
			    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			        pstmt.setInt(1, pno);
			        pstmt.setString(2, pname);
			        pstmt.setString(3, type);
			        pstmt.setString(4, brand);
			        pstmt.setInt(5, quantity);
			        pstmt.setInt(6, price);
			        pstmt.setDate(7, java.sql.Date.valueOf(today)); // LocalDate를 SQL Date로 변환
			        pstmt.executeUpdate();
			        System.out.println("물품이 출고되었습니다");
			    }
			}
	// 현재품목 조회 함수
	void selectCorCon() throws Exception {
	    storeInfos.clear(); // 결과 리스트 초기화
	    String query1 = "SELECT pno, pname, type, brand, quantity, price, PDate FROM prod";
	    int totalPrice = 0, totalQuantity = 0;
	    
	    try (PreparedStatement pstmt = con.prepareStatement(query1);
	         ResultSet rs = pstmt.executeQuery()) {
	        while (rs.next()) {
	            info temp = new info(rs.getInt("pno"), rs.getString("pname"), 
	            				rs.getString("type"),rs.getString("brand"), 
	            				rs.getInt("quantity"), rs.getInt("price"), 
	            				rs.getDate("PDate"));
	            storeInfos.add(temp);
	            totalQuantity += temp.quantity;
	            totalPrice += temp.quantity * temp.price;
	        }

	        for (info i : storeInfos) {
	            System.out.println("번호: " + i.pno + ", 품명: " + i.pname + ", 종류: " + i.type + 
	                               ", 브랜드: " + i.brand + ", 재고수량: " + i.quantity + 
	                               ", 가격: " + i.price + "만원, 입고일: " + i.PDate);
	        }
	        System.out.println("총 물품수: " + totalQuantity + "개, 총 가격: " + totalPrice + "만원");
	    } catch (SQLException e) {
	        System.out.println("Database error: " + e.getMessage());
	    }
	}
	//입고품목 조회함수
	void selectInCon() throws Exception {
	    storeInfos.clear(); // 결과 리스트 초기화
	    query1 = "SELECT pno, pname, type, brand, quantity, price, PDate FROM inprod";
	    System.out.println(query1);
	    rs = stmt.executeQuery(query1); // 워크시트에 쿼리문 입력하고 데이터 받기
	    
	    int totalPrice = 0, totalQuantity = 0;
	    while (rs.next()) {
	        // ResultSet에서 가져온 값으로 info 객체 생성
	        info temp = new info(rs.getInt("pno"), rs.getString("pname"), rs.getString("type"), 
	        rs.getString("brand"), rs.getInt("quantity"), rs.getInt("price"), 
	        rs.getDate("PDate"));
	        storeInfos.add(temp); // ArrayList에 객체 추가
	        totalQuantity += temp.quantity;
	        totalPrice += temp.quantity * temp.price;
	        System.out.println("번호: " + temp.pno + ", 품명: " + temp.pname + ", 종류: " + 
	        temp.type + ", 상표: " + temp.brand + ", 재고수: " + temp.quantity + ", 가격: " + 
	        temp.price + "만원, 입고일: " + temp.PDate);
	    }
	    System.out.println("총 물품: " + totalQuantity + "개, 총 가격: " + totalPrice + "만원");
	}
	
	

	//출고품목 조회함수
	void selectOutCon() throws Exception {
	    storeInfos.clear(); // 결과 리스트 초기화
	    query1 = "SELECT pno, pname, type, brand, quantity, price, PDate FROM outprod";
	    System.out.println(query1);
	    rs = stmt.executeQuery(query1); // 워크시트에 쿼리문 입력하고 데이터 받기
	    
	    int totalPrice = 0, totalQuantity = 0;
	    while (rs.next()) {
	        // ResultSet에서 가져온 값으로 info 객체 생성
	        info temp = new info(rs.getInt("pno"), rs.getString("pname"), rs.getString("type"), 
	        rs.getString("brand"), rs.getInt("quantity"), rs.getInt("price"), 
	        rs.getDate("PDate"));
	        storeInfos.add(temp); // ArrayList에 객체 추가
	        totalQuantity += temp.quantity;
	        totalPrice += temp.quantity * temp.price;
	        System.out.println("번호: " + temp.pno + ", 품명: " + temp.pname + ", 종류: " + 
	        temp.type + ", 상표: " + temp.brand + ", 재고수: " + temp.quantity + ", 가격: " + 
	        temp.price + "만원, 출고일: " + temp.PDate);
	    }
	    System.out.println("총 물품: " + totalQuantity + "개, 총 가격: " + totalPrice + "만원");
	}
	// 번호에 해당하는 함수를 물품테이블에서 삭제
	void deleteCon(int pno) throws Exception {
	    query2 = "DELETE FROM prod WHERE pno = '" + pno + "'";
	    int del = stmt.executeUpdate(query2);
	    if (del > 0) {
	        System.out.println("물품 " + pno + "번 제품이 현재물품에서 제거됩니다.");
	    } else {
	        System.out.println("물품 " + pno + "번 제품이 존재하지 않습니다.");
	    }
	}
	void deleteInCon(int pno) throws Exception {
	    String query = "DELETE FROM INPROD WHERE pno = '" + pno + "'";
	    int del = stmt.executeUpdate(query);
	    if (del > 0) {
	        System.out.println("물품 " + pno + "번 제품이 입고내역에서 삭제되었습니다.");
	    } else {
	        System.out.println("물품 " + pno + "번 제품이 입고내역에 존재하지 않습니다.");
	    }
	}
	void deleteOutCon(int pno) throws Exception {
	    String query = "DELETE FROM OUTPROD WHERE pno = '" + pno + "'";
	    int del = stmt.executeUpdate(query);
	    if (del > 0) {
	        System.out.println("물품 " + pno + "번 제품이 출고내역에서 삭제되었습니다.");
	    } else {
	        System.out.println("물품 " + pno + "번 제품이 출고내역에 존재하지 않습니다.");
	    }
	}
	
	void menu() throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
        	openCon();
            System.out.println("\n=== 메뉴 선택 ===");
            System.out.println("1. 물품 입고");
            System.out.println("2. 물품 입고내역삭제");
            System.out.println("3. 물품 출고");
            System.out.println("4. 물품 출고내역삭제");
            System.out.println("5. 입고 내역조회");
            System.out.println("6. 출고 내역조회");
            System.out.println("7. 현재물품 조회");
            System.out.println("8. 현재물품 내역삭제");
            System.out.println("9. 종료");
            System.out.print("선택: ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();  
                switch (choice) {
            	//물품 입고
                case 1:
                    System.out.print("번호: ");
                    int pno = scanner.nextInt();
                    scanner.nextLine(); 
                    System.out.print("제품명: ");
                    String pname = scanner.nextLine();
                    System.out.print("종류: ");
                    String type = scanner.nextLine();
                    System.out.print("브랜드: ");
                    String brand = scanner.nextLine();
                    System.out.print("재고수: ");
                    int quantity = scanner.nextInt();
                    System.out.print("가격: ");
                    int price = scanner.nextInt();
                    scanner.nextLine();
                    
                    insertCon(pno, pname, type, brand, quantity, price);
                    System.out.println("엔터키를 누르면 메뉴로 돌아갑니다.");
                    scanner.nextLine();
                    break;
                //물품 입고내역삭제
                case 2:
                	System.out.print("입고내역을 삭제할 물품번호: ");
                    int pnoIn = scanner.nextInt();
                    scanner.nextLine();
                    deleteInCon(pnoIn);
                    System.out.println("엔터키를 누르면 메뉴로 돌아갑니다.");
                    scanner.nextLine();
                    break;
                //물품출고
                case 3:
                    System.out.print("출고할 물품 번호: ");
                    int deletePno = scanner.nextInt();
                    scanner.nextLine();
                    query1 = "SELECT pno, pname, type, brand, quantity, price FROM prod WHERE pno = ?";
                    try (PreparedStatement pstmt = con.prepareStatement(query1)) {
                        pstmt.setInt(1, deletePno);
                        rs = pstmt.executeQuery();
                        if (rs.next()) {
                            String pname1 = rs.getString("pname");
                            String type1 = rs.getString("type");
                            String brand1 = rs.getString("brand");
                            int quantity1 = rs.getInt("quantity");
                            int price1 = rs.getInt("price");
                            // prod 테이블에서 물품 제거
                            deleteCon(deletePno); 
                            // outprod에 물품 내역 추가
                            outInsertCon(deletePno, pname1, type1, brand1, quantity1, price1);
                        } else {
                            System.out.println("해당 번호의 물품이 존재하지 않습니다.");
                        }                 
                    }
                    System.out.println("엔터키를 누르면 메뉴로 돌아갑니다.");
                    scanner.nextLine();
                    break;
                //물품 출고내역삭제
                case 4:
                	System.out.print("출고내역을 삭제할 물품번호: ");
                    int pnoOut = scanner.nextInt();
                    scanner.nextLine();
                    deleteOutCon(pnoOut);
                    System.out.println("엔터키를 누르면 메뉴로 돌아갑니다.");
                    scanner.nextLine();
                    break;
                //입고내역조회	
                case 5:
                	selectInCon();
                	System.out.println("엔터키를 누르면 메뉴로 돌아갑니다.");
                    scanner.nextLine();
                    break;
                //출고내역조회
                case 6:
                	selectOutCon();
                	System.out.println("엔터키를 누르면 메뉴로 돌아갑니다.");
                    scanner.nextLine();
                    break;
                //현재물품조회
                case 7:
                	selectCorCon();
                	System.out.println("엔터키를 누르면 메뉴로 돌아갑니다.");
                    scanner.nextLine();
                    break;
                //현재물품삭제
                case 8:
                	System.out.print("물품내역에서 삭제할 물품번호: ");
                    int delPno = scanner.nextInt();
                    scanner.nextLine();
                    deleteCon(delPno);
                    System.out.println("엔터키를 누르면 메뉴로 돌아갑니다.");
                    scanner.nextLine();
                    break;
                //종료
                case 9:
                	closeCon();
                	System.out.println("프로그램을 종료합니다.");
                    scanner.close();
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 시도하세요.");
            }
        }catch (Exception e) {
            // 숫자가 아닌 값을 입력했을 때의 처리
            System.out.println("옳바른 형식으로 입력해주시기 바랍니다.");
            scanner.nextLine();
        }
        }
}
	
	public static void main(String[] args) {
		Dbase5 db = new Dbase5();
		try {
			db.menu();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}