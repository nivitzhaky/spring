package com.example.demo;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Spring Boot Hello案例
 *
 * Created by bysocket on 26/09/2017.
 */
@RestController
public class AppController {

    @RequestMapping(value = "/app",method = RequestMethod.GET)
    public String sayHello() {
        return "Hello";
    }

    @RequestMapping(value = "/app",method = RequestMethod.POST)
    public String postBot(@RequestBody Map<String,Object> webhook) {
        System.out.println(webhook);
        String response = "{\"fulfillmentText\": \"\",\n" +
                "     \"source\": \"dad jokes\"\n" +
                "    }";
        if (webhook.get("queryResult") != null){
            Map<String, Object> query =  ((Map<String, Object>)webhook.get("queryResult"));
            if (query.get("parameters") != null) {
                Map<String, Object> parameters =  ((Map<String, Object>)query.get("parameters"));
                if (parameters.get("subject") != null) {
                    String subject =  ((String)parameters.get("subject"));
                    if ((subject != null) && !(subject.equals(""))) {
                        response = "{\"fulfillmentText\": \"" + subject + "1\",\n" +
                                "     \"source\": \"dad jokes\"\n" +
                                "    }";

                    }
                }
            }
        }


        return response;
    }

}