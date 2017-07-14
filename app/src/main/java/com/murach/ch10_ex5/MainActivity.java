package com.murach.ch10_ex5;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView messageTextView;
    private Button startButton;
    private Button stopButton;
    Timer timer = new Timer(true);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        startButton = (Button) findViewById(R.id.start);
        stopButton = (Button) findViewById(R.id.stop);
        //set listener
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

        startTimer();
    }
    
    private void startTimer() {
        final long startMillis = System.currentTimeMillis();
        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            
            @Override
            public void run() {
                long elapsedMillis = System.currentTimeMillis() - startMillis;
                updateView(elapsedMillis);
                //method to download file from
                downloadFile();
            }
        };
        timer.schedule(task, 0, 1000);
    }

    public void downloadFile() {
         final String URL_STRING = "http://rss.cnn.com/rss/cnn_tech.rss";
         final String FILENAME = "news_feed.xml";
        try{
            // get the URL
            URL url = new URL(URL_STRING);

            // get the input stream
            InputStream in = url.openStream();

            // get the output stream
            FileOutputStream out =
                    openFileOutput(FILENAME, Context.MODE_PRIVATE);

            // read input and write output
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            while (bytesRead != -1)
            {
                out.write(buffer, 0, bytesRead);
                bytesRead = in.read(buffer);
            }
            out.close();
            in.close();
        }
        catch (IOException e) {
            Log.e("News reader", e.toString());
        }
    }

    private void updateView(final long elapsedMillis) {
        // UI changes need to be run on the UI thread
        messageTextView.post(new Runnable() {

            int elapsedSeconds = (int) elapsedMillis/1000;
            int numOfDownloads = (int) elapsedMillis/1000;

            @Override
            public void run() {
                messageTextView.setText("Seconds: " + elapsedSeconds + "File downloaded("
                + numOfDownloads + ")");
            }
        });
    }

    @Override
    protected void onPause() {
        startTimer();
        super.onPause();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start:
                startTimer();
                break;
            case R.id.stop:
                stopTimer();
                break;
            }
        }

    private void stopTimer() {
        timer.cancel();
    }

}
