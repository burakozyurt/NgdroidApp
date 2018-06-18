package com.ngdroidapp;

        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Rect;


        import istanbul.gamelab.ngdroid.base.BaseCanvas;
        import istanbul.gamelab.ngdroid.util.Log;
        import istanbul.gamelab.ngdroid.util.Utils;

/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */


public class GameCanvas extends BaseCanvas {
    private Bitmap tileset,spritesheet,rockset;//Zemin resmimiz,Kovboyumuz
    private Rect tilesource,tiledestination,spritesource,spritedestination,rocksource,rockdestination;

    //tilesource genişlik,yükseklik, tiledestination,genişlik,yükseklik
    private int tilesrcw,tilesrch,tiledstw,tiledsth;

    private int tilesrcx,tilesrcy,tiledstx,tiledsty;
    //spritesource genişlik,yükseklik, spritedestination,genişlik,yükseklik
    private int spritesrcw,spritesrch,spritedstw,spritedsth;
    private int spritesrcx,spritesrcy,spritedstx,spritedsty;
    //sprite hız x/y, sprite işaret x/y
    private int spritevx,spritevy,spriteix,spriteiy;
    //rocksource genişlik,yükseklik, rockdestination,genişlik,yükseklik
    private int rocksrcw,rocksrch,rockdstw,rockdsth;
    private int rocksrcx,rocksrcy,rockdstx,rockdsty;

    //ekrana dokunmaya başladığımız noktanın x/y koordinatları
    private int touchdownx,touchdowny;

    //Yön belirteci
    private int yon;

    //Animasyon türleri:durma,yürüme,silah doğrultma,ateş etme
    private int animationtypes=4;

    //cowboy.png'deki kare numarası,animasyon türleri 0,1,2,3,karenumarası alt ve üst sınır.
    private int framenum,animationtype,animationfirstfremenum[],animationlastframenum[];
    //ekrana do

    public GameCanvas(NgApp ngApp) {
        super(ngApp);
    }

    public void setup() {
        Log.i(TAG, "setup");
        setupTile();
        setupSpriteSheet();
        setupAnimation();

    }
    public void setupTile(){
        tileset=Utils.loadImage(root,"tilea2.png");
        tilesource=new Rect();
        tiledestination=new Rect();
        //Tilea2
        tilesrcw=64;
        tilesrch=64;
        tilesrcx=0;
        tilesrcy=0;
        tiledstw=128;
        tiledsth=128;
        tiledstx=0;
        tiledsty=0;
        //Rock Destination
        rockset=Utils.loadImage(root,"rock02.png");
        rocksource=new Rect();
        rockdestination=new Rect();

        //rockset
        rocksrcx=0;
        rocksrcy=0;
        rocksrcw=328;
        rocksrch=264;
        rockdstw=128;
        rockdsth=128;
        rockdstx=getWidth()/2;
        rockdsty=getHeight()/2;

    }
    public void setupSpriteSheet(){
        spritesheet=Utils.loadImage(root,"cowboy.png");

        spritesource=new Rect();
        spritedestination=new Rect();

        //Spritesheet
        spritedstx=0;
        spritedsty=0;
        spritesrcx=0;
        spritesrcy=0;
        spritesrcw=128;
        spritesrch=128;
        spritedstw=256;
        spritedsth=256;
        //sprite hız ve işaret
        spritevx=spritedstw/16;
        spritevy=spritedsth/16;

        spriteix=1;
        spriteiy=0;

    }
    public void setupAnimation(){
        yon=3;
        //Cowboy.png de soldan sağa kaçıncı karede olduğumuz gösterir 0,1,2,....12
        framenum=0;
        //Animasyon türü: durma=0,yürüme=1,silah doğrultma=2,ateş etme =3
        animationtype=1;

        //animasyon türleri için alt/üst sınır dizisi
        //kare numarası(frameno) bu sınırlar arasında arttıracağız
        animationfirstfremenum=new int[animationtypes];
        animationlastframenum=new int[animationtypes];

        //durma (0) animasyonu 0.karede başlar & biter.
        animationfirstfremenum[0]=0;
        animationlastframenum[0]=0;
        //yürüme(1) animasyonu 1.karede başlar, 8.karede biter.
        animationfirstfremenum[1]=1;
        animationlastframenum[1]=8;

        //silah çekme(2) animasyonu 9.karede başlar, 11.karede biter.
        animationfirstfremenum[2]=9;
        animationlastframenum[2]=11;
        //ateş etme animasyonu 12.karede başlar,13.karede biter.
        animationfirstfremenum[3]=12;
        animationlastframenum[3]=13;

    }

    public void update() {
        Log.i(TAG, "update");
        spritedstx += spritevx * spriteix;
        spritedsty += spritevy * spriteiy;
        framenum++;
        if (framenum >animationlastframenum[animationtype]){
            framenum =animationfirstfremenum[animationtype];
        }
        if (spritedstx >getWidth()-spritedstw){
            spritedstx =getWidth()-spritedstw;
            animationtype =0;
        }else if(spritedstx<0){
            //sol duvarda durur
            spritedstx =0;
            animationtype=0;
        }else if(spritedsty>getHeight()-spritedsth){
            spritedsty =getHeight()-spritedsth;
            animationtype =0;
        }else if(spritedsty<0){
            //üst duvarda durur
            spritedsty =0;
            animationtype =0;
        }else if(spritedestination.contains(rockdestination)){
            animationtype=0;
            spritedstx -= spritevx*spriteix*2;
            spritedsty -= spritevy*spriteiy*2;
            spritevy=0;
            spritevx=0;
            Log.i(TAG,"Collusion Detected");
        }

        //cowboy.png'deki animasyon karesinin koordinatını framenum ve genişlik cinsinden girelim
        spritesrcx=framenum*spritesrcw;
        spritesrcy=yon*spritesrch;
    }

    public void draw(Canvas canvas) {
        Log.i(TAG, "draw");
        for (int i=0;i<getWidth();i+=tiledstw){
            for(int j=0;j<getHeight();j+=tiledsth){
                tilesource.set(tilesrcx,tilesrcy,tilesrcx+tilesrcw,tilesrcy+tilesrch);
                tiledestination.set(i,j,i+tiledstw,j+tiledsth);
                canvas.drawBitmap(tileset,tilesource,tiledestination,null);
            }
        }


        spritesource.set(spritesrcx,spritesrcy,spritesrcx+spritesrcw,spritesrcy+spritesrch);
        spritedestination.set(spritedstx,spritedsty,spritedstx+spritedstw,spritedsty+spritedsth);
        canvas.drawBitmap(spritesheet,spritesource,spritedestination,null);

        rocksource.set(rocksrcx,rocksrcy,rocksrcx+rocksrcw,rocksrcy+rocksrch);
        rockdestination.set(rockdstx,rockdsty,rockdstx+rockdstw,rockdsty+rockdsth);
        canvas.drawBitmap(rockset,rocksource,rockdestination,null);



    }

    public void keyPressed(int key) {

    }

    public void keyReleased(int key) {

    }

    public boolean backPressed() {
        return true;
    }

    public void surfaceChanged(int width, int height) {

    }

    public void surfaceCreated() {

    }

    public void surfaceDestroyed() {

    }

    public void touchDown(int x, int y, int id) {
        touchdownx=x;
        touchdowny=y;

    }

    public void touchMove(int x, int y, int id) {
    }

    public void touchUp(int x, int y, int id) {
        int xfarki=x-touchdownx;
        int yfarki=y-touchdowny;
        //x farki büyükse sağa/sola, yfarki büyükse yukarı/aşağı gitsin

        if (Math.abs(xfarki)>=Math.abs(yfarki)){
            spritevx=spritedstw/16;
            spriteix=1;
            yon=3;
            animationtype=1;
            if(xfarki<0){
                spriteix=-1;
                yon=7;
            }
            spriteiy=0;
        }else {
            spritevy=spritedsth/16;
            spriteiy= 1;
            yon=9;
            animationtype=1;
            if (yfarki<0){
                spriteiy=-1;
                yon=5;
            }
            spriteix=0;
        }

    }


    public void pause() {

    }


    public void resume() {

    }


    public void reloadTextures() {

    }


    public void showNotify() {
    }

    public void hideNotify() {
    }

}
