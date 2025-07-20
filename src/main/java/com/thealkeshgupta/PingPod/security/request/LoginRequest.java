package com.thealkeshgupta.PingPod.security.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    @Size(min=3, max=20)
    private String username;

    @NotBlank
    @Size(min=6, max=40)
    private String password;
}
