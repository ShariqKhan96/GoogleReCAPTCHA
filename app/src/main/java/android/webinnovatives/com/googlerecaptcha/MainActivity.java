package android.webinnovatives.com.googlerecaptcha;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    final String SITE_KEY = "6LdQLY4UAAAAAO3-0DNtihA8s5jWYryMx0-N7ln9";
    final String SECRET_KEY = "6LdQLY4UAAAAAPpKJ0bC8be08sYbltisyY1KTW24";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SafetyNet.getClient(MainActivity.this).verifyWithRecaptcha(SITE_KEY)
                        .addOnSuccessListener(MainActivity.this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                                if (!response.getTokenResult().isEmpty()) {
                                    Log.e("UserResponse", response.getTokenResult());
                                    handleSiteVerify(response.getTokenResult());
                                }
                            }
                        })
                        .addOnFailureListener(MainActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (e instanceof ApiException) {
                                    ApiException apiException = (ApiException) e;
                                    Log.e(TAG, "Error message: " +
                                            CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                                } else {
                                    Log.e(TAG, "Unknown type of error: " + e.getMessage());
                                }
                            }
                        });
            }
        });
    }

    private void handleSiteVerify(final String tokenResult) {
        String url = "https://www.google.com/recaptcha/api/siteverify?secret=" + SECRET_KEY + "&response=" + tokenResult;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        }) {
        };
        Volley.newRequestQueue(MainActivity.this).add(request);
    }
}
