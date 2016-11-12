package trainner.soa.com.sensoresmanager.entidad;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eze on 08/11/2016.
 */
public class Arduino {


    @SerializedName("temp")
    private Float temp;
    @SerializedName("corte")
    private String corte;

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public String getCorte() {
        return corte;
    }

    public void setCorte(String corte) {
        this.corte = corte;
    }
}
