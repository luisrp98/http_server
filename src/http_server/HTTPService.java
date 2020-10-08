/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http_server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rnavarro
 */
public class HTTPService implements Runnable {

    public String fileName, archivo;
    public String[] tokens;
    Resultado r = new Resultado();

    private final Socket clientSocket;
    PrintStream out;

    public HTTPService(Socket c) throws IOException {
        clientSocket = c;
        this.out = new PrintStream(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        BufferedReader in
                = null;
        try {
            PrintWriter CAMBIAR
                    = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;

            // leer la solicitud del cliente
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                if (inputLine.startsWith("GET")) {
                    tokens = inputLine.split(" "); //Separa las palabras del url, obtiene nombre de archivo    
                    if (inputLine.contains("html?")) {
                        archivo = tokens[1].substring(tokens[1].lastIndexOf('/') + 1);
                        IMGFile(archivo);
                        HTMLResultado(archivo);
                    }
                    if (inputLine.contains("html")) {
                        archivo = tokens[1].substring(tokens[1].lastIndexOf('/') + 1);
                        File f = new File(archivo);
                        HTMLFile(f);
                    }
                    if (inputLine.contains("png") || inputLine.contains("gif") || inputLine.contains("jpg")) {
                        archivo = tokens[1].substring(tokens[1].lastIndexOf('/') + 1);
                        IMGFile(archivo);
                    }
                    if (inputLine.contains(".php")) {
                        archivo = inputLine.substring(inputLine.lastIndexOf('?') + 1);
                        HTMLForm(archivo);

                    } else {
                        archivo = "index.html";
                        File f = new File(archivo);
                        HTMLFile(f);

                    }
                }

                //Si recibimos linea en blanco, es fin del la solicitud
                if (inputLine.isEmpty()) {
                    break;
                }
            }

        } catch (IOException ex) {
            System.out.println("Error en la conexion");
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(HTTPService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void HTMLFile(File filepointer) throws FileNotFoundException, IOException {

        System.out.println(archivo);
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html; charset=utf-8");
        out.println("Content-Length: " + filepointer.length());
        out.println();

        try (FileReader file = new FileReader(archivo)) {
            int data;
            System.out.println("sending");

            while ((data = file.read()) != -1) {
                out.write(data);
                out.flush();
            }

            System.out.println("done");
        }
        clientSocket.close();
    }

    private void IMGFile(String archivo) throws FileNotFoundException, IOException {
        File f = new File(archivo);
        out.println("HTTP/1.1 200 OK");
        //Tipo de imagen que se recibe
        if (archivo.endsWith("ico")) {
            out.println("Content-Type: image/ico");
        }
        if (archivo.endsWith("png")) {
            out.println("Content-Type: image/png");
        }
        if (archivo.endsWith("jpg")) {
            out.println("Content-Type: image/jpg");
        }
        if (archivo.endsWith("gif")) {
            out.println("Content-Type: image/gif");
        }
        out.println("Content-Length: " + f.length());
        out.println();
        FileInputStream file;
        try {
            file = new FileInputStream(f);
            int data;
            while ((data = file.read()) != -1) {
                out.write(data);
            }
            System.out.println("sending");
            System.out.println("done");
            out.flush();
            file.close();
        } catch (IOException e) {
        }
        clientSocket.close();
    }

    private void HTMLForm(String archivo) throws FileNotFoundException, IOException {
        String respuestas[] = archivo.split("\\s+|\\&\\s*|\\+\\s*");
        for (int i = 0; i < respuestas.length; i++) {
            System.out.println(respuestas[i]);
        }
        String nombre, apellidos;
        nombre = respuestas[0].substring(respuestas[0].lastIndexOf('=') + 1);
        apellidos = (respuestas[1].substring(respuestas[1].lastIndexOf('=') + 1)) + " " + respuestas[2];
        System.out.println(nombre + " " + apellidos);
        
        r.Resultado(nombre, apellidos);
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html; charset=utf-8");
        //  out.println("Content-Length: " + filepointer.length());
        out.println();

        try (FileReader file = new FileReader(archivo)) {
            int data;
            System.out.println("sending");

            while ((data = file.read()) != -1) {
                out.write(data);
                out.flush();
            }

            System.out.println("done");
        }
        clientSocket.close();
    }

    private void HTMLResultado(String archivo) throws FileNotFoundException, IOException {
        String archivo2 = archivo.substring(0, 15);
        System.out.println(archivo2);
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html; charset=utf-8");
        //  out.println("Content-Length: " + filepointer.length());
        out.println();
        try (FileReader file = new FileReader(archivo2)) {
            int data;
            System.out.println("sending");

            while ((data = file.read()) != -1) {
                out.write(data);
                out.flush();
            }

            System.out.println("done");
        }
        clientSocket.close();
    }


}
