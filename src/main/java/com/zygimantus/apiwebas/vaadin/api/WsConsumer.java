package com.zygimantus.apiwebas.vaadin.api;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Zygimantus
 */
public abstract class WsConsumer {

        protected final Logger LOGGER = LogManager.getLogger(this.getClass());

        @Autowired
        protected HttpServletRequest request;

        @Autowired
        protected HttpServletResponse response;

        @PostConstruct
        protected abstract void init();

        protected final String getSessionAttribute(String key) {
                String attribute = (String) request.getSession().getAttribute(key);

                return attribute;
        }

}
