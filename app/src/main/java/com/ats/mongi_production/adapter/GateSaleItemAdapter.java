package com.ats.mongi_production.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.mongi_production.R;
import com.ats.mongi_production.activity.HomeActivity;
import com.ats.mongi_production.constants.Constants;
import com.ats.mongi_production.model.CartListData;
import com.ats.mongi_production.model.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.ats.mongi_production.activity.HomeActivity.cartArray;

public class GateSaleItemAdapter extends BaseAdapter implements Filterable {

    ArrayList<Item> displayedValues;
    ArrayList<Item> originalValues;
    Context context;
    private static LayoutInflater inflater = null;

    RelativeLayout relativeLayout;

    public GateSaleItemAdapter(ArrayList<Item> displayedValues, Context context) {
        this.displayedValues = displayedValues;
        this.originalValues = displayedValues;
        this.context = context;
        this.inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return displayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return displayedValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<Item> filteredArrayList = new ArrayList<Item>();

                if (originalValues == null) {
                    originalValues = new ArrayList<Item>(displayedValues);
                }

                if (charSequence == null || charSequence.length() == 0) {
                    results.count = originalValues.size();
                    results.values = originalValues;
                } else {
                    charSequence = charSequence.toString().toLowerCase();
                    for (int i = 0; i < originalValues.size(); i++) {
                        String name = originalValues.get(i).getItemName();
                        if (name.toLowerCase().startsWith(charSequence.toString()) || name.toLowerCase().contains(charSequence.toString())) {
                            filteredArrayList.add(new Item(originalValues.get(i).getId(), originalValues.get(i).getItemId(), originalValues.get(i).getItemName(), originalValues.get(i).getItemGrp1(), originalValues.get(i).getItemGrp2(), originalValues.get(i).getItemGrp3(), originalValues.get(i).getItemMrp1(), originalValues.get(i).getItemRate2(), originalValues.get(i).getItemMrp1(), originalValues.get(i).getItemMrp2(), originalValues.get(i).getItemMrp3(), originalValues.get(i).getItemImage(), originalValues.get(i).getItemTax1(), originalValues.get(i).getItemTax2(), originalValues.get(i).getItemTax3(), originalValues.get(i).getItemIsUsed(), originalValues.get(i).getItemSortId(), originalValues.get(i).getGrnTwo(), originalValues.get(i).getDelStatus(), originalValues.get(i).getItemRate3(), originalValues.get(i).getMinQty(), originalValues.get(i).getShelfLife(), originalValues.get(i).getQty()));
                        }
                    }
                    results.count = filteredArrayList.size();
                    results.values = filteredArrayList;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                displayedValues = (ArrayList<Item>) filterResults.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }


    public static class Holder {
        TextView tvIcon, tvName, tvAmount, tvStrikeAmount, tvQty, tvPlus, tvMinus, tvAdd;
        ImageView ivImage;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder;
        View rowView = convertView;

        if (rowView == null) {
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.adapter_gate_sale_item, null);

            holder.tvIcon = rowView.findViewById(R.id.tvCustomEmpProduct_Icon);
            holder.tvName = rowView.findViewById(R.id.tvCustomEmpProduct_Name);
            holder.tvAmount = rowView.findViewById(R.id.tvCustomEmpProduct_Price);
            holder.tvStrikeAmount = rowView.findViewById(R.id.tvCustomEmpProduct_CrossPrice);
            holder.ivImage = rowView.findViewById(R.id.ivCustomEmpProduct_Image);
            holder.tvPlus = rowView.findViewById(R.id.tvCustomEmpProduct_Plus);
            holder.tvMinus = rowView.findViewById(R.id.tvCustomEmpProduct_Minus);
            holder.tvQty = rowView.findViewById(R.id.tvCustomEmpProduct_Qty);
            holder.tvAdd = rowView.findViewById(R.id.tvCustomEmpProduct_Add);


            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.tvName.setText("" + displayedValues.get(position).getItemName());

        double remAmt = displayedValues.get(position).getItemMrp1() % 10;
        double tempAmt = 0;
        if (remAmt != 0) {
            tempAmt = remAmt + displayedValues.get(position).getItemMrp1();
        } else {
            tempAmt = displayedValues.get(position).getItemMrp1();
        }


        holder.tvAmount.setText("Rs. " + tempAmt);
        holder.tvStrikeAmount.setText("Rs. " + tempAmt);

        holder.tvStrikeAmount.setPaintFlags(holder.tvStrikeAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.tvQty.setText("" + displayedValues.get(position).getQty());

        String imagePath = Constants.IMAGE_PATH_MSP_CAKE + displayedValues.get(position).getItemImage();
        try {
            Picasso.get().load(imagePath)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.ivImage);
        } catch (Exception e) {
            Log.e("IMAGE : ", "------------------ " + e.getMessage());
            e.printStackTrace();
        }

        holder.tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.tvQty.getText().toString().isEmpty()) {
                    int qty = Integer.parseInt(holder.tvQty.getText().toString());
                    holder.tvQty.setText("" + (qty + 1));
                    displayedValues.get(position).setQty((qty + 1));
                }
            }
        });

        holder.tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.tvQty.getText().toString().isEmpty()) {
                    if (Integer.parseInt(holder.tvQty.getText().toString()) > 1) {
                        int qty = Integer.parseInt(holder.tvQty.getText().toString());
                        holder.tvQty.setText("" + (qty - 1));
                        displayedValues.get(position).setQty((qty - 1));

                    }
                }
            }
        });


        final double finalTempAmt = tempAmt;
        holder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HomeActivity.llCart.setVisibility(View.VISIBLE);

                CartListData data = new CartListData(displayedValues.get(position).getId(), displayedValues.get(position).getItemId(), displayedValues.get(position).getItemName(), displayedValues.get(position).getItemImage(), finalTempAmt, displayedValues.get(position).getQty(), displayedValues.get(position).getItemGrp1(),displayedValues.get(position).getItemGrp2(), displayedValues.get(position).getItemRate1(), displayedValues.get(position).getItemRate2(), displayedValues.get(position).getItemRate3());
                if (cartArray.size() > 0) {
                    boolean flag = false;
                    for (int i = 0; i < cartArray.size(); i++) {

                        if (cartArray.get(i).getId() == data.getId()) {
                            cartArray.get(i).setQuantity((cartArray.get(i).getQuantity() + displayedValues.get(position).getQty()));
                            flag = false;
                            break;
                        } else {
                            flag = true;
                        }
                    }

                    if (flag) {
                        cartArray.add(data);
                        Toast.makeText(context, "" + displayedValues.get(position).getItemName() + " added to cart", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    cartArray.add(data);
                    Toast.makeText(context, "" + displayedValues.get(position).getItemName() + " added to cart", Toast.LENGTH_SHORT).show();
                }


                if (cartArray.size() > 0) {
                    int totalQty = 0;
                    for (int i = 0; i < cartArray.size(); i++) {

                        totalQty = totalQty + cartArray.get(i).getQuantity();
                    }
                    HomeActivity.tvCartCount.setText("" + totalQty);
                }


            }
        });

        return rowView;
    }


}
