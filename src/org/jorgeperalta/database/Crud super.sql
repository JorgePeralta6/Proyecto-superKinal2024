use superKinalIN5CV;

-- Clientes
-- Agregar
DELIMITER $$
create procedure sp_agregarClientes(nom varchar(30), ape varchar(30), tel varchar(15), dir varchar(150), ni varchar(15))
BEGIN	
	insert into Clientes(nombre, apellido, telefono, direccion, nit) values
		(nom, ape, tel, dir, ni);
END $$
DELIMITER ;
call sp_agregarClientes('Juan','Ramires','1111-1111','Huehuetenango', 'CF');

-- Listar
DELIMITER $$
create procedure sp_listarClientes()
BEGIN
	select *from Clientes;
END$$
DELIMITER ;

call sp_listarClientes();
-- Eliminar
DELIMITER $$
create procedure sp_eliminarClientes(in cliId int)
BEGIN
	delete
		from Clientes
		where clienteId = cliId;
END $$
DELIMITER ;
call sp_eliminarClientes(6);

-- Buscar
DELIMITER $$
CREATE PROCEDURE sp_buscarClientes (in cliId int)
BEGIN
	select * from Clientes
		where clienteId = cliId;
END$$
DELIMITER ;
call sp_buscarClientes(2);
-- Editar
DELIMITER $$
create procedure sp_editarClientes (in cliId int, in nom varchar(30),in ape varchar(30),in tel varchar(15), in dir varchar(150), ni varchar(15))
BEGIN
	update Clientes
		set
			nombre = nom,
			apellido = ape,
			telefono = tel,
			direccion = dir,
            nit = ni
			where clienteId = cliId;
END $$
DELIMITER ;
CALL sp_editarClientes (3, 'Raul', 'Marquez', '3333-3333', 'Suchitepequez', '23478908');
-- Clientes

-- Cargos
DELIMITER $$
create procedure sp_agregarCargo(nomCar varchar(30), desCar varchar(100))
BEGIN
	insert into Cargos(nombreCargo, descripcionCargo) values
		(nomCar, desCar);
END $$
DELIMITER ;

call sp_agregarCargo('Supervisor', 'Supervisa la actividad de los empeleados');

DELIMITER $$
create procedure sp_listarCargo()
BEGIN
	select
		Cargos.cargoId,
		Cargos.nombreCargo,
		Cargos.descripcionCargo
		from Cargos;
END $$
DELIMITER ;
call sp_listarCargo();

DELIMITER $$
create procedure sp_eliminarCargo(carId INT)
BEGIN
	delete
		from Cargos
		where cargoId = carId;
END $$
DELIMITER ;

DELIMITER $$
create procedure sp_buscarCargo(carId INT)
BEGIN
	select
		Cargos.cargoId,
		Cargos.nombreCargo,
		Cargos.descripcionCargo
		from Cargos
		where cargoId = carId;
END $$
DELIMITER ;

DELIMITER $$
create procedure sp_editarCargo(carId INT, nomCar varchar(30), desCar varchar(100))
BEGIN
	update Cargos
		set
			nombreCargo = nomCar,
			descripcionCargo = desCar
			where cargoId = carId;
END $$
DELIMITER ;
-- Cargos

-- Compras
DELIMITER $$
create procedure sp_agregarCompra(in fecCom date, in totCom decimal (10,2))
	BEGIN 
		insert into Compras (fechaCompra, totalCompra) values
			(fecCom, totCom);
    END $$
DELIMITER ;

DELIMITER $$
create procedure sp_listarCompra()
	BEGIN
		select * from Compras;
    END $$
DELIMITER ;

DELIMITER $$
create procedure sp_buscarCompra(in comId int)
	BEGIN	
		select * from Compras 
			where compraId = comId;
    END $$
DELIMITER ;

DELIMITER $$
create procedure sp_eliminarCompra(in comId int)
	BEGIN 
		delete from Compras
        where compraId = comId;
    END $$
DELIMITER ;

DELIMITER $$
create procedure sp_editarCompra(in comId int,in fecCom date,in totCom decimal (10,2))
	BEGIN 
		update Compras
			set 
				fechaCompra = fecCom,
                totalCompra = totCom
                where compraId = comId;
    END $$
DELIMITER ;
-- Compras

-- CategoriaProductos
DELIMITER $$
create procedure sp_agregarCategoriaProducto(in nombC varchar(30), descC varchar(100))
	BEGIN
		insert into CategoriaProductos(nombreCategoria, descripcionCategoria) values
			(nombC, descC);
    END $$
DELIMITER ;
call sp_agregarCategoriaProducto('Aventuras en la Naturaleza','Una gran aventura adentrandoze a la naturaleza');

DELIMITER $$
create procedure sp_listarCategoriaProducto()
	BEGIN
		select
			C.categoriaProductosId,
            C.nombreCategoria,
            C.descripcionCategoria
				from CategoriaProductos C;
    END $$
DELIMITER ;
call sp_listarCategoriaProducto();

DELIMITER $$
create procedure sp_buscarCategoriaProducto(in catPId int)
	BEGIN
		select
			CategoriaProductos.categoriaProductosId,
            CategoriaProductos.nombreCategoria,
            CategoriaProductos.descripcionCategoria
			from CategoriaProductos
			where categoriaProductosId = catPId;
    END $$
DELIMITER ;
call sp_buscarCategoriaProducto(1);

DELIMITER $$
create procedure sp_eliminarCategoriaProducto(in catPId int)
	BEGIN
		delete
			from CategoriaProductos
				where categoriaProductosId = catPId;
    END $$
DELIMITER ;

DELIMITER $$
create procedure sp_editarCategoriaProducto(in catPId int, in nombC varchar(30), in descC varchar(100))
	BEGIN
		update CategoriaProductos
			set
				nombreCategoria = nombC,
                descripcionCategoria = descC
					where categoriaProductosId = catPId;
    END $$
DELIMITER ;

-- CategoriaProductos

-- Distribuidor
DELIMITER $$
create procedure sp_agregarDistribuidor(in nomD varchar(30), in dirD varchar(200), in nitD varchar(15), in telD varchar(15), in web varchar(50))
	BEGIN
		insert into Distribuidores(nombreDistribuidor, direccionDistribuidor, nitDistribuidor, telefonoDistribuidor, web) values
			(nomD, dirD, nitD, telD, web);
    END $$
DELIMITER ;
call sp_agregarDistribuidor ('Henry', 'Suchitepequez','23354216','5432-1223','facebook.com');

DELIMITER $$
create procedure sp_listarDistribuidor()
	BEGIN
		select
			D.distribuidorId,
			D.nombreDistribuidor,
			D.direccionDistribuidor,
			D.nitDistribuidor,
			D.telefonoDistribuidor,
			D.web
				from Distribuidores D;
    END $$
DELIMITER ;
call sp_listarDistribuidor();

DELIMITER $$
create procedure sp_buscarDistribuidor(in disId int)
	BEGIN
		select * from Distribuidores
			where distribuidorId = disId;
    END $$
DELIMITER ;

DELIMITER $$
create procedure sp_eliminarDistribuidor(in disId int)
	BEGIN
		delete
			from Distribuidores
				where distribuidorId = disId;
    END $$
DELIMITER ;

DELIMITER $$
create procedure sp_editarDistribuidor(in disId int, in nomD varchar(30), in dirD varchar(200), in nitD varchar(15), in telD varchar(15), in web varchar(50))
	BEGIN
		update Distribuidores
			set
				nombreDistribuidor = nomD,
                direccionDistribuidor = dirD,
                nitDistribuidor = nitD,
                telefonoDistribuidor = telD,
                web = web
					where distribuidorId = disId;
    END $$
DELIMITER ;
-- Distribuidor

-- Productos
DELIMITER $$
create procedure sp_agregarProducto(in nom varchar(50),in des varchar(100),in can int, in preU decimal(10,2),in preM decimal(10,2),in preC decimal(10,2), in ima blob, in disId int, in catId int)
	BEGIN
		insert into Productos(nombreProducto, descripcionProducto, cantidadStock, precioUnitario, precioVentaMayor, precioCompra, imagenProducto, distribuidorId, categoriaProductosId ) values
			(nom, des, can, preU, preM, preC, ima, disId, catId);
	END $$
DELIMITER ;

DELIMITER $$
create procedure sp_listarProducto()
	BEGIN 
		select * from Productos;
    END $$
DELIMITER ;

DELIMITER $$
create procedure sp_buscarProducto(in proId int)
	BEGIN 
		select * from Productos
        where productoId = proId;
    END $$
DELIMITER ;

DELIMITER $$
create procedure sp_eliminarProducto(in proId int)
	BEGIN
		delete from Productos
			where productoId = proId;
    END $$
DELIMITER ;

DELIMITER $$
create procedure sp_editarProducto(in proId int, in nom varchar(50),in des varchar(100),in can int, in preU decimal(10,2),in preM decimal(10,2),in preC decimal(10,2), in ima blob, in disId int, in catId int )
	BEGIN
		update Productos	
			set 
            nombreProducto = nom,
            descripcionProduto = des,
            cantidadStock = can,
            precioVentaUnitario = preU,
            precioVentaMayor = preM,
            precioCompra = preC,
            imagenProducto = ima,
            distribuidorId = disId,
            categoriaProductosId = catId
            where productoId = proId;
    END $$
DELIMITER ;
-- Productos

-- Promociones
DELIMITER $$
create procedure sp_agregarPromociones(prePro decimal (10,2), desPro varchar (100), fecI date, fecF date, in proId int)
BEGIN
	insert into Promociones(precioPromocion, descripcionPromocion, fechaInicio, fechaFinalizacion, productoId )values
    (prePro,desPro, fecI, fecF, proId);
    
END $$
DELIMITER ;

-- call sp_agregarPromociones();

DELIMITER $$
create procedure sp_listarPromociones()
BEGIN
	select
    Promociones.promocionId,
    Promociones.precioPromocion,
    Promociones.descripcionPromocion,
    Promociones.fechaInicio,
    Promociones.fechaFinalizacion,
    Promociones.productoId
		FROM Promociones;

END $$
DELIMITER ;

-- call sp_listarPromociones();

DELIMITER $$
create procedure sp_eliminarPromociones(in proId int)
BEGIN
	delete
		from Promociones
        where promocionId = proId;
END $$
DELIMITER ;
-- call sp_eliminarPromociones();

-- Buscar Promociones
DELIMITER $$
create procedure sp_buscarPromociones(in proId int)
BEGIN
	select
		Promociones.promocionId,
        Promociones.precioPromocion,
        Promociones.descripcionPromocion,
        Promociones.fechaInicio,
        Promociones.fechaFinalizacion,
        Promociones.productoId
			from Promociones
            where promocionId = proId;
END $$
DELIMITER  ;

DELIMITER $$
create procedure sp_editarPromociones(in promId int, prePro decimal (10,2), desPro varchar (100), fecI date, fecF date, in proId int )
BEGIn
	update Promociones
		set
			precioPromocion = prePro,
            descripcionPromocion = desPro,
            fechaInicio = fecI,
            fechFinalizacion = fecF,
            profuctoId = proId
            where promocionId = promId;
END $$
DELIMITER ;
-- Promociones

-- DetalleCompra
DELIMITER $$
create procedure sp_agregarDetalleCompra(in canC int, in proId int, in comId int)
	BEGIN
		insert into DetalleCompra(cantidadCompra, productoId, compraId) values
			(canC, proId, comId);
    END $$
DELIMITER ;

DELIMITER $$
create procedure sp_listarDetalleCompra()
	BEGIN
		select
			DC.detalleCompraId,
            DC.cantidadCompra,
            DC.productoId,
            DC.compraId
				from DetalleCompra DC;
    END $$
DELIMITER ;

DELIMITER $$
create procedure sp_buscarDetalleCompra(in detCId int)
	BEGIN
		select * from DetalleCompra
				where detalleCompraId = detCId;
    END $$
DELIMITER ;

DELIMITER $$
create procedure sp_eliminarDetalleCompra(in detCId int)
	BEGIN
		delete
			from DetalleCompra
				where detalleCompraId = detCId;
    END $$
DELIMITER ;

DELIMITER $$
create procedure sp_editarDetalleCompra(in detCId int, in canC int, in proId int, in comId int)
	BEGIN
		update DetalleCompra
			set
				cantidadCompra = canC,
                productoId = proId,
                compraId = comId
					where detalleCompraId = detCId;
    END $$
DELIMITER ;
-- DetalleCompra

-- Empleados
DELIMITER $$
	create procedure sp_AgregarEmpleados (in nomEmp varchar (30), in apeEmp  varchar (30), in sud decimal (10, 2), in horEntr time, in horSld time, in cargId int, in encaId int)
		BEGIN 
			insert into Empleados (nombreEmpleado , apellidoEmpleado, sueldo, horaEntrada, horaSalida, cargoId, encargadoId)
				values (nomEmp, apeEmp, sud, horEntr, horSld, cargId, encaId);
		END $$
DELIMITER ;

call sp_AgregarEmpleados('Jorge', 'Peralta', 3420.90, '8:00:00', '17:00:00', 1, null);

DELIMITER $$
create procedure sp_ListarEmpleados()
	BEGIN
		select 
			E1.empleadoId, E1.nombreEmpleado, E1.apellidoEmpleado, E1.sueldo, E1.horaEntrada, E1.horaSalida,
			C.nombreCargo,
			E2.nombreEmpleado 'Encargao' from Empleados E1
			join Cargos C on C.cargoId = E1.cargoId
			left join Empleados E2 on E1.encargadoId = E2.empleadoId;
    END $$
DELIMITER ;
call sp_listarEmpleados();

DELIMITER $$
	create procedure sp_EliminarEmpleados (in empId int)
		BEGIN
			delete
				from Empleados
					where empleadoId = empId;
		END $$
DELIMITER ;

DELIMITER $$
	create procedure sp_BuscarEmpleados (in empId int)
		BEGIN 
			select
				Empleados.empleadoId,
				Empleados.nombreEmpleado,
                Empleados.apellidoEmpleado,
                Empleados.sueldo,
                Empleados.horaEntrada,
                Empleados.horaSalida,
                Empleados.cargoId,
                Empleados.encargadoId
					from Empleados 
						where empleadoId = empId;
		END $$
DELIMITER ;
call sp_BuscarEmpleados(2);

DELIMITER $$
	create procedure sp_EditarEmpleados (in empId int, in nomEmp varchar (30), in apeEmp  varchar (30), in sud decimal (10, 2), in horEntr time, in horSld time, in cargId int, in encaId int)
		BEGIN
			update Empleados
				set	
					nombreEmpleado = nomEmp,
					apellidoEmpleado = apeEmp,
					sueldo = sud,
					horaEntrada = horEntr, 
					horaSalida = horSld,
					cargoId = cargId,
					encargadoId = encaId
					where empleadoId = empId;
		END $$
DELIMITER ;
-- Empleados

-- Facturas
DELIMITER $$
	create procedure sp_AgregarFacturas (in fech date, in hor time, in tot decimal (10, 2), in cliId int, in empId int)
		BEGIN 
			insert into Facturas (fecha, hora, total, clienteId, empleadoId)
				values (fech, hor, tot, cliId, empId);
		END $$
DELIMITER ;

call sp_AgregarFacturas('2023-04-23', '8:00:00', 45.90, 1, 1);

DELIMITER $$
	create procedure sp_ListarFacturas ()
		BEGIN 
			select 
				Facturas.facturaId,
				Facturas.fecha,
                Facturas.hora,
                Facturas.total,
                Facturas.clienteId,
                Facturas.empleadoId
					FROM Facturas;
		END $$
DELIMITER ;

DELIMITER $$
	create procedure sp_EliminarFacturas (in facId int)
		BEGIN
			delete
				from Facturas
					where facturaId = facId;
		END $$
DELIMITER ;

DELIMITER $$
	create procedure sp_BuscarFacturas (in facId int)
		BEGIN 
			select
				Facturas.fecha,
                Facturas.hora,
                Facturas.total,
                Facturas.clienteId,
                Facturas.empleadoId
					from Facturas
						where facturaId = facId;
		END $$
DELIMITER ;

DELIMITER $$
	create procedure sp_EditarFacturas (in facId int, in fech date, in hor time, in tot decimal (10, 2), in cliId int, in empId int)
		BEGIN
			update Facturas
				set	
					fecha = fech,
					hora = hor,
					total = tot,
					clienteId = cliId,
					empleadoId = empId
					where facturaId = facId;
		END $$
DELIMITER ;
-- Facturas

-- TicketSoporte
DELIMITER $$
create procedure sp_agregarTicketSoporte(in descTick varchar (250), in cliId int, in facId int)
BEGIN 
	insert into TicketSoporte (descripcionTicket, estatus , clienteId , facturaId)
		values (descTick, 'Recien Creado', cliId, facId);
	
END $$
DELIMITER ;

call sp_agregarTicketSoporte('Problema del wifi', 1, 1);

DELIMITER $$
create procedure sp_listarTicketSoporte()
BEGIN 
	select TS.ticketSoporteId, TS.descripcionTicket, TS.estatus,
		CONCAT('Id: ', C.clienteId, ' | ', C.nombre, ' ', C.apellido) As 'cliente',
		TS.facturaId from TicketSoporte TS
	join Clientes C on TS.clienteId = C.clienteId;
END $$
DELIMITER ;

select * from TicketSoporte;

DELIMITER $$
create procedure sp_eliminarTicketSoporte(in tickSopId int)
BEGIN
	delete
		from TicketSoporte 
			where ticketSoporteId = tickSopId;
END $$
DELIMITER ;

DELIMITER $$
create procedure sp_buscarTicketSoporte(in tickSopId int)
BEGIN 
	select
		TicketSoporte.descripcionTicket,
		TicketSoporte.estatus,
		TicketSoporte.clienteId,
		TicketSoporte.facturaId
			from TicketSoporte
				where ticketSoporteId = tickSopId;
END $$
DELIMITER ;

DELIMITER $$
create procedure sp_editarTicketSoporte(in tickSopId int, in descTick varchar (250), in est varchar (30), in cliId int, in facId int)
BEGIN
	update TicketSoporte
		set	
			descripcionTicket = descTick,
			estatus = est,
			clienteId = cliId,
			facturaId = facId
			where ticketSoporteId = tickSopId;
END $$
DELIMITER ;
-- TicketSoporte

-- DetalleFactura
DELIMITER $$
	create procedure sp_agregarDetalleFactura  (in factId int, in proId int)
		BEGIN 
			insert into DetalleFactura  (facturaId, productoId)
				values (factId, proId);
		END $$
DELIMITER ;

DELIMITER $$
	create procedure sp_ListarDetalleFactura  ()
		BEGIN 
			select 
				DetalleFactura.detalleFacturaId,
				DetalleFactura.facturaId,
                DetalleFactura.productoId
					FROM DetalleFactura;
		END $$
DELIMITER ;

DELIMITER $$
	create procedure sp_EliminarDetalleFactura   (in detaFacId int)
		BEGIN
			delete
				from DetalleFactura  
					where detalleFacturaId  = detaFacId;
		END $$
DELIMITER ;

DELIMITER $$
	create procedure sp_BuscarDetalleFactura  (in detaFacId int)
		BEGIN
			select
				DetalleFactura.facturaId,
                DetalleFactura.productoId
					from DetalleFactura 
						where detalleFacturaId  = detaFacId;
		END $$
DELIMITER ;

DELIMITER $$
	create procedure sp_EditarDetalleFactura  (in detaFacId int, in factId int, in proId int)
		BEGIN
			update DetalleFactura 
				set	
					facturaId = factId,
					productoId = proId
					where detalleFacturaId  = detaFacId;
		END $$
DELIMITER ;
-- DetalleFactura

-- Encargado
DELIMITER $$
	CREATE PROCEDURE sp_AsignarEncargado (IN encar INT, In empId INT)
		BEGIN
			UPDATE Empleados
				SET 
					Encargados = ecar
                    WHERE empleadoId = empId;
		END $$
DELIMITER ;
-- Apoyo