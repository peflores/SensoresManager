package trainner.soa.com.sensoresmanager.broacast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;

import trainner.soa.com.sensoresmanager.MainActivity;
import trainner.soa.com.sensoresmanager.service.ServicioConsulta;

public class BroadCastRecepcion extends BroadcastReceiver {

    private MainActivity mainActivity;

    private String CONECTADO = "CONECTADO";

    private String DESCONECTADO = "DESCONECTADO";

    private String DETENIDO = "DETENIDO";

    public BroadCastRecepcion() {
    }
    public BroadCastRecepcion(MainActivity mainActivity ) {
        this.mainActivity = mainActivity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ServicioConsulta.ACTION_CONECTADO)) {
            notificarConectado(intent);
        } else if (intent.getAction().equals(ServicioConsulta.ACTION_DESCONECTADO)){
            notificarDesconectado(intent);
        } else if(intent.getAction().equals(ServicioConsulta.ACTION_DETENIDO)){
            notificarDetenido(intent);
        }


    }

    private void notificarConectado(Intent intent) {
        mainActivity.getTxtEstado().setText(CONECTADO);
        mainActivity.getLblSigoConectado().setText(CONECTADO);
    }
    private void notificarDesconectado(Intent intent) {
        mainActivity.getTxtEstado().setText(DESCONECTADO);
        mainActivity.getLblSigoConectado().setText(DESCONECTADO);
    }
    private void notificarDetenido(Intent intent) {

        mainActivity.getTxtEstado().setText(DETENIDO);
        mainActivity.getLblSigoConectado().setText(DETENIDO);
    }
}