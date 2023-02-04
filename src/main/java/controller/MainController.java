package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public void main(){
        System.out.println("Test Connect docker-jenkinks");

    }
}
