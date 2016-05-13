package net.serfozo.fibonacci.web.domain;

public class ErrorDto {
    private String message;

    public ErrorDto(final String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
