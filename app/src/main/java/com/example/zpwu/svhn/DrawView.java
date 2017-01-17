package com.example.zpwu.svhn;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

public class DrawView extends View {

    private Paint paint = new Paint();
    private DrawModel model;
    private Bitmap offscreenbitmap;
    private Canvas offscreencanvas;

    private Matrix matrix = new Matrix();
    private Matrix invMatrix = new Matrix();
    private int drawnLiseSize = 0;
    private boolean setuped = false;

    private float tmpPoints[] = new float[2];

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setModel(DrawModel model) {
        this.model = model;
    }

    public void reset() {
        drawnLiseSize = 0;
        if (offscreenbitmap != null) {
            paint.setColor(Color.WHITE);
            int width = offscreenbitmap.getWidth();
            int height = offscreenbitmap.getHeight();
            offscreencanvas.drawRect(new Rect(0, 0, width, height), paint);
        }
    }

    private void setup() {

        // View size
        float width = getWidth();
        float height = getHeight();

        // Model(bitmap) size
        float modelWidth = model.getWidth();
        float modelHeight = model.getHeight();


        float scaleWidth = width / modelWidth;
        float scaleHeight = height / modelHeight;

        float scale = scaleWidth;
        if (scale > scaleHeight) {
            scale = scaleHeight;
        }

        float newCurrentX = modelWidth * scale / 2;
        float newCurrentY = modelHeight * scale / 2;
        float dx = width / 2 - newCurrentX;
        float dy = height / 2 - newCurrentY;

        matrix.setScale(scale, scale);
        matrix.postTranslate(dx, dy);
        matrix.invert(invMatrix);
        setuped = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (model == null) {
            return;
        }
        if (!setuped) {
            setup();
        }
        if (offscreenbitmap == null) {
            return;
        }
        int startIndex = drawnLiseSize - 1 < 0 ? 0 : drawnLiseSize - 1;
        DrawRenderer.renderModel(offscreencanvas, model, paint, startIndex);
        canvas.drawBitmap(offscreenbitmap, matrix, paint);

        drawnLiseSize = model.getLineSize();
    }

    public void calcutePostion(float x, float y, PointF out) {
        tmpPoints[0] = x;
        tmpPoints[1] = y;
        invMatrix.mapPoints(tmpPoints);
        out.x = tmpPoints[0];
        out.y = tmpPoints[1];
    }

    public void onResume() {
        createBitmap();
    }

    public void onPause() {
        createBitmap();
    }

    private void createBitmap() {
        if (offscreenbitmap != null) {
            offscreenbitmap.recycle();
        }
        offscreenbitmap = Bitmap.createBitmap(model.getWidth(), model.getHeight(), Bitmap.Config.ARGB_8888);
        offscreencanvas = new Canvas(offscreenbitmap);
        reset();
    }

    private void releaseBitmap() {
        if (offscreenbitmap != null) {
            offscreenbitmap.recycle();
            offscreenbitmap = null;
            offscreencanvas = null;
        }
        reset();
    }

    public int[] getPixelData() {
        if (offscreenbitmap != null) {
            return null;
        }

        int width = offscreenbitmap.getWidth();
        int height = offscreenbitmap.getHeight();

        int[] pixels = new int[width * height];
        offscreenbitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int[] retPixels = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            int pix = pixels[i];
            int b = pix & 0xff;
            retPixels[i] = 0xff - b;
        }
        return retPixels;
    }
}
