package com.prayanshu.billreminder;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BillAdapter
        extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

    Context context;

    ArrayList<Bill> list;

    DBHelper db;

    RecyclerView recyclerView;

    TextView tvEmpty;

    public BillAdapter(

            Context context,

            ArrayList<Bill> list,

            DBHelper db,

            RecyclerView recyclerView,

            TextView tvEmpty

    ) {

        this.context = context;

        this.list = list;

        this.db = db;

        this.recyclerView = recyclerView;

        this.tvEmpty = tvEmpty;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(

            @NonNull ViewGroup parent,

            int viewType
    ) {

        View view =
                LayoutInflater.from(context)

                        .inflate(

                                R.layout.item_bill,

                                parent,

                                false
                        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(

            @NonNull ViewHolder holder,

            int position
    ) {

        Bill bill = list.get(position);

        // ===== DATA =====

        holder.tvName.setText(
                bill.getName()
        );

        holder.tvAmount.setText(
                "₹ " + bill.getAmount()
        );

        holder.tvDate.setText(
                "Due: " + bill.getDueDate()
        );

        // ===== STATUS =====

        if (bill.getIsPaid() == 1) {

            holder.tvStatus.setText("PAID ✅");

            holder.tvStatus.setTextColor(
                    Color.parseColor("#10B981")
            );

            holder.btnPaid.setText("Paid");

            holder.btnPaid.setEnabled(false);

            holder.btnPaid.setBackgroundTintList(

                    ColorStateList.valueOf(
                            Color.GRAY
                    )
            );

        }

        else {

            holder.tvStatus.setText("UNPAID");

            holder.tvStatus.setTextColor(
                    Color.parseColor("#EF4444")
            );

            holder.btnPaid.setEnabled(true);

            holder.btnPaid.setText(
                    "Mark Paid"
            );

            holder.btnPaid.setBackgroundTintList(

                    ColorStateList.valueOf(
                            Color.parseColor("#4F46E5")
                    )
            );

            holder.btnPaid.setOnClickListener(v -> {

                db.markPaid(
                        bill.getId()
                );

                bill.setIsPaid(1);

                notifyItemChanged(position);

                Toast.makeText(

                        context,

                        "Bill Marked Paid",

                        Toast.LENGTH_SHORT

                ).show();
            });
        }

        // ===== DELETE =====

        holder.itemView.setOnLongClickListener(v -> {

            new AlertDialog.Builder(context)

                    .setTitle("Delete Bill")

                    .setMessage(
                            "Delete this bill?"
                    )

                    .setPositiveButton(

                            "Delete",

                            (dialog, which) -> {

                                db.deleteBill(
                                        bill.getId()
                                );

                                list.remove(
                                        holder.getAdapterPosition()
                                );

                                notifyDataSetChanged();

                                Toast.makeText(

                                        context,

                                        "Bill Deleted",

                                        Toast.LENGTH_SHORT

                                ).show();

                                // ===== EMPTY STATE =====

                                if (list.isEmpty()) {

                                    recyclerView.setVisibility(
                                            View.GONE
                                    );

                                    tvEmpty.setVisibility(
                                            View.VISIBLE
                                    );
                                }
                            })

                    .setNegativeButton(
                            "Cancel",
                            null
                    )

                    .show();

            return true;
        });

        // ===== EDIT =====

        holder.itemView.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            context,
                            AddBillActivity.class
                    );

            intent.putExtra(
                    "id",
                    bill.getId()
            );

            intent.putExtra(
                    "name",
                    bill.getName()
            );

            intent.putExtra(
                    "amount",
                    bill.getAmount()
            );

            intent.putExtra(
                    "date",
                    bill.getDueDate()
            );

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    // ===== VIEW HOLDER =====

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvName,
                tvAmount,
                tvDate,
                tvStatus;

        Button btnPaid;

        public ViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            tvName =
                    itemView.findViewById(
                            R.id.tvName
                    );

            tvAmount =
                    itemView.findViewById(
                            R.id.tvAmount
                    );

            tvDate =
                    itemView.findViewById(
                            R.id.tvDate
                    );

            tvStatus =
                    itemView.findViewById(
                            R.id.tvStatus
                    );

            btnPaid =
                    itemView.findViewById(
                            R.id.btnPaid
                    );
        }
    }
}