/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bean;

import javax.ejb.Stateless;

/**
 *
 * @author German
 */
@Stateless
public class BeanPrueba implements BeanPruebaLocal {

    @Override
    public int sumar(){
        return 1+1;
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
