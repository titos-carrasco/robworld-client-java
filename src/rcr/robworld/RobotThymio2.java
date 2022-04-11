package rcr.robworld;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 * Clase para conectar remotamento a un robot del tipo Thymio2
 *
 * @author Roberto carrasco (titos.carrasco@gmail.com)
 *
 */
public class RobotThymio2 extends RobotBase {
    private double[] proximitySensorValues = null;
    private double[] proximitySensorDistances = null;
    private double[] groundSensorValues = null;

    /**
     * Constructor del robot del tipo Thymio2
     *
     * @param name Nombre del robot remoto
     * @param host Direccion IP o nombre del servidor Robot World
     * @param port Puerta que utiliza el servidor Robot World
     * @throws Exception Cuando no se pudo abrir la conexion al simulador
     */
    public RobotThymio2(String name, String host, int port) throws Exception {
        super(name, host, port, "thymio2");
    }

    /**
     * Actualiza desde el servidor los sensores del robot
     *
     * @throws Exception Cuando existe un error de conexion con el simulador
     */
    public void getSensors() throws Exception {
        JsonObject json = retrieveSensors();

        proximitySensorValues = getDoubleArray(json.getJsonArray("proximitySensorValues"));
        proximitySensorDistances = getDoubleArray(json.getJsonArray("proximitySensorDistances"));
        groundSensorValues = getDoubleArray(json.getJsonArray("groundSensorValues"));
    }

    /**
     * Retorna el valor de los sensores de proximidad
     *
     * @return Los 7 sensores de proximidad
     */
    public double[] getProximitySensorValues() {
        return proximitySensorValues;
    }

    /**
     * Retorna los sensores de proximidad como valores de distancia
     *
     * @return Las distancias medidas por los 7 sensores de proximidad
     */
    public double[] getProximitySensorDistances() {
        return proximitySensorDistances;
    }

    /**
     * Retorna el valor de los sensores de linea
     *
     * @return Los 2 sensores de linea (0 = negro ... 1000 = blanco)
     */
    public double[] getGroundSensorValues() {
        return groundSensorValues;
    }

    /**
     * Apaga/enciende los leds que posee el robot
     *
     * @param leds Valores de intensidad (0.0 a 1.0) de cada uno de los 23 leds
     * @throws Exception Cuando existe un error de conexion con el simulador
     */
    public void setLedsIntensity(double[] leds) throws Exception {
        JsonArrayBuilder jleds = Json.createArrayBuilder();
        for (double led : leds)
            jleds.add(led);

        JsonObject jobj = Json.createObjectBuilder().add("cmd", "setLedsIntensity").add("leds", jleds.build()).build();

        sendCommand(jobj, true);
    }
}
