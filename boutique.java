package boutique;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;

public class Boutique {

    private Connection conexion;

    public Boutique() {
        this.conexion = ConexionBoutique.conectar();
        if (this.conexion == null) {
            JOptionPane.showMessageDialog(null, "No se pudo establecer la conexión a la base de datos.");
            System.exit(1);
        }
    }

    private void menuLogin() {
        String email = JOptionPane.showInputDialog("Ingrese su correo electrónico:");
        String password = JOptionPane.showInputDialog("Ingrese su contraseña:");

        // Verificar las credenciales del administrador
        if (validarCredenciales(email, password)) {
            // Si las credenciales son válidas, obtener el nombre del administrador desde la base de datos
            String nombreAdmin = obtenerNombreAdministrador(email);
            if (nombreAdmin != null) {
                // Si se obtiene el nombre del administrador, mostrar el menú principal
            	menuPrincipalAdministrador();
                // Registrar la acción en la tabla registros_acciones
                registrarAccion("inicio_sesion", "administradores", nombreAdmin, null, null);
            } else {
                JOptionPane.showMessageDialog(null, "Error al obtener el nombre del administrador.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Credenciales incorrectas. Inténtelo de nuevo.");
        }
    }
    
    
    private boolean validarCredenciales(String email, String contraseña) {
        boolean esValido = false;

        // Validar credenciales para administradores
        String queryAdmin = "SELECT * FROM administradores WHERE email = ? AND password = ?";
        try (PreparedStatement statement = conexion.prepareStatement(queryAdmin)) {
            statement.setString(1, email);
            statement.setString(2, contraseña);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                esValido = true; // Credenciales válidas para administrador
                System.out.println("Credenciales válidas para administrador.");
            } else {
                System.out.println("No se encontró el administrador con las credenciales proporcionadas.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al validar credenciales (administradores): " + ex.getMessage());
        }

        // Validar credenciales para trabajadores si no es administrador
        if (!esValido) {
            String queryTrabajador = "SELECT * FROM trabajadores WHERE email = ? AND password = ?";
            try (PreparedStatement statement = conexion.prepareStatement(queryTrabajador)) {
                statement.setString(1, email);
                statement.setString(2, contraseña);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    esValido = true; // Credenciales válidas para trabajador
                    System.out.println("Credenciales válidas para trabajador.");
                } else {
                    System.out.println("No se encontró el trabajador con las credenciales proporcionadas.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al validar credenciales (trabajadores): " + ex.getMessage());
            }
        }

        return esValido;
    }
    
    private String obtenerNombreAdministrador(String email) {
        String nombreAdmin = null;
        String query = "SELECT nombre FROM administradores WHERE email = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                nombreAdmin = resultSet.getString("nombre");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return nombreAdmin;
    }
    
    private boolean esAdministrador(String email) {
        String query = "SELECT * FROM administradores WHERE email = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            return rs.next(); // Verificar si el usuario es un administrador
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al verificar el rol del usuario: " + ex.getMessage());
        }
        return false;
    }

    public void menuPrincipalAdministrador() {
        int opcion;
        do {
            opcion = Integer.parseInt(JOptionPane.showInputDialog(
                    "Ingrese la opción que desea utilizar:"
                    + "\n1. Menú administrador"
                    + "\n2. Menú prendas"
                    + "\n3. Menú trabajadores"
                    + "\n4. Menú proveedores"
                    + "\n5. Salir"));

            switch (opcion) {
                case 1:
                    menuAdministrador();
                    break;
                case 2:
                    menuPrendas();
                    break;
                case 3:
                    menuTrabajadores();
                    break;
                case 4:
                    menuProveedores();
                    break;
                case 5:
                    JOptionPane.showMessageDialog(null, "SISTEMA CERRADO");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Por favor, seleccione una opción válida.");
            }
        } while (opcion != 5);
    }

    public void menuAdministrador() {
        int opcion;
        do {
            opcion = Integer.parseInt(JOptionPane.showInputDialog(
                    "Ingrese la opción que desea utilizar:"
                    + "\n1. Agregar administrador"
                    + "\n2. Eliminar administrador"
                    + "\n3. Modificar administrador"
                    + "\n4. Volver al menú principal"));

            switch (opcion) {
                case 1:
                    agregarAdministrador();
                    break;
                case 2:
                    eliminarAdministrador();
                    break;
                case 3:
                    modificarAdministrador();
                    break;
                case 4:
                    break; // Volver al menú principal
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Por favor, seleccione una opción válida.");
            }
        } while (opcion != 4);
    }

    public void menuPrendas() {
        int opcion;
        do {
            opcion = Integer.parseInt(JOptionPane.showInputDialog(
                    "Ingrese la opción que desea utilizar:"
                    + "\n1. Agregar prenda"
                    + "\n2. Eliminar prenda"
                    + "\n3. Modificar prenda"
                    + "\n4. Volver al menú principal"));

            switch (opcion) {
                case 1:
                    agregarPrenda();
                    break;
                case 2:
                    eliminarPrenda();
                    break;
                case 3:
                    modificarPrenda();
                    break;
                case 4:
                    break; // Volver al menú principal
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Por favor, seleccione una opción válida.");
            }
        } while (opcion != 4);
    }

    public void menuTrabajadores() {
        int opcion;
        do {
            opcion = Integer.parseInt(JOptionPane.showInputDialog(
                    "Ingrese la opción que desea utilizar:"
                    + "\n1. Agregar trabajador"
                    + "\n2. Eliminar trabajador"
                    + "\n3. Modificar trabajador"
                    + "\n4. Volver al menú principal"));

            switch (opcion) {
                case 1:
                    agregarTrabajador();
                    break;
                case 2:
                    eliminarTrabajador();
                    break;
                case 3:
                    modificarTrabajador();
                    break;
                case 4:
                    break; // Volver al menú principal
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Por favor, seleccione una opción válida.");
            }
        } while (opcion != 4);
    }

    public void menuProveedores() {
        int opcion;
        do {
            opcion = Integer.parseInt(JOptionPane.showInputDialog(
                    "Ingrese la opción que desea utilizar:"
                    + "\n1. Agregar proveedor"
                    + "\n2. Eliminar proveedor"
                    + "\n3. Modificar proveedor"
                    + "\n4. Volver al menú principal"));

            switch (opcion) {
                case 1:
                    agregarProveedor();
                    break;
                case 2:
                    eliminarProveedor();
                    break;
                case 3:
                    modificarProveedor();
                    break;
                case 4:
                    break; // Volver al menú principal
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Por favor, seleccione una opción válida.");
            }
        } while (opcion != 4);
    }

    public void menuTrabajador() {
        int opcion;
        do {
            opcion = Integer.parseInt(JOptionPane.showInputDialog(
                    "Ingrese la opción que desea utilizar:"
                    + "\n1. Buscar prenda"
                    + "\n2. Buscar proveedor"
                    + "\n3. Cerrar sistema"));

            switch (opcion) {
                case 1:
                    menuBuscarPrenda();
                    break;
                case 2:
                    buscarProveedor();
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null, "SISTEMA CERRADO");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Por favor, seleccione una opción válida.");
            }
        } while (opcion != 3);
    }

    private void menuBuscarPrenda() {
        int opcion;
        do {
            opcion = Integer.parseInt(JOptionPane.showInputDialog(
                    "Ingrese la opción que desea utilizar para buscar la prenda:"
                    + "\n1. Buscar por nombre"
                    + "\n2. Buscar por ID"
                    + "\n3. Buscar por descripción"
                    + "\n4. Volver al menú trabajador"));

            switch (opcion) {
                case 1:
                    buscarPrendaPorNombre();
                    break;
                case 2:
                    buscarPrendaPorID();
                    break;
                case 3:
                    buscarPrendaPorDescripcion();
                    break;
                case 4:
                    break; // Volver al menú trabajador
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Por favor, seleccione una opción válida.");
            }
        } while (opcion != 4);
    }

    private void buscarPrendaPorNombre() {
        String nombrePrenda = JOptionPane.showInputDialog("Ingrese el nombre de la prenda a buscar:");
        String query = "SELECT * FROM prendas WHERE nombre = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, nombrePrenda);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                // Mostrar los datos de la prenda
                JOptionPane.showMessageDialog(null, "ID: " + rs.getInt("id")
                        + "\nNombre: " + rs.getString("nombre")
                        + "\nDescripción: " + rs.getString("descripcion")
                        + "\nPrecio: " + rs.getDouble("precio")
                        + "\nCantidad: " + rs.getInt("cantidad"));
            } else {
                JOptionPane.showMessageDialog(null, "Prenda no encontrada.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar prenda: " + ex.getMessage());
        }
    }

    private void buscarPrendaPorID() {
        int idPrenda = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID de la prenda a buscar:"));
        String query = "SELECT * FROM prendas WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setInt(1, idPrenda);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                // Mostrar los datos de la prenda
                JOptionPane.showMessageDialog(null, "ID: " + rs.getInt("id")
                        + "\nNombre: " + rs.getString("nombre")
                        + "\nDescripción: " + rs.getString("descripcion")
                        + "\nPrecio: " + rs.getDouble("precio")
                        + "\nCantidad: " + rs.getInt("cantidad"));
            } else {
                JOptionPane.showMessageDialog(null, "Prenda no encontrada.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar prenda: " + ex.getMessage());
        }
    }

    private void buscarPrendaPorDescripcion() {
        String descripcionPrenda = JOptionPane.showInputDialog("Ingrese la descripción de la prenda a buscar:");
        String query = "SELECT * FROM prendas WHERE descripcion = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, descripcionPrenda);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                // Mostrar los datos de la prenda
                JOptionPane.showMessageDialog(null, "ID: " + rs.getInt("id")
                        + "\nNombre: " + rs.getString("nombre")
                        + "\nDescripción: " + rs.getString("descripcion")
                        + "\nPrecio: " + rs.getDouble("precio")
                        + "\nCantidad: " + rs.getInt("cantidad"));
            } else {
                JOptionPane.showMessageDialog(null, "Prenda no encontrada.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar prenda: " + ex.getMessage());
        }
    }

    private void buscarProveedor() {
        int idproveedor = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del proveedor:"));
        String query = "SELECT * FROM proveedores WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setInt(1, idproveedor);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                // Mostrar los datos del proveedor
                JOptionPane.showMessageDialog(null, "ID: " + rs.getInt("id")
                        + "\nNombre: " + rs.getString("nombre")
                        + "\nContacto: "+ rs.getString("contacto")
                        + "\nTeléfono: " 
                        + rs.getString("telefono"));
            } else {
                JOptionPane.showMessageDialog(null, "Proveedor no encontrado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar proveedor: " + ex.getMessage());
        }
    }

    // Métodos para agregar, eliminar y modificar datos
    private void agregarAdministrador() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del administrador:");
        String email = JOptionPane.showInputDialog("Ingrese el email del administrador:");
        String password = JOptionPane.showInputDialog("Ingrese la contraseña del administrador:");

        // Insertar el nuevo administrador en la base de datos
        String query = "INSERT INTO administradores (nombre, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, nombre);
            statement.setString(2, email);
            statement.setString(3, password);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                // Registro de acción en la tabla registros_acciones
                registrarAccion("Agregar", "administradores", nombre, email, obtenerContraseñaDato("administradores", email));
                JOptionPane.showMessageDialog(null, "Administrador agregado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo agregar el administrador.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al agregar administrador: " + ex.getMessage());
        }
    }
    private String obtenerContraseñaDato(String tabla, String email) {
        // Obtener la contraseña del dato modificado
        String contraseña = null;
        String query = "SELECT password FROM " + tabla + " WHERE email = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                contraseña = rs.getString("password");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener la contraseña del dato modificado: " + ex.getMessage());
        }
        return contraseña;
    }

    private void eliminarAdministrador() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del administrador a eliminar:"));
        
        String query = "DELETE FROM administradores WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Administrador eliminado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un administrador con el ID proporcionado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar administrador: " + ex.getMessage());
        }
    }

    private void modificarAdministrador() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del administrador a modificar:"));
        
        String nombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del administrador:");
        String email = JOptionPane.showInputDialog("Ingrese el nuevo email del administrador:");
        String password = JOptionPane.showInputDialog("Ingrese la nueva contraseña del administrador:");
        
        String query = "UPDATE administradores SET nombre = ?, email = ?, password = ? WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, nombre);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setInt(4, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Administrador modificado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un administrador con el ID proporcionado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al modificar administrador: " + ex.getMessage());
        }
    }

    private void agregarPrenda() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre de la prenda:");
        String descripcion = JOptionPane.showInputDialog("Ingrese la descripción de la prenda:");
        double precio = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el precio de la prenda:"));
        int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de la prenda:"));
        
        String query = "INSERT INTO prendas (nombre, descripcion, precio, cantidad) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, nombre);
            statement.setString(2, descripcion);
            statement.setDouble(3, precio);
            statement.setInt(4, cantidad);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Prenda agregada exitosamente.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al agregar prenda: " + ex.getMessage());
        }
    }

    private void eliminarPrenda() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID de la prenda a eliminar:"));
        
        String query = "DELETE FROM prendas WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Prenda eliminada exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró una prenda con el ID proporcionado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar prenda: " + ex.getMessage());
        }
    }

    private void modificarPrenda() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID de la prenda a modificar:"));
        
        String nombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre de la prenda:");
        String descripcion = JOptionPane.showInputDialog("Ingrese la nueva descripción de la prenda:");
        double precio = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el nuevo precio de la prenda:"));
        int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la nueva cantidad de la prenda:"));
        
        String query = "UPDATE prendas SET nombre = ?, descripcion = ?, precio = ?, cantidad = ? WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, nombre);
            statement.setString(2, descripcion);
            statement.setDouble(3, precio);
            statement.setInt(4, cantidad);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Prenda modificada exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró una prenda con el ID proporcionado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al modificar prenda: " + ex.getMessage());
        }
    }

    private void agregarTrabajador() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del trabajador:");
        String email = JOptionPane.showInputDialog("Ingrese el email del trabajador:");
        String puesto = JOptionPane.showInputDialog("Ingrese el puesto del trabajador:");
        String password = JOptionPane.showInputDialog("Ingrese la contraseña del trabajador:");
        
        String query = "INSERT INTO trabajadores (nombre, email, puesto, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, nombre);
            statement.setString(2, email);
            statement.setString(3, puesto);
            statement.setString(4, password);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Trabajador agregado exitosamente.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al agregar trabajador: " + ex.getMessage());
        }
    }

    private void eliminarTrabajador() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del trabajador a eliminar:"));
        
        String query = "DELETE FROM trabajadores WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Trabajador eliminado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un trabajador con el ID proporcionado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar trabajador: " + ex.getMessage());
        }
    }

    private void modificarTrabajador() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del trabajador a modificar:"));
        
        String nombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del trabajador:");
        String email = JOptionPane.showInputDialog("Ingrese el nuevo email del trabajador:");
        String puesto = JOptionPane.showInputDialog("Ingrese el nuevo puesto del trabajador:");
        String password = JOptionPane.showInputDialog("Ingrese la nueva contraseña del trabajador:");
        
        String query = "UPDATE trabajadores SET nombre = ?, email = ?, puesto = ?, password = ? WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, nombre);
            statement.setString(2, email);
            statement.setString(3, puesto);
            statement.setString(4, password);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Trabajador modificado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un trabajador con el ID proporcionado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al modificar trabajador: " + ex.getMessage());
        }
    }

    private void agregarProveedor() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del proveedor:");
        String contacto = JOptionPane.showInputDialog("Ingrese el contacto del proveedor:");
        String telefono = JOptionPane.showInputDialog("Ingrese el teléfono del proveedor:");
        
        String query = "INSERT INTO proveedores (nombre, contacto, telefono) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, nombre);
            statement.setString(2, contacto);
            statement.setString(3, telefono);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Proveedor agregado exitosamente.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al agregar proveedor: " + ex.getMessage());
        }
    }

    private void eliminarProveedor() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del proveedor a eliminar:"));
        
        String query = "DELETE FROM proveedores WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Proveedor eliminado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un proveedor con el ID proporcionado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar proveedor: " + ex.getMessage());
        }
    }

    private void modificarProveedor() {
        int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del proveedor a modificar:"));
        
        String nombre =JOptionPane.showInputDialog("Ingrese el nuevo nombre del proveedor:");
        String contacto = JOptionPane.showInputDialog("Ingrese el nuevo contacto del proveedor:");
        String telefono = JOptionPane.showInputDialog("Ingrese el nuevo teléfono del proveedor:");
        
        String query = "UPDATE proveedores SET nombre = ?, contacto = ?, telefono = ? WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, nombre);
            statement.setString(2, contacto);
            statement.setString(3, telefono);
            statement.setInt(4, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Proveedor modificado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un proveedor con el ID proporcionado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al modificar proveedor: " + ex.getMessage());
        }
    }
    private void registrarAccion(String tipoAccion, String tabla, String nombreAdmin, String emailModificado, String passwordModificado) {
        // Obtener la fecha y hora actual
        // LocalDateTime fechaHoraActual = LocalDateTime.now(); // Esta es una opción si estás usando Java 8 o superior
        // Timestamp fechaHoraTimestamp = Timestamp.valueOf(fechaHoraActual); // Convertir a Timestamp
        java.util.Date fechaHoraActual = new java.util.Date();

        // Insertar el registro de acción en la tabla registros_acciones
        String query = "INSERT INTO registros_acciones (nombre_administrador, tipo_accion, tabla_modificada, email_modificado, password_modificado, fecha_hora) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, nombreAdmin);
            statement.setString(2, tipoAccion);
            statement.setString(3, tabla);
            statement.setString(4, emailModificado);
            statement.setString(5, passwordModificado);
            statement.setTimestamp(6, new java.sql.Timestamp(fechaHoraActual.getTime()));
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al registrar la acción: " + ex.getMessage());
        }
    }


    public static void main(String[] args) {
        Boutique boutique = new Boutique();
        boutique.menuLogin();
    }
}

class ConexionBoutique {
    private static Connection conexion;

    public static Connection conectar() {
        if (conexion != null) {
            return conexion;
        }
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/boutique", "postgres", "BasesDatos2024");
            JOptionPane.showMessageDialog(null, "Inicio Exitoso a la Base De Datos ");
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + ex.getMessage());
        }
        return conexion;
    }
}
