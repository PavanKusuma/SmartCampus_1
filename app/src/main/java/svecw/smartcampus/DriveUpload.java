package svecw.smartcampus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import utils.Compressor;
import utils.Constants;
import utils.Routes;

/**
 * Created by Pavan_Kusuma on 12/8/2016.
 */

public class DriveUpload extends Activity {

    RelativeLayout selectFileLayout, selectedFileLayout;
    FloatingActionButton selectFile;
    ImageView filePreview, removeFile;
    TextView fileName, fileSize, fileUploadPercentage;
    ProgressBar uploadProgressBar;
    Button uploadBtn;

    String filePath;
    File file, compressedFile;
    boolean isImage = false;
    public static Bitmap bitmap;
    int size = 0;

    // Bytes of the media being uploaded
    byte[] b = Constants.null_indicator.getBytes();
    InputStream is;
    int status, serverResponseCode;


    private final String[] imageFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drive_upload);

        selectFileLayout = (RelativeLayout) findViewById(R.id.selectFileLayout);
        selectFile = (FloatingActionButton) findViewById(R.id.selectFile);
        selectedFileLayout = (RelativeLayout) findViewById(R.id.selectedFileLayout);
        filePreview = (ImageView) findViewById(R.id.filePreview);
        removeFile = (ImageView) findViewById(R.id.removeFile);
        fileName = (TextView) findViewById(R.id.fileName);
        fileSize = (TextView) findViewById(R.id.fileSize);
        fileUploadPercentage = (TextView) findViewById(R.id.fileUploadPercentage);
        uploadProgressBar = (ProgressBar) findViewById(R.id.uploadProgressBar);
        uploadBtn = (Button) findViewById(R.id.uploadBtn);

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // select image file
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, Constants.FILE_PICK);
            }
        });


        // remove the selected file and show the select file layout
        removeFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedFileLayout.setVisibility(View.GONE);
                selectFileLayout.setVisibility(View.VISIBLE);
            }
        });

        // upload button for uploading the selected file
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // display the upload progress views
                fileUploadPercentage.setVisibility(View.VISIBLE);
                uploadProgressBar.setVisibility(View.VISIBLE);

                // upload to server
                new UploadFileToServer1().execute(Routes.uploadToDrive, file.getName());

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {

            // check if result is obtained
            if(requestCode == Constants.FILE_PICK && resultCode == RESULT_OK){

                // check if data exists
                if(data != null) {


                    // get the actual image data to the file
                    file = Compressor.from(getApplicationContext(), data.getData());
                    filePath = file.getPath();

                        // as no data exists hide the "select file layout" and show the "selected file layout"
                        selectFileLayout.setVisibility(View.GONE);

                        // showing the selected file layout and display the file details
                        selectedFileLayout.setVisibility(View.VISIBLE);

                        fileName.setText(file.getName());
                        //fileSize.setText(String.format("%.2f",(file.length()/1024)).concat(" Kb")); // fetches file bytes and converts to KBs
                        fileSize.setText(String.valueOf(file.length()/1024).concat(" Kb")); // fetches file bytes and converts to KBs
                        size = (int) file.length(); // save the file size for showing progress

                        // show the upload button
                        uploadBtn.setVisibility(View.VISIBLE);

                    // check if file is image and compress the image file and
                    // fetch the inputStream of it and show the preview
                    // else fetch the inputStream of it and display file preview
                    if (checkIfImage(file)) {

                        isImage = true;
                        // compress Image
                        customCompressImage();
                    }
                    else {

                        ByteArrayOutputStream buffer = new ByteArrayOutputStream((int)file.length());
                        b = buffer.toByteArray();

                        is = new ByteArrayInputStream(b);
                    }

                }
                else {

                    Toast.makeText(this, "No data exists", Toast.LENGTH_SHORT).show();
                }

            }
            else {

                Toast.makeText(this, "Selection canceled", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // return true if file is image
    public boolean checkIfImage(File file){

        for (String extension : imageFileExtensions)
        {
            if (file.getName().toLowerCase().endsWith(extension))
            {
                return true;
            }
        }
        return false;
    }


    // compress the image
    public void customCompressImage() {
        if (file == null) {

            Toast.makeText(DriveUpload.this, "No file chosen!", Toast.LENGTH_SHORT).show();

        } else {

            // Compress image using RxJava in background thread with custom Compressor
            new Compressor.Builder(this)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.PNG)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .build()
                    .compressToFileAsObservable(file)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {
                            compressedFile = file;
                            setCompressedImage(compressedFile);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                            Toast.makeText(DriveUpload.this, "Error! Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // fetch the compressed image bitmap and display the preview
    private void setCompressedImage(File compressedImage1) {

        Log.v(Constants.appName, "Here is the path " + compressedImage1.getAbsolutePath());

        // fetch the compressed bitmap
        bitmap = BitmapFactory.decodeFile(compressedImage1.getAbsolutePath());

        ByteArrayOutputStream buffer = new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight());
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, buffer);
        b = buffer.toByteArray();
        Log.v(Constants.appName, "Here is the length " + b.length + " _ " + bitmap.getByteCount());

        // show the selected file layout
        selectedFileLayout.setVisibility(View.VISIBLE);

        // here we will display the compressed image
        filePreview.setImageBitmap(bitmap);
        filePreview.setAdjustViewBounds(true);

        // prepare the inputStream
        is = new ByteArrayInputStream(b);

    }

    // upload file to server and show the upload progress
    private class UploadFileToServer extends AsyncTask<String, String, String>{

        String data = "";

        @Override
        protected void onPreExecute() {
            Log.v(Constants.appName, "Debug 1");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            uploadProgressBar.setProgress(Integer.parseInt(values[0])); // update progress
            fileUploadPercentage.setText(String.valueOf(Integer.parseInt(values[0])).concat("%")); // show the percentage
            Log.v(Constants.appName, "Debug 2");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            Log.v(Constants.appName, "Debug 3");
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection connection = null;

            try {
                // Set Request parameter
                data += "?&" + URLEncoder.encode(Constants.name, "UTF-8") + "=" + (params[1]);
                Log.v(Constants.appName, "Debug 4");

                //URL url = new URL(params[0]+data);
                URL url = new URL(params[0]+data);

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                String boundary = "---------------------------boundary";
                String tail = "\r\n--" + boundary + "--\r\n";
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                connection.setDoOutput(true);

                String metadataPart = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"metadata\"\r\n\r\n"
                        + "" + "\r\n";

                String fileHeader1 = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"file\"; filename=\""
                        + fileName + "\"\r\n"
                        + "Content-Type: application/octet-stream\r\n"
                        + "Content-Transfer-Encoding: binary\r\n";

                long fileLength = 0;
                    // check if it is image file
                    if(isImage)
                        fileLength = compressedFile.length() + tail.length();
                    else
                        fileLength = file.length() + tail.length();

                String fileHeader2 = "Content-length: " + fileLength + "\r\n";
                String fileHeader = fileHeader1 + fileHeader2 + "\r\n";
                String stringData = metadataPart + fileHeader;

                long requestLength = stringData.length() + fileLength;
                connection.setRequestProperty("Content-length", "" + requestLength);
                connection.setFixedLengthStreamingMode((int) requestLength);
                connection.connect();

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(stringData);
                out.flush();
                Log.v(Constants.appName, "Debug 5");
                int progress = 0;
                int bytesRead = 0;
                byte buf[] = new byte[1024];
                BufferedInputStream bufInput;

                    // check if it is image file
                    if(isImage)
                        bufInput = new BufferedInputStream(new FileInputStream(compressedFile));
                    else
                        bufInput = new BufferedInputStream(new FileInputStream(file));

                while ((bytesRead = bufInput.read(buf)) != -1) {
                    // write output
                    out.write(buf, 0, bytesRead);
                    //out.flush();
                    progress += bytesRead; // Here progress is total uploaded bytes

                    publishProgress(""+(int)((progress*100)/ size)); // sending progress percent to publishProgress
                }
                Log.v(Constants.appName, "Debug 6");
                // Write closing boundary and close stream
                out.writeBytes(tail);
                out.flush();
                out.close();

                // Get server response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder builder = new StringBuilder();

                while((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                Log.v(Constants.appName, "Debug 7");
            } catch (Exception e) {
                // Exception
                Log.v(Constants.appName, e.getMessage());
            } finally {
                if (connection != null) connection.disconnect();
                Log.v(Constants.appName, "Disconnect");
            }

            return null;
        }
    }

    // upload file to server and show the upload progress
    private class UploadFileToServer1 extends AsyncTask<String, String, String>{

        String data = "";
        FileInputStream fileInputStream;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;


        @Override
        protected void onPreExecute() {
            Log.v(Constants.appName, "Debug 1");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            uploadProgressBar.setProgress(Integer.parseInt(values[0])); // update progress
            fileUploadPercentage.setText(String.valueOf(Integer.parseInt(values[0])).concat("%")); // show the percentage
            Log.v(Constants.appName, "Debug 2" + Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection.setFollowRedirects(false);

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead = 0, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 4 * 1024 * 1024;
            File sourceFile = new File(filePath);
            //File sourceFile = new File(image.getPath());
            if (!sourceFile.isFile()) {

                serverResponseCode = -1;

                //Log.v(Constants.appName, "Image not found");
            }
            else{

                //Log.v(Constants.appName, "Image found");
            }

            try {
                // Set Request parameter
                data += "?&" + URLEncoder.encode(Constants.name, "UTF-8") + "=" + (strings[1]);

                // open a URL connection to the Servlet
                fileInputStream = new FileInputStream(file);
                URL url = new URL(strings[0]+data);
                Log.v(Constants.appName, strings[0]+data);
                conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("file", filePath);
                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""+ fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                Log.v(Constants.appName, "posting..70%");



                // get length of bytes
                bytesAvailable = b.length;

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                int progress = 0;

                while((bytesRead = is.read(b))!= -1){
                    dos.write(b, 0, bytesRead);
                    progress += bytesRead; // Here progress is total uploaded bytes

                    publishProgress(""+(int)((progress*100)/(int)file.length())); // sending progress percent to publishProgress
                }

                /*// read file and write it into form...
                bytesRead = is.read(b, 0, b.length);

                dos.write(b, 0, b.length);


                while (bytesRead > 0) {
                    dos.write(b, 0, b.length);
                    bytesAvailable = is.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = is.read(buffer, 0, bufferSize);

                    Log.v(Constants.appName, "check 1");

                }
*/



                // send multipart form data necessary after file data...
                dos.writeBytes(lineEnd);

                Log.v(Constants.appName, "posting..80%");
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);




                Log.v(Constants.appName, "posting..85%");
                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + " ");
                }

                // Append Server Response To Content String
                Log.v(Constants.appName, sb.toString());

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.v(Constants.appName, "posting..90%");

                            // set progress
                            //progressBar.setProgress(90);
                            //Log.v(Constants.appName, "File Upload Completed.");

                            //Toast.makeText(CollegeWallNewPostActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();
                //Toast.makeText(AddEvent.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                e.printStackTrace();
                //Toast.makeText(AddEvent.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Upload file Exception", "Exception : " + e.getMessage());
            }

            return null;
        }
    }

}
