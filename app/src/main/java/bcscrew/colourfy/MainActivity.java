package bcscrew.colourfy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import bcscrew.colourfy.R;
import io.fotoapparat.Fotoapparat;
import io.fotoapparat.FotoapparatSwitcher;
import io.fotoapparat.error.CameraErrorCallback;
import io.fotoapparat.hardware.CameraException;
import io.fotoapparat.parameter.LensPosition;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.photo.BitmapPhoto;
import io.fotoapparat.result.PendingResult;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.view.CameraView;

import static io.fotoapparat.log.Loggers.fileLogger;
import static io.fotoapparat.log.Loggers.logcat;
import static io.fotoapparat.log.Loggers.loggers;
import static io.fotoapparat.parameter.selector.AspectRatioSelectors.standardRatio;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.autoFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.continuousFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.fixed;
import static io.fotoapparat.parameter.selector.LensPositionSelectors.lensPosition;
import static io.fotoapparat.parameter.selector.Selectors.firstAvailable;
import static io.fotoapparat.parameter.selector.SizeSelectors.biggestSize;

public class MainActivity extends AppCompatActivity {

    private final PermissionsDelegate permissionsDelegate = new PermissionsDelegate(this);
    private boolean hasCameraPermission;
    private CameraView cameraView;
    private TextView textView;
    private View takePictureButton;
    private View hidePictureButton;
    private FotoapparatSwitcher fotoapparatSwitcher;

    private Fotoapparat backFotoapparat;
    private PhotoResult photoResult;

    /*
    start app with cameraView as main activity,
    initialize Fotoapparat (easy pictures)
    enable focusing on touch of cameraView,
    enable taking pictures and hiding pictures (not visible)
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraView = (CameraView) findViewById(R.id.camera_view);
        hasCameraPermission = permissionsDelegate.hasCameraPermission();

        if (hasCameraPermission) {
            cameraView.setVisibility(View.VISIBLE);
        } else {
            permissionsDelegate.requestCameraPermission();
        }

        hidePictureButton = findViewById(R.id.hidePicture);
        hidePictureButton.setVisibility(View.GONE);

        setupFotoapparat();

        focusOnClick();
        takePictureOnClick();
        hidePictureOnClick();
    }

    // initialize Fotoapparat, call constructor
    private void setupFotoapparat() {
        backFotoapparat = createFotoapparat(LensPosition.BACK);
        fotoapparatSwitcher = FotoapparatSwitcher.withDefault(backFotoapparat);
    }

    // Fotoapparat constructor
    private Fotoapparat createFotoapparat(LensPosition position) {
        return Fotoapparat
                .with(this)
                .into(cameraView)
                .previewScaleType(ScaleType.CENTER_CROP)
                .photoSize(standardRatio(biggestSize()))
                .lensPosition(lensPosition(position))
                .focusMode(firstAvailable(
                        continuousFocus(),
                        autoFocus(),
                        fixed()
                ))
                .logger(loggers(
                        logcat(),
                        fileLogger(this)
                ))
                .cameraErrorCallback(new CameraErrorCallback() {
                    @Override
                    public void onError(CameraException e) {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                })
                .build();
    }

    /*
    Initialize hidePictureButton and call hide picture,
    set visibility of hidePictureButton to gone,
    set visibility of takePictureButton to visible
     */
    private void hidePictureOnClick() {
        hidePictureButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePicture();
                hidePictureButton.setVisibility(View.GONE);
                cameraView.setVisibility(View.VISIBLE);
                takePictureButton.setVisibility(View.VISIBLE);
            }
        });
    }

    // remove resultView (bitmap) and textView (colour name)
    private void hidePicture() {
        ImageView imageView = (ImageView) findViewById(R.id.result);
        if (imageView.getDrawable() != null) {
            imageView.setImageResource(0);
            textView.setVisibility(View.GONE);
        }
    }

    /*
    Initialize takePictureButton and call take picture,
    set visibility of takePictureButton to gone,
    set visibility of hidePictureButton to visible
     */
    private void takePictureOnClick() {
        takePictureButton = findViewById(R.id.takePicture);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                takePictureButton.setVisibility(View.GONE);
                cameraView.setVisibility(View.GONE);
                hidePictureButton.setVisibility(View.VISIBLE);
            }
        });
    }

    /*
    Take and save photo to path, when photo has been saved
    convert to bitmap and rotate to fit screen properly,
    call colourOnTouch with rotated bitmap
     */
    private void takePicture() {
        photoResult = fotoapparatSwitcher.getCurrentFotoapparat().takePicture();
        final File path = new File(getExternalFilesDir("photos"),"photo.jpg");
        photoResult.saveToFile(path);
        photoResult
                .toBitmap()
                .whenAvailable(new PendingResult.Callback<BitmapPhoto>() {
                    @Override
                    public void onResult(BitmapPhoto result) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeFile(path.toString(), options);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        final Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0,
                                bitmap.getWidth(), bitmap.getHeight(),
                                matrix, true);
                        colourOnTouch(rotated);
                    }
                });
    }

    // set onTouchListener for this bitmap
    private void colourOnTouch(final Bitmap bitmap) {
        ImageView imageView = (ImageView) findViewById(R.id.result);
        imageView.setImageBitmap(bitmap);
        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                textView = (TextView) findViewById(R.id.colourName);

                // fields for cursor location
                int action = event.getAction();
                int x = (int) event.getX();
                int y = (int) event.getY();

                //set background colour of text box to current colour touching, and print colour name
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        textView.setBackgroundColor(
                                getPictureColor((ImageView) v, bitmap, x, y));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        textView.setBackgroundColor(
                                getPictureColor((ImageView) v, bitmap, x, y));
                        break;
                    case MotionEvent.ACTION_UP:
                        textView.setBackgroundColor(
                                getPictureColor((ImageView) v, bitmap, x, y));
                        break;
                }
                return true;
            }

            private int getPictureColor(ImageView view, Bitmap bm, int x, int y) {

                // pressing off the screen will not cause application to crash
                if (x < 0 || y < 0 || x > view.getWidth() || y > view.getHeight()) {
                    return android.R.color.background_light;
                } else {

                    // fit current coords to bitmap, ratio of bm/view
                    int projectedX = (int) ((double) x * ((double) bm.getWidth() / (double) view.getWidth()));
                    int projectedY = (int) ((double) y * ((double) bm.getHeight() / (double) view.getHeight()));

                    // get information from bitmap at cursor location
                    int pixel = bm.getPixel(projectedX, projectedY);

                    // set name of colour to cursor location
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(ColorName.getColourName(pixel));

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

    private void focusOnClick() {
        cameraView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    default: fotoapparatSwitcher.getCurrentFotoapparat().autoFocus();
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (hasCameraPermission) {
            fotoapparatSwitcher.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (hasCameraPermission) {
            fotoapparatSwitcher.stop();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsDelegate.resultGranted(requestCode, permissions, grantResults)) {
            fotoapparatSwitcher.start();
            cameraView.setVisibility(View.VISIBLE);
        }
    }
}