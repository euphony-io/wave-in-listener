package com.android.check_in_listener.visitorList

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.check_in_listener.databinding.ActivityVisitorListBinding
import com.android.check_in_listener.listenDb.ListenDatabase

class VisitorListActivity : AppCompatActivity() {
    private var _binding: ActivityVisitorListBinding? = null
    private val binding get() = _binding!!
    private lateinit var model: VisitorListViewModel

    private var listenDatabase: ListenDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityVisitorListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model = ViewModelProvider(this).get(VisitorListViewModel::class.java)

        listenDatabase = ListenDatabase.getInstance(this)


        binding.btnAllList.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                showDialogToGetFilePermission()
            } else {
                model.exportDataToCSV();
            }
        }

    }

    private fun showDialogToGetFilePermission() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Permission request")
            .setMessage("you need to allow storage permission to save files.")

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

    override fun onDestroy() {
        _binding = null

        ListenDatabase.destroyInstance()
        listenDatabase = null

        super.onDestroy()
    }
}