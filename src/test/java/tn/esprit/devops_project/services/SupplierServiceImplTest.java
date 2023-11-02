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
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.entities.Supplier;

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
class SupplierServiceImplTest {

    @Autowired
    private SupplierServiceImpl supplierService;

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void retrieveAllSuppliers() {
        final List<Supplier> allSuppliers = this.supplierService.retrieveAllSuppliers();
        assertEquals(allSuppliers.size(), 2);
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void addSupplier() {
        final Supplier supplier = new Supplier();
        supplier.setIdSupplier(33L);
        supplier.setCode("code3");
        supplier.setLabel("label3");
        this.supplierService.addSupplier(supplier);
        //assertEquals(this.supplierService.retrieveAllSuppliers().size(), 4);
        //assertNotNull(this.supplierService.retrieveSupplier(33L));
        assertEquals(this.supplierService.retrieveAllSuppliers().size(), 3);
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void updateSupplier() {
        Supplier supplier = supplierService.retrieveSupplier(1L);
        supplier.setCode("newCode");
        supplier.setLabel("newLabel");
        final Supplier modifiedSupplier = supplierService.updateSupplier(supplier);
        assertEquals(modifiedSupplier.getLabel(), "newLabel");
        assertEquals(modifiedSupplier.getCode(), "newCode");
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void deleteSupplier() {
        //case of ID not found
        assertThrows(EmptyResultDataAccessException.class, ()-> {
            this.supplierService.deleteSupplier(700L);
        });
        //make sure no entities are deleted
        assertEquals(this.supplierService.retrieveAllSuppliers().size(), 2);

        //case of valid ID
        this.supplierService.deleteSupplier(1L);
        assertEquals(this.supplierService.retrieveAllSuppliers().size(), 1);
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void retrieveSupplier() {
        final Supplier supplier = this.supplierService.retrieveSupplier(2L);
        assertEquals(supplier.getLabel(), "label2");
        //case of ID not found
        assertThrows(IllegalArgumentException.class, ()-> {
            this.supplierService.retrieveSupplier(700L);
        });
    }
}