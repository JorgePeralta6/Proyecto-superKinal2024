/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jorgeperalta.model;

/**
 *
 * @author Usuario
 */
public class DetalleCompra extends Compra {
    private int detalleCompraId;
    private int cantidadCompra;
    private int productoId;
    private String producto;
    private int compraId;
    private String compra;

    public DetalleCompra() {
    }

    public DetalleCompra(int detalleCompraId, int cantidadCompra, int productoId, int compraId) {
        this.detalleCompraId = detalleCompraId;
        this.cantidadCompra = cantidadCompra;
        this.productoId = productoId;
        this.compraId = compraId;
    }

    public DetalleCompra(int cantidadCompra, String producto, int compraId, String fechaCompra, double totalCompra) {
        super(compraId, fechaCompra, totalCompra);
        this.cantidadCompra = cantidadCompra;
        this.producto = producto;
        this.compraId = compraId;
    }



    public int getDetalleCompraId() {
        return detalleCompraId;
    }

    public void setDetalleCompraId(int detalleCompraId) {
        this.detalleCompraId = detalleCompraId;
    }

    public int getCantidadCompra() {
        return cantidadCompra;
    }

    public void setCantidadCompra(int cantidadCompra) {
        this.cantidadCompra = cantidadCompra;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCompraId() {
        return compraId;
    }

    public void setCompraId(int compraId) {
        this.compraId = compraId;
    }

    public String getCompra() {
        return compra;
    }

    public void setCompra(String compra) {
        this.compra = compra;
    }

    @Override
    public String toString() {
        return "DetalleCompra{" + "detalleCompraId=" + detalleCompraId + ", cantidadCompra=" + cantidadCompra + ", productoId=" + productoId + ", producto=" + producto + ", compraId=" + compraId + ", compra=" + compra + '}';
    }
    
    
    
    
}
