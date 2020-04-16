package facades;

import dbdao.CouponTypeDBDAO;
import entities.Coupon;
import entities.CouponType;
import entities.Customer;
import exceptions.CouponException;
import exceptions.LoginException;
import exceptions.NotExistException;

import java.time.LocalDate;
import java.util.List;


public class CustomerFacade extends ClientFacade {

    private CouponTypeDBDAO couponTypeDBDAO = new CouponTypeDBDAO();
    private Long clientId = null;

    @Override
    public boolean login(String email, String password) throws Exception {
        if (customerDBDAO.isCustomerExists(email, password)) {
            clientId = customerDBDAO.getOneCustomer(email, password).getId();
            return true;
        } else {
            clientId = null;
            return false;
        }
    }

    public Customer getCustomerDetails() throws LoginException {
        if (clientId == null) {
            throw new LoginException("Please login");
        }
        Customer customer = customerDBDAO.getOneCustomer(clientId);
        if (customer != null) {
            customer.setCoupons(couponDistributionDBDAO.getPurchaseByCustomerId(clientId));
        }
        return customer;
    }


    public void purchaseCoupon(Coupon coupon) throws LoginException, CouponException, NotExistException {
        if (clientId == null) {
            throw new LoginException("you need to login");
        }
        if (coupon.getAmount() <= 0 ||
                (LocalDate.now().isBefore(coupon.getStartDate()) &&
                        LocalDate.now().isAfter(coupon.getEndDate()))) {
            throw new NotExistException("Coupon not found");
        }
        if (couponDistributionDBDAO.isPurchased(clientId, coupon.getId())) {
            throw new CouponException("Coupon have been purchased already");
        }
        couponDistributionDBDAO.addCouponPurchase(clientId, coupon.getId());
        coupon.setAmount(coupon.getAmount() - 1);
        couponDBDAO.updateCoupon(coupon);
    }

    public List<Coupon> getAllCustomerCoupons() throws LoginException {
        if (clientId == null) {
            throw new LoginException("Please login");
        }
        List<Coupon> all = couponDistributionDBDAO.getPurchaseByCustomerId(clientId);
        return all;
    }

    public List<Coupon> getCustomerCouponsByCategory(CouponType category) throws LoginException, NotExistException {
        if (clientId == null) {
            throw new LoginException("Please login");
        }
        Long categoryId = couponTypeDBDAO.getIdCategory(category);
        if (categoryId == null) {
            throw new NotExistException("Choose coupon type");
        }

        List<Coupon> couponsType = couponDistributionDBDAO.getCustomerCouponsByCategory(categoryId, clientId);

        return couponsType;
    }

    public List<Coupon> getCustomerCouponsByPrice(Double maxPrice) throws LoginException, NotExistException {
        if (clientId == null) {
            throw new LoginException("Please login");
        }
        if (maxPrice == null || maxPrice<=0D) {
            throw new NotExistException("Please enter a value");
        }
        return couponDistributionDBDAO.getCustomerCouponsByPrice(maxPrice,clientId);
    }


}







