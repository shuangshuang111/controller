package com.dongnaoedu.mall.common.exception;

/**
 * 
 */
public class MallUploadException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;

    public MallUploadException(String msg){
        super(msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
