package com.springapp.mvc;

import org.openkoala.businesslog.model.SimpleBusinessLog;
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
    public void printWelcome(ModelMap model) {
        List<String> logs = new ArrayList<String>();
        for (SimpleBusinessLog log : SimpleBusinessLog.findAll(SimpleBusinessLog.class)) {
            logs.add(log.toString());
        }


        model.put("logs", logs);
    }
}
