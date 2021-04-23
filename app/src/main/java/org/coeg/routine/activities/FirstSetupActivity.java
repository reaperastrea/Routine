package org.coeg.routine.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.shapes.Shape;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.google.android.material.imageview.ShapeableImageView;

import org.coeg.routine.R;
import org.coeg.routine.backend.InternalStorage;
import org.coeg.routine.backend.PreferencesStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FirstSetupActivity extends AppCompatActivity
{
    public static final int PICK_IMAGE = 1;

    private final PreferencesStorage preferences = PreferencesStorage.getInstance();

    private InternalStorage internalStorage;

    private boolean editMode = false;

    private TextView            txtTitle;
    private TextView            labelAddImage;
    private Button              btnStartTracking;
    private ImageButton         btnExit;
    private EditText            etDisplayName;
    private ShapeableImageView  imgUser;
    private ConstraintLayout    layoutUserImage;

    private Uri imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setup);

        LoadUserPreferences();
        InitStorage();
        InitView();
        InitListener();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        /** Allowing user to dismiss EditText by pressing
         *  somewhere outside the box
         */
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            View v = getCurrentFocus();
            if ( v instanceof EditText)
            {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY()))
                {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK)
        {
            Bitmap image = null;
            imagePath = data.getData();

            // Load bitmap image from user selection
            try
            {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            if (image != null)
            {
                // Show user image selection
                SetImageView(image);
            }
        }
    }

    /**
     * Load user SharedPreferences
     */
    private void LoadUserPreferences()
    {
        preferences.loadPreferences(getApplicationContext());
        if (preferences.getUserId() != -1)
        {
            editMode = true;
        }
    }

    /**
     *
     */
    private void InitStorage()
    {
        internalStorage = new InternalStorage(getApplicationContext());
    }

    /**
     * Initialize variable by findViewByID
     * and setup view properties
     */
    private void InitView()
    {
        txtTitle            = findViewById(R.id.txt_titleFirstSetup);
        labelAddImage       = findViewById(R.id.labelAddImage);
        btnStartTracking    = findViewById(R.id.btn_startTracking);
        btnExit             = findViewById(R.id.btn_exit);
        etDisplayName       = findViewById(R.id.et_inputDisplayName);
        imgUser             = findViewById(R.id.img_inputUserImage);
        layoutUserImage     = findViewById(R.id.layout_userImage);

        float radius = getResources().getDimension(R.dimen.small_border_radius);
        imgUser.setShapeAppearanceModel(
                 imgUser.getShapeAppearanceModel()
                        .toBuilder()
                        .setAllCornerSizes(radius)
                        .build());

        // Check whether user entering edit mode
        if (editMode)
        {
            txtTitle.setText(getString(R.string.title_edit_profile));
            btnExit.setVisibility(View.VISIBLE);
            etDisplayName.setText(preferences.getFullName());
            btnStartTracking.setText(R.string.btn_update_profile);

            // Load image from internal storage
            Bitmap image = internalStorage.GetImageFromInternalStorage();

            if (image != null)
            {
                // Show user image selection
                SetImageView(image);
            }
        }
    }

    /**
     * Initialize component listener
     */
    private void InitListener()
    {
        if (editMode)
        {
            btnExit.setOnClickListener(v -> {
                finish();
                setResult(RESULT_CANCELED);
            });

            btnStartTracking.setOnClickListener(v -> {
                UpdateProfileData();
            });
        }
        else
        {
            btnStartTracking.setOnClickListener(v -> {
                SetProfileData();
            });
        }

        layoutUserImage.setOnClickListener(v -> {
            RequestProfilePicture();
        });
    }

    /**
     * Set preview image
     * @param draw Bitmap to
     */
    private void SetImageView(Bitmap draw)
    {
        labelAddImage.setVisibility(View.INVISIBLE);
        imgUser.setImageBitmap(draw);
    }

    /**
     * Set new data to SharedPreferences
     */
    private void SetProfileData()
    {
        String displayName = etDisplayName.getText().toString();
        Bitmap imageSelection = null;

        // Check if user has entered display name
        if (!CheckForInput(displayName))
        {
            Toast.makeText(this, "Display name can't be empty! (max. 12 char)", Toast.LENGTH_LONG).show();
            return;
        }

        // Load bitmap image from user selection
        if (imagePath != null)
        {
            try
            {
                imageSelection = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        // Check if user has choose an image
        if (!CheckForInput(imageSelection))
        {
            Toast.makeText(this, "Please add an image", Toast.LENGTH_LONG).show();
            return;
        }

        // Save user input data to preferences storage
        preferences.setFullName(displayName);
        preferences.setUserId(PreferencesStorage.getRandomUserId(1, 10000));
        preferences.savePreferences();

        internalStorage.SaveImageToInternalStorage(imageSelection);

        Recheck();
    }

    /**
     * Update data in SharedPreferences
     */
    private void UpdateProfileData()
    {
        String displayName = etDisplayName.getText().toString();
        Bitmap imageSelection = null;

        // Check if user has entered display name
        if (!CheckForInput(displayName))
        {
            Toast.makeText(this, "Display name can't be empty! (max. 12 char)", Toast.LENGTH_LONG).show();
            return;
        }

        // Load bitmap image from user selection
        if (imagePath != null)
        {
            try
            {
                imageSelection = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        // Save user input data to preferences storage
        preferences.setFullName(displayName);
        preferences.setUserId(PreferencesStorage.getRandomUserId(1, 10000));
        preferences.savePreferences();

        if (imagePath != null)
        {
            internalStorage.SaveImageToInternalStorage(imageSelection);
        }

        finish();
        setResult(RESULT_OK);
    }

    /**
     * Request gallery app to system
     * to allow user to choose image
     */
    private void RequestProfilePicture()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    /**
     * Check for user input
     * @param displayName user display name text input
     * @return False if not present
     */
    private boolean CheckForInput(String displayName)
    {
        return displayName.length() >= 1 && displayName.length() <= 12;
    }

    /**
     * Check for user input
     * @param image user image selection
     * @return False if not present
     */
    private boolean CheckForInput(Bitmap image)
    {
        return image != null;
    }

    private void Recheck()
    {
        finish();
        startActivity(new Intent(FirstSetupActivity.this, SplashActivity.class));
    }
}
