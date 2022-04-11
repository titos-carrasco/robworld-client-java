package rcr.robworld;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Clase padre de todos los robots
 *
 * @author Roberto carrasco (titos.carrasco@gmail.com)
 *
 */
public class RobotBase {
    private String name;
    private String host;
    private int port;
    private String tipo;
    private Socket sock;
    protected PrintWriter out;
    protected BufferedReader in;

    private double[] pos = null;
    private double[] speed = null;
    private double angle = 0;

    /**
     * Constructor padre de los robots
     *
     * @param name Nombre del robot remoto
     * @param host Direccion IP o nombre del simulador Robot World
     * @param port Puerta que utiliza el simulador Robot World
     * @param tipo Tipo de robot (epuck, thymio2, marxbot)
     * @throws Exception Cuando no se pudo abrir la conexion al simulador
     */
    RobotBase(String name, String host, int port, String tipo) throws Exception {
        this.host = host;
        this.port = port;
        this.name = name;
        this.tipo = tipo;

        // nos conectamos al simulador
        sock = new Socket(this.host, this.port);
        out = new PrintWriter(sock.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(sock.getInputStream(), "iso-8859-1"));

        // solicitamos acceso enviando el nombre del robot
        JsonObject jobj = Json.createObjectBuilder().add("connect", name).build();
        String resp = sendCommand(jobj, true);

        // si es aceptado nos envia el tipo de robot que somos
        JsonReader jsonReader = Json.createReader(new StringReader(resp));
        JsonObject json = jsonReader.readObject();
        jsonReader.close();

        if (!this.tipo.equals(json.getString("type")))
            throw new Exception("Robot no aceptado");
    }

    /**
     * Cierra la conexion con el robot
     */
    public void close() {
        try {
            if (sock != null) {
                out.close();
                in.close();
                sock.close();
            }
        } catch (Exception e) {
        }
    }

    /**
     * Retorna el nombre del robot
     *
     * @return El nombre
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna la posicion actual del robot en centimetros
     *
     * @return La posicion actual x, y
     */
    public double[] getPos() {
        return pos;
    }

    /**
     * Retorna la velocidad actual del robot
     *
     * @return La velocidad actual para el motor izquierdo [0] y derecho [1]
     */
    public double[] getSpeed() {
        return speed;
    }

    /**
     * Obtiene el angulo de giro actual del robot
     *
     * @return El angulo de giro actual en grados
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Establece la velocidad del robot
     *
     * @param leftSpeed  La velocidad para el motor izquierdo (positivo o negativo)
     * @param rightSpeed La velocidad para el motor dereco (positivo o negativo)
     * @throws Exception Cuando existe un error de conexion con el simulador
     */
    public void setSpeed(int leftSpeed, int rightSpeed) throws Exception {
        JsonObject jobj = Json.createObjectBuilder().add("cmd", "setSpeed").add("leftSpeed", leftSpeed)
                .add("rightSpeed", rightSpeed).build();

        sendCommand(jobj, true);
    }

    /**
     * Obtiene todos los sensores del robot desde el servidor
     *
     * @return Los sensores
     * @throws Exception
     */
    protected JsonObject retrieveSensors() throws Exception {
        JsonObject jobj = Json.createObjectBuilder().add("cmd", "getSensors").build();

        String resp = sendCommand(jobj, true);
        JsonReader jsonReader = Json.createReader(new StringReader(resp));
        JsonObject json = jsonReader.readObject();
        jsonReader.close();

        speed = getDoubleArray(json.getJsonArray("speed"));
        pos = getDoubleArray(json.getJsonArray("pos"));
        angle = json.getJsonNumber("angle").doubleValue();

        return json;
    }

    /**
     * Envia un comando al robot en el servidor
     *
     * @param jobj     El comando en formato json segun lo especifica Robot World
     * @param response El comando genera respuesta desde el servidor
     * @return La respuesta desde el servidor o null
     * @throws Exception
     */
    protected String sendCommand(JsonObject jobj, boolean response) throws Exception {
        out.print(jobj);
        out.print('\n');
        out.flush();

        return response ? in.readLine() : null;
    }

    /**
     * Convierte un arrego en formato json a un double[]
     *
     * @param arr El arreglo a transformar
     * @return El arreglo transformado
     */
    protected double[] getDoubleArray(JsonArray arr) {
        int len = arr.size();
        double[] f = new double[len];

        for (int i = 0; i < len; i++) {
            f[i] = (arr.getJsonNumber(i)).doubleValue();
        }

        return f;
    }
}
