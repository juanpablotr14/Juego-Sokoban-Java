package com.zetcode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;




public class basesDatos {

   Connection conexion;
   java.sql.Statement statement;

    public static int puntajes = 50;
    private static String nombreUsuario = "";

    public String pedirEnConsola(){

        System.out.print("\033[H\033[2J");
        System.out.flush();

        Scanner sc = new Scanner( System.in );
        System.out.println("Ingrese porfavor su nombre: ");
        basesDatos.nombreUsuario = sc.nextLine();

        try {
            
            enviarBase( basesDatos.nombreUsuario );

        } catch (Exception e) {
            JOptionPane.showMessageDialog( null , e.getMessage() );
            e.printStackTrace();
        }
        return basesDatos.nombreUsuario;
    }


    public String puntajeActualizado(){
        
        try {
            actualizarPuntajeBase( basesDatos.nombreUsuario );
        } catch (Exception e) {
            JOptionPane.showMessageDialog( null , "No se pudo actualizar" );
            e.printStackTrace();
        }

        return "";
    }
    

    public ArrayList<String> recibirBase(){
        
        ArrayList<String> nombresUsuarios = new ArrayList<String>();
        String consulta = "SELECT nombre, puntajes FROM public.puntajes;";
        try {

            conexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","1234");
            statement = conexion.createStatement();
            ResultSet respuesta = statement.executeQuery( consulta );
               
            while ( respuesta.next() ){
                
                nombresUsuarios.add( respuesta.getString("nombre"));
            }

            return nombresUsuarios;
        } catch (SQLException e){ e.printStackTrace(); };

        return nombresUsuarios;
    }


    public String enviarBase( String nombre ) throws Exception{
        
        try {
            conexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","1234");
            statement = conexion.createStatement();

            ArrayList<String> nombresExistentes = recibirBase();
            
            String insertar = "INSERT INTO public.puntajes("
                +"nombre, puntajes)"
                +"VALUES ('"+nombre+"', 50);";

            for( int i = 0; i < nombresExistentes.size(); i++ ){
                
                if( nombresExistentes.get( i ).compareTo( nombre ) == 0 ){
                    System.out.println("Ese nombre ya existe, no se puede añadir, por favor reiniciar el programa");
                    throw new Exception("Ese nombre ya existe, por favor reinicia el programa :D");
                }

                if( i == ( ( nombresExistentes.size() ) - 1 ) ){
                    statement.executeUpdate( insertar );
                    return "";
                }

            }
        } catch (SQLException e) {  

            System.out.println( e.getMessage() );
            e.printStackTrace(); 
        }
        return "";
    }

    public Integer actualizarPuntajeBase( String nombre ) throws Exception{
        try {
            conexion = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","1234");
            statement = conexion.createStatement();

            ArrayList<String> nombresExistentes = recibirBase();
            
            String actualizar = "UPDATE public.puntajes SET nombre='"+nombre+"', puntajes="+basesDatos.puntajes+" WHERE nombre='"+nombre+"';";

            for( int i = 0; i < nombresExistentes.size(); i++ ){
                
                statement.executeUpdate( actualizar);
                

            }  }catch (SQLException e) {  

            System.out.println( e.getMessage() );
            e.printStackTrace(); 
        }
        return 0;
        
    }



}
