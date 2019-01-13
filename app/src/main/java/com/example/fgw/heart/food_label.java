package com.example.fgw.heart;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class food_label extends AppCompatActivity {
    private ImageView imageView,imageView3;
    private Button buttonGallery, buttonCamera,buttonUpdate;
    private Uri file;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_label);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        FirebaseVisionLabelDetectorOptions options =
                new FirebaseVisionLabelDetectorOptions.Builder()
                        .setConfidenceThreshold(0.8f)
                        .build();
        textView = findViewById(R.id.textView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        buttonUpdate = (Button) findViewById(R.id.imageButton);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(food_label.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED){
                    allowpermissioncamera();

                }
                else {
                    takePhoto();}

            }
        });
    }

    private static File getOutputMediaFile () {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }

        }


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");}


        private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            file = Uri.fromFile(getOutputMediaFile());

            intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent,100);

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {


            file = data.getData();
            try {

                Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), file);

                imageView3.setImageBitmap(bitmap1);
                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap1);

                FirebaseVisionCloudLabelDetector detector = FirebaseVision.getInstance()
                        .getVisionCloudLabelDetector();
//                Task<List<FirebaseVisionLabel>> result =
//                        detector.detectInImage(image)
//                                .addOnSuccessListener(
//                                        new OnSuccessListener<List<FirebaseVisionLabel>>() {
//                                            @Override
//                                            public void onSuccess(List<FirebaseVisionLabel> labels) {
//                                                Toast.makeText(getApplicationContext(),"work", Toast.LENGTH_SHORT).show();
//
//                                                for (FirebaseVisionLabel label: labels) {
//                                                    String text = label.getLabel();
//                                                    String entityId = label.getEntityId();
//                                                    float confidence = label.getConfidence();
//                                                    textView.setText(text+entityId+confidence);
//                                                }
//
//                                            }
//                                        })
//                                .addOnFailureListener(
//                                        new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                // Task failed with an exception
//                                                // ...
//                                                Toast.makeText(getApplicationContext(),"not work", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
                Task<List<FirebaseVisionCloudLabel>> result =
                        detector.detectInImage(image)
                                .addOnSuccessListener(
                                        new OnSuccessListener<List<FirebaseVisionCloudLabel>>() {
                                            @Override
                                            public void onSuccess(List<FirebaseVisionCloudLabel> labels) {
                                                // Task completed successfully

                                                // ...
                                                for (FirebaseVisionCloudLabel label: labels) {
                                                    String text = label.getLabel();
                                                    String entityId = label.getEntityId();
                                                    float confidence = label.getConfidence();
                                                    textView.setText(text+entityId+confidence);
                                                    Toast.makeText(getApplicationContext(),text+entityId+confidence, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                            }
                                        });
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else {

                imageView3.setImageURI(file);
            FirebaseVisionImage image;
            try {
                image = FirebaseVisionImage.fromFilePath(getApplicationContext(), file);

                FirebaseVisionLabelDetector detector = FirebaseVision.getInstance()
                        .getVisionLabelDetector();
                Task<List<FirebaseVisionLabel>> result =
                        detector.detectInImage(image)
                                .addOnSuccessListener(
                                        new OnSuccessListener<List<FirebaseVisionLabel>>() {
                                            @Override
                                            public void onSuccess(List<FirebaseVisionLabel> labels) {
                                                Toast.makeText(getApplicationContext(),"work", Toast.LENGTH_SHORT).show();

                                                for (FirebaseVisionLabel label: labels) {
                                                    String text = label.getLabel();
                                                    String entityId = label.getEntityId();
                                                    float confidence = label.getConfidence();
                                                    textView.setText(text+entityId+confidence);
                                                }

                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                                Toast.makeText(getApplicationContext(),"not work", Toast.LENGTH_SHORT).show();
                                            }
                                        });
//                Task<List<FirebaseVisionCloudLabel>> result =
//                        detector.detectInImage(image)
//                                .addOnSuccessListener(
//                                        new OnSuccessListener<List<FirebaseVisionCloudLabel>>() {
//                                            @Override
//                                            public void onSuccess(List<FirebaseVisionCloudLabel> labels) {
//                                                // Task completed successfully
//
//                                                // ...
//                                                for (FirebaseVisionCloudLabel label: labels) {
//                                                    String text = label.getLabel();
//                                                    String entityId = label.getEntityId();
//                                                    float confidence = label.getConfidence();
//                                                    textView.setText(text+entityId+confidence);
//                                                    Toast.makeText(getApplicationContext(),text+entityId+confidence, Toast.LENGTH_SHORT).show();
//
//                                                }
//                                            }
//                                        })
//                                .addOnFailureListener(
//                                        new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                // Task failed with an exception
//                                                // ...
//                                            }
//                                        });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }




    private void allowpermissioncamera() {

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        }, 100);
    }
}
