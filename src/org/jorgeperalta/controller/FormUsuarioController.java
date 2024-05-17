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
import javafx.fxml.Initializable;
import org.jorgeperalta.dao.Conexion;
import org.jorgeperalta.system.Main;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class FormUsuarioController implements Initializable {
    private Main stage;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
    }    

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    public void agregarUsuario(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
