package org.buaa.ly.MyCar.service;

import org.buaa.ly.MyCar.http.dto.AccountDTO;

public interface RedisService {

    AccountDTO findAccountDTO(String token);

    void putAccountDTO(AccountDTO accountDTO);

    void deleteAccountDTO(String token);

    String findPhoneCode(String phone);

    boolean findPhoneCodeTimeout(String phone);

    void putPhoneCode(String phone, String code);

    void deletePhoneCode(String phone);

    String findPicture(String filename);

    void putPicture(String filename, String code);

    void deletePicture(String filename);
}
