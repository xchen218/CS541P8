package com.xchen218.cs541p8;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

public class Playground extends SurfaceView implements View.OnTouchListener {

    private static int WIDTH = 100;
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
        setOnTouchListener(this);
        initGame();
    }

    private Dot getDot(int x, int y){
        return matrix[y][x];
    }
    private void redraw(){
        Canvas c = getHolder().lockCanvas();
        c.drawColor(Color.LTGRAY);
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        for(int i = 0; i < ROW; i++){
            int offset = 0;
            if(i%2 == 1){
                offset = WIDTH/2;
            }
            for(int j = 0; j < COL; j++){
                    Dot one = getDot(j, i);
                    switch (one.getStatus()){
                        case Dot.STATUS_OFF:
                            paint.setColor(0xFFEEEEEE);
                            break;
                        case Dot.STATUS_ON:
                            paint.setColor(0xFFFFAA00);
                            break;
                        case Dot.STATUS_IN:
                            paint.setColor(0xFFFF0000);
                            break;
                        default:
                            break;
                    }
                    c.drawOval(new RectF(one.getX() * WIDTH + offset, one.getY() * WIDTH,
                            (one.getX()+1) * WIDTH + offset, (one.getY()+1)*WIDTH), paint);
            }
        }
        getHolder().unlockCanvasAndPost(c);
    }

    Callback callback = new Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            redraw();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            WIDTH = width/(COL+1);
            redraw();
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
            int x = (int) ((Math.random() * 1000) % COL);
            int y = (int) ((Math.random() * 1000) % ROW);
            if(getDot(x, y).getStatus() == Dot.STATUS_OFF){
                getDot(x, y).setStatus(Dot.STATUS_ON);
                i++;
                //Log.d("block", String.valueOf(i));
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            //Toast.makeText(getContext(), event.getX()+":"+event.getY(), Toast.LENGTH_SHORT).show();
            int x, y;
            y = (int) (event.getY()/WIDTH);
            if(y % 2 == 0){
                x = (int) (event.getX()/WIDTH);
            }else{
                x = (int) ((event.getX()-WIDTH/2)/WIDTH);
            }
            if(x + 1 > COL || y + 1 > ROW){
                initGame();
            }else{
                getDot(x, y).setStatus(Dot.STATUS_ON);
            }
            redraw();
        }
        return true;
    }
}
