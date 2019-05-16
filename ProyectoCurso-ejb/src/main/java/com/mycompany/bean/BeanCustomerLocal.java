/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bean;

import javax.ejb.Local;

/**
 *
 * @author German
 */
@Local
public interface BeanCustomerLocal {
    
    public void guardarClientesSinOrden();
    
    public void guardarCliente();
    
    public void editarClienteSinOrden();
    
    public void editarCliente();
    
    public void eliminarCliente();
    
    public void obtenerCliente();
}
