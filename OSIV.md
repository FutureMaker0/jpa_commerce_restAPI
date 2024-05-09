# OSIV란?
  - Open Session In View의 약자로, 하이버네이트가 지원하는 기능이다.
  - JPA 등장 이후로는 Open EntityManager In View라고 하는데, 관례상 OSIV라고 칭한다.
  - 트래픽이 많은 서비스 대응을 위해 반드시 알고 있어야 할 개념.

## 관련 WARN
  ```text
  2024-05-09T16:51:40.581+09:00  WARN 8514 --- [  restartedMain] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
  ```
