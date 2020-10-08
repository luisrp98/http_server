/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http_server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author luisr
 */
public class Resultado {
        public void Resultado(String nombre, String apellidos) {
        String resultado1 = "<table class=\"egt\">\n"
                + "\n"
                + "    <tr>\n"
                + "\n"
                + "        <td>Nombre</td>\n"
                + "\n"
                + "        <td>Apellido</td>\n"
                + "\n"
                + "    </tr>";
        File f = new File("Registro.html");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(resultado1 + "\n    <tr>\n"
                    + "        <td>" + nombre + "</td>\n"
                    + "\n"
                    + "        <td>" + apellidos + "</td>\n"
                    + "\n"
                    + "    </tr>\n"
                    + "\n"
                    + "</table>");
            bw.close();
        } catch (IOException e) {
        }
    }
}
