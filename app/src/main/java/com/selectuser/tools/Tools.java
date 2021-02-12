package com.selectuser.tools;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.databinding.BindingConversion;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

public class Tools {

    private static final String TAG = "Tools";

    public enum ArrayToStringMode{Dec, UnsignedDec, Hex, Char}

    @SuppressLint("DefaultLocale")
    static public String byteArrayToString(byte[] array, ArrayToStringMode mode){
        StringBuilder out = new StringBuilder();
        switch (mode){
            case Hex:
                for (byte d : array)
                    out.append(String.format("x%X ", d));
                break;
            case Dec:
                for (byte d : array)
                    out.append(String.format("%d ", d));
                break;
            case UnsignedDec:
                for (byte d : array)
                    out.append(String.format("%d ", 0xff&d));
                break;
            case Char:
                return  new String(array, StandardCharsets.UTF_8);
        }

        return out.toString();
    }



    public static long getUnsignedInt(int x) {
        return x & 0x00000000ffffffffL;
    }

    @BindingConversion
    public static String convertDate6ToString(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }


    public static Date getDate6FromArray(byte[] array, int pos){
        int end = pos + 6;
        if (array.length < end) {
            Log.e(TAG, "Array out of bounds");
            return new Date();
        }

        GregorianCalendar calendar = new GregorianCalendar();

        int year = 0;
        int month = 0;
        int day = 0;
        String string = new String(array, StandardCharsets.UTF_8);
        try {
            year = Integer.parseInt(string.substring(pos, pos + 2)) + 2000;
            pos+=2;
            month = Integer.parseInt(string.substring(pos, pos + 2)) - 1;
            pos+=2;
            day = Integer.parseInt(string.substring(pos, pos + 2));
        }
        catch (NumberFormatException e)
        {
            Log.e(TAG, "Date conversion error");
            return null;
        }

        calendar.set(year, month, day);
        return calendar.getTime();
    }

    public static String convertArray8bitCharToString(byte[] array, int pos, int length){
        int end = pos + length;
        if (array.length < end) {
            Log.e(TAG, "Array out of bounds");
            return "";
        }

        StringBuilder out = new StringBuilder();

        for (int i = pos; i < end;  i++){
            byte value = array[i];
            if (convertTableChar16To8bit.containsValue(value)){
                out.append(convertTableChar16To8bit.getKey(value));
            } else {
                out.append((char)value);
            }
        }

        return out.toString();
    }


    public static byte[] convertStringToArray8bitChar(String string){


        char[] chars = string.toCharArray();
        byte[] array = new byte[chars.length];
        for (int i = 0; i < chars.length; i++){
            if (convertTableChar16To8bit.containsKey(chars[i])){
                array[i] = convertTableChar16To8bit.get(chars[i]).byteValue();
            } else {
                array[i] = (byte) chars[i];
            }
        }

        return array;
    }

    private static class BiMap<K, V> {

        TreeMap<K, V> map = new TreeMap<K, V>();
        TreeMap<V, K> invertedMap = new TreeMap<V, K>();

        public void put(K k, V v) {
            map.put(k, v);
            invertedMap.put(v, k);
        }

        public V get(K k) {
            return map.get(k);
        }

        public K getKey(V v) {
            return invertedMap.get(v);
        }

        public boolean containsKey(K k){
            return map.containsKey(k);
        }

        public boolean containsValue(V v){
            return invertedMap.containsKey(v);
        }
    }

    private static BiMap<Character, Byte> convertTableChar16To8bit = new BiMap<Character, Byte>(){
        {
            int i = 192;
            put('А', (byte) i++);
            put('Б', (byte) i++);
            put('В', (byte) i++);
            put('Г', (byte) i++);
            put('Д', (byte) i++);
            put('Е', (byte) i++);
            put('Ж', (byte) i++);
            put('З', (byte) i++);
            put('И', (byte) i++);
            put('Й', (byte) i++);
            put('К', (byte) i++);
            put('Л', (byte) i++);
            put('М', (byte) i++);
            put('Н', (byte) i++);
            put('О', (byte) i++);
            put('П', (byte) i++);
            put('Р', (byte) i++);
            put('С', (byte) i++);
            put('Т', (byte) i++);
            put('У', (byte) i++);
            put('Ф', (byte) i++);
            put('Х', (byte) i++);
            put('Ц', (byte) i++);
            put('Ч', (byte) i++);
            put('Ш', (byte) i++);
            put('Щ', (byte) i++);
            put('Ъ', (byte) i++);
            put('Ы', (byte) i++);
            put('Ь', (byte) i++);
            put('Э', (byte) i++);
            put('Ю', (byte) i++);
            put('Я', (byte) i++);

            put('а', (byte) i++);
            put('б', (byte) i++);
            put('в', (byte) i++);
            put('г', (byte) i++);
            put('д', (byte) i++);
            put('е', (byte) i++);
            put('ж', (byte) i++);
            put('з', (byte) i++);
            put('и', (byte) i++);
            put('й', (byte) i++);
            put('к', (byte) i++);
            put('л', (byte) i++);
            put('м', (byte) i++);
            put('н', (byte) i++);
            put('о', (byte) i++);
            put('п', (byte) i++);
            put('р', (byte) i++);
            put('с', (byte) i++);
            put('т', (byte) i++);
            put('у', (byte) i++);
            put('ф', (byte) i++);
            put('х', (byte) i++);
            put('ц', (byte) i++);
            put('ч', (byte) i++);
            put('ш', (byte) i++);
            put('щ', (byte) i++);
            put('ъ', (byte) i++);
            put('ы', (byte) i++);
            put('ь', (byte) i++);
            put('э', (byte) i++);
            put('ю', (byte) i++);
            put('я', (byte) i++);
        }
    };


}