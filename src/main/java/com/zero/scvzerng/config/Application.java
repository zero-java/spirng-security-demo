package com.zero.scvzerng.config;

import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;
import java.util.EnumSet;

/**
 *
 * Created by scvzerng on 2017/7/3.
 */
public class Application extends AbstractAnnotationConfigDispatcherServletInitializer  {
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{MvcConfig.class};
    }

    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    public void onStartup(ServletContext context) throws ServletException {
        super.onStartup(context);
        String name = "springSecurityFilterChain";
        DelegatingFilterProxy filter = new DelegatingFilterProxy(name);
        FilterRegistration.Dynamic registration = context.addFilter(name, filter);
        if(registration == null) {
            throw new IllegalStateException("重复注册过滤器 [" + filter + "]");
        } else {
            registration.setAsyncSupported(true);
            EnumSet dispatcherTypes =  EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.ASYNC);
            registration.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
        }
    }

}
