package com.maple.maplepicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maple.maplepicturebackend.model.dto.user.UserLoginRequest;
import com.maple.maplepicturebackend.model.dto.user.UserQueryRequest;
import com.maple.maplepicturebackend.model.dto.user.UserRegisterRequest;
import com.maple.maplepicturebackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.maplepicturebackend.model.vo.LoginUserVO;
import com.maple.maplepicturebackend.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Administrator
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-03-16 14:23:27
*/
public interface UserService extends IService<User> {


    long userRegister(UserRegisterRequest userRegisterRequest);

    LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    boolean userLogout(HttpServletRequest request);

    String getEncryptPassword(String userPassword);

    LoginUserVO getLoginUserVO(User user);

    UserVO getUserVO(User user);

    List<UserVO> getUserVOList(List<User> userList);

    User getLoginUser(HttpServletRequest request);

    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);


}
