package org.buaa.ly.MyCar.service.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.exception.BaseError;
import org.buaa.ly.MyCar.exception.CodeCheckErrror;
import org.buaa.ly.MyCar.exception.CodeNotFoundError;
import org.buaa.ly.MyCar.exception.SendCodeError;
import org.buaa.ly.MyCar.http.ResponseStatusMsg;
import org.buaa.ly.MyCar.service.MessageService;
import org.buaa.ly.MyCar.service.UploadService;
import org.buaa.ly.MyCar.utils.ImageVerificationUtil;
import org.buaa.ly.MyCar.utils.TelVerificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component("messageService")
@Slf4j
public class MessageServiceImpl implements MessageService {

    private UploadService uploadService;
    private Map<String,String> cache = Maps.newConcurrentMap();

    private static int TIMEOUT = 60*10;
    private static final int w = 90;
    private static final int h = 30;

    @Autowired
    void setUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @Override
    public String getCode(String phone) {
        String code = TelVerificationUtil.SendCode(phone);

        if ( code == null ) throw new SendCodeError();

        cache.put(phone, code);

        return code;
    }

    @Override
    public void checkCode(String phone, String code) {
        String c = cache.get(phone);

        if ( c == null ) throw new CodeNotFoundError();

        if ( c.compareTo(code) != 0 ) throw new CodeCheckErrror();
    }

    @Override
    public String getPicture() {
        String filename = Long.toString(System.currentTimeMillis());
        try {
            String code = ImageVerificationUtil.outputVerifyImage(w, h, new File(uploadService.getCodePrefix() + "/" + filename + ".jpg"), 4);
            cache.put(filename, code);
        } catch (IOException e) {
            throw new BaseError();
        }
        return filename;
    }

    @Override
    public void checkPicture(String picture, String code) {
        String c = cache.get(picture);

        if ( c == null ) throw new CodeNotFoundError();

        if ( c.compareTo(code) != 0 ) throw new CodeCheckErrror();

        cache.remove(picture);

        uploadService.delete(uploadService.getCodePrefix()+"/"+picture+".jpg");
    }
}
