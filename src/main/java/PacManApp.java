import javax.swing.*;

public class PacManApp extends JFrame {

    public PacManApp() {
        add(new Game());
    }


    public static void main(String[] args) {
        PacManApp pac = new PacManApp();
        pac.setVisible(true);
        pac.setTitle("Pacman");
        pac.setSize(480,540);
        pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
        pac.setLocationRelativeTo(null);

    }



}
