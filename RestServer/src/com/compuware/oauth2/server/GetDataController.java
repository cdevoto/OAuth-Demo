package com.compuware.oauth2.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/get_data")
public class GetDataController {
	
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleRedirect(HttpServletRequest request,
                                       HttpServletResponse response) {
	     return new ModelAndView("get_data");
            
    }
	
}
