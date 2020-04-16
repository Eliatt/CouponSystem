package facades;

import entities.Coupon;
import entities.CouponType;
import exceptions.CouponException;
import exceptions.LoginException;
import exceptions.NotExistException;
import org.junit.Test;

import java.util.Locale;

import static java.lang.System.out;
import static org.junit.Assert.*;

public class CustomerFacadeTest {

    private CustomerFacade customerFacade;

    @Test
    public void login() throws Exception {
        customerFacade = new CustomerFacade();
        out.println(customerFacade.login("ExampleCust@mail", "ExampleCust"));
    }

    @Test
    public void getCustomerDetails() throws Exception {
        customerFacade = new CustomerFacade();
        login();
        try {
            out.println(customerFacade.getCustomerDetails());
            customerFacade.getCustomerDetails().getCoupons().forEach(out::println);
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void purchaseCoupon() throws Exception {
        login();
        Coupon coupon = customerFacade.couponDBDAO.getOneCoupon(5L);
        try {
            customerFacade.purchaseCoupon(coupon);
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (CouponException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllCustomerCoupons() throws Exception {
        login();
        try {
            System.out.println(customerFacade.getAllCustomerCoupons());
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCustomerCouponsByCategory() throws Exception {
        login();
        try {
            System.out.println(customerFacade.getCustomerCouponsByCategory(CouponType.LIFE_STYLE));
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCustomerCouponsByPrice() throws Exception {
        login();
        try {
            System.out.println(customerFacade.getCustomerCouponsByPrice(1500.00D));
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        }
    }
}
