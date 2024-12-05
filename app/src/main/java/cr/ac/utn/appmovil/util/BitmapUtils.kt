package cr.ac.utn.appmovil.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

object BitmapUtils {

    /**
     * Convierte un Bitmap a un arreglo de bytes (ByteArray).
     * @param bitmap El Bitmap a convertir.
     * @return Un arreglo de bytes que representa la imagen.
     */
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    /**
     * Convierte un arreglo de bytes (ByteArray) a un Bitmap.
     * @param byteArray El arreglo de bytes a convertir.
     * @return Un Bitmap generado a partir del arreglo de bytes.
     */
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
