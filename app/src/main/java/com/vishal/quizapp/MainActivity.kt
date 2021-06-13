package com.vishal.quizapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val outlinedView: com.google.android.material.textfield.TextInputLayout = findViewById(R.id.outlinedTextField)
        val discordId:com.google.android.material.textfield.TextInputEditText = findViewById(R.id.discord_id_area)
        val submitButton: Button = findViewById(R.id.submit_button)
        val profileImage: ImageView = findViewById(R.id.profile_image)
        val loading: ProgressBar = findViewById(R.id.progressBar2)
        val saveButton : Button = findViewById(R.id.save_button)
        submitButton.setOnClickListener {
            if (discordId.text.toString() == "") {
                outlinedView.error = "Enter a valid ID:"
            }else{
                outlinedView.error = ""
                loading.visibility = ProgressBar.VISIBLE
                Toast.makeText(this, "Please wait..", Toast.LENGTH_SHORT).show()
                val queue = Volley.newRequestQueue(this)
                val apiUrl = "https://discord.com/api/v8/users/${discordId.text.toString()}"
                val jsonObject: JsonObjectRequest = object : JsonObjectRequest(
                    Method.GET, apiUrl, null,
                    Response.Listener {response ->
                        val url = "https://cdn.discordapp.com/avatars/${response["id"]}/${response["avatar"]}.png?size=1024"
                        Picasso.get().load(url).into(profileImage)
                        profileImage.visibility = ImageView.VISIBLE
                        saveButton.visibility = Button.VISIBLE
                        loading.visibility = ProgressBar.GONE},

                    Response.ErrorListener {error ->
                        if (error.toString() == "com.android.volley.ClientError"){
                            Toast.makeText(this, "Please enter a Valid discord user ID", Toast.LENGTH_LONG).show()
                            loading.visibility = ProgressBar.GONE
                            saveButton.visibility = Button.GONE
                            profileImage.visibility = ImageView.GONE
                        }
                    }
                ) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["Authorization"] = "Bot NzQ3NjUyNzk0MTc3MjkwMzEw.X0R_7A.Oe0XAYxplIeXvNYT0gEfwIhvzDo"
                        return params
                    }
                }
                queue.add(jsonObject)
            }
            discordId.setText("")
        }
    }

}
