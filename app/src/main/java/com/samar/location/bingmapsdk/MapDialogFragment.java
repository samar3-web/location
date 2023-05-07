package com.samar.location.bingmapsdk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapImage;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapView;
import com.samar.location.R;
import com.samar.location.renthouse.AddHouseActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapDialogFragment extends DialogFragment {

    private MapView mapView;
    private FloatingActionButton mFab;
    private MapElementLayer mPinLayer;
    private MapImage mPinImage;
    private int mUntitledPushpinCount = 0;
    private Geopoint geopoint;
    private static final String MY_API_KEY = "AlwLTKgevIemLkhFY8wA2oDQwpxY8SBBAR8a5dXymXDFKTmfGWKkXnJGQkGzXUMM";
    public static final int ADDRESSES = 10;
    private List<Address> addressList;

    public MapDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the map view
        mapView = new MapView(requireContext(), MapRenderMode.VECTOR);
        mapView.setCredentialsKey(MY_API_KEY);
        mapView.onCreate(savedInstanceState);
        mPinLayer = new MapElementLayer();
        mapView.getLayers().add(mPinLayer);
        mPinImage = getPinImage();
        geopoint = mapView.getCenter();
        addPin(geopoint, "House");
        /*geopoint = mapView.getCenter();
        addPin(geopoint, "House");*/
/*
        // Set the map scene to show both locations
        Bundle args = getArguments();
        ParcelableGeopoint parcelableElementLocation = args.getParcelable("elementLocation");
        double elementLatitude = parcelableElementLocation.getLatitude();
        double elementLongitude = parcelableElementLocation.getLongitude();
        Geopoint elementLocation = new Geopoint(elementLatitude, elementLongitude);

        ParcelableGeopoint parcelableMyLocation = args.getParcelable("myLocation");
        double myLatitude = parcelableMyLocation.getLatitude();
        double myLongitude = parcelableMyLocation.getLongitude();
        Geopoint myLocation = new Geopoint(myLatitude, myLongitude);
        mapView.setScene(
                MapScene.createFromLocationAndZoomLevel(elementLocation, 15),
                MapAnimationKind.NONE);

        // Add pins for both locations
        mPinLayer = new MapElementLayer();
        mapView.getLayers().add(mPinLayer);
        mPinImage = getPinImage();
        addPin(elementLocation, "Restaurant");
        addPin(myLocation, "My location");*/


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*// Create a dialog with the map view
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mapView);
        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
        builder.setTitle(null);
        return builder.create();*/

        // Créer une instance de la vue du fragment
        View fragmentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_map_dialog, null);
        mapView = fragmentView.findViewById(R.id.map_view);
        mapView.setCredentialsKey(MY_API_KEY);
        mapView.onCreate(savedInstanceState);

        mPinLayer = new MapElementLayer();
        mapView.getLayers().add(mPinLayer);
        mPinImage = getPinImage();
        geopoint = mapView.getCenter();
        addPin(geopoint, "House");
       /* mapView.addOnMapLoadingStatusChangedListener(new OnMapLoadingStatusChangedListener() {
            @Override
            public boolean onMapLoadingStatusChanged(MapLoadingStatus mapLoadingStatus) {
                // Remove the previous pin
                mPinLayer.getElements().clear();

                // Add a new pin at the center of the map
                geopoint = mapView.getCenter();
                addPin(geopoint, "House");
                return false;
            }
        });*/
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPinLayer.getElements().clear();

                // Add a new pin at the center of the map
                geopoint = mapView.getCenter();
                addPin(geopoint, "House");
                return false;
            }
        });
        // Add a pin at the center of the map
        mPinLayer.getElements().clear();
        geopoint = mapView.getCenter();
        addPin(geopoint, "House");

       /* mapView.addOnMapDragListener(new OnMapDragListener() {
            @Override
            public boolean onMapDrag(MapDragEventArgs mapDragEventArgs) {
                // Remove the previous pin
                mPinLayer.getElements().clear();

                // Add a new pin at the center of the map
                geopoint = mapView.getCenter();
                addPin(geopoint, "House");
                return false;
            }
        });*/
/*        // Set the map scene to show both locations
        Bundle args = getArguments();
        ParcelableGeopoint parcelableElementLocation = args.getParcelable("elementLocation");
        double elementLatitude = parcelableElementLocation.getLatitude();
        double elementLongitude = parcelableElementLocation.getLongitude();
        Geopoint elementLocation = new Geopoint(elementLatitude, elementLongitude);

        ParcelableGeopoint parcelableMyLocation = args.getParcelable("myLocation");
        double myLatitude = parcelableMyLocation.getLatitude();
        double myLongitude = parcelableMyLocation.getLongitude();
        Geopoint myLocation = new Geopoint(myLatitude, myLongitude);
        mapView.setScene(
                MapScene.createFromLocationAndZoomLevel(elementLocation, 15),
                MapAnimationKind.NONE);

        // Add pins for both locations
        mPinLayer = new MapElementLayer();
        mapView.getLayers().add(mPinLayer);
        mPinImage = getPinImage();
        addPin(elementLocation, getString(R.string.destination));
        addPin(myLocation, getString(R.string.location));
       // FloatingActionButton fabDirections = fragmentView.findViewById(R.id.fab_directions);*/
        Button btn = fragmentView.findViewById(R.id.btn_directions);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Holding : "+geopoint.getPosition().getLatitude()+", "+geopoint.getPosition().getLongitude(),Toast.LENGTH_LONG).show();


                AddHouseActivity.houseNo.setVisibility(View.GONE);
                AddHouseActivity.street.setVisibility(View.GONE);
                AddHouseActivity.city.setVisibility(View.GONE);
                AddHouseActivity.post.setVisibility(View.GONE);
                AddHouseActivity.location.setVisibility(View.GONE);
                AddHouseActivity.latitude.setVisibility(View.GONE);
                AddHouseActivity.longitude.setVisibility(View.GONE);

                Geocoder geocoder = new Geocoder(getContext());
                try {
                    addressList = geocoder.getFromLocation(geopoint.getPosition().getLatitude(), geopoint.getPosition().getLongitude(), ADDRESSES);


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                String countryName = addressList.get(0).getCountryName();
                String houseNo = addressList.get(0).getAddressLine(0);
                String address = addressList.get(0).getAddressLine(1);
                String CountryCode = addressList.get(0).getCountryName();
                String adminArea = addressList.get(0).getAdminArea();
                String subAdminArea = addressList.get(0).getSubAdminArea();
                String locality = addressList.get(0).getLocality();
                String subLocality = addressList.get(0).getSubLocality();
                String postalCode = addressList.get(0).getPostalCode();
                String streetName = addressList.get(0).getThoroughfare();
                String latitude = String.valueOf(addressList.get(0).getLatitude());
                String longitude = String.valueOf(addressList.get(0).getLongitude());

                StringBuilder sb = new StringBuilder();
                sb.append("countryName: ").append(addressList.get(0).getCountryName()).append("\n")
                        .append("houseNo: ").append(addressList.get(0).getAddressLine(0)).append("\n")
                        .append("address: ").append(addressList.get(0).getAddressLine(1)).append("\n")
                        .append("CountryCode: ").append(addressList.get(0).getCountryCode()).append("\n")
                        .append("adminArea: ").append(addressList.get(0).getAdminArea()).append("\n")
                        .append("subAdminArea: ").append(addressList.get(0).getSubAdminArea()).append("\n")
                        .append("locality: ").append(addressList.get(0).getLocality()).append("\n")
                        .append("subLocality: ").append(addressList.get(0).getSubLocality()).append("\n")
                        .append("postalCode: ").append(addressList.get(0).getPostalCode()).append("\n")
                        .append("streetName: ").append(addressList.get(0).getThoroughfare()).append("\n");

                String addressString = sb.toString();
                System.out.println(addressString);

                AddHouseActivity.bingBtn.setText(houseNo+", "+address);
                /*AddHouseActivity.houseNo.setText(houseNo);
                AddHouseActivity.street.setText(subAdminArea);
                AddHouseActivity.city.setText(adminArea);
                AddHouseActivity.post.setText(postalCode);
                AddHouseActivity.location.setText(locality);
                AddHouseActivity.latitude.setText(latitude);
                AddHouseActivity.longitude.setText(longitude);*/
                if (houseNo != null) {
                    AddHouseActivity.houseNo.setVisibility(View.VISIBLE);
                    AddHouseActivity.houseNo.setText(houseNo);
                }
                if (subAdminArea != null) {
                    AddHouseActivity.street.setVisibility(View.VISIBLE);
                    AddHouseActivity.street.setText(subAdminArea);
                }
                if (adminArea != null) {
                    AddHouseActivity.city.setVisibility(View.VISIBLE);
                    AddHouseActivity.city.setText(adminArea);
                }
                if (postalCode != null) {
                    AddHouseActivity.post.setVisibility(View.VISIBLE);
                    AddHouseActivity.post.setText(postalCode);
                }
                if (locality != null) {
                    AddHouseActivity.location.setVisibility(View.VISIBLE);
                    AddHouseActivity.location.setText(locality);
                }
                if (latitude != null) {
                    AddHouseActivity.latitude.setVisibility(View.VISIBLE);
                    AddHouseActivity.latitude.setText(latitude);
                }
                if (longitude != null) {
                    AddHouseActivity.longitude.setVisibility(View.VISIBLE);
                    AddHouseActivity.longitude.setText(longitude);
                }




                dismiss();
            }
        });


        // Créer une nouvelle AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(fragmentView);
        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
       /* builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(getContext(),"Holding : "+geopoint.getPosition().getLatitude()+", "+geopoint.getPosition().getLongitude(),Toast.LENGTH_LONG).show();

            }
        });*/
        builder.setTitle(null);
        return builder.create();
    }

    private MapImage getPinImage() {
        // Create a pin image from a drawable resource
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pin, null);

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return new MapImage(bitmap);
    }

    private void addPin(Geopoint location, String title) {
        // Add a pin to the map at the given location
        MapIcon pushpin = new MapIcon();
        pushpin.setLocation(location);
        pushpin.setTitle(title);
        pushpin.setImage(mPinImage);

        pushpin.setNormalizedAnchorPoint(new PointF(0.5f, 1f));
        if (title.isEmpty()) {
            pushpin.setContentDescription(String.format(
                    Locale.ROOT,
                    "Untitled pushpin %d",
                    ++mUntitledPushpinCount));
        }
        mPinLayer.getElements().add(pushpin);
    }

}