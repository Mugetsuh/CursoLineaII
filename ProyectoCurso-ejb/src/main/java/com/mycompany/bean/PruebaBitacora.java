/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bean;

import com.mycompany.controller.BitacoraJpaController;
import com.mycompany.controller.CustomersJpaController;
import com.mycompany.entity.Bitacora;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author German
 */
@Stateless
public class PruebaBitacora implements PruebaBitacoraLocal {

    @Override
    public void guardarBitacora(){
        try {
            Bitacora bitacora = new Bitacora(1, "bitacora de prueba 2", new Date(), 1.2D);
            BitacoraJpaController jpa = new BitacoraJpaController();
            jpa.create(bitacora);
        } catch (Exception ex) {
            Logger.getLogger(PruebaBitacora.class.getName()).log(Level.SEVERE, null, ex);
        }           
        
    }
    
    @Override
    public void editarBitacora(){
        try {
            //Bitacora bitacora = new Bitacora(5, "editar bitacora", new Date(), 1.2D);
            BitacoraJpaController jpa = new BitacoraJpaController();
            Bitacora bitacora  = jpa.getEntityManager().getReference(Bitacora.class, 1);
            bitacora.setDescripcion("editando bitacora numero 1");
            jpa.edit(bitacora);
        } catch (Exception ex) {
            Logger.getLogger(PruebaBitacora.class.getName()).log(Level.SEVERE, null, ex);
        }         
    }
    
    @Override
    public void eliminarBitacora() {
        try {
            //Bitacora bitacora = new Bitacora(5, "editar bitacora", new Date(), 1.2D);
            BitacoraJpaController jpa = new BitacoraJpaController();
            //bitacora  = jpa.getEntityManager().getReference(Bitacora.class, 1);
            jpa.destroy(10);
        } catch (Exception ex) {
            Logger.getLogger(PruebaBitacora.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    @Override
    public void buscarBitacora() {
        try {
            BitacoraJpaController jpa = new BitacoraJpaController();
            Bitacora bitacora = jpa.findBitacora(1);
            System.out.println("---------------" + bitacora.getDescripcion());
        } catch (Exception ex) {
            Logger.getLogger(PruebaBitacora.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }   
    
}
