/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jorgeperalta.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.jorgeperalta.dao.Conexion;
import org.jorgeperalta.dto.CategoriaProductoDTO;
import org.jorgeperalta.model.CategoriaProducto;
import org.jorgeperalta.system.Main;
import org.jorgeperalta.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class FormCategoriaProductosController implements Initializable {
    
    private Main stage;
    private int op;
    
    private static Connection conexion;
    private static PreparedStatement statement;
    
    @FXML
    Button btnGuardar, btnCancelar;
    @FXML
    TextField tfCategoriaProductoId, tfNombreCategoria, tfDescripcionCategoria;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(CategoriaProductoDTO.getCategoriaProductoDTO().getCategoriaProducto()!= null){
            cargarDatos(CategoriaProductoDTO.getCategoriaProductoDTO().getCategoriaProducto());
        }
    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnCancelar){
            stage.menuCategoriaProductosView();
        }else if(event.getSource() == btnGuardar){
            if(op == 1){
                if(!tfNombreCategoria.getText().equals("") && !tfDescripcionCategoria.getText().equals("")){
                    agregarCategoriaProducto();
                    stage.menuCategoriaProductosView();
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                    tfNombreCategoria.requestFocus();
                    return;
                }
            }else if(op == 2){
                if(!tfNombreCategoria.getText().equals("") && !tfDescripcionCategoria.getText().equals("")){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(106).get() == ButtonType.OK){
                        editarCategoriaProducto();
                        CategoriaProductoDTO.getCategoriaProductoDTO().setCategoriaProducto(null);
                        stage.menuCategoriaProductosView();
                    }
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                    tfNombreCategoria.requestFocus();
                    return;
                }
 
            }
        }
    }
    
    public void cargarDatos(CategoriaProducto categoriaProducto){
        tfCategoriaProductoId.setText(Integer.toString(categoriaProducto.getCategoriaProductosId()));
        tfNombreCategoria.setText(categoriaProducto.getNombreCategoria());
        tfDescripcionCategoria.setText(categoriaProducto.getDescripcionCategoria());       
    }
    
    public void agregarCategoriaProducto(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarCategoriaProducto(?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfNombreCategoria.getText());
            statement.setString(2, tfDescripcionCategoria.getText());
            statement.execute();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(statement != null){
                    statement.close();
                }
                if(statement != null){
                    conexion.close();
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
    
    public void editarCategoriaProducto(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCategoriaProducto(?,?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfCategoriaProductoId.getText()));
            statement.setString(2, tfNombreCategoria.getText());
            statement.setString(3, tfDescripcionCategoria.getText());
            statement.execute();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(statement != null){
                    statement.close();
                }
                if(conexion != null){
                    conexion.close();
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }finally{
            
            }
        }
    }
    
    public void setOp(int op) {
        this.op = op;
    }
    
}
