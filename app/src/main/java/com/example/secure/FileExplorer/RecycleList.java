package com.example.secure.FileExplorer;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.secure.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class RecycleList extends RecyclerView.Adapter<RecycleList.ViewHolder> {

    private LinkedList<Model> data/* = new LinkedList<>()*/;
    private Updater update;
    private MainActivity mainContext;

    //ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public interface Updater {
        void updateCallBack(String pathName);

        void longClick(String pathName);
    }

    public RecycleList(LinkedList<Model> data, Updater update, MainActivity mainContext) {
        this.data = data;
        this.update = update;
        this.mainContext = mainContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listItem = inflater.inflate(R.layout.card_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.filePath.setText(data.get(position).getItemName());

        if(Cache.Instance.gettMap().containsKey(data.get(position).getItemPath())){
            System.out.println("Il dato c'è");
            //double start = System.nanoTime();
            long temp = Cache.Instance.gettMap().get(data.get(position).getItemPath());
            //double end = System.nanoTime();
            //System.out.println("Item: "+data.get(position).getItemName()+" Cache Time: "+(end - start)+" nanoSec");
            holder.itemSize.setText(readableFileSize(temp));
        }else{
            System.out.println("Il dato non c'è");
            AsyncLoad async = new AsyncLoad(holder.itemSize, data.get(position));
            async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //executor.execute(new ItemSize(holder.itemSize, data.get(position), mainContext));
        }

        holder.setImage(data.get(position).getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update.updateCallBack(data.get(position).getItemName());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                update.longClick(data.get(position).getItemName());
                return true;
            }
        });
    }

    public String readableFileSize(long size) {
        if(size <= 0)
            return "0 B";

        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView filePath;
        TextView itemSize;
        ImageView image;
        LinearLayout linLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.filePath = itemView.findViewById(R.id.fileName);
            this.itemSize = itemView.findViewById(R.id.itemSize);
            this.image = itemView.findViewById(R.id.imgV);
            this.linLayout = itemView.findViewById(R.id.linearLayout);
        }

        public void setImage(final int downloadUri)
        {
            Glide.with(itemView.getContext()).load(downloadUri).into(image);
        }
    }
}
