package com.example.virtuallibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private List<String> categoryList;
    private OnCategoryClickListener listener;
    private int selectedPosition = 0; // Default to "All" category

    public interface OnCategoryClickListener {
        void onCategoryClick(String category);
    }

    public CategoryAdapter(Context context, List<String> categoryList, OnCategoryClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = categoryList.get(position);
        holder.categoryChip.setText(category);

        // Set selection state
        holder.categoryChip.setChecked(position == selectedPosition);

        // Handle click
        holder.categoryChip.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = position;

            // Notify about changes
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);

            // Trigger callback
            if (listener != null) {
                listener.onCategoryClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        Chip categoryChip;

        CategoryViewHolder(View itemView) {
            super(itemView);
            categoryChip = itemView.findViewById(R.id.categoryChip);
        }
    }
}