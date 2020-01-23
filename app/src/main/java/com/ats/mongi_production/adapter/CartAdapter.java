package com.ats.mongi_production.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ats.mongi_production.R;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.model.CartListData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.ats.mongi_production.activity.HomeActivity.cartArray;
import static com.ats.mongi_production.activity.HomeActivity.tvCartCount;
import static com.ats.mongi_production.fragment.CartFragment.tvGrandTotal;
import static com.ats.mongi_production.fragment.CartFragment.tvTotal;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private ArrayList<CartListData> cartList;
    private Context context;
    boolean isTouched = false;

    public CartAdapter(ArrayList<CartListData> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvWeight, tvQty, tvUnitPrice, tvSubTotal, tvMinusIcon, tvPlusIcon, tvDeleteIcon, tvOfferPrice;
        ImageView ivImage;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvDesc = view.findViewById(R.id.tvDesc);
            tvWeight = view.findViewById(R.id.tvWeight);
            tvQty = view.findViewById(R.id.tvOrderQty);
            tvUnitPrice = view.findViewById(R.id.tvUnitPrice);
            tvSubTotal = view.findViewById(R.id.tvSubTotal);
            tvMinusIcon = view.findViewById(R.id.tvMinusIcon);
            tvPlusIcon = view.findViewById(R.id.tvPlusIcon);
            tvDeleteIcon = view.findViewById(R.id.tvDelete);
            tvOfferPrice = view.findViewById(R.id.tvOfferPrice);
            ivImage = view.findViewById(R.id.ivImage);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cart, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CartListData model = cartList.get(position);
       // Log.e("Adapter : ", " model : " + model);

        holder.tvName.setText("" + model.getItemName());

        String imagePath = Constants.IMAGE_PATH_MSP_CAKE + model.getItemImage();
        try {
            Picasso.get().load(imagePath)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.ivImage);
        } catch (Exception e) {
            Log.e("IMAGE : ", "------------------ " + e.getMessage());
            e.printStackTrace();
        }


        holder.tvUnitPrice.setText("Rs. " + String.format("%.2f", model.getItemRate()));
        holder.tvUnitPrice.setPaintFlags(holder.tvUnitPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        final SharedPreferences pref = context.getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        float percentDiscount = pref.getFloat("catDiscount", 0);
        double disc = model.getItemRate() * (percentDiscount / 100);
        //final double offer = Math.round(displayedValues.get(position).getItemRate() - disc);
        final double offer = model.getItemRate() - disc;
        holder.tvOfferPrice.setText("Rs. " + String.format("%.2f", offer));
        holder.tvSubTotal.setText("Rs. " + String.format("%.2f", (offer * model.getQuantity())));


        holder.tvQty.setText("" + model.getQuantity());

        holder.tvPlusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.tvQty.getText().toString().isEmpty()) {
                    int qty = Integer.parseInt(holder.tvQty.getText().toString());
                    holder.tvQty.setText("" + (qty + 1));
                    model.setQuantity((qty + 1));

                    //holder.tvSubTotal.setText("Rs. " + (displayedValues.get(position).getItemRate() * displayedValues.get(position).getQuantity()));
                    holder.tvSubTotal.setText("Rs. " + String.format("%.2f", (offer * model.getQuantity())));

                    int totalQty = 0;
                    double totalSum = 0;
                    for (int i = 0; i < cartArray.size(); i++) {
                        totalSum = totalSum + (cartArray.get(i).getQuantity() * cartArray.get(i).getItemRate());
                        totalQty = totalQty + cartArray.get(i).getQuantity();
                    }
                    tvCartCount.setText("" + totalQty);
                    //tvTotal.setText("" + Math.round(totalSum));
                    tvTotal.setText("" + String.format("%.2f", totalSum));

                    float percentDiscount = pref.getFloat("catDiscount", 0);
                    //  if (percentDiscount > 0) {
                    double disc = totalSum * (percentDiscount / 100);
                    double grandTot = Math.round(totalSum - disc);
                    //tvGrandTotal.setText("" + grandTot);
                    tvGrandTotal.setText("" + String.format("%.2f", (totalSum - disc)));

                }
            }
        });

        holder.tvMinusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.tvQty.getText().toString().isEmpty()) {
                    if (Integer.parseInt(holder.tvQty.getText().toString()) > 1) {
                        int qty = Integer.parseInt(holder.tvQty.getText().toString());
                        holder.tvQty.setText("" + (qty - 1));
                        model.setQuantity((qty - 1));

                        // holder.tvSubTotal.setText("Rs. " + (displayedValues.get(position).getItemRate() * displayedValues.get(position).getQuantity()));
                        holder.tvSubTotal.setText("Rs. " + String.format("%.2f", (offer * model.getQuantity())));

                        int totalQty = 0;
                        double totalSum = 0;
                        for (int i = 0; i < cartArray.size(); i++) {
                            totalSum = totalSum + (cartArray.get(i).getQuantity() * cartArray.get(i).getItemRate());
                            totalQty = totalQty + cartArray.get(i).getQuantity();
                        }
                        tvCartCount.setText("" + totalQty);
                        //tvTotal.setText("" + Math.round(totalSum));
                        tvTotal.setText("" + String.format("%.2f", totalSum));


                        float percentDiscount = pref.getFloat("catDiscount", 0);
                        //if (percentDiscount > 0) {
                        double disc = totalSum * (percentDiscount / 100);
                        double grandTot = Math.round(totalSum - disc);
                        //tvGrandTotal.setText("" + grandTot);
                        tvGrandTotal.setText("" + String.format("%.2f", (totalSum - disc)));
                        // }
                    }
                }
            }
        });


        holder.tvDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                builder.setTitle("Confirm Action");
                builder.setMessage("Remove " + model.getItemName() + " from cart");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int count = Integer.parseInt(tvCartCount.getText().toString());
                        tvCartCount.setText("" + (count - model.getQuantity()));


                        cartArray.remove(model);
                        notifyDataSetChanged();

                        double totalSum = 0;
                        for (int i = 0; i < cartArray.size(); i++) {
                            totalSum = totalSum + (cartArray.get(i).getQuantity() * cartArray.get(i).getItemRate());
                        }
                        //tvTotal.setText("" + Math.round(totalSum));
                        tvTotal.setText("" + String.format("%.2f", totalSum));


                        float percentDiscount = pref.getFloat("catDiscount", 0);
                        // if (percentDiscount > 0) {
                        double disc = totalSum * (percentDiscount / 100);
                        double grandTot = Math.round(totalSum - disc);
                        tvGrandTotal.setText("" + String.format("%.2f", (totalSum - disc)));
                        //}


                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

}
