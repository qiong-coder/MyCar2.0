package org.buaa.ly.MyCar.service;

import org.springframework.web.context.ServletContextAware;

import javax.servlet.http.Part;

public interface UploadService extends ServletContextAware {

    String getCodePrefix();

    String save(String type, String id, Part attachment);

    String save(Part attachment);

    String find(String type, String id);

    void delete(String filename);

}
