package com.cos.bank.user.dto;

import com.cos.bank.user.domain.User;
import com.cos.bank.util.CustomDataFormatter;
import lombok.*;

public class LoginDto {

    @Getter
    @Setter
    public static class Request {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String username;
        private String createdAt;

        public static Response of(User user) {
            return Response.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .createdAt(CustomDataFormatter.of(user.getCreatedAt()))
                    .build();
        }
    }

}
