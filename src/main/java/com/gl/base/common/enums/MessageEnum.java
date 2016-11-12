package com.gl.base.common.enums;

/**
 * Created by gl on 2016/11/12.
 */
public enum  MessageEnum {

    /**
     * 邮件发送失败
     */
    EMAIL_SEND_FAILURE(400,"邮件发送失败");

    /**
     * code码
     */
    private   Integer value;
    /**
     * 描述
     */
    private   String desc;

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    MessageEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
