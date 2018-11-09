package quaeio.com.smartchat;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

/**
 * Created by serge.delossantos on 11/9/2018.
 */

public class CustomInfoWindow extends MarkerInfoWindow {
    public CustomInfoWindow(MapView mapView) {
        super(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, mapView);

        Button btn = (Button)(mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo));
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Button clicked", Toast.LENGTH_LONG).show();
            }
        });
    }
}
