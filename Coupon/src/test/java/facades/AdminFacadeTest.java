package facades;


import entities.Company;
import entities.Coupon;
import entities.Customer;
import exceptions.ExistException;
import exceptions.LoginException;
import exceptions.NotExistException;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminFacadeTest {
    private AdminFacade adminFacade = new AdminFacade();

    public AdminFacadeTest() {
    }

    @Test
    public void login() {
        System.out.println(adminFacade.login("admin@admin", "admin"));
    }

    @Test
    public void addCompany() throws LoginException {
        System.out.println(adminFacade.login("admin@admin", "admin"));
        Company company = new Company(
                "test",
                "test@mail",
                "test", new ArrayList<Coupon>());
        try {
            adminFacade.addCompany(company);
        } catch (ExistException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void updateCompany() {
        adminFacade.login("admin@admin", "admin");
        Company company = new Company((long) 1, "AlphaVox", "alpha@amps.com", "Tubes4More");
        try {
            adminFacade.updateCompany(company);
        } catch (NotExistException e) {
            e.printStackTrace();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteCompany() {
        adminFacade.login("admin@admin", "admin");
        try {
            Company company = adminFacade.getOneCompany(1L);
            System.out.println(company);
            adminFacade.deleteCompany(company);
        } catch (LoginException e) {
            System.out.println(e.getMessage());
        } catch (NotExistException e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void getOneCompany() {
        adminFacade.login("admin@admin", "admin");
        try {
            Company company = adminFacade.getOneCompany(1L);

            System.out.println(company);
            company.getCoupons().forEach(System.out::println);

        } catch (LoginException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getAllCompanies() {
        adminFacade.login("admin@admin", "admin");
        try {
            List<Company> all = adminFacade.getAllCompanies();
            if (all != null) {
                all.forEach(System.out::println);
            }
        } catch (LoginException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void addCustomer() {
        adminFacade.login("admin@admin", "admin");

        Customer customer = new Customer("EX", "AMPLE", "example@customer.mail", "exampleCustomer");
        try {
            adminFacade.addCustomer(customer);
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (ExistException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getOneCustomer() {

        adminFacade.login("admin@admin", "admin");
        try {
            Customer customer = adminFacade.getOneCustomer(4L);
            System.out.println(customer);
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllCustomers() {
        adminFacade.login("admin@admin", "admin");
        try {
            List<Customer> allCustomers = adminFacade.getAllCustomers();
            if (allCustomers != null) {
                allCustomers.forEach(System.out::println);
            }
        } catch (LoginException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void deleteCustomer() {
        adminFacade.login("admin@admin", "admin");
        try {
            Customer customer = adminFacade.getOneCustomer(5L);
            System.out.println(customer + " successfully deleted");
            adminFacade.deleteCustomer(customer.getId());
        } catch (LoginException e) {
            System.out.println(e.getMessage());
        } catch (NotExistException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void updateCustomer() throws Exception {
        adminFacade.login("admin@admin", "admin");
        Customer customer = new Customer(
                1L,
                "Yehoram",
                "Ga'on",
                "yeh@ga.mail",
                "kazablan");
        adminFacade.updateCustomer(customer);
    }
}