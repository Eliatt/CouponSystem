package tasks;

import dbdao.CouponDBDAO;
import dbdao.CouponDistributionDBDAO;
import entities.Coupon;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class CleaningTask implements Runnable {

    private CouponDBDAO couponDBDAO;
    private CouponDistributionDBDAO couponDistributionDBDAO;
    private boolean stop = false;


    public CleaningTask() {

        this.couponDBDAO = new CouponDBDAO();
        this.couponDistributionDBDAO = new CouponDistributionDBDAO();

    }

    @Override
    public void run() {
        while (!stop) {

            try {
                for (Coupon coupon : couponDBDAO.getAllCoupons()) {
                    if (coupon.getEndDate().isBefore(LocalDate.now())) {
                        couponDistributionDBDAO.revokePurchaseByCouponId(coupon.getId());
                        couponDBDAO.deleteCoupon(coupon.getId());
                    }
                }
                TimeUnit.HOURS.sleep(24);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void stop() {
        stop = true;
    }
}

