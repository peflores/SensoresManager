package trainner.soa.com.sensoresmanager;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import trainner.soa.com.sensoresmanager.broacast.BroadCastRecepcion;
import trainner.soa.com.sensoresmanager.service.ServicioConsulta;

public class MainActivity extends AppCompatActivity {

    private TabHost TbH;
    private Button empezar;
    private Button detener;
    private Boolean presionado = Boolean.FALSE;
    private TextView txtEstado;
    private Intent servicio;
    private EditText txtDirIp;
    private EditText txtPuerto;
    private String HOST = "HOST";
    private String PUERTO = "PUERTO";
    private TextView lblSigoConectado;

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
    }
    private void inicializarComponentes() {
        empezar = (Button) findViewById(R.id.btnEmpezar);
        txtEstado = (TextView) findViewById(R.id.txtEstado);
        txtDirIp = (EditText) findViewById(R.id.txtDirIp);
        txtPuerto = (EditText) findViewById(R.id.txtPuerto);
        empezar.setOnClickListener(getListerner());
        servicio = new Intent(this, ServicioConsulta.class);
        lblSigoConectado = (TextView) findViewById(R.id.lblSigoConectado);
        detener = (Button) findViewById(R.id.btnDetener);
        detener.setOnClickListener(getListenerDetener());
        regsitrarFiltros();
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

    @NonNull
    private View.OnClickListener getListerner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCamposValidos()){
                    servicio.putExtra(HOST,txtDirIp.getText().toString());
                    servicio.putExtra(PUERTO, txtPuerto.getText().toString());
                    startService(servicio);

                }

            }
        };
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
}
