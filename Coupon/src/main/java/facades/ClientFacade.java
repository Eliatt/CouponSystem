package facades;


import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import dbdao.CouponDistributionDBDAO;
import dbdao.CustomerDBDAO;

public abstract class ClientFacade {
    protected CompanyDBDAO companyDBDAO;
    protected CustomerDBDAO customerDBDAO;
    public CouponDBDAO couponDBDAO;
    protected CouponDistributionDBDAO couponDistributionDBDAO;

    public ClientFacade() {
        this.companyDBDAO = new CompanyDBDAO();
        this.customerDBDAO = new CustomerDBDAO();
        this.couponDBDAO = new CouponDBDAO();
        this.couponDistributionDBDAO = new CouponDistributionDBDAO();
    }

    public abstract boolean login(String email, String password) throws Exception;

}




