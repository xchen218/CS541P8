package com.xchen218.cs541p8;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class Playground extends SurfaceView {
    public Playground(Context context) {
        super(context);
        getHolder().addCallback(callback);
    }

    private void redraw(){
        Canvas c = getHolder().lockCanvas();
        c.drawColor(Color.CYAN);
        getHolder().unlockCanvasAndPost(c);
    }

    Callback callback = new Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            redraw();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };
}
