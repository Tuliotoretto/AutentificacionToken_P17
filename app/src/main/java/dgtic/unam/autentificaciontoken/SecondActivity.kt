package dgtic.unam.autentificaciontoken

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import dgtic.unam.autentificaciontoken.databinding.ActivitySecondBinding
import dgtic.unam.autentificaciontoken.model.UserClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private lateinit var userClient: UserClient
    private lateinit var token: String
    var builder = Retrofit.Builder()
        .baseUrl("http://192.168.0.4:8080/")
        .addConverterFactory(GsonConverterFactory.create())
    var retrofit = builder.build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        //datos que manda la actividad
        var bundle: Bundle? = intent.extras
        token= bundle?.getString("token")?:""
        userClient = retrofit.create(UserClient::class.java)
        binding.token.setOnClickListener {
            tokenSecret()
        }
        binding.closeSesion.setOnClickListener {
            cerrarSesion()
        }
    }
    private fun cerrarSesion() {
        token = ""
        Toast.makeText(binding.token.context, "Cerrando la sesión", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun tokenSecret() {
        var call = userClient.data(token)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        var res = response.body()
                        Toast.makeText(binding.token.context, res?.string(), Toast.LENGTH_SHORT)
                            .show()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(
                        binding.token.context,
                        "Token no es correcto",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(binding.token.context, "error :(", Toast.LENGTH_SHORT).show()
            }
        })
    }
}