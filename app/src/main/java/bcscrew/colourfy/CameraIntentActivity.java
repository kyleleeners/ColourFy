package bcscrew.colourfy;


import android.graphics.Color;
import android.R.color;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class CameraIntentActivity extends AppCompatActivity  {

    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private static final int CAMERA_RESULT = 1;
    private ImageView mPhotoCapturedImageView;
    private TextView textSource;
    private static Uri capturedImageUri = null;

    private Bitmap bitmapMaster;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // start app in camera mode
        takePhoto(null);

        // use default camera layout
        setContentView(R.layout.activity_camera_intent);
        textSource = (TextView)findViewById(R.id.sourceuri);

        // pull up the photo
        mPhotoCapturedImageView = (ImageView) findViewById(R.id.capturePhotoImageView);

        // allows for touch
        mPhotoCapturedImageView.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // fields for cursor location
                int action = event.getAction();
                int x = (int) event.getX();
                int y = (int) event.getY();

                 //set background colour of text box to current colour touching, and print colour name
                switch(action){
                    case MotionEvent.ACTION_DOWN:
                        textSource.setBackgroundColor(
                                getPictureColor((ImageView)v, bitmapMaster, x, y));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        textSource.setBackgroundColor(
                                getPictureColor((ImageView)v, bitmapMaster, x, y));
                        break;
                    case MotionEvent.ACTION_UP:
                        textSource.setBackgroundColor(
                                getPictureColor((ImageView)v, bitmapMaster, x, y));
                        break;
                }
                return true;
            }

            private int getPictureColor(ImageView view, Bitmap bm, int x, int y) {

                // pressing off the screen will not cause application to crash
                if (x < 0 || y < 0 || x > view.getWidth() || y > view.getHeight()) {
                    return color.background_light;
                }
                else {

                    // fit current coords to bitmap, ratio of bm/view
                    int projectedX = (int)((double)x * ((double)bm.getWidth()/(double)view.getWidth()));
                    int projectedY = (int)((double)y * ((double)bm.getHeight()/(double)view.getHeight()));

                    // get information from bitmap at cursor location
                    int pixel = bm.getPixel(projectedX, projectedY);

                    // set name of colour to cursor location
                    textSource.setText(ColorName.getColourName(pixel));

                    // create colour object that will be pasted on top and bottom of screen
                    int red = Color.red(pixel);
                    int blue = Color.blue(pixel);
                    int green = Color.green(pixel);
                    int alpha = Color.alpha(pixel);

                    return Color.argb(alpha, red, green, blue);
                }
            }
        });
    }

    public void takePhoto(View view) {

        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {
            // get a bit map from data and set it as the content of current view
            Bundle extras = data.getExtras();
            bitmapMaster = (Bitmap) extras.get("data"); //displaying bitmap thumbnail --> bad quality
            mPhotoCapturedImageView.setImageBitmap(bitmapMaster);
        }
    }
}
