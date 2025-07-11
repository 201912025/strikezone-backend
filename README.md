## 구현된 기능 목록

### 1. 댓글(Comment)
- 댓글 작성 (`POST /api/comments`)
- 댓글 수정 (`PATCH /api/comments/{commentId}`)
- 댓글 삭제 (`DELETE /api/comments/{commentId}?userId=…`)
- 게시글별 댓글 목록 조회 (페이징) (`GET /api/comments?postId=…&page=…`)

### 2. 선수(Player)
- 선수 등록 (`POST /api/players`)
- 선수 정보 수정 (`PATCH /api/players/{playerId}`)
- 선수 단건 조회 (`GET /api/players/{playerId}`)

### 3. 투표(Poll) & 투표 옵션(PollOption)
- 투표 생성 (`POST /api/polls`)
- 투표 수정 (`PUT /api/polls/{pollId}`)
- 투표 삭제 (`DELETE /api/polls/{pollId}`)
- 전체 투표 목록 조회 (`GET /api/polls`)
- 투표 옵션 생성 (`POST /api/polloptions`)
- 투표 옵션 삭제 (`DELETE /api/polloptions/{optionId}`)
- 특정 투표의 옵션 목록 조회 (`GET /api/polloptions?pollId=…`)

### 4. 게시글(Post)
- 게시글 작성 (`POST /api/posts`)
- 전체 게시글 조회 (`GET /api/posts`)
- 페이징된 게시글 조회 (`GET /api/posts/paged?page=…`)
- 키워드 검색 (`GET /api/posts/search?keyword=…&searchType=…`)
- 팀별 검색 (`GET /api/posts/team?teamName=…`)
- 단건 조회 (`GET /api/posts/{postId}`)
- 게시글 수정 (`PATCH /api/posts/{postId}`)
- 게시글 삭제 (`DELETE /api/posts/{postId}`)
- 인기 게시글 조회 (`GET /api/posts/popular`)
- 좋아요 증가 (`PATCH /api/posts/{postId}/like`)

### 5. 팀(Team)
- 팀 등록 (`POST /api/teams`)
- 전체 팀 조회 (`GET /api/teams`)
- 단건 팀(및 소속 선수) 조회 (`GET /api/teams/{teamId}`)
- 팀 삭제 (`DELETE /api/teams/{teamId}`)

### 6. 사용자(User)
- 회원 가입 (`POST /api/users/join`)
- 내 정보 조회 (`GET /api/users`)
- 정보 수정 (`POST /api/users/update`)
- 회원 탈퇴 (`POST /api/users/delete`)


## 아키텍처

![아키텍처 다이어그램](https://github.com/201912025/strikezone-backend/blob/main/docs/images/aws-architecture.png)

![Redis 아키텍처 다이어그램](https://github.com/201912025/strikezone-backend/blob/main/docs/images/redis-architecture.png)

## 기술 스택

- **언어 & 프레임워크**
    - Java 17
    - Spring Boot

- **보안**
    - Spring Security & JWT

- **데이터베이스**
    - Spring Data JPA
    - MY SQL (RDS)
    - QueryDsl

- **캐싱 & 성능**
    - Redis (ElasticCache)
 
- **컨테이너 & 인프라**
  - Docker  
  - AWS EC2  
  - AWS Application Load Balancer (ALB)
