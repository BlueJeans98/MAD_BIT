package com.example.tab_layout;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class frag2_class extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private ArrayList<String> imagePaths;
    private RecyclerView imagesRV;
    private RecyclerViewAdapter imageRVAdapter;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment2, null);
        context = getContext();
        imagePaths = new ArrayList<>();
        imagesRV = root.findViewById(R.id.idRVImages);

        requestPermissions();

        imageRVAdapter.setOnItemLongClickListener(new RecyclerViewAdapter.OnItemLongClickListener(){

            //아이템 클릭
        @Override
        public void onItemLongClick(View v, int position) {
            String path = imagePaths.get(position);
            itemLongClick(path, position);
        }

    });


        return root;
    }

    private void itemLongClick(String path, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.image_delete, null, false);
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        final Button image_delete = view.findViewById(R.id.image_delete);

        image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                imagePaths.remove(position);
                imageRVAdapter.notifyItemRemoved(position);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean checkPermission() {
        // in this method we are checking if the permissions are granted or not and returning the result.
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext() , READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        if (checkPermission()) {
            // if the permissions are already granted we are calling
            // a method to get all images from our external storage.
            Toast.makeText(getContext(), "Permissions granted..", Toast.LENGTH_SHORT).show();
            getImagePath();
        } else {
            // if the permissions are not granted we are
            // calling a method to request permissions.
            requestPermission();
        }
    }

    private void requestPermission() {
        //on below line we are requesting the rea external storage permissions.
        ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }




    private void prepareRecyclerView() {

        // in this method we are preparing our recycler view.
        // on below line we are initializing our adapter class.
        imageRVAdapter = new RecyclerViewAdapter(getContext(), imagePaths);

        // on below line we are creating a new grid layout manager.
        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);

        // on below line we are setting layout
        // manager and adapter to our recycler view.
        imagesRV.setLayoutManager(manager);
        imagesRV.setAdapter(imageRVAdapter);
    }

    private void getImagePath() {
        // in this method we are adding all our image paths
        // in our arraylist which we have created.
        // on below line we are checking if the device is having an sd card or not.
        boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        if (isSDPresent) {

            // if the sd card is present we are creating a new list in
            // which we are getting our images data with their ids.
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};

            // on below line we are creating a new
            // string to order our images by string.
            final String orderBy = MediaStore.Images.Media._ID;

            // this method will stores all the images
            // from the gallery in Cursor
            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);

            // below line is to get total number of images
            int count = cursor.getCount();

            // on below line we are running a loop to add
            // the image file path in our array list.

            for (int i = 0; i < count; i++) {

                // on below line we are moving our cursor position
                cursor.moveToPosition(i);

                // on below line we are getting image file path
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

                // after that we are getting the image file path
                // and adding that path in our array list.
                imagePaths.add(cursor.getString(dataColumnIndex));
            }

            // calling a method to
            // prepare our recycler view.
            prepareRecyclerView();

            imageRVAdapter.notifyDataSetChanged();
            // after adding the data to our
            // array list we are closing our cursor.
            cursor.close();
        }
    }

    private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result) {
                        Toast.makeText(getContext(), "Permissions Granted..", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Permissions denined, Permissions are required to use the app..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
}