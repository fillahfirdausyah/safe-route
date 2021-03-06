package com.c22ps305team.saferoute.ui.auth.register

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.c22ps305team.saferoute.databinding.ActivityRegisterBinding
import com.c22ps305team.saferoute.utils.Result
import com.c22ps305team.saferoute.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding


    private val regViewModel: RegisterViewModel by viewModels(){
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupListener()
        setupObserver()
    }

    private fun setupListener() {

        binding.buttonSubmit.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            when{
                username.isEmpty() -> {
                    setUsernameError("Username must be filled!")
                }
                password.length < 3 -> {
                    setPasswordError("Password must be filled!")
                }
                else -> {
                    //val register = AuthRequest(username, password)
                    regViewModel.register(username, password)
                }
            }
        }

    }

    private fun setupObserver() {
        regViewModel.registerResponse.observe(this){ registerResponse ->
            when(registerResponse) {
                is Result.Loading -> {
                    onLoading(true)
                }
                is Result.Success -> registerResponse.data.let {
                    onLoading(false)
                    onSuccess()
                }

                is Result.Error -> registerResponse.data.let {
                    onLoading(false)
                    onFailed()
                }
            }
        }
    }


    private fun onLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun onSuccess() {
        Snackbar.make(binding.root, "Register Success!", Snackbar.LENGTH_LONG).show()
        //Toast.makeText(this, "Register Success!", Toast.LENGTH_SHORT).show()
    }

    private fun onFailed() {
        Snackbar.make(binding.root, "Register Failed!", Snackbar.LENGTH_LONG).show()
        //Toast.makeText(this, "Register Failed!", Toast.LENGTH_SHORT).show()
    }


    private fun setUsernameError(e : String?){
        binding.edtUsername.error = e
    }

    private fun setPasswordError(e: String?){
        binding.edtPassword.error = e
    }

}