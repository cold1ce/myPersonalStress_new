package com.fimrc.mysensornetwork.sensors.time.audio;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.fimrc.sensorfusionframework.sensors.SensorTimeController;
import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;

import java.util.Date;

/**
 * Created by Sven on 24.02.2017.
 */

public class AudioController extends SensorTimeController {

    private final int CENTRE_POINT = 32768;
    private final int AA_adjust = 3;
    int sample_rate = 8000;
    int bufferSize;
    AudioRecord p;
    short[] output = null;

    public AudioController(AudioModule module){
        super(module);

        boolean buffer_error = false;

        // determine required buffer size
        bufferSize = AudioRecord.getMinBufferSize(sample_rate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

        // actually use sample_rate buffer size
        if (bufferSize<sample_rate)
            bufferSize = sample_rate;

        // start player and see if it's there!
        do{
            try{
                p = new AudioRecord(MediaRecorder.AudioSource.MIC, sample_rate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
                if (p != null)
                    if (p.getState() == AudioRecord.STATE_INITIALIZED){
                        // reserve memory for recorded output
                        output = new short[sample_rate];
                        // release player again until needed
                        p.release();
                        p = null;
                        buffer_error = false;
                    }
            }catch(Exception e){
                buffer_error = true;
                // increase buffer size by 10%
                bufferSize += bufferSize/10;
            }
        }while(buffer_error == true);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Date date = new Date(System.currentTimeMillis());
        SensorRecord record = new SensorRecord(module.getNextIndex(), date , structure);
        record.addData("frequency", frequency());
        record.addData("amplitude", amplitude());
        module.log(record);
    }

    private String frequency(){
        long    frequency = -1;
        long frequency_new = 0;
        int i, recorded, read, level=0;
        boolean sign = false;

        // now record
        try{
            // get player resources
            p = new AudioRecord(MediaRecorder.AudioSource.MIC, sample_rate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, sample_rate * 4);
            if (p != null)
                if (p.getState() == AudioRecord.STATE_INITIALIZED){
                    // start recording
                    p.startRecording();

                    // try to read from AudioRecord player until sample_rate buffer is full!
                    i = 0;
                    while (i < sample_rate){
                        // try to always read the buffer size we gave originally
                        read = bufferSize;
                        if (read>sample_rate-i)
                            read = sample_rate-i;
                        // now read
                        recorded = p.read(output, i, read);
                        // error in reading?
                        if (recorded == AudioRecord.ERROR_INVALID_OPERATION) {
                            return "AudioHandler:invalid operation!";
                        }
                        i += recorded;
                    }
                    // stop and release player again
                    p.stop();
                    p.release();
                    p = null;

                    for (i=0;i<sample_rate;i++){
                        if (output[i]<0 && sign == true){
                            frequency_new++;
                            sign = false;
                        }else if (output[i]>=0 && sign == false){
                            frequency_new++;
                            sign = true;
                        }
                        // count amplitude level
                        level += CENTRE_POINT-(long)(Math.abs((int)output[i]));
                    }
                    // now determine level
                    level /= sample_rate;

                    // volume too low?
                    if (level<10)
                        frequency_new = 0;
                    else
                        // now determine frequency by half of all counted spikes
                        frequency_new /= 2;
                }
        }catch(Exception e){
            return "AudioHandler:invalid operation!";
        }

        return String.valueOf(frequency_new);
    }

    private String amplitude(){
        double level_new = 0, ampl;
        double level_d;
        int i, recorded, read;

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        // now record
        try{
            // get player resources
            p = new AudioRecord(MediaRecorder.AudioSource.MIC, sample_rate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
            if (p != null)
                if (p.getState() == AudioRecord.STATE_INITIALIZED){
                    // start recording
                    p.startRecording();

                    // try to read from AudioRecord player until sample_rate buffer is full, i.e., one second of data!
                    i = 0;
                    while (i < sample_rate){
                        // try to always read the buffer size we gave originally
                        read = bufferSize;
                        if (read>sample_rate-i)
                            read = sample_rate-i;
                        // now read
                        recorded = p.read(output, i, read);
                        // error in reading?
                        if (recorded == AudioRecord.ERROR_INVALID_OPERATION){
                            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT);
                            return "AudioHandler:invalid operation!";
                        }
                        i += recorded;
                    }
                    // stop and release player again
                    p.stop();
                    p.release();
                    p = null;

                    level_new = 0;
                    // count changes in sign
                    for (i=0;i<output.length;i++){
                        // given that the PCM encoding delivers unsigned SHORTS, we need to convert the amplitude around the 32768 centre point!
			    		//ampl = (long)(CENTRE_POINT-Math.abs(output[i]));
                        ampl = (long)Math.abs(output[i]);

                        // now ampl*ampl for field force
                        level_new += ampl * ampl;
                    }

                    // compute decibel through 10*log10 (A1^2/A0^2)
                    level_d = 10 * Math.log10((double)level_new / (double)output.length) + (double)AA_adjust;
                    level_new = level_d;
                }
        }catch(Exception e){

            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT);
            return "AudioHandler:invalid operation!";
        }

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT);

        return String.valueOf(level_new);
    }



}
