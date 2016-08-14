package cl.farmaonline.www.farmaonline;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexionMysqlHelp extends AppCompatActivity {

    Button btnconsultar, btGuardar;
    EditText id, tipo, ciudad, lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_mysql_help);

        btnconsultar =(Button) findViewById(R.id.btBuscar);
        btGuardar = (Button) findViewById(R.id.btGuardar);
        id = (EditText) findViewById(R.id.etId);
        tipo = (EditText) findViewById(R.id.etTip);
        ciudad = (EditText) findViewById(R.id.etCiu);
        lat = (EditText) findViewById(R.id.etLat);
        lng = (EditText) findViewById(R.id.etLng);

        btnconsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConsultarDatos().execute("http://www.webinfo.cl/farmaOnline/consulta.php?id="+id.getText().toString());
            }
        });

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new cargarDatos().execute("http://www.webinfo.cl/farmaOnline/registro.php?nombres="+lat.getText().toString()+"&telefono="+lng.getText().toString());
            }
        });
    }

    private class cargarDatos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(),"Se almaceno en MYSQL correctamente",Toast.LENGTH_SHORT).show();
            lat.setText("");
            lng.setText("");

        }
    }

    private class ConsultarDatos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        public void onPostExecute(String result) {

            JSONArray jsn = null;

            try {
                jsn = new JSONArray(result);
                tipo.setText(jsn.getString(1));
                ciudad.setText(jsn.getString(2));
                lat.setText(jsn.getString(3));
                lng.setText(jsn.getString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private String downloadUrl(String myurl) throws IOException {
        Log.i("URL",""+myurl);
        myurl = myurl.replace(" ","%20");
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("respuesta", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }



}
