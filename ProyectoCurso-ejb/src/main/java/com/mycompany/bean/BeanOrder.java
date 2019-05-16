/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bean;

import com.mycompany.controller.OrdersJpaController;
import com.mycompany.entity.Customers;
import com.mycompany.entity.Orders;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author German
 */
@Stateless
public class BeanOrder implements BeanOrderLocal {

    @Override
    public void guardar() {
        try {
            OrdersJpaController jpa = new OrdersJpaController();
            Orders orders = new Orders();
            orders.setAmount(1.25D);
            Customers customers = new Customers();
            customers.setCustomerId(6);
            orders.setCustomerId(customers);
            jpa.create(orders);
        } catch (Exception ex) {
            Logger.getLogger(BeanOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void editar() {
        try {
            OrdersJpaController jpa = new OrdersJpaController();
            Orders orders = new Orders();
            orders.setAmount(100D);
            orders.setOrderId(6);
            Customers customers = new Customers();
            customers.setCustomerId(5);
            orders.setCustomerId(customers);
            jpa.edit(orders);
        } catch (Exception ex) {
            Logger.getLogger(BeanOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
