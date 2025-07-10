# 지역화폐 주유소 찾기

## Deployment

[상생카드주유소.kro.kr](http://xn--hy1bo9toc36al0tu1c76n.kro.kr/)

## 목적

- 기존 지역화폐 홈페이지에서 제공되는 정보는 주유소의 위치를 바로 파악하기 어려움
- 상생카드의 경우 어플이 존재하지 않아 실제 가맹점 위치를 찾는과정에서 번거로움 발생
- 되도록 가벼우면서 간단하게 구현하며, 모바일에서도 확인 가능하게 하는것이 목표

## 주요 기능

- 광주 광역시의 지역화폐 “상생카드”의 사용 가능한 주유소의 위치 제공
- 상생 카드 화폐의 결제 금액을 제공
- 유종별 최저가 보유 주유소 정보 제공

## Skill.

- Front-End : javaScript, HTML/CSS, Thymeleaf
    - javaScript : 유가정보 필터링, 지도 위치 기능 구현
    - HTML/CSS : 메인화면, 유가 정보 화면 구현
    - Thyemleaf : 서버에서 데이터를 받아오기 위해 선택
    
- Back-End : JAVA ,Spring-boot, REST API, JSON 데이터 처리
    - Spring-boot : 외부 API연동이 핵심이기에 API연동이 쉬운 스프링부트 채택(추후 바우처 카드 종류 증가 및 기능 추가에 대해서도 유리)
    - 유가 정보를 제공받아 필요한 정보만 DTO로 새롭게 정의하여 사용
- 외부 API : opinet 유가 정보 api, 네이버 지도 api
    - restTemplate를 활용하여 외부 api로부터 json 데이터 호출
- etc : AWS(aws elastic beanstalk), git


### 구성
메인화면
<img width="1909" height="919" alt="화면 캡처 2025-02-06 030437" src="https://github.com/user-attachments/assets/3508cf76-9046-4f07-858e-89147c91294f" />

상세페이지
<img width="1890" height="921" alt="화면 캡처 2025-02-06 032022" src="https://github.com/user-attachments/assets/347cf15e-4431-4549-a7be-852b8dceaa13" />

좌측 모달창 활성화
<img width="1903" height="916" alt="화면 캡처 2025-02-06 032047" src="https://github.com/user-attachments/assets/cac20c03-db4c-4edf-afa8-d6d69c8137bc" />

유종별 최저가 찾기
<img width="1898" height="920" alt="화면 캡처 2025-02-06 032104" src="https://github.com/user-attachments/assets/9bc02e8c-7295-4fcd-93a5-606fd0a0bf48" />


## 반응형 디자인
<div>
        <img width="400" height="680" alt="화면 캡처 2025-02-07 172604" src="https://github.com/user-attachments/assets/6cdce0c4-22a7-499b-9058-164edb38f3dc" />
        <img width="400" height="677" alt="화면 캡처 2025-02-07 172617" src="https://github.com/user-attachments/assets/f4f54e14-4492-4d3e-a046-2b8aedd69cdc" />        
</div>

