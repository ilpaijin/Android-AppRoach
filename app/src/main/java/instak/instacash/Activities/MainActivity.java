package instak.instacash.Activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import instak.instacash.Model.Currency;
import instak.instacash.Model.PaymentMethod;
import instak.instacash.R;
import instak.instacash.Tasks.GetCurrenciesTask;
import instak.instacash.Tasks.GetPaymentMethodsTask;
import instak.instacash.ui.Adapters.CurrenciesListAdapter;
import instak.instacash.ui.Adapters.PaymentsListAdapter;
import instak.instacash.utils.util;

public class MainActivity extends AppCompatActivity implements
        GetPaymentMethodsTask.TaskListener,
        GetCurrenciesTask.TaskListener {

    public List<Currency> currenciesList;
    public Spinner currenciesListSpinner;
    public RecyclerView paymentsListView;
    public CollapsingToolbarLayout collapsingToolbar;
    public TextInputLayout btcAmountEditText;
    private FloatingActionButton getOfferBtn;
    private Currency selectedCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbar.setTitle(" ");

        paymentsListView = (RecyclerView) findViewById(R.id.paymentMethodsListView);
        paymentsListView.setHasFixedSize(true);
        paymentsListView.setLayoutManager(llm);

        currenciesListSpinner = (Spinner) findViewById(R.id.currenciesSpinner);
        currenciesListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                int item = currenciesListSpinner.getSelectedItemPosition();

                if (item != 0) {
                    selectedCurrency = currenciesList.get(item);
                    Toast.makeText(getApplicationContext(), "selected "+ currenciesList.get(item).countryName, 30).show();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {}
        });

            new GetCurrenciesTask(MainActivity.this, MainActivity.this).execute();

        getOfferBtn = (FloatingActionButton) findViewById(R.id.getOfferBtn);
        getOfferBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btcAmountEditText = (TextInputLayout) findViewById(R.id.btcAmountWrapper);
//                btcAmountEditText.setHint(getString(R.string.crypto_amount_to_sell));
                String btcAmount = btcAmountEditText.getEditText().getText().toString();

                if (btcAmount.matches("")) {
                    Toast.makeText(getApplicationContext(), "You did not enter a BTC amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedCurrency == null || selectedCurrency.currencyCode.matches("")) {
                    Toast.makeText(getApplicationContext(), "You did not selected a country", Toast.LENGTH_SHORT).show();
                    return;
                }

                collapsingToolbar.setTitle(" BTC " + btcAmount);

                util.hideKeyboard(MainActivity.this, paymentsListView);

                new GetPaymentMethodsTask(MainActivity.this, MainActivity.this, selectedCurrency, btcAmount).execute();
            }
        });
    }

    @Override
    public void onPaymentMethodsReceived(List<PaymentMethod> paymentsList) {

        if (paymentsList.size() == 0) {
            TextView emptyList = (TextView) findViewById(R.id.emptyList);
            emptyList.setText("No payments found");
        }

        paymentsListView.setAdapter(new PaymentsListAdapter(paymentsList, selectedCurrency));
    }

    @Override
    public void onCurrenciesReceived(List<Currency> currenciesList) {
        this.currenciesList = currenciesList;

        CurrenciesListAdapter adapter = new CurrenciesListAdapter(MainActivity.this, R.layout.currencies_spinner, currenciesList);
        adapter.insert(new Currency("Select a country", "", ""), 0);

        currenciesListSpinner.setAdapter(adapter);
        currenciesListSpinner.setSelection(0);
    }
}
