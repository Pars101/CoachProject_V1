package com.example.mrleo.coachproect;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

public class AlarmCardManager {
    private static final String UUID_FILE_NAME = "alarmcardid";

    public static void saveAlarmCardId(UUID cardId){
        Context context = MainApplication.getAppContext();
        try {
            FileOutputStream fout = context.openFileOutput(UUID_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(cardId);
            oos.close();
            fout.close();
        } catch(NotSerializableException e){
            Log.i("Not Serializable", e.getMessage());
        } catch(InvalidClassException e){
            Log.i("Invalid Class", e.getMessage());
        } catch(IOException e){
            Log.i("IO Class", e.getMessage());
        }
    }

    public static UUID readAlarmCardId(){
        Context context = MainApplication.getAppContext();
        File file = context.getFileStreamPath(UUID_FILE_NAME);
        if(file != null && file.exists()) {
            try {
                FileInputStream fis = context.openFileInput(UUID_FILE_NAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
                UUID id = (UUID) ois.readObject();
                ois.close();
                fis.close();
                return id;
            } catch (IOException e) {
                Log.i("IOExceptionCardSet", e.getMessage());
            } catch (ClassNotFoundException e) {
                Log.i("ClassNotFound", e.getMessage());
            }
        }

        return null;
    }
}
