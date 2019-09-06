package com.qthegamep.pattern.project2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qthegamep.pattern.project2.model.ResponseCode;

import java.util.Objects;

public class ErrorResponseDTO {

    @JsonProperty(
            value = "requestId",
            required = true)
    private String requestId;
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
