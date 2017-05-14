package com.zygimantus.apiwebas;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aeonbits.owner.ConfigFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Zygimantus
 * @param <T>
 */
public abstract class AController<T> implements ApplicationContextAware {

    protected static ApplicationContext context;

    protected final Logger LOGGER = LogManager.getLogger(this.getClass());

    protected final AppConfig appConfig = ConfigFactory.create(AppConfig.class);

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @PostConstruct
    protected abstract void init();

    @RequestMapping(value = "", method = RequestMethod.GET)
    protected abstract T page();

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        AController.context = ac;
    }

    protected String getSessionAttribute(String key) {
        String username = (String) request.getSession().getAttribute(key);

        return username;
    }

}
