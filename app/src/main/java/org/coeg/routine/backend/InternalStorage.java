package org.coeg.routine.backend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InternalStorage
{
    Context context;
    File pictureFile;

    public InternalStorage(Context context)
    {
        this.context = context;
        pictureFile = getOutputMediaFile();
    }

    /**
     * Get image path location
     * @return File
     */
    private File getOutputMediaFile()
    {
        File internalStorageDir = context.getFilesDir();
        return new File(internalStorageDir.getPath() + File.separator + "profile.png");
    }

    /**
     * Save image to internal storage
     * @param image Bitmap that you want to save
     */
    public boolean SaveImageToInternalStorage(Bitmap image)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (FileNotFoundException e)
        {
            Log.d("DEBUG-TEST", "File not found: " + e.getMessage());
            return false;
        }
        catch (IOException e)
        {
            Log.d("DEBUG-TEST", "Error accessing file: " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Load image profile that are stored in internal storage
     * @return Bitmap
     */
    public Bitmap GetImageFromInternalStorage()
    {
        Bitmap image = null;

        try
        {
            image = BitmapFactory.decodeStream(new FileInputStream(pictureFile));
        }
        catch (FileNotFoundException e)
        {
            return null;
        }

        return image;
    }
}
