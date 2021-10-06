package com.android.check_in_listener

import android.Manifest
import android.animation.Animator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.check_in_listener.databinding.ActivityMainBinding
import com.android.check_in_listener.listenDb.ListenDatabase
import com.android.check_in_listener.visitorList.VisitorListActivity
import com.github.ybq.android.spinkit.style.FadingCircle

class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_CODE = 17389
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var isListening = false
    private lateinit var model: MainViewModel

    private var listenDatabase: ListenDatabase? = null

    lateinit var loadingCircle: FadingCircle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model = ViewModelProvider(this).get(MainViewModel::class.java)

        listenDatabase = ListenDatabase.getInstance(this)

        requestPermissions()

        setLoadingCircle()

        binding.btnListen.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED
            ) {
                showDialogToGetMicroPhonePermission()
            } else {
                if (!isListening) startListen()
                else endListen()
                isListening = model.listener(isListening)
                Log.d("listen", "MainActivity - onCreate() :  islisten: $isListening")
            }
        }

        binding.btnVisitorList.setOnClickListener {
            startActivity(Intent(this, VisitorListActivity::class.java))
        }

        model.isSuccess.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                isListening = model.listener(isListening)
                endListen()
                successCheckAnim()
            }
        })
    }

    private fun successCheckAnim(){
        binding.icCheck.visibility = View.VISIBLE
        binding.icCheck.playAnimation()
        binding.icCheck.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {
            }
            override fun onAnimationEnd(animation: Animator?) {
                binding.icCheck.visibility = View.GONE
            }
            override fun onAnimationCancel(animation: Animator?) {
            }
            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
    }

    private fun setLoadingCircle(){
        loadingCircle = FadingCircle()
        loadingCircle.color = Color.parseColor("#57AEFF")
    }

    private fun startListen(){
        binding.loadingMain.setIndeterminateDrawable(loadingCircle)
        loadingCircle.setVisible(true, true)
        loadingCircle.start()
        binding.loadingMain.visibility = View.VISIBLE
        binding.btnListen.text = getString(R.string.listen_end)
    }

    private fun endListen(){
        binding.btnListen.text = getString(R.string.listen_start)
        loadingCircle.stop()
        binding.loadingMain.visibility = View.GONE
        loadingCircle.setVisible(false, false)
    }

    private fun requestPermissions(): Boolean {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        val permissions: Array<String> = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        ActivityCompat.requestPermissions(this, permissions, 0)
        return false

    }

    private fun showDialogToGetMicroPhonePermission() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Permission request")
            .setMessage("you need to allow microphone permission to receive data.")

        builder.setPositiveButton("OK") { dialogInterface, i ->
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        builder.setNegativeButton("Later") { dialogInterface, i ->

        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onPause() {
        super.onPause()
        Log.d("listen", "MainActivity - onCreate() :  islisten: $isListening")
        if (isListening) {
            isListening = model.listener(isListening)
            endListen()
        }
        binding.icCheck.visibility = View.GONE
    }

    override fun onDestroy() {
        _binding = null

        ListenDatabase.destroyInstance()
        listenDatabase = null

        super.onDestroy()
    }
}
