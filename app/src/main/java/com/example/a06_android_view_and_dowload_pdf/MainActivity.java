package com.example.a06_android_view_and_dowload_pdf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton download;
    PDFView PDF_View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        download = findViewById(R.id.download);
        PDF_View = findViewById(R.id.PDF_View);

        new MainActivity.Retrivie_PDF().execute("https://www.africau.edu/images/default/sample.pdf");
        download.setOnClickListener(v -> Dowload());
    }

    void Dowload(){
        Toast.makeText(this, "Đã bấm nút Download", Toast.LENGTH_SHORT).show();
        String URL_DOWNLOAD = "https://www.africau.edu/images/default/sample.pdf";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        Download_PDF(URL_DOWNLOAD);
        builder.show();
        Toast.makeText(this, "Đã tải xuống File PDF", Toast.LENGTH_SHORT).show();
    }

    private void Download_PDF(String pdf_URL){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdf_URL)); // Tạo một đối tượng yêu cầu tài xuống (DownloadManager.Request), đối tượng được cấu hình từ URL
        request.setTitle("PDF DOWNLOAD");
        request.setDescription("Download the PDF File");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE); // Đưa ra thông báo trong quá trình tải xuống (Hiển thị trên thanh thông báo điện thoại)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Curriculum Vitae.pdf"); // Đặt vị trí tải xuống là thư mục tải xuống của bộ nhớ ngoài và đặt tên
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE); // Đối tượng này được sử dụng để quản lý quá trình tải xuống
        manager.enqueue(request); // Đưa ra yêu cầu tải xuống vào hàng đợi của DownloadManager ==> Sau khi gọi sẽ tự động quản lý quá trình tải xuống tệp vào thư mục đã chỉ định
    }

    class Retrivie_PDF extends AsyncTask<String, Void, InputStream>{

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode()==200){
                    inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                }
            }catch (IOException e){
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            PDF_View.fromStream(inputStream).load();
        }
    }
}