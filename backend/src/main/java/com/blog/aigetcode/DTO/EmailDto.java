package com.blog.aigetcode.DTO;

import javax.validation.constraints.NotNull;

public class EmailDto {

    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String subject;
    private String text;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
