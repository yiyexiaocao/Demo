package com.gl.maintest;

import com.gl.base.common.email.SendEmail;

/**
 * Created by gl on 2016/11/12.
 */
public class EmailTest {

    public static void main(String[] args) {
        SendEmail sendEmail = new SendEmail();

        sendEmail.send("861236754@qq.com",null,null,"邮件发送","<h1>你好，</br>笨蛋</h1>",null);

    }
}
