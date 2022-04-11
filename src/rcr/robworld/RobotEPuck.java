package rcr.robworld;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Clase para conectar remotamento a un robot del tipo EPuck
 *
 * @author Roberto carrasco (titos.carrasco@gmail.com)
 *
 */
public class RobotEPuck extends RobotBase {
    private double[] proximitySensorValues = null;
    private double[] proximitySensorDistances = null;

    /**
     * Constructor del robot del tipo EPuck
     *
     * @param name Nombre del robot remoto
     * @param host Direccion IP o nombre del servidor Robot World
     * @param port Puerta que utiliza el servidor Robot World
     * @throws Exception Cuando no se pudo abrir la conexion al simulador
     */
    public RobotEPuck(String name, String host, int port) throws Exception {
        super(name, host, port, "epuck");
    }

    /**
     * Enciende/apaga el anillo circular (leds) que rodea al robot
     *
     * @param on Enciende (false) o apaga (true) el anillo circular
     * @throws Exception Cuando existe un error de conexion con el simulador
     */
    public void setLedRing(boolean on) throws Exception {
        JsonObject jobj = Json.createObjectBuilder().add("cmd", "setLedRing").add("estado", on ? 1 : 0).build();

        sendCommand(jobj, true);
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
    }

    /**
     * Retorna el valor de los sensores de proximidad
     *
     * @return Los 8 sensores de proximidad
     */
    public double[] getProximitySensorValues() {
        return proximitySensorValues;
    }

    /**
     * Retorna los sensores de proximidad como valores de distancia
     *
     * @return La distancia medida por los 8 sensores de proximidad
     */
    public double[] getProximitySensorDistances() {
        return proximitySensorDistances;
    }

    /**
     * Recupera desde el servidor la imagen capturada por la camara del robot
     * La imagen es un arreglo de 60x1 pixeles donde el valor de un pixel
     * se encuentra en formato RGBA
     *
     * @return La imagen capturada
     * @throws Exception Cuando existe un error de conexion con el simulador
     */
    public List<int[]> getCameraImage() throws Exception {
        JsonObject jobj = Json.createObjectBuilder().add("cmd", "getCameraImage").build();

        sendCommand(jobj, false);

        int[] s = new int[4];
        for (int i = 0; i < 4; i++)
            s[i] = in.read();

        int len = (s[0] << 24) + (s[1] << 16) + (s[2] << 8) + s[3];

        int[] data = new int[len];
        for (int i = 0; i < len; i++)
            data[i] = in.read();

        List<int[]> imagen = new ArrayList<>();
        for (int i = 0; i < len; i += 4) {
            int[] color = { data[i + 0], data[i + 1], data[i + 2], data[i + 3] };
            imagen.add(color);
        }

        return imagen;
    }

}
