package com.eap.common.config;


import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * 如果不使用MapperScannerConfigurer
 * 那麼需要在AdminApplication配置@MapperScan("com.eap.admin.mapper")
 *
 * @Author 鬼王
 * @Date 2018/04/18 14:10
 */
@Configuration
@AutoConfigureAfter(MybatisConfiguration.class)
public class MapperConfiguration {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(Environment environment){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(environment.getProperty("mybatis.mapper-package"));
        MapperHelper mapperHelper = new MapperHelper();
        Config config = new Config();
        config.setIDENTITY(environment.getProperty("mybatis.identity"));
        config.setOrder(environment.getProperty("mybatis.order"));
        config.setWrapKeyword(environment.getProperty("mybatis.wrap-keyword"));
        config.setNotEmpty(true);
        mapperHelper.setConfig(config);
        mapperScannerConfigurer.setMapperHelper(mapperHelper);

        return mapperScannerConfigurer;
    }

}