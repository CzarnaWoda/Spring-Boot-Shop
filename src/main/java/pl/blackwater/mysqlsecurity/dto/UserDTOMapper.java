package pl.blackwater.mysqlsecurity.dto;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import pl.blackwater.mysqlsecurity.model.User;

@Component
public class UserDTOMapper {

    public static UserDTO fromUser(User user){
        final UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        return userDTO;
    }
    public static User fromUserDTO(UserDTO userDTO){
        final User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        return user;
    }
}
