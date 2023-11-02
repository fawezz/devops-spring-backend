package tn.esprit.devops_project.services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tn.esprit.devops_project.entities.Stock;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class StockServiceImplTest {

    @Autowired
    private StockServiceImpl stockService;

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void addStock() {
        final Stock stock = new Stock();
        stock.setTitle("Title");
        stock.setProducts(emptySet());
        this.stockService.addStock(stock);
        assertEquals(this.stockService.retrieveAllStock().size(),2);
        //assertEquals(this.stockService.retrieveStock(29L).getTitle(),"Title");

        final Stock stock2 = new Stock(30L, "Title",emptySet());
        this.stockService.addStock(stock2);
        assertEquals(this.stockService.retrieveAllStock().size(),3);
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveStock() {
        final Stock stock = this.stockService.retrieveStock(11L);
        assertEquals("stock 1", stock.getTitle());
        assertEquals(stock.getProducts().size(), 0);
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveStock_NullOrNotFound() {
        assertThrows(NullPointerException.class, ()-> {
            this.stockService.retrieveStock(700L);
        });

        assertThrows(Exception.class, ()-> {
            this.stockService.retrieveStock(null);
        });
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveAllStock() {
        final List<Stock> allStocks = this.stockService.retrieveAllStock();
        assertEquals(allStocks.size(), 1);

    }


}