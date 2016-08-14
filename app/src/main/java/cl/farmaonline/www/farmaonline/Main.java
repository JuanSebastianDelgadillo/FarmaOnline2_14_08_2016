package cl.farmaonline.www.farmaonline;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Main extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;
    double latGrua=-33.012961;
    double lngGrua=-71.541119;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        buscar_reg();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        miUbicacion();
        AgregarGruas();
    }

    public void buscar_reg(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"dbusuarios", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        if (db!=null){
            Cursor c = db.rawQuery("SELECT * FROM user_farma",null);
            if(c.moveToFirst()){
                Toast.makeText(getApplicationContext(),"Si hay registros",Toast.LENGTH_SHORT).show();
                Intent reg = new Intent(getApplicationContext(),ConexionMysqlHelp.class);
                startActivity(reg);
            }else{
                Toast.makeText(getApplicationContext(),"No hay registros",Toast.LENGTH_SHORT).show();
                Intent reg = new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(reg);

            }
        }

    }

    private void AgregarGruas(){
        LatLng grua = new LatLng(latGrua, lngGrua);
        mMap.addMarker(new MarkerOptions()
                .position(grua)
                .title("Farmacia cruz verde "+latGrua+" "+lngGrua)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_cverde))
               );
    }

    private void agregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Mi Ubicacion")
               );
        mMap.animateCamera(miUbicacion);
    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            agregarMarcador(lat, lng);
        }
    }

    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,locListener);
    }
}
