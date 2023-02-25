package com.samar.location.owner_requestlist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.samar.location.R;
import com.samar.location.models.Tenant;

import java.util.List;

public class Customer_Adapter extends RecyclerView.Adapter<Customer_Adapter.MyViewHolder>
{   Context context;
    List<Tenant> tenantList;
    List<String> houseNos;

    public Customer_Adapter(Context context, List<Tenant> tenantList, List<String> houseNos) {
        this.context = context;
        this.houseNos = houseNos;
        this.tenantList=tenantList;
    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.cus_layout,parent, false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

      holder.houseNo.setText("Your house, "+houseNos.get(position)+" is rented.");
      Tenant tenant=tenantList.get(position);
      holder.tenantinfo.setText("Tenant Name : "+tenant.getName()+"\n"+
              "Email : "+tenant.getEmail()+"\n"+
              "PaymentDate : "+tenant.getPaymentDate()+"\n"+
              "ShiftingDate : "+tenant.getShiftingDate()+"\n"+
              "Amount Paid : "+tenant.getAmtPaid()+"\n"+
              "Have a nice day!");









    }

    @Override
    public int getItemCount() {
        return tenantList.size();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

     TextView houseNo , tenantinfo;
     Button callTenant;
        public MyViewHolder( View view) {
            super(view);

             houseNo=view.findViewById(R.id.houseNo);
             tenantinfo=view.findViewById(R.id.tenantinfo);
             callTenant=view.findViewById(R.id.call_tenant);


             callTenant.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Tenant tenant= tenantList.get(getAdapterPosition());
                     String phone="tel:"+tenant.getPhone();
                     Intent intent=new Intent(Intent.ACTION_CALL);
                     intent.setData(Uri.parse(phone));
                     context.startActivity(intent);
                 }
             });
        }
    }
}
