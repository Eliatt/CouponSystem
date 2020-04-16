package tests;

import entities.*;
import exceptions.CouponException;
import exceptions.ExistException;
import exceptions.LoginException;
import exceptions.NotExistException;
import facades.AdminFacade;
import facades.ClientFacade;
import facades.CompanyFacade;
import facades.CustomerFacade;
import pool.ConnectionPool;
import tasks.CleaningTask;
import utils.LoginManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Test {
    private Thread cleaningTask;
    private ConnectionPool pool = ConnectionPool.getInstance();
    private LoginManager loginManager = LoginManager.getInstance();
    private AdminFacade adminFacade;
    private ClientFacade clientFacade;
    private CompanyFacade companyFacade;
    private CustomerFacade customerFacade;


    // Running a thread, aimed to remove expired coupons once a day.

    public Test() {
        cleaningTask = new Thread(new CleaningTask());
        cleaningTask.setDaemon(true);
        cleaningTask.start();
    }


    public void testAll() throws LoginException, ExistException {

        // Managing login and access permissions.

//        For testing purposes please use:
//        Admin Email = admin@admin, Admin Password = admin
//        Company Email = alpha@amps, Company Password = Tubes4More
//        Customer Email = alpha@amps, Customer Password = Tubes4More


        Scanner input = new Scanner(System.in);
        String action = "";
        String login = "L";
        String exit = "X";
        String back = "Z";
        while (!action.equals(exit)) {
            System.out.println("Choose: L(login, X(abort)or Z to go back.");
            action = input.nextLine();
            if (action.equals(login)) {
                while (!action.equals(back) && !action.equals(exit)) {
                    System.out.println("Login as? Please choose: \nADMIN,\n" +
                            "    COMPANY,\n" +
                            "    CUSTOMER");
                    String userType = input.nextLine();
                    System.out.println("Enter  email");
                    String userEmail = input.nextLine();
                    System.out.println("Enter  password");
                    String userPassword = input.nextLine();
                    userType = userType.toUpperCase().trim();
                    try {
                        ClientType user = ClientType.valueOf(userType);
                        clientFacade = loginManager.login(userEmail, userPassword, user);
                    } catch (Exception e) {
                    }
                    if (clientFacade instanceof AdminFacade) {
                        adminFacade = (AdminFacade) clientFacade;
                        adminTasks();
                        adminFacade = null;
                    } else if (clientFacade instanceof CustomerFacade) {
                        customerFacade = (CustomerFacade) clientFacade;
                        customerTasks();
                        customerFacade = null;
                    } else if (clientFacade instanceof CompanyFacade) {
                        companyFacade = (CompanyFacade) clientFacade;
                        companyTasks();
                        companyFacade = null;
                    }
                    action = back;
                }

            }
        }

        abort();
    }

    public void abort() {
        cleaningTask.interrupt();
        pool.closeConnection();
    }


//      Allocating appropriate tasks for appropriate users: Administrator.
//      For testing purposes please use:
//      Admin Email = admin@admin , Admin Password = admin


    public void adminTasks() throws LoginException, ExistException {
        System.out.println("Logged as Admin");

        //Insert test values
        Company testCompany = new Company("ExampleCompName", "ExampleComp@mail", "ExampleComp", new ArrayList<Coupon>());
        try {
            adminFacade.addCompany(testCompany);
        } catch (ExistException e) {
            System.out.println(e.getMessage());
        }
        //Insert test values
        Customer testCustomer = new Customer("ExampleCustFirstName", "ExampleCustLastName", "ExampleCust@mail", "ExampleCust");
        try {
            adminFacade.addCustomer(testCustomer);
        } catch (
                ExistException e) {
            System.out.println(e.getMessage());
        }
        try {
            adminFacade.getAllCompanies().forEach(System.out::println);
        } catch (LoginException e) {
            e.printStackTrace();
        }
        try {
            adminFacade.getAllCustomers().forEach(System.out::println);
        } catch (LoginException e) {
            e.printStackTrace();
        }
        try {
            Company company = adminFacade.getOneCompany(1L);
            if (company != null)
                System.out.println(company);
        } catch (NotExistException e) {
            System.out.println(e.getMessage());
        }
        try {
            Customer customer = (adminFacade.getOneCustomer(testCustomer.getId()));
            System.out.println(customer);
        } catch (NotExistException e) {
            System.out.println(e.getMessage());
        }
        try {
            testCompany = adminFacade.getOneCompany(1L);
            if (testCompany != null) {
                testCompany.setPassword("password");
                adminFacade.updateCompany(testCompany);
            }
        } catch (NotExistException e) {
            e.printStackTrace();
        }

        try {
            Customer customer = adminFacade.getOneCustomer(1L);
            if (customer != null) {
                customer.setLastName("LastName");
                adminFacade.updateCustomer(customer);
            }
        } catch (NotExistException e) {
            e.printStackTrace();
        }

    }

    //     Allocating appropriate tasks for appropriate users: Company.
//     For testing purposes please use:
//     Company Email = ExampleComp@mail , Company Password = ExampleComp

    public void companyTasks() throws LoginException {
        System.out.println("Logged as Company");

        Coupon testCoupon = companyFacade.createCoupon(
                CouponType.LIFE_STYLE,
                "YUP!",
                "UPP....",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                10,
                500.00,
                "https://cdn.pixabay.com/photo/2019/07/14/11/07/motorcycle-4336847__340.jpg");

        try {
            companyFacade.addCoupon(testCoupon);

        } catch (ExistException e) {
            System.out.println(e.getMessage());
        }
        try {
            System.out.println(companyFacade.getCompanyCoupons(1L));
        } catch (LoginException e) {
            e.printStackTrace();
        }
        try {
            companyFacade.getCompanyCoupons(1L).forEach(System.out::println);
        } catch (LoginException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(companyFacade.getCompanyCouponsByCategory(CouponType.LIFE_STYLE));
        } catch (LoginException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(companyFacade.getCompanyCouponsByMaxPrice(1500D));
        } catch (LoginException e) {
            e.printStackTrace();
        }
        try{
        Coupon coupon = companyFacade.getCouponById(13L);
        if (coupon != null) {
            coupon.setAmount(1111);
            coupon.setPrice(1111);
            companyFacade.updateCoupon(coupon);
        }
        }  catch (LoginException e) {
        e.printStackTrace();
        }
        try {
            Coupon coupon = companyFacade.getCouponById(13L);
            if (coupon != null) {

                companyFacade.deleteCoupon(coupon.getId());
            }
        } catch (NotExistException e) {
            e.printStackTrace();
        }

    }


//     Allocating appropriate tasks for appropriate users: Customer.
//     For testing purposes please use:
//     Customer Email = ExampleCust@mail , Customer Password = ExampleCust

    public void customerTasks() {
        System.out.println("Logged as customer");


        Coupon coupon = customerFacade.couponDBDAO.getOneCoupon(9L);
        try {
            customerFacade.purchaseCoupon(coupon);
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (CouponException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        }


        try {
            System.out.println(customerFacade.getCustomerDetails());
        } catch (LoginException e) {
            e.printStackTrace();
        }


        try {
            customerFacade.getAllCustomerCoupons().forEach(System.out::println);
        } catch (LoginException e) {
            e.printStackTrace();
        }


        try {
            System.out.println(customerFacade.getCustomerCouponsByCategory(CouponType.LIFE_STYLE));
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        }


        try {
            System.out.println(customerFacade.getCustomerCouponsByPrice(1500.00D));
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        }
    }
    }







