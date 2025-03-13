package xyz.iggy.rabbit_arnab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeithController {
    @GetMapping
    public String hiKeith(){
        return "Hi Keith Legolas";
    }
}
