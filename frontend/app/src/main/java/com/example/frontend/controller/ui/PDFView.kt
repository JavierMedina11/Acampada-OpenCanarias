package com.example.frontend.controller.ui

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.R
import com.example.frontend.controller.io.ServiceImpl
import kotlinx.android.synthetic.main.activity_p_d_f_view.*


class PDFView : AppCompatActivity() {

    private val STORAGE_PERMISSION_CODE: Int = 1000
    private val PDF_SELECTION_CODE = 99
    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p_d_f_view)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                // Permission Denied
                requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
            }else{
                // Permission Success
                getCompile()
            }
        }else{
            //System os is less than marshallow, runtime permission not required, perform download.
            getCompile()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission from popup was granted, perform download
                    getCompile()
                } else {
                    // permission from popup was denied, show error message
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG)
                }
            }
        }
    }

    private fun getCompile() {
        val roomServiceImpl = ServiceImpl()
        roomServiceImpl.getReporte(this) { response ->
            run {
                Log.v("Aqui!", "Hasta Aqui!!!")
            }
        }
        handler.postDelayed({
            startDownloading()
        }, 1000)
        handler.postDelayed({
            selectPdfFromStorage()
        }, 1100)

    }

    private fun selectPdfFromStorage(){
        Toast.makeText(this, "selectPDF", Toast.LENGTH_LONG).show()
        val browseStorage = Intent(Intent.ACTION_GET_CONTENT)
        browseStorage.type = "application/pdf"
        browseStorage.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(browseStorage, "Select PDF"), PDF_SELECTION_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PDF_SELECTION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedPdfFromStorage = data.data
            showReporte(selectedPdfFromStorage)
        }
    }

    private fun showReporte(uri: Uri?) {
        pdfView.fromUri(uri)
                .defaultPage(0)
                .spacing(10)
                .load()
    }

    private fun startDownloading() {
        val url = "http://192.168.56.1:8000/reports/zonasAca.pdf";
        //val url = "http://192.168.1.129:8000/reports/zonasAca.pdf";
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        Toast.makeText(this, "The file is downloading...", Toast.LENGTH_LONG).show()

        //request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}")

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }
}