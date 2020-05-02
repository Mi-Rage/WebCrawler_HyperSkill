package crawler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebCrawler extends JFrame {

    final String TITLE_OF_PROGRAM = "Web Crawler";
    final int START_LOCATION = 200;
    final int WINDOW_WIDTH = 600;
    final int WINDOW_HEIGHT = 400;

    DefaultTableModel model;

    public WebCrawler() {

        setTitle(TITLE_OF_PROGRAM);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(START_LOCATION, START_LOCATION, WINDOW_WIDTH, WINDOW_HEIGHT);

        Container container = getContentPane();


        // Создаем область с таблицей
        model = new DefaultTableModel(0, 2);
        model.setColumnIdentifiers(new String[]{"URL", "Title"});
        final JTable tableArea = new JTable(model);
        tableArea.setName("TitlesTable");
        tableArea.setEnabled(false);
        final JScrollPane scrollPane = new JScrollPane(tableArea);

        // Собираем все панели
        container.add(upArea(), BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(downArea(), BorderLayout.SOUTH);

        setVisible(true);
    }

    //Верхняя область с полем имени файла и кнопкой Get text!
    private JPanel upArea() {

        //Создадим панель с менеджером размещений
        JPanel upArea = new JPanel();
        upArea.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel urlLabel = new JLabel("URL: ");
        // Создадим поле для ввода URL
        JTextField urlTextField = new JTextField(30);
        urlTextField.setName("UrlTextField");
        // Создадим поле для вывода TITLE
        JLabel tLabel = new JLabel("Title: ");
        JLabel titleLabel = new JLabel();
        titleLabel.setName("TitleLabel");
        //Кнопка RUN
        JButton runButton = new JButton("Parse");
        runButton.setName("RunButton");
        runButton.addActionListener(actionEvent -> {
            //Получили URL из текстового поля
            String url = urlTextField.getText();
            // Установим название в поле вывода
            String title = getMainTitle(url);
            titleLabel.setText(title);

            model.setRowCount(0);
            model.addRow(new Object[]{url, title});
            getLinkAndTitle(url);

        });

        // Собираем верхнюю панель
        upArea.add(urlLabel);
        upArea.add(urlTextField);
        upArea.add(runButton);
        upArea.add(tLabel);
        upArea.add(titleLabel);

        return upArea;
    }

    private JPanel downArea() {
        JPanel downArea = new JPanel();
        downArea.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel exportLabel = new JLabel("Export: ");
        JTextField exportUrlText = new JTextField(30);
        exportUrlText.setName("ExportUrlTextField");
        //Кнопка SAVE
        JButton exportButton = new JButton("SAVE");
        exportButton.setName("ExportButton");
        exportButton.addActionListener(actionEvent -> {
            //Получили имя сохраняемого файла из текстового поля
            String exportUrl = exportUrlText.getText();
            File file = new File(exportUrl);
            try  (PrintWriter printWriter = new PrintWriter(file)) {

                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        printWriter.println(model.getValueAt(i, j));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        });

        // Собираем нижнюю панель
        downArea.add(exportLabel);
        downArea.add(exportUrlText);
        downArea.add(exportButton);

        return downArea;
    }

    public String getMainTitle(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
            if (!connection.getContentType().contains("text/html")) {
                throw new RuntimeException("NOT TEXT");
            }
            InputStream inputStream = connection.getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                Pattern pattern = Pattern.compile("(<title>)(.*)(</title>)");
                Matcher matcher = pattern.matcher(nextLine);
                if (matcher.find()) {
                    inputStream.close();
                    return matcher.group(2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void getLinkAndTitle(String url) {
        String link;
        String title;

        try {
            URLConnection connection = new URL(url).openConnection();
            InputStream inputStream = connection.getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                Matcher matcherLink = Pattern.compile("(?<=href=[\"']).*(?=[\"']>)").matcher(nextLine);
                if (matcherLink.find()) {
                    StringBuilder linkBuilder = new StringBuilder(matcherLink.group());
                    link = LinkUtilities.makeAbsoluteLink(linkBuilder, url);
                    title = getMainTitle(link);
                    if (!title.equals("")) {
                        model.addRow(new Object[]{link, title});
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

