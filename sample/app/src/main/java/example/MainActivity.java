package example;

import android.app.Activity;
import android.os.Bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import my.example.R;

public class MainActivity extends Activity {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);

    // CR Code Review

    // TODO To Do

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        LOGGER.info("MainActivity::onCreate");
        LOGGER.info("AndroidLibrary.process: {}", AndroidLibrary.process(this));
        LOGGER.info("JavaLibrary.process: {}", JavaLibrary.process(this));

        new Thread() {
            @Override
            public void run() {
                try {
                    Object object = "";
                    Double value = (Double) object;
                } catch (Exception e) {

                }

                "".length();
            }
        }.start();
    }

}
