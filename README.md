# jpa_commerce_restAPI
jpa 기반 커머스 웹 서비스 API 설계 및 구현 리포지토리
jpa_toypjt_commerce 프로젝트와 기본적인 MVC 코드를 공유하며, rest API 방식의 서비스를 위한 api controller를 추가하는 방식으로 진행

# 기술 스택
  - Spring Data Jpa

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
        - dto
          - ObjectFormat
          - OrderDtoL1
     
## API 어노테이션
  - @RestController: @Controller + @ResponseBody
  - @RequestBody: json으로 넘어온 body를 객체로 바로 매핑해서 값을 넣어준다. (json data -> Entity 객체)

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

## 회원수정 API(method="PUT") - 멱등하다
  - 회원이름 정보 수정 API
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

## 회원 + 주문 + 배송정보 를 조회하는 API 개발
> 본 API 개발을 통해, 지연 로딩으로 인해 발생할 수 있는 문제를 확인하고, 성능을 잡아나갈 수 있는 방식을 설명한다.
> '일대일' 또는 '다대일' 연관관계를 갖는 형태로 join으로 인한 성능 저하가 비교적 덜 발생하는 저난이도의 API에서의 성능 최적화를 어떻게 할 수 있을 것인가. (__ToOne)
- OrderApiController V1: 엔티티 직접 노출
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
- OrderApiController V2: 엔티티 > DTO 변환
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
- OrderApiController V3: 엔티티 > DTO 변환, fetch join 통한 쿼리 최적화 및 성능 향상
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











