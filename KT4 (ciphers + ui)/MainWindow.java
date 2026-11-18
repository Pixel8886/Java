import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MainWindow extends JFrame {


    private final JComboBox<String> cipherBox = new JComboBox<>(new String[]{"Шифр Цезаря", "Шифр Виженера"});
    private final JComboBox<String> modeBox = new JComboBox<>(new String[]{"Шифрование", "Расшифровка"});

    private final JTextField inputField = new JTextField(30);
    private final JTextField outputField = new JTextField(30);
    private final JTextField keyField = new JTextField(30);

    private int row = 0;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }


    public MainWindow() {
        super("Шифры Цезаря и Виженера");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        JButton inputButton = new JButton("Обзор…");
        JButton outputButton = new JButton("Обзор…");
        JButton runButton = new JButton("Выполнить");


        addRow("Шифр:", cipherBox, null);
        addRow("Режим:", modeBox, null);
        addRow("Входной файл:", inputField, inputButton);
        addRow("Выходной файл:", outputField, outputButton);
        addRow("Ключ (число или слово):", keyField, null);
        addRow("", runButton, null);


        inputButton.addActionListener(e -> browse(inputField, FileDialog.LOAD));
        outputButton.addActionListener(e -> browse(outputField, FileDialog.SAVE));
        runButton.addActionListener(e -> run());

        pack();
        setLocationRelativeTo(null);
    }


    private void browse(JTextField target, int mode) {
        FileDialog dialog = new FileDialog(this, "Выберите файл", mode);
        dialog.setVisible(true);
        if (dialog.getFile() != null) {
            target.setText(dialog.getDirectory() + dialog.getFile());
        }
    }


    private void run() {
        try {

            Path in = Paths.get(inputField.getText().trim());
            if (!Files.isRegularFile(in)) {
                throw new IllegalArgumentException("Входной файл не найден");
            }
            String outPath = outputField.getText().trim();
            if (outPath.isEmpty()) {                    
                throw new IllegalArgumentException("Укажите выходной файл");
            }


            String text = Files.readString(in, StandardCharsets.UTF_8);
            String key = keyField.getText().trim();
            boolean encrypt = modeBox.getSelectedIndex() == 0;


            String result;
            if (cipherBox.getSelectedIndex() == 0) {

                int shift = Integer.parseInt(key);
                result = encrypt ? CaesarCipher.encrypt(text, shift) : CaesarCipher.decrypt(text, shift);
            } else {

                result = encrypt ? VigenereCipher.encrypt(text, key) : VigenereCipher.decrypt(text, key);
            }


            Files.writeString(Paths.get(outPath), result, StandardCharsets.UTF_8);
            JOptionPane.showMessageDialog(this, "Готово! Результат сохранён в файл:\n" + outPath);


        } catch (NumberFormatException e) {
            error("Ключ шифра Цезаря должен быть целым числом");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }


    private void error(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }



    private void addRow(String label, JComponent field, JButton button) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = row++;
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        add(new JLabel(label), c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(field, c);
        if (button != null) {
            c.gridx = 2;
            c.weightx = 0;
            c.fill = GridBagConstraints.NONE;
            add(button, c);
        }
    }
}
