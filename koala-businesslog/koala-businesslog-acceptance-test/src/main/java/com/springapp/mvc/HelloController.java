package com.springapp.mvc;

import business.ContractApplication;
import business.InvoiceApplication;
import business.ProjectApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HelloController {

    @Autowired
    private ContractApplication contractApplication;

    @Autowired
    private ProjectApplication projectApplication;

    @Autowired
    private InvoiceApplication invoiceApplication;

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        List<String> names = new ArrayList<String>();

        names.add("1");
        names.add("2");
        names.add("3");
        names.add("4");

        projectApplication.findSomeProjects(names);
        // TODO 同步问题
        invoiceApplication.addInvoice("xxxx", 1l);
        invoiceApplication.addInvoice("yyyyyyyy", 1l);
        return "hello";
    }
}