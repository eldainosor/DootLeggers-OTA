package com.rage.dootleggersota.Fragments;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.rage.dootleggersota.Adapter.ChangelogAdapter;
import com.rage.dootleggersota.Modal.ChangelogModal;
import com.rage.dootleggersota.R;

import java.io.File;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class UpdateFragment extends Fragment {

    private TextView updateName, updateDate, updateCodenmae, progressText;
    private ArrayList<ChangelogModal> changelog = new ArrayList<>();
    private RoundCornerProgressBar progressBar;
    private ArrayList<String> data;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private final String DIRECTORY = Environment.getExternalStorageDirectory() + "/BootleggersOTA/";
    public boolean downloadStarted = false;
    private boolean isDownloading = false;
    private Button download;
    private int downloadId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_update, container, false);
        defineObjects(layout);
        setValues();
        return  layout;
    }

    private void defineObjects (View layout) {
        updateName = layout.findViewById(R.id.textViewUpdateName);
        updateDate = layout.findViewById(R.id.textViewUpdateDate);
        updateCodenmae = layout.findViewById(R.id.textViewUpdateCodename);
        recyclerView = layout.findViewById(R.id.recyclerViewChangelog);
        progressBar = layout.findViewById(R.id.roundCornerProgressBarDownloadProgress);
        progressText = layout.findViewById(R.id.textViewUpdateProgressDownload);
        //450 of 549 MB Done (2 minutes left) • 83%
        download = layout.findViewById(R.id.buttonDownload);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!downloadStarted) {
                    downloadFile();
                    download.setText("Starting...");
                    downloadStarted = true;
                    isDownloading = true;
                }
                else if (isDownloading) {
                    PRDownloader.pause(downloadId);
                    download.setText("Resume");
                    isDownloading = false;
                }
                else if (!isDownloading) {
                    PRDownloader.resume(downloadId);
                    download.setText("Pause");
                    isDownloading = true;
                }

            }
        });
    }

    private void setValues () {
        if (getArguments() != null) {
            data = getArguments().getStringArrayList("data");
            String versionnumber = "Bootleggers v" + data.get(0).substring(data.get(0).indexOf('=')+1);
            String versionDate = convertToString(data.get(1).substring(data.get(1).lastIndexOf("-")+1));
            String codeName = data.get(2).substring(data.get(2).lastIndexOf('=')+1);
            updateName.setText(versionnumber);
            updateDate.setText(versionDate);
            updateCodenmae.setText(codeName);
            //changelog
            makeChangelog();
            adapter = new ChangelogAdapter(changelog, getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        }
    }

    private String convertToString (String code) {
        String year = code.substring(0, 4);
        int monthCode = Integer.parseInt(code.substring(4, 6));
        String day = code.substring(6);
        String month = "";
        switch (monthCode) {
            case 1 : month = "January";
                     break;
            case 2 : month = "February";
                     break;
            case 3 : month = "March";
                     break;
            case 4 : month = "April";
                     break;
            case 5 : month = "May";
                     break;
            case 6 : month = "June";
                     break;
            case 7 : month = "July";
                     break;
            case 8 : month = "August";
                     break;
            case 9 : month = "September";
                     break;
            case 10 : month = "October";
                     break;
            case 11 : month = "November";
                     break;
            case 12 : month = "December";
                     break;
        }
        return month + " " + day + ", " + year;
    }

    private void makeChangelog () {
        int pos = 6; // changelog starts at 6th pos
        for (int i = pos; i < data.size(); i = i+2) {
            ChangelogModal obj = new ChangelogModal(data.get(i), data.get(i+1));
            changelog.add(obj);
        }
    }

    private void downloadFile () {
        File folder = new File(DIRECTORY);
        folder.mkdirs();
        PRDownloader.initialize(getContext());
        String link = data.get(4).substring(data.get(4).indexOf("=") + 1).trim();
        String filename = "download.temp";
        downloadId = PRDownloader.download(link, DIRECTORY, filename).build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        progressBar.setVisibility(View.VISIBLE);
                        progressText.setVisibility(View.VISIBLE);
                        download.setText("Pause");
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        //450 of 549 MB Done (2 minutes left) • 83%
                        int downloadedInt = (int) ((progress.currentBytes / 1000000));
                        int totalInt = (int) ((progress.totalBytes / 1000000));
                        double downloaded = ((progress.currentBytes / 1000000));
                        double total = (int) ((progress.totalBytes / 1000000));
                        float pp = (float) (downloaded / total);
                        int per = (int) (pp * 100);
                        String tt = downloadedInt + " MB of " + totalInt + " MB Done • " + per + "%";
                        progressText.setText(tt);
                        progressBar.setProgress(per);
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        String finalfilename = data.get(1).substring(data.get(1).indexOf("=") + 1).trim() + ".zip";
                        File downloadedFile = new File(DIRECTORY + "/download.temp");
                        downloadedFile.renameTo(new File(DIRECTORY + finalfilename));
                        progressText.setText("Completed.\nFile is Located in sdcard/BootleggersOTA/");
                        progressBar.setProgress(100f);
                        download.setVisibility(View.GONE);
                        isDownloading = false;
                    }

                    @Override
                    public void onError(Error error) {
                        progressText.setText("Error in downloading file!");
                        Log.e("UpdateFragment", error.toString());
                        progressBar.setActivated(false);
                        File downloadedFile = new File(DIRECTORY + "/download.temp");
                        downloadedFile.delete();
                        isDownloading = false;
                    }
                });

    }


}
