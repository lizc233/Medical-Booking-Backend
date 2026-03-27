package com.leo.medical.service;

import com.leo.medical.dto.UserLoginDTO;
import com.leo.medical.entity.User;

public interface UserService {
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
