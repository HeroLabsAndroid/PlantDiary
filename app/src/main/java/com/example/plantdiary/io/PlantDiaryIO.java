package com.example.plantdiary.io;

import android.content.Context;
import android.util.Log;

import com.example.plantdiary.plant.Plant;
import com.example.plantdiary.plantaction.PlantLogItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class PlantDiaryIO {

    static public ArrayList<DeadPlant> loadDeadPlants(Context con) {
        ArrayList<DeadPlant> dat = new ArrayList<>();

        try {
            FileInputStream fis = con.openFileInput("deadplantlog.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Log.d("RDDAT", String.format(Locale.getDefault(), "Opened file (%s) and Object Stream.", fis.toString()));
            int cnt = (int)ois.readObject();
            for(int i=0; i< cnt; i++) dat.add(DeadPlant.fromSave((DeadPlantSave) ois.readObject()));
            Log.d("RDDAT", String.format(Locale.getDefault(),"Read %d objects.",dat.size()));
            ois.close();
            fis.close();

            dat.sort(DeadPlant::compareTo);
        } catch(Exception e) {
            Log.e("RDDAT", e.toString());
        }

        return dat;
    }



    static public void saveDeadPlants(Context con, ArrayList<DeadPlant> logs) throws JSONException, IOException {
        JSONObject deadplantsave = new JSONObject();

        JSONArray deadplantarr = new JSONArray();

        for(DeadPlant pl: logs) {
            deadplantarr.put(pl.toJSONSave());
        }
        deadplantsave.put("deadplants", deadplantarr);

        FileOutputStream fos = con.openFileOutput("deadplants.dat", Context.MODE_PRIVATE);

        FileWriter fw;
        try {
            fw = new FileWriter(fos.getFD());
            fw.write(deadplantsave.toString());
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.getFD().sync();
            fos.close();
        }

        return jsave.toString();

        /*try {
            FileOutputStream fos = con.openFileOutput("deadplantlog.dat", Context.MODE_PRIVATE);
            Log.d("SAVDAT", "Opened file.");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            Log.d("SAVDAT", "Opened Object Stream");
            oos.writeObject(logs.size());
            for(DeadPlant pl: logs) {
                oos.writeObject(pl.toSave());
            }
            Log.d("SAVDAT", String.format(Locale.getDefault(), "Wrote %d Objects.", logs.size()));
            oos.close();
            fos.close();
        } catch (Exception e) {
            Log.e("SAVDAT", e.toString());
        }*/

    }

    static public ArrayList<Plant> loadData(Context con) {
        ArrayList<Plant> dat = new ArrayList<>();

        try {
            FileInputStream fis = con.openFileInput("plantlog.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Log.d("RDDAT", String.format(Locale.getDefault(), "Opened file (%s) and Object Stream.", fis.toString()));
            int cnt = (int)ois.readObject();
            for(int i=0; i< cnt; i++) dat.add(new Plant((PlantSave) ois.readObject()));
            Log.d("RDDAT", String.format(Locale.getDefault(),"Read %d objects.",dat.size()));
            ois.close();
            fis.close();

            dat.sort(Plant::compareTo);
        } catch(Exception e) {
            Log.e("RDDAT", e.toString());
        }

        return dat;
    }



    static public void saveData(Context con, ArrayList<Plant> logs) {
        try {
            FileOutputStream fos = con.openFileOutput("plantlog.dat", Context.MODE_PRIVATE);
            Log.d("SAVDAT", "Opened file.");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            Log.d("SAVDAT", "Opened Object Stream");
            oos.writeObject(logs.size());
            for(Plant pl: logs) {
                oos.writeObject(pl.toSave());
            }
            Log.d("SAVDAT", String.format(Locale.getDefault(), "Wrote %d Objects.", logs.size()));
            oos.close();
            fos.close();
        } catch (Exception e) {
            Log.e("SAVDAT", e.toString());
        }

    }
}
