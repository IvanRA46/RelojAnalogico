/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package relojanalogico;

/**
 *
 * @author ivann
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import javax.imageio.ImageIO;

public class RelojAnalogico extends JPanel implements Runnable {
    private BufferedImage fondo;
    private int width = 800;
    private int height = 600;
    private Thread hilo;
    
    public RelojAnalogico() {
        try {
            // Cargar la imagen de fondo
            fondo = ImageIO.read(new File("C:\\Users\\ivann\\OneDrive\\Documentos\\NetBeansProjects\\RelojAnalogico\\src\\relojanalogico\\All.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(width, height));
        hilo = new Thread(this);
        hilo.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dibujar imagen de fondo
        g2d.drawImage(fondo, 0, 0, width, height, this);

        // Dibujar el círculo del reloj con degradado
        int radio = 250;
        Point centro = new Point(width / 2, height / 2);
        GradientPaint gradiente = new GradientPaint(centro.x - radio, centro.y - radio, Color.LIGHT_GRAY,
                centro.x + radio, centro.y + radio, Color.DARK_GRAY);
        g2d.setPaint(gradiente);
        g2d.fillOval(centro.x - radio, centro.y - radio, 2 * radio, 2 * radio);

        // Dibujar números del reloj
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        for (int i = 1; i <= 12; i++) {
            double angle = Math.toRadians((i - 3) * 30);  // Convertir a radianes
            int x = centro.x + (int) (Math.cos(angle) * (radio - 40));
            int y = centro.y + (int) (Math.sin(angle) * (radio - 40));
            g2d.drawString(String.valueOf(i), x - 10, y + 10);
        }

        // Obtener tiempo actual
        Calendar calendario = Calendar.getInstance();
        int horas = calendario.get(Calendar.HOUR);
        int minutos = calendario.get(Calendar.MINUTE);
        int segundos = calendario.get(Calendar.SECOND);

        // Dibujar manecillas
        dibujarManecilla(g2d, centro, segundos * 6, radio - 50, Color.RED, 2);  // Manecilla de segundos
        dibujarManecilla(g2d, centro, minutos * 6, radio - 80, Color.BLUE, 4); // Manecilla de minutos
        dibujarManecilla(g2d, centro, (horas % 12) * 30 + minutos / 2, radio - 120, Color.GREEN, 6);  // Manecilla de horas
    }

    private void dibujarManecilla(Graphics2D g2d, Point centro, double angulo, int longitud, Color color, int grosor) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(grosor));
        double radianes = Math.toRadians(angulo - 90);
        int x = centro.x + (int) (Math.cos(radianes) * longitud);
        int y = centro.y + (int) (Math.sin(radianes) * longitud);
        g2d.drawLine(centro.x, centro.y, x, y);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);  // Actualizar cada segundo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
        }
    }

    public static void main(String[] args) {
        JFrame ventana = new JFrame("Reloj Analógico");
        RelojAnalogico reloj = new RelojAnalogico();
        ventana.add(reloj);
        ventana.pack();
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
    }
}
