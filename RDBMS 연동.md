# R-DBMS: mySQL 연동

## 연동 및 데이터 활용 순서
1. 프로젝트 진행 전이라면 MySQL Driver를 Dependency 등록 해주면 되고, 진행 중 mySQL로 바꾸고자 하면 build.gradle에 아래 두 줄을 적용한다.
  - implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
  - implementation 'com.mysql:mysql-connector-j' (IDE, dbms 버전에 따라 사용되는 구문이 조금씩 다르니 확인 후 추가 필요)
2. 설정 파일 셋팅(application.yml 또는 application.properties)
  - 총 6가지 관련 항목을 설정해준다.
    - spring.datasource.url: jdbc:mysql://localhost:3306/[프로젝트 이름]?useSSL=false&useUnicode=true&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true
    - spring.datasource.username: root
    - spring.datasource.password: 적절히 설정(blank 시 enter)
    - spring.datasource.driver-class-name: com.mysql.cj.jdbc.Driver
    - spring.jpa.database-platform: org.hibernate.dialect.MySQLDialect
    - spring.jpa.hibernate.ddl-auto: update (create로 설정 시 특정 테이블이 존재하지 않는다는 에러 발생할 수 있음.)
   
3. 설정 완료 후 애플리케이션을 실행한다.
4. 터미널에서 CLI로 mySQL을 사용한다.
   - mysql -u root -p
   - password 입력(공백일 경우 엔터)
   - mySQL 접속 완료
5. show databases; 명령어 입력하여 테이블 리스트 확인
6. use [프로젝트 이름]; 명령어 입력하여 프로젝트 테이블 변경 요청, Database changed 메시지 확인.
7. show databases; 명령어 입력하면 프로젝트 테이블 목록 확인 가능.
8. desc [테이블명]; 명령어 통해 테이블 상세정보 확인 가능.
9. 여기서부터 데이터베이스 사용한 SQL 명령문 통해 데이터 조회 가능. (ex. select * from product;)
10. MVC 애플리케이션일 경우 웹 서비스에서, Restful API일 경우 Postman과 같은 툴을 통해 메서드 요청을 보낸다.(GET, POST, PUT 등)
    - 회원 등록 후, 데이터베이스 추가 내역 확인 가능
    - 회원 등록 API 통해 json 데이터 및 요청 보낸 후, 데이터베이스 추가 내역 확인 가능
11. control+c 명령어로 mySQL 연결 탈출

### mySQL 실행 및 조회 이미지
<img width="173" alt="mysql1" src="https://github.com/FutureMaker0/practical_developer_knowledge/assets/120623320/a54768b3-46ac-4329-985a-ffcaf1822a5b">
<img width="469" alt="mysql2" src="https://github.com/FutureMaker0/practical_developer_knowledge/assets/120623320/cc3dcf9a-24af-4038-aa17-efe6ea3ab0b3">
<img width="195" alt="mysql3" src="https://github.com/FutureMaker0/practical_developer_knowledge/assets/120623320/1ff02a4a-584f-4f91-8c0c-14419ea6308c">
<img width="541" alt="mysql4" src="https://github.com/FutureMaker0/practical_developer_knowledge/assets/120623320/059d0094-441f-495f-8943-18edb2614bab">
<img width="938" alt="mysql5" src="https://github.com/FutureMaker0/practical_developer_knowledge/assets/120623320/d765748c-9ded-488f-aac2-e190d186afce">
<img width="582" alt="mysql6" src="https://github.com/FutureMaker0/practical_developer_knowledge/assets/120623320/4f190d38-f454-4a95-bed2-7e6bfa5063c6">

