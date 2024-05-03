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
import org.jorgeperalta.dto.CargoDTO;
import org.jorgeperalta.model.Cargo;
import org.jorgeperalta.system.Main;
import org.jorgeperalta.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class FormCargosController implements Initializable {

private Main stage;
    private int op;
    
    private static Connection conexion;
    private static PreparedStatement statement;
    
    @FXML
    Button btnGuardar, btnCancelar;
    @FXML
    TextField tfCargoId, tfNombreCargo, tfDescripcionCargo;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(CargoDTO.getCargoDTO().getCargo() != null){
            cargarDatos(CargoDTO.getCargoDTO().getCargo());
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
            stage.menuCargosView();
        }else if(event.getSource() == btnGuardar){
            if(op == 1){
                if(!tfNombreCargo.getText().equals("") && !tfDescripcionCargo.getText().equals("")){
                    agregarCargo();
                    stage.menuCargosView();
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                    tfNombreCargo.requestFocus();
                    return;
                }
            }else if(op == 2){
                if(!tfNombreCargo.getText().equals("") && !tfDescripcionCargo.getText().equals("")){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(106).get() == ButtonType.OK){
                        editarCargo();
                        CargoDTO.getCargoDTO().setCargo(null);
                        stage.menuCargosView();
                    }
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                    tfNombreCargo.requestFocus();
                    return;
                }
 
            }
        }
    }
    
    public void cargarDatos(Cargo cargo){
        tfCargoId.setText(Integer.toString(cargo.getCargoId()));
        tfNombreCargo.setText(cargo.getNombreCargo());
        tfDescripcionCargo.setText(cargo.getDescripcionCargo());       
    }
    
    public void agregarCargo(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarCargo(?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfNombreCargo.getText());
            statement.setString(2, tfDescripcionCargo.getText());
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
    
    public void editarCargo(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCargo(?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfCargoId.getText()));
            statement.setString(2, tfNombreCargo.getText());
            statement.setString(3, tfDescripcionCargo.getText());
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
