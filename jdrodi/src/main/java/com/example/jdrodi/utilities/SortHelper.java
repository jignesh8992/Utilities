package com.example.jdrodi.utilities;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SortHelper {


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static int compareDate(File file1, File file2) {
        long lastModified1 = file1.lastModified();
        long lastModified2 = file2.lastModified();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Long.compare(lastModified2, lastModified1);
        } else {
            return Long.compare(lastModified2, lastModified1);

        }
    }


    public static ArrayList<String> sortVideos(ArrayList<File> list) {

        ArrayList<String> sortedList = new ArrayList<>();
        Collections.sort(list, new Comparator<File>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public int compare(File object1, File object2) {
                return SortHelper.compareDate(object1, object2);
            }
        });

        for (int i = 0; i < list.size(); i++) {
            String path = list.get(i).getPath();
            switch (FileFormatHelper.FileType.getFileType(new File(path))) {
                case VIDEO:
                    sortedList.add(path);
                    break;
            }
        }
        return sortedList;
    }

    public static ArrayList<String> sortPhotos(ArrayList<File> list) {
        ArrayList<String> sortedList = new ArrayList<>();
        Collections.sort(list, new Comparator<File>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public int compare(File object1, File object2) {
                return SortHelper.compareDate(object1, object2);
            }
        });
        for (int i = 0; i < list.size(); i++) {
            String path = list.get(i).getPath();
            switch (FileFormatHelper.FileType.getFileType(new File(path))) {
                case IMAGE:
                    sortedList.add(path);
                    break;
            }
        }
        return sortedList;
    }

    public static ArrayList<String> sortPhotos2(ArrayList<String> list) {
        ArrayList<String> sortedList = new ArrayList<>();
        /*Collections.sort(list, new Comparator<File>() {
            @Override
            public int compare(File object1, File object2) {
                return SortHelper.compareDate(object1, object2);
            }
        });*/
        for (int i = 0; i < list.size(); i++) {
            String path = list.get(i);
            switch (FileFormatHelper.FileType.getFileType(new File(path))) {
                case IMAGE:
                    sortedList.add(path);
                    break;
            }
        }
        return sortedList;
    }

    public static ArrayList<String> sortVideos2(ArrayList<String> list) {
        ArrayList<String> sortedList = new ArrayList<>();
        /*Collections.sort(list, new Comparator<File>() {
            @Override
            public int compare(File object1, File object2) {
                return SortHelper.compareDate(object1, object2);
            }
        });*/
        for (int i = 0; i < list.size(); i++) {
            String path = list.get(i);
            switch (FileFormatHelper.FileType.getFileType(new File(path))) {
                case VIDEO:
                    sortedList.add(path);
                    break;
            }
        }
        return sortedList;
    }


    public static ArrayList<String> loadLatest(ArrayList<File> list) {

        ArrayList<String> sortedList = new ArrayList<>();
        Collections.sort(list, new Comparator<File>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public int compare(File object1, File object2) {
                return SortHelper.compareDate(object1, object2);
            }
        });

        int length = 6;
        if (list.size() < 6) {
            length = list.size();
        }

        for (int i = 0; i < length; i++) {
            String path = list.get(i).getPath();
            switch (FileFormatHelper.FileType.getFileType(new File(path))) {
                case IMAGE:
                    sortedList.add(path);
                    break;
                case VIDEO:
                    sortedList.add(path);
                    break;
            }
        }
        return sortedList;
    }

    public static ArrayList<String> sort(ArrayList<File> list) {
        ArrayList<String> sortedList = new ArrayList<>();
        Collections.sort(list, new Comparator<File>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public int compare(File object1, File object2) {
                return SortHelper.compareDate(object1, object2);
            }
        });
        for (int i = 0; i < list.size(); i++) {
            sortedList.add(list.get(i).getPath());
        }
        return sortedList;
    }
}