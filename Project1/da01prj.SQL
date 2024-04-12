CREATE TABLE prod(
PNO	 	 NUMBER(1)NOT NULL,		--번호
PNAME	 VARCHAR2(10),			--제품이름
TYPE	 VARCHAR2(9),			--제품종류
BRAND	 VARCHAR2(10),			--상표명
QUANTITY NUMBER(4),				--재고 수
PRICE	 NUMBER(7,2)			--가격
);

SELECT * FROM prod;
INSERT INTO prod VALUES (2, '노트북', '가전', 'MSI', 20, 150);