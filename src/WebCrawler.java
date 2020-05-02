package crawler;
 
import javax.swing.;
import java.awt.;
 
public class WebCrawler extends JFrame {
 
    final String TITLE_OF_PROGRAM = Simple Window;
    final int START_LOCATION = 200;
    final int WINDOW_WIDTH = 450;
    final int WINDOW_HEIGHT = 450;
 
    JTextArea textArea;
 
    public WebCrawler() {
 
        setTitle(TITLE_OF_PROGRAM);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(START_LOCATION, START_LOCATION, WINDOW_WIDTH, WINDOW_HEIGHT);
 
         Создаем область с текстом
        textArea = new JTextArea(HTML code);
        textArea.setName(TextArea);
        textArea.setEnabled(false);
 
         Добавляем область во фрейм
        add(BorderLayout.CENTER, textArea);
        setVisible(true);
 
    }
}