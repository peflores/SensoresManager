package trainner.soa.com.sensoresmanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import trainner.soa.com.sensoresmanager.entidad.Arduino;
import trainner.soa.com.sensoresmanager.inteface.ClienteService;

public class ServicioConsulta extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_CONECTADO = "trainner.soa.com.sensoresmanager.service.action.CONECTADO";
    public static final String ACTION_DESCONECTADO = "trainner.soa.com.sensoresmanager.service.action.DESCONECTADO";
    public static final String ACTION_DETENIDO = "trainner.soa.com.sensoresmanager.service.action.DETENIDO";

    private Thread hilo;
    private Boolean continuar = Boolean.TRUE;
    private StringBuilder url;
    private Retrofit srvArduino;
    private String HOST = "HOST";
    private String PUERTO = "PUERTO";
    private String HTTP = "http://";
    String DOSPUNTOS = ":";

    public ServicioConsulta() {


        url =  new StringBuilder(HTTP);

    }
    @Override
    public void onDestroy() {
        continuar = Boolean.FALSE;
        Log.i("MENSAJE", "LLAMO AL DESTROID");


            try {
                hilo.join();
                Intent brcEstado = new Intent();
                brcEstado.setAction(ACTION_DETENIDO);
                sendBroadcast(brcEstado);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        hacerTiempo();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        if(hilo == null || !hilo.isAlive()){
            hilo = new Thread(getConsultarEstado(intent));
            hilo.start();
        }
        return START_STICKY;

    }

    @NonNull
    private Runnable getConsultarEstado(final Intent intent) {
        return new Runnable() {
            @Override
            public void run() {
                String hos = intent.getStringExtra(HOST);
                String puerto = intent.getStringExtra(PUERTO);
                url.append(hos).append(DOSPUNTOS).append(puerto).append("/SoaRest/");
                srvArduino = new Retrofit.Builder().baseUrl(url.toString())
                        .addConverterFactory(GsonConverterFactory.create() ).build();
                ClienteService servicio =   srvArduino.create(ClienteService.class);
                do {
                    Call<Arduino> estadoCallBack = servicio.getEstado();
                    estadoCallBack.enqueue(getCallBack());
                    hacerTiempo();

                }while(continuar);
            }
        };
    }

    @NonNull
    private Callback<Arduino> getCallBack() {
        return new Callback<Arduino>() {
            @Override
            public void onResponse(Call<Arduino> call, Response<Arduino> response) {
                int codigo = response.code();
                if (codigo == 200){
                    Arduino arduino = response.body();
                    //envio de broadCast para avisar que esta conectado.
                    Intent brcEstado = new Intent();
                    brcEstado.setAction(ACTION_CONECTADO);
                    sendBroadcast(brcEstado);
                }else {
                    sendAvisoDesconexion();
                }

            }

            @Override
            public void onFailure(Call<Arduino> call, Throwable t) {
                sendAvisoDesconexion();

            }

            private void sendAvisoDesconexion(){
                Intent brcEstado = new Intent();
                brcEstado.setAction(ACTION_DESCONECTADO);
                sendBroadcast(brcEstado);

            }

        };

    }

    private void hacerTiempo(){
        try {
            Thread.sleep(500);
        }catch (InterruptedException e){

        }
    }
}
