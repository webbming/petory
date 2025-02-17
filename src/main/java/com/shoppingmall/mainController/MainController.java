package com.shoppingmall.mainController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

	 @GetMapping("/controller/{control}")
	    public String controller(@PathVariable("control") String control) {
		if(control.equals("order")) {
			return "order/index";
		}
		else if(control.equals("product")) {
			return "product/index";
		}
		else if(control.equals("board")) {
			return "board/index";
		}
		else if(control.equals("cart")) {
			return "cart/index";
		}
		else if(control.equals("user")) {
			return "user/index";
		}
		else return "/";
	}
}
