package trainner.soa.com.sensoresmanager.entidad;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eze on 08/11/2016.
 */
public class Arduino {

    @SerializedName("temp")
    private String temp;

    @SerializedName("corte")
    private String corte;

    @SerializedName("humedad")
    private String humedad;

    @SerializedName("ventilador")
    private String ventilador;

    @SerializedName("humo")
    private String humo;

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getCorte() {
        return corte;
    }

    public void setCorte(String corte) {
        this.corte = corte;
    }

    public String getHumedad() {
        return humedad;
    }

    public void setHumedad(String humedad) {
        this.humedad = humedad;
    }

    public String getVentilador() {
        return ventilador;
    }

    public void setVentilador(String ventilador) {
        this.ventilador = ventilador;
    }

    public String getHumo() {
        return humo;
    }

    public void setHumo(String humo) {
        this.humo = humo;
    }
}
