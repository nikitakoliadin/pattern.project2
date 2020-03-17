package com.qthegamep.pattern.project2.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Error {

    @BsonId
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(
            value = "_id",
            required = true)
    private ObjectId objectId;

    @NotEmpty
    @BsonProperty(value = "requestId")
    @JsonProperty(
            value = "requestId",
            required = true)
    private String requestId;

    @NotNull
    @BsonProperty(value = "errorCode")
    @JsonProperty(
            value = "errorCode",
            required = true)
    private Integer errorCode;

    @NotEmpty
    @BsonProperty(value = "errorMessage")
    @JsonProperty(
            value = "errorMessage",
            required = true)
    private String errorMessage;

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

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
        Error error = (Error) o;
        return Objects.equals(objectId, error.objectId) &&
                Objects.equals(requestId, error.requestId) &&
                Objects.equals(errorCode, error.errorCode) &&
                Objects.equals(errorMessage, error.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, requestId, errorCode, errorMessage);
    }

    @Override
    public String toString() {
        return "Error{" +
                "objectId=" + objectId +
                ", requestId='" + requestId + '\'' +
                ", errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
