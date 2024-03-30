package com.irojas.demojwt.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository; 

    @Transactional
    public UserResponse updateUser(UserRequest userRequest) {
       
        User user = User.builder()
                        .id(userRequest.getId()) 
                        .firstname(userRequest.getFirstname())
                        .lastname(userRequest.getLastname()) 
                        .country(userRequest.getCountry())
                        .role(userRequest.getRole())
                        .build();
        
        userRepository.updateUser(user.getId(), user.getFirstname(), user.getLastname(), user.getCountry(), user.getRole());

        return new UserResponse("El usuario se actualizó satisfactoriamente");
    }

    public UserDTO getUser(Integer id) {
        User user = userRepository.findById(id).orElse(null);
       
        if (user != null) {
            UserDTO userDTO = UserDTO.builder()
                                    .id(user.getId())
                                    .username(user.getUsername())
                                    .firstname(user.getFirstname())
                                    .lastname(user.getLastname())
                                    .country(user.getCountry())
                                    .role(user.getRole())
                                    .build();
            return userDTO;
        }
        return null;
    }
}
