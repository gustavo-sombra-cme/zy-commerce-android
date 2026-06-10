.PHONY: build test lint clean help

GRADLEW := ./gradlew

## build: Compile debug APK
build:
	$(GRADLEW) assembleDebug

## test: Run all unit tests
test:
	$(GRADLEW) test

## test-single class=<ClassName>: Run a single test class
test-single:
	$(GRADLEW) test --tests "*.$(class)"

## lint: Run Android lint
lint:
	$(GRADLEW) lint

## check: lint + test
check: lint test

## clean: Remove build artifacts
clean:
	$(GRADLEW) clean

## help: Show this help
help:
	@grep -E '^## ' Makefile | sed 's/## //'
