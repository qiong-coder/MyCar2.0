package org.buaa.ly.MyCar.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.exception.*;
import org.buaa.ly.MyCar.service.MessageService;
import org.buaa.ly.MyCar.service.RedisService;
import org.buaa.ly.MyCar.service.UploadService;
import org.buaa.ly.MyCar.utils.ImageVerificationUtil;
import org.buaa.ly.MyCar.utils.TelVerificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Random;

@Component("messageService")
@Slf4j
public class MessageServiceImpl implements MessageService {

    private UploadService uploadService;
    private RedisService redisService;

    private static final int w = 90;
    private static final int h = 30;

    @Autowired
    void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public String getCode(String phone) {

        if ( redisService.findPhoneCodeTimeout(phone) ) {
            throw new CodeRequestTimeoutError();
        }

        String code = "4444"; //String.format("%4.0f",Math.random()*10000); //TelVerificationUtil.SendCode(phone);

        if ( code == null ) throw new SendCodeError();

        redisService.putPhoneCode(phone, code);

        return code;
    }

    @Override
    public void checkCode(String phone, String code) {
        String c = redisService.findPhoneCode(phone);

        if ( c == null ) throw new CodeNotFoundError();

        if ( c.compareToIgnoreCase(code) != 0 ) throw new CodeCheckErrror();

        redisService.deletePhoneCode(phone);
    }

    @Override
    public String getPicture() {

        String filename = Long.toString(System.currentTimeMillis());
        try {
            String code = ImageVerificationUtil.outputVerifyImage(w, h, new File(uploadService.getCodePrefix() + "/" + filename + ".jpg"), 4);
            redisService.putPicture(filename, code);
        } catch (IOException e) {
            throw new BaseError();
        }
        return filename;

    }

    @Override
    public void checkPicture(String picture, String code) {

        String c = redisService.findPicture(picture);

        if ( c == null ) throw new CodeNotFoundError();

        if ( c.compareToIgnoreCase(code) != 0 ) throw new CodeCheckErrror();

        redisService.deletePicture(picture);

        uploadService.delete(uploadService.getCodePrefix()+"/"+picture+".jpg");

    }
}
