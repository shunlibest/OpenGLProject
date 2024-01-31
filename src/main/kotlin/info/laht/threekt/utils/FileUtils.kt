package info.laht.threekt.utils

import java.io.*

object FileUtils {
    fun readResourceFile(name: String?): String {
        val resource = ClassLoader.getSystemResource(name)
        val file = File(resource.path)
        return convertFileToString(file.absolutePath)
    }

    fun convertFileToString(filePath: String?): String {
        return try {
            val fileInputStream = FileInputStream(filePath)
            val bufferedReader = BufferedReader(InputStreamReader(fileInputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
                stringBuilder.append("\n") // 可根据需要自定义换行符
            }
            bufferedReader.close()
            fileInputStream.close()
            stringBuilder.toString()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}