<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-%3E%3D%205.5.0-blue">
  <img alt="node" src="https://img.shields.io/badge/node-%3E%3D%209.3.0-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-admin">
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```
<br>

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-admin/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-admin/blob/master/LICENSE.md) licensed.

## step1 - 지하철 노선 관리

인수테스트 작성 목록
- [x] 지하철 노선을 생성한다.
- [x] 기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.
- [x] 지하철 노선 목록을 조회한다.
- [x] 지하철 노선을 조회한다.
- [x] 지하철 노선을 수정한다.
- [x] 지하철 노선을 제거한다.

step1 리뷰 코멘트 작성<br>
https://github.com/next-step/atdd-subway-admin/pull/478

## step2 - 인수 테스트 리팩터링
- [x] 노선 생성 시 종점역(상행, 하행) 정보를 요청 파라미터에 함께 추가하기
  - 두 종점역은 구간의 형태로 관리되어야 함
- [x] 노선 조회 시 응답 결과에 역 목록 추가하기
  - 상행역 부터 하행역 순으로 정렬되어야 함

## step3 - 구간 추가 기능
- [x] (step2 fix) 라인 조회 시 상행역부터 하행역 순으로 지하철역이 나타남. 상행 종점과 하행 종점만 나오도록 구현했음.
  - [x] 상-하행 순으로 정렬 Line 단위 테스트
- [x] 역 사이에 새로운 역을 등록할 경우 새로운 길이를 뺀 나머지를 새롭게 추가된 역과의 길이로 설정
- [x] 새로운 역을 상행 종점으로 등록할 경우
- [x] 새로운 역을 하행 종점으로 등록할 경우
- [x] 구간 등록 시 예외 케이스를 고려하기
  - [x] 역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음
  - [x] 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음
  - [x] 상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음

## step4 - 구간 제거 기능
- [x] 노선의 구간을 제거하는 기능
  - [x] 종점이 제거될 경우 다음으로 오던 역이 종점이 됨
  - [x] 중간역이 제거될 경우 재배치를 함(노선에 A - B - C 역이 연결되어 있을 때 B역을 제거할 경우 A - C로 재배치 됨, 거리는 두 구간의 거리의 합으로 정함)
- [x] 구간 삭제 시 예외 케이스
  - [x] 구간이 하나인 노선에서 마지막 구간을 제거할 때
  - [x] 노선에 등록되어있지 않은 역을 제거하려 할때
- 구현 방식
  - 인수 테스트 작성(1st)
  - 도메인 단위 테스트 작성(2st)
