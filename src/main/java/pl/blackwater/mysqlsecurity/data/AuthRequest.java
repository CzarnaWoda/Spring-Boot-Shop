package pl.blackwater.mysqlsecurity.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AuthRequest {

    @NotBlank(message = "Nie podałeś poprawnego imienia")
    @Size(min = 4, max = 32, message = "Imie musi składać się od 4 do 32 znaków")
    private String firstName;
    @NotBlank(message = "Nie podałeś poprawnego nazwiska")
    @Size(min = 4, max = 32, message = "Nazwisko musi składać się od 4 do 32 znaków")
    private String lastName;
    @Email(message = "Email który podałeś nie jest poprawnie sformatowany!")
    @NotBlank(message = "Nie podałeś poprawnego emaila")
    @Size(min = 4, max = 32, message = "Email musi składać się od 4 do 32 znaków")
    private String email;
    @NotBlank(message = "Nie podałeś poprawnego hasła")
    @Size(min = 10, max = 128, message = "Hasło musi składać sie z conajmniej 10 znaków i nie może mieć więcej niż 128 znaków")
    private String password;
    @NotBlank(message = "Nie podałeś poprawnego numeru telefonu")
    @Size(min = 9, max = 9 , message = "Numer składa się z 9 cyfr")
    private String phone;



}
