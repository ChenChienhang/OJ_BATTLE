package com.team10.ojbattle.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/5/19 11:42
 * @version: 1.0
 */
@Data
@NoArgsConstructor
public class ResetUsrVO {

    private String email;

    private String verificationCode;

    private String password;
}
