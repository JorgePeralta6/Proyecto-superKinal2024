/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jorgeperalta.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import org.jorgeperalta.dao.Conexion;
import org.jorgeperalta.model.CategoriaProducto;
import org.jorgeperalta.model.Distribuidor;
import org.jorgeperalta.model.Producto;
import org.jorgeperalta.system.Main;
import org.jorgeperalta.utils.SuperKinalAlert;

/**
 *
 * @author Usuario
 */
public class MenuProductosController implements Initializable {

    private Main stage;
    private File imageFile = null;
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;

    private List<File> files = null;

    @FXML
    Button btnRegresar, btnAgregar, btnEditar, btnListar, btnEliminar, btnBuscar, btnImagen;

    @FXML
    TableView tblProductos;

    @FXML
    TableColumn colProductoId, colNombreProducto, colDescripcion, colStock, colPrecioU, colPrecioM, colPrecioCompra, colDistribuidor, colCategoria;

    @FXML
    TextField tfProductoId, tfEmpleadoId, tfNombreProducto, tfDescripcion, tfStock, tfPrecioU, tfPrecioM, tfPrecioC;

    @FXML
    ComboBox cmbDistribuidor, cmbCategoria;

    @FXML
    ImageView imgCargar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbDistribuidor.setItems(listarDistribuidores());
        cmbCategoria.setItems(listarCategorias());
        cargarLista();
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        try {
            if (event.getSource() == btnRegresar) {
                stage.menuPrincipalView();
            } else if (event.getSource() == btnAgregar) {
                if (!tfNombreProducto.getText().isEmpty() && !tfDescripcion.getText().isEmpty() && !tfStock.getText().isEmpty() && !tfPrecioU.getText().isEmpty() && !tfPrecioM.getText().isEmpty() && !tfPrecioC.getText().isEmpty()
                        && cmbDistribuidor.getSelectionModel().getSelectedItem() != null && cmbCategoria.getSelectionModel().getSelectedItem() != null) {
                    agregarProducto();
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(401);
                    cargarLista();
                }
            } else if (event.getSource() == btnListar) {
                cargarLista();
            } else if (event.getSource() == btnEditar) {
                if (!tfNombreProducto.getText().isEmpty() && !tfDescripcion.getText().isEmpty() && !tfStock.getText().isEmpty() && !tfPrecioU.getText().isEmpty() && !tfPrecioM.getText().isEmpty() && !tfPrecioC.getText().isEmpty()
                        && cmbDistribuidor.getSelectionModel().getSelectedItem() != null && cmbCategoria.getSelectionModel().getSelectedItem() != null) {
                    editarProducto();
                    cargarLista();
                }
            } else if (event.getSource() == btnEliminar) {
                int proId = ((Producto) tblProductos.getSelectionModel().getSelectedItem()).getProductoId();
                eliminarProducto(proId);
                cargarLista();
            } else if (event.getSource() == btnBuscar) {
                tblProductos.getItems().clear();
                if (tfProductoId.getText().isEmpty()) {
                    cargarLista();
                } else {
                    tblProductos.getItems().add(buscarProducto());
                    colProductoId.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("productoId"));
                    colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
                    colDescripcion.setCellValueFactory(new PropertyValueFactory<Producto, String>("descripcionProducto"));
                    colStock.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidadStock"));
                    colPrecioU.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioVentaUnitario"));
                    colPrecioM.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioVentaMayor"));
                    colPrecioCompra.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioCompra"));
                    colDistribuidor.setCellValueFactory(new PropertyValueFactory<Producto, String>("Distribuidor"));
                    colCategoria.setCellValueFactory(new PropertyValueFactory<Producto, String>("Categoria"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int obtenerIndexDistribuidor(int id) {
        int index = 0;
        for (int i = 0; i < cmbDistribuidor.getItems().size(); i++) {
            int disCmb = ((Distribuidor) cmbDistribuidor.getItems().get(i)).getDistribuidorId();
            if (disCmb == id) {
                index = i;
                break;
            }
        }
        return index;
    }

    public int obtenerIndexCategoria(int id) {
        int index = 0;
        for (int i = 0; i < cmbCategoria.getItems().size(); i++) {
            int catCmb = ((CategoriaProducto) cmbCategoria.getItems().get(i)).getCategoriaProductosId();
            if (catCmb == id) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void cargarLista() {
        tblProductos.setItems(listarProductos());
        colProductoId.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("productoId"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Producto, String>("descripcionProducto"));
        colStock.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidadStock"));
        colPrecioU.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioVentaUnitario"));
        colPrecioM.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioVentaMayor"));
        colPrecioCompra.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioCompra"));
        colDistribuidor.setCellValueFactory(new PropertyValueFactory<Producto, String>("Distribuidor"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<Producto, String>("Categoria"));
    }

    public void cargarEditar() throws SQLException {
        Image imagen = null;
        Producto producto = (Producto) tblProductos.getSelectionModel().getSelectedItem();
        if (producto != null) {
            tfEmpleadoId.setText(Integer.toString(producto.getProductoId()));
            colNombreProducto.setText(producto.getNombreProducto());
            tfDescripcion.setText(producto.getDescripcionProducto());
            tfStock.setText(Integer.toString(producto.getCantidadStock()));
            tfPrecioU.setText(Double.toString(producto.getPrecioVentaUnitario()));
            tfPrecioM.setText(Double.toString(producto.getPrecioVentaMayor()));
            tfPrecioC.setText(Double.toString(producto.getPrecioCompra()));
            cmbDistribuidor.getSelectionModel().select(obtenerIndexDistribuidor(producto.getDistribuidorId()));
            cmbCategoria.getSelectionModel().select(obtenerIndexCategoria(producto.getCategoriaProductosId()));
            InputStream file = producto.getImagenProducto().getBinaryStream();
            imagen = new Image(file);
            imgCargar.setImage(imagen);
        }
    }

    public ObservableList<Producto> listarProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarProductos()";
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

    @FXML
    public void handleOnDrag(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    public void handleOnDrop(DragEvent event) {
        try {
            if (event.getDragboard().hasFiles()) {
                files = event.getDragboard().getFiles();
                if (!files.isEmpty()) {
                    File file = files.get(0);
                    FileInputStream fileInputStream = new FileInputStream(file);
                    imgCargar.setImage(new Image(fileInputStream));
                    imageFile = file;
                } else {
                   
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarProducto(int proId) {
        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarProducto(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, proId);
            statement.execute();

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
    }

    public Producto buscarProducto() {
        Producto producto = null;
        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_BuscarProducto(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfProductoId.getText()));
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
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
                producto = new Producto(Id, nombre, descripcion, stock, precioU, precioM, precioC, imagen, distribuidor, categoria);
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
        return producto;
    }

    public void agregarProducto() {
        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarProducto(?,?,?,?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfNombreProducto.getText());
            statement.setString(2, tfDescripcion.getText());
            statement.setInt(3, Integer.parseInt(tfStock.getText()));
            statement.setDouble(4, Double.parseDouble(tfPrecioU.getText()));
            statement.setDouble(5, Double.parseDouble(tfPrecioM.getText()));
            statement.setDouble(6, Double.parseDouble(tfPrecioC.getText()));
            if (imgCargar.getImage() != null) {
                statement.setBinaryStream(7, null);
            } else {
                InputStream img = new FileInputStream(files.get(0));
                statement.setBinaryStream(7, img);
            }
            statement.setInt(8, ((Distribuidor) cmbDistribuidor.getSelectionModel().getSelectedItem()).getDistribuidorId());
            statement.setInt(9, ((CategoriaProducto) cmbCategoria.getSelectionModel().getSelectedItem()).getCategoriaProductosId());
            statement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void editarProducto() {
        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "Call sp_editarProducto(?,?,?,?,?,?,?,?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfEmpleadoId.getText()));
            statement.setString(2, tfNombreProducto.getText());
            statement.setString(3, tfDescripcion.getText());
            statement.setInt(4, Integer.parseInt(tfStock.getText()));
            statement.setDouble(5, Double.parseDouble(tfPrecioU.getText()));
            statement.setDouble(6, Double.parseDouble(tfPrecioM.getText()));
            statement.setDouble(7, Double.parseDouble(tfPrecioC.getText()));
            if (imgCargar.getImage() != null) {
                statement.setBinaryStream(8, null);
            } else {
                InputStream img = new FileInputStream(imageFile);
                statement.setBinaryStream(8, img);
            }
            statement.setInt(9, ((Distribuidor) cmbDistribuidor.getSelectionModel().getSelectedItem()).getDistribuidorId());
            statement.setInt(10, ((CategoriaProducto) cmbCategoria.getSelectionModel().getSelectedItem()).getCategoriaProductosId());
            statement.execute();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<Distribuidor> listarDistribuidores() {
        ArrayList<Distribuidor> distribuidores = new ArrayList<>();
        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarDistribuidores()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int Id = resultSet.getInt("distribuidorId");
                String nombre = resultSet.getString("nombreDistribuidor");
                String direccion = resultSet.getString("direccionDistribuidor");
                String nit = resultSet.getString("nitDistribuidor");
                String telefono = resultSet.getString("telefonoDistribuidor");
                String web = resultSet.getString("web");

                distribuidores.add(new Distribuidor(Id, nombre, direccion, nit, telefono, web));
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
        return FXCollections.observableList(distribuidores);
    }

    public ObservableList<CategoriaProducto> listarCategorias() {
        ArrayList<CategoriaProducto> categorias = new ArrayList<>();
        try {
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarCategoriaProductos()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int catPId = resultSet.getInt("categoriaProductosId");
                String nombreCat = resultSet.getString("nombreCategoria");
                String desCat = resultSet.getString("descripcionCategoria");
                categorias.add(new CategoriaProducto(catPId, nombreCat, desCat));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conexion != null) {
                    conexion.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return FXCollections.observableList(categorias);

    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }

}
