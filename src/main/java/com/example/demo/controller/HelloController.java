package com.example.demo.controller;

import com.example.demo.model.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class HelloController {
    @GetMapping("hello")
    public String hello(Model model, HttpServletResponse response ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth);
        model.addAttribute("data","hello");
        return "hello";
    }
    @GetMapping("/loginPage")
    public String loginPage(Model model) {
        model.addAttribute("Users", Users.class);
        return "loginPage";
    }
}
