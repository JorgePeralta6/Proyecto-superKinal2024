/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jorgeperalta.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jorgeperalta.dao.Conexion;
import org.jorgeperalta.dto.EmpleadoDTO;
import org.jorgeperalta.model.Empleado;
import org.jorgeperalta.system.Main;
import org.jorgeperalta.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class FormEmpleadosController implements Initializable {
    private Main stage;    
    private int op;
    private static Connection conexion;
    private static PreparedStatement statement;
    
    @FXML
    TextField tfEmpleadoId, tfNombreEmpleado, tfApellidoEmpleado, tfSueldo, tfHoraEntrada, tfHoraSalida, tfCargo, tfEncargado;
    
    @FXML
    Button btnGuardar, btnCancelar;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(EmpleadoDTO.getEmpleadoDTO().getEmpleado() != null){
            cargarDatos(EmpleadoDTO.getEmpleadoDTO().getEmpleado());
        }
        
    }    
    
    public void cargarDatos(Empleado empleado){
        tfEmpleadoId.setText(Integer.toString(empleado.getEmpleadoId()));
        tfNombreEmpleado.setText(empleado.getNombreEmpleado());
        tfApellidoEmpleado.setText(empleado.getApellidoEmpleado());
        tfSueldo.setText(Double.toString(empleado.getSueldo()));
        tfHoraEntrada.setText(empleado.getHoraEntrada());
        tfHoraSalida.setText(empleado.getHoraSalida());
        tfCargo.setText(empleado.getCargo());
        tfEncargado.setText(empleado.getEncargado());

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
    
    public void agregarEmpleado(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarEmpleados(?,?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfNombreEmpleado.getText());
            statement.setString(2, tfApellidoEmpleado.getText());
            statement.setString(3, tfSueldo.getText());
            statement.setString(4, tfHoraEntrada.getText());
            statement.setString(5, tfHoraSalida.getText());
            statement.setString(6, tfCargo.getText());
            statement.setString(7, tfEncargado.getText());

            statement.execute();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(conexion != null){
                    conexion.close();
                }else if(statement != null){
                    statement.close();
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }
    
    public void editarEmpleado(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarEmpleados(?,?,?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfEmpleadoId.getText()));
            statement.setString(2, tfNombreEmpleado.getText());
            statement.setString(3, tfApellidoEmpleado.getText());
            statement.setString(4, tfSueldo.getText());
            statement.setString(5, tfHoraEntrada.getText());
            statement.setString(6, tfHoraSalida.getText());
            statement.setString(7, tfCargo.getText());
            statement.setString(8, tfEncargado.getText());
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
            }
        }
    }
    
    
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnCancelar){
            stage.menuEmpleadosView();
        }else if(event.getSource() == btnGuardar){
            if(op == 1){
                agregarEmpleado();
                EmpleadoDTO.getEmpleadoDTO().setEmpleado(null);
                stage.menuEmpleadosView();
            }else if(op == 2){
                editarEmpleado();
                EmpleadoDTO.getEmpleadoDTO().setEmpleado(null);
                stage.menuEmpleadosView();
            }
            
            stage.menuEmpleadosView();
        }
      
    }
        
    
}
