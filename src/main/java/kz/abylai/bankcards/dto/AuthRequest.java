package kz.abylai.bankcards.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String phone;
    private String password;
}
