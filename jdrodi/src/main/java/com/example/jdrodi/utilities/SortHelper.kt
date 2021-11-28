package com.example.jdrodi.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import java.util.*

object SortHelper

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
fun compareDate(file1: File, file2: File): Int {
    val lastModified1 = file1.lastModified()
    val lastModified2 = file2.lastModified()
    return lastModified2.compareTo(lastModified1)
}

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
fun sortVideos(list: ArrayList<File>): ArrayList<String> {
    val sortedList = ArrayList<String>()
    list.sortWith(Comparator { object1, object2 -> compareDate(object1, object2) })
    for (i in list.indices) {
        val path = list[i].path
        when (getFileType(File(path))) {
            FileType.VIDEO -> sortedList.add(path)
        }
    }
    return sortedList
}

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
fun sortPhotos(list: ArrayList<File>): ArrayList<String> {
    val sortedList = ArrayList<String>()
    list.sortWith { object1, object2 -> compareDate(object1, object2) }
    for (i in list.indices) {
        val path = list[i].path
        when (getFileType(File(path))) {
            FileType.IMAGE -> sortedList.add(path)
        }
    }
    return sortedList
}

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
fun sortPhotos2(list: ArrayList<String>): ArrayList<String> {
    val sortedList = ArrayList<String>()
    list.sortWith { object1, object2 -> compareDate(File(object1), File(object2)) }
    for (i in list.indices) {
        val path = list[i]
        when (getFileType(File(path))) {
            FileType.IMAGE -> sortedList.add(path)
        }
    }
    return sortedList
}

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
fun sortVideos2(list: ArrayList<String>): ArrayList<String> {
    val sortedList = ArrayList<String>()
    list.sortWith { object1, object2 -> compareDate(File(object1), File(object2)) }
    for (i in list.indices) {
        val path = list[i]
        when (getFileType(File(path))) {
            FileType.VIDEO -> sortedList.add(path)
        }
    }
    return sortedList
}

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
fun loadLatest(list: ArrayList<File>): ArrayList<String> {
    val sortedList = ArrayList<String>()
    list.sortWith { object1, object2 -> compareDate(object1, object2) }
    var length = 6
    if (list.size < 6) {
        length = list.size
    }
    for (i in 0 until length) {
        val path = list[i].path
        when (getFileType(File(path))) {
            FileType.IMAGE, FileType.VIDEO -> sortedList.add(path)
        }
    }
    return sortedList
}

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
fun sort(list: ArrayList<File>): ArrayList<String> {
    val sortedList = ArrayList<String>()
    list.sortWith { object1, object2 -> compareDate(object1, object2) }
    for (i in list.indices) {
        sortedList.add(list[i].path)
    }
    return sortedList
}