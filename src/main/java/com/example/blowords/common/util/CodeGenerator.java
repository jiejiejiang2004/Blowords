package com.example.blowords.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class CodeGenerator {
    public static void main(String[] args) {
        Properties properties = loadProperties();
        String url = properties.getProperty("spring.datasource.url");
        String username = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");
        
        // 代码生成
        FastAutoGenerator.create(url, username, password)
                // 全局配置
                .globalConfig(builder -> {
                    builder.author("杰杰酱") // 设置作者
                            .outputDir(System.getProperty("user.dir") + "/src/main/java") // 输出目录
                            .enableSwagger() // 开启Swagger（适配Knife4j）
                            .commentDate("yyyy-MM-dd") // 注释日期格式
                            .disableOpenDir(); // 生成后不打开文件夹
                })
                // 包配置
                .packageConfig(builder -> {
                    builder.parent("com.example.blowords") // 父包名
                            .moduleName("") // 模块名（空）
                            .entity("entity") // 实体类包名
                            .mapper("mapper") // Mapper包名
                            .service("service") // Service包名
                            .serviceImpl("service.impl") // ServiceImpl包名
                            .controller("controller") // Controller包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper")); // Mapper.xml路径
                })
                // 策略配置
                .strategyConfig(builder -> {
                    // 要生成的表名（填你创建的表）
                    builder.addInclude(
                            "user", 
                            "word_category", 
                            "word", 
                            "word_book", 
                            "book_word", 
                            "user_word_list", 
                            "list_word"
                        )
                            .addTablePrefix("t_", "sys_") // 忽略表前缀（这里没有，可留空）
                            // 实体类策略
                            .entityBuilder()
                            .enableLombok() // 开启Lombok
                            .enableTableFieldAnnotation() // 开启字段注解
                            .idType(com.baomidou.mybatisplus.annotation.IdType.AUTO) // 主键自增
                            // Controller策略
                            .controllerBuilder()
                            .enableRestStyle() // 开启RestController
                            .enableHyphenStyle() // 接口地址下划线转驼峰
                            // Service策略
                            .serviceBuilder()
                            .formatServiceFileName("%sService") // Service命名格式
                            .formatServiceImplFileName("%sServiceImpl");
                })
                // 模板引擎（Freemarker）
                .templateEngine(new FreemarkerTemplateEngine())
                // 执行生成
                .execute();
    }

    /**
     * 加载application.yml配置文件
     */
    private static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            // 读取application.yml文件
            FileInputStream inputStream = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/application.yml");
            
            // 解析yml文件（简单实现，实际项目中可使用更完善的yml解析库）
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            String content = new String(bytes);
            
            // 提取数据库配置
            String[] lines = content.split("\\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.startsWith("url:")) {
                    properties.setProperty("spring.datasource.url", line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("username:")) {
                    properties.setProperty("spring.datasource.username", line.substring(line.indexOf(":") + 1).trim());
                } else if (line.startsWith("password:")) {
                    properties.setProperty("spring.datasource.password", line.substring(line.indexOf(":") + 1).trim());
                }
            }
            
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 如果读取失败，使用默认值
            properties.setProperty("spring.datasource.url", "jdbc:mysql://localhost:3306/blowords?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true");
            properties.setProperty("spring.datasource.username", "root");
            properties.setProperty("spring.datasource.password", "123456");
        }
        return properties;
    }
}
