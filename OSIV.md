# OSIV란?
  - Open Session In View의 약자로, 하이버네이트가 지원하는 기능이다.
  - JPA 등장 이후로는 Open EntityManager In View라고 하는데, 관례상 OSIV라고 칭한다.
  - 트래픽이 많은 서비스 대응을 위해 반드시 알고 있어야 할 개념.

## 관련 WARN
  ```text
  2024-05-09T16:51:40.581+09:00  WARN 8514 --- [  restartedMain] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
  ```

## 적용
  - application.yml
    ```yml
    spring:
      jpa:
        open-in-view: true/false
    ```

## 장단점
  - OSIV ON:
    - 장점: DB connection을 끝까지 유지하면서 지연로딩이 가능하다.
    - 단점: DB connection 고갈로 서비스 장애가 발생할 수 있다.
  - OSIV OFF:
    - 장점: DB connection의 빠른 반환으로 커넥션 풀 고갈을 방지할 수 있다.
    - 단점: 모든 지연로딩을 트랜잭션 내부에서 처리해야만 한다. 따라서 지연로딩 관련 코드를 transaction이 적용되는 범위 안으로 모두 옮겨주어야 한다.
    - off한 상태에서 서비스를 문제없이 관리하는 방법: Command(핵심 서비스 로직)와 query(변동성이 높은 특정 쿼리 중심 서비스 로직)을 분리 패키징한다.
      - OSIV OFF한 상태에서 지연로딩이 가능하게 한다.
      - 디비 커넥션 낭비를 막아 커넥션 고갈로 인한 서비스 장애를 방지한다.
      - 코드 가독성을 높여 향후 유지보수성을 높인다.</br>
  -> 클라이언트 서비스에서 트래픽이 많은 실시간 API는 옵션을 꺼서 커넥션 풀을 효율적으로 운용하고, 커넥션을 많이 사용하지 않는 기능에서는 옵션을 켜놓는 경우가 많다.
