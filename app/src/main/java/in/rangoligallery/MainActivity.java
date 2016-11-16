package in.rangoligallery;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.GalleryAdapter;
import app.AppController;
import model.Image;
import utils.ConnectionDetector;
import utils.MatrialAlertDialogHelper;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    // private static final String endpoint = "http://api.androidhive.info/json/glide.json";
    private static final String endpoint = "http://203.115.81.42/Dg/readdata.php";
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    private InterstitialAd interstitial = null;
    private ImageView flipkartImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getResources().getString(
                R.string.Rangoli_Interstitial_LiveId));
        interstitial.setAdListener(myAdlistener);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(
                AdRequest.DEVICE_ID_EMULATOR).build();
        interstitial.loadAd(adRequest);

        flipkartImage = (ImageView) findViewById(R.id.FlipkartImage);


        flipkartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uriUrl = Uri.parse("https://dl.flipkart.com/dl/offers?affid=prijechgm");
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                    startActivity(launchBrowser);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request." + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        if (ConnectionDetector.isConnectingToInternet(MainActivity.this)) {
            fetchImages();
        } else {
            MatrialAlertDialogHelper
                    .confirmDialog(
                            "No Internet Connection",
                            "It looks like your internet connection is off. Please turn it on and try again",
                            MainActivity.this, MainActivity.class,
                            MatrialAlertDialogHelper.CONFIRM_FINISH, "OK");

        }
    }

    private void fetchImages() {

        pDialog.setMessage("Loading Rangoli...");
        pDialog.setCancelable(false);
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(endpoint,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();

                        images.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Image image = new Image();
                                // JSONObject url = object.getJSONObject("url");

                                // image.setName(object.getString("name"));
                                image.setSmall(object.getString("url"));
                                image.setMedium(object.getString("url"));
                                image.setLarge(object.getString("url"));
                                // image.setTimestamp(object.getString("timestamp"));

                                images.add(image);

                            } catch (JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    /* AdMob Interstitial Ad Listener */
    private AdListener myAdlistener = new AdListener() {

        public void onAdOpened() {
        }

        @Override
        public void onAdLoaded() {
            if (interstitial.isLoaded()) {
                interstitial.show();
            }
        }

        public void onAdFailedToLoad(int errorCode) {
        }

    };
    /* End */

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}