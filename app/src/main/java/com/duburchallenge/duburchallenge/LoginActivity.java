package com.duburchallenge.duburchallenge;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.AndroidTestCase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.IOException;
import java.net.URI;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout tilEmail, tilPwd;
    private EditText edtEmail, edtPwd;
    private Button btnLogin;
    private boolean isValidPwd, isValidEmail;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        tilEmail = findViewById(R.id.til_email);
        tilPwd = findViewById(R.id.til_pwd);
        edtEmail = findViewById(R.id.email);
        edtPwd = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValidEmail = true;
                String email = s.toString();
                String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                if (email.equalsIgnoreCase("")) {
                    tilEmail.setError("Please, Enter Email Address");
                    isValidEmail = false;
                    tilEmail.setErrorEnabled(true);
                } else if (!email.matches(emailPattern)) {
                    tilEmail.setError("Invalid email address");
                    isValidEmail = false;
                    tilEmail.setErrorEnabled(true);
                } else {
                    tilEmail.setError("");
                    tilEmail.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValidPwd = true;
                String password = s.toString();
                String passwordPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
                if (password.equalsIgnoreCase("")) {
                    tilPwd.setError("Please, Enter Password");
                    isValidPwd = false;
                    tilPwd.setErrorEnabled(true);
                } else if (!password.matches(passwordPattern)) {
                    tilPwd.setError("Password must be 8 character long with number,capital and small letter with special symbol)");
                    isValidPwd = false;
                    tilPwd.setErrorEnabled(true);
                } else {
                    tilPwd.setError("");
                    tilPwd.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnLogin.setOnClickListener(LoginActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            if (isValidEmail == isValidPwd) {
                if (edtEmail.getText().toString().equalsIgnoreCase(email) && edtPwd.getText().toString().equalsIgnoreCase(password)) {
                        Intent intent = new Intent(LoginActivity.this,MapActivity.class);
                        startActivity(intent);
//                        getResponse(); //Mock server
                }
            }

        }
    }

    public void getResponse() throws IOException, InterruptedException {
        RestClient restClient = new RestClient();
        restClient.getClient();
    }

}

final class RestClient {

    private static IRestService mRestService = null;

    public static IRestService getClient() {
        if (mRestService == null) {
            final OkHttpClient client = new OkHttpClient();
            // ***YOUR CUSTOM INTERCEPTOR GOES HERE***
            client.interceptors().add(new FakeInterceptor());

            final Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    // Endpoint
                    .baseUrl(IRestService.ENDPOINT)
                    .client(client)
                    .build();

            mRestService = retrofit.create(IRestService.class);
            mRestService.getImg("img");
        }
        return mRestService;
    }
}

class FakeInterceptor implements Interceptor {
    // FAKE RESPONSES.
    private final static String img = "{\"img\":http://image10.bizrate-images.com/resize?sq=60&uid=2216744464\"}";
//    private final static String TEACHER_ID_2 = "{\"id\":1,\"age\":16,\"name\":\"Tovmas Apoyan\"}";

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        okhttp3.Response response = null;
        if (BuildConfig.DEBUG) {
            String responseString;
            // Get Request URI.
            final URI uri = chain.request().url().uri();
            // Get Query String.
            final String query = uri.getQuery();
            // Parse the Query String.
            final String[] parsedQuery = query.split("=");
            if (parsedQuery[0].equalsIgnoreCase("img")) {
                responseString = img;
            } else {
                responseString = "";
            }

            response = new Response.Builder()
                    .code(200)
                    .message(responseString)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                    .addHeader("content-type", "application/json")
                    .build();
            Log.e("res =", "" + responseString);
        } else {
            response = chain.proceed(chain.request());
        }

        return response;
    }
}

interface IRestService {

    String ENDPOINT = "http://www.vavian.com/";

    @GET("/")
    Call<Model> getImg(@Query("email") final String id);
}
