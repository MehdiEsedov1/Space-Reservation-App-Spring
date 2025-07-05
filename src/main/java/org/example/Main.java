package org.example;

import org.example.config.SpringConfig;
import org.example.console.AppConsole;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        AppConsole appConsole = context.getBean(AppConsole.class);
        appConsole.mainMenu();
    }
}
