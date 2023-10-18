package pl.blackwater.mysqlsecurity.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginRequest {

    @Email(message = "Email który podałeś nie jest poprawnie sformatowany!")
    @NotBlank(message = "Nie podałeś poprawnego emaila")
    @Size(min = 4, max = 32, message = "Email musi składać się od 4 do 32 znaków")
    private String email;
    @NotBlank
    private String password;
}
