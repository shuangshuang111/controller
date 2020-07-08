package com.dongnaoedu.mall.service;


import com.dongnaoedu.mall.pojo.front.Member;

public interface LoginService {

    Member userLogin(String username, String password);

    Member getUserByToken(String token);

    int logout(String token);
}
