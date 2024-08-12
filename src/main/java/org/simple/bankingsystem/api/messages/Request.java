package org.simple.bankingsystem.api.messages;

public class Request {
    private Long id;

    public Request(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
