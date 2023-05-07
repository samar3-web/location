package com.samar.location.bingmapsdk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
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

import java.util.Locale;

public class MapDialogFragment extends DialogFragment {

    private MapView mapView;
    private FloatingActionButton mFab;
    private MapElementLayer mPinLayer;
    private MapImage mPinImage;
    private int mUntitledPushpinCount = 0;
    private Geopoint geopoint;
    private static final String MY_API_KEY = "AlwLTKgevIemLkhFY8wA2oDQwpxY8SBBAR8a5dXymXDFKTmfGWKkXnJGQkGzXUMM";

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