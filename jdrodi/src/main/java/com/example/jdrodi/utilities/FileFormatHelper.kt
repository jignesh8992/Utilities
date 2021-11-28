package com.example.jdrodi.utilities

import android.webkit.MimeTypeMap
import java.io.File

object FileFormatHelper

fun getFileExtension(filename: String): String {
    return if (filename.contains(".")) filename.substring(filename.lastIndexOf(".") + 1) else ""
}

fun getMimeType(file: File): String? {
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(getFileExtension(file.name))
}

enum class FileType { DIRECTORY, MISC_FILE, AUDIO, IMAGE, VIDEO, DOC, PPT, XLS, PDF, TXT, ZIP }

fun getFileType(file: File): FileType {
    if (file.isDirectory) return FileType.DIRECTORY
    val mime = getMimeType(file) ?: return FileType.MISC_FILE
    if (mime.startsWith("audio")) return FileType.AUDIO
    if (mime.startsWith("image")) return FileType.IMAGE
    if (mime.startsWith("video")) return FileType.VIDEO
    if (mime.startsWith("application/ogg")) return FileType.AUDIO
    if (mime.startsWith("application/msword")) return FileType.DOC
    if (mime.startsWith("application/vnd.ms-word")) return FileType.DOC
    if (mime.startsWith("application/vnd.ms-powerpoint")) return FileType.PPT
    if (mime.startsWith("application/vnd.ms-excel")) return FileType.XLS
    if (mime.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml")) return FileType.DOC
    if (mime.startsWith("application/vnd.openxmlformats-officedocument.presentationml")) return FileType.PPT
    if (mime.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml")) return FileType.XLS
    if (mime.startsWith("application/pdf")) return FileType.PDF
    if (mime.startsWith("text")) return FileType.TXT
    return if (mime.startsWith("application/zip")) FileType.ZIP else FileType.MISC_FILE
}