package ipt.sd.fractalsdandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class FirstFragment extends Fragment {

    // variavel criada para facilitar o debug
    static final String BAL_ADDRESS = "192.168.1.76:10019";

    EditText pointXTxt, pointYTxt, zoomTxt, iterationsTxt, serverAddressTxt, framesTxt;
    Button generateFractalBt, zoomPointBt;
    MainActivity act;
    String[] serverAdr;
    Spinner dropdown;


    public FirstFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        act = (MainActivity) getActivity();

        pointXTxt = getView().findViewById(R.id.pointXTxt);
        pointYTxt = getView().findViewById(R.id.pointYTxt);
        zoomTxt = getView().findViewById(R.id.zoomTxt);
        iterationsTxt = getView().findViewById(R.id.iterationTxt);
        serverAddressTxt = getView().findViewById(R.id.serverAddressTxt);
        generateFractalBt = getView().findViewById(R.id.generateFractalBt);
        zoomPointBt = getView().findViewById(R.id.zoomPointBt);
        framesTxt = getView().findViewById(R.id.framesText);
        serverAddressTxt.setText(BAL_ADDRESS);
        dropdown = getView().findViewById(R.id.spinner);

        String[] items = new String[]{"Mandelbrot", "Burning Ship", "Julia 1", "Julia 2"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        generateFractalBt.setOnClickListener((view2) -> {
            serverAdr = serverAddressTxt.getText().toString().split(":");
            getFractalFrames(serverAdr[0], Integer.parseInt(serverAdr[1]), getFractalParams());
        });

        zoomPointBt.setOnClickListener((view2) -> {
            pointXTxt.setText("-1.7685374027917619472408834008472010565952062055525180562253268903921439965790");
            pointYTxt.setText("0.0005400495035209695647102872054426089298508762991118705637984149391509006042");
            zoomTxt.setText("4.0");
            iterationsTxt.setText("500");
        });

    }

    /**
     * Recebe as frames a partir da input stream e transforma os bytes em imagens
     * @param input stream do balanceador
     * @return as frames já renderizadas
     */
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

    /**
     * Estabelece ligacao com o balanceador, envia os parametros do fractal e recebe as frames
     * do mesmo
     * @param serverName Ip do servidor
     * @param serverPort Porta do servidor
     * @param fractalParams Parametros do fractal
     */
    public void getFractalFrames(String serverName, int serverPort, String fractalParams) {
        new Thread(() -> {
            try {
                // open connection
                Socket balc = new Socket(serverName, serverPort);
                DataOutputStream out = new DataOutputStream(balc.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(balc.getInputStream());
                //getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Connected to balancer", Toast.LENGTH_SHORT));
                //send fractal parameters to server
                out.writeUTF(fractalParams);
                out.flush();

                // get fractal image
                act.frames = readFrames(in);

                // close connection
                out.flush();
                balc.close();
                act.refresh = true;
            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Pega em todos os campos de input e transforma numa unica {@link String}
     * @return propriedades do fratal separados por um espaco
     */
    private String getFractalParams(){
        String params = pointXTxt.getText().toString() + " " +
                pointYTxt.getText().toString() + " " +
                zoomTxt.getText().toString() + " " +
                iterationsTxt.getText().toString() + " " +
                "500 650 " +
                framesTxt.getText().toString();

        switch (dropdown.getSelectedItem().toString()) {
            case "Burning Ship":
                params += " b";
                break;
            case "Julia 1":
                params += " j1";
                break;
            case "Julia 2":
                params += " j2";
                break;
            default:
                params += " m";
                break;
        }

        return params;
    }
}