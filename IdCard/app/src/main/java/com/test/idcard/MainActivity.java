package com.test.idcard;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;

    /* Intents permission requests codes */
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int CAMERA_REQUEST_CODE = 100;

    /* Files paths */
    public String currentPhotoPath;
    public String currentScannedCardPhotoPath;

    public Bitmap capturedImg;
    public Bitmap scannedCard;
    public Bitmap showImg;

    public File scannedCardImgFile;

    ImageView imageView;

    //Loading the OpenCV library onto the Android app
    static {
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "Loaded OpenCV successfully.");
        } else {
            Log.i(TAG, "OpenCV wasn't loaded successfully.");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionCheck();

        mContext = getApplicationContext();
        imageView = findViewById(R.id.img_result);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        /*  CAMERA INTENT  */
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                capturedImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(currentPhotoPath));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    /*  Creates the img file and starts the phone's camera,
        hence used only once when it's prompted */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null; // the img file
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                Log.e("CREATE FILE", "Error occurred while creating the image file");
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }


        }

    }

    /* Create a unique img file for the captured/chosen img */
    private File createImageFile() throws IOException {

        //Img file name must be unique
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPG_" + timeStamp + "_";

        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = "file:" + imageFile.getAbsolutePath();

        Log.d(TAG, "path : " + currentPhotoPath);

        return imageFile;
    }

    /* Create a unique img file for the scanned card img */
    private File createImageFileCardScanned(Context context) throws IOException {
        //Img file name must be unique
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPG_" + timeStamp + "SCANNED_CARD";

        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentScannedCardPhotoPath = "file:" + imageFile.getAbsolutePath();

        return imageFile;
    }


    /*  a method that writes a bitmap to a certain file, used for storing the scanned imgs */
    private File writeBitmapToFile(Bitmap bm, File theFile) throws IOException {

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bos);
        byte[] bitMapData = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(theFile);
        fos.write(bitMapData);
        fos.flush();
        fos.close();

        return theFile;
    }

    private void initImage(Mat tmp) {
        Bitmap bitmap = null;
        try {
            //Imgproc.cvtColor(seedsImage, tmp, Imgproc.COLOR_RGB2BGRA);
            bitmap = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tmp, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Glide.with(mContext)
                .load(bitmap)
                .into(imageView);
    }

    private void initImage(Bitmap bitmap) {
        Glide.with(mContext)
                .load(bitmap)
                .into(imageView);
    }

    private void permissionCheck() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onClickButton(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_camera:
                flushPaths(); //flush the imgs paths -if exist- for a better interacting app cycle
                dispatchTakePictureIntent();
                break;
            case R.id.btn_start:
                detectEdges(capturedImg);
                break;
            case R.id.btn_image:
                initImage(showImg);
                break;
        }

    }

    //for a better app cycle experience
    private void flushPaths() {
        currentPhotoPath = null;
        currentScannedCardPhotoPath = null;
    }

    private void detectEdges(Bitmap bitmap) {
        Mat rgba = new Mat();
        Utils.bitmapToMat(bitmap, rgba);

        Mat edges = new Mat(rgba.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.medianBlur(edges, edges,1);
        Imgproc.Canny(edges, edges, 50, 150);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);


        MatOfPoint2f cardContour = new MatOfPoint2f();


        boolean cardContourFound = false; //flag
        double maxPerimeter = getMaxPerimeter(contours);
        int counter = 3; //try for 3 times only in order not to fall into an infinite loop
        while (counter >= 0) {
            for (MatOfPoint c : contours) {
                double perimeter = Imgproc.arcLength(new MatOfPoint2f(c.toArray()), true);
                MatOfPoint2f approximatePolygon = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(c.toArray()), approximatePolygon, 0.02 * perimeter, true);

                if (approximatePolygon.toArray().length == 4) {

                    if (contours.toArray().length == 1) {

                        cardContour = approximatePolygon;
                        cardContourFound = true;
                        counter = -1;
                        break;

                    } else {
                        if (perimeter == maxPerimeter && maxPerimeter > 600) {
                            cardContour = approximatePolygon;
                            cardContourFound = true;
                            counter = -1;
                            break;
                        }
                    }
                }
            }

            if (!cardContourFound) {
                //fill the card's segments
                Mat filledEdges = new Mat();
                for (int i = 0; i < contours.toArray().length; i++) {
                    Imgproc.drawContours(edges, contours, i, new Scalar(255, 255, 255), Imgproc.FILLED);
                }

                //apply morphological close operation to let it have one contour only
                Mat Kernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(3, 3));
                Imgproc.morphologyEx(edges, filledEdges, Imgproc.MORPH_CLOSE, Kernel, new Point(-1, -1), 10);


                //find contours again after the close operation
                contours = new ArrayList<>();
                hierarchy = new Mat();
                Imgproc.findContours(filledEdges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

                maxPerimeter = getMaxPerimeter(contours);
                counter--;
            }

        }



        Bitmap resultBitmap = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(rgba, resultBitmap);
        showImg = resultBitmap;


    }


    private static double getMaxPerimeter(List<MatOfPoint> contours) {
        double maxPerimeter = Imgproc.arcLength(new MatOfPoint2f(contours.get(0).toArray()), true);
        if (contours.toArray().length > 1) {
            for (MatOfPoint c : contours) {
                double perimeter = Imgproc.arcLength(new MatOfPoint2f(c.toArray()), true);
                if (perimeter > maxPerimeter) {
                    maxPerimeter = perimeter;
                }
            }
        }
        return maxPerimeter;
    }


}


