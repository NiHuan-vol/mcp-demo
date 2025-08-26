package com.demo.core.license;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * License初始化器，用于在应用启动时设置默认License信息
 */
@Component
public class LicenseInitializer implements ApplicationRunner {
    
    @Autowired
    private LicenseManager licenseManager;
    
    @Override
    public void run(ApplicationArguments args) {
        // 初始化默认License信息
        initializeDefaultLicenses();
    }
    
    /**
     * 初始化默认License信息
     */
    private void initializeDefaultLicenses() {
        // 为core模块设置默认License
        LicenseInfo coreLicense = new LicenseInfo();
        coreLicense.setTenantId("default-tenant");
        coreLicense.setModuleId("core");
        coreLicense.setRemainingCalls(10000);
        coreLicense.setTotalCallsLimit(10000);
        coreLicense.setExpiryDate(LocalDateTime.now().plus(365, ChronoUnit.DAYS));
        coreLicense.setValid(true);
        coreLicense.setLicenseType("DEFAULT");
        coreLicense.setIssuedAt(LocalDateTime.now());
        
        // 注册License
        licenseManager.registerLicense(coreLicense);
        
        // 为manufacturing模块设置默认License
        LicenseInfo manufacturingLicense = new LicenseInfo();
        manufacturingLicense.setTenantId("default-tenant");
        manufacturingLicense.setModuleId("manufacturing");
        manufacturingLicense.setRemainingCalls(5000);
        manufacturingLicense.setTotalCallsLimit(5000);
        manufacturingLicense.setExpiryDate(LocalDateTime.now().plus(365, ChronoUnit.DAYS));
        manufacturingLicense.setValid(true);
        manufacturingLicense.setLicenseType("DEFAULT");
        manufacturingLicense.setIssuedAt(LocalDateTime.now());
        
        // 注册License
        licenseManager.registerLicense(manufacturingLicense);
        
        // 为recommendation模块设置默认License
        LicenseInfo recommendationLicense = new LicenseInfo();
        recommendationLicense.setTenantId("default-tenant");
        recommendationLicense.setModuleId("recommendation");
        recommendationLicense.setRemainingCalls(5000);
        recommendationLicense.setTotalCallsLimit(5000);
        recommendationLicense.setExpiryDate(LocalDateTime.now().plus(365, ChronoUnit.DAYS));
        recommendationLicense.setValid(true);
        recommendationLicense.setLicenseType("DEFAULT");
        recommendationLicense.setIssuedAt(LocalDateTime.now());
        
        // 注册License
        licenseManager.registerLicense(recommendationLicense);
        
        System.out.println("默认License信息初始化完成");
    }
}