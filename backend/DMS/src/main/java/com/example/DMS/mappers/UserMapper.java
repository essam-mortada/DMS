package com.example.DMS.mappers;

import com.example.DMS.DTO.userDTO;
import com.example.DMS.models.User;

public class UserMapper {
    public static userDTO toDTO(User user) {
        userDTO dto = new userDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
