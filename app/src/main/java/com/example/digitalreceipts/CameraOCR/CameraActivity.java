package com.example.digitalreceipts.CameraOCR;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.digitalreceipts.R;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CameraActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 2888;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 6604;

    TextView receiptDisplay;
    Button galleryButton;
    Button cameraButton;
    Button retrieveAPI;
    Uri imageUri;
    TBApi tabscannerapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);



        receiptDisplay = findViewById(R.id.receiptDisplay);
        galleryButton = findViewById(R.id.galleryButton);
        cameraButton = findViewById(R.id.cameraButton);
        tabscannerapi = TBApi.getInstance(receiptDisplay);

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
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    Log.i("hihi","permission not granted");
                }
                else if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1011);
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1012);
                }
                else
                {
                    Log.i("hihi","transition");
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {
            Log.i("hihi","gallery onActivity");
            imageUri = data.getData();
            File imageFile = new File(FileUtil.getPath(imageUri, this));
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file",
                    imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile));
            //receiptDisplay.setImageURI(imageUri);
            tabscannerapi.postRequest(filePart);

        }
        else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            imageUri = data.getData();
            Log.i("hihi","Camera onActivity");
            File imageFile = new File(FileUtil.getPath(imageUri, this));
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file",
                    imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile));
            //receiptDisplay.setImageURI(imageUri);
            tabscannerapi.postRequest(filePart);

        }
    }
}
