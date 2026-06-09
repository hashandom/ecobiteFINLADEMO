package com.ecobite.reorder_service.scheduler;

import com.ecobite.reorder_service.DTOs.request.ReorderRequest;
import com.ecobite.reorder_service.feign.ProductClient;
import com.ecobite.reorder_service.repository.ReorderRepository;
import com.ecobite.reorder_service.service.ReorderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReorderScheduler {
    private final ProductClient productClient;
    private final ReorderService reorderService;
    private final ReorderRepository reorderRepository;

    public ReorderScheduler(ProductClient productClient,
                            ReorderService reorderService, ReorderRepository reorderRepository) {
        this.productClient = productClient;
        this.reorderService = reorderService;
        this.reorderRepository = reorderRepository;
    }

    //Runs every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void autoReorderCheck() {

        System.out.println("Running auto reorder check...");

        var products = productClient.getAllProducts();

        for (var product : products) {

            if (product.getStock() <= product.getReorderLevel()) {
                boolean alreadyExists =
                        reorderRepository.existsByProductIdAndStatus(
                                product.getId(),
                                "CREATED"
                        );

                if (alreadyExists) {

                    System.out.println(
                            "Reorder already exists for: "
                                    + product.getId()
                    );

                    continue;
                }

                try {
                    ReorderRequest request = new ReorderRequest();
                    request.setProductId(product.getId());
                    int reorderQty =
                            product.getReorderLevel() * 2;

                    request.setQuantity(reorderQty);

                    reorderService.createReorder(request);

                    System.out.println("Reorder created for: " + product.getId());

                } catch (Exception e) {

                    System.out.println(
                            "Skipped product: "
                                    + product.getId()
                                    + " Reason: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }
}
