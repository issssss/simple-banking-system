package org.simple.bankingsystem.api.messages;

import org.springframework.http.HttpStatus;

public class Response {
    private Long id;
    private String message;
    private HttpStatus status;

    public Response() {}
    public Response(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
    public Response(Long id, String message, HttpStatus status) {
        this.id = id;
        this.message = message;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
