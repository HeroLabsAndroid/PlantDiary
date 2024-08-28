package com.example.plantdiary.io;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.example.plantdiary.plant.Plant;
import com.example.plantdiary.plantaction.PlantLogItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class PlantDiaryIO {
    static public ArrayList<DeadPlant> deadplants_from_json(String s) throws JSONException {
        ArrayList<DeadPlant> dparr = new ArrayList<>();

        JSONObject jsave = new JSONObject(s);
        JSONArray jdparr = jsave.getJSONArray("deadplants");
        for(int i=0; i<jdparr.length(); i++) {
            dparr.add(new DeadPlant(jdparr.getJSONObject(i)));
        }

        return dparr;
    }

    static private ArrayList<Plant> plants_from_json(String s) throws JSONException {
        ArrayList<Plant> parr = new ArrayList<>();

        JSONObject jsave = new JSONObject(s);
        JSONArray jparr = jsave.getJSONArray("plants");
        for(int i=0; i<jparr.length(); i++) {
            parr.add(new Plant(jparr.getJSONObject(i)));
        }

        return parr;
    }
    static public ArrayList<DeadPlant> loadDeadPlants(Context con) {
        try {
            FileInputStream fis = con.openFileInput("deadplants.dat");
            FileReader fileReader = new FileReader(fis.getFD());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();

            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();// This response will have Json Format String

            String response = stringBuilder.toString();

            Log.d("JSON DATA (LOADED)", response);

            return deadplants_from_json(response);
        } catch (Exception e) {
            Log.e("DEADPLANT_FROM_JSON", e.getLocalizedMessage());
            return new ArrayList<>();
        }


    }

    static public ArrayList<Plant> loadData(Context con) {
        File todel = new File("deadplants.dat");
        if(todel.exists()) todel.delete();
        try {
            FileInputStream fis = con.openFileInput("plantlog.dat");
            FileReader fileReader = new FileReader(fis.getFD());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();

            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();// This response will have Json Format String

            String response = stringBuilder.toString();

            Log.d("JSON DATA (LOADED)", response);

            return plants_from_json(response);
        } catch (Exception e) {
            Log.e("PLANT_FROM_JSON", e.getLocalizedMessage());
            return new ArrayList<>();
        }


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
            Log.d("JSON DATA (SAVED)", deadplantsave.toString());
        }
    }


    static public void saveData(Context con, ArrayList<Plant> logs) throws IOException, JSONException {
        JSONObject plantsave = new JSONObject();

        JSONArray plantarr = new JSONArray();

        for(Plant pl: logs) {
            plantarr.put(pl.toJSONSave());
        }
        plantsave.put("plants", plantarr);


        FileOutputStream fos = con.openFileOutput("plantlog.dat", Context.MODE_PRIVATE);
        Log.d("SAVDAT", "Opened file.");

        FileWriter fw;
        try {
            fw = new FileWriter(fos.getFD());
            fw.write(plantsave.toString());
            fw.close();
            Log.d("JSON DATA (SAVED)", plantsave.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.getFD().sync();
            fos.close();
        }

    }

    static public boolean exportData(ArrayList<Plant> plantLogs, ArrayList<DeadPlant> deadplantlogs, Uri uri, ContentResolver conres) {
        try {
            String state = Environment.getExternalStorageState();
            if(!Environment.MEDIA_MOUNTED.equals(state)) {
                return false;
            }

            JSONObject save = new JSONObject();

            JSONArray plantarr = new JSONArray();
            JSONArray deadplantarr = new JSONArray();

            for(Plant pl: plantLogs) {
                plantarr.put(pl.toJSONSave());
            }
            save.put("plants", plantarr);
            for(DeadPlant pl: deadplantlogs) {
                deadplantarr.put(pl.toJSONSave());
            }
            save.put("deadplants", deadplantarr);

            ParcelFileDescriptor pfd = conres.openFileDescriptor(uri, "w");
            assert pfd != null;
            FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());


            fos.write(save.toString().getBytes());
            fos.flush();
            fos.close();
            pfd.close();
            return true;
        } catch(Exception e) {
            Log.e("DATEXPORT", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();
            return false;
        }
    }

    static public FullData data_from_json(String s) {
        try {
            JSONObject jsave = new JSONObject(s);
            JSONArray plarr = jsave.getJSONArray("plants");
            JSONArray dplarr = jsave.getJSONArray("deadplants");

            JSONObject plantsave = new JSONObject();
            plantsave.put("plants", plarr);
            ArrayList<Plant> plants = plants_from_json(plantsave.toString());

            JSONObject deadplantsave = new JSONObject();
            deadplantsave.put("deadplants", dplarr);
            ArrayList<DeadPlant> deadplants = deadplants_from_json(deadplantsave.toString());

            return new FullData(plants, deadplants);
        } catch(Exception e) {
            Log.e("DATA_FROM_JSON", "error creating data arrays from json object: "+e.getMessage());
            return new FullData(new ArrayList<Plant>(), new ArrayList<DeadPlant>());
        }


    }

    static public FullData importData(Uri uri, ContentResolver conres) {
        try {
            String state = Environment.getExternalStorageState();
            if(!Environment.MEDIA_MOUNTED.equals(state)) {
                return new FullData(new ArrayList<Plant>(), new ArrayList<DeadPlant>());
            }
            ParcelFileDescriptor pfd = conres.openFileDescriptor(uri, "r");
            assert pfd != null;
            FileInputStream fis = new FileInputStream(pfd.getFileDescriptor());

            FileReader fileReader = new FileReader(fis.getFD());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();

            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();// This response will have Json Format String

            String response = stringBuilder.toString();

            fis.close();
            pfd.close();
            return data_from_json(response);
        } catch(Exception e) {
            Log.e("DATEXPORT", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();
            return new FullData(new ArrayList<Plant>(), new ArrayList<DeadPlant>());
        }
    }




}
