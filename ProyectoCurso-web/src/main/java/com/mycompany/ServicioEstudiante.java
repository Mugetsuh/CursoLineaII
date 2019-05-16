/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany;

import com.mycompany.bean.BeanCustomerLocal;
import com.mycompany.bean.BeanOrderLocal;
import com.mycompany.bean.BeanPruebaLocal;
import com.mycompany.bean.PruebaBitacoraLocal;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author German
 */
@javax.enterprise.context.RequestScoped
@Path("estudiante")
public class ServicioEstudiante {
    
    @EJB
    BeanPruebaLocal beanPrueba;
    
    @EJB
    PruebaBitacoraLocal bitacora; 
    
    @EJB
    BeanCustomerLocal cliente;
    
    @EJB
    BeanOrderLocal orden;
    
    @GET
    @Path("/bitacora")
    @Produces(MediaType.APPLICATION_JSON)
    public Response bitacora() {
        int suma = beanPrueba.sumar();
        System.out.println(suma);
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/crearbitacora")
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarbitacora() {
        bitacora.guardarBitacora();
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/editarbitacora")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editarbitacora() {
        bitacora.editarBitacora();
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/eliminarbitacora")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarbitacora() {
        bitacora.eliminarBitacora();
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/buscarbitacora")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarbitacora() {
        bitacora.buscarBitacora();
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/crearclientesin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearclientesin() {
        cliente.guardarClientesSinOrden();
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/crearcliente")
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearcliente() {
        cliente.guardarCliente();
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/editarclientesin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editarclientesin() {
        cliente.editarClienteSinOrden();
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/editarcliente")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editarcliente() {
        cliente.editarCliente();
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/eliminarcliente")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarcliente() {
        cliente.eliminarCliente();
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/obtenercliente")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenercliente() {
        cliente.obtenerCliente();
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/guardarorden")
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarorden() {
        orden.guardar();
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/editarorden")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editarorden() {
        orden.editar();
        return Response.status(Response.Status.OK).build();
    }
}

