package com.mmall.service;

import com.mmall.common.Constant;
import com.mmall.common.SystemResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Arrays;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public SystemResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0) {
            return SystemResponse.getErrorResponse("用户名不存在");
        }
        password = Arrays.toString(DigestUtils.md5Digest(password.getBytes()));
        User user = userMapper.selectLogin(username, password);
        if(user == null) {
            return SystemResponse.getErrorResponse("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return SystemResponse.getSuccessResponse("登录成功", user);
    }

    @Override
    public SystemResponse<String> register(User user) {
        SystemResponse<String> checkValid = this.checkValid(user.getUsername(), Constant.USERNAME);
        if(! checkValid.isSuccess()) {
            return checkValid;
        }
        checkValid = this.checkValid(user.getEmail(), Constant.EMAIL);
        if(! checkValid.isSuccess()) {
            return checkValid;
        }
        user.setRole(Constant.Role.ROLE_CUSTOMER);
        String password = Arrays.toString(DigestUtils.md5Digest(user.getPassword().getBytes()));
        user.setPassword(password);
        int resultCount = userMapper.insert(user);
        if(resultCount == 0) {
            return SystemResponse.getErrorResponse("注册失败");
        }
        return SystemResponse.getSuccessResponse("注册成功");
    }

    @Override
    public SystemResponse<String> checkValid(String str, String type) {
        if(StringUtils.isNotBlank(str)) {
            switch (type) {
                case Constant.USERNAME:
                    int usernameCount = userMapper.checkUsername(str);
                    if(usernameCount > 0) {
                        return SystemResponse.getErrorResponse("用户名已存在");
                    }
                    break;
                case Constant.EMAIL:
                    int emailCount = userMapper.checkEmail(str);
                    if(emailCount > 0) {
                        return SystemResponse.getErrorResponse("email已存在");
                    }
                    break;
            }
        } else {
            return SystemResponse.getErrorResponse("参数不能为空");
        }
        return SystemResponse.getSuccessResponse("校验成功");
    }

    @Override
    public SystemResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if(resultCount > 0) {
            //用户答案正确
            String token = UUID.randomUUID().toString();
            TokenCache.setValue(TokenCache.TOKEN_PREFIX + username, token);
            return SystemResponse.getSuccessResponse("答案正确",token);
        } else {
            return SystemResponse.getErrorResponse("答案错误");
        }
    }

    @Override
    public SystemResponse<String> resetPasswordByToken(String username, String newPassword, String token) {
        //检查用户名是否存在
        SystemResponse usernameValid = this.checkValid(username, Constant.USERNAME);
        if(! usernameValid.isSuccess()) {
            return SystemResponse.getErrorResponse("用户名不存在");
        }
        //获取缓存中的token
        String tokenCache = TokenCache.getValue(TokenCache.TOKEN_PREFIX + username);
        if(StringUtils.isNotBlank(tokenCache) && token.equals(tokenCache)) {
            //token正确进行更新密码
            newPassword = Arrays.toString(DigestUtils.md5Digest(newPassword.getBytes()));
            int resultCount = userMapper.updatePasswordByUsername(username, newPassword);
            if(resultCount > 0) {
                //将使用过的token作废掉
                TokenCache.setValue(TokenCache.TOKEN_PREFIX + username, StringUtils.EMPTY);
                return SystemResponse.getSuccessResponse("修改密码成功");
            } else {
                return SystemResponse.getErrorResponse("修改密码失败");
            }
        } else {
            return SystemResponse.getErrorResponse("token错误或已失效");
        }
    }
}
