package org.example.meetlearning.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoovUserDto {
    private String email;
    private String username;
    private String areaCode = "+86";
    private String phone;
    private String userId;

    public VoovUserDto(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
