package com.test.medpersonal.presentation.fragments.registrationFragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.test.medpersonal.R
import com.test.medpersonal.databinding.FragmentRegistrationBinding
import com.test.medpersonal.domain.models.Users
import com.test.medpersonal.presentation.App


class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private val binding: FragmentRegistrationBinding by viewBinding()
    private lateinit var googleClient: GoogleSignInClient
    private var list: ArrayList<String> = arrayListOf()
    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGoogle()
        registration()


        binding.btnGmail.setOnClickListener {
            googleSignUp()
        }
    }


    // инициализация Google
    private fun initGoogle() {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client))
            .requestEmail()
            .build()
        googleClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    // вход через гугл
    private fun googleSignUp() {
        val intent = googleClient.signInIntent
        resultLauncher.launch(intent)
    }

    // обработка токена
    private fun authWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        App.fbAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = App.fbAuth.currentUser
                } else {
                    Toast.makeText(
                        requireContext(),
                        "AuthError ", Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    //проверка на результат и навигирование в groupFragment если RESULT_OK
    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

        if (it.resultCode == Activity.RESULT_OK) {
            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            findNavController().navigate(R.id.groupFragment)
            try {
                val account = task.getResult(ApiException::class.java)!!
                authWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }

    // слушатель на изменение edittext(активность кнопки регистрации)
    private fun registration() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnRegistration.isEnabled = s?.length in 6..60
                binding.btnRegistration.setOnClickListener {
                    signUpWithEmailAndPassword()

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    // регистрация почты и пароля и вывод окна повторной отправки
    private fun signUpWithEmailAndPassword() {
        App.fbAuth.createUserWithEmailAndPassword(
            binding.etEmail.text.toString(), binding.etPassword.text.toString()
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val builder = AlertDialog.Builder(requireContext())
                    val dialogView = layoutInflater.inflate(R.layout.item_waiting_dialog, null)
                    builder.setView(dialogView)
                    sendEmailVerification(task.result?.user!!)
                    val string = task.result?.user!!
                    verifyCheck()
                    dialogView.visibility = View.VISIBLE

                    countDownTimer(
                        dialogView.findViewById(R.id.tvCountTime),
                        dialogView.findViewById(R.id.btnSendAgain)
                    )
                    dialogView.findViewById<Button>(R.id.btnSendAgain).setOnClickListener() {
                        sendEmailVerification(string)
                    }
                    dialogView.findViewById<Button>(R.id.btnNext).setOnClickListener() {
                        database = Firebase.database.reference

                        val auth = FirebaseAuth.getInstance()
                        val usersModel = Users(
                            null,
                            null,
                            binding.etRegisUser.text.toString(),
                            null,
                            binding.etSpecific.text.toString(),
                            binding.etCompany.text.toString(),
                            auth.currentUser?.uid.toString(),
                        )
                        val db = FirebaseFirestore.getInstance()

                        val userId = auth.currentUser?.uid.toString()

                        val childUpdates = hashMapOf<String, Any>(
                            "/users/$userId/" to usersModel
                        )
                        database.updateChildren(childUpdates)

                        findNavController().navigate(R.id.authFragment)
                        dialogView.visibility = View.GONE

                    }
                    builder.create().show()

                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.cannot_registrated_rus),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    // отправка верификации на почту
    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    context,
                    getString(R.string.send_email_verification_rus),
                    Toast.LENGTH_SHORT
                ).show()
                user.reload()
            } else Toast.makeText(
                context,
                getString(R.string.cannot_send_email_verification_rus),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // таймер повторной отправки
    private fun countDownTimer(count: TextView, button: Button) {
        object : CountDownTimer(30000, 1000) {
            override fun onTick(p0: Long) {
                count.text = "" + p0 / 1000
            }

            override fun onFinish() {
                button.visibility = View.VISIBLE
            }
        }.start()
    }


    // Check if user is signed in (non-null) and update UI accordingly.
    override fun onStart() {
        super.onStart()
        val currentUser = App.fbAuth.currentUser
    }

    private fun verifyCheck() {
        App.authListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser!!.isEmailVerified) {
                //
                close()
            }
        }
    }


    private fun close() {
        findNavController().navigate(R.id.authFragment)
    }
}