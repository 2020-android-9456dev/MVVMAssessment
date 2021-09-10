
package com.example.myassignment.presentables

import android.content.res.AssetFileDescriptor
import com.example.myassignment.BuildConfig
import com.example.myassignment.extenstions.copyInputStreamToFile
import com.example.myassignment.utils.CommonParser
import com.example.myassignment.utils.CustomResult
import com.example.myassignment.utils.CustomResultStatus
import java.io.*


interface LocalFilePresentable {
    /**
     * getLocalModel: Get a local JSON model and parse
     *
     * @param mapper
     * @param assetFileDescriptor
     * @param file
     * @param type
     * @return CustomResult<T>
     */
    fun <T> getLocalModel(mapper: CommonParser, assetFileDescriptor: AssetFileDescriptor? = null, file: File? = null, type: Class<T>): CustomResult<T> {
        try {
            val json = getLocalAssetFile(assetFileDescriptor, file)

            if (json.status == CustomResultStatus.ERROR) {
                return CustomResult.error(json.error?.exception, null)
            }

            val result = mapper.parseString(json.data ?: "", type)
            return CustomResult.success(result)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.stackTrace
            return CustomResult.error(e, null)
        }
    }

    /**
     * getLocalAssetFile: Get a local asset from the internal app package and read into a CustomResult
     *
     * @param assetFileDescriptor
     * @param file
     * @return CustomResult<String>
     */
    private fun getLocalAssetFile(assetFileDescriptor: AssetFileDescriptor? = null, file: File? = null): CustomResult<String> {
        return try {
            // Load File
            var inputStream: InputStream? = null

            // Check file first and then the descriptor
            file?.let { checkedFile ->
                if (checkedFile.exists()) inputStream = checkedFile.inputStream()
            } ?: run {
                assetFileDescriptor?.let {
                    inputStream = it.createInputStream()
                }
            }

            // Check stream valid
            inputStream?.let {
                val results = StringBuilder()
                BufferedReader(InputStreamReader(inputStream)).forEachLine { results.append(it) }
                return CustomResult.success(results.toString().trimIndent())
            }

            CustomResult.error(IOException(), null)
        } catch (e: IOException) {
            e.printStackTrace()
            CustomResult.error(e, null)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            CustomResult.error(e, null)
        }
    }

    /**
     * getLocalAsset: Open a local asset from a fileDescriptor, copy to local(internal) storage and return the full path.
     *
     * @param localPath
     * @param fileName
     * @return String
     */
    fun getLocalAsset(localPath: String, fileName: String, assetFileDescriptor: AssetFileDescriptor): String? {
        try {
            // opens input stream from the HTTP connection
            val inputStream = assetFileDescriptor.createInputStream()

            //Create dirs
            val dirCheck = File(localPath)
            if (!dirCheck.exists()) {
                dirCheck.mkdirs()
            }

            //Delete file if already exists
            val fileCheck = File("$localPath/$fileName")
            if (fileCheck.exists()) {
                fileCheck.delete()
            }

            //Create file
            fileCheck.createNewFile()

            //Copy input stream to file
            fileCheck.copyInputStreamToFile(inputStream)

            inputStream.close()
            return fileCheck.absolutePath
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.stackTrace
            return null
        }
    }
}