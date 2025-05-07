# AI 프로젝트: [Weebee]

> 금융 이해도 분석 / 랭크 배정 / 예측 모델링에 대한 설명을 토대로 맞춤 학습과 금융 서비스 제공


---

## 👥 팀원 소개

| 이름 | 역할 | GitHub |
|------|------|--------|
| 김정수 | 백엔드 개발 | [@kimjeongsoo20190147](https://github.com/kimjeongsoo20190147) |
| 이승연 | 백엔드 개발 | [@yeonseungg](https://github.com/yeonseungg) |
| 이재영 | 백엔드 개발 | [@이재영](https://github.com/lejy) |

---

## 🎯 프로젝트 목표

- ✅ 사용자 금융 이해도 별 맞춤 학습 제공 & 금융 서비스 추천천

---

## 🚀 실행 방법 (로컬 개발)

```bash
# 1. .env 생성
db 관련 정보 입력

# 2. application.properties에 
spring.config.import=optional:dotenv:./.env 입력
```

→ (http://localhost:8080/swagger-ui/index.html#/)에서 Swagger UI 확인 가능

---

## 📊 사용 기술

- **언어**: Java 21
- **웹 프레임워크**: JAVA SPRINGBOOT
- **배포**: Docker, GitHub Actions (선택)

---

## 🧪 테스트
swagger와 postman을 통해 테스트 가능

---


## 커밋 규칙

feat : 새로운 기능 추가

fix : 버그 수정

docs : 문서 수정

style : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우

refactor : 코드 리펙토링

test : 테스트 코드, 리펙토링 테스트 코드 추가

chore : 빌드 업무 수정, 패키지 매니저 수정
