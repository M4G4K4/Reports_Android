package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLOutput;
import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    List<Notes> notes;
    LayoutInflater inflater;


    Adapter(Context context , List<Notes> notes){
        this.notes = notes;
        this.inflater = LayoutInflater.from(context);
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_grid,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        String title = notes.get(position).getTitle();
        String description = notes.get(position).getDescription();
        String time = notes.get(position).getTime();
        String date = notes.get(position).getDate();


        holder.title.setText(title);
        holder.description.setText(description);
        holder.time.setText(time);
        holder.date.setText(date);


    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView description;
        TextView time;
        TextView date;
        TextView id;

        public ViewHolder (@NonNull View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.cardTitle);
            description = itemView.findViewById(R.id.cardDescription);
            time = itemView.findViewById(R.id.cardTime);
            date = itemView.findViewById(R.id.cardDate);
            id = itemView.findViewById(R.id.id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(),Edit.class);
                    intent.putExtra("ID", notes.get(getAdapterPosition()).getId());
                    view.getContext().startActivity(intent);

                }
            });

        }
    }











}
