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

# 格式 化
format:
	$(MVNW) $(MVN_ARGS) spotless:apply

# 检查格式
format-check:
	$(MVNW) $(MVN_ARGS) spotless:check

# 测试
test:
	$(MVNW) $(MVN_ARGS) test

# 验证
verify:
	$(MVNW) $(MVN_ARGS) verify

# 清理
clean:
	$(MVNW) $(MVN_ARGS) clean
