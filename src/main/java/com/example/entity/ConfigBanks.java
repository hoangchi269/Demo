package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Configuration
@ConfigurationProperties(prefix = "bank")
@PropertySource(value = "classpath:bank.yaml", factory = YamlPropertySourceFactory.class)
@Component
public class ConfigBanks {
    private List<Bank> banks;
    @Data
    public static class Bank {
        private String bankCode;
        private String privateKey;
        private String ips;
    }
}
