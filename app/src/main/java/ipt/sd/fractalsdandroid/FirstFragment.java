package ipt.sd.fractalsdandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class FirstFragment extends Fragment {

    ImageView fractalView;
    EditText pointXTxt, pointYTxt, zoomTxt, iterationsTxt, serverAddressTxt, frames;
    Button generateFractalBt, resetValuesBt;
    String[] serverAddr;
    MainActivity act;



    public FirstFragment(){

    }

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition transition = TransitionInflater.from(requireContext()).inflateTransition(R.id.imageView);
        setSharedElementEnterTransition(transition);
    }

     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        act = (MainActivity) getActivity();

        fractalView = getView().findViewById(R.id.imageView);
        pointXTxt = getView().findViewById(R.id.pointXTxt);
        pointYTxt = getView().findViewById(R.id.pointYTxt);
        zoomTxt = getView().findViewById(R.id.zoomTxt);
        iterationsTxt = getView().findViewById(R.id.iterationTxt);
        serverAddressTxt = getView().findViewById(R.id.serverAddressTxt);
        generateFractalBt = getView().findViewById(R.id.generateFractalBt);
        resetValuesBt = getView().findViewById(R.id.resetButton);
        frames = getView().findViewById(R.id.framesText);

        generateFractalBt.setOnClickListener((view2) -> {
            serverAddr = serverAddressTxt.getText().toString().split(":");
            int frames = Integer.parseInt(String.valueOf(this.frames.getText()));
            getFractalImage(serverAddr[0], Integer.parseInt(serverAddr[1]), getFractalParams(), frames);

            SecondFragment secondFragment = new SecondFragment();
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.container1, secondFragment).commit();

        });

        resetValuesBt.setOnClickListener((view2) -> {
            pointXTxt.setText("-1.7685374027917619472408834008472010565952062055525180562253268903921439965790");
            pointYTxt.setText("0.0005400495035209695647102872054426089298508762991118705637984149391509006042");
            zoomTxt.setText("4.0");
            iterationsTxt.setText("500");

            BitmapDrawable obj = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(new byte[1], 0, 0));
            fractalView.setImageDrawable(obj);
            fractalView.postInvalidate();
        });

    }




    /*Utils
        ||
        ||
        \/
    */


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

                act.imagem = readImage(in);
                Thread.sleep(20);

                // close connection
                balc.close();
                out.close();
                in.close();
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

}