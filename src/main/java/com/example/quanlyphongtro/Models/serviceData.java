package com.example.quanlyphongtro.Models;

import java.math.BigDecimal;

public class serviceData {
    private String serviceName;
    private BigDecimal servicePrice;

    public serviceData(String serviceName, BigDecimal servicePrice) {
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
    }

    public String getServiceName() {
        return serviceName;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }
}
