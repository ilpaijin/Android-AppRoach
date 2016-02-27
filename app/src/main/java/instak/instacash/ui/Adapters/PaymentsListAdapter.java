package instak.instacash.ui.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import instak.instacash.Model.Currency;
import instak.instacash.Model.PaymentMethod;
import instak.instacash.R;

public class PaymentsListAdapter extends RecyclerView.Adapter<PaymentsListAdapter.PaymentViewHolder> {

    private List<PaymentMethod> paymentList;
    private Currency selectedCurrency;

    public PaymentsListAdapter(List<PaymentMethod> payments, Currency selectedCurrency) {
        paymentList = payments;
        this.selectedCurrency = selectedCurrency;
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View rowView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.payment_method_single_item, viewGroup, false);

        return new PaymentViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(PaymentViewHolder paymentViewHolder, int position) {

        PaymentMethod p = paymentList.get(position);

        paymentViewHolder.name.setText(p.name);
        paymentViewHolder.price.setText(selectedCurrency.currencyCode + p.price.toString());

        Context ctx = paymentViewHolder.logo.getContext();

        int resourceId = ctx.getResources().getIdentifier( p.label.replaceAll("-", ""), "drawable", ctx.getPackageName());

        if (resourceId == 0) {
            resourceId = ctx.getResources().getIdentifier("no_image_available", "drawable", ctx.getPackageName());
        }

        paymentViewHolder.logo.setImageResource(resourceId);
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView price;
        public ImageView logo;

        public PaymentViewHolder(View v) {
            super(v);

            name = (TextView) v.findViewById(R.id.paymentMethodName);
            price = (TextView) v.findViewById(R.id.bestPrice);
            logo = (ImageView) v.findViewById(R.id.paymentMethodLogo);
        }
    }
}
