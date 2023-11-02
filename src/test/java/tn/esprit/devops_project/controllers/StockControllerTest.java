package tn.esprit.devops_project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tn.esprit.devops_project.entities.Stock;

import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    public void addStock() throws Exception {
        final Stock stock1 = new Stock(70, "new title", null);
        ObjectMapper objectMapper = new ObjectMapper();
        String stockJson = objectMapper.writeValueAsString(stock1);

        mockMvc.perform(MockMvcRequestBuilders.post("/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stockJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    public void retrieveStock() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/stock/11"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    public void retrieveAllStock() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/stock"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}