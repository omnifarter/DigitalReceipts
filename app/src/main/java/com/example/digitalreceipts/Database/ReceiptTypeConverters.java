package com.example.digitalreceipts.Database;

import androidx.room.TypeConverter;

import com.example.digitalreceipts.ReceiptItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ReceiptTypeConverters {


    // handles conversion to List Format from JSON format
    @TypeConverter
    public static List<ReceiptItem> stringToItemlist (String data)
    {
        if (data == null)
        {
            return Collections.emptyList();
        }
        Gson jsonItemList = new Gson();
        Type listType = new TypeToken<List<ReceiptItem>>() {}.getType();
        return jsonItemList.fromJson(data, listType);
    }
    // handles conversion to JSON format from List Format
    @TypeConverter
    public static String itemlistToString(List<ReceiptItem> item)
    {
        Gson jsonItemList = new Gson();
        return jsonItemList.toJson(item);
    }
}
