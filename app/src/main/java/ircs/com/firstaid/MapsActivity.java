package ircs.com.firstaid;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final String TAG = getClass().getSimpleName();
    private GoogleMap mMap;
    private LocationManager locationManager;
    private Location loc;
    static Snackbar s;
    boolean isInFront;
    private ProgressDialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        currentLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Delhi and move the camera
        LatLng delhi = new LatLng(28.6139, 77.2090);

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(delhi));
    }

    private class GetPlaces extends AsyncTask<Void, Void, ArrayList<place>> {

        private ProgressDialog dialog;
        private Context context;
        private String places;

        public GetPlaces(Context context, String places) {
            this.context = context;
            this.places = places;
        }

        @Override
        protected void onPostExecute(ArrayList<place> result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            mMap.addMarker(new MarkerOptions().title("Your Location").
                    position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            try {
                for (int i = 0; i < result.size(); i++) {
                    mMap.addMarker(new MarkerOptions()
                            .title(result.get(i).getName())
                            .position(
                                    new LatLng(result.get(i).getLatitude(), result
                                            .get(i).getLongitude()))
                            .snippet(result.get(i).getVicinity()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(loc.getLatitude(), loc.getLongitude())) // Sets the center of the map to
                    // Mountain View
                    .zoom(13) // Sets the zoom
                    .tilt(30) // Sets the tilt of the camera to 30 degrees
                    .build(); // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override
        protected ArrayList<place> doInBackground(Void... arg0) {
            PlacesService service = new PlacesService("AIzaSyBcKckh-rMxnH0KXmek_MygitkEvBSXpLo");
            ArrayList<place> findPlaces = (ArrayList<place>) service.findPlaces(loc.getLatitude(), // 28.6475944
                    loc.getLongitude()); // 77.09326
            return findPlaces;
        }
    }

    private void currentLocation() {
        System.out.println("Inside1");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        String provider = locationManager
                .getBestProvider(new Criteria(), false);

        Location location = locationManager.getLastKnownLocation(provider);

        if (location == null) {
                locationManager.requestLocationUpdates(provider, 0, 0, listener);
            s = Snackbar.make(this.findViewById(android.R.id.content), "Location Fetching...", Snackbar.LENGTH_INDEFINITE);
//            s.show();
            dialog1 = new ProgressDialog(this);
            dialog1.setCancelable(false);
            dialog1.setMessage("Fetching Location..");
            dialog1.isIndeterminate();
            dialog1.show();
            System.out.println("Inside3");
        } else {
            System.out.println("Inside2");
            loc = location;
            new GetPlaces(MapsActivity.this,
                    "hospitals").execute();
            Log.e(TAG, "location : " + location);
        }
    }

    private LocationListener listener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "location update : " + location);
            loc = location;
            locationManager.removeUpdates(listener);
            if (dialog1.isShowing()) {
                dialog1.dismiss();
            }
            if(isInFront){
                new GetPlaces(MapsActivity.this,
                        "hospitals").execute();
            }

        }
    };
    @Override
    public void onResume() {
        super.onResume();
        isInFront = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isInFront = false;
    }
}

