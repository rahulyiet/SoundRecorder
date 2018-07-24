package com.example.rahulyiet.soundrecorder;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


  private Button Recorder,Play,Stop;
  private MediaRecorder mediaRecorder;
 private MediaPlayer mediaPlayer;
  private File outputFile=null;

//  private boolean isRecording = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        Recorder = findViewById(R.id.recorder);
//        Play = findViewById(R.id.play);
        Stop = findViewById(R.id.stop);




//        Stop.setEnabled(false);



//        Play.setEnabled(false);


//        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        mediaRecorder.setOutputFile(outputFile);
//
//
//
//        Recorder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//
//                    mediaRecorder.prepare();
//                    mediaRecorder.start();
//                } catch (IllegalStateException ise) {
//
//                } catch (IOException ioe) {
//
//                }
//
//                Recorder.setEnabled(false);
//                Stop.setEnabled(true);
//                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
//            }
//        });
//
//         Stop.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 mediaRecorder.stop();
//                 mediaRecorder.release();
//                 mediaRecorder=null;
//                 Recorder.setEnabled(true);
//                 Stop.setEnabled(false);
//                 Play.setEnabled(true);
//                 Toast.makeText(getApplicationContext(),"Audio recorder stopped",Toast.LENGTH_SHORT).show();
//             }
//         });
//
//         Play.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 MediaPlayer mediaPlayer = new MediaPlayer();
//                 try {
//                     mediaPlayer.setDataSource(outputFile);
//                     mediaPlayer.prepare();
//                     mediaPlayer.start();
//                     Toast.makeText(getApplicationContext(),"Audio play successfully",Toast.LENGTH_SHORT).show();
//                 } catch (IOException e) {
//                     e.printStackTrace();
//                 }
//             }
//         });





    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recordButton(View view) throws IOException {

//        isRecording = true;
        Stop.setEnabled(true);
//        Play.setEnabled(false);
        Recorder.setEnabled(false);
        File dir=Environment.getExternalStorageDirectory();

        try {

            outputFile=File.createTempFile("sound",".3gp",dir);
//
        } catch (Exception e) {
            return;
        }

        mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(outputFile.getAbsolutePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();


    }

    public void playButton(View view) throws IOException {

        Play.setEnabled(false);
        Recorder.setEnabled(false);
        Stop.setEnabled(true);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(String.valueOf(outputFile));
        mediaPlayer.prepare();
        mediaPlayer.start();


    }

    public void stopButton(View view) {

        Recorder.setEnabled(true);
        Stop.setEnabled(false);
        //stopping recorder  
        mediaRecorder.stop();
        mediaRecorder.release();
        //after stopping the recorder, create the sound file and add it to media library.  
        addRecordingToMediaLibrary();
    }

    private void addRecordingToMediaLibrary() {

        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, "audio" + outputFile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, outputFile.getAbsolutePath());

        //creating content resolver and storing it in the external content uri
        ContentResolver contentResolver = getContentResolver();
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);

        //sending broadcast message to scan the media file so that it can be available
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();
    }
}
