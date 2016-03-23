package util;

import java.sql.Connection;
import java.sql.*;
import java.sql.SQLException;
import oracle.jdbc.OracleDriver;

/**
 * Recursos Humanos
 *
 * @author Alba Consuelo Nieto
 */
public class ServiceLocator {

    //Usuario hr
    private final String USUARIO = "hr";

    //Pass word hr
    private final String PASS = "hr";

    private final String SID = "XE";
    private final String HOST = "localhost";

    // Puerto estándar
    private final int PUERTO = 1521;

    /**
     * Instancia del ServiceLocator
     */
    private static ServiceLocator instance = null;

    /**
     * Conexion compartida a la Base de Datos
     */
    private Connection conexion = null;

    /**
     * Bandera que indica el estado de la conexion
     */
    private boolean conexionLibre = true;

    /**
     * @return instancia del ServiceLocator para el manejo de la conexion
     */
    public static ServiceLocator getInstance() {
        if (instance == null) {
            try {
                instance = new ServiceLocator();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    // Método para retornar la conexión
    public Connection getConnection() {
        return conexion;
    }

    // Método para el Driver
    public void registrarDriver() throws SQLException {
        OracleDriver oracleDriver =  new OracleDriver();
        DriverManager.registerDriver(oracleDriver);
    }

    /**
     * @throws Exception dice si no se pudo crear la conexion
     */
    private ServiceLocator() throws Exception {
        try {
            /**
             * TODO Establecer la conexion a la bd. usuario= hr, password= hr *
             */
            
            if (conexion == null || conexion.isClosed() == true) {
                String datosConexion = "jdbc:oracle:thin:@"+HOST+":"+PUERTO+":"+SID;
                registrarDriver();
                conexion = DriverManager.getConnection(datosConexion,USUARIO,PASS);
            }
            
            
        if(conexion!=null)
        {
        System.out.println("Conexion exitosa");
        }
        else{System.out.println("Conexion fallida");}
            conexion.setAutoCommit(false);

        } catch (Exception e) {
            throw new RHException("ServiceLocator", "ERROR_CONEXION_BD " + e);
        }
    }

    /**
     * Toma la conexion para que ningun otro proceso la puedan utilizar
     *
     * @return da la conexion a la base de datos
     */
    public synchronized Connection tomarConexion() {
        while (!conexionLibre) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        conexionLibre = false;
        notify();
        return conexion;
    }

    /**
     * Libera la conexion de la bases de datos para que ningun otro proceso la
     * pueda utilizar
     */
    public synchronized void liberarConexion() {
        while (conexionLibre) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        conexionLibre = true;
        notify();
    }

    /**
     * Cierra la conexion a la base de datos cuando se termina de ejecutar el
     * programa
     */
    public void close() {
        try {
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza los cambios en la base de datos. Con este metodo se asegura que
     * no halla inconsitencias en el modelo relacional de la Base de datos.
     *
     * Se utiliza cuando el procedimiento de insercion es terminado
     * correctamente y se asegura que los datos en el modelo estan bien
     * relacionados.
     */
    public void commit() {
        try {
            conexion.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deshace los cambios en la base de datos. Con este metodo se asegura que
     * no halla inconsitencias en el modelo relacional de la Base de datos.
     *
     * Se utiliza por lo general cuando se devuelve una Exepcion. Los
     * procedimientos intermedios no deberia quedar almacenados en la base de
     * datos.
     */
    public void rollback() {
        try {
            conexion.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
