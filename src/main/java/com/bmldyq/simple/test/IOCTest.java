package com.bmldyq.simple.test;

import com.bmldyq.simple.core.container.ioc.ApplicationContextFactory;
import com.bmldyq.simple.test.service.TestService;

public class IOCTest {

    public static void main(String[] args) {
        ApplicationContextFactory acf = ApplicationContextFactory.getApplicationContextFactoryInstance();
        try {
            TestService service = acf.getBean(TestService.class);
            System.out.println(service.getMsg("ddd"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
