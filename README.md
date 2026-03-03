# 🍰 CAKEY - 케이크 가게 탐색 서버
**CAKEY**는 서울 지하철역 기반으로 케이크 가게를 탐색하고 디자인을 찜할 수 있는 서비스의 백엔드 서버입니다.
---
## 📋 목차
- [기술 스택](#-기술-스택)
- [주요 기능](#-주요-기능)
- [프로젝트 구조](#-프로젝트-구조)
- [CI/CD](#-cicd)
---
## 🛠 기술 스택
### Backend
| 구분 | 기술 |
|------|------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.2.7 |
| **ORM** | Spring Data JPA, QueryDSL 5.0.0 |
| **Security** | Spring Security, JWT (JJWT 0.11.5) |
| **OAuth** | Spring OAuth2 Client (Kakao) |
| **Database** | MySQL 8.x, PostgreSQL |
| **Cache** | Caffeine Cache |
| **API Client** | Spring Cloud OpenFeign 4.1.0 |
### Infrastructure
| 구분 | 기술 |
|------|------|
| **Cloud** | AWS EC2 |
| **Container** | Docker, Amazon Corretto 17 |
| **CI/CD** | GitHub Actions |
| **Notification** | Discord Webhook |
---




### 멀티 모듈 구조
```
cakey-server
├── cakey-api         # API 엔드포인트, 컨트롤러, 서비스 계층 (실행 가능 JAR)
├── cakey-domain      # 도메인 엔티티, 레포지토리, QueryDSL
├── cakey-auth        # JWT 인증, 카카오 OAuth2 연동
├── cakey-common      # 공통 상수, 유틸리티, 예외 처리
└── cakey-external    # 외부 API 연동 (Discord 알림)
```
---
## ✨ 주요 기능
### � 지하철역 기반 검색
- **지역별 가게 탐색**: 서울 주요 20+ 지하철역 주변 케이크 가게 검색
- **좌표 기반 지도 조회**: 가게 위치 좌표 제공
- **필터링**: 역별 가게/디자인 필터링
### 🎂 케이크 디자인 탐색
- **카테고리 필터링**: 생일(BIRTH), 응원(CHEER), 기념일(ANNIV), 시즌(SEASON)
- **테마 필터링**: 귀여움, 미니멀, 캐릭터, 럭셔리, 유머, 판타지 등
- **정렬 옵션**: 인기순/최신순 조회
### ❤️ 찜하기 시스템
- **케이크 찜하기**: 마음에 드는 디자인 저장
- **가게 찜하기**: 좋아하는 가게 저장
- **찜 목록 조회**: 인기순/최신순 정렬
### 🔐 사용자 인증
- **카카오 소셜 로그인**: OAuth2 기반 간편 로그인
- **JWT 인증**: Access Token + Refresh Token 캐싱
### 🏪 가게 정보
- **상세 정보 조회**: 주소, 전화번호, 영업시간
- **사이즈/맛 정보**: 가게별 케이크 사이즈 및 맛 정보
- **카카오 오픈채팅**: 가게 문의 링크 제공
---
## � 프로젝트 구조
```
cakey-api/src/main/java/com/cakey/
├── CakeyServerApplication.java      # 메인 애플리케이션
├── cake/                            # 케이크 도메인
│   ├── controller/                  # API 컨트롤러
│   ├── dto/                         # 요청/응답 DTO
│   ├── exception/                   # 도메인 예외
│   └── service/                     # 비즈니스 로직
├── cakelikes/                       # 케이크 찜하기
├── store/                           # 가게 도메인
├── storelikes/                      # 가게 찜하기
├── user/                            # 사용자 도메인
├── common/
│   ├── exception/handler/           # 전역 예외 처리
│   ├── filter/                      # 인증 필터
│   ├── resolver/                    # Argument Resolver
│   └── response/                    # 통합 응답 형식
└── config/                          # 설정 클래스
cakey-domain/src/main/java/com/cakey/
├── cake/
│   ├── domain/                      # 엔티티 (Cake, DayCategory)
│   ├── dto/                         # 쿼리 DTO
│   ├── facade/                      # 도메인 로직 조합
│   └── repository/                  # JPA + QueryDSL 레포지토리
├── cakelike/                        # 케이크 찜하기 도메인
├── store/                           # 가게 도메인 (Store, Station)
├── storelike/                       # 가게 찜하기 도메인
├── user/                            # 사용자 도메인
├── caketheme/                       # 케이크 테마 (ThemeName)
├── size/                            # 케이크 사이즈
├── operationtime/                   # 영업시간
└── common/                          # BaseTimeEntity 등
cakey-auth/src/main/java/com/cakey/
├── client/kakao/api/                # 카카오 API 클라이언트
├── jwt/auth/                        # JWT 생성/검증
└── exception/                       # 인증 예외
cakey-external/src/main/java/com/cakey/
└── feign/discord/                   # Discord 알림 연동
```
---
## 🔄 CI/CD
GitHub Actions를 통한 자동 배포 파이프라인:
```
dev 브랜치 Push
       ↓
  GitHub Actions
       ↓
  ┌────────────────────────┐
  │ 1. Checkout            │
  │ 2. Set up JDK 17       │
  │ 3. application.yml 생성 │
  │ 4. Build (Gradle)      │
  │ 5. Docker Build & Push │
  └────────────────────────┘
       ↓
  Docker Hub
       ↓
  EC2 SSH Deploy
```
### 워크플로우 파일
- `CI.yml`: PR → dev 브랜치 빌드 검증
- `DOCKER-CD.yml`: dev 브랜치 Push 시 Docker 빌드 및 EC2 배포
---

