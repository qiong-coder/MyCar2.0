package org.buaa.ly.MyCar.exception;

import static org.buaa.ly.MyCar.http.ResponseStatusMsg.NO_FILE;

public class FileNotFoundError extends BaseError {

    public FileNotFoundError() {
        super(NO_FILE);
    }

}
