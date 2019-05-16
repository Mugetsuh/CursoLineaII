/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bean;

import com.mycompany.controller.BitacoraJpaController;
import com.mycompany.controller.CustomersJpaController;
import com.mycompany.controller.exceptions.RollbackFailureException;
import com.mycompany.entity.Bitacora;
import com.mycompany.entity.Customers;
import com.mycompany.entity.Orders;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author German
 */
@Stateless
public class BeanCustomer implements BeanCustomerLocal {

    @Override
    public void guardarClientesSinOrden() {
        try {
            CustomersJpaController jpa = new CustomersJpaController();
            Customers c = new Customers();
            c.setCustomerName("German Garcia");
            jpa.create(c);
        } catch (Exception ex) {
            Logger.getLogger(PruebaBitacora.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void guardarCliente() {
        try {
            CustomersJpaController jpa = new CustomersJpaController();
            Customers c = new Customers();
            c.setCustomerName("Maurico Gomez");
            Orders order1 = new Orders();
            order1.setAmount(1D);
            order1.setCustomerId(c);
            Orders order2 = new Orders();
            order2.setAmount(2D);
            order2.setCustomerId(c);
            List<Orders> listaOrders = new ArrayList<>();
            listaOrders.add(order1);
            listaOrders.add(order2);
            c.setOrdersList(listaOrders);
            jpa.create(c);
        } catch (Exception ex) {
            Logger.getLogger(PruebaBitacora.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void editarClienteSinOrden() {
        try {
            CustomersJpaController jpa = new CustomersJpaController();
            Customers c = new Customers();
            c.setCustomerName("No tiene nombre");
            c.setCustomerId(10);
            jpa.edit(c);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(BeanCustomer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BeanCustomer.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }
    
    @Override
    public void editarCliente(){
        try {
            //Bitacora bitacora = new Bitacora(5, "editar bitacora", new Date(), 1.2D);
            CustomersJpaController jpa = new CustomersJpaController();
            Customers customer = jpa.getEntityManager().getReference(Customers.class, 10);
            customer.setCustomerName("M Garcia G");
            customer.getOrdersList().get(0).setAmount(20D);
            customer.getOrdersList().get(1).setAmount(30D);
            jpa.edit(customer);
        } catch (Exception ex) {
            Logger.getLogger(PruebaBitacora.class.getName()).log(Level.SEVERE, null, ex);
        }         
    }
    
    @Override
    public void eliminarCliente() {
        try {
            CustomersJpaController jpa = new CustomersJpaController();
            jpa.destroy(10);
        } catch (Exception ex) {
            Logger.getLogger(PruebaBitacora.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    @Override
    public void obtenerCliente() {
        CustomersJpaController jpa = new CustomersJpaController();
        Customers c = jpa.findCustomers(6);
        System.out.println(c.getCustomerId() + " " + c.getCustomerName());
        for (Orders order : c.getOrdersList()) {
            System.out.println(order.getOrderId() + " " + order.getAmount());
        }
    }
}
