package com.example.digitalreceipts.CameraOCR;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CameraFragment extends DialogFragment {
    private static final int GALLERY_REQUEST = 2888;
    private static final int CAMERA_REQUEST = 1888;
    public ReceiptsManager receiptsManager;
    private static final int MY_CAMERA_PERMISSION_CODE = 6604;

    TextView receiptDisplay;
    Button galleryButton;
    Button cameraButton;
    Button retrieveAPI;
    Uri imageUri;
    TBApi tabscannerapi;
    ImageView imageView;

    public CameraFragment(){}
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

        tabscannerapi = TBApi.getInstance(getContext(),imageView,receiptDisplay, receiptsManager);

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
                if (getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    Log.i("hihi","permission not granted");
                }
                else if(getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
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
        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {
            Log.i("hihi","gallery onActivity");
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

        }
        else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            imageUri = data.getData();
            Log.i("hihi","Camera onActivity");
            File imageFile = new File(FileUtil.getPath(imageUri, getContext()));
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file",
                    imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile));
            //receiptDisplay.setImageURI(imageUri);
            tabscannerapi.postRequest(filePart);
            receiptDisplay.setText("Receipt has been uploaded!");


        }
    }
}
