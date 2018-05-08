package org.buaa.ly.MyCar.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages = {"org.buaa.ly.MyCar.service",
        "org.buaa.ly.MyCar.exception.advice",
        "org.buaa.ly.MyCar.repository",
        "org.buaa.ly.MyCar.logic"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)})
@Import({TestJpaConfig.class, RedisConfig.class})
public class TestRootConfig {
}
