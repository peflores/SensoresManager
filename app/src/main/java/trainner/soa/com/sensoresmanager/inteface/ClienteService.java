package trainner.soa.com.sensoresmanager.inteface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import trainner.soa.com.sensoresmanager.entidad.Arduino;

/**
 * Created by Eze on 08/11/2016.
 */
public interface ClienteService {

       @GET("test")
    Call<Arduino> getEstado();


    @GET("rele")
    Call<Arduino> setRele(@Query("RELE") String rele);

}
