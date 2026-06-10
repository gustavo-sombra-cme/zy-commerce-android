# Architecture — ZY-Commerce Android

## Layer Map
```
app/src/main/kotlin/com/zycommerce/android/
├── ZyCommerceApp.kt              ← Hilt application class
├── MainActivity.kt               ← Single activity, hosts NavHost
├── navigation/
│   └── AppNavigation.kt          ← NavHost + route definitions
├── core/
│   ├── network/
│   │   └── NetworkModule.kt      ← Hilt module: OkHttpClient, Retrofit
│   └── ui/theme/
│       ├── Theme.kt              ← MaterialTheme wrapper
│       ├── Color.kt              ← Brand color tokens
│       └── Type.kt               ← Typography scale
└── features/
    └── catalog/
        ├── data/
        │   ├── remote/
        │   │   ├── CatalogApiService.kt    ← Retrofit interface
        │   │   └── dto/                    ← Wire types (serializable)
        │   └── CatalogRepositoryImpl.kt   ← Repository impl: maps DTO → domain
        ├── di/
        │   └── CatalogModule.kt           ← Hilt bindings for catalog
        ├── domain/
        │   ├── model/Product.kt           ← Domain models (no framework deps)
        │   ├── repository/CatalogRepository.kt  ← Repository interface
        │   └── usecase/                   ← One class per use case
        └── presentation/
            ├── list/
            │   ├── ProductListScreen.kt   ← Composable screen
            │   ├── ProductListViewModel.kt← StateFlow + events
            │   └── ProductListUiState.kt  ← Sealed interface
            └── detail/
                ├── ProductDetailScreen.kt
                ├── ProductDetailViewModel.kt
                └── ProductDetailUiState.kt
```

## Layer Rules
| From | May call | Must NOT call |
|------|----------|---------------|
| Composable (UI) | ViewModel | Repository, UseCase, ApiService |
| ViewModel | UseCase | Repository, ApiService, Android UI |
| UseCase | Repository | ApiService, ViewModel |
| Repository impl | ApiService, DTOs | ViewModel, UseCase |

## State Management Pattern
- **UI state**: `StateFlow<SealedInterface>` exposed from ViewModel
- **One-shot events**: `Channel<EventType>` consumed via `LaunchedEffect`
- **No mutable state in Composables** beyond ephemeral dialog open/close

## Error Handling
- All repository methods return `Result<T>` — never throw across layers
- ViewModel converts `Result` failures to error state or event snackbars
- Network errors are caught inside `runCatching {}` in repository impls
