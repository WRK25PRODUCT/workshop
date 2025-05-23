package com.gft.workshop.config.business;

import lombok.Generated;

@Generated
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }

}
