package facades;

import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import dbdao.CustomerDBDAO;
import entities.Company;
import entities.Coupon;
import entities.Customer;
import exceptions.ExistException;
import exceptions.LoginException;
import exceptions.NotExistException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminFacade extends ClientFacade {

    private ArrayList<Company> deleteCompanies = null;
    private ArrayList<Customer> deleteCustomers = null;
    private CompanyDBDAO companyDBDAO = new CompanyDBDAO();
    private CouponDBDAO couponDBDAO = new CouponDBDAO();
    private CustomerDBDAO customerDBDAO = new CustomerDBDAO();

    private Boolean isLogin = false;

    @Override
    public boolean login(String email, String password) {
        String adminEmail = "admin@admin";
        String adminPassword = "admin";
        isLogin = (email.equals(adminEmail) && password.equals(adminPassword));
        return isLogin;
    }


    public void addCompany(Company company) throws ExistException, LoginException {
        if (!isLogin) {
            throw new LoginException("Please log in");
        }
        if (companyDBDAO.isCompanyNameExists(company.getName())) {
            throw new ExistException("Company name already in use");
        }
        if (companyDBDAO.isCompanyEmailExists(company.getEmail())) {
            throw new ExistException("Company email already in use");
        }
        companyDBDAO.addCompany(company);
    }

    public void updateCompany(Company company) throws NotExistException, LoginException {
        if (!isLogin) {
            throw new LoginException("Please log in");
        }
        Company companyById = companyDBDAO.getOneCompany(company.getId());
        if (companyById == null) {
            throw new NotExistException("No such company found");
        }
        companyById.setEmail(company.getEmail());
        companyById.setPassword(company.getPassword());
        companyDBDAO.updateCompany(companyById);
    }


    public void deleteCompany(Company company) throws LoginException, NotExistException {
        if (!isLogin) {
            throw new LoginException("Please log in");
        }
        if (!companyDBDAO.isCompanyExists(company.getEmail(), company.getPassword())) {
            throw new NotExistException("No such company found");
        }
        Company company1 = companyDBDAO.getByEmailAndPassword(company.getEmail(), company.getPassword());
        if (deleteCompanies == null) {
            deleteCompanies = new ArrayList<>();
        }
        if (company != null) {
            company.setCoupons(couponDBDAO.getCompanyCoupons(company.getId()));
            company.setCoupons(couponDBDAO.getCompanyCoupons(company.getId()));

            if (company != null) {
                if (company.getCoupons() != null) {
                    for (Coupon coupon : company.getCoupons()) {
                        couponDistributionDBDAO.revokePurchaseByCouponId(coupon.getId());
                        couponDBDAO.deleteCoupon(coupon.getId());
                    }
                    company.setCoupons(null);
                }
                companyDBDAO.deleteCompany(company.getId());
                if (deleteCompanies == null) {
                    deleteCompanies = new ArrayList<>();
                }
                deleteCompanies.add(company);
            }
        }

    }

    public Company getOneCompany(Long id) throws LoginException, NotExistException {
        if (!isLogin) {
            throw new LoginException("Please log in");
        }
        Company company = companyDBDAO.getOneCompany(id);
        if (company == null) {
            throw new NotExistException("No such company found");
        }
        company.setCoupons(couponDBDAO.getCompanyCoupons(id));
        return company;
    }

    public ArrayList<Company> getAllCompanies() throws LoginException {
        if (!isLogin) {
            throw new LoginException("Please log in");
        }
        return companyDBDAO.getAllCompanies();
    }

    public void addCustomer(Customer customer) throws LoginException, ExistException {
        if (!isLogin) {
            throw new LoginException("Please log in");
        }
        if (customerDBDAO.isCustomerEmailExist(customer.getEmail())) {
            throw new ExistException("Customer email already in use");
        }
        customerDBDAO.addCustomer(customer);
    }

    public void updateCustomer(Customer customer) throws LoginException, NotExistException, ExistException {
        if (!isLogin) {
            throw new LoginException("Please login");
        }
        Customer customerById = customerDBDAO.getOneCustomer(customer.getId());
        if (customerById == null) {
            throw new NotExistException("No such customer found");
        }
        customerById.setFirstName(customer.getFirstName());
        customerById.setLastName(customer.getLastName());
        customerById.setEmail(customer.getEmail());
        customerById.setPassword(customer.getPassword());
        customerDBDAO.updateCustomer(customerById);
    }


    public void deleteCustomer(Long customerId) throws LoginException, NotExistException, SQLException {
        if (!isLogin) {
            throw new LoginException("Please login");
        }
        Customer customer = customerDBDAO.getOneCustomer(customerId);
        if (customer != null) {
            couponDistributionDBDAO.revokePurchaseByCustomerId(customerId);
            customerDBDAO.deleteCustomer(customerId);
            if (deleteCustomers == null) {
                deleteCustomers = new ArrayList<>();
            }
            deleteCustomers.add(customer);
        } else {
            throw new NotExistException("No such customer found");
        }
    }

    public List<Customer> getAllCustomers() throws LoginException {
        if (!isLogin) {
            throw new LoginException("you need to login");
        }
        List<Customer> allCustomers = customerDBDAO.getAllCustomers();
        return customerDBDAO.getAllCustomers();
    }


    public void returnDeleteCustomer(Long id) throws LoginException, NotExistException, ExistException {
        if (!isLogin) {
            throw new LoginException("Please login");
        }
        if (deleteCustomers == null) {
            throw new NotExistException("No such customer found");
        }
        for (int i = 0; i < deleteCustomers.size(); i++) {
            if (id.equals(deleteCustomers.get(i).getId())) {
                Customer back = deleteCustomers.remove(i);
                back.setId(null);
                customerDBDAO.addCustomer(back);
                break;
            }
        }
    }


    public Customer getOneCustomer(Long customerId) throws LoginException, NotExistException {
        if (!isLogin) {
            throw new LoginException("Please login");
        }
        return customerDBDAO.getOneCustomer(customerId);
    }

        }




