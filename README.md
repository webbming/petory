# 펫토리 (Petory) 🐾



## 📌 프로젝트 소개

- 반려동물 커뮤니티와 쇼핑몰이 통합된 웹 서비스입니다.  
- 유저는 반려동물에 대한 정보를 공유하거나 필요한 물품을 구매할 수 있습니다.

## 🛠️ 기술 스택

| 구분        | 스택                                                         |
|-------------|--------------------------------------------------------------|
| **Backend** | Java 17, Spring Boot 3.x, Spring Security, JPA, MyBatis     |
| **Frontend**| HTML, CSS, JavaScript (Thymeleaf or JSP)                    |
| **Database**| MariaDB, MySQL                                               |
| **DevOps**  | Docker, GitHub Actions, AWS EC2/S3 (선택)                    |
| **Tool**    | IntelliJ IDEA, Eclipse, Git, Postman                         |
| **API 문서**| Springdoc OpenAPI (Swagger UI)                               |


## 🏗️ 서버 아키텍처 (Mermaid)
![서버 구조도](https://github.com/soncastle/shoppingmall/blob/master/src/main/resources/static/images/ui/서버구조도.PNG?raw=true)

## 👥 팀원 소개

| 이름     | 역할        | GitHub                                   |
|----------|-------------|-------------------------------------------|
| 김진후 | 팀장 / 백엔드 | [@soncastle](https://github.com/soncastle) |
| 홍길동 | 프론트엔드   | [@honggildong](https://github.com/honggildong) |
| 이몽룡 | 디자이너     | -                                         |

---

## ✨ 주요 기능

- 🛒 **반려동물 용품 스토어**  
  - 상품 목록, 상세 페이지
  - 장바구니, 주문 기능
  - 결제 완료 후 주문 내역 확인

- 🐾 **커뮤니티**  
  - 게시글 작성, 수정, 삭제
  - 댓글 및 좋아요 기능
  - 인기 게시글 자동 정렬

- 📬 **회원 시스템**
  - 이메일 인증 기반 회원가입
  - 소셜 로그인 (Google, Kakao)
  - 마이페이지에서 정보 수정

---

## 🔧 핵심 기술 및 구현 내용

- 🔐 **Spring Security 기반 로그인 시스템 구현**
- 📨 **이메일 인증 및 임시 비밀번호 발급 기능 구현**
- 📁 **도메인별 패키지 구조 및 예외 처리 통합**
- 🔄 **RESTful API 설계 및 Swagger 연동**
- 📦 **Docker로 환경 분리 및 배포 자동화**

---

## 📈 기술적 성과

- 100% 자체 개발, 외부 템플릿 미사용
- 커뮤니티와 쇼핑몰 기능 완전 연동
- 실시간 알림 기능 및 비동기 처리 일부 구현
- CSRF, 세션 인증, 경로 접근 제한 등 보안 설정 강화

---

## 🔮 향후 개선 계획

- ✅ 상품 리뷰 기능 추가
- ✅ 실시간 채팅 고객센터 연동
- ✅ Redis 세션 저장소 전환
- ✅ React 기반 프론트 리팩토링
- ✅ 관리자 페이지 분리 및 통계 기능 추가

