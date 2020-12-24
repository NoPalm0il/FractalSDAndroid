package ipt.sd.fractalsdandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    ImageView fractalView;
    EditText pointXTxt, pointYTxt, zoomTxt, iterationsTxt, serverAddressTxt;
    Button generateFractalBt, resetValuesBt;
    String[] serverAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fractalView = findViewById(R.id.imageView);
        pointXTxt = findViewById(R.id.pointXTxt);
        pointYTxt = findViewById(R.id.pointYTxt);
        zoomTxt = findViewById(R.id.zoomTxt);
        iterationsTxt = findViewById(R.id.iterationTxt);
        serverAddressTxt = findViewById(R.id.serverAddressTxt);
        generateFractalBt = findViewById(R.id.generateFractalBt);
        resetValuesBt = findViewById(R.id.resetButton);

        generateFractalBt.setOnClickListener((view) -> {
            serverAddr = serverAddressTxt.getText().toString().split(":");
            getFractalImage(serverAddr[0], Integer.parseInt(serverAddr[1]), getFractalParams());
        });

        resetValuesBt.setOnClickListener((view) -> {
            pointXTxt.setText("-1.0");
            pointYTxt.setText("0.0");
            zoomTxt.setText("4.0");
            iterationsTxt.setText("500");

            BitmapDrawable obj = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(new byte[1], 0, 0));
            fractalView.setImageDrawable(obj);
            fractalView.postInvalidate();
        });

        // TODO: ISTO
        fractalView.setOnClickListener((view) -> {
            float x = Float.parseFloat(pointXTxt.getText().toString());
            float y = Float.parseFloat(pointYTxt.getText().toString());
            PointF currCenter = new PointF(x, y);
            PointF newCenter = getRealCoordinates(view.getX(), view.getY(), 400, currCenter);
            pointXTxt.setText("" + newCenter.x);
            pointYTxt.setText("" + newCenter.y);
            double newZoom = Double.parseDouble(zoomTxt.getText().toString()) / 3.2;
            zoomTxt.setText("" +newZoom);
            serverAddr = serverAddressTxt.getText().toString().split(":");
            getFractalImage(serverAddr[0], Integer.parseInt(serverAddr[1]), getFractalParams());
        });
    }

    private void updateGui(final Bitmap bmp) {
        Handler uiThread = new Handler(Looper.getMainLooper());
        uiThread.post(() -> {
            BitmapDrawable obj = new BitmapDrawable(getResources(), bmp);
            fractalView.setImageDrawable(obj);
            fractalView.postInvalidate();
        });
    }

    private Bitmap readImage(DataInputStream input) throws IOException {
        byte[] byteArray = new byte[1024 * 1024]; // 1 MB buffer
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int bytesRead, totalBytes = 0;
        //read all bytes from stream
        while ((bytesRead = input.read(byteArray)) > 0) {
            bos.write(byteArray, 0, bytesRead); // put byte array in memory
            totalBytes += bytesRead; // increase total bytes
        }
        //builds a bitmap with byte array
        return BitmapFactory.decodeByteArray(bos.toByteArray(), 0, totalBytes);
    }

    public void getFractalImage(String serverName, int serverPort, String fractalParams) {
        Thread thr = new Thread(() -> {
            try {
                // open connection
                Socket balc = new Socket(serverName, serverPort);
                DataOutputStream out = new DataOutputStream(balc.getOutputStream());
                DataInputStream in = new DataInputStream(balc.getInputStream());
                // type of socket
                out.writeChar('c');
                //send fractal parameters to server
                out.writeUTF(fractalParams);
                out.flush();
                // get fractal image
                Bitmap bmp = readImage(in);
                // display image
                updateGui(bmp);
                // close connection
                balc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thr.start();
    }

    public PointF getRealCoordinates(float xx, float yy, int size, PointF center) {
        float ws = Float.parseFloat(zoomTxt.getText().toString());
        float pixelSize = ws / size;
        float minX = center.x - ws / 2;
        float miny = center.y + ws / 2;
        float x = minX + pixelSize * xx;
        float y = miny - pixelSize * yy;
        return new PointF(x, y);
    }

    private String getFractalParams(){
        return pointXTxt.getText().toString() + " " +
                pointYTxt.getText().toString() + " " +
                zoomTxt.getText().toString() + " " +
                iterationsTxt.getText().toString() + " " +
                "500 500";
    }
}