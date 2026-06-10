# Testing Standards — ZY-Commerce Android

## Test Pyramid
```
         [E2E / Instrumented]   ← Espresso / Compose UI tests  (few)
       [Integration tests]      ← Repository + fake server      (some)
    [Unit tests]                ← ViewModel + UseCase logic      (many)
```

## Unit Test Rules
- **ViewModel tests**: mock use cases with MockK, use `StandardTestDispatcher`
- **Use case tests**: mock repository, test business logic only
- **No Android framework** in unit tests (`junit4`, not `junit-ext`)
- Use **Turbine** for StateFlow / Channel assertions
- Replace `Dispatchers.Main` with `StandardTestDispatcher` in `@Before`

## Coverage Requirements
| Layer | Required Coverage |
|-------|-------------------|
| Use cases | 80%+ |
| ViewModels | 70%+ |
| Repository impls | 60%+ |

## Running Tests
```bash
./gradlew test                     # all unit tests
./gradlew test --tests '*.ProductListViewModelTest'  # single class
./gradlew connectedAndroidTest     # instrumented tests (requires emulator)
```
