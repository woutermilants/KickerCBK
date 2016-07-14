package Android.KickerCBKL;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ToastHandler extends Activity {

    private Handler hm;

    @Override
    public void onCreate(Bundle b) {
        hm = new Handler() {
            public void handleMessage(Message m) {
                Toast.makeText(getApplicationContext(), "my message", Toast.LENGTH_LONG).show();
            }
        };
    }

    public Handler returnHandler() {
        return hm;
    }

}
