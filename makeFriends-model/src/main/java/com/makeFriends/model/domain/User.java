package com.makeFriends.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BasePojo {
    private Long id;
    private String mobile;
    private String password;
    private String hxUser;
    private String hxPassword;
}
