package tn.esprit.devops_project.services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.entities.Supplier;

import java.util.List;

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
class OperatorServiceImplTest {

    @Autowired
    private OperatorServiceImpl operatorService;

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveAllOperators() {
        final List<Operator> allOperators = this.operatorService.retrieveAllOperators();
        assertEquals(allOperators.size(), 2);
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void addOperator() {
        final Operator operator = new Operator(5L,"new fname","new lname","new password",emptySet());
        operator.setIdOperateur(3L);
        this.operatorService.addOperator(operator);
        //assertNotNull(this.operatorService.retrieveOperator(3L));
        assertEquals(this.operatorService.retrieveAllOperators().size(), 3);
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void deleteOperator() {
        this.operatorService.deleteOperator(1L);
        assertEquals(this.operatorService.retrieveAllOperators().size(), 1);
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void updateOperator() {
        Operator operator = operatorService.retrieveOperator(1L);
        operator.setLname("new lname");
        operator.setFname("new fname");
        operator.setPassword("new password");
        operator.setInvoices(emptySet());
        final Operator modifiedOperator = operatorService.updateOperator(operator);
        assertEquals(modifiedOperator.getFname(), "new fname");
        assertEquals(modifiedOperator.getLname(), "new lname");
        assertEquals(modifiedOperator.getPassword(), "new password");
        assertEquals(modifiedOperator.getInvoices().size(), 0);
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveOperator() {
        final Operator operator = this.operatorService.retrieveOperator(2L);
        assertEquals(operator.getFname(), "fname2");
        //case of ID not found
        assertThrows(NullPointerException.class, ()-> {
            this.operatorService.retrieveOperator(700L);
        });
    }
}