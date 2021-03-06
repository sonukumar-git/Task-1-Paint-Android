package com.example.task1paint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.UUID;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {
    private MyDrawingView myView;
    private ImageButton btnPencil,btnEraser,btnBackground;
    private ImageView plusminus;
    private long mDefaultColor;
    private Button btnSave;
    private static final int UNIQUE_REQUEST_CODE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myView=(MyDrawingView)findViewById(R.id.drawing);

        btnEraser=findViewById(R.id.btn_eraser);
        btnEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myView.setEraser(true);
            }
        });

        btnPencil=findViewById(R.id.btn_pencil);
        btnPencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.setEraser(false);
                mDefaultColor = ContextCompat.getColor(MainActivity.this, R.color.colorSecondaryText);

                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(MainActivity.this, (int)mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {


                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mDefaultColor = color;
                        myView.setColor(color);
                        Log.isLoggable("color", color);


                    }
                });
                colorPicker.show();


            }
        });

        btnBackground=findViewById(R.id.btn_background);
        btnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDefaultColor = ContextCompat.getColor(MainActivity.this, R.color.colorSecondaryText);

                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(MainActivity.this, (int)mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {


                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mDefaultColor = color;
                        myView.setBackground_Color(color);
                        Log.isLoggable("color", color);


                    }
                });
                colorPicker.show();

            }
        });


        btnSave=findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            UNIQUE_REQUEST_CODE);


                }
                else {

                    AlertDialog.Builder saveDialog = new AlertDialog.Builder(MainActivity.this);
                    saveDialog.setTitle("Save drawing");
                    saveDialog.setMessage("Save drawing to device Gallery?");
                    saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //save drawing
                            myView.setDrawingCacheEnabled(true);
               /* File file = new File(filePath);
                FileOutputStream fOut = new FileOutputStream(file);
                mDrawingView
                        .getDrawingTime().compress(Bitmap.CompressFormat.PNG, 85, fOut);
                fOut.flush();
                fOut.close();*/
                            String imgSaved = MediaStore.Images.Media.insertImage(
                                    getContentResolver(), myView.canvasBitmap,
                                    UUID.randomUUID().toString() + ".png", "drawing");
                            if (imgSaved != null) {
                                Toast savedToast = Toast.makeText(getApplicationContext(),
                                        "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                                savedToast.show();
                            } else {
                                Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                        "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                                unsavedToast.show();
                            }
                            // Destroy the current cache.
                            myView.destroyDrawingCache();
                        }
                    });
                    saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    saveDialog.show();
                }


            }
        });


        plusminus=findViewById(R.id.plusminus);
        plusminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog brushsize=new Dialog(MainActivity.this);
                brushsize.setContentView(R.layout.activity_size_activity);
                brushsize.setTitle("ZOOM IN/OUT");
                ImageView zoom_in=brushsize.findViewById(R.id.zoom_in);
                zoom_in.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myView.setsizeofBrush(10);

                        brushsize.dismiss();


                    }
                });

                ImageView zoom_out=brushsize.findViewById(R.id.zoom_out);
                zoom_out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myView.setsizeofBrush(-10);


                        brushsize.dismiss();
                    }
                });





                    brushsize.show();



            }
        });






    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==UNIQUE_REQUEST_CODE)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Thank you! Permisiion Granted", Toast.LENGTH_SHORT).show();
            }
            else if(grantResults[0]==PackageManager.PERMISSION_DENIED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("This Permisision is important").setTitle("Important Permission ");
                    dialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            UNIQUE_REQUEST_CODE);
                                }
                            });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Cannot be done", Toast.LENGTH_SHORT).show();
                        }
                    });

                    dialog.show();
                    Toast.makeText(this, "required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Will never show this to you again!", Toast.LENGTH_SHORT).show();
                }

            }

        }

    }
}
