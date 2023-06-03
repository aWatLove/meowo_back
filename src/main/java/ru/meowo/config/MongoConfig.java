package ru.meowo.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.internal.MongoClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {
//    @Bean(name = "readMongoTemplate")
//    public MongoTemplate readMongoTemplate() {
//        MongoClient mongoClient = new MongoClientImpl();
//        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "your_database_name");
//        return mongoTemplate;
//    }
//
//    @Bean(name = "writeMongoTemplate")
//    public MongoTemplate writeMongoTemplate() {
//        MongoClient mongoClient = // Создайте MongoClient для подключения "write"
//        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "your_database_name");
//        return mongoTemplate;
//    }
}
