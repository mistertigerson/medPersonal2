package com.test.medpersonal.presentation.fragments.authFragment

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.test.medpersonal.R
import com.test.medpersonal.databinding.FragmentAuthBinding

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding: FragmentAuthBinding by viewBinding()
    private lateinit var googleClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    var storage: FirebaseStorage? = null
    var database: FirebaseDatabase? = null
    var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
//        dialog!!.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGmail()
        initEditText()

        binding.tvRegistrationSignIn.setOnClickListener {
            findNavController().navigate(R.id.registrationFragment)
        }
        binding.tvRecover.setOnClickListener {
            resetPassword()
        }

        binding.btnGmailInSign.setOnClickListener {
            gmailSignIn()
        }
    }

    private fun resetPassword() {
        binding.tvRecover.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Forgot password")
            val dialogView = layoutInflater.inflate(R.layout.fragment_dialog, null)
            builder.setView(dialogView)
            dialogView.findViewById<Button>(R.id.btnSendLink).setOnClickListener {
                forgetPassword(dialogView.findViewById<EditText>(R.id.etPasswordEmail))
                countTimer(
                    dialogView.findViewById<TextView>(R.id.tvCountTimer),
                    dialogView.findViewById<TextView>(R.id.tvResent)
                )
            }
            dialogView.findViewById<TextView>(R.id.tvResent).setOnClickListener {
                forgetPassword(dialogView.findViewById(R.id.etPasswordEmail))

            }
            //builder.setNegativeButton("close", DialogInterface.OnClickListener { _, _ -> })


            builder.create().show()
        }
    }

    private fun forgetPassword(username: EditText) {
        if (username.text.toString().isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Fields cannot be empty ", Toast.LENGTH_SHORT
            ).show()
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            return
        } else {
            auth.sendPasswordResetEmail(username.text.toString().trim())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Password reset link sent ",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            task.exception!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
        }
    }

    private fun countTimer(tv: TextView, tv2: TextView) {
        object : CountDownTimer(30000, 1000) {
            override fun onTick(p0: Long) {
                tv.text = "" + p0 / 1000
            }

            override fun onFinish() {
                tv2.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun initEditText() {
        binding.btnSignIn.setOnClickListener {
            if (binding.etMailSignIn.text.toString().isEmpty() || binding.etPasswordSignIn.text
                    .toString().isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Fields cannot be empty ", Toast.LENGTH_SHORT
                ).show()
            } else {
                auth.signInWithEmailAndPassword(
                    binding.etMailSignIn.text.toString(), binding
                        .etPasswordSignIn.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        findNavController().navigate(R.id.groupFragment)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "You have some errors ", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun initGmail() {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client))
            .requestEmail()
            .build()
        googleClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun gmailSignIn() {
        val intent = googleClient.signInIntent
        resultLauncher.launch(intent)
    }

    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(it.data)
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    signWithGmail(account)
                } catch (e: ApiException) {
                    e.printStackTrace()

                }
            }
        })

    private fun signWithGmail(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(OnCompleteListener<AuthResult>() { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "Google signIn done")
                    close()
                } else {
                    Log.e(
                        ContentValues.TAG,
                        "Google signIn error" + task.exception?.message.toString()
                    )

                    Toast.makeText(
                        requireContext(),
                        "Error ", Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun close() {
        val navController = findNavController()
        navController.navigateUp()

    }


}