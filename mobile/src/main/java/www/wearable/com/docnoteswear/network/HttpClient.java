package www.wearable.com.docnoteswear.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by arvind on 5/20/16.
 */
public class HttpClient {

    protected static RequestQueue queue;
    protected Context mContext;

    public HttpClient(Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }

    }
}
