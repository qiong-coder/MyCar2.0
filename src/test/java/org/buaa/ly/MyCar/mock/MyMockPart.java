package org.buaa.ly.MyCar.mock;

import org.springframework.mock.web.MockPart;

import java.io.FileOutputStream;
import java.io.IOException;

public class MyMockPart extends MockPart {

    public MyMockPart(String name, String fileName, byte[] content) {
        super(name, fileName, content);
    }


    @Override
    public void write(String fileName) throws IOException {
        FileOutputStream file  = new FileOutputStream(fileName);
        byte[] content = new byte[(int)getSize()];
        getInputStream().read(content);
        file.write(content);
    }
}
