package com.springapp.mvc;

import org.openkoala.businesslog.model.DefaultBusinessLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zjzhai
 * Date: 12/11/13
 * Time: 5:08 PM
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.put("logs", "size:" + DefaultBusinessLog.findAll(DefaultBusinessLog.class).size());
        return "admin";

    }
}
