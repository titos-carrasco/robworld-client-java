package rcr.robworld;

import javax.json.JsonObject;

/**
 * Clase para conectar remotamento a un robot del tipo Marxbot
 *
 * @author Roberto carrasco (titos.carrasco@gmail.com)
 *
 */
public class RobotMarxbot extends RobotBase {
    private double[] virtualBumpers = null;

    /**
     * Constructor del robot del tipo Marxbot
     *
     * @param name Nombre del robot remoto
     * @param host Direccion IP o nombre del servidor Robot World
     * @param port Puerta que utiliza el servidor Robot World
     * @throws Exception Cuando no se pudo abrir la conexion al simulador
     */
    public RobotMarxbot(String name, String host, int port) throws Exception {
        super(name, host, port, "marxbot");
    }

    /**
     * Actualiza desde el servidor los sensores del robot
     *
     * @throws Exception Cuando existe un error de conexion con el simulador
     */
    public void getSensors() throws Exception {
        JsonObject json = retrieveSensors();

        virtualBumpers = getDoubleArray(json.getJsonArray("virtualBumpers"));
    }

    /**
     * Retorna los sensores de presion que rodean al servidor
     *
     * @return Los 24  sensores de presion
     */
    public double[] getVirtualBumpers() {
        return virtualBumpers;
    }
}
