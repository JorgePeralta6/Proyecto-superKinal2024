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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.jorgeperalta.dao.Conexion;
import org.jorgeperalta.system.Main;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class LoginController implements Initializable {
    private Main stage;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @FXML
    TextField tfUsuario;
    @FXML
    PasswordField tfPassword;
    @FXML
    Button btnIniciar, btnRegistrar;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        Usuario usuario = buscar
        
        if(event.getSource() == btnIniciar){
        
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }    

    public void setStage(Main stage) {
        this.stage = stage;
    }
      
    public void buscarUsuario(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarUsuario(?)";
            statement = conexion.prepareStatement(sql); 
            statement.setString(1, tfUsuario.getText());
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
            
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
