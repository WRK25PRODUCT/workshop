package com.gft.workshop.config;

import org.springframework.context.annotation.Bean;
import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DozerConfig {

    @Bean(name="mapper")
    DozerBeanMapper getMapper() {

        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();

        List<String> mapingFiles = List.of("dozer-configuration-mappings.xml");

        dozerBeanMapper.setMappingFiles(mapingFiles);

        return dozerBeanMapper;
    }
}
