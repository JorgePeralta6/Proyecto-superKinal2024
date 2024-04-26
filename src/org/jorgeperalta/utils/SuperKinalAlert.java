/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jorgeperalta.utils;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Usuario
 */
public class SuperKinalAlert {
   
    private static SuperKinalAlert instance;
    
    private SuperKinalAlert(){
    }
    
    public static SuperKinalAlert getInstance(){
        if(instance == null){
            instance = new SuperKinalAlert();
        }
        return instance;
    }
    
    
    public void mostrarAlertaInfo(int code){
        if(code == 400){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos Pendientes");
            alert.setHeaderText("Cakpos Pendientes");
            alert.setContentText("Hay campos obligatorios que hay que llenar");
            alert.showAndWait();
        }else if(code == 401){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Campos Pendientes");
            alert.setHeaderText("Campos Pendientes");
            alert.setContentText("El registro se ha creado");
            alert.showAndWait();
        }
    }
    
    public Optional<ButtonType> mostrarAlertaConfirmacion(int code){
        Optional<ButtonType> action = null;
        if(code == 405){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminacion de registro");
            alert.setHeaderText("Eliminacion de registro");
            alert.setContentText("¿Desea eliminar el registro?");
            action = alert.showAndWait();
        }else if(code == 106){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Edision de registro");
            alert.setHeaderText("Edicion del registro");
            alert.setContentText("¿Desea editar este registro?");
            action = alert.showAndWait();
        }
        return action;
    }
}
