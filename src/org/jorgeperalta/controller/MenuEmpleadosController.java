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
public class MenuEmpleadosController implements Initializable {
    private Main stage;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultset = null;
    
    @FXML
    TableView tblEmpleados;
    
    @FXML
    TableColumn colEmpleadoId, colNombreEmpleado, colApellidoEmpleado, colSueldo, colHoraEntrada, colHoraSalida, colCargo, colEncargado;
    
    @FXML
    TextField tfEmpleadosId;
    
    @FXML
    Button btnAgregar, btnEditar, btnEliminar, btnBuscar, btnRegresar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarLista();
    }    
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnAgregar){
            stage.formEmpleadosView(1);
        }else if(event.getSource() == btnEditar){
            EmpleadoDTO.getEmpleadoDTO().setEmpleado((Empleado)tblEmpleados.getSelectionModel().getSelectedItem());
            stage.formEmpleadosView(2);
        }else if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnEliminar){
            int empId = ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getEmpleadoId();
            eliminarEmpleados(empId);
            cargarLista();
        }else if (event.getSource() == btnBuscar){
            tblEmpleados.getItems().clear();
            if(tfEmpleadosId.getText().equals("")){
                cargarLista();
            }else{
                tblEmpleados.getItems().add(buscarEmpleado());
                colEmpleadoId.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("empleadoId"));
                colNombreEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombreEmpleado"));
                colApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidoEmpleado"));
                colSueldo.setCellValueFactory(new PropertyValueFactory<Empleado, String>("sueldo"));
                colHoraEntrada.setCellValueFactory(new PropertyValueFactory<Empleado, String>("horaEntrada"));
                colHoraSalida.setCellValueFactory(new PropertyValueFactory<Empleado, String>("horaSalida"));
                colCargo.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombreCargo"));
                colEncargado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("Encargado"));
            }
        }
    }
    
    public ObservableList<Empleado> listarEmpleados(){
        ArrayList<Empleado> empleados = new ArrayList<>();
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_ListarEmpleados()";
            statement = conexion.prepareStatement(sql);
            resultset = statement.executeQuery();
            
            while(resultset.next()){
                int empleadoId = resultset.getInt("empleadoId");
                String nombreEmpleado = resultset.getString("nombreEmpleado");
                String apellidoEmpleado = resultset.getString("apellidoEmpleado");
                double sueldo = resultset.getDouble("sueldo");
                String horaEntrada = resultset.getString("horaEntrada");
                String horaSalida = resultset.getString("horaSalida");
                String cargo = resultset.getString("nombreCargo");
                String encargado = resultset.getString("Encargado");

                empleados.add(new Empleado(empleadoId, nombreEmpleado, apellidoEmpleado, sueldo, horaEntrada, horaSalida, cargo, encargado));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(resultset != null){
                    resultset.close();
                }
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
        return FXCollections.observableList(empleados);
    }
    
    
    
    public void cargarLista(){
        tblEmpleados.setItems(listarEmpleados());
        colEmpleadoId.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("empleadoId"));
        colNombreEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombreEmpleado"));
        colApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidoEmpleado"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleado, Double>("sueldo"));
        colHoraEntrada.setCellValueFactory(new PropertyValueFactory<Empleado, String>("horaEntrada"));
        colHoraSalida.setCellValueFactory(new PropertyValueFactory<Empleado, String>("horaSalida"));
        colCargo.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombreCargo"));
        colEncargado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("Encargado"));

    }
    
    public void eliminarEmpleados(int empId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarEmpleados(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, empId);
            statement.execute();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            SuperKinalAlert.getInstance().mostrarAlertaInfo(500);
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
    
    public Empleado buscarEmpleado(){
        Empleado empleado = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_BuscarEmpleados(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1 ,Integer.parseInt(tfEmpleadosId.getText()));
            resultset = statement.executeQuery();
            
            if(resultset.next()){
                int empleadoId = resultset.getInt("empleadoId");
                String nombreEmpleado = resultset.getString("nombreEmpleado");
                String apellidoEmpleado = resultset.getString("apellidoEmpleado");
                double sueldo = resultset.getDouble("sueldo");
                String horaEntrada = resultset.getString("horaEntrada");
                String horaSalida = resultset.getString("horaSalida");
                String cargo = resultset.getString("nombreCargo");
                String encargado = resultset.getString("Encargado");
                
                empleado = (new Empleado(empleadoId, nombreEmpleado, apellidoEmpleado, sueldo, horaEntrada, horaSalida, cargo, encargado));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(resultset != null){
                    resultset.close();
                }
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
        
        return empleado;
    }
    
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
}