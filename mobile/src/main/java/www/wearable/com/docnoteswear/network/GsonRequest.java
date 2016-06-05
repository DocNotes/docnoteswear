package www.wearable.com.docnoteswear.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Inspired from google volley tutorial. Gson Wrapper for volley, requests with
 * POST or GET method that will be parsed into Java objects by Gson.
 */
public class GsonRequest<T> extends Request<T> {
    private Gson mGson = new Gson();
    private Class<T> clazz;
    private Map<String, String> headers;
    private Map<String, String> params;
    private Listener<T> listener;
    private String mRequestBody;
    private static final String PROTOCOL_CHARSET = "utf-8";
    /**
     * Content type for request.
     */
    private static String protocol_content_type = String.format(
            "application/x-www-form-urlencoded; charset=%s", PROTOCOL_CHARSET);

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url   URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     */
    public GsonRequest(int method, String url, Class<T> clazz,
                       Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);

       /* this.setRetryPolicy(new DefaultRetryPolicy(
                WebServiceUtils.REQUEST_TIMEOUT_MS,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        this.clazz = clazz;
        this.listener = listener;
        mGson = new Gson();
    }

    /**
     * Make a POST request and return a parsed object from JSON.
     *
     * @param url   URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     */
    public GsonRequest(int method, String url, Class<T> clazz,
                       Map<String, String> params, Map<String, String> headers,
                       Listener<T> listener, Response.ErrorListener errorListener, String jsonBody,
                       String contentType) {

        super(method, url, errorListener);
        System.setProperty("http.keepAlive", "false");
        this.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.clazz = clazz;
        this.params = params;
        this.listener = listener;
        this.headers = headers;
        this.mRequestBody = jsonBody;
        if (contentType != null)
            protocol_content_type = contentType;
        mGson = new Gson();
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    /**
     * The response from the server will be recieved here and the listener's
     * onResponse callback method will be called.
     */
    @Override
    public void deliverResponse(T response) {
        if (listener != null)
            listener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
    }

    @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody
                    .getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog
                    .wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }


    @Override
    public String getBodyContentType() {
        return protocol_content_type;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            Log.d("uri", response.statusCode + "");
            if (response.statusCode != 400) {
                String json = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers));
                Log.d("json", json);
                return Response.success(mGson.fromJson(json, clazz),
                        HttpHeaderParser.parseCacheHeaders(response));
            } else {
                Log.d("400", new String(response.data));
                return Response.error(new VolleyError(new String(response.data)));
            }

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return Response.error(new ParseError());
        }


    }


    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {

        return super.parseNetworkError(volleyError);
    }




    private String getParseMessage(byte[] data) {
        JsonParser parser = new JsonParser();
        String message = null;
        JsonElement jsonElement = parser.parse(new String(data));
        if (!jsonElement.isJsonNull()) {
            message = ((JsonObject) jsonElement).get("message").getAsString();
        }
        return message;
    }

}






