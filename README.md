# jpa_commerce_restAPI
- jpa 기반 커머스 웹 서비스 Restful API 설계 및 구현 프로젝트
- jpa_toypjt_commerce 프로젝트와 기본적인 MVC 코드를 공유하며, rest API 방식의 서비스를 위한 api controller를 추가하는 방식으로 진행

# 기술 스택
  - Java(java 8, intelliJ IDEA)
  - Spring Data Jpa
  - Postman
  - H2 database(+mySQL)

## 프로젝트 트리 구성
- jpa
  - commerce
    - ...
    - api
      - member
        - controller
          - MemberApiController
        - dto
          - CustomFormat
          - MemberDto
          - RegistMemberRequest
          - RegistMemberResponse
          - UpdateMemberRequest
          - UpdateMemberResponse
      - order
        - controller
          - OrderApiControllerL1
          - OrderApiControllerL2
        - dto
          - L1
            - OrderDtoL1
            - OrderJpaDirectDtoL1
            - OrderProductDtoL1
          - L2
            - OrderDtoL2
            - OrderJpaDirectDtoL2
            - OrderProductDtoL2
            - OrderFlatDto
          - ObjectFormat
        - repository
          - queryRepositoryL1
          - queryRepositoryL2
    - repository
      - orderRepository
        - regist()
        - fineOrderById()
        - fineAllOrders()
        - fineByName()
        - fineByStatus()
        - findAllUsingMemberDelivery()
        - findAllUsingProduct()
     
## API 어노테이션
  - @RestController: @Controller + @ResponseBody
  - @RequestBody: json으로 넘어온 body를 객체로 바로 매핑해서 값을 넣어준다. (json data -> Entity 객체)
  - @Data: 아래 어노테이션들의 조합, 코드 가독성을 높이고 반복 작업을 줄이며 코드 간결성을 향상시킬 수 있다. DTO에서 주로 활용.
    1. @Getter: 클래스 모든 필드에 대한 getter 메서드 자동 생성
    2. @Setter: 클래스 모든 필드에 대한 setter 메서드 자동 생성
    3. @ToString: 클래스의 toString() 메서드 자동 생성
    4. @EqualsAndHashCode: 클래스의 equals() 및 hashCode() 메서드 자동 생성
   
## API 명세
  <img width="450" alt="" src="https://github.com/FutureMaker0/jpa_toypjt_commerce/assets/120623320/4245a37e-e254-466b-a94a-79b4cd167954"></br>
  <img width="450" alt="" src="https://github.com/FutureMaker0/jpa_toypjt_commerce/assets/120623320/61655785-5948-418c-985d-a3107949955c">
  - 서비스에서의 대부분의 성능 이슈는 '조회' 시 발생한다.

## 회원등록 API(method="POST")
  - registMemberV1: Member 엔티티를 그대로 파라미터에서 쓰고 있으므로 그로부터 유발되는 문제들이 발생할 수 있다.
    - 엔티티 필드 수정이 일어나면 API 스펙이 바뀌어버리는 중대한 문제가 발생할 수 있다. 엔티티는 굉장히 여러 곳에서 사용하기 때문에 바뀔 가능성이 높은데, 엔티티가 변경될 때마다 API를 그에 맞춰 다시 만들어야하는 상황이 발생한다.
    - 특정 API와 엔티티가 1:1로 딱 붙어 매칭이 되어버리면 그 이외 활용 상황에서 원하는대로 사용할 수 없는 문제가 반드시 생긴다.
    - 특정 API를 위한 별도의 DTO(Data Transfer Object)를 만들어서 파라미터로 받아 써야 한다. 엔티티를 외부에서 오는 그대로 바인딩 받아서 쓰면 안된다. 큰 장애가 발생한다.
    - 실무에서는 등록 API가 하나가 아니라 여러개일 확률이 매우 높다. 간편가입, 소셜가입 등등 많은 케이스를 엔티티 원본 하나만으로 감당할 수 없다.
    - API를 만들 때는 엔티티를 파라미터로 받지 말자. 또한 엔티티를 외부에 노출해서도 안된다. 
  - registMemberV2:
    - 별도의 DTO를 만들어 활용하기 때문에, 엔티티를 변경해도 V2와 매칭되어 있는 API 스펙이 변경될 일이 없다.
    - DTO를 만드는 조금의 번거로움? 만 추가되었다.
    - DTO를 받으면 해당 API 스펙에서 정의하고 있는 필드가 무엇인지를 한눈에 확인할 수 있다.
    - DTO에다가 @어노테이션 붙여서 validation도 가능하다.
    - V1으로 개발하면서 엔티티를 임의 수정했다가 발생할 사이드 이펙트의 범위를 측정하기조차 힘들게 될 것이다.
    - API는 요청 및 응답을 엔티티 원본을 사용하는게 아니라 DTO 객체를 사용해서 하는 것을 권장한다.

## 회원수정 API(method="PUT")
  - 회원이름 정보 수정 API('멱등하다'로 표현한다.)
  - UpdateMemberRequest, UpdateMemberResponse DTO 객체를 별도로 만든다.
  - 수정 기능은 굉장히 제한적이기 때문에 Regist와는 별개의 DTO를 새로 만든다.
  - 기존에 만들어놓은 updateMember() 함수가 주소값까지 모두 파라미터로 받게 되어 있다. (id, name, country, city, zipcode)
    - 이에 요청 DTO(updateMemberRequest에는 모든 값을 저장하도록 하지만 별도의 @NotEmpty 어노테이션이 없으므로 변경을 원치 않는 값은 공백으로 전송이 가능하며, 받은 요청 데이터만 수정하여 응답은 id, name만을 내보내도록 설계하였다.

## 회원조회 API(전체 List 조회)
  - application.yml jpa.hibernate.ddl-auto: none 으로 setting
  - GET 요청을 보냈을 때, aborted error가 발생하였다.
  - 양방향 연관관계 참조 시 에러 발생. 객체 간의 무한 순환 참조가 발생하여 JSON을 작성하지 못하고 있는 상태가 발생한다. 예를 들어, 부모 객체가 자식 객체를 참조하고 자식 객체가 다시 부모 객체를 참조할 때 발생 가능하다.
    - JSON 직렬화 시 순환 참조 처리를 위해 jackson 라이브러리의 @JsonIgnore를 Member 엔티티 내 List<Order> orders 필드에 걸어줌으로써 한쪽에서의 참조를 끊어줌으로서 순환 참조를 멈추고 정상적으로 json 데이터를 반환하였다.
  - v1 API
    ```java
    @GetMapping("/api/v1/members")
    public List<Member> memberListV1() {
        return memberServiceSpringDataJpa.findAllMembers();
    } 
    ```
    - 엔티티의 모든 필드가 정제 없이 클라이언트 응답에 노출된다.
    - 엔티티 자체에 @JsonIgnore 등의 어노테이션이 붙어 화면 계층을 위한 로직이 붙는다. 이는 특정 API 의존도를 높이기 때문에 좋은 것이 아니다.
    - 엔티티가 변경되면 API 스펙이 변하며, 컬렉션을 직접 반환하면 향후 API 스펙을 변경하기 어렵다. json의 시작이 {}로 시작하지 않고 []로 나올 경우 스펙이 굳어서 확장이 불가능하다. 유연성이 확 떨어진다.
    --> 이러한 이슈들을 해결하기 위해 API 응답 스펙에 맞추어 별도의 DTO를 만든다.
  - v2 API
    ```java
    @GetMapping("/api/v2/members")
    public CustomFormat memberListV2() {
        List<Member> allMembers = memberServiceSpringDataJpa.findAllMembers();
        List<MemberDto> memberDtoList = allMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new CustomFormat(memberDtoList);
    }

    //====== memberList =====//
    // api/v2/members의 반환 자료형
    // [] 타입 응답에서 {} 타입의 json object(객체) 형태로 응답해주기 위해 껍데기를 한 번 씌워주었다. 향후 확장성을 고려하여 꼭 필요한 작업이다.
    @Data
    @AllArgsConstructor
    static class CustomFormat<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
    ```
    - v2 API를 위한 DTO 밑 반환형을 두어 엔티티를 직접 사용하는 v1에서 발생할 수 있는 여러 문제들을 해결하도록 하였다.

# API 실무적 개발(성능 최적화)
  - CREATE, UPDATE는 성능 문제가 거의 발생하지 않는데 대부분의 성능 저하는 조회에서 발생한다. 장애의 90% 이상이 조회 API에서 나온다.
  - 필요한 최소환의 쿼리만 나가야하는데 조금이라도 엮인 쿼리들이 너무 많이 나가다보니 데이터를 확인하여 가져오는 과정에서 성능 저하가 발생하는 것이다.
  - join의 관점에서 일대일, 다대일과 같이 조인을 해도 데이터가 방대해지지 않는 경우에는 조인을 비교적 막 해도 되고 성능 이슈가 잘 없다.
  - 그런데 일대다 조인이 되면 1개 당 N개 데이터가 붙어서 데이터가 방대해져버리므로 이러한 상황에서 성능 최적화하는 것이 쉽지 않다.

## 주문 + 회원 + 배송정보 조회 API 개발 (__ToOne 연관관계)
> 본 API 개발을 통해, 지연 로딩으로 인해 발생할 수 있는 문제를 확인하고, 성능을 잡아나갈 수 있는 방식을 설명한다.
> '일대일' 또는 '다대일' 연관관계를 갖는 형태로 join으로 인한 성능 저하가 비교적 덜 발생하는 저난이도의 API에서의 성능 최적화를 어떻게 할 수 있을 것인가. (__ToOne)
- OrderApiControllerL1 V1: 엔티티 직접 노출
  - 일단 json 순환 참조를 막기 위해 양방향이 걸리는 데는 모두 다 한 쪽에 가서 @JsonIgnore를 걸어주어 한 쪽을 끊어줘야 한다. ex) Member에 있는 order, OrderProducts에 있는 order, Delivery에 있는 order
  - 무한 참조를 해결했더니 또 다른 에러가 발생했다. (Type definition error: [simple type, class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor]) 이하생략
    - 지연 로딩(FetchType.LAZY)로 설정하면서 발생하는 오류로, 지연 로딩을 하면서 연관관계에 있는 엔티티들은 실제 엔티티 객체가 아닌 프록시 객체로 가지고 있는데, 그 상태에서 데이터를 엮어서(serialize) 가져올 수 없어서 발생한다.
    - 프록시 객체에서 데이터를 갖고 오려하는, 지연로딩이 걸려있는 연관관계 필드들을 일단 null로 값을 가져오는 형태로 일단 해결할 수 있는 방법으로 해결이 가능하다.(Hibernate5JakartaModule 설치 및 Spring Bean으로 등록하는 방법)
      ```java
      // build.gradle
      implementation 'com.fasterxml.jackson.datatype:jackson-datatype-hibernate5-jakarta'

      // mainApplication
      @Bean
      Hibernate5JakartaModule hibernate5JakartaModule() {
      	return new Hibernate5JakartaModule();
      }
      ```
      이 형태로 모듈을 활용해주면 프록시 객체에서의 지연 로딩으로 발생하는 문제를 해결할 수 있다.
    - Hibernate5JakartaModule 활용으로 인해 특정 필드 값이 null로 반환되는데, LAZY LOADING 초기화를 통해 원하는 값을 밀어넣는 방법
      ```java
      for (Order order : allOrders) {
        order.getMember().getName(); // LAZY loading 강제 초기화
        order.getDelivery().getAddress(); // LAZY loading 강제 초기화
      }
      ```
- OrderApiControllerL1 V2: 엔티티 > DTO 변환
  > 엔티티를 가져오되, 중간 DTO를 거쳐 데이터를 한 차례 가공하여 엔티티를 직접 노출하지 않고 DTO를 최종 리턴 값으로 반환하는 형태.
  - 반환 형태가 Dto를 거친 리스트 형태로 반환.
    ```java
    @GetMapping("/api/v2/orders")
    public List<OrderDtoL1> ordersV2() { // 실무에서는 List 반환은 안된다. Result로 감싸서 {} 객체 형태로 반환해야 한다.
        List<Order> allOrders = orderRepository.findAllOrders(new SearchOption());
        List<OrderDtoL1> resultDto = allOrders.stream()
                .map(o -> new OrderDtoL1(o))
                .collect(Collectors.toList());
  
        return resultDto;
    }
  
    // DTO - Dto 패키지에 별도 정의
    @Data
    public class OrderDtoL1 {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
    
        public OrderDtoL1(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getOrderStatus();
            address = order.getDelivery().getAddress();
        }
    
    }
    ```
  - 반환 형태가 Dto를 거친 리스트를 다시한 번 ObjectFormat을 거쳐 json 객체 {} 형태로 반환.
    ```java
    @GetMapping("/api/v2-object/orders")
    public ObjectFormat objectOrdersV2() {
        List<Order> allOrders = orderRepository.findAllOrders(new SearchOption());
        List<OrderDtoL1> resultDto = allOrders.stream()
                .map(o -> new OrderDtoL1(o))
                .collect(Collectors.toList());

        return new ObjectFormat(resultDto);
    }

    // ObjectFormat
    @Data
    @AllArgsConstructor
    public class ObjectFormat<T> {
        private T data;
    }
    ```
- OrderApiControllerL1 V3: 엔티티 > DTO 변환, fetch join 통한 쿼리 최적화 및 성능 향상
  ```java
  @GetMapping("api/v3-object/orders")
  public ObjectFormat objectOrdersV3() {
      List<Order> allOrders = orderRepository.findAllUsingMemberDelivery();
      List<OrderDtoL1> resultDto = allOrders.stream()
              .map(order -> new OrderDtoL1(order))
              .collect(Collectors.toList());

      return new ObjectFormat(resultDto);
  }

  // orderRepository.findAllUsingMemberDeliversy()
  public List<Order> findAllUsingMemberDelivery() {
      List<Order> resultOrders = em.createQuery(
              "select o from Order o" +
                      " join fetch o.member m" +
                      " join fetch o.delivery d", Order.class
      ).getResultList();
      return resultOrders;
  }
  ```
  - Mapper 통한 과정은 이전과 동일하다.
  - V2까지는, LAZY loading으로 인해 N+1 문제가 발생하여 쿼리가 비효율적으로 많이 나가 데이터 조회 성능이 좋지 못했다.
  - V3에서는, 주문 리포지토리에 findAllUsingMemberDelivery() 라는 메서드를 새로이 정의하였고, 쿼리에서 fetch join을 사용하여 주문, 회원, 배송 정보를 한 번에 조회하여 성능을 향상시키고자 하였다.
    ```sql
    # sql query 결과 로그
    select
      o1_0.order_id,
      d1_0.delivery_id,
      d1_0.city,
      d1_0.country,
      d1_0.zipcode,
      d1_0.delivery_status,
      m1_0.member_id,
      m1_0.city,
      m1_0.country,
      m1_0.zipcode,
      m1_0.name,
      o1_0.order_date,
      o1_0.order_status
    from orders o1_0
      join member m1_0 on m1_0.member_id=o1_0.member_id
      join delivery d1_0 on d1_0.delivery_id=o1_0.delivery_id;
    ```
- OrderApiControllerL1 V4: JPA에서 DTO로 바로 조회, 좀 더 높은 수준의 성능 최적화가 가능하다.
  > DTO는 Entity를 참조해도 괜찮다. 그러나 반대로 Entity가 DTO를 참조해선 안된다.
  - JPA는 엔티티 또는 value object만 반환이 가능하다. DTO를 반환 형식으로 사용할 수는 없기에 조작을 좀 해줘야 한다.
    ```java
    // AS-IS
    public List<OrderJpaDirectDto> findOrderDtoList() {
        List<OrderJpaDirectDto> resultDtoList = em.createQuery(
                "select o from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderJpaDirectDto.class
        ).getResultList();

        return resultDtoList;
    }

    // orderRepository.findOrderDtos()
    // TO-BE
    public List<OrderJpaDirectDto> findOrderDtoList() {
        List<OrderJpaDirectDto> resultDtoList = em.createQuery(
                "select new jpa.commerce.api.order.dto.OrderJpaDirectDto(o.id, m.name, o.orderDate, o.orderStatus, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderJpaDirectDto.class
        ).getResultList();

        return resultDtoList;
    }
    ```
    - select o -> select new jpa.commerce.api.order.dto.OrderJpaDirectDto(o.id, m.name, o.orderDate, o.orderStatus, d.address)로 변경하여 DTO를 바로 가지고 오도록 변경하였다.
      ```sql
      # sql query 결과 로그
      select
        o1_0.order_id,
        m1_0.name,
        o1_0.order_date,
        o1_0.order_status,
        d1_0.city,
        d1_0.country,
        d1_0.zipcode
      from orders o1_0
        join member m1_0 on m1_0.member_id=o1_0.member_id
        join delivery d1_0 on d1_0.delivery_id=o1_0.delivery_id;
      ```
      - V3 fetch join 대비 원하는 데이터들만 간략하게 가져오는 것을 확인할 수 있다. 직접 쿼리를 짰기 때문에 당연한 것이다.
      - fetch join은 이렇게 select 절에서 데이터를 퍼올리는 과정에서 데이터를 불필요한 것까지 많이 가지고 온다.
--> V3, V4 간에는 trade-off가 존재하기 때문에 무엇이 맞다고 이야기하기 어렵다.
  - V3의 경우, 많은 API에서 활용 가능성이 열려 있다. fetch join 된 테이블에서 원본 주문 리스트를 얻고 여러 DTO를 적용하여 추가 가공이 가능하도록 열려 있는 것이다.
  - v4의 경우, 애초에 가질 값을 위해 쿼리를 fit하게 작성해버렸기 때문에 추가 가공이 어려워 다른 API에서의 활용 가능성이 없는 것이다. 물론 쿼리의 수가 적기 때문에 성능 측면에서는 V3보다 우수하다. 그러나 사실 최근에는 네트워크 성능이
    좋아져서 이런 차이에서 발생하는 성능 차이는 거의 존재하지 않는다고 봐도 무방하다. 테이블간 join 하는 과정에서 네트워크 리소스를 많이 먹을 뿐, select 절에서 가지고 오는 필드 수는 크게 영향이 없다.
    > Repository는 엔티티에 대한 객체 그래프를 조회하는 용도에 핵심적으로 사용되어야 하는데, V4의 경우 API 스펙에 맞춰서 쿼리가 핏하게 짜져있어 용도에 벗어났다고 볼 수 있다. Repository가 화면에 의존하며 API 스펙이 바뀐 상황에서
      계속 활용하고자 할 경우 다시 뜯어고쳐야 한다.

### V4 및 전체 정리
  - V4:
    1. 일반적인 SQL을 사용할 때 처럼, 원하는 값만 반환하기 위한 JPQL 작성
    2. em.createQuery의 select 에서 new 명령어를 사용하여 JPQL의 결과를 DTO로 변환
    3. select 절에서 원하는 데이터를 지정하는 방식이므로 DB -> 애플리케이션 서비스 간 네트워크 용량 최적화
    -> 직접 원하는 값을 지정하므로 속도(성능)의 향상을 기대할 수 있지만, repository 재사용성이 떨어지고 특정 API 스펙에 맞춘 메서드가 repository 내 위치함으로써 리포지토리의 본질적 목적에서 벗어나게 함.

#### 문제점에 대한 해결책
  > Entity로 조회한 뒤 DTO를 통해 가공하면 리포지토리의 재사용성이 좋고, 개발도 한결 간편해진다는 것을 기억한다.
  - 조회 성능 향상을 위해 특정 API 용으로 fit하게 설계한 repository 내 메서드를 별도 패키징하여, 리포지토리는 본질적 목적을 준수하도록 한다.
  ```java
  - jpa
    - commerce
      - api
        - ...
        - order
          - repository
            - queryRepository
          - controller
            - OrderApiControllerL1
          - dto
            - ObjectFormat
            - OrderDtoL1
            - OrderJpaDirectDto
  ```

#### query 방식으로 조회 시 권장하는 개발 방법론 순서
  1. Entity를 직접 조회한 후 DTO로 변환하여 사용 - ordersV2()
  2. fetch join 하여 성능 최적화 - ordersV3() (왠만하면 이 단계에서 성능 이슈는 잡힌다.)
  3. 특정 API용 조회 query를 통해 DTO 직접 조회 - ordersV4()
  4. 3단계 까지의 과정으로 최적화가 안된다면, native SQL 이나 JDBC template을 사용하여(EntityManager를 사용하는 것이 아닌) SQL을 직접 다루어 조회한다.

## 주문 + 회원 + 배송정보 조회 API 개발 (__ToMany 연관관계, 컬렉션 조회)
> ToOne 관계 시 가지고 오던 엔티티에 더해서, OrderProduct와 Product 엔티티 데이터까지 함께 가져온다.
- OrderApiControllerL2 V1: 엔티티 직접 노출
- OrderApiControllerL2 V2: property 에러가 날 경우, 대부분은 @Getter, @Setter, @Data 등의 필수 어노테이션이 누락된 경우이다.
  ```json
  {
      "orderId": 1,
      "name": "m1",
      "orderDate": "2024-05-03T14:25:12.248311",
      "orderStatus": "ORDER",
      "address": {
          "country": "한국",
          "city": "부산",
          "zipcode": "12345"
      },
      "orderProductList": null
  }
  ```
  - 기존 데이터들은 정상적으로 잘 나오는데, 엔티티인 OrderProduct는 기댓값이 아닌 null이 출력되는 것을 확인할 수 있다.
  ```java
  public OrderDtoL2(Order order) {
      orderId = order.getId();
      name = order.getMember().getName();
      orderDate = order.getOrderDate();
      orderStatus = order.getOrderStatus();
      address = order.getDelivery().getAddress();
      
      order.getOrderProducts().stream().forEach(orderProduct -> orderProduct.getProduct().getName());
      orderProductList = order.getOrderProducts();
  }
  ```
  - OrderProduct를 Dto 생성자에서 초기화해주어 Hibernate5JakartaModule로 인해 값이 null로 출력되는 것을 해결할 수 있다.
  - Dto를 활용했으나 필드 중 List<OrderProduct> orderProductList; 즉 랩핑된 형태의 엔티티가 존재하며, API 개발 시 주문상품에 대한 데이터 전체가 노출이 되어 버리는 점에서 개선이 필요하다.
    - 단순히 Dto의 형태로 엔티티를 감싸서 사용하는 것만으로는 근본적 해결책이라 할 수 없다.
    - 본 상황에서는, OrderProduct 까지도 Dto로 변환하여 사용해야 한다.
    ```java
    private List<OrderProductDto> orderProductList;

    public OrderDtoL2(Order order) {
      orderId = order.getId();
      name = order.getMember().getName();
      orderDate = order.getOrderDate();
      orderStatus = order.getOrderStatus();
      address = order.getDelivery().getAddress();

      orderProductList = order.getOrderProducts().stream()
              .map(orderProduct -> new OrderProductDto(orderProduct))
              .collect(Collectors.toList());
    }

    @Data
    public class OrderProductDto {
    
        private String name;
        private int orderPrice;
        private int count;
    
        public OrderProductDto(OrderProduct orderProduct) {
            name = orderProduct.getProduct().getName();
            orderPrice = orderProduct.getOrderPrice();
            count = orderProduct.getCount();
        }
    }
    ```
    - Order > Dto 변환 과정에서, OrderProduct 까지 별도의 Dto를 구성하여 null값 노출을 막고 OrderProduct 엔티티 전체가 노출되는 것을 방지한다.
    - Dto로 변환하고자 하는 엔티티 내부에서 또 다른 엔티티를 연관관계로 가지는 필드가 있다면, 그 엔티티까지도 Dto를 활용하여 이중 변환 해줘야 문제가 없다.
      ```json
      // API 수행 결과
      {
        "orderId": 1,
        "name": "m1",
        "orderDate": "2024-05-03T15:16:01.352796",
        "orderStatus": "ORDER",
        "address": {
            "country": "한국",
            "city": "부산",
            "zipcode": "12345"
        },
        "orderProductList": [
            {
                "name": "cn1",
                "orderPrice": 10000,
                "count": 10
            },
            {
                "name": "cn2",
                "orderPrice": 20000,
                "count": 20
            }
        ]
      }
      ```
  ### V2 동작 로직
    1. Repository에서 모든 주문을 조회한다.
    2. 주문 엔티티 원본을 주문 Dto로 변환한 뒤 리스트로 반환한다. 변환 시 생성자로 주문 엔티티를 파라미터로 넘기고, Dto에서는 넘겨받은 엔티티 데이터를 활용해서 값을 초기화한다.
    3. 그런데 Order 내부에 OrderProduct라고 하는 엔티티가 연관관계로 존재한다. OrderProduct 원본을 Dto로 변환하고 마찬가지로 리스트로 반환한다. 초기화 과정은 Order Dto와 동일.
    4. Order, OrderProduct 모두 직접적인 엔티티 조회 없이 원하는 값들만 지정하여 API로 호출이 가능하게 되었다.

  ### V2 SQL query 및 성능
  ```sql
  select o1_0.order_id,o1_0.delivery_id,o1_0.member_id,o1_0.order_date,o1_0.order_status from orders o1_0 join member m1_0 on m1_0.member_id=o1_0.member_id fetch first 100 rows only;
  select m1_0.member_id,m1_0.city,m1_0.country,m1_0.zipcode,m1_0.name from member m1_0 where m1_0.member_id=1;
  select d1_0.delivery_id,d1_0.city,d1_0.country,d1_0.zipcode,d1_0.delivery_status from delivery d1_0 where d1_0.delivery_id=1;
  select o1_0.order_id,o1_0.delivery_id,o1_0.member_id,o1_0.order_date,o1_0.order_status from orders o1_0 where o1_0.delivery_id=1;
  select op1_0.order_id,op1_0.order_product_id,op1_0.count,op1_0.order_price,op1_0.product_id from order_product op1_0 where op1_0.order_id=1;
  select p1_0.product_id,p1_0.dtype,p1_0.name,p1_0.price,p1_0.stock_quantity,p1_0.upload_file_upload_file_id,p1_0.author,p1_0.isbn,p1_0.actor,p1_0.director,p1_0.brand,p1_0.etc from product p1_0 where p1_0.product_id=1;
  select p1_0.product_id,p1_0.dtype,p1_0.name,p1_0.price,p1_0.stock_quantity,p1_0.upload_file_upload_file_id,p1_0.author,p1_0.isbn,p1_0.actor,p1_0.director,p1_0.brand,p1_0.etc from product p1_0 where p1_0.product_id=2;
  select m1_0.member_id,m1_0.city,m1_0.country,m1_0.zipcode,m1_0.name from member m1_0 where m1_0.member_id=2;
  select d1_0.delivery_id,d1_0.city,d1_0.country,d1_0.zipcode,d1_0.delivery_status from delivery d1_0 where d1_0.delivery_id=2;
  select o1_0.order_id,o1_0.delivery_id,o1_0.member_id,o1_0.order_date,o1_0.order_status from orders o1_0 where o1_0.delivery_id=2;
  select op1_0.order_id,op1_0.order_product_id,op1_0.count,op1_0.order_price,op1_0.product_id from order_product op1_0 where op1_0.order_id=2;
  select p1_0.product_id,p1_0.dtype,p1_0.name,p1_0.price,p1_0.stock_quantity,p1_0.upload_file_upload_file_id,p1_0.author,p1_0.isbn,p1_0.actor,p1_0.director,p1_0.brand,p1_0.etc from product p1_0 where p1_0.product_id=3;
  select p1_0.product_id,p1_0.dtype,p1_0.name,p1_0.price,p1_0.stock_quantity,p1_0.upload_file_upload_file_id,p1_0.author,p1_0.isbn,p1_0.actor,p1_0.director,p1_0.brand,p1_0.etc from product p1_0 where p1_0.product_id=4;
  ```
  - 엔티티의 직접적인 노출을 막고 Dto를 활용하여 원하는 데이터를 뽑아내는데 주력하여, 성능적인 측면이 고려되어 있지 않아 조회 시 위와 같이 과도한 쿼리가 발생하는 것을 확인할 수 있다.

- OrderApiControllerL2 V3: fetch join 통한 쿼리 최적화
  > DataBase에서의 distinct 옵션이 적용되려면, 모든 필드 값이 동일해야 한다. 필드가 5개라면 모든 필드의 값이 동일해야 distinct 옵션이 정상적으로 걸리는 것이다.
  > 그러나 JPA에서 println 또는 log 를 통해 Order id 값을 조회해보면, 중복 id값을 가지는 애들은 중복을 제거하여 하나만 출력해준다. 이것은 DB와는 관련 없이 JPA 자체적으로 제공해주는 기능이다.
  > 그래서, postman 통해 API 조회했을 때 중복 제거되며, 다만 데이터베이스 테이블에서는 위와 같은 이유로 중복이 제거되지 않고 기존 데이터가 뻥튀기 된 채로 유지된다.
    ```java
    public List<Order> findAllUsingProduct() {
        List<Order> resultOrderList = em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderProducts op" +
                        " join fetch op.product p", Order.class
        ).getResultList();
        return resultOrderList;
    }
    ```
    - 위 코드블럭에 있는 'distinct' 명령어는, 데이터베이스에 distinct 키워드를 날려주고, root entity가 중복될 경우 id값 기준으로 중복을 걸러서 컬렉션에 담아주는 2가지 기능을 제공한다.
    - 컬렉션 조회(__ToMany)에서의 fetch join을 통한 쿼리 최적화는, 명확한 장단점을 가지고 있다.
      - 장점: fetch join을 통해 N번 수행되던 SQL 쿼리가 1번만 수행됨, 일대다 데이터로의 뻥튀기가 되어 데이터 row가 증가하고 중복이 생기지만 distinct 명령어를 통해 충분히 해결이 가능하다. 조회를 원하는 엔티티가 fetch join
        으로 인해 중복 조회는 큰 문제가 아니라는 것이다.
      - 단점: 페이징(Paging)이 안된다. 일대다를 fetch join 하는 순간 페이징 쿼리가 안나간다.(setFirstResult(), setMaxResult())
        - 일대다 fetch join에서 set__Result()와 같은 페이징 메서드를 사용할 경우, 하이버네이트가 WARN 경고와 함께 Memory 내부에서 페이징 처리한 다음 결과를 반환한다. 대규모 서비스에서는 out of memory로 큰일이 날 것이다.
          ```java
          public List<Order> findAllUsingProduct() {
              List<Order> resultOrderList = em.createQuery(
                      "select distinct o from Order o" +
                              " join fetch o.member m" +
                              " join fetch o.delivery d" +
                              " join fetch o.orderProducts op" +
                              " join fetch op.product p", Order.class)
                      .setFirstResult(1)
                      .setMaxResults(10)
                      .getResultList();
              return resultOrderList;
          }
          ```
          ```text
          // tomcat 서버 실행 결과 확인 가능 WARN log
          2024-05-05T11:36:56.503+09:00  WARN 2298 --- [nio-8080-exec-1] org.hibernate.orm.query                  : HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory
          ```
        - 일대다 fetch join은 1개 엔티티로만 적용하할 수 있다. 그 이상이 될 경우 데이터 정합성이 깨질 수 있다.

  - OrderApiControllerL2 V3.1: fetch join 통한 쿼리 최적화 - 엔티티 > DTO 변환, 일대다 연관관계 페이징
    > 일대다 컬렉션을 fetch join 하면 다(N)을 기준으로 데이터가 확장되기 떄문에 그 상태에서 페이징 적용이 불가하다. (Order를 기준으로 페이징을 원하나 데이터가 확장되는 순간 하위 컬렉션을 기준으로 페이징이 되버린다.
    > 페이징의 목적은 다(N)이 아니라, 일(1)을 기준으로 페이징 하는 것이다.
    > V3에서 확인했듯 이런 경우, 하이버네이트가 경고 로그와 함께 메모리로 모든 데이터를 퍼올려 페이징을 시도한다. 이는 매우 위험한 것이다.
    - 컬렉션 엔티티 조회 + 페이징 까지 적용 할 수 있는 방법은 무엇일까?
      1. __ToOne: fetch join 한다. 몇 번이고 fetch join 해도 크게 어려움이 없다.
      2. __ToMany(컬렉션): fetch join 하지 않고 지연 로딩으로 조회한다.(fetch = FetchType.LAZY)
      3. 지연 로딩 하면서도 성능 최적화를 위해 'hibernate.default_batch_fetch_size', '@BatchSize' 옵션을 적용한다.
         - hibernate_default_batch_fetch_size: 전체 프로젝트에 적용되는 글로벌 설정
         - @BatchSize: 개별 메서드에 적용되는 최적화 어노테이션
         -> 이 최적화 옵션을 프록시 객체나 컬렉션을 한꺼번에 설정한 size만큼 'IN' 쿼리로 조회한다.
    - 단 1개 쿼리를 통한 최소한의 조회 숫자를 보장하지는 않더라도, 적절히 최적화된 숫자의 sql 쿼리 횟수(1+N+M -> 1+1+1)와 페이징 적용 두 마리 토끼 모두를 잡을 수 있는 방식.
      > 1 + N + M: 전체 fetch join 쿼리(1) + fetch join 테이블 내 컬렉션1(N) + fetch join 테이블 내 컬렉션2(M)
        -> fetch join 쿼리(1) + batch_fetch_size 적용 통한 컬렉션 IN 쿼리1(1) + batch_fetch_size 적용 통한 컬렉션 IN 쿼리2(1)
      - SQL query: http://localhost:8080/api/v3.1-collection-object/orders
      - API 조회 결과
        ```json
        {
            "data": [
                {
                    "orderId": 1,
                    "name": "m1",
                    "orderDate": "2024-05-06T18:14:10.222436",
                    "orderStatus": "ORDER",
                    "address": {
                        "country": "한국",
                        "city": "부산",
                        "zipcode": "12345"
                    },
                    "orderProductList": [
                        {
                            "name": "cn1",
                            "orderPrice": 10000,
                            "count": 10
                        },
                        {
                            "name": "cn2",
                            "orderPrice": 20000,
                            "count": 20
                        }
                    ]
                },
                {
                    "orderId": 2,
                    "name": "m2",
                    "orderDate": "2024-05-06T18:14:10.260597",
                    "orderStatus": "ORDER",
                    "address": {
                        "country": "미국",
                        "city": "LA",
                        "zipcode": "98765"
                    },
                    "orderProductList": [
                        {
                            "name": "cn3",
                            "orderPrice": 30000,
                            "count": 30
                        },
                        {
                            "name": "cn4",
                            "orderPrice": 40000,
                            "count": 40
                        }
                    ]
                }
            ]
        }
        ```
        ```sql
        // __ToOne 연관관계 fetch join
        select
            o1_0.order_id,
            d1_0.delivery_id,
            d1_0.city,
            d1_0.country,
            d1_0.zipcode,
            d1_0.delivery_status,
            m1_0.member_id,
            m1_0.city,
            m1_0.country,
            m1_0.zipcode,
            m1_0.name,
            o1_0.order_date,
            o1_0.order_status 
        from
            orders o1_0 
        join
            member m1_0 
                on m1_0.member_id=o1_0.member_id 
        join
            delivery d1_0 
                on d1_0.delivery_id=o1_0.delivery_id 
        offset
            ? rows 
        fetch
            first ? rows only

        // default_batch_fetch_size = 10으로 설정했을 때, __ToMany 컬렉션 OrderProduct를 IN 쿼리로 가져오는 모습
        select
            op1_0.order_id,
            op1_0.order_product_id,
            op1_0.count,
            op1_0.order_price,
            op1_0.product_id 
        from
            order_product op1_0 
        where
            op1_0.order_id in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)

        // default_batch_fetch_size = 10으로 설정했을 때, __ToMany 컬렉션 Product를 IN 쿼리로 가져오는 모습
        select
            p1_0.product_id,
            p1_0.dtype,
            p1_0.name,
            p1_0.price,
            p1_0.stock_quantity,
            p1_0.upload_file_upload_file_id,
            p1_0.author,
            p1_0.isbn,
            p1_0.actor,
            p1_0.director,
            p1_0.brand,
            p1_0.etc 
        from
            product p1_0 
        where
            p1_0.product_id in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        ```
  - OrderApiControllerL2 V4: JPA에서 DTO를 바로 조회
    - OrderJpaDirectDtoL2를 생성자 초기화하는 과정에서 컬렉션을 파라미터로 넣어줄 수 없는 문제. (List<<OrderProductDtoL2>> OrderProductList;)
      - OrderJpaDirectDtoL2: JPQL 쿼리를 사용하여, JPA에서 다이렉트로 데이터 조회를 하기 위한 DTO
        ```java
        @Data
        public class OrderJpaDirectDtoL2 {
        
            private Long orderId;
            private String name;
            private LocalDateTime orderDate;
            private OrderStatus orderStatus;
            private Address address;
            private List<OrderProductDtoL2> orderProductList;
        
            public OrderJpaDirectDtoL2(Long orderId,
                                       String name,
                                       LocalDateTime orderDate,
                                       OrderStatus orderStatus,
                                       Address address) {
                this.orderId = orderId;
                this.name = name;
                this.orderDate = orderDate;
                this.orderStatus = orderStatus;
                this.address = address;
            }
        
        }
        ```   
      - OrderProductDtoL2: OrderJpaDriectDtoL2에 포함된 컬렉션 List<OrderProduct> OrderProducts를 처리하기 위한 DTO
        ```java
        @Data
        public class OrderProductDtoL2 {
        
            @JsonIgnore
            private Long orderId;
            private String name;
            private int orderPrice;
            private int count;
        
            public OrderProductDtoL2(Long orderId, String name, int orderPrice, int count) {
                this.orderId = orderId;
                this.name = name;
                this.orderPrice = orderPrice;
                this.count = count;
            }
        
        }
        ```
      - QueryRepository: JPQL을 통해 DTO를 직접 조회하는 메서드만 별도로 패키징한 리포지토리. 기본 Repository와 분리하여 코드 파악 및 유지보수를 용이하게 한다.
        - 특이점: OrderJpaDirectDtoL2에서 생성자 초기화하지 못한 컬렉션 OrderProduct List를 이 리포지토리 내에서 루프 돌리면서 컬렉션 채우는 로직이 수행된다.
        ```java
        public List<OrderJpaDirectDtoL2> findOrderJpaDirectDtoL2List() {
            List<OrderJpaDirectDtoL2> resultDtoList = findOrderList();
    
            // 컬렉션 부분은 위에서 처리가 안되므로, 다시 직접 가져와서 루프 돌면서 초기화(컬렉션 채우는 것) 작업을 추가적으로 해주고 있다.
            resultDtoList.forEach(o -> {
                List<OrderProductDtoL2> orderProductList = findOrderProductList(o.getOrderId());
                o.setOrderProductList(orderProductList);
            });
            return resultDtoList;
        }
        ```
    - 동작 원리 및 정리
      - query: 컬렉션 제외 루트 DTO 1회, 컬렉션 N회 실행
      - ToOne(다대일, 일대일) 연관관계를 먼저 조회하고, ToMany(컬렉션)은 각각 별도로 처리한다.(CustomRepository)
        - ToOne 연관관계는 fetch join을 아무리 해도 수평적으로 데이터 column이 붙을 뿐 row가 증가하지 않는다.
        - ToMany 연관관계는 fetch join 하는 순간 N을 기준으로 데이터가 확장되어 어지럽게 된다. 그러므로 상기 findOrderProductList 등의 별도 메서드를 구현하여 컬렉션은 별도 조회한다.
    - 결과 JSON
      ```json
      {
          "data": [
              {
                  "orderId": 1,
                  "name": "m1",
                  "orderDate": "2024-05-07T15:49:49.768189",
                  "orderStatus": "ORDER",
                  "address": {
                      "country": "한국",
                      "city": "부산",
                      "zipcode": "12345"
                  },
                  "orderProductList": [
                      {
                          "name": "cn1",
                          "orderPrice": 10000,
                          "count": 10
                      },
                      {
                          "name": "cn2",
                          "orderPrice": 20000,
                          "count": 20
                      }
                  ]
              },
              {
                  "orderId": 2,
                  "name": "m2",
                  "orderDate": "2024-05-07T15:49:49.793267",
                  "orderStatus": "ORDER",
                  "address": {
                      "country": "미국",
                      "city": "LA",
                      "zipcode": "98765"
                  },
                  "orderProductList": [
                      {
                          "name": "cn3",
                          "orderPrice": 30000,
                          "count": 30
                      },
                      {
                          "name": "cn4",
                          "orderPrice": 40000,
                          "count": 40
                      }
                  ]
              }
          ]
      }
      ```
    - 결과 SQL Query
      ```sql
      // Order 조회 (root 1회)
      select
          o1_0.order_id,
          m1_0.name,
          o1_0.order_date,
          o1_0.order_status,
          d1_0.city,
          d1_0.country,
          d1_0.zipcode 
      from
          orders o1_0 
      join
          member m1_0 
              on m1_0.member_id=o1_0.member_id 
      join
          delivery d1_0 
              on d1_0.delivery_id=o1_0.delivery_id

      // 컬렉션 N회 실행
      // Order 1에 속한 OrderProduct 조회
      select
          op1_0.order_id,
          p1_0.name,
          op1_0.order_price,
          op1_0.count 
      from
          order_product op1_0 
      join
          product p1_0 
              on p1_0.product_id=op1_0.product_id 
      where
          op1_0.order_id=1

      // Order 2에 속한 OrderProduct 조회
      select
          op1_0.order_id,
          p1_0.name,
          op1_0.order_price,
          op1_0.count 
      from
          order_product op1_0 
      join
          product p1_0 
              on p1_0.product_id=op1_0.product_id 
      where
          op1_0.order_id=2
      ```
  - OrderApiControllerL2 V5: JPA에서 DTO를 바로 조회 - 컬렉션 조회 최적화
    - Order의 OrderProduct들을 가지고 올 때, 일일이 루프를 돌리는 것이 아니라 IN 쿼리로 관련 항목을 한번에 긁어온 다음 Map을 사용하여 메모리에 올려놓고 필요할 떄 원하는 값만 꺼내서 set 해준다.
      ```java
      public List<OrderJpaDirectDtoL2> findOrderJpaDirectDtoL2List_optimized() {
          // root 1번 조회
          List<OrderJpaDirectDtoL2> resultOrders = findOrderList();
  
          // Order 데이터 만큼 메모리에 올린다.
          List<Long> orderIds = getOrderIds(resultOrders);
          Map<Long, List<OrderProductDtoL2>> orderProductMap = getOrderProductMap(orderIds);
  
          // 루프 돌며 컬렉션 데이터를, 메모리에 올려놓은 데이터를 활용하여 채운다.
          resultOrders.forEach(o -> o.setOrderProductList(orderProductMap.get(o.getOrderId())));
  
          // 결과값 반환
          return resultOrders;
      }
      ```
      - V4 DTO 조회에서 조회 성능 최적화를 위한 코드가 일부 추가되었다.
    - ToOne 연관관계를 먼저 조회하고, 이를 통해 얻은 식별자 orderId로 ToMany 관계인 Order 내 OrderPruduct를 한꺼번에 조회한다.
      ```java
      private static List<Long> getOrderIds(List<OrderJpaDirectDtoL2> resultOrders) {
          List<Long> orderIds = resultOrders.stream()
                  .map(o -> o.getOrderId())
                  .collect(Collectors.toList());
  
          return orderIds;
      }
      ```
    - Map<Long, List<exDtos>>를 적용하여 매칭 성능이 향상되도록 하였다.(O(1))
      ```java
      private Map<Long, List<OrderProductDtoL2>> getOrderProductMap(List<Long> orderIds) {
          List<OrderProductDtoL2> orderProductList = em.createQuery(
                          "select new jpa.commerce.api.order.dto.L2.OrderProductDtoL2(op.order.id, p.name, op.orderPrice, op.count)" +
                                  " from OrderProduct op" +
                                  " join op.product p" +
                                  " where op.order.id in :orderIds", OrderProductDtoL2.class)
                  .setParameter("orderIds", orderIds)
                  .getResultList();
  
          Map<Long, List<OrderProductDtoL2>> orderProductMap = orderProductList.stream()
                  .collect(Collectors.groupingBy(OrderProductDtoL2 -> OrderProductDtoL2.getOrderId()));
  
          return orderProductMap;
      }
      ```
      - Map으로 변환하기 전 JPQL 쿼리를 보면, '=' 등호에서 'in' 명령어로 바뀐 것을 확인할 수 있다. 이는 하위 엔티티를 IN 쿼리를 통해 한 번에 긁어옴으로써 루프를 돌릴때마다 별도 쿼리가 나가는 상황을 최적화한다.(O(N)->O(1))
      - 조회 쿼리 갯수 축소, 성능 최적화가 상당 부분 달성되고 있지만, 직접 작성해야하는 코드가 ~V4 대비 증가한다. 이러한 trade-off가 존재하므로, 개발환경 및 진행상황에 따라 적절한 방식을 선택하는 것이 중요하다.
    - Query: root 1회, 컬렉션: 1회
      ```sql
      // root query
      select
          o1_0.order_id,
          m1_0.name,
          o1_0.order_date,
          o1_0.order_status,
          d1_0.city,
          d1_0.country,
          d1_0.zipcode 
      from
          orders o1_0 
      join
          member m1_0 
              on m1_0.member_id=o1_0.member_id 
      join
          delivery d1_0 
              on d1_0.delivery_id=o1_0.delivery_id
      
      // collection query
      select
          op1_0.order_id,
          p1_0.name,
          op1_0.order_price,
          op1_0.count 
      from
          order_product op1_0 
      join
          product p1_0 
              on p1_0.product_id=op1_0.product_id 
      where
          op1_0.order_id in (1, 2)
      ```
  - OrderApiControllerL2 V5: JPA에서 DTO를 바로 조회 - flat data optimization
    - /api/order/dto/L2/OrderFlatDto
      ```java
      @Data
      public class OrderFlatDto {
          // Order - OrderProduct - Product join 하여 한방 쿼리로 가져온다.
      
          private Long orderId;
          private String name;
          private LocalDateTime orderDate;
          private OrderStatus orderStatus;
          private Address address;
      
          private String productName;
          private int orderPrice;
          private int count;
      
          public OrderFlatDto(Long orderId,
                              String name,
                              LocalDateTime orderDate,
                              OrderStatus orderStatus,
                              Address address,
                              String productName,
                              int orderPrice,
                              int count) {
              this.orderId = orderId;
              this.name = name;
              this.orderDate = orderDate;
              this.orderStatus = orderStatus;
              this.address = address;
              this.productName = productName;
              this.orderPrice = orderPrice;
              this.count = count;
          }
      }
      ```
    - /api/order/repository/queryRepositoryL2
      ```java
      public List<OrderFlatDto> findOrderJpaDirectDtoL2List_flatData() {
          List<OrderFlatDto> flatDtoList = em.createQuery(
                          "select new jpa.commerce.api.order.dto.L2.OrderFlatDto(o.id, m.name, o.orderDate, o.orderStatus, d.address, p.name, op.orderPrice, op.count)" +
                                  " from Order o" +
                                  " join o.member m" +
                                  " join o.delivery d" +
                                  " join o.orderProducts op" +
                                  " join op.product p", OrderFlatDto.class)
                  .getResultList();
  
          return flatDtoList;
      }
      ```
    - /api/order/controller/OrderApiControllerL2
      ```java
      @GetMapping("/api/v6-collection/orders")
      public List<OrderFlatDto> orderListV6() {
          List<OrderFlatDto> allOrders = queryRepositoryL2.findOrderJpaDirectDtoL2List_flatData();
          return allOrders;
      }
  
      @GetMapping("/api/v6-collection-object/orders")
      public CustomFormat objectOrderListV6() {
          List<OrderFlatDto> allOrders = queryRepositoryL2.findOrderJpaDirectDtoL2List_flatData();
          return new CustomFormat(allOrders);
      }
      ```
    - 동작원리 및 정리
      - Query: root 1번
      - Order, OrderProduct, Product 엔티티를 join하여 join 테이블에서 query 한방으로 모든 데이터를 한꺼번에 가져온다.
      - 쿼리가 한번이라는 점에서 장점이 있지만, join으로 인해 N을 기준으로 데이터가 뻥튀기 되는 과정에서 중복 데이터가 생길 수 밖에 없다. 이러한 이유로 오히려 조회 속도가 느려질 수도 있는 방식이다.
      - 조회한 flatData 형식을 원하는 반환 형식(ex.OrderJpaDriectDtoL2)으로 변환하기 위해 애플리케이션(컨트롤러 layer)에서 추가 작업 등이 필요할 수 있다.
      - join하여 N(OrderProduct)을 기준으로 데이터가 확장되었기 때문에 원하는 형태(Order 기준)로 Paging이 불가능하다.
    - 결과 JSON
      ```json
      {
          "data": [
              {
                  "orderId": 1,
                  "name": "m1",
                  "orderDate": "2024-05-09T12:43:20.735997",
                  "orderStatus": "ORDER",
                  "address": {
                      "country": "한국",
                      "city": "부산",
                      "zipcode": "12345"
                  },
                  "productName": "cn1",
                  "orderPrice": 10000,
                  "count": 10
              },
              {
                  "orderId": 1,
                  "name": "m1",
                  "orderDate": "2024-05-09T12:43:20.735997",
                  "orderStatus": "ORDER",
                  "address": {
                      "country": "한국",
                      "city": "부산",
                      "zipcode": "12345"
                  },
                  "productName": "cn2",
                  "orderPrice": 20000,
                  "count": 20
              },
              {
                  "orderId": 2,
                  "name": "m2",
                  "orderDate": "2024-05-09T12:43:20.78113",
                  "orderStatus": "ORDER",
                  "address": {
                      "country": "미국",
                      "city": "LA",
                      "zipcode": "98765"
                  },
                  "productName": "cn3",
                  "orderPrice": 30000,
                  "count": 30
              },
              {
                  "orderId": 2,
                  "name": "m2",
                  "orderDate": "2024-05-09T12:43:20.78113",
                  "orderStatus": "ORDER",
                  "address": {
                      "country": "미국",
                      "city": "LA",
                      "zipcode": "98765"
                  },
                  "productName": "cn4",
                  "orderPrice": 40000,
                  "count": 40
              }
          ]
      }
      ```
      - root query 한방으로 데이터를 가져왔으나, join으로 인해 데이터가 N을 기준으로 확장되고 데이터 중복이 발생한 것을 확인할 수 있다.(OrderProduct 4개 cn1, cn2, cn3, cn4 출력을 위해 Order 1, 2가 각각 2번씩 출력)
    - 결과 Query
      ```sql
      select
          o1_0.order_id,
          m1_0.name,
          o1_0.order_date,
          o1_0.order_status,
          d1_0.city,
          d1_0.country,
          d1_0.zipcode,
          p1_0.name,
          op1_0.order_price,
          op1_0.count 
      from
          orders o1_0 
      join
          member m1_0 
              on m1_0.member_id=o1_0.member_id 
      join
          delivery d1_0 
              on d1_0.delivery_id=o1_0.delivery_id 
      join
          order_product op1_0 
              on o1_0.order_id=op1_0.order_id 
      join
          product p1_0 
              on p1_0.product_id=op1_0.product_id
      ```
    - DataBase 조회 결과</br>
      <img width="600" alt="스크린샷 2024-05-09 오후 1 18 23" src="https://github.com/FutureMaker0/jpa_toypjt_commerce/assets/120623320/0e2c7bee-0b50-4b2b-8549-2b35a5d672b3">

### 프로젝트 진행 간 느낀점
  - API 개발을 크게 'Entity를 직접 조회(이후 Dto로 가공)'와 'JPA에서 Dto 직접 조회' 크게 2가지 방식으로 구분하여 진행했는데, 각각의 장단점에 대해 고민하였다.
  - 그 과정에서 '성능의 최적화'와 '코드 복잡도 증가' 사이에서 어떤 것이 더 효율적이고 적합한가에 대해 많은 고민을 했고, 다양한 방식을 시도해볼 수 있었다.


