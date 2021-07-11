package com.janwes.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Janwes
 * @version 1.0
 * @package com.janwes.entity
 * @date 2021/7/11 14:49
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private String username;

    private String password;

    private String email;

    private String gender;

    private String nickname;
}
