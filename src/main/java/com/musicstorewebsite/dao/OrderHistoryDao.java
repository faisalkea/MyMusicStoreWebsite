package com.musicstorewebsite.dao;

import com.musicstorewebsite.model.Cart;

/**
 * Created by faisaljarkass on 1/24/2016.
 */
public interface OrderHistoryDao {

    void addOrderHistoryByCart(Cart cart);
}
