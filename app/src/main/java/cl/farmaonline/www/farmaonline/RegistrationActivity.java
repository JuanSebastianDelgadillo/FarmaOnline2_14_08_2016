package cl.farmaonline.www.farmaonline;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    EditText eTinsNombre;
    EditText eTinsApellido;
    EditText eTprefTel;
    EditText eTinsTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void agregarClient(View view){

        eTinsNombre = (EditText) findViewById(R.id.eTnombre);
        eTinsApellido = (EditText) findViewById(R.id.eTApellido);
        eTprefTel = (EditText) findViewById(R.id.eTpref_tel);
        eTinsTel = (EditText) findViewById(R.id.eTtelefono);

        String nombre = eTinsNombre.getText().toString();
        String apellido = eTinsApellido.getText().toString();
        String preTel = eTprefTel.getText().toString();
        String telefono = eTinsTel.getText().toString();

        ConexionSQLiteHelper base = new ConexionSQLiteHelper(this,"dbusuarios",null,1);
        SQLiteDatabase db = base.getReadableDatabase();
        if(db!=null){
            ContentValues registronuevo = new ContentValues();
            registronuevo.put("nom_user", nombre);
            registronuevo.put("ape_user", apellido);
            registronuevo.put("tel_user",preTel+""+telefono );
            long i = db.insert("user_farma",null,registronuevo);
            if(i>0){
                Toast.makeText(getApplicationContext(),"Agregado coorectamente",Toast.LENGTH_SHORT).show();
                Intent centro = new Intent(getApplicationContext(),Main.class);
                startActivity(centro);
            }
        }
    }
}
