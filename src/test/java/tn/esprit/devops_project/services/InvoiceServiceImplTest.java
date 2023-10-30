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
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class InvoiceServiceImplTest {

    @Autowired
    private InvoiceServiceImpl invoiceService;
    @Autowired
    private OperatorServiceImpl operatorService;

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveAllInvoices() {
        final List<Invoice> allInvoice = this.invoiceService.retrieveAllInvoices();
        assertEquals(allInvoice.size(), 2);
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void cancelInvoice() {
        this.invoiceService.cancelInvoice(1L);
        assertEquals(this.invoiceService.retrieveInvoice(1L).getArchived(), true);
        assertThrows(NullPointerException.class, ()-> {
            this.invoiceService.cancelInvoice(700L);
        });
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveInvoice() {
        final Invoice invoice = this.invoiceService.retrieveInvoice(2L);
        assertEquals(invoice.getAmountInvoice(), 2.2F);
        //case of ID not found
        assertThrows(NullPointerException.class, ()-> {
            this.invoiceService.retrieveInvoice(700L);
        });
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void getInvoicesBySupplier() {
        final List<Invoice> invoices = invoiceService.getInvoicesBySupplier(1L);
        assertEquals(invoices.size(),2);
        assertThrows(NullPointerException.class, ()-> {
            this.invoiceService.getInvoicesBySupplier(700L);
        });
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void assignOperatorToInvoice() {
        this.invoiceService.assignOperatorToInvoice(1L, 1L);
        assertEquals(operatorService.retrieveOperator(1L).getInvoices().size(),1);
        assertThrows(NullPointerException.class, ()-> {
            this.invoiceService.assignOperatorToInvoice(1L, 700L);
        });
        assertThrows(NullPointerException.class, ()-> {
            this.invoiceService.assignOperatorToInvoice(700L, 1L);
        });

    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void getTotalAmountInvoiceBetweenDates() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2020-01-01");
        Date endDate = dateFormat.parse("2025-01-01");
        //only calculates the amount where archived = true
        assertEquals(invoiceService.getTotalAmountInvoiceBetweenDates(startDate,endDate),1.1F);
    }
}