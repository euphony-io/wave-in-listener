package com.android.check_in_listener.visitorList

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.check_in_listener.databinding.ActivityVisitorListBinding
import com.android.check_in_listener.listenDb.ListenDatabase
import com.android.check_in_listener.showCustomToast
import com.android.check_in_listener.visitorList.adapter.VisitorListRvAdapter

class VisitorListActivity : AppCompatActivity() {
    private var _binding: ActivityVisitorListBinding? = null
    private val binding get() = _binding!!
    private lateinit var model: VisitorListViewModel

    private var isExportingSuccess = false
    private var listenDatabase: ListenDatabase? = null

    private lateinit var visitorListRvAdaptaer: VisitorListRvAdapter

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
                isExportingSuccess = model.exportDataToCSV()
            }

            if (isExportingSuccess) {
                Toast(this).showCustomToast(
                    "방문자 기록 파일을 생성하였습니다\n내 파일/Documents/VisitorsList 폴더를 확인해주세요!",
                    this, Toast.LENGTH_LONG)
            }
            else {
                Toast(this).showCustomToast("방문자 기록 파일 생성에 실패하였습니다.",
                    this, Toast.LENGTH_SHORT)
            }
        }

        binding.btnBack.setOnClickListener { onBackPressed() }

        initRecyclerView()

    }

    private fun initRecyclerView(){
        visitorListRvAdaptaer = VisitorListRvAdapter()
        binding.rvVisitor.apply {
            adapter = visitorListRvAdaptaer
            layoutManager = LinearLayoutManager(this@VisitorListActivity, RecyclerView.VERTICAL, false)
        }
        viewAllVisitorList()
    }

    private fun viewAllVisitorList(){
        model.getAllVisitorList()?.observe(this, Observer {
            visitorListRvAdaptaer.submitList(it)
        })
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