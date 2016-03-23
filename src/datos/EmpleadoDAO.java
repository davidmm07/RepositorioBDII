package datos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import negocio.Empleado;

import util.RHException;
import util.ServiceLocator;

/**
 * @author Alba Consuelo Nieto. Esta clase encapsula el acceso a la Base de
 * Datos
 */
public class EmpleadoDAO {

    /*
     * Constructor de la clase
     */
    public EmpleadoDAO() {

    }

    /**
     * Incluye una nueva fila en la tabla EMPLOYEES.
     *
     * @throws RHException
     */
    public void incluirEmpleado(Empleado empleado) throws RHException {
        try {

            String strSQL = "INSERT INTO employees (employee_id, first_name, last_name, job_id, email,salary, hire_date) VALUES(?,?,?,?,?,?,?)";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, empleado.getEmployee_id());
            prepStmt.setString(2, empleado.getFirst_name());
            prepStmt.setString(3, empleado.getLast_name());
            prepStmt.setString(4, empleado.getJob_id());
            prepStmt.setString(5, empleado.getEmail());
            prepStmt.setDouble(6, empleado.getSalary());
            prepStmt.setDate(7, java.sql.Date.valueOf(empleado.getHire_date()));
            prepStmt.executeUpdate();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        } catch (SQLException e) {
            throw new RHException("EmpleadoDAO", "No pudo crear el empleado" + e.getMessage());
        } finally {
            ServiceLocator.getInstance().liberarConexion();
        }

    }

    public void modificarEmpleado() {

    }

    public void eliminarEmpleado() {

    }

    public void buscarEmpleado(Integer employee_id) {
        try {
            Empleado e = new Empleado(); //Instancia el objeto para retornar los datos del empleado
            String strSQL = "SELECT employee_id, first_name, last_name, job_id, email, hire_date FROM employees WHERE employee_id = ?";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setInt(1, employee_id);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                e.setEmployee_id(rs.getInt(1));
                e.setFirst_name(rs.getString(2));
                rs.getString(3);
                rs.getString(4);
                rs.getString(5);
                rs.getDate(6);
            }
        } catch (SQLException e) {

        }

    }

    public void actualizarSalario(Empleado empleado) throws RHException {
        /**
         * TODO Implementar el método*
         */
        try {
            String strSQL = "UPDATE employees SET salary = ? WHERE employee_id = ?";
            Connection conexion = ServiceLocator.getInstance().tomarConexion();
            PreparedStatement prepStmt = conexion.prepareStatement(strSQL);
            prepStmt.setDouble(1, empleado.getSalary());
            prepStmt.setInt(2, empleado.getEmployee_id());
            prepStmt.executeQuery();
            prepStmt.close();
            ServiceLocator.getInstance().commit();
        } catch (SQLException e) {
            throw new RHException("EmpleadoDAO", "No pudo actualizar el salario" + e.getMessage());
        }finally {
            ServiceLocator.getInstance().liberarConexion();
        }
    }



}
