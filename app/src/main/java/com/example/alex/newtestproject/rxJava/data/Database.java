package com.example.alex.newtestproject.rxJava.data;


import com.example.alex.newtestproject.App;
import com.example.alex.newtestproject.rxJava.fragment.view.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

/**
 * Created by Administrator on 2017/6/8.
 *
 */
public class Database {
    private static String DATA_FILE_NAME = "data.db";

    File dataFile = new File(App.getInstance().getFilesDir(), DATA_FILE_NAME);
    Gson gson = new Gson();

    private Database() {
    }

    public static Database getInstance() {
        return Holder.instance;
    }

    public void writeItems(List<Item> items) {
        String json = gson.toJson(items);
        // 存入json 以文件的形式
        try {
            if (!dataFile.exists()) {
                dataFile.createNewFile();
            }
            Writer writer = new FileWriter(dataFile);
            writer.write(json);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Item> readItems() {
        // Hard code adding some delay, to distinguish reading from memory and reading disk clearly
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Reader reader = new FileReader(dataFile);
            return gson.fromJson(reader, new TypeToken<List<Item>>() {
            }.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete() {
        dataFile.delete();
    }

    private static class Holder {
        private static Database instance;

        static {
            instance = new Database();
        }
    }
}
