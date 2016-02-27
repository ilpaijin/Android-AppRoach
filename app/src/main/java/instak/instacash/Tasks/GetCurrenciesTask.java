package instak.instacash.Tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import instak.instacash.Model.Currency;
import instak.instacash.Model.PaymentMethod;
import instak.instacash.R;
import instak.instacash.ui.Adapters.CurrenciesListAdapter;

public class GetCurrenciesTask extends AsyncTask<Void, Void, Void> {

    public interface TaskListener {
        void onCurrenciesReceived(List<Currency> currencies);
    }

    public List<Currency> currenciesList = new ArrayList<>();
    private WeakReference<TaskListener> mTaskListenerRef;
    public Activity activity;

    public GetCurrenciesTask(TaskListener listener, Activity activity) {
        this.mTaskListenerRef = new WeakReference<>(listener);
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... aVoid) {

        try {
            HttpURLConnection urlConn = (HttpURLConnection) new URL(activity.getString(R.string.endpoint_currencies)).openConnection();

//            if (urlConn.getResponseCode() == 404) {
//                urlConn.disconnect();
//                return null;
//            }

            if (urlConn.getResponseCode() != 200) {
                Toast.makeText(activity.getApplicationContext(), "HTTP error code : " + urlConn.getResponseCode(), Toast.LENGTH_SHORT).show();
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((urlConn.getInputStream())));

            buildCurrenciesList(br.readLine());

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
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        notifyCurrenciesReceived(currenciesList);
    }

    public void notifyCurrenciesReceived(List<Currency> currenciesList) {
        mTaskListenerRef.get().onCurrenciesReceived(currenciesList);
    }

    /**
     *
     * @param br
     */
    private void buildCurrenciesList(String br) {
        try {
            JSONObject jsonResponse = new JSONObject(br);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("data");

            for(int i = 0; i<jsonMainNode.length();i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String cname = jsonChildNode.optString("name");
                String cocode = jsonChildNode.optString("country_code");
                String cucode = jsonChildNode.optString("currency_code");
                currenciesList.add(new Currency(cname, cocode, cucode));
            }
        } catch(JSONException e){
            Toast.makeText(activity.getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}