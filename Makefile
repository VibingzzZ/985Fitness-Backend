.PHONY: help format format-check test verify clean

MVNW := ./mvnw
MVN_ARGS := --batch-mode --no-transfer-progress

help:
	@echo "Available commands:"
	@echo "  make format        Apply Java formatting"
	@echo "  make format-check  Check Java formatting"
	@echo "  make test          Run tests"
	@echo "  make verify        Run Maven verification"
	@echo "  make clean         Remove build outputs"

format:
	$(MVNW) $(MVN_ARGS) spotless:apply

format-check:
	$(MVNW) $(MVN_ARGS) spotless:check

test:
	$(MVNW) $(MVN_ARGS) test

verify:
	$(MVNW) $(MVN_ARGS) verify

clean:
	$(MVNW) $(MVN_ARGS) clean
