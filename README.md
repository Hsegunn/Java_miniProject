### 자바 이클립스와 오라클을 연동하여 물류관리시스템 만들어보기

#### 1일차
- DBeaver에서 물류테이블 생성
- 이클립스로 들어가서 오라클 라이브러리를 자바에 연결
- 오라클에서 테이블을 생성
- 테이블이름을 자바 이클립스에서 객체로 생성하고 생성자 선언
- 데이터 연결,종료,입력,불러오기,삭제 함수 생성

#### 2일차
##### 기능업데이트
- Scanner 함수로 입력값을 줘서 데이터 입출력
- 조작가능한 메뉴창 생성

#### 3일차 
##### 테이블 2개(입고물품테이블,출고물품테이블) 추가
- 입고, 출고, 현재물품 테이블을 각각 생성해서 기능변경 및 추가하기
- 물품추가 -> 입고 , 물품삭제 -> 출고, 전체물품조회 -> 현재물품조회, 입출고 내역조회기능 추가 
- 입고 테이블: 입고내역만 출력 
- 출고 테이블: 출고된 내역만 출력 
- 현재물품 테이블: 입고되고 출고가 안된 남아있는 물품들을 출력

- 출력조회를 하면 입고된 내역도 같이 출력되는 오류와 내역조회마다 출력이 추가되는 오류발생
- `storeInfos` 리스트가 메소드 호출시 마다 초기화되지 않고 계속해서 데이터가 추가되기 때문에 
- storeInfos.clear() 함수를 추가하여 중복내역이 출력되지 않도록 리스트 초기화로 해결

#### 4일차 
##### 기능업데이트
- 삭제 함수를 입고, 출고 내역 삭제하는 함수로 변경하여 메뉴에 추가
- 메뉴에서 기능을 수행한 뒤 "ENTER"를 입력을 하면 메뉴가 실행되도록 수정 
- 조회함수끝에 총 물품 갯수와 총 가격 추가(현재물품조회함수, 입출고 내역조회함수)

#### 5일차
##### 컬럼추가
- 입출고테이블에 날짜 컬럼을 추가
- 날짜컬럼 형식을 년,월,일으로 출력
- 입출고내역과 현재물품내역에 날짜를 출력하기 위해서 함수수정

#### 5일차
##### 자료 정리및 프로젝트완료
- 만든 프로그램 세부사항 ppt로 정리하고 프로젝트 완료