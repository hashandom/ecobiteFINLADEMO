package com.ecobite.dashboard_service.service;

import com.ecobite.dashboard_service.dto.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DashboardServiceImpl implements DashboardService{
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Response getSummary() {

        Response summary = new Response();

        Integer totalProducts = restTemplate.getForObject(
                "http://localhost:8080/products/count", Integer.class);

        Integer lowStock = restTemplate.getForObject(
                "http://localhost:8080/products/low-stock/count", Integer.class);

        Integer spoilage = restTemplate.getForObject(
                "http://localhost:8080/spoilage/count", Integer.class);

        summary.setTotalProducts(totalProducts != null ? totalProducts : 0);
        summary.setLowStockProducts(lowStock != null ? lowStock : 0);
        summary.setSpoilageCount(spoilage != null ? spoilage : 0);

        return summary;
    }
}
