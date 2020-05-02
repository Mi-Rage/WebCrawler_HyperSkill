package crawler;
 
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
 
public class WebCrawler extends JFrame {
 
    final String TITLE_OF_PROGRAM = "Simple Window";
    final int START_LOCATION = 200;
    final int WINDOW_WIDTH = 450;
    final int WINDOW_HEIGHT = 450;
    final String LINE_SEPARATOR = System.getProperty("line.separator");
    String siteText;
 
    private final JTextArea textArea;
    private JTextField urlTextField;
 
    public WebCrawler() {
 
        setTitle(TITLE_OF_PROGRAM);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(START_LOCATION, START_LOCATION, WINDOW_WIDTH, WINDOW_HEIGHT);
 
        Container container = getContentPane();
 
        // Создаем область с текстом
        textArea = new JTextArea("HTML code?");
        textArea.setName("HtmlTextArea");
        textArea.setEnabled(false);
        textArea.setLineWrap(true);
        JScrollPane scrollableTextArea = new JScrollPane(textArea);
 
        // Добавляем области во фрейм
        container.add(scrollableTextArea, BorderLayout.CENTER);
        container.add(upArea(), BorderLayout.NORTH);
        setVisible(true);
        textArea.append(siteText);
    }
 
    //Верхняя область с полем имени файла и кнопкой Get text!
    private JPanel upArea() {
        //Создадим панель с менеджером размещений
        JPanel upArea = new JPanel();
        upArea.setLayout(new FlowLayout(FlowLayout.CENTER));
        // Создадим поле для ввода URL
        urlTextField = new JTextField(30);
        urlTextField.setName("UrlTextField");
        //Кнопка RUN
        JButton runButton = new JButton("Get text!");
        runButton.setName("RunButton");
        runButton.addActionListener(actionEvent -> {
            //Получили URL из текстового поля
            String url = urlTextField.getText();
 
            //Пробуем создать поток и читать из него пока есть что
            try (InputStream inputStream = new URL(url).openStream()) {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                final StringBuilder stringBuilder = new StringBuilder();
 
                String nextLine;
                while ((nextLine = reader.readLine()) != null) {
                    stringBuilder.append(nextLine);
                    stringBuilder.append(LINE_SEPARATOR);
                }
                siteText = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
 
            // Что прочитали выводим в наше текстовоео поле.
            textArea.setText(siteText);
        });
 
        // Собираем панель из поля ввода имени и 2х кнопок
        upArea.add(urlTextField);
        upArea.add(runButton);
 
        return upArea;
    }
 
}