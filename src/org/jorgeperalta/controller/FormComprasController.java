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
import org.jorgeperalta.dto.CompraDTO;
import org.jorgeperalta.model.Compra;
import org.jorgeperalta.system.Main;
import org.jorgeperalta.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class FormComprasController implements Initializable {
    private Main stage;
    private int op;
    
    private static Connection conexion;
    private static PreparedStatement statement;
    
    @FXML
    TextField tfCompraId, tfFechaCompra, tfTotalCompra;
        
    @FXML
    Button btnGuardar, btnCancelar;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnCancelar){
            stage.menuComprasView();
        }else if(event.getSource() == btnGuardar){
            if(op == 1){
                 if(!tfFechaCompra.getText().equals("")){
                    agregarCompra();
                    stage.menuComprasView();
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                    tfFechaCompra.requestFocus();
                    return;
                }  
            }else if(op == 2){
                if(!tfFechaCompra.getText().equals("")){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(106).get() == ButtonType.OK){
                        editarCompra();
                        CompraDTO.getCompraDTO().setCompra(null);
                        stage.menuComprasView();
                    }
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                    tfFechaCompra.requestFocus();
                    return;
                }
                
            }

        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(CompraDTO.getCompraDTO().getCompra() != null){
            cargarDatos(CompraDTO.getCompraDTO().getCompra());
        }
    }
    
    public void agregarCompra(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarCompra(?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfFechaCompra.getText());
            statement.setString(2, tfTotalCompra.getText());
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
            }
        }
    }
    
    public void cargarDatos(Compra compra){
        tfCompraId.setText(Integer.toString(compra.getCompraId()));
        tfFechaCompra.setText(compra.getFechaCompra());
        tfTotalCompra.setText(Double.toString(compra.getTotalCompra()));
    }
    
    public void editarCompra(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCompra(?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfCompraId.getText()));
            statement.setString(2, tfFechaCompra.getText());
            statement.setString(3, tfTotalCompra.getText());
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
            }
        }
    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }

    public void setOp(int op) {
        this.op = op;
    }
    
}
