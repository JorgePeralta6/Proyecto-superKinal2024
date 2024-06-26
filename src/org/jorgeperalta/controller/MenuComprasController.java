/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jorgeperalta.controller;

import java.net.URL;
import java.sql.Blob;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jorgeperalta.dao.Conexion;
import org.jorgeperalta.dto.CompraDTO;
import org.jorgeperalta.model.Compra;
import org.jorgeperalta.system.Main;
import org.jorgeperalta.utils.SuperKinalAlert;
import org.jorgeperalta.model.DetalleCompra;
import org.jorgeperalta.model.Producto;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class MenuComprasController implements Initializable {
    private Main stage;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @FXML
    TableView tblCompras;
    @FXML
    TableColumn colCompraId, colFechaCompra, colTotalCompra, colProducto, colCantidadCompra;
    @FXML
    Button btnAgregar, btnEditar, btnRegresar, btnEliminar, btnBuscar;
    @FXML
    TextField tfCompraId;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
           stage.menuPrincipalView(); 
        }
        else if(event.getSource() == btnAgregar){
            stage.formComprasView(1);
        }
        else if(event.getSource() == btnEditar){
            CompraDTO.getCompraDTO().setCompra((Compra)tblCompras.getSelectionModel().getSelectedItem());
            stage.formComprasView(2);
        }
        else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(405).get() == ButtonType.OK){
                int comId = ((Compra)tblCompras.getSelectionModel().getSelectedItem()).getCompraId();
                eliminarCompra(comId);
                cargarDatos();
            }    
        }
        else if(event.getSource() == btnBuscar){
            tblCompras.getItems().clear();
            if(tfCompraId.getText().equals("")){
                cargarDatos();
            }else{
                tblCompras.getItems().add(buscarCompra());
                colCompraId.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("compraId"));
                colFechaCompra.setCellValueFactory(new PropertyValueFactory<DetalleCompra, String>("fechaCompra"));
                colTotalCompra.setCellValueFactory(new PropertyValueFactory<DetalleCompra, String>("totalCompra"));
                colProducto.setCellValueFactory(new PropertyValueFactory<DetalleCompra, String>("Producto"));
                colCantidadCompra.setCellValueFactory(new PropertyValueFactory<DetalleCompra, String>("cantidadCompra"));
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }  
    
    public void cargarDatos(){
        tblCompras.setItems(listarCompras());
        colCompraId.setCellValueFactory(new PropertyValueFactory<Compra, Integer>("compraId"));
        colFechaCompra.setCellValueFactory(new PropertyValueFactory<Compra, String>("fechaCompra"));
        colTotalCompra.setCellValueFactory(new PropertyValueFactory<Compra, String>("totalCompra"));
        colProducto.setCellValueFactory(new PropertyValueFactory<DetalleCompra, String>("Producto"));
        colCantidadCompra.setCellValueFactory(new PropertyValueFactory<DetalleCompra, String>("cantidadCompra"));
    }
    
    public ObservableList<DetalleCompra> listarCompras(){
            ArrayList<DetalleCompra> detalleCompra = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarDetalleCompra()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int compraId = resultSet.getInt("compraId");
                String fecha = resultSet.getString("fechaCompra");
                double total = resultSet.getDouble("totalCompra");
                int cantidadCompra = resultSet.getInt("cantidadCompra");
                String producto = resultSet.getString("Producto");
                
                detalleCompra.add(new DetalleCompra(cantidadCompra, producto, compraId, fecha, total));
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
        return FXCollections.observableList(detalleCompra);
    }
     
    public void eliminarCompra(int comId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarCompra(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, comId);
            statement.execute();
        }catch(SQLException e){
            SuperKinalAlert.getInstance().mostrarAlertaInfo(500);
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
        
    public DetalleCompra buscarCompra(){
        DetalleCompra detalleCompra = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarDetalleCompra(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfCompraId.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int compraId = resultSet.getInt("compraId");
                String fecha = resultSet.getString("fechaCompra");
                double total = resultSet.getDouble("totalCompra");
                int cantidad = resultSet.getInt("cantidadCompra");
                String producto = resultSet.getString("Producto");
                
                detalleCompra = (new DetalleCompra(cantidad, producto, compraId, fecha, total));
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
        return detalleCompra;
    }  
    
     public ObservableList<Producto> listarProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarProducto()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int Id = resultSet.getInt("productoId");
                String nombre = resultSet.getString("nombreProducto");
                String descripcion = resultSet.getString("descripcionProducto");
                int stock = resultSet.getInt("cantidadStock");
                double precioU = resultSet.getDouble("precioVentaUnitario");
                double precioM = resultSet.getDouble("precioVentaMayor");
                double precioC = resultSet.getDouble("precioCompra");
                Blob imagen = resultSet.getBlob("imagenProducto");
                String distribuidor = resultSet.getString("Distribuidor");
                String categoria = resultSet.getString("Categoria");

                productos.add(new Producto(Id, nombre, descripcion, stock, precioU, precioM, precioC, imagen, distribuidor, categoria));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return FXCollections.observableList(productos);
    }
    
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }  
    
}
