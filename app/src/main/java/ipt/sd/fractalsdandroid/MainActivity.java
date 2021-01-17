package ipt.sd.fractalsdandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String BAL_ADDRESS = "194.210.113.23:10011";

    ImageView fractalView;
    EditText pointXTxt, pointYTxt, zoomTxt, iterationsTxt, serverAddressTxt, framesTxt;
    Button generateFractalBt, resetValuesBt;
    String[] serverAdr;
    int frames;
    Button btFrag1, btFrag2;
    public volatile Bitmap imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btFrag1 = findViewById(R.id.btnFrag1);
        btFrag2 = findViewById(R.id.btnFrag2);

        FirstFragment firstFragment = new FirstFragment();
        SecondFragment secondFragment = new SecondFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, firstFragment).commit();

        btFrag1.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, firstFragment).commit();

        });

        btFrag2.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, secondFragment).commit();

        });



        fractalView = findViewById(R.id.imageView);
        pointXTxt = findViewById(R.id.pointXTxt);
        pointYTxt = findViewById(R.id.pointYTxt);
        zoomTxt = findViewById(R.id.zoomTxt);
        iterationsTxt = findViewById(R.id.iterationTxt);
        serverAddressTxt = findViewById(R.id.serverAddressTxt);
        generateFractalBt = findViewById(R.id.generateFractalBt);
        resetValuesBt = findViewById(R.id.resetButton);
        framesTxt = findViewById(R.id.framesText);

        serverAddressTxt.setText(BAL_ADDRESS);

        generateFractalBt.setOnClickListener((view) -> {
            serverAdr = serverAddressTxt.getText().toString().split(":");
            frames = Integer.parseInt(String.valueOf(this.framesTxt.getText()));
            getFractalFrames(serverAdr[0], Integer.parseInt(serverAdr[1]), getFractalParams(), frames);
        });

        resetValuesBt.setOnClickListener((view) -> {
            pointXTxt.setText("-1.7685374027917619472408834008472010565952062055525180562253268903921439965790");
            pointYTxt.setText("0.0005400495035209695647102872054426089298508762991118705637984149391509006042");
            zoomTxt.setText("4.0");
            iterationsTxt.setText("1000");

            BitmapDrawable obj = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(new byte[1], 0, 0));
            fractalView.setImageDrawable(obj);
            fractalView.postInvalidate();
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

    private ArrayList<Bitmap> readFrames(ObjectInputStream input) throws IOException, InterruptedException, ClassNotFoundException {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        byte[][] byteArray; // 1 MB buffer
        //read all bytes from stream
        byteArray = (byte[][]) input.readObject();
        for (byte[] frame : byteArray) {
            Bitmap img = BitmapFactory.decodeByteArray(frame, 0, frame.length);
            if (img != null)
                bitmaps.add(img);
        }
        //builds a bitmap with byte array
        return bitmaps;
    }

    public void getFractalFrames(String serverName, int serverPort, String fractalParams, int frames) {
        new Thread(() -> {
            try {
                // open connection
                Socket balc = new Socket(serverName, serverPort);
                DataOutputStream out = new DataOutputStream(balc.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(balc.getInputStream());
                //send fractal parameters to server
                out.writeUTF(fractalParams);
                out.flush();
                // get fractal image
                ArrayList<Bitmap> bmp = readFrames(in);
                 for (int i = 0; i < frames; i++) {
                 // display image
                 updateGui(bmp.get(i));
                 Thread.sleep(40);
                 }
                // close connection
                balc.close();
            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String getFractalParams(){
        return pointXTxt.getText().toString() + " " +
                pointYTxt.getText().toString() + " " +
                zoomTxt.getText().toString() + " " +
                iterationsTxt.getText().toString() + " " +
                "500 500 " +
                framesTxt.getText().toString();
    }
}