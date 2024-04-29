# jpa_commerce_restAPI
jpa 기반 커머스 웹 서비스 API 설계 및 구현 리포지토리
jpa_toypjt_commerce 프로젝트와 기본적인 MVC 코드를 공유하며, rest API 방식의 서비스를 위한 api controller를 추가하는 방식으로 진행

## 프로젝트 트리 구성
- jpa
  - commerce
    - domain
    - repository
    - service
    - web
      - form
      - controller
    - exception
    - file
    - api
      - MemberApiController
     
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







