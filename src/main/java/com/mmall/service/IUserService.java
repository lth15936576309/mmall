package com.mmall.service;

import com.mmall.common.SystemResponse;
import com.mmall.pojo.User;

public interface IUserService {

    /**
     * 登录校验
     * @param username
     * @param password
     * @return
     */
    SystemResponse<User> login(String username, String password);

    /**
     * 注册保存用户数据
     * @param user
     * @return
     */
    SystemResponse<String> register(User user);

    /**
     * 校验用户数据有效性
     * @param str 用户填写数据
     * @param type 数据类型
     * @return 校验结果
     */
    SystemResponse<String> checkValid(String str, String type);

    SystemResponse<String> checkAnswer(String username, String question, String answer);

    SystemResponse<String> resetPasswordByToken(String username, String newPassword, String token);
}
