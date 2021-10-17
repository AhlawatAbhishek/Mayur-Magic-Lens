package com.mayur.mayurmagiclens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private int reqCode = 1;
    private Bitmap bitmap;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Button clickBtn, searchResultBtn;
    private RecyclerView recyclerView;
    private RecycleSrchAdaptor recycleSrchAdaptor;
    private ArrayList<RecycleSearchModel> arrayList;
    String title, link, show_link, snippet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        imageView = (ImageView) findViewById(R.id.homeImg);
        progressBar = (ProgressBar) findViewById(R.id.homeProgBar);
        clickBtn = (Button) findViewById(R.id.homeBtnClick);
        searchResultBtn = (Button) findViewById(R.id.homeBtnSrchRes);
        recyclerView = (RecyclerView) findViewById(R.id.HomeRecycView);
        arrayList = new ArrayList<>();
        recycleSrchAdaptor = new RecycleSrchAdaptor(this, arrayList);
        recyclerView.setAdapter(recycleSrchAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
        clickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                recycleSrchAdaptor.notifyDataSetChanged();
                takePicIntent();
            }
        });
        searchResultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                recycleSrchAdaptor.notifyDataSetChanged();
                progressBar.setVisibility(View.VISIBLE);
                getRes();
            }
        });

    }

    private void getRes() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getCloudImageLabeler();
        labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                String srchQry = firebaseVisionImageLabels.get(0).getText();
                getSearchResults(srchQry);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeActivity.this, "Failed to identify object", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSearchResults(String srchQry) {
        String apiKey = "79c145a2fe79afbaf4a56087ffb9d811c66ccc0478a35598d2c49ac95f7150b2";
        String url = "https://serpapi.com/search.json?q=" + srchQry.trim() + "&location=Delhi,India&hl=en&gl=us&google_domain=google.com&api_key=" + apiKey;
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONArray orgArr = response.getJSONArray("organic_results");
                    for (int indx = 0; indx < orgArr.length(); indx++) {
                        JSONObject organicObj = orgArr.getJSONObject(indx);
                        if (organicObj.has("title")) {
                            title = organicObj.getString("title");
                        }
                        if (organicObj.has("link")) {
                            link = organicObj.getString("link");
                        }
                        if (organicObj.has("displayed_link")) {
                            show_link = organicObj.getString("displayed_link");
                        }
                        if (organicObj.has("snippet")) {
                            snippet = organicObj.getString("snippet");
                        }
                        arrayList.add(new RecycleSearchModel(title, link, show_link, snippet));
                    }
                    recycleSrchAdaptor.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(HomeActivity.this, "Not able to fetch Search Results", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (reqCode == requestCode && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    private void takePicIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, reqCode);
        }
    }
}