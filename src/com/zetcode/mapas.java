package com.zetcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.sound.sampled.*;

public class mapas {
    
    boolean error = true;
    String mapa = "";
    String mapaSeleccionado = "";
    Scanner recibirData = new Scanner( System.in );
    int seleccion = 0;
    String nombreUsuario = "";


    public mapas(){

        JOptionPane.showMessageDialog(null, "Por favor mirar la consola para seleccionar mapa");
        
    }

    public String escogerNuevoMapa( String nombreUser ){

        this.nombreUsuario = nombreUser;

        while( error ){

            try{

                System.out.println("Hola " + this.nombreUsuario + ", bienvenido al menu de seleccion de mapas: ");
                System.out.print("Hay 3 mapas diferentes , ponga a continuacion cual desea escoger ( opciones: 1 / 2 / 3): ");

        
                mapaSeleccionado = recibirData.nextLine();
                System.out.println( mapaSeleccionado );

                seleccion = Integer.parseInt( mapaSeleccionado ) ;

                if( seleccion == 1 || seleccion == 2 || seleccion == 3){
                    
                    error = false; 

                }else throw new Exception("Por favor ingrese un mapa valido :(" );;

            
            } catch( Exception e){
                System.out.println( e.getMessage() );
                JOptionPane.showMessageDialog(null, e.getMessage());
                System.exit(0);
            }

        }  

        return creandoMapa();

    }


    public String creandoMapa(){

        System.out.println("Recuerde: Con las teclas w/a/s/d puede empujar las bolas 2 casillas");
        System.out.println("Recuerde: Con las teclas t/f/g/h puede mover la pelota roja 2 casillas");
        System.out.println("Hay un limite de 2 para los poderes, recuerde que pueden jugar en su contra( cuidado con quedar en una pared ) :D");
        System.out.println("Abra la ventana del juego");
        mapa = "";

        try {

            RandomAccessFile archivoMapa1 = new RandomAccessFile("mapa1.txt","r");
            RandomAccessFile archivoMapa2 = new RandomAccessFile("mapa2.txt","r");
            RandomAccessFile archivoMapa3 = new RandomAccessFile("mapa3.txt","r");



            if( seleccion == 1){

                String nuevaLinea = archivoMapa1.readLine();

                while( nuevaLinea != null ){
                    mapa += nuevaLinea;
                    mapa += "\n";

                    nuevaLinea = archivoMapa1.readLine();
                }

                //Creando la cancion 
                File cancion_map1 = new File("La Pantera Mambo  Orquesta La 33.wav");

                try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(cancion_map1)) {
    
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
    
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    
                    e.printStackTrace();
                }
                
                this.error = false;
                return mapa;

            }else if( seleccion == 2){

                String nuevaLinea = archivoMapa2.readLine();

                while( nuevaLinea != null ){
                    mapa += nuevaLinea;
                    mapa += "\n";

                    nuevaLinea = archivoMapa2.readLine();
                }

                //Creando la cancion 
                File cancion_map2 = new File("FOQUITOS DE NAVIDAD  FULL REMIX VIDEO RANDOM.wav");

                try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(cancion_map2)) {
    
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
    
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    
                    e.printStackTrace();
                }

                this.error = false;
                return mapa;

            }else if( seleccion == 3){


                String nuevaLinea = archivoMapa3.readLine();

                while( nuevaLinea != null ){
                    mapa += nuevaLinea;
                    mapa += "\n";

                    nuevaLinea = archivoMapa3.readLine();
                }

                //Creando la cancion 
                File cancion_map2 = new File("Elmo Russian Hard Bass  Long Version.wav");

                try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(cancion_map2)) {
    
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
    
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    
                    e.printStackTrace();
                }

                this.error = false;
                return mapa;

            }else{

                this.error = true;
                System.out.println("Ingrese un mapa valido por favor");

            }

        } catch (FileNotFoundException e) {
            
            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return mapa;
    }

}
