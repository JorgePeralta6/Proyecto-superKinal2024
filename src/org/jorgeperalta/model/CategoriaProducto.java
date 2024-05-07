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
public class CategoriaProducto {
    private int categoriaProductosId;
    private String nombreCategoria;
    private String decripcionCategoria;

    public CategoriaProducto() {
    }

    public CategoriaProducto(int categoriaProductosId, String nombreCategoria, String decripcionCategoria) {
        this.categoriaProductosId = categoriaProductosId;
        this.nombreCategoria = nombreCategoria;
        this.decripcionCategoria = decripcionCategoria;
    }

    public int getCategoriaProductosId() {
        return categoriaProductosId;
    }

    public void setCategoriaProductosId(int categoriaProductosId) {
        this.categoriaProductosId = categoriaProductosId;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDecripcionCategoria() {
        return decripcionCategoria;
    }

    public void setDecripcionCategoria(String decripcionCategoria) {
        this.decripcionCategoria = decripcionCategoria;
    }

    @Override
    public String toString() {
        return "CategoriaProducto{" + "categoriaProductosId=" + categoriaProductosId + ", nombreCategoria=" + nombreCategoria + ", decripcionCategoria=" + decripcionCategoria + '}';
    }
    
    
}
