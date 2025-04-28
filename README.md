# 🐶😺 펫토리 (Petory)

- **펫토리**는 반려동물 **용품 판매**와 **커뮤니티 기능**을 통합한 플랫폼입니다.  
- 사용자는 반려동물에 대한 정보를 공유하거나, 필요한 용품을 구매할 수 있습니다.

## 📌 프로젝트 개요
- **프로젝트명:** 펫토리
- **개발 기간:** 2025.02 ~ 2025.04 (총 8주)
- **팀 구성:** 총 5명 (팀장 1명, 팀원 4명)


## 🎯 프로젝트 핵심 목표

- 반려동물 용품 구매와 정보 공유가 가능한 **커뮤니티 기반 통합 플랫폼** 개발  
- **도커 기반의 CI/CD 자동화 환경** 구축으로 안정적인 서비스 제공  
- **접근 권한 분리**를 통한 보안성과 관리 효율성 향상
  
## 🏗️ 아키텍처

- **Spring MVC 패턴**: Controller → Service → Repository → DB 흐름
- **DTO, Entity 분리**: 데이터 구조 명확화 및 불필요한 의존성 제거
- **AWS RDS(MariaDB)**: 안정적인 데이터 관리
- **Docker + EC2**: 서버 배포 및 관리

## 🧩 디자인 패턴

- **싱글톤 패턴**: Service, Repository에서 빈 단일 인스턴스 관리
- **MVC 패턴**: 요청과 응답 분리로 유지보수성 향상
- **DTO 패턴**: 계층 간 데이터 전달 시 의존성 제거

## 👤 내 역할

- **장바구니 기능 개발**
  - 사용자가 장바구니에 상품을 추가, 삭제 및 수량을 업데이트 할 수 있도록 구현
  - 장바구니 총 금액 계산 및 세션 관리 기능 개발
  - 장바구니 조회 및 관리 API 설계 및 구현

## 🛠️ 기술 스택

| 구분         | 스택                                                                                                                                                                                                                  |
|--------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Backend**  | ![Java](https://img.shields.io/badge/Java_21-007396?style=for-the-badge&logo=java&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white) ![JPA](https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=hibernate&logoColor=white) ![RESTful API](https://img.shields.io/badge/RESTful_API-6DB33F?style=for-the-badge&logo=rest&logoColor=white) |
| **Frontend** | ![HTML](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white) ![CSS](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white) ![JavaScript](https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E) ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white) |
| **Database** | ![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white) ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white) |
| **DevOps**   | ![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) ![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white) ![AWS EC2](https://img.shields.io/badge/AWS_EC2-FF9900?style=for-the-badge&logo=amazon-aws&logoColor=white) ![AWS S3](https://img.shields.io/badge/AWS_S3-569A31?style=for-the-badge&logo=amazon-s3&logoColor=white) |
| **Tool**     | ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white) ![Eclipse](https://img.shields.io/badge/Eclipse-2C2255?style=for-the-badge&logo=eclipse&logoColor=white) ![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white) ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white) |
| **API 문서** | ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black) |


## ✨ 주요 기능

- 🛒 **스토어**
  - 상품 목록, 상세 페이지
  - 상품 찜 , 리뷰 및 별점

- 🐾 **커뮤니티**  
  - 게시글 작성, 수정, 삭제 및 검색
  - 댓글 및 좋아요 기능
  - 인기 게시글 자동 정렬

- 📬 **회원 시스템**
  - 회원 가입 및 로그인
  - 소셜 로그인 (Google, Kakao)
  - 프로필 수정 및 활동기록

- ➕  **장바구니**
  - 상품 추가/삭제 및 수량 업데이트
  - 장바구니 세션 관리
  
- 📦  **주문**
  - 주문 및 결제 처리
  - 배송 관련 , 회원 쿠폰 관리


## 🔧 핵심 기술 및 구현 내용

- 📁 **도메인 주도 설계(DDD) 기반의 도메인 설계**
- 🚥 **Spring Security 통한 인증 및 권한별 인가 시스템**
- 🔐 **Oauth2 를 이용한 소셜 로그인**
- 📨 **SMTP 기반의 이메일 전송을 통한 비밀번호 찾기**
- 🛒 **Spring Data JPA를 활용한 페이징 처리**
- 🔄 **Swagger API 를 이용한 API 관리 및 명세**
- 📦 **Docker로 환경 분리 및 배포 자동화**


## 주요 페이지

- 🏠 메인 페이지 (Home)

- 🛍️ 상품 상세 페이지 (Product Detail)

- 🛒 장바구니 (Cart)

- 📦 주문/결제 페이지 (Order / Checkout)
  
- 👤 마이페이지 (My Page)
  
- 🛠️ 관리자 페이지 (Admin)

- 🏪 고객센터 (FAQ)


## 📈 기술적 성과

- API 공통 응답 객체 사용을 통한 응답 구조의 일관성 확립 ➡️ 프론트 엔드 개발 시 응답 구조에 대한 편의성 
- 하나의 DTO 클래스 내에서 용도별 static 중첩 클래스 사용 ➡️ 가독성 향상 및 유지보수
- 요청이 잦은 API 의 경우 인덱싱 처리 ➡️ 데이터 조회 속도 개선
- 도메인별 MVC 디자인 패턴을 활용한 유지보수 효율성 향상 
- REST API를 활용한 실시간 정보 전달
- CSRF, 세션 인증, 경로 접근 제한 등 보안 설정 강화
- Jenkins와 GitHub, 도커, 우분투(리눅스)를 활용한 CI/CD 기능 구현
- AWS를 활용하여 실시간 배포 및 안전성을 갖춘 서버 제공


## 🔮 향후 개선 계획

- ✅ 실시간 채팅 고객센터 연동
- ✅ Redis 세션 저장소 전환
- ✅ React 기반 프론트 리팩토링
- ✅ 관리자 페이지 분리 및 통계 기능 추가
- ✅ 모바일 등 다양한 환경에서 동작 가능한 반응형 UI 구현
- ✅ 구글, 네이버 등의 결제 api를 활용항 가상 결제 기능 구현
