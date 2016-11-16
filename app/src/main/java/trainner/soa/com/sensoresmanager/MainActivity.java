package trainner.soa.com.sensoresmanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import trainner.soa.com.sensoresmanager.broacast.BroadCastRecepcion;
import trainner.soa.com.sensoresmanager.broacast.ServicioVentilador;
import trainner.soa.com.sensoresmanager.entidad.Arduino;
import trainner.soa.com.sensoresmanager.inteface.ClienteService;
import trainner.soa.com.sensoresmanager.service.ServicioConsulta;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final int NOTIF_ALERTA_ID = 1;
    private TabHost TbH;
    private Button empezar;
    private Button detener;
    private Button scanQR;
    private Boolean presionado = Boolean.FALSE;
    private TextView txtEstado;
    private Intent servicio;
    private Intent servicioRele;
    private EditText txtDirIp;
    private EditText txtPuerto;
    private String HOST = "HOST";
    private String PUERTO = "PUERTO";
    private TextView lblSigoConectado;
    private Switch ventilador;
    private EditText txtHumedad;
    private EditText txtHumo;
    private EditText txtTemperatura;
    private EditText txtCorte;
    private Bitmap largeIcon;
    private NotificationCompat.Builder mBuilder;
    private Intent notIntent;
    private  PendingIntent contIntent;
    private Sensor acelerometro;
    private Float cordenadaX;
    private Float cordenadaY;
    private Float cordenadaZ;
    private long ultimaActualizacion;
    private Float ultimoX;
    private Float ultimoY;
    private Float ultimoZ;
    private static final int SE_AGITO = 500;
    private SensorManager mSensorManager;
    public Boolean hacerTiempo = Boolean.FALSE;
    private static Boolean CONTINUAR_NOTIFICACION = Boolean.TRUE;
    public TextView getLblSigoConectado() {
        return lblSigoConectado;
    }

    public void setLblSigoConectado(TextView lblSigoConectado) {
        this.lblSigoConectado = lblSigoConectado;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarComponentes();
        crearTabs();

        largeIcon = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_dialog_alert);
        mBuilder = new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(android.R.drawable.stat_sys_warning)
                        .setLargeIcon(largeIcon)
                        .setContentTitle("Se Corto la Luz")
                        .setContentText("App Monitoreo de Luz")
                        .setTicker("Alerta!")
                .setVibrate(new long[]{100, 250, 100, 500});
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(defaultSound);
        notIntent = new Intent(MainActivity.this, MainActivity.class);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acelerometro = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


    }
    private void inicializarComponentes() {

        empezar = (Button) findViewById(R.id.btnEmpezar);
        scanQR = (Button) findViewById(R.id.btnScanQR);
        txtEstado = (TextView) findViewById(R.id.txtEstado);
        txtDirIp = (EditText) findViewById(R.id.txtDirIp);
        txtTemperatura = (EditText) findViewById(R.id.txtTemperatura);
        txtHumedad = (EditText) findViewById(R.id.txtHumedad);
        txtHumo = (EditText) findViewById(R.id.txtHumo);
        ventilador = (Switch) findViewById(R.id.interruptorVentilado);
        ventilador.setEnabled(Boolean.FALSE);
        txtCorte = (EditText) findViewById(R.id.txtVElectricidad);
        txtPuerto = (EditText) findViewById(R.id.txtPuerto);
        empezar.setOnClickListener(getListerner());
        servicio = new Intent(this, ServicioConsulta.class);
        servicioRele = new Intent(this,ServicioVentilador.class);
        lblSigoConectado = (TextView) findViewById(R.id.lblSigoConectado);
        detener = (Button) findViewById(R.id.btnDetener);
        detener.setOnClickListener(getListenerDetener());
        final IntentIntegrator scan = new IntentIntegrator(this);
        scanQR.setOnClickListener(getListeneScanQR(scan));
        ventilador.setOnCheckedChangeListener(getListenerCheckedChanged());
        regsitrarFiltros();
        cordenadaX = new Float(0);
        cordenadaY = new Float(0);
        cordenadaZ = new Float(0);
        ultimoX = new Float(0);
        ultimoY = new Float(0);
        ultimoZ = new Float(0);

    }

    @NonNull
    private CompoundButton.OnCheckedChangeListener getListenerCheckedChanged() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                servicioRele.putExtra(HOST,txtDirIp.getText().toString());
                servicioRele.putExtra(PUERTO, txtPuerto.getText().toString());

                    if(isChecked){
                        servicioRele.putExtra("RELE","ON");
                        buttonView.setChecked(Boolean.TRUE);
                    }else{
                        servicioRele.putExtra("RELE","OFF");
                        buttonView.setChecked(Boolean.FALSE);
                    }
                hacerTiempo = Boolean.FALSE;
                startService(servicioRele);
            }


        };
    }

    @NonNull
    private View.OnClickListener getListeneScanQR(final IntentIntegrator scan) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan.initiateScan();
            }
        };
    }

    //Acá obtenemos los datos del servicio QR
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //Se obtiene el resultado del proceso de scaneo y se parsea
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //Quiere decir que se obtuvo resultado pro lo tanto:
            //Desplegamos en pantalla el contenido del código de barra scaneado
            String scanContent = scanningResult.getContents();

            if(isValido(scanContent)){
                scanContent=scanContent.replaceAll("\"","");
                scanContent = scanContent.substring(1, scanContent.length()-1);
                String[] split = scanContent.split(":");

                if("ip".equalsIgnoreCase(split[0]) &&
                        "port".equalsIgnoreCase(split[1].split(",")[1])) {

                    String host = split[1].split(",")[0];
                    String puerto = split[2];
                    txtPuerto.setText(puerto);
                    txtDirIp.setText(host);

                }

            }
        }else{

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Valores no reconocidos", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private boolean isValido(String scanContent) {
        return scanContent.startsWith("{") &&
                scanContent.split(":").length == 3 &&
                scanContent.contains("ip")&&
                scanContent.contains(",")&&
                scanContent.contains("port") &&
                scanContent.endsWith("}");
    }

    private void regsitrarFiltros() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ServicioConsulta.ACTION_DESCONECTADO);
        filter.addAction(ServicioConsulta.ACTION_CONECTADO);
        filter.addAction(ServicioConsulta.ACTION_DETENIDO);
        BroadCastRecepcion recepcion = new BroadCastRecepcion(this);
        registerReceiver(recepcion, filter);
    }

    @NonNull
    private View.OnClickListener getListenerDetener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopService(servicio);
            }
        };
    }

    /**
     * ACÁ SE INICIA EL SERVICION, ES EL LISTERNER DEL BOTONO EMPEZAR
     * @return
     */
    @NonNull
    private View.OnClickListener getListerner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCamposValidos()){
                    //asociamos la notificacion
                    contIntent = PendingIntent.getActivity( MainActivity.this, 0, notIntent, 0);
                    //PASAMOS PARAMETRO AL SERVICIO DE CONSULTA.
                    mBuilder.setContentIntent(contIntent);
                    servicio.putExtra(HOST, txtDirIp.getText().toString());
                    servicio.putExtra(PUERTO, txtPuerto.getText().toString());
                    activarSensor();
                    startService(servicio);

                }

            }
        };
    }

    private void activarSensor(){
        mSensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private boolean isCamposValidos() {
        return txtDirIp.getText().toString().trim().length() > 0 &&
                txtPuerto.getText().toString().trim().length() > 0;
    }

    private void crearTabs() {
        Resources res = getResources();
        TbH = (TabHost) findViewById(R.id.tabHost); //llamamos al Tabhost
        TbH.setup();                                                         //lo activamos

        TabHost.TabSpec tab1 = TbH.newTabSpec("tab1");  //aspectos de cada Tab (pestaña)
        TabHost.TabSpec tab2 = TbH.newTabSpec("tab2");

        tab1.setIndicator("", ResourcesCompat.getDrawable(getResources(), android.R.drawable.ic_menu_manage, null));    //qué queremos que aparezca en las pestañas
        tab1.setContent(R.id.ejemplo1); //definimos el id de cada Tab (pestaña)

        tab2.setIndicator("", ResourcesCompat.getDrawable(getResources(), android.R.drawable.ic_menu_more, null));    //qué queremos que aparezca en las pestañas
        tab2.setContent(R.id.ejemplo2);

        TbH.addTab(tab1); //añadimos los tabs ya programados
        TbH.addTab(tab2);
    }

    public void enviarNotificacion(){


        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        /*if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
        */
        return false;
    }
    public TabHost getTbH() {
        return TbH;
    }

    public void setTbH(TabHost tbH) {
        TbH = tbH;
    }

    public Button getEmpezar() {
        return empezar;
    }

    public void setEmpezar(Button empezar) {
        this.empezar = empezar;
    }

    public Boolean getPresionado() {
        return presionado;
    }

    public void setPresionado(Boolean presionado) {
        this.presionado = presionado;
    }

    public TextView getTxtEstado() {
        return txtEstado;
    }

    public void setTxtEstado(TextView txtEstado) {
        this.txtEstado = txtEstado;
    }

    public Intent getServicio() {
        return servicio;
    }

    public void setServicio(Intent servicio) {
        this.servicio = servicio;
    }

    public EditText getTxtDirIp() {
        return txtDirIp;
    }

    public void setTxtDirIp(EditText txtDirIp) {
        this.txtDirIp = txtDirIp;
    }

    public EditText getTxtPuerto() {
        return txtPuerto;
    }

    public void setTxtPuerto(EditText txtPuerto) {
        this.txtPuerto = txtPuerto;
    }

    public String getHOST() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    public String getPUERTO() {
        return PUERTO;
    }

    public void setPUERTO(String PUERTO) {
        this.PUERTO = PUERTO;
    }

    public Button getDetener() {
        return detener;
    }

    public void setDetener(Button detener) {
        this.detener = detener;
    }

    public Switch getVentilador() {
        return ventilador;
    }

    public void setVentilador(Switch ventilador) {
        this.ventilador = ventilador;
    }

    public EditText getTxtHumedad() {
        return txtHumedad;
    }

    public void setTxtHumedad(EditText txtHumedad) {
        this.txtHumedad = txtHumedad;
    }

    public EditText getTxtHumo() {
        return txtHumo;
    }

    public void setTxtHumo(EditText txtHumo) {
        this.txtHumo = txtHumo;
    }

    public EditText getTxtTemperatura() {
        return txtTemperatura;
    }

    public void setTxtTemperatura(EditText txtTemperatura) {
        this.txtTemperatura = txtTemperatura;
    }

    public EditText getTxtCorte() {
        return txtCorte;
    }

    public void setTxtCorte(EditText txtCorte) {
        this.txtCorte = txtCorte;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long actualActualizacion = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((actualActualizacion - ultimaActualizacion) > 200) {
                long diffTime = (actualActualizacion - ultimaActualizacion);
                ultimaActualizacion = actualActualizacion;
                cordenadaX = event.values[0] ;
                cordenadaY = event.values[1];
                cordenadaZ = event.values[2];

            double movimiento = Math.abs(cordenadaX + cordenadaY + cordenadaZ - ultimoX - ultimoY - ultimoZ) / diffTime * 10000;
                if(movimiento >  SE_AGITO){
                    agitar();
                }
                ultimoX = cordenadaX;
                ultimoY = cordenadaY;
                ultimoZ = cordenadaZ;
            }
        }
    }

    private void agitar(){
        getVentilador().setEnabled(Boolean.TRUE);
        hacerTiempo = Boolean.FALSE;
        ventilador.toggle();

    }

    public void actualizarPantalla(String ventilador,String humedad,String humo,String corte,String temperatura, String host, String puerto){
        if(hacerTiempo){

            if("NO".equalsIgnoreCase(corte) && CONTINUAR_NOTIFICACION){
                CONTINUAR_NOTIFICACION = Boolean.FALSE;
                enviarNotificacion();
            }
            getTxtDirIp().setText(host);
            getTxtPuerto().setText(puerto);
            getVentilador().setEnabled(Boolean.TRUE);
            if("SI".equalsIgnoreCase(ventilador)){
                getVentilador().setChecked(Boolean.TRUE);

            }else if("NO".equalsIgnoreCase(ventilador)){
                getVentilador().setChecked(Boolean.FALSE);
            }
            if(humedad != null && humedad.length() > 0){
                getTxtHumedad().setText(humedad.concat("%"));
            }
            if(temperatura != null && temperatura.length() > 0){
                getTxtTemperatura().setText(temperatura.concat("°"));
            }
            getTxtHumo().setText(humo);
            getTxtCorte().setText(corte);

        }
        hacerTiempo = Boolean.TRUE;
    }
    private void hacerTiempo(){
        try {
            Thread.sleep(1500);
        }catch (InterruptedException e){

        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
