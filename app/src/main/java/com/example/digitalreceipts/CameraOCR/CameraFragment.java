package com.example.digitalreceipts.CameraOCR;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.digitalreceipts.Database.ReceiptsManager;
import com.example.digitalreceipts.MainActivity.ReceiptsRoom;
import com.example.digitalreceipts.R;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CameraFragment extends DialogFragment {
    private static final int GALLERY_REQUEST = 2888;
    private static final int CAMERA_REQUEST = 1888;
    public ReceiptsManager receiptsManager;
    private static final int MY_CAMERA_PERMISSION_CODE = 6604;
    private int STORAGE_PERMISSION_CODE = 1;

    TextView receiptDisplay;
    Button galleryButton;
    Button cameraButton;
    Button retrieveAPI;
    Uri imageUri;
    TBApi tabscannerapi;
    ImageView imageView;
    Uri tempCameraUri;
    File tempFile;

    public CameraFragment() {
    }

    public static CameraFragment newInstance(String title) {
        CameraFragment frag = new CameraFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_camera, container, false);
        receiptDisplay = rootView.findViewById(R.id.receiptDisplay);
        galleryButton = rootView.findViewById(R.id.galleryButton);
        cameraButton = rootView.findViewById(R.id.cameraButton);
        imageView = rootView.findViewById(R.id.gif_loading);
        imageView.setBackgroundResource(R.drawable.app_icon);
        receiptsManager = ViewModelProviders.of(this).get(ReceiptsManager.class);
        receiptsManager.getAllReceipts().observe(this, new Observer<List<ReceiptsRoom>>() {
            @Override
            public void onChanged(List<ReceiptsRoom> receiptsRooms) {
                // for recycleview when we want to display data. currently only showing data
                Toast.makeText(getContext(), "DB manipulated", Toast.LENGTH_SHORT).show();
            }
        });

        tabscannerapi = TBApi.getInstance(getContext(), imageView, receiptDisplay, receiptsManager);

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    //TODO: Figure out how to store temp URI path in ext. storage

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Start writing files
                    if (cameraIntent.resolveActivity(getContext().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = URICreator.createImageFile(getContext());
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            Log.i("CameraRequest", "URI creation failed");
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(getContext(),
                                    "com.example.android.fileprovider",
                                    photoFile);
                            tempCameraUri = photoURI;
                            tempFile = photoFile;
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(cameraIntent, 1);
                        }
                        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT)
                        //startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }else {
                    requestStoragePermission();
                }
            }
        });
        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {
            Log.i("hihi", "gallery onActivity");
            imageUri = data.getData();
            File imageFile = new File(FileUtil.getPath(imageUri, getContext()));
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file",
                    imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile));
            //receiptDisplay.setImageURI(imageUri);
            /** The next line is actually running  asynchronously in the background thread. Hence, will
             * be great if we can run some form of animation directly after the next line to play
             * animations
             */

            //TODO @Crystal : Add aniamtion in the form of gif/whatever works here
            tabscannerapi.postRequest(filePart);
            imageView.setBackgroundResource(R.color.bgcolor);
            Glide.with(getContext()).load(R.drawable.loading_screen).into(imageView);
            receiptDisplay.setText("Receipt is loading...");

        } else if (resultCode == RESULT_OK && requestCode == 1) {
            //imageUri = data.getData();
            Log.i("hihi", "Camera onActivity");


            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file",
                    tempFile.getName(), RequestBody.create(MediaType.parse("image/*"), tempFile));
            //receiptDisplay.setImageURI(imageUri);
            tabscannerapi.postRequest(filePart);
            imageView.setBackgroundResource(R.color.bgcolor);
            Glide.with(getContext()).load(R.drawable.loading_screen).into(imageView);
            receiptDisplay.setText("Receipt has been uploaded!");


        }
    }


    private void requestStoragePermission() {
        if (shouldShowRequestPermissionRationale( Manifest.permission.CAMERA)) {
            //Toast.makeText(getActivity(), "Permission box coming out", Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission needed")
                    .setMessage("Permission is needed to take photo of the receipts")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions( new String[]{Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            Toast.makeText(getActivity(),"Requesting permission",Toast.LENGTH_SHORT).show();
            requestPermissions( new String[]{Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
