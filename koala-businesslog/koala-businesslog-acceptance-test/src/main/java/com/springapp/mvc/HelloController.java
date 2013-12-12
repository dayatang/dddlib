package com.springapp.mvc;

import business.ContractApplication;
import business.ProjectApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HelloController {

    @Autowired
    private ContractApplication contractApplication;

    @Autowired
    private ProjectApplication projectApplication;

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        contractApplication.addInvoice("项目名", 1, 2);
        int projectId = 1;
        return "hello";
    }
}