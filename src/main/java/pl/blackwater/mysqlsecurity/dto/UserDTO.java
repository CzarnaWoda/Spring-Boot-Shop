package pl.blackwater.mysqlsecurity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean enabled;
    private boolean isNotLocked;
    private boolean isUsingMfa;
    private LocalDateTime createdAt;
}
