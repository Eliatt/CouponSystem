package facades;


import entities.Coupon;
import entities.CouponType;
import exceptions.ExistException;
import exceptions.LoginException;
import exceptions.NotExistException;
import org.junit.Test;

import java.time.LocalDate;

public class CompanyFacadeTest {
    private CompanyFacade companyFacade = new CompanyFacade();

    public CompanyFacadeTest() {

    }

    @Test
    public void login() {
        companyFacade = new CompanyFacade();
        System.out.println(companyFacade.login("ExampleComp@mail", "ExampleComp"));
    }

    @Test
    public void addCoupon() {
        login();
        try {
            Coupon coupon = companyFacade.createCoupon(
                    CouponType.FOOD,
                    "Cleaners",
                    "trying to clean it",


//                    LocalDate.of(2020,03,01),
//                    LocalDate.of(2020,03,05),

                    LocalDate.now(),
                    LocalDate.now().plusDays(30),

                    10,
                    500.00,
                    "https://cdn.pixabay.com/photo/2019/07/14/11/07/motorcycle-4336847__340.jpg");

            companyFacade.addCoupon(coupon);

        } catch (LoginException e) {
            e.printStackTrace();
        } catch (ExistException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateCoupon() throws LoginException, ExistException {
        login();
        Coupon coupon = companyFacade.couponDBDAO.getOneCoupon(54L);
        System.out.println(coupon);
        coupon.setAmount(18);
        coupon.setPrice(170.09);
        companyFacade.updateCoupon(coupon);

    }

    @Test
    public void deleteCoupon() {
        login();
        try {
            companyFacade.deleteCoupon(1L);
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCompanyCoupons() {
        login();
        try {
            companyFacade.getCompanyCoupons(1L).forEach(System.out::println);
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCompanyCouponsByType() {
        login();
        try {
            companyFacade.getCompanyCouponsByCategory(CouponType.LIFE_STYLE).forEach(System.out::println);
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCompanyCouponsByMaxPrice() {
        login();
        try {
            companyFacade.getCompanyCouponsByMaxPrice(1000D).forEach(System.out::println);
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
    }




