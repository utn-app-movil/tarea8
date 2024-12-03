package cr.ac.utn.appmovil.network

import cr.ac.utn.appmovil.model.ApiContact
import cr.ac.utn.appmovil.model.ApiContactResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiContactService {

    // Obtener todos los contactos
    @GET("people/")
    fun getAllContacts(): Call<ApiContactResponse>

    // Obtener un contacto específico por ID
    @GET("people/{id}")
    fun getContactById(@Path("id") id: Int): Call<ApiContact>

    // Crear un nuevo contacto
    @POST("people/")
    fun createContact(@Body contact: ApiContact): Call<Void>

    // Actualizar un contacto
    @PUT("people/{id}")
    fun updateContact(@Path("id") id: Int, @Body contact: ApiContact): Call<Void>

    // Eliminar un contacto
    @DELETE("people/{id}")
    fun deleteContact(@Path("id") id: Int): Call<Void>
}

