package com.ecobite.reorder_service.scheduler;

import com.ecobite.reorder_service.DTOs.request.ReorderRequest;
import com.ecobite.reorder_service.feign.ProductClient;
import com.ecobite.reorder_service.service.ReorderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReorderScheduler {
    private final ProductClient productClient;
    private final ReorderService reorderService;

    public ReorderScheduler(ProductClient productClient,
                            ReorderService reorderService) {
        this.productClient = productClient;
        this.reorderService = reorderService;
    }

    //Runs every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void autoReorderCheck() {

        System.out.println("Running auto reorder check...");

        var products = productClient.getAllProducts();

        for (var product : products) {

            if (product.getStock() <= product.getReorderLevel()) {

                try {
                    ReorderRequest request = new ReorderRequest();
                    request.setProductId(product.getId());
                    request.setQuantity(50); // or dynamic

                    reorderService.createReorder(request);

                    System.out.println("Reorder created for: " + product.getId());

                } catch (Exception e) {
                    System.out.println("Skipped product: " + product.getId());
                }
            }
        }
    }
}
