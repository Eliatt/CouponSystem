package dao;

public interface CouponDistributionDAO {

    void addCouponPurchase(Long customerId,Long couponId);
    void deleteCouponPurchase(Long customerId,Long couponId);
    Boolean isPurchased(Long customerId,Long couponId);
}
