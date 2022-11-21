package com.zetcode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;


public class Board extends JPanel {

    private final int OFFSET = 30;
    private final int SPACE = 20;
    private final int LEFT_COLLISION = 1;
    private final int RIGHT_COLLISION = 2;
    private final int TOP_COLLISION = 3;
    private final int BOTTOM_COLLISION = 4;
    private       int CONTADOR_PODERES = 0;
    private       int CONTADOR_PODERES_2 = 0;

    private       int CONTADOR_MOVIMIENTOS = 0;
    private final int TECLA_T = 5;
    private final int TECLA_F = 6;
    private final int TECLA_H = 7; 
    private final int TECLA_G = 8;

    private ArrayList<Wall> walls;
    private ArrayList<Baggage> baggs;
    private ArrayList<Area> areas;


    private boolean suma1 = true;
    private boolean suma2 = true;
    private boolean suma3 = true;
    private boolean suma4 = true;
    private boolean suma5 = true;
    private boolean suma6 = true;
    
    private Player soko;
    private int w = 0;
    private int h = 0;
    
    private boolean isCompleted = false;

    mapas mapa              = new mapas();
    basesDatos pedirNombre  = new basesDatos();
    basesDatos insertarData = new basesDatos();
    
    private String level    = mapa.escogerNuevoMapa( pedirNombre.pedirEnConsola());
  
    
    public Board() {

        initBoard();

    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        initWorld();

    }

    public int getBoardWidth() {

        return this.w;

    }

    public int getBoardHeight() {

        return this.h;

    }

    private void initWorld() {
        
        walls = new ArrayList<>();
        baggs = new ArrayList<>();
        areas = new ArrayList<>();

        int x = OFFSET;
        int y = OFFSET;

        Wall wall;
        Baggage b;
        Area a;

        for (int i = 0; i < level.length(); i++) {

            char item = level.charAt(i);

            switch (item) {

                case '\n':
                    y += SPACE;

                    if (this.w < x) {
                        this.w = x;
                    }

                    x = OFFSET;
                    break;

                case '#':
                    wall = new Wall(x, y);
                    walls.add(wall);
                    x += SPACE;
                    break;

                case '$':
                    b = new Baggage(x, y);
                    baggs.add(b);
                    x += SPACE;
                    break;

                case '.':
                    a = new Area(x, y);
                    areas.add(a);
                    x += SPACE;
                    break;

                case '@':
                    soko = new Player(x, y);
                    x += SPACE;
                    break;

                case ' ':
                    x += SPACE;
                    break;

                default:
                    break;
            }

            h = y;
        }
    }

    private void buildWorld(Graphics g) {

        g.setColor(new Color(250, 240, 170));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        ArrayList<Actor> world = new ArrayList<>();

        world.addAll(walls);
        world.addAll(areas);
        world.addAll(baggs);
        world.add(soko);

        for (int i = 0; i < world.size(); i++) {

            Actor item = world.get(i);

            if (item instanceof Player || item instanceof Baggage) {
                
                g.drawImage(item.getImage(), item.x() + 2, item.y() + 2, this);

            } else {
                
                g.drawImage(item.getImage(), item.x(), item.y(), this);

            }

            if (isCompleted) {
                
                g.setColor(new Color(0, 0, 0));
                g.drawString("Completed", 25, 20);
                

            }

        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        buildWorld(g);
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            restarPuntaje();

            if (isCompleted) {

                return;

            }
            
            

            int key = e.getKeyCode();

            switch (key) {
                
                case KeyEvent.VK_LEFT:
                    
                    if (checkWallCollision(soko,    LEFT_COLLISION)) {
                        return;
                    }
                    
                    if (checkBagCollision(LEFT_COLLISION)) {
                        return;
                    }
                    
                    soko.move(-SPACE, 0);
                    
                    break;
                    
                case KeyEvent.VK_RIGHT:
                    
                    if (checkWallCollision(soko, RIGHT_COLLISION)) {
                        return;
                    }
                    
                    if (checkBagCollision(RIGHT_COLLISION)) {
                        return;
                    }
                    
                    soko.move(SPACE, 0);
                    
                    break;
                    
                case KeyEvent.VK_UP:
                    
                    if (checkWallCollision(soko, TOP_COLLISION)) {
                        return;
                    }
                    
                    if (checkBagCollision(TOP_COLLISION)) {
                        return;
                    }
                    
                    soko.move(0, -SPACE);
                    
                    break;
                    
                case KeyEvent.VK_DOWN:
                    
                    if (checkWallCollision(soko, BOTTOM_COLLISION)) {
                        return;
                    }
                    
                    if (checkBagCollision(BOTTOM_COLLISION)) {
                        return;
                    }
                    
                    soko.move(0, SPACE);
                    
                    break;

                case KeyEvent.VK_W:

                    if( checkBagCollisionPower( TOP_COLLISION )){

                        return;
                    }
                    
                break;


                case KeyEvent.VK_A:

                    if( checkBagCollisionPower( LEFT_COLLISION)){

                        return;
                    }
                    
                break;


                case KeyEvent.VK_S:

                    if( checkBagCollisionPower( BOTTOM_COLLISION )){

                        return;
                    }
                    
                break;

                case KeyEvent.VK_D:

                    if( checkBagCollisionPower( RIGHT_COLLISION )){

                        return;
                    }
                    
                break;

                case KeyEvent.VK_T:
                
                    saltarSobrePelotaPoder( TECLA_T );
            
                break;

                case KeyEvent.VK_G:
                    
                    saltarSobrePelotaPoder( TECLA_G );
            
                break;
                    
                case KeyEvent.VK_H:
                    
                    saltarSobrePelotaPoder( TECLA_H );
            
                break;

                case KeyEvent.VK_F:
                    
                    saltarSobrePelotaPoder( TECLA_F );
            
                break;

                case KeyEvent.VK_R:
                    
                    restartLevel();
                    
                    break;
                    
                default:
                    break;
            }
         
            repaint();
        }
    }

    private boolean checkWallCollision(Actor actor, int type) {

        switch (type) {
            
            case LEFT_COLLISION:
                
                
                for (int i = 0; i < walls.size(); i++) {
                    
                    Wall wall = walls.get(i);
                    
                    if (actor.isLeftCollision(wall)) {
                        
                        return true;
                    }
                }
                
                return false;
                
            case RIGHT_COLLISION:
                
                for (int i = 0; i < walls.size(); i++) {
                    
                    Wall wall = walls.get(i);
                    
                    if (actor.isRightCollision(wall)) {
                        return true;
                    }
                }
                
                return false;
                
            case TOP_COLLISION:
                
                for (int i = 0; i < walls.size(); i++) {
                    
                    Wall wall = walls.get(i);
                    
                    if (actor.isTopCollision(wall)) {
                        
                        return true;
                    }
                }
                
                return false;
                
            case BOTTOM_COLLISION:
                
                for (int i = 0; i < walls.size(); i++) {
                    
                    Wall wall = walls.get(i);
                    
                    if (actor.isBottomCollision(wall)) {
                        
                        return true;
                    }
                }
                
                return false;
                
            default:
                break;
        }
        
        return false;
    }

    private boolean checkBagCollision(int type ) {

        switch (type) {
            
            case LEFT_COLLISION:
                
                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);

                    if (soko.isLeftCollision(bag)) {

                        for (int j = 0; j < baggs.size(); j++) {
                            
                            Baggage item = baggs.get(j);
                            
                            if (!bag.equals(item)) {
                                
                                if (bag.isLeftCollision(item)) {
                                    return true;
                                }
                            }
                            
                            if (checkWallCollision(bag, LEFT_COLLISION)) {
                                return true;
                            }
                        }
                        
                        bag.move(-SPACE, 0);
                        isCompleted();
                    }
                }
                
                return false;
                
            case RIGHT_COLLISION:
                
                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);
                    
                    if (soko.isRightCollision(bag)) {
                        
                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j);
                            
                            if (!bag.equals(item)) {
                                
                                if (bag.isRightCollision(item)) {
                                    return true;
                                }
                            }
                            
                            if (checkWallCollision(bag, RIGHT_COLLISION)) {
                                return true;
                            }
                        }
                        
                        bag.move(SPACE, 0);
                        isCompleted();
                    }
                }
                return false;
                
            case TOP_COLLISION:
                
                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);
                    
                    if (soko.isTopCollision(bag)) {
                        
                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j);

                            if (!bag.equals(item)) {
                                
                                if (bag.isTopCollision(item)) {
                                    return true;
                                }
                            }
                            
                            if (checkWallCollision(bag, TOP_COLLISION)) {
                                return true;
                            }
                        }
                        
                        bag.move(0, -SPACE);
                        isCompleted();
                    }
                }

                return false;
                
            case BOTTOM_COLLISION:
                
                for (int i = 0; i < baggs.size(); i++) {

                    Baggage bag = baggs.get(i);
                    
                    if (soko.isBottomCollision(bag)) {
                        
                        for (int j = 0; j < baggs.size(); j++) {

                            Baggage item = baggs.get(j);
                            
                            if (!bag.equals(item)) {
                                
                                if (bag.isBottomCollision(item)) {
                                    return true;
                                }
                            }
                            
                            if (checkWallCollision(bag,BOTTOM_COLLISION)) {
                                
                                return true;
                            }
                        }
                        
                        bag.move(0, SPACE);
                        isCompleted();
                    }
                }
                
                break;
                
            default:
                break;
        }

        return false;
    }

    public void isCompleted() {

        
        int nOfBags = baggs.size();
        int finishedBags = 0;

        for (int i = 0; i < nOfBags; i++) {
            
            Baggage bag = baggs.get(i);
            
            for (int j = 0; j < nOfBags; j++) {
                
                Area area =  areas.get(j);
                
                if (bag.x() == area.x() && bag.y() == area.y()) {
                
                    finishedBags += 1;

                    if( finishedBags == 1 && suma1 ){
                        sumarPuntaje();
                        suma1 = false;
                    }

                    else if( finishedBags == 2 && suma2 ){
                        sumarPuntaje();
                        suma2 = false;
                    }

                    else if( finishedBags == 3 && suma3 ){
                        sumarPuntaje();
                        suma3 = false;
                    }

                    else if( finishedBags == 4 && suma4 ){
                        sumarPuntaje();
                        suma4 = false;
                    }
                   
                    else if( finishedBags == 5 && suma5 ){
                        suma5 = false;
                        sumarPuntaje();
                    }

                    else if( finishedBags == 6 && suma6 ){
                        sumarPuntaje();
                        suma6 = false;
                    }
                }
            }
        }

        if (finishedBags == nOfBags) {
            
            isCompleted = true;

            File cancion_completed = new File("JIJIJA-sonido.wav");

                try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(cancion_completed)) {
    
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
    
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    
                    e.printStackTrace();
                }

            repaint();

        }
    }

    private void restartLevel() {

        CONTADOR_PODERES = 0;
        CONTADOR_PODERES_2 = 0;
        areas.clear();
        baggs.clear();
        walls.clear();

        initWorld();

        if (isCompleted) {
            isCompleted = false;
        }
    }

//Codigo de jurgen


private boolean checkBagCollisionPower(int type) {

if( CONTADOR_PODERES < 2 ){

    switch (type) {
        
        case LEFT_COLLISION:

            CONTADOR_PODERES = CONTADOR_PODERES + 1;

            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = baggs.get(i);

                if (soko.isLeftCollision(bag)) {

                    for (int j = 0; j < baggs.size(); j++) {
                        
                        Baggage item = baggs.get(j);
                        
                        if (!bag.equals(item)) {
                            
                            if (bag.isLeftCollision(item)) {
                                return true;
                            }
                        }
                        
                        if (checkWallCollision(bag, LEFT_COLLISION)) {
                            return true;
                        }
                    }
                    
                    bag.move(-SPACE - 20, 0);
                    isCompleted();
                }
            }
            
            return false;
            
        case RIGHT_COLLISION:
            
            CONTADOR_PODERES = CONTADOR_PODERES + 1;

            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = baggs.get(i);
                
                if (soko.isRightCollision(bag)) {
                    
                    for (int j = 0; j < baggs.size(); j++) {

                        Baggage item = baggs.get(j);
                        
                        if (!bag.equals(item)) {
                            
                            if (bag.isRightCollision(item)) {
                                return true;
                            }
                        }
                        
                        if (checkWallCollision(bag, RIGHT_COLLISION)) {
                            return true;
                        }
                    }
                    
                    bag.move(SPACE + 20, 0);
                    isCompleted();
                }
            }
            return false;
            
        case TOP_COLLISION:

            CONTADOR_PODERES = CONTADOR_PODERES + 1;
            
            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = baggs.get(i);
                
                if (soko.isTopCollision(bag)) {
                    
                    for (int j = 0; j < baggs.size(); j++) {

                        Baggage item = baggs.get(j);

                        if (!bag.equals(item)) {
                            
                            if (bag.isTopCollision(item)) {
                                return true;
                            }
                        }
                        
                        if (checkWallCollision(bag, TOP_COLLISION)) {
                            return true;
                        }
                    }
                    
                    bag.move(0, -SPACE - 20);
                    isCompleted();
                }
            }

            return false;
            
        case BOTTOM_COLLISION:

            CONTADOR_PODERES = CONTADOR_PODERES + 1;
            
            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = baggs.get(i);
                
                if (soko.isBottomCollision(bag)) {
                    
                    for (int j = 0; j < baggs.size(); j++) {

                        Baggage item = baggs.get(j);
                        
                        if (!bag.equals(item) ) {
                            
                            if (bag.isBottomCollision(item)) {
                                return true;
                            }
                        }
                        
                        if (checkWallCollision(bag,BOTTOM_COLLISION)) {
                            
                            return true;
                        }
                    }
                    
                    bag.move(0, SPACE + 20);
                    isCompleted();
                }
            }
            
            break;
            
        default:
            break;         
    }
    return false;
}
    return false;   
}



    public void saltarSobrePelotaPoder( int type ){


        if( CONTADOR_PODERES_2 < 2 ){

            CONTADOR_PODERES_2 ++;

            switch ( type ) {
                    
                case TECLA_F:
                    
                    if (checkWallCollision(soko,    LEFT_COLLISION)) {
                        return;

                    }else soko.move(-SPACE - 20, 0);
                    
                    break;
                    
                case TECLA_H:
                    
                    if (checkWallCollision(soko, RIGHT_COLLISION)) {
                        return;

                    }else soko.move(SPACE + 20, 0);   
                    
                    break;
                    
                case TECLA_T:
                    
                    if (checkWallCollision(soko, TOP_COLLISION)) {
                        return;
                    

                    }else soko.move(0, -SPACE - 20);
                    
                    
                    
                    break;
                    
                case TECLA_G:
                    
                    if (checkWallCollision(soko, BOTTOM_COLLISION)) {
                        return;

                    }else  soko.move(0, SPACE + 20);
                    
                    break;
                    
                default:
                    break;
            }
        repaint();

        }
    }

    // CODIGO MILO ( lo voy a anular por ahora By Jurgen )

   /* public void movimientosUsuario ( int type) {
        if (CONTADOR_MOVIMIENTOS <= 10) {
            CONTADOR_MOVIMIENTOS++;

            switch ( type ) {
                case KeyEvent.VK_W:

                    if( checkBagCollisionPower( TOP_COLLISION )){
                        CONTADOR_MOVIMIENTOS = CONTADOR_MOVIMIENTOS + 2;
                        return;
                    }
                    
                break;


                case KeyEvent.VK_A:

                    if( checkBagCollisionPower( LEFT_COLLISION)){
                        CONTADOR_MOVIMIENTOS = CONTADOR_MOVIMIENTOS + 2;
                        return;
                    }
                    
                break;


                case KeyEvent.VK_S:

                    if( checkBagCollisionPower( BOTTOM_COLLISION )){
                        CONTADOR_MOVIMIENTOS = CONTADOR_MOVIMIENTOS + 2;
                        return;
                    }
                    
                break;

                case KeyEvent.VK_D:

                    if( checkBagCollisionPower( RIGHT_COLLISION )){
                        CONTADOR_MOVIMIENTOS = CONTADOR_MOVIMIENTOS + 2;
                        return;
                    }
                    
                break;

                default:
                    break;
            }
        }
    }

    */


    public void restarPuntaje(){

        CONTADOR_MOVIMIENTOS++;
    
        if( CONTADOR_MOVIMIENTOS == 40 ){

            basesDatos.puntajes = basesDatos.puntajes - 5;
            System.out.println("40 " + basesDatos.puntajes );
            CONTADOR_MOVIMIENTOS = 0;
            try {
                insertarData.puntajeActualizado();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            CONTADOR_MOVIMIENTOS = 0;
        }
    
    };
    
    
    public void sumarPuntaje(){
        
        
            CONTADOR_MOVIMIENTOS = 0;
            basesDatos.puntajes = basesDatos.puntajes + 30;
            System.out.println("ok " + basesDatos.puntajes );
            try {

                insertarData.puntajeActualizado();
            } catch (Exception e) {

                e.printStackTrace();
            }
  
    };
}

   

