package com.rage.bootleggersota.Adapter;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.rage.bootleggersota.Modal.BuildModal;
import com.rage.bootleggersota.R;

import java.io.File;
import java.util.ArrayList;

public class ChangelogAdapter extends RecyclerView.Adapter<ChangelogAdapter.ViewHolder> {

    private ArrayList <BuildModal> list;
    private Context context;

    public ChangelogAdapter(ArrayList<BuildModal> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_build, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final BuildModal item = list.get(i);

        if (!item.isBreakText()) { // default
            viewHolder.title.setText(item.getTitle());
            viewHolder.description.setText(item.getDescription());
            viewHolder.updateSize.setText(item.getSize());
            viewHolder.downloadTouch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Starting Download...", Toast.LENGTH_SHORT).show();
                    viewHolder.downloadTouch.setVisibility(View.GONE);// touch view
                    viewHolder.downloadLayout.setVisibility(View.GONE);// layout
                    viewHolder.downloadProgressBar.setVisibility(View.VISIBLE);// progress bar
                    downloadFile(item.getDownloadLink(), viewHolder, item);
                }
            });
        }
        else {
            viewHolder.build.setVisibility(View.GONE);
            viewHolder.textBreak.setVisibility(View.VISIBLE);
            viewHolder.textBreak.setText(item.getBreakText());
            viewHolder.textBreak.setTextColor(item.getColorBreakText());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, updateSize, description, textBreak;
        public CardView build;
        public View downloadTouch;
        public DonutProgress downloadProgress;
        public ConstraintLayout downloadLayout;
        public ProgressBar downloadProgressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textViewBuildName);
            updateSize = itemView.findViewById(R.id.textViewBuildSize);
            description = itemView.findViewById(R.id.textViewBuildOverview);
            build = itemView.findViewById(R.id.cardViewBuild);
            textBreak = itemView.findViewById(R.id.textViewListBreak);
            downloadTouch = itemView.findViewById(R.id.viewDownload);
            downloadProgress = itemView.findViewById(R.id.progressViewCircular);
            downloadLayout = itemView.findViewById(R.id.constraintLayoutDownload);
            downloadProgressBar = itemView.findViewById(R.id.progressBarDownload);

        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    private int downloadId;
    private String directory = Environment.getExternalStorageDirectory() + "/BootleggersOTA/";
    private boolean isDownloading = false;

    private void downloadFile (String link, final ViewHolder viewHolder, final BuildModal item) {
        File downloaded = new File(Environment.getExternalStorageDirectory() + context.getResources().getString(R.string.directory) + "/download.temp");
        downloaded.delete();
        PRDownloader.initialize(context);
        downloadId = PRDownloader.download(link, directory, "download").build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        viewHolder.downloadProgressBar.setVisibility(View.GONE);
                        viewHolder.downloadProgress.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Download Started", Toast.LENGTH_SHORT).show();
                        Log.e("Download", "Start");
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        //450 of 549 MB Done (2 minutes left) • 83%
                        double downloaded = ((progress.currentBytes / 1000000));
                        double total = (int) ((progress.totalBytes / 1000000));
                        float pp = (float) (downloaded / total);
                        float per = (pp * 100);
                        viewHolder.downloadProgress.setProgress(per);
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show();
                        File downloaded = new File(Environment.getExternalStorageDirectory() + context.getResources().getString(R.string.directory)+ "/download");
                        downloaded.renameTo(new File(Environment.getExternalStorageDirectory() + context.getResources().getString(R.string.directory)  + "/" + item.getFilename()));
                    }

                    @Override
                    public void onError(Error error) {
                        Log.e("Adapter", error.toString());
                        viewHolder.downloadProgress.setVisibility(View.GONE);
                        viewHolder.downloadLayout.setVisibility(View.VISIBLE);
                        viewHolder.downloadTouch.setVisibility(View.VISIBLE);
                        viewHolder.downloadProgressBar.setVisibility(View.GONE);
                        File downloaded = new File(Environment.getExternalStorageDirectory() + context.getResources().getString(R.string.directory) + "/download.temp");
                        downloaded.delete();
                    }
                });
    }

}