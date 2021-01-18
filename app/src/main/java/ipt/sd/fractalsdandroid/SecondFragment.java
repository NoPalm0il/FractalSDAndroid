package ipt.sd.fractalsdandroid;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    ImageView fractalView;
    MainActivity act;


    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        act = (MainActivity) getActivity();
        fractalView = getView().findViewById(R.id.imageView);


        atualiza act = new atualiza();
        act.setDaemon(true);
        act.start();

    }


    private void updateGui(final Bitmap bmp) {
        Handler uiThread = new Handler(Looper.getMainLooper());
        uiThread.post(() -> {
            BitmapDrawable obj = new BitmapDrawable(getResources(), bmp);
            fractalView.setImageDrawable(obj);
            fractalView.postInvalidate();
        });
    }

    public class atualiza extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                if (act.refresh) {
                    new play().start();
                    act.refresh = false;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class play extends Thread {
        @Override
        public void run() {
            ArrayList<Bitmap> frames = (ArrayList<Bitmap>) act.imagem.clone();
            for (Bitmap name : frames) {
                updateGui(name);
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
