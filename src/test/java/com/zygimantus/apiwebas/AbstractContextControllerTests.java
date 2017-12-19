package com.zygimantus.apiwebas;

import com.zygimantus.apiwebas.config.DispatcherConfig;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Zygimantus
 */
@ActiveProfiles("test")
@WebAppConfiguration
@ContextConfiguration(classes = {DispatcherConfig.class}, loader = AnnotationConfigWebContextLoader.class)
public class AbstractContextControllerTests {

    protected final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    protected WebApplicationContext wac;

}
