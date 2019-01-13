package com.example.fgw.heart;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class openn extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    Mat mat1, mat2, mat3;
    private CascadeClassifier cascadeClassifier;
    private Mat grayscaleImage;
    private int absoluteLogoSize;
    CameraBridgeViewBase cameraBridgeViewBase;
    public static openn parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openn);

        if(OpenCVLoader.initDebug())
        {
            Toast.makeText(getApplicationContext(),"oPENCV LOADED",Toast.LENGTH_LONG).show();
        }
        cameraBridgeViewBase = findViewById(R.id.camera);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);
        cameraBridgeViewBase.enableView();
        Assetbridge.unpack(this);
    }

    //Initialise OpenCV dependencies before we do anything
    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    initializeOpenCVDependencies();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    private void initializeOpenCVDependencies() {

        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = getResources().openRawResource(R.raw.object);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "cascade.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[789096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
                Log.e("path", buffer.toString());

            }
            is.close();
            os.close();

            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            Log.e("path", cascadeClassifier.toString());
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }

        // Initialise the camera view
        cameraBridgeViewBase.enableView();
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        mat1 = new Mat(width,height, CvType.CV_8UC4);
//        mat2 = new Mat(width,height, CvType.CV_8UC4);
//        mat3 = new Mat(width,height, CvType.CV_8UC4);
        grayscaleImage = new Mat(height, width, CvType.CV_8UC4);

        absoluteLogoSize = (int) (height* 0.8);// * 0.2
    }

    @Override
    public void onCameraViewStopped() {
        mat1.release();
        mat2.release();
        mat3.release();
        grayscaleImage.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mat1= inputFrame.rgba();

        Imgproc.cvtColor(mat1, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
        MatOfRect burger_king = new MatOfRect();
        //Toast.makeText(getApplicationContext(), burger_king.toString(),Toast.LENGTH_SHORT).show();
        //Imgproc.rectangle(mat1, new Point(100,100), new Point(500,500),new Scalar(0,0,0,255),2);

        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, burger_king, 1.1,3,3,
                    new Size(30, 30), new Size());
            Log.e("CASCADE", "DETECTED");
        }else
        {
            Log.e("CASCADE", "NOT DETECTED");
        }

        final Rect[] burger_kingArray = burger_king.toArray();
        for (int i = 0; i <burger_kingArray.length; i++){
            double xd1 = burger_kingArray[i].tl().x;
            double yd1 = burger_kingArray[i].tl().y;
            double xd2 = burger_kingArray[i].br().x;
            double yd2 = burger_kingArray[i].br().y;
            int ixd1 = (int) xd1;
            int iyd1 = (int) yd1;
            int ixd2 = (int) xd2;
            int iyd2 = (int) yd2;



            // Create a rectangle around it
            Imgproc.rectangle(mat1, burger_kingArray[i].tl(), burger_kingArray[i].br(), new Scalar(0, 0, 0, 255), 3);


            //Toast.makeText(getApplicationContext(),burger_kingArray[i].tl().toString() + burger_kingArray[i].br() + "LASSLSLSLASLs;a,d.as';za;", Toast.LENGTH_SHORT).show();

            Rect roi = new Rect(ixd1, iyd1, ixd2 - ixd1, iyd2 - iyd1);
//        Core.transpose(mat1,mat2);
//        Imgproc.resize(mat2,mat3,mat3.size(),0,0,0);
//        Core.flip(mat3,mat1,1);

    }
        Imgproc.rectangle(mat1, new Point(400,500), new Point(100,100), new Scalar(255, 0, 0, 0), 2);
//        Imgproc.rectangle(mat1, new Point(700,100), new Point(100,150), new Scalar(0, 0, 0, 255), 1);
//        Imgproc.rectangle(mat1, new Point(800,100), new Point(500,550), new Scalar(0, 0, 0, 255), 1);
//        Imgproc.rectangle(mat1, new Point(900,100), new Point(900,350), new Scalar(0, 0, 0, 255), 1);
        return mat1;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"not woking",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
    }
}
