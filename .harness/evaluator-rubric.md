# Evaluator Rubric — ZY-Commerce Android

## Dimensions

| Dim | A | B | C | D |
|-----|---|---|---|---|
| **Architecture** | Strict layer separation, no cross-layer calls | Minor layer leak | Multiple layer violations | Spaghetti |
| **State mgmt** | StateFlow + sealed classes, no leaked state | Mostly correct | Some LiveData mixed in | Uncontrolled mutable state |
| **Error handling** | All errors surfaced to UI, no silent swallows | Most paths covered | Some uncaught exceptions | Crashes on error |
| **Testability** | All ViewModels tested, mocked correctly | 50%+ tested | Some tests, mostly passing | No tests or all failing |
| **UI/UX** | Loading/error/empty states, confirmations, snackbars | Most states covered | Basic loading only | No feedback states |

## Scoring
- **A** (Excellent): All 5 dimensions A or B
- **B** (Good): Max 2 dimensions at C, none at D
- **C** (Acceptable): Max 1 dimension at D
- **D** (Needs work): Any 2+ dimensions at D
