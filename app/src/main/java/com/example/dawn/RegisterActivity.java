package com.example.dawn;

import static com.example.dawn.utils.FileUtil.getImagePath;
import static com.example.dawn.utils.MyLogger.toast;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dawn.config.Config;
import com.example.dawn.dto.Account.UserItemDTO;
import com.example.dawn.network.RetrofitClient;
import com.example.dawn.utils.FileUtil;
import com.example.dawn.utils.MyLogger;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    private Uri avatarUri;
    private ImageView avatarPreview;
    private EditText emailInput, usernameInput, passwordInput;

    private final ActivityResultLauncher<String> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            avatarUri = uri;
                            avatarPreview.setImageURI(uri); // preview/edit
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        avatarPreview = findViewById(R.id.avatarPreview);
        emailInput = findViewById(R.id.emailInput);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);

        findViewById(R.id.chooseAvatarButton)
                .setOnClickListener(v -> imagePicker.launch("image/*"));

    }

    public void onRegisterClick(View view) {

        String email = emailInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            toast("Заповніть усі поля");
            return;
        }

        if (avatarUri == null) {
            toast("Додайте зображення");
            return;
        }

        register(email, username, password, avatarUri);
    }


    private void register(String email, String username, String password, Uri imageUri){
        String mimeType = getContentResolver().getType(imageUri);
        if (mimeType == null) mimeType = "image/jpeg";

        RequestBody emailPart =
                RequestBody.create(emailInput.getText().toString(), MultipartBody.FORM);
        RequestBody usernamePart =
                RequestBody.create(usernameInput.getText().toString(), MultipartBody.FORM);
        RequestBody passwordPart =
                RequestBody.create(passwordInput.getText().toString(), MultipartBody.FORM);

        MultipartBody.Part avatarPart = null;
        if(imageUri != null) {
            String imagePath = getImagePath(imageUri);
            if (imagePath != null) {
                File file = new File(imagePath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                avatarPart = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);
            }
        }


        RetrofitClient.getInstance()
                .getUserApi()
                .register(emailPart, usernamePart, passwordPart, avatarPart)
                .enqueue(new Callback<UserItemDTO>() {
                    @Override
                    public void onResponse(Call<UserItemDTO> call, Response<UserItemDTO> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            toast("User створена");
                            //setResult(RESULT_OK);
//                            finish();
                            goToMainActivity();
                        } else if (response.isSuccessful() && response.body() == null) {
                            Log.d("RegisterActivity", "Response successful but body is null. Code: " + response.code());
                            toast("User створена");
                            //setResult(RESULT_OK);
//                            finish();
                            goToMainActivity();
                        } else {
                            String errorBody = "";
                            try {
                                if (response.errorBody() != null) {
                                    errorBody = response.errorBody().string();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.e("RegisterActivity", "Server error: " + response.code() + ", body: " + errorBody);
                            toast("Помилка сервера: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserItemDTO> call, Throwable t) {
                        Log.e("RegisterActivity", "onFailure type: " + t.getClass().getName());
                        Log.e("RegisterActivity", "message: " + t.getMessage(), t);
                        toast("Помилка: " + t.getMessage());
                    }
                });
    }
    private String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imagePath = cursor.getString(column_index);
            cursor.close();
            return imagePath;
        }

        return null;
    }
}
