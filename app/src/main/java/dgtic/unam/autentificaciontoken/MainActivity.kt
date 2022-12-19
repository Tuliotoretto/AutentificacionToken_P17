package dgtic.unam.autentificaciontoken

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import dgtic.unam.autentificaciontoken.databinding.ActivityMainBinding
import dgtic.unam.autentificaciontoken.model.Login
import dgtic.unam.autentificaciontoken.model.UserClient
import dgtic.unam.autentificaciontoken.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userClient: UserClient
    var builder = Retrofit.Builder()
        .baseUrl("http://192.168.0.4:8080/")
        .addConverterFactory(GsonConverterFactory.create())
    var retrofit = builder.build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userClient = retrofit.create(UserClient::class.java)
        supportActionBar?.hide()
        binding.login.setOnClickListener {
            login()
        }
    }
    private fun login() {
        var usuario=binding.username.text.toString()
        var password=binding.password.text.toString()
        if(!usuario.isEmpty()&&!usuario.isEmpty()){
            var login = Login(usuario, password)
            var call = userClient.login(login)
            call.enqueue(object: Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if(response.isSuccessful){
                        var token=response.body()?.token.toString()
                        Toast.makeText(binding.login.context,response.body()?.token.toString(),Toast.LENGTH_SHORT).show()
                        var paso: Intent = Intent(binding.login.context, SecondActivity::class.java).apply {
                            putExtra("token", token)
                        }
                        startActivity(paso)
                    }else{
                        Toast.makeText(binding.login.context,"Credenciales no validas",Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    Toast.makeText(binding.login.context,"error :(",Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            Toast.makeText(binding.login.context,"Introduzca las credenciales",Toast.LENGTH_SHORT).show()
        }
    }
}