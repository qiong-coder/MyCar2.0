package org.buaa.ly.MyCar.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.exception.BaseError;
import org.buaa.ly.MyCar.exception.FileNotFoundError;
import org.buaa.ly.MyCar.http.ResponseStatusMsg;
import org.buaa.ly.MyCar.service.UploadService;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

@Component("uploadService")
@Slf4j
public class UploadServiceImpl implements UploadService {

    private String media_path;
    private String code_path;

    @Override
    public void setServletContext(@Nonnull  ServletContext servletContext) {
        media_path = servletContext.getRealPath("/")+"../images";
        File file = new File(media_path);
        if ( !file.exists() ) file.mkdirs();

        code_path = media_path+"/codes";
        file = new File(code_path);
        if ( !file.exists() ) file.mkdirs();
    }

    String id2dir(String id) {
        if ( id.length() == 18 ) {
            String id_prefix = id.substring(0, 6);
            String birth_year = id.substring(6, 10);
            String birth_month_days = id.substring(10, 14);
            return "/" + id_prefix + "/" + birth_year + "/" + birth_month_days + "/" + id.substring(14);
        } else {
            return "/" + id;
        }
    }

    void mkPrefix(String prefix) {
        String prefixPath = prefix.substring(0, prefix.lastIndexOf('/'));
        File dir = new File(prefixPath);
        if ( !dir.exists() ) dir.mkdirs();
    }


    String getSuffix(String filename) {
        if ( filename == null || filename.lastIndexOf('.') == -1 ) return null;
        else return filename.substring(filename.lastIndexOf('.'));
    }

    @Override
    public String getCodePrefix() {
        return code_path;
    }

    @Override
    public String find(String type, String id) {
        String prefix = "/" + type + id2dir(id);
        for ( String suffix : new String[] {".jpg",".png"}) {
            File file = new File(media_path + prefix + suffix);
            if ( file.exists() ) return prefix + suffix;
        }
        throw new FileNotFoundError();
    }

    @Override
    public String save(Part attachment) {

        try {
            attachment.write(media_path + "/" +attachment.getSubmittedFileName());
        } catch (IOException e) {
            throw new BaseError(ResponseStatusMsg.ERROR);
        }

        return "/" + attachment.getSubmittedFileName();

    }

    @Override
    public String save(String type, String id, Part attachment) {

        String prefix = media_path + "/" + type + id2dir(id);

        mkPrefix(prefix);

        String fSuffix = getSuffix(attachment.getSubmittedFileName());

        if ( fSuffix == null ) throw new BaseError(ResponseStatusMsg.ERROR);

        try {
            attachment.write(prefix+fSuffix);
        } catch ( IOException e ) {
            throw new BaseError(ResponseStatusMsg.ERROR);
        }

        return type + id2dir(id) + fSuffix;
    }

    @Override
    public void delete(String filename) {
        File file = new File(filename);
        if ( file.exists() ) file.delete();
    }
}
