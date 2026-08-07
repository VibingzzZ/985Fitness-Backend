package com.fitness985.fitnesscommon.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ResultTest {

    @Test
    void successWithoutDataShouldHaveSuccessCodeAndNoData() {
        Result<Void> result = Result.success();

        assertThat(result.getCode()).isEqualTo(1);
        assertThat(result.getData()).isNull();
        assertThat(result.getMsg()).isNull();
    }

    @Test
    void successWithDataShouldHaveSuccessCodeAndProvidedData() {
        Result<String> result = Result.success("hello");

        assertThat(result.getCode()).isEqualTo(1);
        assertThat(result.getData()).isEqualTo("hello");
        assertThat(result.getMsg()).isNull();
    }

    @Test
    void successWithNullDataShouldStillReportSuccessCode() {
        Result<String> result = Result.success(null);

        assertThat(result.getCode()).isEqualTo(1);
        assertThat(result.getData()).isNull();
    }

    @Test
    void successShouldSupportComplexGenericTypes() {
        Result<List<String>> result = Result.success(List.of("a", "b"));

        assertThat(result.getData()).containsExactly("a", "b");
        assertThat(result.getCode()).isEqualTo(1);
    }

    @Test
    void errorShouldHaveFailureCodeAndMessageWithNoData() {
        Result<String> result = Result.error("something went wrong");

        assertThat(result.getCode()).isEqualTo(0);
        assertThat(result.getMsg()).isEqualTo("something went wrong");
        assertThat(result.getData()).isNull();
    }

    @Test
    void errorWithNullMessageShouldKeepMessageNull() {
        Result<String> result = Result.error(null);

        assertThat(result.getCode()).isEqualTo(0);
        assertThat(result.getMsg()).isNull();
    }

    @Test
    void settersShouldUpdateResultState() {
        Result<Integer> result = new Result<>();

        result.setCode(2);
        result.setMsg("custom");
        result.setData(42);

        assertThat(result.getCode()).isEqualTo(2);
        assertThat(result.getMsg()).isEqualTo("custom");
        assertThat(result.getData()).isEqualTo(42);
    }

    @Test
    void equalsAndHashCodeShouldBeBasedOnFields() {
        Result<String> first = Result.success("value");
        Result<String> second = Result.success("value");

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentData() {
        Result<String> first = Result.success("value-1");
        Result<String> second = Result.success("value-2");

        assertThat(first).isNotEqualTo(second);
    }
}