package dev.danvega.ssc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by bslota on 2017-03-14.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(MvcConfig.class);

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        LOG.info("adding viewController");

        registry.addViewController("/").setViewName("regular/home");
        registry.addViewController("/regular/home").setViewName("regular/home");
        registry.addViewController("/special/home").setViewName("special/home");
        registry.addViewController("/regular/login").setViewName("regular/login");
        registry.addViewController("/special/login").setViewName("special/login");
    }
}
