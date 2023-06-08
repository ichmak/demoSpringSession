package org.example;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapAttributeConfig;
import com.hazelcast.config.MapIndexConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.hazelcast.HazelcastIndexedSessionRepository;
import org.springframework.session.hazelcast.PrincipalNameExtractor;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.io.IOException;

@Configuration
@EnableWebMvc
@EnableHazelcastHttpSession
@ComponentScan(basePackages = { "org.example.controller" })
public class AppConfig {


    @Bean
    HazelcastInstance hazelcastInstance() throws IOException {
        MapAttributeConfig attributeConfig = new MapAttributeConfig()
                .setName(HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
                .setExtractor(PrincipalNameExtractor.class.getName());


        Config config = new XmlConfigBuilder(ResourceUtils.getFile("classpath:hazelcast.xml").getPath()).build();

        config.getMapConfig(HazelcastIndexedSessionRepository.DEFAULT_SESSION_MAP_NAME)
                .addMapAttributeConfig(attributeConfig)
                .setTimeToLiveSeconds(0)
                .setMaxIdleSeconds(60 * 60 * 24 * 3)
                .setEvictionPolicy(EvictionPolicy.LRU)
                .addMapIndexConfig(new MapIndexConfig(
                        HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE, false));

        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

}
