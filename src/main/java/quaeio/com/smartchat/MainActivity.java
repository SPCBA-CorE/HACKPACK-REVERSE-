package quaeio.com.smartchat;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MapView map = null;
    LocationManager locationManager;
    MapController myMapController;
    ArrayList<OverlayItem> overlayItemArray;
    private Thread t;
    Road road = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        //startpoint
        GeoPoint startPoint = new GeoPoint(14.5869123, 121.0645714);
        GeoPoint babeQue = new GeoPoint(14.5878117, 121.0612649);
        GeoPoint lungHin = new GeoPoint(14.5875469,121.0630315);
        GeoPoint matgalNe = new GeoPoint(14.5870419,121.0621723);

        IMapController mapController = map.getController();
        mapController.setZoom(16);
        mapController.setCenter(lungHin);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

        Marker babeQueMarker = new Marker(map);
        babeQueMarker.setPosition(babeQue);
        babeQueMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(babeQueMarker);

        Marker lungHinMarker = new Marker(map);
        lungHinMarker.setPosition(lungHin);
        lungHinMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(lungHinMarker);

        Marker matgalNeMarker = new Marker(map);
        matgalNeMarker.setPosition(matgalNe);
        matgalNeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(matgalNeMarker);

        startMarker.setIcon(getResources().getDrawable(R.drawable.person));
        startMarker.setTitle("Start point");

        babeQueMarker.setIcon(getResources().getDrawable(R.drawable.person));
        babeQueMarker.setTitle("Start point");

        lungHinMarker.setIcon(getResources().getDrawable(R.drawable.person));
        lungHinMarker.setTitle("Start point");

        matgalNeMarker.setIcon(getResources().getDrawable(R.drawable.person));
        matgalNeMarker.setTitle("Start point");

        startMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                /*Toast.makeText(getApplicationContext(), "My current location is: "+ String.valueOf(marker.getPosition().getLongitude()+", "+String.valueOf(marker.getPosition().getLatitude())),
                        Toast.LENGTH_LONG).show();*/
                Intent intent = new Intent(MainActivity.this, BotActivity.class);
                startActivity(intent);

                return true;
            }
        });

        babeQueMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                /*Toast.makeText(getApplicationContext(), "My current location is: "+ String.valueOf(marker.getPosition().getLongitude()+", "+String.valueOf(marker.getPosition().getLatitude())),
                        Toast.LENGTH_LONG).show();*/
                Intent intent = new Intent(MainActivity.this, BotActivity.class);
                startActivity(intent);

                return true;
            }
        });

        lungHinMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                /*Toast.makeText(getApplicationContext(), "My current location is: "+ String.valueOf(marker.getPosition().getLongitude()+", "+String.valueOf(marker.getPosition().getLatitude())),
                        Toast.LENGTH_LONG).show();*/
                Intent intent = new Intent(MainActivity.this, BotActivity.class);
                startActivity(intent);

                return true;
            }
        });

        matgalNeMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                /*Toast.makeText(getApplicationContext(), "My current location is: "+ String.valueOf(marker.getPosition().getLongitude()+", "+String.valueOf(marker.getPosition().getLatitude())),
                        Toast.LENGTH_LONG).show();*/
                Intent intent = new Intent(MainActivity.this, BotActivity.class);
                startActivity(intent);

                return true;
            }
        });

        map.invalidate();
    }
}


