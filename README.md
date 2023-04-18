# 구인구직 프로젝트
![image](https://user-images.githubusercontent.com/80329856/232012419-7744ae5a-1870-4be2-892c-e59728d92bfb.png)

# 기술스택
- JDK 11
- Springboot 2.7.8
- MyBatis
- h2 디비
- EC2 서버 배포

## 1단계 DTO 생성 (완료)
- 로그인 회원가입 DTO 생성
- 고객센터 DTO 생성

### 개인
- 회원정보 DTO 생성
- 이력서 등록 DTO 생성
- 이력서 리스트 DTO 생성
- 이력서 디테일 DTO 생성
- 이력서 수정 DTO 생성
- 이력서 삭제 DTO 생성
- 공고 리스트 DTO 생성
- 공고 디테일 DTO 생성
- 지원 이력 및 합격 DTO 생성
- 스크랩 하기 DTO 생성
- 스크랩 삭제 DTO 생성
- 스크랩 보기 DTO 생성
- 추천 공고 리스트 DTO 생성

### 기업
- 회원정보 DTO 생성
- 공고 등록 DTO 생성
- 공고 디테일 DTO 생성
- 공고 리스트 DTO 생성
- 공고 수정 DTO 생성
- 공고 삭제 DTO 생성
- 지원 이력 및 합격 DTO 생성
- 스크랩 하기 DTO 생성
- 스크랩 삭제 DTO 생성
- 스크랩 보기 DTO 생성
- 추천 이력서 DTO 생성

## 2단계 Junit 테스트 및 추가 기능 구현 (완료)
- JUnit test
- JWT 구현
- AOP 구현

## 3단계 모니터링 및 배포 
- Sentry.io 모니터링 구성하기(에러로그 log4j) 
- EC2 배포 (완료)

# 핵심 로직
## DTO 변환
### 기존 세팅
- Select 는 controller에서 진행
- 모델로 값을 보내줌
### 변환하는 이유
- RestController로 변경
- 데이터만 보내고, ajax를 이용하여 화면을 구성하기 위해서
![image](https://user-images.githubusercontent.com/80329856/232013999-1d910cc3-d114-4a94-b8e5-89b913f71d76.png)
Controller는 서비스만 요청하며, 서비스에서는 DTO로 정리된 값을 보내주게된다. 

## JWT 토큰
![image](https://user-images.githubusercontent.com/80329856/232014128-0bf5464f-2880-4123-bb13-57f36b22ed64.png)
![image](https://user-images.githubusercontent.com/80329856/232014235-30b115cf-7918-401b-8f09-a2f6b053b66f.png)
- 세션에 저장된 것은 임시로 사용하는 정보
- 인터셉터를 이용해서 컨트롤러가 끝날 때, 세션을 초기화 해준다.
- 
## AOP 유효성 검사
![image](https://user-images.githubusercontent.com/80329856/232014619-bb4f02c3-9d48-432e-a02d-54f7dfef94e2.png)
- 데이터가 들어오는 PostMapping과 PutMapping의 데이터를 검사하도록 함. 
- 검사할 DTO에 @Valid 추가
- BindingResult 매개변수 추가 
- ![image](https://user-images.githubusercontent.com/80329856/232014672-313c97f1-bbb2-435f-a520-e5d63a9b169a.png)

## Junit 테스트
![image](https://user-images.githubusercontent.com/80329856/232014737-9145dee4-8d0b-4f7e-8875-6c7a16025777.png)

## API문서
https://suave-roadway-a9f.notion.site/1-API-1de99090da964d95aed22a3ef77086f3
![image](https://user-images.githubusercontent.com/80329856/232015144-68f5fc5c-4542-42f5-9e65-8e5c4b950636.png)
![image](https://user-images.githubusercontent.com/80329856/232015171-b724b042-4a41-4f94-b6f3-31c79783958c.png)
![image](https://user-images.githubusercontent.com/80329856/232015214-f718cb82-e094-40ae-9ba6-d6a637345c49.png)


## EC2 서버 배포
- 환경변수를 이용한 비밀키 적용
- Post man을 이용해서 실행 
![image](https://user-images.githubusercontent.com/80329856/232015258-fccfa4e3-3097-43bd-ada8-d9ca98a4f3b1.png)


# 협업 전략
### github
- Organaization을 이용해 하나의 팀을 먼저 만들고 레파지토리를 생성 및 세팅
  - 팀장의 레파지토리에 초대하는 것 보다 하나의 팀을 만들어서 같은 레파지토리를 보는 것이 좋겠다 판단
  - 또한 이 팀원으로 두번째 프로젝트를 진행할 때 다시 초대할 일 없이 계속해서 이용 가능함
- master-dev-topic으로 각자의 토픽에 해야 할 일을 설정하고 dev에 merge
### 일정관리
- 처음에는 일정관리 시트를 만들어서 미리 시간이 안되는 날을 체크하여, 가능한 날 주기적으로 오후 7시~9시에 만나서 짧은 회의와 정리를 진행
