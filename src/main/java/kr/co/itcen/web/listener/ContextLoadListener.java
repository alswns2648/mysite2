package kr.co.itcen.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class ContextLoadListener implements ServletContextListener {

   
    public void contextDestroyed(ServletContextEvent sce)  { 
        
    }

    public void contextInitialized(ServletContextEvent serveletContextEvent)  { 
    	String contextConfigLocation = serveletContextEvent.getServletContext().getInitParameter("contextConfigLocation");
    	System.out.println("MySite2 Application starts with" + contextConfigLocation);
    }
	
}
