package com.saiproject.sdcardaccess;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewSwitcher.ViewFactory {


    Button btnDownloadFolder;
    Button btnMusicFolder;
    Button btnDocumentFolder;
    Button btnRingtonesFolder;
    Button btnPodcastsFolder;
    Button btnMoviesFolder;
    Button btnAlarmsFolder;
    Button btnPicturesFolder;

    Button btnSaveFile;
    EditText edtValue;

    Button btnRetreive;
    TextView textValue;

    ImageView imgSai;


    Button btnAccessPictures;

    LinearLayout linearLayoutHorizontal;
    ImageSwitcher imageSwitcher;


    ArrayList<String> filePathNames;
    File[] files;




    public static final int REQUEST_CODE = 5981;     //Code for permission

/* Checks if Storage Permission is available or not */

    public boolean isStoragePermissionGranted(){


        if(Build.VERSION.SDK_INT >= 23) {
            /* If WRITE_EXTERNAL_STORAGE is granted then READ_EXTERNAL_STORAGE is also granted */

            //Storing our permissions in manifest.xml and read from here
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("LOG", "Permission is granted");
                return true;

            }
            /* Request permission since not granted */
            else {

                Log.v("LOG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);  //Requesting permission
                return false;
            }
        }

        else {  //Permission is automatically granted on SDK_INT < 23

            Log.v("LOG","Permission is granted");
            return true;
        }

        }



/* When Permission is requested */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED)            //grantResults[0] contains result for WRITE permission, if we can write read is automatically provided.
            Log.v("LOG","Permission" + permissions[0] + grantResults[0]);

}


/* Create a folder from Java Code */

    public File returnFolderInsideStorage(String directoryName, String folderName){


        File filepath = new File(Environment.getExternalStoragePublicDirectory(directoryName),folderName);  /* getExternalStoragePublicDirectory returns folder inside /storage/ */

        if(!filepath.mkdirs()) //The file is  can't be created
            Toast.makeText(this, "Can't create folder in this directory", Toast.LENGTH_SHORT).show();

        else{
            Toast.makeText(this, "Your folder is created " + folderName, Toast.LENGTH_SHORT).show();
        }

        return filepath;

    }


/* Stores files to Download Folder */

    public void storeFileToDownloadsFolder(){


        File filepath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"Newfile.txt");




            try{
                  FileOutputStream fileOutputStream = new FileOutputStream(filepath);   //FileOutputStream is an output stream to write data to files
                  OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream); //outputStreamWriter than writes on it
                  outputStreamWriter.append(edtValue.getText().toString());     // Write edtValue in the file
                  outputStreamWriter.close();           //Close outputstreamwriter and fileoutputstreamwriter
                  fileOutputStream.close();

                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                Log.i("LOG","saved");

             }

            catch(Exception e){


                    Log.i("LOG",e.toString());
                    Toast.makeText(this, "Exception occurred, check log for info", Toast.LENGTH_SHORT).show();
                }











    }


/* Retrieve files from Download Folder */

    public void retreiveFileFromDownloads(){

            File filepath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"Newfile.txt");

            try {


                FileInputStream fileInputStream = new FileInputStream(filepath);   // Create the inputStream
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream)); //InputStream reader reads from bytes to characters, buffered reader then stores in a buffer
                String fileData = "";
                String bufferData = "";

                // While EOL is not reached
                while((fileData = bufferedReader.readLine()) != null){

                    bufferData += fileData + "\n";

                }

                textValue.setText(bufferData);
                bufferedReader.close();
            }

            catch (Exception e){

                Log.i("LOG",e.toString());
                Toast.makeText(this, "Couldn't retreive file, check LOG", Toast.LENGTH_SHORT).show();

            }

    }

/* Save Image to Picture Folder in Folder */

    public void saveImagetoPictures(){

        try {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.sai);
            File filepath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Sai.png");
            OutputStream outputStream = new FileOutputStream(filepath);     //OutputStream is the super class of fileoutputstream and is abstract, so we can't directly instantiate it
            bm.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, "Image saved!", Toast.LENGTH_SHORT).show();
        }

        catch(Exception e) {

            Log.i("LOG", e.toString());
            Toast.makeText(this, "Can't save Picture! Check Log", Toast.LENGTH_SHORT).show();
        }


        }


/*  makeView method to implement ImageSwitcher */

    @Override
    public View makeView() {

        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new ImageSwitcher.LayoutParams(1000,1000));

        return imageView;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SDCARDChecker.isExternalStorageAvailable(this);

        isStoragePermissionGranted();

        /* Intialize */

        btnDownloadFolder = (Button) findViewById(R.id.btnDownloadDirectory);
        btnDocumentFolder = (Button) findViewById(R.id.btnDocumentsDirectory);
        btnMusicFolder = (Button) findViewById(R.id.btnMusicDirectory);
        btnRingtonesFolder = (Button) findViewById(R.id.btnRingToneDirectory);
        btnPodcastsFolder = (Button) findViewById(R.id.btnPodcastsDirectory);
        btnMoviesFolder = (Button) findViewById(R.id.btnMoviesDirectory);
        btnAlarmsFolder = (Button) findViewById(R.id.btnAlarmsDirectory);
        btnPicturesFolder = (Button) findViewById(R.id.btnPictureDirectory);



        btnSaveFile = (Button) findViewById(R.id.btnSaveFile);
        edtValue = (EditText) findViewById(R.id.edtValue);

        btnRetreive = (Button) findViewById(R.id.btnRetreive);
        textValue = (TextView) findViewById(R.id.txtValue);

        imgSai = (ImageView) findViewById(R.id.imgSai);

        btnAccessPictures = (Button) findViewById(R.id.btnAllowAccess);

        linearLayoutHorizontal = (LinearLayout) findViewById(R.id.linearLayoutHorizontal);
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);


        imageSwitcher.setFactory(this);             // This is needed to implement makeView and derived from ViewSwitcher.ViewFactory

        //Setting Animation
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right));





        btnAccessPictures.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {

                                                     if (isStoragePermissionGranted()) {

                                                         filePathNames = new ArrayList<String>();

                                                         String parent = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                                                         String child = "AnimalImages";
                                                         File filepath = new File(parent, child);


                                                         if (filepath.isDirectory() && filepath != null) {


                                                             files = filepath.listFiles();       //List all the files inside AnimalImages

                                                             for (File file : files) {

                                                                 filePathNames.add(file.getAbsolutePath()); //Get absolute path of the file in files

                                                             }


                                                             for (final String filepathName : filePathNames) {


                                                                 final ImageView imageView = new ImageView(MainActivity.this);
                                                                 imageView.setImageURI(Uri.parse(filepathName));         // setImageURI is reads image from SD Card.
                                                                 imageView.setLayoutParams(new LinearLayout.LayoutParams(500, 500));


                                                                 imageView.setOnClickListener(new View.OnClickListener() {
                                                                     @Override
                                                                     public void onClick(View v) {

                                                                         imageSwitcher.setImageURI(Uri.parse(filepathName));
                                                                     }
                                                                 });


                                                                 linearLayoutHorizontal.addView(imageView); //Add image to linearLayout horizontal
                                                             }

                                                         }
                                                     }
                                                 }
                                             });


        btnRetreive.setOnClickListener(this);
        btnAlarmsFolder.setOnClickListener(this);
        btnSaveFile.setOnClickListener(this);
        btnPicturesFolder.setOnClickListener(this);
        btnMoviesFolder.setOnClickListener(this);
        btnMusicFolder.setOnClickListener(this);
        btnDownloadFolder.setOnClickListener(this);
        btnDocumentFolder.setOnClickListener(this);
        btnPodcastsFolder.setOnClickListener(this);
        btnRingtonesFolder.setOnClickListener(this);

        imgSai.setOnClickListener(this);



    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {

        switch(v.getId()){


            case R.id.btnDownloadDirectory:

                returnFolderInsideStorage(Environment.DIRECTORY_DOWNLOADS,"Fav Downloads");
                break;


            case R.id.btnDocumentsDirectory:

                returnFolderInsideStorage(Environment.DIRECTORY_DOCUMENTS,"Fav Documents");
                break;


            case R.id.btnRingToneDirectory:

                returnFolderInsideStorage(Environment.DIRECTORY_RINGTONES,"Fav Ringtones");
                break;

            case R.id.btnAlarmsDirectory:

                returnFolderInsideStorage(Environment.DIRECTORY_ALARMS,"Important Alarms");
                break;


            case R.id.btnMoviesDirectory:

                returnFolderInsideStorage(Environment.DIRECTORY_MOVIES,"fAV Movies");
                break;

            case R.id.btnMusicDirectory:

                returnFolderInsideStorage(Environment.DIRECTORY_MUSIC,"fAV Music");
                break;

            case R.id.btnPictureDirectory:

                returnFolderInsideStorage(Environment.DIRECTORY_PICTURES,"fAV Pictures");
                break;

            case R.id.btnPodcastsDirectory:

                    returnFolderInsideStorage(Environment.DIRECTORY_PODCASTS,"fAV Podcasts");
                break;


            case R.id.btnSaveFile:

                    storeFileToDownloadsFolder();
                    break;


            case R.id.btnRetreive:

                    retreiveFileFromDownloads();
                    break;

            case R.id.imgSai:

                    saveImagetoPictures();
                    break;






        }
    }
}
