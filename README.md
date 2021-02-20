## **소셜 미디어 환경에서 기계학습을 적용한 개인 맞춤형 화장품 추천 시스템**

![https://user-images.githubusercontent.com/38436013/108601469-16f5ad00-73e0-11eb-9d1d-de6895195c99.png](https://user-images.githubusercontent.com/38436013/108601469-16f5ad00-73e0-11eb-9d1d-de6895195c99.png)

                                                                         <메인>

### **프로젝트 소개**

1. 사용자의 선호도, 개인의 성향을 고려하여 적합한 화장품을 제공하고자 함  👉🏾 사용자의 성별, 나이, 직업, 구매 정보 등의 개인 정보를 통해 비슷한 성향의 사용자를 추출하고, 유사 사용자의 화장품을 추천
2. 소셜 미디어로부터 화장품과 관련된 대량의 정보를 이용하여 개인 맞춤형 화장품 추천  👉🏾 트위터에서 실시간 정보를 수집하여 트렌드 분석하여 인기 있는 제춤 추천

### **프로젝트 수행**

github_repo

2019.03 ~ 2019.11

4학년 졸업 작품 및 졸업 논문으로 발표

3명이 한 팀원으로, 신혜란, 임유정, 홍유진 3명이 기여

[🥰](https://www.notion.so/8ba3c7f277564255bb1406f3f42b630a)

### 관련 이론

1. 개인 맞춤형 화장품 추천
    - 군집화
        - 유사하거나 관련 있는 것끼리 묶는 과정
        - 텐서플로우의 군집화 대표 알고리즘인 'K-MEANS' 이용하여 추천 시스템에 구현
        - ‘K-means’의 K는 군집화 할 그룹의 개수이며, K개의 군집을 선정해 데이터와 군집점 사이의 유사도를 계산하여 군집화를 이루는 방법

    - 협업 필터링
        - 사용자가 선호할 만한 것을 추천하는 알고리즘
        - 사용자 기반 협업필터링(유사 사용자 찾아내 이들이 공통으로 좋아하는 항목 추천)을 적용
2. 화장품 트렌드 분석
- 트위터 같은 SNS 기반으로 데이터 크롤링하여, 수집한 데이터를 통해 핫 토픽을 생성
    - TF-IDF
        - TF는 단어 빈도로, 특정 단어가 얼마나 자주 등장하는지를 나타내는 값, 이 값이 클수록 문서에서 중요하다고 판단
        - IDF는 DF의 역수로, DF는 문서 빈도를 말하며 특정 단어가 포함된 문서 수를 의미
        - TF * IDF 클수록 해당 문서에서 자주 등장하는 단어 ⇒ 핫 토픽 으로 규정

        ![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/72fe055d-586e-46b9-a3f1-c4d32f8929df/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/72fe055d-586e-46b9-a3f1-c4d32f8929df/Untitled.png)

### 시스템 구성도

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/dc1c4a3d-0b53-4ca6-9018-e0937aeb0f76/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/dc1c4a3d-0b53-4ca6-9018-e0937aeb0f76/Untitled.png)

- 트렌드 분석과 개인 맞춤형 추천
    1. **트렌드 분석**
    - 트렌드 분석 과정
        - 트위터에서 화장품 데이터 실시간 크롤링 → MariaDB에 저장 → 실시간 트렌드 분석 및 기계학습 입력 데이터로 활용
    - 트렌드 분석 동작
        - 수집된 실시간 데이터는 Hannanum 오픈 소스 이용하여 형태소 분리 되어 1시간 단위로 만들어지는 파일에 저장됨
        - 저장된 키워드 중 화장품 이름, 브랜드와 일치하는 키워드 추출
        - 추출된 키워드는 시간을 고려한 TF-IDF 알고리즘을 사용하여 핫 토픽을 구하는 데 사용
        - 시간을 고려한 TF-IDF는 갑자기 많이 언급된 핫 토픽을 구하기 위함
        - 단어시간대별로 단어마다 TF-IDF를 구하고, 현재시간대의 TF-IDF와 이전시간대의 TF-IDF값의 비율을 구함
        - rate 클수록 이전 시간대보다 현재 해당 단어 많이 언급된 것임

        ![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/fe30f6c6-2cf6-4133-9d9c-a7052d9e7d2b/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/fe30f6c6-2cf6-4133-9d9c-a7052d9e7d2b/Untitled.png)

    2. **개인 맞춤형 추천**

    - 머신러닝(군집화 + 협업필터링) 추천 알고리즘
        - 군집화된 유사 사용자에 대해 협업 필터링 적용
        - 협업 필터링의 적용은 먼저, 군집에 화장품을 추가하여, 추가된 화장품 에 대한 수를 센다. 질의한 사용자가 구매한 화장품 목록을 빼고, 추천 목록에서 사용자가 구매한 화장품을 뺀 다른 상품들에서 구매 수(중복된 화장품 수)가 많은 순서대로 추천
    - 속성 기반 알고리즘
        - 사용자 데이터와 화장품 데이터의 같은 속성인 피부타입, 나이, 성별을 비교하여 맞는 컬럼의 수를 기반으로 질의를 한 사용자에 대해 화장품 이름을 추천

[Untitled](https://www.notion.so/3f33e36b1d334a90b0d443acf4048a00)

### 주요 기능

- 트렌드 분석 화면

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/31cdee10-dde6-458e-b36f-baa1b4df88e5/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/31cdee10-dde6-458e-b36f-baa1b4df88e5/Untitled.png)

⇒ 1시간마다 받은 트윗 키워드를 분석, 본 화면의 내비게이션 바 하단에는 1시간마다 rank값이 가장 큰 10개의 키워드를 구해 해당 키워드에 대한 TF-IDF추이를 차트로 보여줌

- TF-IDF 추이 버튼

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/631ba834-65dc-4c66-8611-1722aaa6b7fb/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/631ba834-65dc-4c66-8611-1722aaa6b7fb/Untitled.png)

⇒ 차트에서 보여준 각 키워드들의 TF-IDF와 rank 값이 수치로 볼 수 있음, 이 값들은 서버 컴퓨터 DB의 ‘topk_table’에서 한 시간마다 자동으로 갱신됨

- 화장품 추천 결과 화면

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8fe292d2-6a16-40e5-b35c-8421bdd18ef5/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8fe292d2-6a16-40e5-b35c-8421bdd18ef5/Untitled.png)

⇒ 추천 알고리즘의 결과로 나온 개인 맞춤형 화장품 상위 랭크 3개를 추천, 각 화장품의 사진, 브랜드명, 제품명, 속성 3가지를 보여줌

- 추천리스트 보기 버튼

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ff4c430d-3bc5-44c9-bd6c-8f42091c8be1/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ff4c430d-3bc5-44c9-bd6c-8f42091c8be1/Untitled.png)

### 성능 분석 및 개선할 점

1. 속성 추천 알고리즘과 기계학습 알고리즘의 추천 아이템 수의 변화에 따른 중복 추천 아이템 수의 유사도

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3cb257d7-0500-4aaf-aeab-23359806638d/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3cb257d7-0500-4aaf-aeab-23359806638d/Untitled.png)

⇒ 총 추천 화장품 수가 증가할수록 유사율도 비례할 것이라는 예상과 다른 결과가 나옴 

2. 기계학습 알고리즘의 학습 수에 따른 속성 알고리즘의 중복 추천 변화

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/1d61219d-dccd-498b-baeb-ddba4b2468fa/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/1d61219d-dccd-498b-baeb-ddba4b2468fa/Untitled.png)

⇒ 학습 반복횟수가 증가할수록 중복율도 비례할 것이라는 예상과 접하지 않을 결과가 나옴

문제 원인

👉성능 분석 결과에 따라 기계학습 추천 알고리즘에서 사용자 구매내역 테이블에서 50%의 확률로 무작위 화장품을 구매하고, 혹은 자신의 속성을 참고하여 화장품을 구매하도록 구현했기 때문에 예상과 다른 결과가 나온 것을 추정.  앞으로는 사용자 DATA SET 생성 부분을 개선할 예정.
