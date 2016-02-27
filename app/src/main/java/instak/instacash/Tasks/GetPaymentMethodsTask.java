package instak.instacash.Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import instak.instacash.Model.Currency;
import instak.instacash.Model.PaymentMethod;
import instak.instacash.R;

public class GetPaymentMethodsTask extends AsyncTask<Void, Void, Void> {

    public interface TaskListener {
        void onPaymentMethodsReceived(List<PaymentMethod> paymentList);
    }

    private List<PaymentMethod> paymentsList = new ArrayList<>();
    private WeakReference<TaskListener> mTaskListenerRef;
    private Activity activity;
    private Currency currency;
    private String btcAmount;
    private ProgressDialog progressBar;

    public GetPaymentMethodsTask(TaskListener listener, Activity activity, Currency currency, String btcAmount) {
        this.mTaskListenerRef = new WeakReference<>(listener);
        this.activity = activity;
        this.currency = currency;
        this.btcAmount = btcAmount;
    }

    @Override
    protected Void doInBackground(Void... params) {

        Uri uri = Uri.parse(String.format(activity.getString(R.string.endpoint_payment_methods), currency.countryCode))
                .buildUpon()
                .appendQueryParameter("crypto_amount", btcAmount)
                .build();

        try {
            HttpURLConnection urlConn = (HttpURLConnection) new URL(uri.toString()).openConnection();

//            if (urlConn.getResponseCode() == 404) {
//            }

            if (urlConn.getResponseCode() != 200) {
                Toast.makeText(activity.getApplicationContext(), "HTTP error code : " + urlConn.getResponseCode(), Toast.LENGTH_SHORT).show();
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((urlConn.getInputStream())));

            buildPaymentList(br.readLine());

            urlConn.disconnect();

        } catch(MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar = new ProgressDialog(activity);
        progressBar.show();

        paymentsList.clear();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (progressBar.isShowing()) {
            progressBar.hide();
        }

        notifyPaymentMethodsReceived(paymentsList);
    }

    public void notifyPaymentMethodsReceived(List<PaymentMethod> paymentsList) {
        mTaskListenerRef.get().onPaymentMethodsReceived(paymentsList);
    }

    /**
     *
     * @param br
     */
    private void buildPaymentList(String br) {
        try {
            JSONObject jsonResponse = new JSONObject(br);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("data");

            for(int i = 0; i<jsonMainNode.length();i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("name");
                String price = jsonChildNode.optString("treshold_price");
                String label = jsonChildNode.optString("label");
                paymentsList.add(new PaymentMethod(name, new BigDecimal(price), label));
            }
        } catch(JSONException e){
            Toast.makeText(activity.getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
