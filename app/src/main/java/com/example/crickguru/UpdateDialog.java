package com.example.crickguru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jsoup.Jsoup;

import java.io.IOException;

public class UpdateDialog extends AppCompatActivity {

    // Initialize variables
    TextView tvCurrentVersion,tvLatestVersion;
    String sCurrentVersion,sLatestVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatedialog);
        getSupportActionBar().hide();


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
                    Intent i=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
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

            }

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
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // cancel alert dialog
                    dialogInterface.cancel();

                }
            });

            // show alert dialog
            builder.show();
        }
    }