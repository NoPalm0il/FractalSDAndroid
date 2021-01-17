package ipt.sd.fractalsdandroid;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        new atualiza().start();

    }


    private void updateGui(final Bitmap bmp) {
        Handler uiThread = new Handler(Looper.getMainLooper());
        uiThread.post(() -> {
            BitmapDrawable obj = new BitmapDrawable(getResources(), bmp);
            fractalView.setImageDrawable(obj);
            fractalView.postInvalidate();
        });
    }


    public class atualiza extends Thread{
        @Override
        public void run() {
            while(!isInterrupted()){
                if(act.imagem != null)
                    updateGui(act.imagem);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

//ImageView do Fractal

/*<ImageView
            android:layout_width="500dp"
            android:layout_height="810dp" android:id="@+id/imageView"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintBottom_toBottomOf="parent" app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintHorizontal_bias="0.651" app:layout_constraintVertical_bias="0.565"/>
*/