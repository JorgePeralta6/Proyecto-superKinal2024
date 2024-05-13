/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jorgeperalta.controller;

import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jorgeperalta.dao.Conexion;
import org.jorgeperalta.model.Producto;
import org.jorgeperalta.model.Promocion;
import org.jorgeperalta.system.Main;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class MenuPromocionesController implements Initializable {
    private Main stage;
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @FXML
    TableView tblPromociones;
    @FXML
    TableColumn colPromocionId, colPrecioPromocion,colDescripcionPromocion,colFechaInicio,colFechaFinalizado,colProductoId;
    @FXML
    TextField tfPromocionId,tfPrecio, tfFechaInicio,ftFechaFinalizo;
    @FXML
    ComboBox cmbProducto;
    @FXML
    Button btnGuardar, btnRegresar, btnVaciar;
    @FXML
    TextArea taDescripcion;
    
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnGuardar){
            if(tfPromocionId.getText().equals("")){
                agregarPromocion();
                cargarDatos();
            }else {
                editarPromocion();
                cargarDatos();
            }
        }else if(event.getSource() == btnVaciar){
            vaciarCampos();
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbProducto.setItems(listarProductos());
        cargarDatos();
        
    } 
    
    public void vaciarCampos(){
        tfPromocionId.clear();
        tfPrecio.clear();
        taDescripcion.clear();
        tfFechaInicio.clear();
        ftFechaFinalizo.clear();
        cmbProducto.getSelectionModel().clearSelection();
    }
    
    public void cargarDatos(){
        tblPromociones.setItems(listarPromociones());
        colPromocionId.setCellValueFactory(new PropertyValueFactory<Promocion, Integer>("promocionId"));
        colPrecioPromocion.setCellValueFactory(new PropertyValueFactory<Promocion, Double>("precioPromocion"));
        colDescripcionPromocion.setCellValueFactory(new PropertyValueFactory<Promocion, String>("descripcionPromocion"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<Promocion, Date>("fechaInicio"));
        colFechaFinalizado.setCellValueFactory(new PropertyValueFactory<Promocion, Date>("fechaFinalizacion"));
        colProductoId.setCellValueFactory(new PropertyValueFactory<Promocion, Integer>("productoId"));
        tblPromociones.getSortOrder().add(colPromocionId);
    }
    
    public int obtenerIndexProducto(){
        int index = 0;
        for(int i = 0; i >= cmbProducto.getItems().size(); i++){
            String cargoCmb = cmbProducto.getItems().get(i).toString();
            String cargoTbl = ((Promocion) tblPromociones.getSelectionModel().getSelectedItem()).getProducto();
            if(cargoCmb.equals(cargoTbl)){
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    public void cargarDatosEditar(){
        Promocion ts = (Promocion) tblPromociones.getSelectionModel().getSelectedItem();
        if(ts != null){
            tfPromocionId.setText(Integer.toString(ts.getPromocionId()));
            tfPrecio.setText(Double.toString(ts.getPrecioPromocion()));
            taDescripcion.setText(ts.getDescripcionPromocion());
            tfFechaInicio.setText(ts.getFechaInicio().toString());
            ftFechaFinalizo.setText(ts.getFechaFinalizacion().toString());
            cmbProducto.getSelectionModel().select(obtenerIndexProducto());
        }
    }
    
    public ObservableList<Promocion> listarPromociones(){
        ArrayList<Promocion> promociones = new ArrayList<>();
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarPromociones()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                int promocionId = resultSet.getInt("promocionId");
                double precioPromocion = resultSet.getDouble("precioPromocion");
                String descripcionPromocion = resultSet.getString("descripcionPromocion");
                Date fechaInicio = resultSet.getDate("fechaInicio");
                Date fechaFinalizo = resultSet.getDate("fechaFinalizacion");
                int productoId = resultSet.getInt("productoId");
                
                promociones.add(new Promocion(promocionId, precioPromocion, descripcionPromocion, fechaInicio, fechaFinalizo, productoId));
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
        return FXCollections.observableArrayList(promociones);
        
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
    
    public void agregarPromocion(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarPromociones(?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setDouble(1, Double.parseDouble(tfPrecio.getText()));
            statement.setString(2, taDescripcion.getText());
            statement.setDate(3, Date.valueOf(tfFechaInicio.getText()));
            statement.setDate(4, Date.valueOf(ftFechaFinalizo.getText()));
            statement.setInt(5, ((Producto) cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
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
    
    public void editarPromocion(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarPromociones(?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfPromocionId.getText()));
            statement.setDouble(2, Double.parseDouble(tfPrecio.getText()));
            statement.setString(3, taDescripcion.getText());
            statement.setDate(4, Date.valueOf(tfFechaInicio.getText()));
            statement.setDate(5, Date.valueOf(ftFechaFinalizo.getText()));
            statement.setInt(6, ((Producto) cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
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
   
    
}
