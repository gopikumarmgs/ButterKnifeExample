package com.bk.gs.butterknifeexapmle;


        import android.app.ProgressDialog;

        import android.content.DialogInterface;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.VolleyLog;
        import com.android.volley.toolbox.ImageLoader;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.StringRequest;

        import org.json.JSONObject;

        import butterknife.BindString;
        import butterknife.BindView;
        import butterknife.ButterKnife;
        import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String IMAGE_REQUEST_URL = "https://cnet4.cbsistatic.com/hub/i/2011/10/27/a66dfbb7-fdc7-11e2-8c7c-d4ae52e62bcc/android-wallpaper5_2560x1600_1.jpg";
    private static final String STRING_REQUEST_URL = "https://api.ipify.org/";
    private static final String JSON_OBJECT_REQUEST_URL = "https://api.ipify.org?format=json";

    ProgressDialog progressDialog;
    private static final String TAG = "MainActivity";

    //Bind the View using Butter Knife
    @BindView(R.id.button_get_string) Button btn_string;
    @BindView(R.id.button_get_json_object) Button btn_json;
    @BindView(R.id.button_get_image) Button btn_image;

    //Bind the String Values using Butter Knife
    @BindString(R.string.loading) String loadingTxt;
    @BindString(R.string.ok) String okTxt;

    private View showDialogView;
    AlertDialog.Builder alertDialogBuilder;
    TextView outputTextView;
    ImageView outputImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
    }

    //Bind the Click Events using Butter Knife
    @OnClick(R.id.button_get_string)
    public void getStringResponse() {

        progressDialog.setMessage(loadingTxt);
        progressDialog.show();

        StringRequest strReq = new StringRequest(STRING_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                AlertDialog.Builder builder = getDialog();
                outputTextView.setText(response.toString());
                builder.show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });
        // Adding String request to request queue
        VolleyApplication.getInstance().getRequestQueue().add(strReq);
    }

    //Bind the Click Events using Butter Knife
    @OnClick(R.id.button_get_json_object)
    public void getJson(){

        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(JSON_OBJECT_REQUEST_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AlertDialog.Builder builder = getDialog();
                        outputTextView.setText(response.toString());
                        builder.show();
                        progressDialog.hide();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });

        // Adding JsonObject request to request queue
        VolleyApplication.getInstance().getRequestQueue().add(jsonObjectReq);
    }

    //Bind the Click Events using Butter Knife
    @OnClick(R.id.button_get_image)
    public void getImage(){
        ImageLoader imageLoader = VolleyApplication.getInstance().getmImageLoader();

        imageLoader.get(IMAGE_REQUEST_URL, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    AlertDialog.Builder builder = getDialog();
                    outputImageView.setImageBitmap(response.getBitmap());
                    builder.show();
                }
            }
        });
    }

    private AlertDialog.Builder getDialog() {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        showDialogView = li.inflate(R.layout.dialog, null);
        outputImageView = (ImageView)showDialogView.findViewById(R.id.image_view_dialog);
        outputTextView = (TextView)showDialogView.findViewById(R.id.text_view_dialog);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(showDialogView);
        alertDialogBuilder
                .setPositiveButton(okTxt, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setCancelable(false)
                .create();
        return alertDialogBuilder;
    }
}



