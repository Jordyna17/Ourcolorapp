package com.example.Ourcolorapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ImageView result;
    Bitmap bitmap;
    TextView rgbValue;
    TextView hexValue;
    TextView showcolor;
    ImageView color_display;
    String rgbcolor, hexcolor;

    RequestQueue queue;


    static final int REQUEST_IMAGE_CAPTURE = 1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button click = (Button)findViewById(R.id.camera);

        rgbValue = findViewById(R.id.rgbcolor);
        hexValue = findViewById(R.id.hexcolor);

        showcolor = findViewById(R.id.showcolor);

        color_display = findViewById(R.id.color_display);

       queue = Volley.newRequestQueue(this);


       // Button click2 = (Button)findViewById(R.id.button);

        result = (ImageView)findViewById(R.id.Camera);

        //click2.setOnClickListener(new View.OnClickListener() {

           // public void onClick(View v) {
             //   ShowColor(rgbcolor, hexcolor);
           // }

        //});


        result.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    final int action = event.getAction();
                    final int evX = (int) event.getX();
                    final int evY = (int) event.getY();

                    int touchColor = getColor(result, evX, evY);

                    int r = (touchColor >> 16) & 0xFF;
                    int g = (touchColor >> 8) & 0xFF;
                    int b = (touchColor >> 0) & 0xFF;

                    rgbcolor = String.valueOf(r) + "," + String.valueOf(g) + "," + String.valueOf(b) + ",";
                    rgbValue.setText("RBG: " + rgbcolor);

                    hexcolor = Integer.toHexString(touchColor);



                    if (hexcolor.length() > 2) {
                        hexcolor = hexcolor.substring(2, hexcolor.length());
                    }
                    if (action == MotionEvent.ACTION_UP) {
                        color_display.setBackgroundColor(touchColor);
                        hexValue.setText("HEX:     #" + hexcolor);
                    }

                    ShowColor(rgbcolor, hexcolor);

                } catch (Exception e) {

                }
                return true;
            }
        });


    }

    public void ShowColor(String rgb, String hex) {

        String url ="http://thecolorapi.com/id?hex=" + hex + "&rgb=" + rgb;
        Log.d("url_string",url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                public void onResponse(JSONObject response) {
                    try
                    {
                        JSONObject mainname = response.getJSONObject("name");


                        String color = mainname.getString("value");
                        Log.d("name_string", color.toString());

                        showcolor.setText(color);




                    } catch (Exception e) {
                        Log.d("error", e.getMessage());
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error1", error.getMessage());
                }
            }

            );

            //volley.getmInstance(MainActivity.this).addToRequestque(jsonObjectRequest);
            queue.add(jsonObjectRequest);
            //Log.d(jor.toString())

        }




    private int getColor(ImageView result, int evX, int evY) {
        result.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(result.getDrawingCache());
        result.setDrawingCacheEnabled(false);
        return bitmap.getPixel(evX, evY);
    }

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            result.setImageBitmap(imageBitmap);


        }


    }
}
