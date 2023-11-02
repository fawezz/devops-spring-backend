package tn.esprit.devops_project.services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void addProduct() {
        final Product product = new Product();
        product.setTitle("new title");
        product.setPrice(12.5F);
        product.setCategory(ProductCategory.ELECTRONICS);
        //case stock is not found
        assertThrows(NullPointerException.class, ()-> {
            this.productService.addProduct(product,700L);
        });
        //case stock exists
        this.productService.addProduct(product,1L);
        assertEquals(this.productService.retreiveAllProduct().size(), 3);
    }
    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retreiveProductStock() {
        final List<Product> products = productService.retreiveProductStock(1L);
        assertEquals(products.size(),2);
    }
    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retrieveProduct() {
        final Product product = this.productService.retrieveProduct(2L);
        assertEquals(product.getTitle(), "title2");
        //case of ID not found
        assertThrows(NullPointerException.class, ()-> {
            this.productService.retrieveProduct(700L);
        });
    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retreiveAllProduct() {
        final List<Product> allProducts = this.productService.retreiveAllProduct();
        assertEquals(allProducts.size(), 2);
    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retrieveProductByCategory() {
        final List<Product> clothingProducts = productService.retrieveProductByCategory(ProductCategory.CLOTHING);
        assertEquals(clothingProducts.size(),1);
        //testing valueOf
        assertEquals(ProductCategory.valueOf(clothingProducts.get(0).getCategory().name()),ProductCategory.CLOTHING);
    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void deleteProduct() {
        this.productService.deleteProduct(1L);
        assertEquals(this.productService.retreiveAllProduct().size(), 1);
    }


}