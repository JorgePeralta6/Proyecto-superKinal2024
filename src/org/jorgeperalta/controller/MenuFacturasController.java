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
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jorgeperalta.dao.Conexion;
import org.jorgeperalta.model.Cliente;
import org.jorgeperalta.model.DetalleCompra;
import org.jorgeperalta.model.Empleado;
import org.jorgeperalta.model.Factura;
import org.jorgeperalta.system.Main;
import org.jorgeperalta.model.DetalleFactura;
import org.jorgeperalta.model.Producto;
import org.jorgeperalta.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class MenuFacturasController implements Initializable {
    
    private Main stage;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultset = null;
    
    @FXML
    Button btnRegresar, btnGuardar, btnVaciar, btnEliminar, btnBuscar;
    
    @FXML
    TextField tfFacturaId, tfHora, tfTotal, tfFecha, tfFacturad;
    
    @FXML
    ComboBox cmbCliente, cmbEmpleado, cmbProducto;
    
    @FXML
    TableView tblFacturas;
    
    @FXML
    TableColumn colFacturaId, colCliente, colEmpleado, colFecha, colHora, colTotal, colProducto;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnGuardar){
            if(tfFacturaId.getText().equals("")){
                agregarFacturas();
                cargarDatos();
            }else{
                editarFacturas();
                cargarDatos();
            }
        }else if(event.getSource() == btnVaciar){
            vaciarCampos();
        }else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(405).get() == ButtonType.OK){
                int facId = ((Factura)tblFacturas.getSelectionModel().getSelectedItem()).getFacturaId();
                eliminarFactura(facId);
                cargarDatos();
            }    
        }else if(event.getSource() == btnBuscar){
            tblFacturas.getItems().clear();
            if(tfFacturad.getText().equals("")){
                cargarDatos();
            }else{
                tblFacturas.getItems().add(buscarCompra());
                colFacturaId.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("facturaId"));
                colFecha.setCellValueFactory(new PropertyValueFactory<DetalleFactura, LocalDate>("fecha"));
                colHora.setCellValueFactory(new PropertyValueFactory<DetalleFactura, LocalTime>("hora"));
                colCliente.setCellValueFactory(new PropertyValueFactory<DetalleFactura, String>("Cliente"));
                colProducto.setCellValueFactory(new PropertyValueFactory<DetalleFactura, String>("Producto"));
                colEmpleado.setCellValueFactory(new PropertyValueFactory<DetalleFactura, String>("Empleado"));
                colTotal.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Double>("total"));
            }
        }
    } 
    
    
    public void vaciarCampos(){
        tfFacturaId.clear();
        tfTotal.clear();
        cmbCliente.getSelectionModel().clearSelection();
        cmbEmpleado.getSelectionModel().clearSelection();
        cmbProducto.getSelectionModel().clearSelection();
        
    }
    
    public void cargarDatos(){
        tblFacturas.setItems(listarFacturas());
        colFacturaId.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("facturaId"));
        colFecha.setCellValueFactory(new PropertyValueFactory<DetalleFactura, LocalDate>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<DetalleFactura, LocalTime>("hora"));
        colCliente.setCellValueFactory(new PropertyValueFactory<DetalleFactura, String>("Cliente"));
        colProducto.setCellValueFactory(new PropertyValueFactory<DetalleFactura, String>("Producto"));
        colEmpleado.setCellValueFactory(new PropertyValueFactory<DetalleFactura, String>("Empleado"));
        colTotal.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Double>("total"));
        tblFacturas.getSortOrder().add(colFacturaId);
    }
    
    public void cargarDatosEditar(){
        Factura fa = (Factura)tblFacturas.getSelectionModel().getSelectedItem();
        if(fa != null){
            tfFacturaId.setText(Integer.toString(fa.getFacturaId()));
            cmbCliente.getSelectionModel().select(obtenerIndexCliente());
            cmbProducto.getSelectionModel().select(obtenerIndexProducto());
            cmbEmpleado.getSelectionModel().select(obtenerIndexEmpleado());
        }
    }
    
    public int obtenerIndexCliente(){
        int index = 0;
        for(int i = 0 ; i <= cmbCliente.getItems().size() ; i++){
            String clienteCmb = cmbCliente.getItems().get(i).toString();
            String clienteTbl = ((Factura)tblFacturas.getSelectionModel().getSelectedItems()).getCliente();
            if(clienteCmb.equals(clienteTbl)){
                index = i;
                break;
            }
            
        }
        return index;
    }
    
    public int obtenerIndexEmpleado(){
        int index = 0;
        for(int i = 0 ; i <= cmbEmpleado.getItems().size() ; i++){
            String empleadoCmb = cmbEmpleado.getItems().get(i).toString();
            String empleadoTbl = ((Factura)tblFacturas.getSelectionModel().getSelectedItems()).getEmpleado();
            if(empleadoCmb.equals(empleadoTbl)){
                index = i;
                break;
            }
            
        }
        return index;
    }
    
    public int obtenerIndexProducto(){
        int index = 0;
        for(int i = 0; i >= cmbProducto.getItems().size(); i++){
            String productoCmb = cmbProducto.getItems().get(i).toString();
            String productoTbl = ((DetalleFactura) tblFacturas.getSelectionModel().getSelectedItem()).getProducto();
            if(productoCmb.equals(productoTbl)){
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    public ObservableList<DetalleFactura> listarFacturas(){
        ArrayList<DetalleFactura> detalleFactura = new ArrayList<>();
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_ListarDetalleFactura()";
            statement = conexion.prepareStatement(sql);
            resultset = statement.executeQuery();
            
            while(resultset.next()){
                int facturaId = resultset.getInt("facturaId");
                Date fecha = resultset.getDate("fecha");
                Time hora = resultset.getTime("hora");
                String cliente = resultset.getString("Cliente");
                String producto = resultset.getString("Producto");
                String empleado = resultset.getString("Empleado");
                double total = resultset.getDouble("total");
                
                detalleFactura.add(new DetalleFactura(facturaId, fecha.toLocalDate(), hora.toLocalTime(),cliente, producto, empleado,total));
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
        
        return FXCollections.observableList(detalleFactura);
    }
    
    public ObservableList<Cliente> listarClientes(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarClientes()";
            statement = conexion.prepareStatement(sql);
            resultset = statement.executeQuery();
            
            while(resultset.next()){
                int clienteId = resultset.getInt("clienteId");
                String nombre = resultset.getString("nombre");
                String apellido = resultset.getString("apellido");
                String telefono = resultset.getString("telefono");
                String direccion = resultset.getString("direccion");
                String nit = resultset.getString("nit");
                
                clientes.add(new Cliente(clienteId, nombre, apellido, telefono, direccion, nit));
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
        return FXCollections.observableList(clientes);
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
     public ObservableList<Producto> listarProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarProducto()";
            statement = conexion.prepareStatement(sql);
            resultset = statement.executeQuery();

            while (resultset.next()) {
                int Id = resultset.getInt("productoId");
                String nombre = resultset.getString("nombreProducto");
                String descripcion = resultset.getString("descripcionProducto");
                int stock = resultset.getInt("cantidadStock");
                double precioU = resultset.getDouble("precioVentaUnitario");
                double precioM = resultset.getDouble("precioVentaMayor");
                double precioC = resultset.getDouble("precioCompra");
                Blob imagen = resultset.getBlob("imagenProducto");
                String distribuidor = resultset.getString("Distribuidor");
                String categoria = resultset.getString("Categoria");

                productos.add(new Producto(Id, nombre, descripcion, stock, precioU, precioM, precioC, imagen, distribuidor, categoria));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (resultset != null) {
                    resultset.close();
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
    
    public void agregarFacturas(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_AgregarFacturas(?, ?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(LocalDate.now()));
            statement.setTime(2, Time.valueOf(LocalTime.now()));
            statement.setInt(3, ((Cliente)cmbCliente.getSelectionModel().getSelectedItem()).getClienteId());
            statement.setInt(4, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
            statement.setInt(5, ((Empleado)cmbEmpleado.getSelectionModel().getSelectedItem()).getEmpleadoId());
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
    
    public void editarFacturas(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_EditarDetalleFactura(?, ?, ?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfFacturaId.getText()));
            statement.setDate(2, Date.valueOf(tfFecha.getText()));
            statement.setTime(3, Time.valueOf(tfHora.getText()));
            statement.setInt(4, ((Cliente)cmbCliente.getSelectionModel().getSelectedItem()).getClienteId());
            statement.setInt(5, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
            statement.setInt(6, ((Empleado)cmbEmpleado.getSelectionModel().getSelectedItem()).getEmpleadoId());
            statement.execute();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(conexion != null){
                conexion.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void eliminarFactura(int facId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_EliminarFacturas(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, facId);
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
     
    public DetalleFactura buscarCompra(){
        DetalleFactura detalleFactura = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarDetalleCompra(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfFacturad.getText()));
            resultset = statement.executeQuery();
            
            if(resultset.next()){
                int facturaId = resultset.getInt("facturaId");
                Date fecha = resultset.getDate("fecha");
                Time hora = resultset.getTime("hora");
                String cliente = resultset.getString("Cliente");
                String producto = resultset.getString("Producto");
                String empleado = resultset.getString("Empleado");
                double total = resultset.getDouble("total");
                
                detalleFactura = (new DetalleFactura(facturaId, fecha.toLocalDate(), hora.toLocalTime(),cliente, producto, empleado,total));
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
        return detalleFactura;
    }  
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbCliente.setItems(listarClientes());
        cmbEmpleado.setItems(listarEmpleados());
        cmbProducto.setItems(listarProductos());
        cargarDatos();
        tfFecha.setText(LocalDate.now().toString());
        tfHora.setText(LocalTime.now().toString());
    }  
    
    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    
}
