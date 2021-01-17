package ipt.sd.fractalsdandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

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


/*
        fractalView = findViewById(R.id.imageView);
        pointXTxt = findViewById(R.id.pointXTxt);
        pointYTxt = findViewById(R.id.pointYTxt);
        zoomTxt = findViewById(R.id.zoomTxt);
        iterationsTxt = findViewById(R.id.iterationTxt);
        serverAddressTxt = findViewById(R.id.serverAddressTxt);
        generateFractalBt = findViewById(R.id.generateFractalBt);
        resetValuesBt = findViewById(R.id.resetButton);
        frames = findViewById(R.id.framesText);
*/



        /*

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabItem tabDados = findViewById(R.id.dadosTab);
        TabItem tabFractal = findViewById(R.id.fractalTab);
        ViewPager viewPager = findViewById(R.id.viewPager);


        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


         */









/*
        generateFractalBt.setOnClickListener((view) -> {
            serverAddr = serverAddressTxt.getText().toString().split(":");
            int frames = Integer.parseInt(String.valueOf(this.frames.getText()));
            getFractalImage(serverAddr[0], Integer.parseInt(serverAddr[1]), getFractalParams(), frames);

        });

        resetValuesBt.setOnClickListener((view) -> {
            pointXTxt.setText("-1.7685374027917619472408834008472010565952062055525180562253268903921439965790");
            pointYTxt.setText("0.0005400495035209695647102872054426089298508762991118705637984149391509006042");
            zoomTxt.setText("4.0");
            iterationsTxt.setText("500");

            BitmapDrawable obj = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(new byte[1], 0, 0));
            fractalView.setImageDrawable(obj);
            fractalView.postInvalidate();
        });
*/
    }

    /*
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

    public void getFractalImage(String serverName, int serverPort, String fractalParams, int frames) {
        Thread thr = new Thread(() -> {
            try {
                // open connection
                Socket balc = new Socket(serverName, serverPort);
                DataOutputStream out = new DataOutputStream(balc.getOutputStream());
                DataInputStream in = new DataInputStream(balc.getInputStream());
                //send fractal parameters to server
                out.writeUTF(fractalParams);
                out.flush();

                Bitmap bmp = readImage(in);
                // display image
                updateGui(bmp);
                Thread.sleep(20);

                // close connection
                balc.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thr.start();
    }

    private String getFractalParams(){
        return pointXTxt.getText().toString() + " " +
                pointYTxt.getText().toString() + " " +
                zoomTxt.getText().toString() + " " +
                iterationsTxt.getText().toString() + " " +
                "500 500 " +
                frames.getText().toString();
    }

     */
}