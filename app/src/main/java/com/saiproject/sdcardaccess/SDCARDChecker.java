package com.saiproject.sdcardaccess;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class SDCARDChecker {

    public static void isExternalStorageAvailable(Context context){


        boolean isReadable = false;
        boolean isWriteable = false;

        String state = Environment.getExternalStorageState();  // Stores the state of the external storage
        if(Environment.MEDIA_MOUNTED.equals(state)){

            //Both readable and writable

            isReadable = isWriteable = true;
            Toast.makeText(context, "Readable and Writeable SD Card", Toast.LENGTH_LONG).show();

        }

        else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
           //Readable but not writeable

            isReadable = true;
            isWriteable = false;

            Toast.makeText(context, "Readable but not Writeable SD Card", Toast.LENGTH_LONG).show();

        }

        else{
            //Neither readable nor writeable

            isReadable = isWriteable = false;
            Toast.makeText(context, "Neither readable nor writeable", Toast.LENGTH_LONG).show();
        }









    }


}
