package com.ngdroidapp;

        import android.app.Activity;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Rect;
        import android.media.AudioManager;
        import android.media.MediaExtractor;
        import android.media.MediaPlayer;
        import android.media.SoundPool;
        import android.widget.Toast;


        import com.mycompany.myngdroidapp.GameActivity;
        import com.mycompany.myngdroidapp.R;

        import java.util.Random;
        import java.util.logging.Handler;

        import istanbul.gamelab.ngdroid.base.BaseCanvas;
        import istanbul.gamelab.ngdroid.core.MainThread;
        import istanbul.gamelab.ngdroid.core.NgMediaPlayer;
        import istanbul.gamelab.ngdroid.util.Log;
        import istanbul.gamelab.ngdroid.util.Utils;

/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */


public class GameCanvas extends BaseCanvas {
    private Bitmap tileset, spritesheet, rockset, button, bullet, enemy, reloadbutton, explosion;//Zemin resmimiz,Kovboyumuz,Butonumuz
    private Rect tilesource, tiledestination, spritesource, spritedestination, rocksource, rockdestination, buttonsource, buttondestination,
            bulletsource, bulletdestination, enemysource, enemydestination,reloadsource, reloaddestination, explosionsource, explosiondestination;
    //Random metodu
    Random newRandomx ,newRandomy;
    private int randomx,randomy;

    //Ateş Efekti
    private MediaPlayer fxshot,fxfire,fxlose;

    //explosionsource genişlik, yükseklik, explosiondestination, genişlik, yükseklik
    private int explosionsrcw, explosionsrch, explosiondstw, explosiondsth;
    private int explosionsrcx, explosionsrcy, explosiondstx, explosiondsty;

    //enemy source genişlik yükseklik, enemydestination,genişlik,yükseklik
    private int enemysrcw, enemysrch, enemydstw, enemydsth;
    private int enemysrcx, enemysrcy, enemydstx, enemydsty;
    //enemy hız x/y enemy işaret x/y
    private int enemyvx, enemyvy, enemyix, enemyiy;
    //reload button genişlik yükseklik reload destination genişlik yükseklik
    private int reloadsrcw, reloadsrch, reloaddstw, reloaddsth;
    private int reloadsrcx, reloadsrcy, reloaddstx, reloaddsty;

    //tilesource genişlik,yükseklik, tiledestination,genişlik,yükseklik
    private int tilesrcw, tilesrch, tiledstw, tiledsth;
    private int tilesrcx, tilesrcy, tiledstx, tiledsty;
    //button source genişlik yükseklik,buttondestination,genişlik,yükseklik
    private int buttonsrcw, buttonsrch, buttondstw, buttondsth;
    private int buttonsrcx, buttonsrcy, buttondstx, buttondsty;
    //bulletsource genişlik,yükseklik, bulletdestination genişlik,yükseklik
    private int bulletsrcw, bulletsrch, bulletdstw, bulletdsth;
    private int bulletsrcx, bulletsrcy, bulletdstx, bulletdsty;
    //bullet hız x/y , işaret x/y
    private int bulletvx, bulletvy, bulletix, bulletiy;

    //spritesource genişlik,yükseklik, spritedestination,genişlik,yükseklik
    private int spritesrcw, spritesrch, spritedstw, spritedsth;
    private int spritesrcx, spritesrcy, spritedstx, spritedsty;
    //sprite hız x/y, sprite işaret x/y
    private int spritevx, spritevy, spriteix, spriteiy;
    //rocksource genişlik,yükseklik, rockdestination,genişlik,yükseklik
    private int rocksrcw, rocksrch, rockdstw, rockdsth;
    private int rocksrcx, rocksrcy, rockdstx, rockdsty;

    //ekrana dokunmaya başladığımız noktanın x/y koordinatları
    private int touchdownx, touchdowny;
    //Atak x atak y koordinatları
    private int attackx, attacky;
    //Yön belirteci
    private int yon, shotcontrol;

    //Explosion kare numarası x karenumarası y
    private int explosionframenumx,explosionframenumy;

    //Animasyon türleri:durma,yürüme,silah doğrultma,ateş etme
    private int animationtypes = 4;

    //cowboy.png'deki kare numarası,animasyon türleri 0,1,2,3,karenumarası alt ve üst sınır.
    private int framenum, animationtype, animationfirstfremenum[], animationlastframenum[];
    //ekrana do

    //ateş kontrolü
    public boolean bulletcontrol, enemycontrol, spritesheetcontrol, reloadcontrol,explosioncontrol;
    //DrawText için kullanılıyor.
    private Paint paint;

    public GameCanvas(NgApp ngApp) {
        super(ngApp);
    }


    public void setup() {
        Log.i(TAG, "setup");
        setupTile();
        setupEnemy();
        setupSpriteSheet();
        setupAnimation();
        setupBullet();
        setupReloadButton();
        setupText();
        setupExplosion();
        setupSound();
    }
    
    public void setupExplosion(){
        explosion = Utils.loadImage(root,"explosion2.png");
        explosionsource = new Rect();
        explosiondestination = new Rect();
        explosioncontrol = false;
        explosionsrcx = 0;
        explosionsrcy = 0;
        explosionsrcw = 128;
        explosionsrch = 128;

        explosiondstx = enemydstx;
        explosiondsty = enemydsty;
        explosiondstw = 128;
        explosiondsth = 128;


    }
    public void setupSound(){
        //public SoundPool (int maxStreams, int streamType, int srcQuality)
        fxshot = MediaPlayer.create(root.activity, R.raw.fxhostcartoon);
        fxfire = MediaPlayer.create(root.activity, R.raw.fxfoom);
        fxlose = MediaPlayer.create(root.activity, R.raw.fxlose2);
    }
    public void playShotSound(){
        fxshot.start();
    }
    public void playFireSound(){
        fxfire.start();
        fxfire.setVolume(1.0f,1.0f);
    }
    public void playLoseSound(){fxlose.start();}
    public void setupText(){
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(32);
    }
    public void setupReloadButton(){
        
        reloadbutton = Utils.loadImage(root,"reload.png");
        reloadsource = new Rect();
        reloaddestination = new Rect();
        reloadcontrol = false;

        reloadsrcx = 0;
        reloadsrcy = 0;
        reloadsrcw = reloadbutton.getWidth();
        reloadsrch = reloadbutton.getHeight();

        reloaddstw = 196;
        reloaddsth = 196;

        reloaddstx = getWidth() / 2 - reloaddstw / 2;
        reloaddsty = getHeight() -reloaddsth -32;

    }
    public void setupTile() {
        tileset = Utils.loadImage(root, "tilea2.png");
        tilesource = new Rect();
        tiledestination = new Rect();
        //buttonset
        button = Utils.loadImage(root, "buttonpush.png");
        buttonsource = new Rect();
        buttondestination = new Rect();
        buttonsrcx = 0;
        buttonsrcy = 0;
        buttonsrcw = button.getWidth() / 2;
        buttonsrch = button.getHeight();
        buttondstw = getWidth() / 6;
        buttondsth = getWidth() / 6;

        buttondstx = getWidth() - buttondstw - 32;
        buttondsty = getHeight() - buttondstw - 32;
        //Tilea2
        tilesrcw = 64;
        tilesrch = 64;
        tilesrcx = 0;
        tilesrcy = 0;
        tiledstw = 128;
        tiledsth = 128;
        tiledstx = 0;
        tiledsty = 0;
        //Rock Destination
        rockset = Utils.loadImage(root, "rock02.png");
        rocksource = new Rect();
        rockdestination = new Rect();

        //rockset
        rocksrcx = 0;
        rocksrcy = 0;
        rocksrcw = rockset.getWidth();
        rocksrch = rockset.getHeight();
        rockdstw = 128;
        rockdsth = 128;
        rockdstx = getWidth() / 2 - rockdstw / 2;
        rockdsty = getHeight() / 2 - rockdsth / 2;


    }

    public void setupSpriteSheet() {
        spritesheet = Utils.loadImage(root, "cowboy.png");
        spritesource = new Rect();
        spritedestination = new Rect();
        spritesheetcontrol = true;
        //Spritesheet
        spritedstx = 0;
        spritedsty = 0;
        spritesrcx = 0;
        spritesrcy = 0;
        spritesrcw = 128;
        spritesrch = 128;
        spritedstw = 256;
        spritedsth = 256;
        //sprite hız ve işaret
        spritevx = spritedstw / 16;
        spritevy = spritedsth / 16;

        spriteix = 1;
        spriteiy = 0;

    }

    public void setupBullet() {
        bullet = Utils.loadImage(root, "bullet.png");
        bulletsource = new Rect();
        bulletdestination = new Rect();
        bulletsrcx = 0;
        bulletsrcy = 0;
        bulletsrcw = 32;
        bulletsrch = 32;

        bulletvx = 0;
        bulletix = 0;
        bulletvy = 0;
        bulletvy = 0;

        bulletdstx = 0;
        bulletdsty = 0;
        bulletdstw = 16;
        bulletdsth = 16;
        bulletcontrol = false;
    }

    public void setupAnimation() {
        yon = 3;
        shotcontrol = 0;
        //Cowboy.png de soldan sağa kaçıncı karede olduğumuz gösterir 0,1,2,....12
        framenum = 0;
        //Animasyon türü: durma=0,yürüme=1,silah doğrultma=2,ateş etme =3
        animationtype = 1;

        //animasyon türleri için alt/üst sınır dizisi
        //kare numarası(frameno) bu sınırlar arasında arttıracağız
        animationfirstfremenum = new int[animationtypes];
        animationlastframenum = new int[animationtypes];

        //durma (0) animasyonu 0.karede başlar & biter.
        animationfirstfremenum[0] = 0;
        animationlastframenum[0] = 0;
        //yürüme(1) animasyonu 1.karede başlar, 8.karede biter.
        animationfirstfremenum[1] = 1;
        animationlastframenum[1] = 8;

        //silah çekme(2) animasyonu 9.karede başlar, 11.karede biter.
        animationfirstfremenum[2] = 9;
        animationlastframenum[2] = 11;
        //ateş etme animasyonu 12.karede başlar,13.karede biter.
        animationfirstfremenum[3] = 11;
        animationlastframenum[3] = 13;

    }

    public void setupEnemy() {
        //Enemy Set

        //Random
        newRandomx=new Random();
        newRandomy=new Random();
        randomx = newRandomx.nextInt(getWidth() - enemydstw);
        randomy = newRandomy.nextInt(getHeight() - enemydsth);
        //Atak Koordinatları
        attackx = 0;
        attacky = 0;
        enemycontrol = true;
        enemy = Utils.loadImage(root, "enemyufo.png");
        enemysource = new Rect();
        enemydestination = new Rect();

        enemysrcx = 0;
        enemysrcw = enemy.getWidth();
        enemysrcy = 0;
        enemysrch = enemy.getHeight();

        enemydstw = 128;
        enemydsth = 128;
        enemydstx = getWidth() / 2 - (enemydstw / 2);
        enemydsty = getHeight() / 2 + (enemydsth);

        enemyvx = enemydstw / 8;
        enemyvy = enemydsth / 8;

        enemyix = 1;
        enemyiy = 0;

    }

    public void shoot() {
        //bullet yönetimi
        bulletcontrol = true;

        bulletvx = 160;
        bulletvy = 160;
        switch (yon) {
            case 3:
                bulletix = 1;
                bulletiy = 0;
                bulletdstx = spritedstx + (spritedstw / 2);
                bulletdsty = spritedsty + (spritedsth / 2);
                break;
            case 7:
                bulletix = -1;
                bulletiy = 0;
                bulletdstx = spritedstx + (spritedstw / 2);
                bulletdsty = spritedsty + (spritedsth / 2);
                break;
            case 9:
                bulletix = 0;
                bulletiy = 1;
                bulletdstx = spritedstx + (spritedstw / 4);
                bulletdsty = spritedsty + (spritedsth);
                break;
            case 5:
                bulletix = 0;
                bulletiy = -1;
                bulletdstx = spritedstx + (spritedstw / 2);
                bulletdsty = spritedsty + (spritedsth);
                break;
        }

    }
    public void EnemyWalk(){
        //Karakterimiz düşmanın 128 birimlik alanında olup olmadığını kontrol ettik

        if(!(Utils.checkCollision(enemydstx - 256,enemydsty - 256, enemydstx + enemydstw + 256 , enemydsty + enemydsth + 256,
                spritedstx, spritedsty, spritedstx + spritedstw, spritedsty +spritedsth ))){
            if(enemydsty > randomy + enemydstw / 2 || enemydsty < randomy - enemydstw / 2 ){
                enemyix = 0;
                enemyiy = (randomy - enemydsty) / Math.abs((randomy - enemydsty)); //+1 veya -1 döndürür buda düşmanın y ekseninde ki ivmesini belirler

            }else if(enemydstx >  randomx + enemydstw || enemydstx < randomx - enemydstw){
                enemyix = (randomx - enemydstx) / Math.abs((randomx - enemydstx));
                enemyiy = 0;
            }
            else {
                randomx = newRandomx.nextInt(getWidth() - enemydstw);
                randomy = newRandomy.nextInt(getHeight() - enemydsth);
            }
        }
        else{
            EnemyAttack();
        }

    }
    public void EnemyAttack(){
       if(spritesheetcontrol) {
           attackx = spritedstx + spritedstw / 2;
           attacky = spritedsty + spritedsth / 2;
           if(attacky < attackx){
               if (enemydsty < spritedsty + spritedsth / 2) {
                   enemyiy = 1;
                   enemyix = 0;
               }else if (enemydsty > spritedsty + spritedstw / 2) {
                   enemyiy = -1;
                   enemyix = 0;
               }else if (enemydstx < spritedstx + spritedstw / 2) {
                   enemyix = 1;
                   enemyiy = 0;
               }else if (enemydstx > spritedstx + spritedstw / 2) {
                   enemyix = -1;
                   enemyiy = 0;
               }
           }else {
               if (enemydstx < spritedstx + spritedstw / 2) {
                   enemyix = 1;
                   enemyiy = 0;
               }else if (enemydstx > spritedstx + spritedstw / 2) {
                   enemyix = -1;
                   enemyiy = 0;
               }else if (enemydsty < spritedsty + spritedsth / 2) {
                   enemyiy = 1;
                   enemyix = 0;
               }else if (enemydsty > spritedsty + spritedstw / 2) {
                   enemyiy = -1;
                   enemyix = 0;
               }
           }

       }
    }
    public void update() {
        bulletdstx += bulletvx * bulletix;
        bulletdsty += bulletvy * bulletiy;

        enemydstx += enemyvx * enemyix;
        enemydsty += enemyvy * enemyiy;

        spritedstx += spritevx * spriteix;
        spritedsty += spritevy * spriteiy;
        framenum++;
        explosionframenumx++;
        if (explosionframenumx > 4){
            explosionframenumx = 0;
            explosionframenumy++;
        if(explosionframenumy > 4) {
            explosionframenumy = 0;
            explosioncontrol = false;
            }
        }
        if(enemycontrol) {
            EnemyWalk();
        }else {
            enemyix = 0;
            enemyiy = 0;
        }
        if (bulletcontrol) {
            if(Utils.checkCollision(bulletdestination,rockdestination)){
                bulletcontrol = false;
            }else if (Utils.checkCollision(bulletdestination, enemydestination)) {
                    if(enemycontrol) {
                        bulletcontrol = false;
                        enemycontrol = false;
                        reloadcontrol = true;
                        enemyix = 0;
                        explosionframenumx = 0;
                        explosionframenumy = 0;
                        explosioncontrol = true;
                        playFireSound();
                    }
            }else if (bulletdstx > getWidth() + 64 || bulletdstx < -96 || bulletdsty > getHeight() + 64 || bulletdsty < -96) {
                bulletcontrol = false;
                Log.i("Control", "" + bulletdstx);
            }

        }
        if (framenum > animationlastframenum[animationtype]) {
            framenum = animationfirstfremenum[animationtype];
            if (shotcontrol == 1) {
                animationtype = 0;
                shotcontrol = 0;
            }
        }

        if (enemydstx > getWidth() - enemydstw){
            enemyix = -1;
            randomx = newRandomx.nextInt(getWidth() - enemydstw);
        }else if(enemydstx < 0) {
            enemyix = 1;
        }else if(enemydsty > getHeight() - spritedsth){
            enemyiy = -1;
            randomy = newRandomy.nextInt(getHeight() - enemydsth);

        }else if(enemydsty < 0){
            enemyiy = 1;
        }
        if (spritedstx > getWidth() - spritedstw) {
            spritedstx = getWidth() - spritedstw;
            animationtype = 0;
        } else if (spritedstx < 0) {
            //sol duvarda durur
            spritedstx = 0;
            animationtype = 0;
        } else if (spritedsty > getHeight() - spritedsth) {
            spritedsty = getHeight() - spritedsth;
            animationtype = 0;
        } else if (spritedsty < 0) {
            //üst duvarda durur
            spritedsty = 0;
            animationtype = 0;
        }

        if (Utils.checkCollision(spritedestination, rockdestination)) {
            animationtype = 0;
            spritedstx -= spritevx * spriteix * 2;
            spritedsty -= spritevy * spriteiy * 2;
            spritevy = 0;
            spritevx = 0;
            Log.i(TAG, "Collusion Detected Rock/SpriteSheet");
        }
        if(enemycontrol){
        if (Utils.checkCollision(spritedestination, enemydestination)) {
            animationtype = 0;
            spritedstx -= spritevx * spriteix * 2;
            spritedsty -= spritevy * spriteiy * 2;
            spritevy = 0;
            spritevx = 0;
            spritesheetcontrol = false;
            reloadcontrol = true;
            playLoseSound();
            randomx = newRandomx.nextInt(getWidth() - enemydstw);
            randomy = newRandomy.nextInt(getHeight() - enemydsth);
            Log.i(TAG, "Collusion Detected Enemy/SpriteSheet");
        }
        }

        //cowboy.png'deki animasyon karesinin koordinatını framenum ve genişlik cinsinden girelim
        spritesrcx = framenum * spritesrcw;
        spritesrcy = yon * spritesrch;



        explosionsrcx = explosionframenumx * explosionsrcw;
        explosionsrcy = explosionframenumy * explosionsrch;
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < getWidth(); i += tiledstw){
            for(int j = 0; j < getHeight(); j += tiledsth){
                tilesource.set(tilesrcx, tilesrcy ,tilesrcx + tilesrcw, tilesrcy + tilesrch);
                tiledestination.set(i, j, i + tiledstw, j+tiledsth);
                canvas.drawBitmap(tileset, tilesource, tiledestination,null);

            }
        }

        canvas.drawText("Cowboy x: " + spritedsty + spritedsth / 2, 10, 48, paint);
        canvas.drawText("Cowboy y: " + spritedstx + spritedstw / 2,10, 80,paint);
        if (enemycontrol) {
            enemysource.set(enemysrcx, enemysrcy, enemysrcx + enemysrcw, enemysrcy + enemysrch);
            enemydestination.set(enemydstx, enemydsty, enemydstx + enemydstw, enemydsty + enemydsth);
            canvas.drawBitmap(enemy, enemysource, enemydestination, null);
        }
        if(explosioncontrol){
                explosionsource.set(explosionsrcx, explosionsrcy, explosionsrcw + explosionsrcx, explosionsrcy + explosionsrch);
                explosiondestination.set(enemydstx - enemydstw / 2, enemydsty - enemydsth / 2, enemydstx + explosiondstw, enemydsty + explosiondsth);
                canvas.drawBitmap(explosion, explosionsource, explosiondestination, null);
                canvas.drawText("Explosion Detected ",10, 112,paint);
        }
        if(spritesheetcontrol) {
            spritesource.set(spritesrcx, spritesrcy, spritesrcx + spritesrcw, spritesrcy + spritesrch);
            spritedestination.set(spritedstx, spritedsty, spritedstx + spritedstw, spritedsty + spritedsth);
            canvas.drawBitmap(spritesheet, spritesource, spritedestination, null);
        }

        rocksource.set(rocksrcx, rocksrcy, rocksrcx + rocksrcw, rocksrcy+rocksrch);
        rockdestination.set(rockdstx, rockdsty, rockdstx+rockdstw, rockdsty+rockdsth);
        canvas.drawBitmap(rockset, rocksource, rockdestination,null);

        buttonsource.set(buttonsrcx, buttonsrcy, buttonsrcx + buttonsrcw, buttonsrch);
        buttondestination.set(buttondstx, buttondsty, buttondstx+buttondstw, buttondsty + buttondsth);
        canvas.drawBitmap(button, buttonsource, buttondestination,null);

        if (reloadcontrol){
            reloadsource.set(reloadsrcx, reloadsrcy, reloadsrcx + reloadsrcw, reloadsrcy + reloadsrch);
            reloaddestination.set(reloaddstx, reloaddsty, reloaddstx + reloaddstw, reloaddsty + reloaddsth);
            canvas.drawBitmap(reloadbutton, reloadsource, reloaddestination, null);
            if(!explosioncontrol)canvas.drawText("Reload",10, 112,paint);
            else canvas.drawText("Reload ",10, 144,paint);

        }

        if(bulletcontrol){
          //  Log.i("Control","Cizdiriliyor Kurşun" + bulletdstx);
            bulletsource.set(bulletsrcx, bulletsrcy, bulletsrcx + bulletsrcw, bulletsrcy + bulletsrch);
            bulletdestination.set(bulletdstx, bulletdsty, bulletdstx + bulletdstw, bulletdsty + bulletdsth);
            canvas.drawBitmap(bullet, bulletsource, bulletdestination,null);
        }

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
    /**
     *
     * @param x
     * @param y
     * @param id
     */
    public void touchDown(int x, int y, int id) {
        touchdownx = x;
        touchdowny = y;
        if(touchdownx > getWidth() - buttondstw - 32 && touchdowny > getHeight() - buttondsth - 32 && touchdownx < getWidth() - 32 &&
                touchdowny < getHeight() - 32){
            buttonsrcx = button.getWidth() / 2;
        }


    }

    public void touchMove(int x, int y, int id) {
        if(x > getWidth() - buttondstw - 32 && y > getHeight() - buttondsth - 32 && x < getWidth() - 32 && y < getHeight() - 32){
            buttonsrcx = button.getWidth() / 2;
        }else {
            buttonsrcx = 0;

        }
    }

    public void touchUp(int x, int y, int id) {
        int xfarki = x - touchdownx;
        int yfarki = y - touchdowny;

        if(Math.abs(xfarki) < buttondstw/2 && Math.abs(yfarki) < buttondstw/2){
            if(x > getWidth() - buttondstw - 32 && y > getHeight() - buttondsth - 32 && x < getWidth() - 32 && y < getHeight() - 32){
                shotcontrol = 1;
                animationtype = 3;
                spritevy = 0;
                spritevx = 0;
                framenum = 11;
                buttonsrcx = 0;
                if(!bulletcontrol){
                    shoot();
                    playShotSound();
                }
            }
            else if(x > getWidth()/2 - reloaddstw / 2 && y > getHeight() - 32 - reloaddsth && x < getWidth() / 2 +reloaddstw / 2 && y < getHeight() -32 ){
                Log.i("Reload Control","Reload Clicked");
                if(!enemycontrol){
                    enemycontrol = true;
                    reloadcontrol = false;
                    enemyix = 1;
                    explosioncontrol = false;
                }
                if(!spritesheetcontrol){
                    spritesheetcontrol=true;
                    spritedstx = 0;
                    spritedsty = 0;
                    spriteix = 0;
                    spriteiy = 0;
                    reloadcontrol = false;
                }
            }
        }else {
        //x farki büyükse sağa/sola, y farki büyükse yukarı/aşağı gitsin
        if (Math.abs(xfarki) >= Math.abs(yfarki)){
            spritevx=spritedstw / 16;

            spriteix = 1;
            yon = 3;
            animationtype = 1;
            if(xfarki < 0){
                spriteix =- 1;
                yon = 7;
            }
            spriteiy = 0;
        }else {
            buttonsrcx = 0;
            spritevy = spritedsth / 16;
            spriteiy = 1;
            yon = 9;
            animationtype = 1;
            if (yfarki < 0){
                spriteiy =- 1;
                yon = 5;
            }
            spriteix = 0;
        }
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
