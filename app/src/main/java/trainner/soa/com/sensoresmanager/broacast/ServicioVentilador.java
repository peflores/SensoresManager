package trainner.soa.com.sensoresmanager.broacast;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import trainner.soa.com.sensoresmanager.MainActivity;
import trainner.soa.com.sensoresmanager.entidad.Arduino;
import trainner.soa.com.sensoresmanager.inteface.ClienteService;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServicioVentilador extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "trainner.soa.com.sensoresmanager.broacast.action.FOO";
    private static final String ACTION_BAZ = "trainner.soa.com.sensoresmanager.broacast.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "trainner.soa.com.sensoresmanager.broacast.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "trainner.soa.com.sensoresmanager.broacast.extra.PARAM2";
    private MainActivity mainActivity;
    private String HTTP = "http://";
    private String HOST = "HOST";
    private String PUERTO = "PUERTO";
    private ClienteService clienteServicio;
    private Retrofit srvArduino;
    private StringBuilder url;
    private String DOSPUNTOS = ":";
    private static Boolean CREADO = Boolean.FALSE;



    public ServicioVentilador(){
        super("ServicioVentilador");
        url =  new StringBuilder(HTTP);
    }
    public ServicioVentilador(final MainActivity mainActivity){
        super("ServicioVentilador");
        this.mainActivity = mainActivity;
        url =  new StringBuilder(HTTP);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ServicioVentilador.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ServicioVentilador.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String host = intent.getStringExtra(HOST);
        String puerto = intent.getStringExtra(PUERTO);
        url.append(host);
        url.append(DOSPUNTOS);
        url.append(puerto);
        url.append("/");

            srvArduino = new Retrofit.Builder().baseUrl(url.toString())
                    .addConverterFactory(GsonConverterFactory.create() ).build();
            clienteServicio =  srvArduino.create(ClienteService.class);
        Call<Arduino> estadoCallBack = clienteServicio.setRele(intent.getStringExtra("RELE"));
        estadoCallBack.enqueue(getCallbackVentilacion());
    }

    @NonNull
    private Callback<Arduino> getCallbackVentilacion() {
        return new Callback<Arduino>() {
            @Override
            public void onResponse(Call<Arduino> call, Response<Arduino> response) {

            }

            @Override
            public void onFailure(Call<Arduino> call, Throwable t) {

            }
        };
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
