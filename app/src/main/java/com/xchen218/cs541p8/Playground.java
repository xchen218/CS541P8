package com.xchen218.cs541p8;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class Playground extends SurfaceView {

    private static final int ROW = 10;
    private static final int COL = 10;
    private static final int BLOCKS = 10;

    private Dot matrix[][];
    private Dot cat;
    public Playground(Context context) {
        super(context);
        getHolder().addCallback(callback);
        matrix  = new Dot[ROW][COL];
        for(int i = 0; i < ROW; i++){
            for(int j = 0; j < COL; j++){
                matrix[i][j] = new Dot(j, i);
            }
        }

        initGame();
    }

    private Dot getDot(int x, int y){
        return matrix[y][x];
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

    private void initGame(){
        for(int i = 0; i < ROW; i++){
            for(int  j = 0; j < COL; j++){
                matrix[i][j].setStatus(Dot.STATUS_OFF);
            }
        }
        cat = new Dot(4,5);
        getDot(4,5).setStatus(Dot.STATUS_IN);
        for(int i = 0; i < BLOCKS;){
            if(getDot(0, 0).getStatus() == Dot.STATUS_OFF){
                
            }
        }
    }
}
