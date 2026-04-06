# 주요 기능 설명

#### 검색 화면:
- 이미지와 동영상을 검색하고 리스트 형태로 표시
- Paging과 Coroutine/Flow를 사용해 대용량 데이터를 효율적으로 로드
- Compose를 활용해 반응형 UI 구현
#### 보관 화면:
- 사용자가 선택한 이미지를 저장하고 관리
- SharedPreference를 활용해 로컬에 간단히 상태 저장
- MVVM 구조를 적용해 UI와 데이터 로직 분리

#### 기술 스택 적용
- MVVM + Hilt: 의존성 관리와 구조화된 데이터 흐름
- Coroutine/Flow: 비동기 검색 데이터 처리 및 UI 업데이트 최적화
- Compose: 직관적이고 반응형 UI 구현
- Paging: 검색 결과 리스트 효율적 로딩
- Coil: 이미지 로딩 최적화
- SharedPreference: 간단한 로컬 데이터 보관
