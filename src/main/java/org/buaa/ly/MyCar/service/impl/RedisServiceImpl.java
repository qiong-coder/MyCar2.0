package org.buaa.ly.MyCar.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.http.dto.AccountDTO;
import org.buaa.ly.MyCar.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("redisService")
@Slf4j
public class RedisServiceImpl implements RedisService {

    private StringRedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    private final String ACCOUNT_PREFIX="mycar:account:";
    private final int ACCOUNT_EXPIRE_DAYS=15;

    private final String CODE_PREFIX="mycar:code:";
    private final int CODE_EXPIRE_MINUTES=15;
    private final String CODE_TIMEOUT_PREFIX="mycar:code:timeout:";
    private final int CODE_TIMEOUT_EXPIRE_MINUTES=10;

    private final String PICTURE_PREFIX="mycar:picture:";
    private final int PICTURE_EXPIRE_MINUTES=15;


    @Override
    public AccountDTO findAccountDTO(String token) {
        String value = redisTemplate.opsForValue().get(ACCOUNT_PREFIX+token);
        if ( value == null ) return null;
        else return JSON.parseObject(value, AccountDTO.class);
    }

    @Override
    public void putAccountDTO(AccountDTO accountDTO) {
        redisTemplate.opsForValue().set(ACCOUNT_PREFIX+accountDTO.getToken(), accountDTO.toString(), ACCOUNT_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override
    public void deleteAccountDTO(String token) {
        redisTemplate.delete(ACCOUNT_PREFIX+token);
    }

    @Override
    public String findPhoneCode(String phone) {
        String code = redisTemplate.opsForValue().get(CODE_PREFIX+phone);
        return code;
    }

    @Override
    public boolean findPhoneCodeTimeout(String phone) {
        return redisTemplate.opsForValue().get(CODE_TIMEOUT_PREFIX+phone) != null;
    }

    @Override
    public void putPhoneCode(String phone, String code) {
        redisTemplate.opsForValue().set(CODE_TIMEOUT_PREFIX+phone,code,CODE_TIMEOUT_EXPIRE_MINUTES,TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(CODE_PREFIX+phone,code,CODE_EXPIRE_MINUTES,TimeUnit.MINUTES);
    }

    @Override
    public void deletePhoneCode(String phone) {
        redisTemplate.delete(CODE_TIMEOUT_PREFIX+phone);
        redisTemplate.delete(CODE_PREFIX+phone);
    }

    @Override
    public String findPicture(String filename) {
        return redisTemplate.opsForValue().get(PICTURE_PREFIX+filename);
    }

    @Override
    public void putPicture(String filename, String code) {
        redisTemplate.opsForValue().set(PICTURE_PREFIX+filename, code, PICTURE_EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

    @Override
    public void deletePicture(String filename) {
        redisTemplate.delete(PICTURE_PREFIX+filename);
    }
}
