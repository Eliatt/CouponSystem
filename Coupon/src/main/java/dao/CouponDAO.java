package dao;

import entities.Coupon;
import exceptions.NotExistException;

import java.util.List;


public interface CouponDAO {
    void addCoupon(Coupon coupon);

    void updateCoupon(Coupon coupon);

    void deleteCoupon(Long couponId) throws NotExistException;

    List<Coupon> getAllCoupons();

    Coupon getOneCoupon(Long couponId);

}

