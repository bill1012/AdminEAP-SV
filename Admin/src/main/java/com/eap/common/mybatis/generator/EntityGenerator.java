package com.eap.common.mybatis.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import static org.apache.ibatis.io.Resources.getResourceAsStream;

/**
 * @Author 鬼王
 * @Date 2018/04/20 07:43
 */
public class EntityGenerator {

    public static void main(String[] args) throws Exception {
        ConfigurationParser cp = new ConfigurationParser(null);
        Configuration config = cp.parseConfiguration(getResourceAsStream("builder/generatorConfig.xml"));
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, null);
        myBatisGenerator.generate(null);
    }

}
