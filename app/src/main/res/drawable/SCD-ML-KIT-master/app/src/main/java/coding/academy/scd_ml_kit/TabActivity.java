package coding.academy.scd_ml_kit;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;

public class TabActivity extends AppCompatActivity {
    private TabLayout tabLayout ;

    private ViewPager2 tabviewPager ;

    private static final int requestPermissionID = 101;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_PHOTO_GALLERY = 2;

   // private ViewPager tabviewPager ;
    private TabbedPagerAdapter mTabPagerAdapter ;
    private static final String DIALOG_DATE = "DialogDate";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        tabviewPager =  findViewById(R.id.tab_view_pager);
        tabviewPager.setAdapter(new TabbedPagerAdapter(this) );

        tabLayout  = (TabLayout) findViewById(R.id.tablayout_id) ;


        TabLayoutMediator tabLayoutMediator =new TabLayoutMediator(
                tabLayout, tabviewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position)
                {
                    case 0 :
                           tab.setText("Camera");
                           tab.setIcon(R.drawable.ic_baseline_camera_24) ;
                    break;
                    case 1 :

                        tab.setText("Gallary");
                        tab.setIcon(R.drawable.ic_baseline_insert_photo_24) ;

                        break;
                    case 2 :
                        tab.setText("About us");
                        tab.setIcon(R.drawable.ic_baseline_adb_24) ;
                        break;

                }


            }


        }) ;
        tabLayoutMediator.attach();


        Toolbar mainToolbar= findViewById(R.id.Main_toolbar) ;
        setSupportActionBar(mainToolbar);

       // getSupportActionBar().setIcon(R.mipmap.ic_app_icon_toolbar); //also displays wide logo

        checkPermission(REQUEST_TAKE_PHOTO, false);



    }


    public void checkPermission(int requestCode, boolean open) {
        switch (requestCode) {

            case REQUEST_TAKE_PHOTO:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED) {
                        //permission not enabled, request it
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                    } else if (open) {
                        //permission already granted
                        openCamera();
                    }
                } else {
                    if (open) {
                        //permission already granted
                        openCamera();
                    }
                }

                break;


            case REQUEST_PHOTO_GALLERY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED) {
                        selectPicture();
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                    }
                }
                break;


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != requestPermissionID) {
            Log.e("MainActivity", "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                // openCamera();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public void openCamera() {
        photoFile = PicUtil.createTempFile(photoFile);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            try {

            } catch (Exception ex) {
                // Error occurred while creating the File
                Log.e("Main", ex.toString());

            }

            if (photoFile != null) {

              /*  imageUri = Uri.fromFile(image);

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);


*/
                Uri photoURI = FileProvider.getUriForFile(this,
                        "coding.academy.scd_ml_kit.fileprovider",
                        photoFile);

                //  Uri photoURI =  Uri.fromFile(photoFile);
                Log.e("Main", photoURI.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }


        }

        //open the camera => create an Intent object

        //  intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , "image/*" );
        //   startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }

    public File photoFile;

    private void selectPicture() {
        photoFile = PicUtil.createTempFile(photoFile);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_PHOTO_GALLERY);
    }




}
