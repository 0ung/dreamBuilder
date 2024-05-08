# dreamBuilder
# 기능 정의서

## 1. 회원가입
- [x]  **필수 정보**: 이메일, 비밀번호, 이름 
- [ ] **입력값 유효성 검사**: 입력된 정보의 유효성을 확인
- [ ] **기본 권한**: 일반 유저. 최초 관리자 권한은 데이터베이스에서 직접 부여

## 2. 로그인
- [ ] **필요 정보**: 이메일, 비밀번호
- [ ] **보안**: 스프링 시큐리티를 이용한 보안 강화
- [ ] **인증 방법**: JWT 토큰 기반 인증 및 OAuth2 카카오 인증

## 3. 게시물 관리 (CRUD)
- [ ] **게시물 생성**
  - 입력 정보: 제목, 다중 해시태그, 본문, 대표이미지
  - 특이 사항: 본문에 주민번호가 포함되면 자동으로 블러 처리
- [ ] **게시물 조회**
  - 검색 기능: 해시태그, 제목, 작성자 이름, 본문 내용
  - 페이지네이션: 페이지당 10개의 게시물
  - 게시물 조회 기능: 게시물의 조회 수를 카운트
  - 정렬 옵션: 제목, 이름, 생성 일시 (기본은 생성 일시 내림차순)
- [ ] **게시물 업데이트**
  - 제한: 작성자 본인만 수정 가능
- [ ] **게시물 삭제**
  - 제한: 작성자 및 관리자만 삭제 가능
- [ ] **게시물 좋아요**
  - 기능: 회원은 하나의 게시물에 대해 좋아요를 on/off 할 수 있음
- [ ] **파일 첨부**
  - 허용 파일: 이미지, 문서, 동영상
  - 최대 파일 크기: 100MB
  - 확장자 유효성 검사: 지정된 확장자만 허용
  - 게시물 본문에 이미지, 동영상 표시 및 문서 다운로드 기능

## 4. 댓글 시스템
- [ ] **댓글 생성**
  - 제한: 로그인한 사용자
- [ ] **댓글 수정**
  - 제한: 본인만 가능
- [ ] **댓글 조회**
  - 표시: 댓글은 최초 5개 표시, 날짜순으로 정렬
  - 더보기 기능을 통해 추가 댓글 조회
- [ ] **댓글 삭제**
  - 제한: 본인 및 관리자만 가능
- [ ] **대댓글 시스템**
  - 생성, 조회, 삭제: 댓글에 대한 답글 기능

## 5. 관리자 기능
- [ ] **관리자 권한 생성**
  - 방법: 데이터베이스에서 최초 생성
- [ ] **게시물 관리**
  - 삭제된 게시물: 관리자가 삭제 시 비노출 처리, 복원 가능
  - 기능: 삭제한 게시물 리스트를 엑셀로 다운로드
- [ ] **방문자 수 집계**
  - 데이터: 접속자 IP, 접속 일자
  - 집계 주기: 일별, 주별, 월별
  - 차트 표시: Chart.js를 이용한 시각화
- [ ] **기본 조회 기능**
  - 게시물, 댓글, 회원 정보 조회


------

## 일정 관리
- 요구사항 정의서
- 기능정의서
- DB 설계서
- 화면설계서
- 단위 테스트 계획서
- 단위 테스트 결과서
- 회의록
