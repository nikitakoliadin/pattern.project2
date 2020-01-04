package com.qthegamep.pattern.project2.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Schema(name = "Error",
        description = "This is error response object")
public class ErrorResponseDTO {

    @NotNull
    @Schema(description = "Error code",
            required = true,
            example = "500")
    @JsonProperty(
            value = "errorCode",
            required = true)
    private Integer errorCode;

    @NotEmpty
    @Schema(description = "Error message",
            required = true,
            example = "INTERNAL_ERROR")
    @JsonProperty(
            value = "errorMessage",
            required = true)
    private String errorMessage;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponseDTO that = (ErrorResponseDTO) o;
        return Objects.equals(errorCode, that.errorCode) &&
                Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorCode, errorMessage);
    }

    @Override
    public String toString() {
        return "ErrorResponseDTO{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
