package com.blog.aigetcode.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Authority extends SuperEntity {
    @Column(nullable = false)
    private String name;

    public Authority() {
    }

    public Authority(Role name) {
        this.name = name.value;
    }

    public enum Role implements GrantedAuthority {
        ADMIN(Code.ADMIN),
        USER(Code.USER);

        public final String value;

        Role(String value) {
            this.value = value;
        }

        @Override
        public String getAuthority() {
            return value;
        }

        public class Code {
            public static final String ADMIN = "ROLE_ADMIN";
            public static final String USER = "ROLE_USER";
        }
    }


    public String getName() {
        return name;
    }
}
