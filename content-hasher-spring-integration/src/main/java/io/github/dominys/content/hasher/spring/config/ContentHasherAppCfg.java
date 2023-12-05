package io.github.dominys.content.hasher.spring.config;

import io.github.dominys.content.hasher.spring.ContentHasherFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContentHasherAppCfg {

    @Bean
    @ConditionalOnMissingBean
    public ContentHasherFactoryBean contentHasherFactoryBean() {
        return new ContentHasherFactoryBean();
    }
}
