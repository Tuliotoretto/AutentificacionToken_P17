package dgtic.unam.autentificaciontoken.model

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserClient {
    @POST("signin")
    fun login(@Body login:Login): Call<Usuario>
    @GET("admin")
    fun data(@Header("Authorization") token: String):Call<ResponseBody>
}