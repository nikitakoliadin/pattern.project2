package com.qthegamep.pattern.project2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qthegamep.pattern.project2.model.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(name = "Error",
        description = "This is error response object")
public class ErrorResponseDTO {

    @Schema(description = "Request ID",
            required = true,
            example = "WYAtQAwLqb")
    @JsonProperty(
            value = "requestId",
            required = true)
    private String requestId;

    @Schema(description = "Response code",
            required = true,
            example = "INTERNAL_ERROR")
    @JsonProperty(
            value = "responseCode",
            required = true)
    private ResponseCode responseCode;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponseDTO that = (ErrorResponseDTO) o;
        return Objects.equals(requestId, that.requestId) &&
                responseCode == that.responseCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, responseCode);
    }

    @Override
    public String toString() {
        return "ErrorResponseDTO{" +
                "requestId='" + requestId + '\'' +
                ", responseCode=" + responseCode +
                '}';
    }
}
