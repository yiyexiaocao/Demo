package com.gl.controller.welcome;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @DESC welcomeController
 * @Author by gl on 2016/11/17.
 * @Date 2016/11/17 11:53
 */
@RequestMapping("/welcomeCtl")
@Controller
public class WelcomeController {

    /**
     * @DESC 跳转到欢迎页面
     * @Author gl
     * @DATE 2016/11/17 11:57
     *
     **/
    @RequestMapping("/toWelcomePage")
    public ModelAndView toWelcomePage(){

        ModelAndView mav = new  ModelAndView();
        mav.setViewName("/common/welcome");

        return mav;
    }
}
