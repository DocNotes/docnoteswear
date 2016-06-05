package www.wearable.com.docnoteswear;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;

import www.wearable.com.docnoteswear.model.PatientData;
import www.wearable.com.docnoteswear.network.CompanyHttpClient;
import www.wearable.com.docnoteswear.network.HttpClient;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getName();
    CompanyHttpClient client ;
    private GoogleApiClient googleApiClient;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.sync);
        client = new CompanyHttpClient(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
        button.setOnClickListener(buttonClickListener);
        googleApiClient.connect();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(googleApiClient.isConnected()) {
                client.getAllPatients(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response.toString());
                        new DataTask(response).execute();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class DataTask extends AsyncTask<Void , Void , Void>
    {
        String data ;

        public DataTask(String data){
            this.data = data;
        }

        @Override
        protected Void doInBackground(Void... voids) {
           /* PutDataMapRequest dataMap = PutDataMapRequest.create ("/patient_event");
            dataMap.getDataMap().putDataMap("data" , data.getFromDataMap());
            PutDataRequest request = dataMap.asPutDataRequest();
            request.setUrgent();
            DataApi.DataItemResult dataItemResult = Wearable.DataApi
                    .putDataItem(googleApiClient , request).await();
            if (dataItemResult.getStatus().isSuccess()) {
                Log.v("myTag", "DataMap: " + dataMap + " sent successfully to data layer ");
            }
            else {
                // Log an error
                Log.v("myTag", "ERROR: failed to send DataMap to data layer");
            }*/


            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
            for (Node node : nodes.getNodes()) {
                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), "/patient_event", data.getBytes()).await();
                if (result.getStatus().isSuccess()) {
                    Log.v("myTag", "Message: {" + data.toString() + "} sent to: " + node.getDisplayName());
                } else {
                    // Log an error
                    Log.v("myTag", "ERROR: failed to send Message");
                }
            }

            return null;
        }
    }
}


