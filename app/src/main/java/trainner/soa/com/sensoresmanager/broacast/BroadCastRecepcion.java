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
        String ventilador = intent.getStringExtra(ServicioConsulta.VENTILADOR);
        String humedad = intent.getStringExtra(ServicioConsulta.HUMEDAD);
        String humo = intent.getStringExtra(ServicioConsulta.HUMO);
        String corte = intent.getStringExtra(ServicioConsulta.CORTE);
        String temperatura = intent.getStringExtra(ServicioConsulta.TEMPERATURA);
        if("SI".equals(ventilador)){
            mainActivity.getVentilador().setChecked(Boolean.TRUE);
        }else if("NO".equals(ventilador)){
            mainActivity.getVentilador().setChecked(Boolean.FALSE);
        }
        if(humedad != null && humedad.length() > 0){
            mainActivity.getTxtHumedad().setText(humedad.concat("%"));
        }
        if(temperatura != null && temperatura.length() > 0){
            mainActivity.getTxtTemperatura().setText(temperatura.concat("°"));
        }
        mainActivity.getTxtHumo().setText(humo);
        mainActivity.getTxtCorte().setText(corte);

    }
    private void notificarDesconectado(Intent intent) {
        mainActivity.getTxtEstado().setText(DESCONECTADO);
        mainActivity.getLblSigoConectado().setText(DESCONECTADO);
        clearCampo();
    }
    private void notificarDetenido(Intent intent) {

        mainActivity.getTxtEstado().setText(DETENIDO);
        mainActivity.getLblSigoConectado().setText(DETENIDO);
        clearCampo();
    }

    private void clearCampo() {
        mainActivity.getVentilador().setChecked(Boolean.FALSE);
        mainActivity.getTxtHumedad().setText("");
        mainActivity.getTxtTemperatura().setText("");
        mainActivity.getTxtHumo().setText("");
        mainActivity.getTxtCorte().setText("");
    }
}