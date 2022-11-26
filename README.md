# Gold Miner

## 충전코드와 유효기한이 있는 금

## 사용 기술

* Language: Kotlin 1.6
* target JDK: JDK 17
* SpringBoot MVC
* H2 database
* Spring Data JPA
* kotest, mockkito

## 실행 방법

```
./gradlew bootRun
```

## Test 실행

```
./gradlew test
```

## Test 영역

* Controller, Controller advice unit test
* Domain layer use case test with FakeRepository
* Infra layer(Spring Data JPA)

## Happy path 사용

* /http/happypath 디렉토리의 http 이용
  * 사용자 context 변경 시 logout 을 꼭 해주세요!

## Error path 사용

* /http/errorpath 디렉토리의 http 이용
  * 사용자 context 변경 시 logout 을 꼭 해주세요!

## 구현 범위

* ADMIN
    * 충전 코드 생성
        * POST /admin/redeemcodes
    * 충전 코드 조회
        * GET /admin/redeemcodes
        * paging 추가
* 일반 사용자
    * 충전 코드 활성화를 통한 금 충전
        * POST /users/{userId}/golds/earn-by-redeem
    * 현재 소유 금 조회
        * GET /user/{userId}/golds
        * entity tag cache 추가
    * 금 사용하기
        * POST /users/{userId}/golds/consume

## 주요 도메인

* GoldLedger
    * 금의 충전내역 및 사용내역을 기입
* GoldBalance
    * 유저의 현재 금 소유 현황
* RedeemCode
    * 충전코드 내용 기입

## 개발적으로 신경 쓰고자 한 부분

* Clean Architecture
* Domain Entity <-> Infra Entity Segregation (순수 도메인 객체)
* 불변객체 적극 활용
* Use Case 기반
* 영역간 의존성을 domain 을 바라보는 방향으로 (domain은 다른 영역으로의 의존성 없도록)
    * request 영역 -> domain
    * infra 영역 -> domain
* multi module 사용 (api, core)
* 충전 코드의 중복 활성화 방지를 위해 낙관적 락 사용

## 주요 검증

* /admin path 아래는 admin role 을 가진 유저만 접근 가능 (충전코드 생성 및 조회)
* /users path 아래는 admin role 혹은 user role 가져야 하고, user 인 경우 자기 자신의 금에 대해서만 변경 및 조회 가능

## 기타 검증

* 사용자 입력값 검증 (코드 길이, 금액 양수, 코드만료 기한 미래여야 함, 골드의 만료등)

## 회고

* 쉽게 생각했던 골드 차감 로직 버그 잡느라고 테스트 코드를 많이 늘렸습니다.
* RedeemController test code 가 두개인데, Security Filter Chain 이 WebMvcTest 진행 시 문제가 좀 있는 것 같아서 나누어서 진행했습니다.
    * 관련 issue : https://github.com/spring-projects/spring-boot/issues/31162
* spring security......
