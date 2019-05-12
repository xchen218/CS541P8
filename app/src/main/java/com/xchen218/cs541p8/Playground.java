package com.xchen218.cs541p8;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.inputmethodservice.Keyboard;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.util.Vector;

public class Playground extends SurfaceView implements View.OnTouchListener {
    int k = 1;
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

    private boolean isAtEdge(Dot d){
        if(d.getX() * d.getY() == 0 || d.getX() + 1 == COL || d.getY() + 1 == ROW){
            return true;
        }
        return false;
    }

    private Dot getNeighbor(Dot d, int dir){
        switch (dir){
            case 1:
                return getDot(d.getX() - 1, d.getY());
            case 2:
                if(d.getY() % 2 == 0){
                    return getDot(d.getX() - 1, d.getY() - 1);
                }else{
                    return getDot(d.getX(), d.getY() - 1);
                }
            case 3:
                if(d.getY() % 2 == 0){
                    return getDot(d.getX(), d.getY() - 1);
                }else{
                    return getDot(d.getX() + 1, d.getY() - 1);
                }
            case 4:
                return getDot(d.getX() + 1, d.getY());
            case 5:
                if(d.getY() % 2 == 0){
                    return getDot(d.getX(), d.getY() + 1);
                }else{
                    return getDot(d.getX() + 1, d.getY() + 1);
                }
            case 6:
                if(d.getY() % 2 == 0){
                    return getDot(d.getX() - 1, d.getY() + 1);
                }else{
                    return getDot(d.getX(), d.getY() + 1);
                }
            default:
                break;
        }
        return null;

    }

    private int getDistance(Dot d, int dir){
        int distance = 0;
        Dot curr = d, next;
        while(true){
            next = getNeighbor(curr, dir);
            if(next.getStatus() == Dot.STATUS_ON){
                return distance * -1;
            }
            if(isAtEdge(next)){
                return distance + 1;
            }
            distance ++;
            curr = next;
        }
        //return 0;
    }

    private void moveto(Dot d){
        d.setStatus(Dot.STATUS_IN);
        getDot(cat.getX(), cat.getY());
        cat.setXY(d.getX(), d.getY());
    }

    private void move(){
        if(isAtEdge(cat)){
            lose();
            return;
        }
        Vector<Dot> available = new Vector<>();
        for(int i = 1; i < 7; i++){
            Dot n = getNeighbor(cat, i);
            if(n.getStatus() == Dot.STATUS_OFF){
                available.add(n);
            }
        }
        if(available.size() == 0){
            win();
        }else{
            moveto(available.get(0));
        }
    }

    private void lose(){
        Toast.makeText(getContext(), "You lose! The prisoner escaped!", Toast.LENGTH_SHORT).show();
    }

    private void win(){
        Toast.makeText(getContext(), "You win! The prisoner is caught!", Toast.LENGTH_SHORT).show();
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
                //getNeighbor(cat, k).setStatus(Dot.STATUS_IN);
                //k++;
                initGame();
            }else if(getDot(x, y).getStatus() == Dot.STATUS_OFF){
                getDot(x, y).setStatus(Dot.STATUS_ON);
                move();
            }
            redraw();
        }
        return true;
    }
}
