package com.example.jeremy.lab9;

import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;

public class Task2Activity extends AppCompatActivity {
    TextView txtStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_task2 );
        txtStorage = findViewById( R.id.txtStorage );
        txtStorage.setText( "" );


        if (  externalMemoryAvailable()){
            String avalaibleSpace = getAvailableInternalMemorySize();
            txtStorage.setText( "Ext. Storage Available " + avalaibleSpace);

        } else {
            txtStorage.setText( "Ext. Storage Not Available" );
        }


    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        Log.e("dcv",state);
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String secStore = System.getenv("SECONDARY_STORAGE");
        File f_secs = new File(secStore);
        f_secs.canRead();
        String state = Environment.getExternalStorageState();
        Log.e("dcv",state);
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }



    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return String.valueOf( (availableBlocks * blockSize)/ 1048576 );
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }



}