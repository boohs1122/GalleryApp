# 📷 GalleryApp — 이미지·동영상 검색 갤러리

> Kakao 검색 API를 활용해 이미지와 동영상을 한 번에 검색하고, 마음에 드는 항목을 보관함에 저장할 수 있는 안드로이드 앱입니다.
> **Jetpack Compose · MVVM · Hilt · Paging 3 · Coroutine/Flow** 기반으로, 최신 안드로이드 개발 스택을 적용해 구현했습니다.

![Kotlin](https://img.shields.io/badge/Kotlin-2.2.0-7F52FF?logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-BOM%202026.03-4285F4?logo=jetpackcompose&logoColor=white)
![Hilt](https://img.shields.io/badge/Hilt-2.56-34A853?logo=android&logoColor=white)
![minSdk](https://img.shields.io/badge/minSdk-28-3DDC84?logo=android&logoColor=white)
![targetSdk](https://img.shields.io/badge/targetSdk-36-3DDC84?logo=android&logoColor=white)

<br>

## 📸 스크린샷

| 검색 화면 | 보관함 화면 | 오프라인 알림 |
| :---: | :---: | :---: |
| _이미지 추가 예정_ | _이미지 추가 예정_ | _이미지 추가 예정_ |

> 실행 화면(GIF/스크린샷)을 `docs/screenshots/`에 추가한 뒤 위 표의 경로를 연결하면 됩니다.

<br>

## ✨ 주요 기능

### 🔍 검색
- 키워드 하나로 **이미지와 동영상을 동시에 검색**하고 최신순으로 정렬해 표시
- **Paging 3**로 스크롤 시 다음 페이지를 끊김 없이 로드 (무한 스크롤)
- 이미지·동영상 두 API의 페이지네이션 종료 시점을 각각 추적해 **한쪽이 끝나도 다른 쪽 결과를 계속 로드**

### 📌 보관함
- 검색 결과에서 원하는 항목을 탭하여 보관함에 저장/삭제
- **SharedPreferences + Gson**으로 로컬에 영속 저장
- `StateFlow` 기반 단일 소스로, 보관 상태가 **검색 화면과 보관함 화면에 실시간 동기화**

### 📡 네트워크 상태 알림
- `ConnectivityManager`를 `Flow`로 감싸 **네트워크 연결 상태를 실시간 감지**
- 오프라인 전환 시 화면 하단에 Snackbar로 "네트워크가 차단되었습니다" 안내, 재연결 시 자동 해제

<br>

## 🛠 기술 스택

| 분류 | 기술 | 사용 목적 |
| --- | --- | --- |
| Language | Kotlin 2.2.0 | 전체 구현 |
| UI | Jetpack Compose (Material 3) | 선언형 UI, 반응형 화면 구성 |
| Architecture | MVVM | UI / 비즈니스 로직 / 데이터 계층 분리 |
| DI | Hilt 2.56 | 의존성 주입, 모듈 단위 구성 |
| Async | Coroutine / Flow | 비동기 처리, 상태 스트림 관리 |
| Paging | Paging 3 | 검색 결과 점진적 로딩 |
| Network | Retrofit 2.11 + OkHttp 4.12 | Kakao 검색 REST API 통신 |
| Image Loading | Coil 2.7 | 썸네일 비동기 로딩/캐싱 |
| Local Storage | SharedPreferences + Gson | 보관함 데이터 영속화 |
| Navigation | Navigation Compose | 화면 간 이동 |

<br>

## 🏗 아키텍처

MVVM + 단방향 데이터 흐름(UDF)을 기반으로, 관심사를 계층으로 분리했습니다.

```
UI (Compose)  ──►  ViewModel  ──►  Repository  ──►  Remote API (Kakao)
   ▲                  │                                    
   └── State (Flow) ◄─┘                              Local (SharedPreferences)
```

- **UI Layer** — `MainActivity`, Compose 화면(`SearchScreen`, `BookmarkScreen`)과 공통 컴포넌트. 상태를 구독해 렌더링하고 이벤트를 ViewModel로 전달
- **ViewModel** — `SearchViewModel`, `BookmarkViewModel`. UI 상태를 `Flow`/`StateFlow`로 노출하고 페이징·보관함 로직을 보유
- **Data Layer** — `SearchRepository`로 데이터 소스를 추상화. 원격(Retrofit)·로컬(SharedPreferences) 접근을 캡슐화
- **DI** — `data/di`의 Hilt 모듈(`NetworkModule`, `RepositoryModule`, `AppModule`, `ConnectivityModule`)이 의존성 그래프를 구성

### 📁 프로젝트 구조

```
com.example.gallerysearchapp
├── GalleryApplication.kt          # @HiltAndroidApp 진입점
├── MainActivity.kt                # Scaffold · Navigation · 오프라인 Snackbar
├── data
│   ├── api/SearchService.kt       # Kakao 이미지/동영상 검색 엔드포인트
│   ├── connectivity/              # NetworkConnectivityObserver (Flow 기반 연결 감지)
│   ├── di/                        # Hilt 모듈 (Network/Repository/App/Connectivity)
│   ├── local/PreferenceStorage.kt # 보관함 영속화 (SharedPreferences + Gson)
│   ├── model/SearchDto.kt         # API 응답 DTO
│   └── repository/                # SearchRepository(+Impl)
├── ui
│   ├── components/                # TopAppBar, NavHost
│   ├── model/ImageData.kt         # 화면용 도메인 모델 (이미지/동영상 통합)
│   ├── screen/                    # SearchScreen, BookmarkScreen, PagingSource
│   └── theme/                     # Color, Theme, Type
└── viewmodel/                     # SearchViewModel, BookmarkViewModel
```

<br>

## 💡 핵심 구현 포인트

### 1. 두 개의 API를 하나의 페이징 스트림으로 병합
이미지(`v2/search/image`)와 동영상(`v2/search/vclip`) API를 **코루틴 `async`로 병렬 호출**한 뒤 `dateTime` 기준 최신순으로 병합합니다.
각 API의 종료 여부(`isImageEnd`, `isVideoEnd`)를 페이징 키에 담아 추적하므로, **한쪽 소스가 먼저 소진돼도 다른 소스의 결과를 계속 불러옵니다.**

```kotlin
val imageDeferred = async { if (isImageEnd) null else runCatching { apiService.searchImage(...) }.getOrNull() }
val videoDeferred = async { if (isVideoEnd) null else runCatching { apiService.searchVideo(...) }.getOrNull() }
// 두 결과를 합쳐 dateTime 내림차순 정렬 후 SearchResult로 반환
```

### 2. Paging 3 커스텀 PagingSource
`ImagePagingSource`에서 페이지·종료 플래그를 함께 들고 다니는 `SearchPagingKey`로 다중 소스 페이지네이션을 제어하고, `Pager`의 결과를 `cachedIn(viewModelScope)`으로 캐싱해 구성 변경에도 안정적으로 동작합니다.

### 3. StateFlow 단일 소스로 보관 상태 동기화
`PreferenceStorage`가 보관함 목록을 `StateFlow`로 보유하는 **단일 진실 공급원(Single Source of Truth)** 역할을 합니다.
검색 화면은 이 Flow를 가공한 `bookmarkIds`를 구독하므로, 어느 화면에서 보관/삭제하든 **양쪽 화면이 즉시 동기화**됩니다.

### 4. Flow 기반 네트워크 연결 감지
`ConnectivityManager.NetworkCallback`을 `callbackFlow`로 감싸 연결 상태를 `Flow<Boolean>`로 노출하고, `awaitClose`로 콜백을 안전하게 해제합니다. `distinctUntilChanged`로 상태 전환 시에만 방출해 중복 알림을 방지합니다.

<br>

## 🚀 시작하기

### 요구 사항
- Android Studio (AGP 8.13 호환 버전)
- JDK 17
- [Kakao Developers](https://developers.kakao.com/)에서 발급받은 **REST API 키**

### 빌드 & 실행
1. 저장소 클론
   ```bash
   git clone https://github.com/boohs1122/GalleryApp.git
   ```
2. Kakao REST API 키 설정 — `app/build.gradle.kts`의 `buildConfigField "KAKAO_API_KEY"` 값에 발급받은 키(`KakaoAK {REST_API_KEY}`)를 입력
   > 🔐 보안을 위해 키는 `local.properties`나 환경 변수로 분리해 관리하는 것을 권장합니다.
3. Android Studio에서 열고 `Run ▶` 또는
   ```bash
   ./gradlew :app:installDebug
   ```

<br>

## 📌 기술적 의사결정 메모

- **Compose 단독 UI** — XML 없이 Compose + Material 3로 전체 화면을 구성해 선언형 UI와 상태 기반 렌더링에 집중
- **이미지/동영상 통합 모델(`ImageData`)** — 서로 다른 응답 DTO를 화면용 단일 모델로 매핑해 UI 단을 단순화
- **계층별 Hilt 모듈 분리** — Network/Repository/Connectivity 등 책임 단위로 모듈을 나눠 의존성 그래프의 가독성과 테스트 용이성 확보
