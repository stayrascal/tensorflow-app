package com.example.zpwu.svhn;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    public static final String TAG = "MainActivity";
    private static final int PIXEL_WIDTH = 28;

    private TextView resultText;

    private float lastX;
    private float lastY;

    private DrawModel model;
    private DrawView drawView;

    private PointF tmpPoint = new PointF();
    private DigitDetector detector = new DigitDetector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean ret = detector.setup(this);
        if (!ret) {
            Log.i(TAG, "Detector setup failed");
        }

        model = new DrawModel(PIXEL_WIDTH, PIXEL_WIDTH);
        drawView = (DrawView) findViewById(R.id.view_draw);
        drawView.setModel(model);
        drawView.setOnTouchListener(this);

        View detectButton = findViewById(R.id.button_detect);
        detectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetectClicked();
            }
        });

        View clearButton = findViewById(R.id.button_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClearClicked();
            }
        });

        resultText = (TextView) findViewById(R.id.text_result);
    }

    @Override
    protected void onResume() {
        drawView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        drawView.onPause();
        super.onPause();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        if (action == MotionEvent.ACTION_DOWN) {
            processTouchDown(event);
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            processTouchMove(event);
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            processTouchUp();
            return true;
        }
        return false;
    }

    private void processTouchDown(MotionEvent event) {
        lastX = event.getX();
        lastY = event.getY();
        drawView.calcutePostion(lastX, lastY, tmpPoint);
        float lastConvX = tmpPoint.x;
        float lastConvY = tmpPoint.y;
        model.startLine(lastConvX, lastConvY);
    }

    private void processTouchMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        drawView.calcutePostion(x, y, tmpPoint);
        float newConvX = tmpPoint.x;
        float newConvY = tmpPoint.y;
        model.startLine(newConvX, newConvY);
        lastX = x;
        lastY = y;
        drawView.invalidate();
    }

    private void processTouchUp() {
        model.endLine();
    }

    private void onDetectClicked() {
        int pixels[] = drawView.getPixelData();
        int digit = detector.detectDigit(pixels);

        Log.i(TAG, "digit =" + digit);

        resultText.setText("Detected = " + digit);
    }

    private void onClearClicked() {
        model.clear();
        drawView.reset();
        drawView.invalidate();

        resultText.setText("");
    }

}
