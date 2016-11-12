package trainner.soa.com.sensoresmanager;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TabHost TbH;
    private Button empezar;
    private Boolean presionado = Boolean.FALSE;
    private TextView txtEstado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        empezar = (Button) findViewById(R.id.btnEmpezar);
        txtEstado = (TextView) findViewById(R.id.txtEstado);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("");
        //setSupportActionBar(toolbar);

        crearTabs();

        empezar.setOnClickListener(getListerner());


    }

    @NonNull
    private View.OnClickListener getListerner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtEstado.setText("estado");
            }
        };
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
}
