package com.mobatia.naisapp.activities.bus_routes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.sports.model.BusModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;


import java.util.ArrayList;

/**
 * Created by Sona NR on 6/1/16.
 */
public class Location extends FragmentActivity implements LocationListener, URLConstants, JSONConstants,OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener{
    WebView mapView;
    RelativeLayout relativeHeader, mProgressRelLayout;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    Bundle extras;
    Context mContext = this;
    GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private GoogleMap mMap;
    ArrayList<BusModel> busModelArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_busroutes);
        initUi();

    }


    private void initUi() {
        extras=getIntent().getExtras();
        if(extras!=null){
            busModelArrayList =  (ArrayList)extras
                    .getSerializable("busModelArray");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(Location.this, "Pick up and drop off locations");
        headermanager.getHeader(relativeHeader, 0);
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AppUtils.hideKeyBoard(mContext);
                finish();
            }
        });
        home = headermanager.getLogoButton();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(Location.this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }





    /**
     * Called when the location has changed.
     * <p/>
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    @Override
    public void onLocationChanged(android.location.Location location) {
        location.getLatitude();
        location.getLongitude();
    }

    /**
     * Called when the provider status changes. This method is called when
     * a provider is unable to fetch a location or if the provider has recently
     * become available after a period of unavailability.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     * @param status   {@link LocationProvider#OUT_OF_SERVICE} if the
     *                 provider is out of service, and this is not expected to change in the
     *                 near future; {@link LocationProvider#TEMPORARILY_UNAVAILABLE} if
     *                 the provider is temporarily unavailable but is expected to be available
     *                 shortly; and  if the
     *                 provider is currently available.
     * @param extras   an optional Bundle which will contain provider specific
     *                 status variables.
     *                 <p/>
     *                 <p> A number of common key/value pairs for the extras Bundle are listed
     *                 below. Providers that use any of the keys on this list must
     *                 provide the corresponding value as described below.
     *                 <p/>
     *                 <ul>
     *                 <li> satellites - the number of satellites used to derive the fix
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Called when the provider is enabled by the user.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * Called when the provider is disabled by the user. If requestLocationUpdates
     * is called on an already disabled provider, this method is called
     * immediately.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderDisabled(String provider) {

    }




    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for(int i=0;i<busModelArrayList.size();i++) {
            LatLng latLng = new LatLng(Double.parseDouble(busModelArrayList.get(i).getLatitude()), Double.parseDouble(busModelArrayList.get(i).getLongitude()));

            //Adding marker to map
            if(i==0){
                mMap.addMarker(new MarkerOptions()
                        .position(latLng) //setting position
                        .draggable(true) //Making the marker draggable
                        .title(busModelArrayList.get(i).getRoute_name()).snippet("Start"));
            }else if(i==busModelArrayList.size()-1) {
                mMap.addMarker(new MarkerOptions()
                        .position(latLng) //setting position
                        .draggable(true) //Making the marker draggable
                        .title(busModelArrayList.get(i).getRoute_name()).snippet("Finish")); //Adding a title
            }else{
                mMap.addMarker(new MarkerOptions()
                        .position(latLng) //setting position
                        .draggable(true) //Making the marker draggable
                        .title(busModelArrayList.get(i).getRoute_name()));
            }

            //Moving the camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            //Animating the camera
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();

        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    public void getCheckGps() {
        if (AppUtils.checkInternet(mContext)) {
            String provider = Settings.Secure.getString(mContext.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (!provider.equals("")) {
                AppController.isProviderEnabled=true;
            } else {
                AppUtils.showDialogAlertGPS((Activity) mContext, "GPS Alert", "Please enable your location", R.drawable.gps, R.drawable.round);

            }
        }
    }


}
