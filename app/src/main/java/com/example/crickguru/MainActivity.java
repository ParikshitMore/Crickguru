package com.example.crickguru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9,img10;
    EditText team1,team2,team3,team4,team5,team6,team7,team8,team9,team10;
    EditText prediction1,prediction2,prediction3,prediction4,prediction5,prediction6,prediction7,prediction8,prediction9,
    prediction10;
    Button button1,btnUpload,next; EditText ed1;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    StorageReference storageReference;
    String sCurrentVersion,sLatestVersion;
    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference, demoRef;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST1 = 71; private final int PICK_IMAGE_REQUEST2 = 72;
    private final int PICK_IMAGE_REQUEST3 = 73; private final int PICK_IMAGE_REQUEST4 = 74;
    private final int PICK_IMAGE_REQUEST5 = 75; private final int PICK_IMAGE_REQUEST6 = 76;
    private final int PICK_IMAGE_REQUEST7 = 77; private final int PICK_IMAGE_REQUEST8 = 78;
    private final int PICK_IMAGE_REQUEST9 = 79; private final int PICK_IMAGE_REQUEST10 = 80;
    private AdView adView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
     // getSupportActionBar().hide();
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {}
        });

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
                        .build());

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        adView = findViewById(R.id.adView);

        // Create an ad request.
        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
////////////////////////////////////////////////////////////////////////// Adview /////////////////////////////////////

        // Get latest version from play store
        //new GetLatestVersion().execute();
        // Get current version
        sCurrentVersion=BuildConfig.VERSION_NAME;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        // final String passcode1=editText.getText().toString().trim();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String passc=dataSnapshot.child("Version").getValue().toString();
                if (sCurrentVersion.equals(passc))
                {
                  //  Intent i=new Intent(getApplicationContext(), MainActivity.class);
                  //  startActivity(i);
                }
                else if(!sCurrentVersion.equals(passc))
                {
                    updateAlertDialog();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        next=(Button) findViewById(R.id.next);

        team1= (EditText)findViewById(R.id.team1); team2 = (EditText)findViewById(R.id.team2);
        team3 = (EditText)findViewById(R.id.team3); team4= (EditText)findViewById(R.id.team4);
        team5= (EditText)findViewById(R.id.team5); team6= (EditText)findViewById(R.id.team6);
        team7= (EditText)findViewById(R.id.team7); team8= (EditText)findViewById(R.id.team8);
        team9= (EditText)findViewById(R.id.team9); team10= (EditText)findViewById(R.id.team10);

        img1 =(ImageView) findViewById(R.id.img1);img2=(ImageView) findViewById(R.id.img2);
        img3=(ImageView) findViewById(R.id.img3);img4=(ImageView) findViewById(R.id.img4);
        img5=(ImageView) findViewById(R.id.img5);img6=(ImageView) findViewById(R.id.img6);
        img7=(ImageView) findViewById(R.id.img7);img8=(ImageView) findViewById(R.id.img8);
        img9=(ImageView) findViewById(R.id.img9);img10=(ImageView) findViewById(R.id.img10);

        prediction1= (EditText)findViewById(R.id.prediction1); prediction2= (EditText)findViewById(R.id.prediction2);
        prediction3= (EditText)findViewById(R.id.prediction3); prediction4= (EditText)findViewById(R.id.prediction4);
        prediction5= (EditText)findViewById(R.id.prediction5);

        btnUpload = (Button) findViewById(R.id.btnUpload);

        storageReference = FirebaseStorage.getInstance().getReference();

        demoRef = FirebaseDatabase.getInstance().getReference().child("demo");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), Predictions.class);
                startActivity(i);
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               chooseImage1();
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage2();
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage3();
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage4();
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage5();
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage6();
            }
        });
        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage7();
            }
        });
        img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage8();
            }
        });
        img9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage9();
            }
        });
        img10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage10();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 private void updateAlertDialog() {
     // Initialize AlertDialog
     AlertDialog.Builder builder=new AlertDialog.Builder(this);
     // Set title
     builder.setTitle(getResources().getString(R.string.app_name));
     // set message
     builder.setMessage("Update Available");
     // Set non cancelable
     builder.setCancelable(false);

     // On update
     builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialogInterface, int i) {
             // Open play store
             startActivity(new Intent(Intent .ACTION_VIEW,
                     Uri.parse("market://details?id"+getPackageName())));
             // Dismiss alert dialog
             dialogInterface.dismiss();
         }
     });

     // on cancel
    // builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
   //      @Override
    //     public void onClick(DialogInterface dialogInterface, int i) {
    //         // cancel alert dialog
   //          dialogInterface.cancel();

  //            }
  //       });

          // show alert dialog
         builder.show();
    }
 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void chooseImage1() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST1);
    }
    private void chooseImage2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST2);
    }
    private void chooseImage3() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST3);
    }
    private void chooseImage4() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST4);
    }
    private void chooseImage5() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST5);
    }

    private void chooseImage6() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST6);
    }

    private void chooseImage7() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST7);
    }
    private void chooseImage8() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST8);
    }
    private void chooseImage9() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST9);
    }
    private void chooseImage10() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST10);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //img1
        if(requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img1.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        //img2
        if(requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img2.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        //img3
        if(requestCode == PICK_IMAGE_REQUEST3 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img3.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        //img4
        if(requestCode == PICK_IMAGE_REQUEST4 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img4.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        //img5
        if(requestCode == PICK_IMAGE_REQUEST5 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img5.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        //img6
        if(requestCode == PICK_IMAGE_REQUEST6 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img6.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        //img7
        if(requestCode == PICK_IMAGE_REQUEST7 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img7.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        //img8
        if(requestCode == PICK_IMAGE_REQUEST8 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img8.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        //img9
        if(requestCode == PICK_IMAGE_REQUEST9 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img9.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        //img10
        if(requestCode == PICK_IMAGE_REQUEST10 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img10.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadImage() {

        try {
            String value1 = team1.getText().toString(); demoRef.child("team1").setValue(value1);
            String value2 = team2.getText().toString(); demoRef.child("team2").setValue(value2);
            String value3 = team3.getText().toString(); demoRef.child("team3").setValue(value3);
            String value4 = team4.getText().toString(); demoRef.child("team4").setValue(value4);
            String value5 = team5.getText().toString(); demoRef.child("team5").setValue(value5);
            String value6 = team6.getText().toString(); demoRef.child("team6").setValue(value6);
            String value7 = team7.getText().toString(); demoRef.child("team7").setValue(value7);
            String value8 = team8.getText().toString(); demoRef.child("team8").setValue(value8);
            String value9 = team9.getText().toString(); demoRef.child("team9").setValue(value9);
            String value10 = team10.getText().toString();demoRef.child("team10").setValue(value10);

            String value11 = prediction1.getText().toString(); demoRef.child("prediction1").setValue(value11);
            String value12 = prediction2.getText().toString(); demoRef.child("prediction2").setValue(value12);
            String value13 = prediction3.getText().toString(); demoRef.child("prediction3").setValue(value13);
            String value14 = prediction4.getText().toString(); demoRef.child("prediction4").setValue(value14);
            String value15 = prediction5.getText().toString(); demoRef.child("prediction5").setValue(value15);

            if (filePath != null) {

                // Code for showing progressDialog while uploading
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

               // Defining the child of storageReference
                StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

                // adding listeners on upload
                // or failure of image
                ref.putFile(filePath)
                        .addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(
                                            UploadTask.TaskSnapshot taskSnapshot)
                                    {

                                        // Dismiss dialog
                                        progressDialog.dismiss();

                  Toast.makeText(MainActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                    //creating the upload object to store uploaded image details
                    Upload upload = new Upload( taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());

                    //adding an upload to firebase database
                    String uploadId = demoRef.push().getKey();
                    demoRef.child(uploadId).setValue(upload);

                                    }
                                })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {

                                // Error, Image not uploaded
                                progressDialog.dismiss();
                                Toast
                                        .makeText(MainActivity.this,
                                                "Failed " + e.getMessage(),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .addOnProgressListener(
                                new OnProgressListener<UploadTask.TaskSnapshot>() {

                                    // Progress Listener for loading
                                    // percentage on the dialog box
                                    @Override
                                    public void onProgress(
                                            UploadTask.TaskSnapshot taskSnapshot)
                                    {
                                        double progress
                                                = (100.0
                                                * taskSnapshot.getBytesTransferred()
                                                / taskSnapshot.getTotalByteCount());
                                        progressDialog.setMessage(
                                                "Uploaded "
                                                        + (int)progress + "%");
                                    }
                                });
            }
        }
        catch(Exception e)
        {
            e.getMessage();
        }
    }


}