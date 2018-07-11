package com.iflyVoice.voice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("demo")
public class TestController {
	
	   @RequestMapping(value="/index",method = RequestMethod.GET)
	   public String test(){
		  return "index";
	   }

}
