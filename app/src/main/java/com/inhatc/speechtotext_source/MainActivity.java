package com.inhatc.speechtotext_source;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Intent objIntent;
    private SpeechRecognizer mRecognizer;
    private ImageButton btnImgRecording;
    private TextView txtSTT, txtTitle;
    private boolean bRecording=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTitle=(TextView) findViewById(R.id.txtTitle);
        btnImgRecording=(ImageButton) findViewById(R.id.btnImageRecording);
        txtSTT=(TextView) findViewById(R.id.txtShowText);

        CheckPermission();

        objIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        objIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        objIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        btnImgRecording.setOnClickListener(click);
    }

    View.OnClickListener click=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnImageRecording:
                    if(!bRecording){
                        txtSTT.setText("[ Speech To Text ]"+"\n");
                        Start_Record();
                        Toast.makeText(getApplicationContext(), "Start recording your voice...", Toast.LENGTH_SHORT).show();
                    }else{
                        Stop_Record();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    RecognitionListener recognitionListener=new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            String strMessage;
            switch (error){
                case SpeechRecognizer.ERROR_AUDIO:
                    strMessage="Audio error...";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    strMessage="Client error...";
                    return;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    strMessage="Permission error...";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    strMessage="Network Timeout...";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    strMessage="Not found error...";
                    if(bRecording) Start_Record();
                    return;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    strMessage="Recognizer busy...";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    strMessage="Server error...";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    strMessage="Speech timeout...";
                    break;
                default:
                    strMessage="Unknown error...";
                    break;
            }
            Toast.makeText(getApplicationContext(), "eMessage: "+strMessage, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle bundle) {
            ArrayList<String> arrayList_Result;
            arrayList_Result=bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            String[] strResult=new String[arrayList_Result.size()];
            arrayList_Result.toArray(strResult);
            txtSTT.append("\n"+strResult[0]);

            mRecognizer.startListening(objIntent);
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };

    public void Start_Record(){
        bRecording=true;

        btnImgRecording.setImageResource(R.drawable.microphone);
        txtTitle.setText("Click the MIC to stop recording...");
        mRecognizer=SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        mRecognizer.setRecognitionListener(recognitionListener);
        mRecognizer.startListening(objIntent);
    }

    public void Stop_Record(){
        bRecording=false;

        btnImgRecording.setImageResource(R.drawable.microphone);
        txtTitle.setText("Click the MIC to start recording...");
        mRecognizer.stopListening();
        Toast.makeText(getApplicationContext(), "Recording is stop...", Toast.LENGTH_SHORT).show();
    }

    void CheckPermission(){
        if((ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.INTERNET,android.Manifest.permission.RECORD_AUDIO},1);
        }
    }

}