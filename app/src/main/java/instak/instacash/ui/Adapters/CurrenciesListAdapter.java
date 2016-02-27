package instak.instacash.ui.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import instak.instacash.Model.Currency;
import instak.instacash.R;

public class CurrenciesListAdapter extends ArrayAdapter<Currency> {

    private Activity context;
    private List<Currency> currenciesList;

    public CurrenciesListAdapter(Activity context, int resource, List<Currency> currencies) {
        super(context, resource, currencies);
        this.context = context;
        this.currenciesList = currencies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.currencies_spinner, parent, false);
        }

        TextView CountryName = (TextView) convertView.findViewById(R.id.countryName);

        CountryName.setText(currenciesList.get(position).countryName);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.currencies_dropdown_spinner, parent, false);
        }

        TextView CountryName = (TextView) convertView.findViewById(R.id.countryName);

        CountryName.setText(currenciesList.get(position).countryName);

        return convertView;
    }
}
