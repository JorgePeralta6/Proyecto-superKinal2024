/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jorgeperalta.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jorgeperalta.dao.Conexion;
import org.jorgeperalta.model.Cliente;
import org.jorgeperalta.model.Factura;
import org.jorgeperalta.model.TicketSoporte;
import org.jorgeperalta.system.Main;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class MenuTicketSoporteController implements Initializable {
    private Main stage;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @FXML
    ComboBox cmbEstatus, cmbClientes, cmbFacturas;
    @FXML
    Button btnRegresar, btnGuardar, btnVaciar;
    @FXML
    TableView tblTickets;
    @FXML
    TableColumn colTicketId, colDescripcion, colEstatus, colCliente, colFactura;
    @FXML
    TextArea taDescripcion;
    @FXML
    TextField tfTicketId;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        } else if(event.getSource() == btnGuardar){
            if(tfTicketId.getText().equals("")){
                agregarTicket();
                cargarDatos();
            }else{
                editarTicket();
                cargarDatos();
            }
        } else if(event.getSource() == btnVaciar){
            vaciarCampos();
        }
    }
    
    public void vaciarCampos(){
        tfTicketId.clear();
        taDescripcion.clear();
        cmbEstatus.getSelectionModel().select(0);
        cmbClientes.getSelectionModel().clearSelection();
        cmbFacturas.getSelectionModel().clearSelection();
    }
    
    public void cargarDatos(){
        tblTickets.setItems(listarTickets());
        colTicketId.setCellValueFactory(new PropertyValueFactory<TicketSoporte, Integer>("ticketSoporteId"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("descripcionTicket"));
        colEstatus.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("estatus"));
        colCliente.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("cliente"));
        colFactura.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("factura"));
        tblTickets.getSortOrder().add(colTicketId);
    }
    
    public void cargarDatosEditar(){
        TicketSoporte ts = (TicketSoporte)tblTickets.getSelectionModel().getSelectedItem();
        if(ts != null){
            tfTicketId.setText(Integer.toString(ts.getTicketSoporteId()));
            taDescripcion.setText(ts.getDescripcionTicket());
            cmbEstatus.getSelectionModel().select(0);
            cmbClientes.getSelectionModel().select(obtenerIndexCliente());
            cmbFacturas.getSelectionModel().select(obtenerIndexFactura());
        }
    }
    
    public int obtenerIndexCliente(){
        int index = 0;
        for(int i = 0 ; i < cmbClientes.getItems().size() ; i++){
            String clienteCmb = cmbClientes.getItems().get(i).toString();
            String clienteTbl = ((TicketSoporte)tblTickets.getSelectionModel().getSelectedItem()).getCliente();
            if(clienteCmb.equals(clienteTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
        public int obtenerIndexFactura(){
        int index = 0;
        for(int i = 0 ; i < cmbFacturas.getItems().size() ; i++){
            String facturaCmb = cmbFacturas.getItems().get(i).toString();
            String facturaTbl = ((TicketSoporte)tblTickets.getSelectionModel().getSelectedItem()).getFactura();
            if(facturaCmb.equals(facturaTbl)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    public void cargarCmbEstatus(){
        cmbEstatus.getItems().add("En proceso");
        cmbEstatus.getItems().add("Finalizado");
    }
    
    public ObservableList<TicketSoporte> listarTickets(){
        ArrayList<TicketSoporte> tickets = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarTicketSoporte()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int ticketSoporteId = resultSet.getInt("ticketSoporteId");
                String descripcion = resultSet.getString("descripcionTicket");
                String estatus = resultSet.getString("estatus");
                String cliente = resultSet.getString("cliente");
                String factura = resultSet.getString("factura");
                
                tickets.add(new TicketSoporte(ticketSoporteId, descripcion, estatus, cliente, factura));      
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(resultSet != null){
                    resultSet.close();
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
        
        return FXCollections.observableList(tickets);
    }
 
    public ObservableList<Cliente> listarClientes(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarClientes()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int clienteId = resultSet.getInt("clienteId");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String telefono = resultSet.getString("telefono");
                String direccion = resultSet.getString("direccion");
                String nit = resultSet.getString("nit");
                
                clientes.add(new Cliente(clienteId, nombre, apellido, telefono, direccion, nit));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(resultSet != null){
                    resultSet.close();
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
        
        return FXCollections.observableList(clientes);
    }
    
    public ObservableList<Factura> listarFacturas(){
        ArrayList<Factura> facturas = new ArrayList<>();
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_ListarFacturas()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int facturaId = resultSet.getInt("facturaId");
                Date fecha = resultSet.getDate("fecha");
                Time hora = resultSet.getTime("hora");
                Double total = resultSet.getDouble("total");
                String cliente = resultSet.getString("cliente");
                String empleado = resultSet.getString("empleado");


                
                facturas.add(new Factura(facturaId, fecha.toLocalDate(), hora.toLocalTime(),total, cliente, empleado));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(resultSet != null){
                    resultSet.close();
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
        
        return FXCollections.observableList(facturas);
    }
    
    public void agregarTicket(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarTicketSoporte(?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, taDescripcion.getText());
            statement.setInt(2, ((Cliente)cmbClientes.getSelectionModel().getSelectedItem()).getClienteId());
            statement.setInt(3, ((Factura)cmbFacturas.getSelectionModel().getSelectedItem()).getFacturaId());
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
    
    public void editarTicket(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarTicketSoporte(?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfTicketId.getText()));
            statement.setString(2, taDescripcion.getText());
            statement.setString(3, cmbEstatus.getSelectionModel().getSelectedItem().toString());
            statement.setInt(4, ((Cliente)cmbClientes.getSelectionModel().getSelectedItem()).getClienteId());
            statement.setInt(5, ((Factura)cmbFacturas.getSelectionModel().getSelectedItem()).getFacturaId());
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
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarCmbEstatus();
        cmbClientes.setItems(listarClientes());
        cmbFacturas.setItems(listarFacturas());
        cargarDatos();
    }    

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
}
