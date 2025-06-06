package javapasswordgenerator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox; // ¡Importamos JCheckBox!
import javax.swing.Timer;    // Para el mensaje de copiado temporal

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;

import java.security.SecureRandom;

public class Javapasswordgenerator {

    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBER_CHARS = "0123456789";
    private static final String SYMBOL_CHARS = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    private static final SecureRandom random = new SecureRandom();

    private static String lastGeneratedPassword = ""; // Para mantener la contraseña para copiar

    public static void main(String[] args) {
        JFrame frame = new JFrame("Generador de Contraseñas Seguras");
        frame.setSize(450, 350); // Ajustamos la altura para los nuevos checkboxes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel appTitle = new JLabel("Generador de Contraseñas Seguras");
        appTitle.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(appTitle);

        panel.add(new JLabel(" ")); // Espacio

        JLabel lengthLabel = new JLabel("Longitud de la Contraseña:");
        JTextField lengthField = new JTextField(5);

        // --- Nuevos JCheckBoxes para opciones de caracteres ---
        JLabel optionsLabel = new JLabel("Incluir:");
        JCheckBox lowerCaseCbx = new JCheckBox("Minúsculas");
        JCheckBox upperCaseCbx = new JCheckBox("Mayúsculas");
        JCheckBox numbersCbx = new JCheckBox("Números");
        JCheckBox symbolsCbx = new JCheckBox("Símbolos");

        // Por defecto, que estén todas marcadas para empezar
        lowerCaseCbx.setSelected(true);
        upperCaseCbx.setSelected(true);
        numbersCbx.setSelected(true);
        symbolsCbx.setSelected(true);

        JButton generateButton = new JButton("Generar Contraseña");

        JTextArea passwordDisplayArea = new JTextArea(5, 30);
        passwordDisplayArea.setEditable(false);
        passwordDisplayArea.setLineWrap(true);
        passwordDisplayArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(passwordDisplayArea);

        JButton copyButton = new JButton("Copiar Contraseña");
        copyButton.setEnabled(false);

        JLabel copyStatusLabel = new JLabel("");
        copyStatusLabel.setForeground(Color.BLUE);
        copyStatusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyStatusLabel.setVisible(false);

        // --- Añadimos los componentes al panel ---
        panel.add(lengthLabel);
        panel.add(lengthField);
        panel.add(generateButton);

        panel.add(optionsLabel); // Etiqueta para las opciones
        panel.add(lowerCaseCbx);
        panel.add(upperCaseCbx);
        panel.add(numbersCbx);
        panel.add(symbolsCbx);

        panel.add(scrollPane);
        panel.add(copyButton);
        panel.add(new JLabel(" ")); // Espacio
        panel.add(copyStatusLabel);

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int passwordLength = Integer.parseInt(lengthField.getText());

                    if (passwordLength <= 0) {
                        passwordDisplayArea.setText("La longitud debe ser un número positivo.");
                        copyButton.setEnabled(false);
                        lastGeneratedPassword = "";
                        copyStatusLabel.setVisible(false);
                        return;
                    }

                    // --- Lógica actualizada para incluir solo los caracteres seleccionados ---
                    StringBuilder availableCharsBuilder = new StringBuilder();
                    if (lowerCaseCbx.isSelected()) {
                        availableCharsBuilder.append(LOWERCASE_CHARS);
                    }
                    if (upperCaseCbx.isSelected()) {
                        availableCharsBuilder.append(UPPERCASE_CHARS);
                    }
                    if (numbersCbx.isSelected()) {
                        availableCharsBuilder.append(NUMBER_CHARS);
                    }
                    if (symbolsCbx.isSelected()) {
                        availableCharsBuilder.append(SYMBOL_CHARS);
                    }

                    String availableChars = availableCharsBuilder.toString();

                    // Validar si al menos un tipo de carácter ha sido seleccionado
                    if (availableChars.isEmpty()) {
                        passwordDisplayArea.setText("¡Selecciona al menos un tipo de carácter!");
                        copyButton.setEnabled(false);
                        lastGeneratedPassword = "";
                        copyStatusLabel.setVisible(false);
                        return;
                    }

                    StringBuilder generatedPasswordBuilder = new StringBuilder();
                    for (int i = 0; i < passwordLength; i++) {
                        int randomIndex = random.nextInt(availableChars.length());
                        char randomChar = availableChars.charAt(randomIndex);
                        generatedPasswordBuilder.append(randomChar);
                    }

                    lastGeneratedPassword = generatedPasswordBuilder.toString();
                    passwordDisplayArea.setText(lastGeneratedPassword);
                    copyButton.setEnabled(true);
                    copyStatusLabel.setVisible(false);

                } catch (NumberFormatException ex) {
                    passwordDisplayArea.setText("Por favor, introduce un número válido para la longitud.");
                    copyButton.setEnabled(false);
                    lastGeneratedPassword = "";
                    copyStatusLabel.setVisible(false);
                }
            }
        });

        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(lastGeneratedPassword);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);

                copyStatusLabel.setText("¡Contraseña copiada al portapapeles!");
                copyStatusLabel.setVisible(true);

                Timer timer = new Timer(1500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        copyStatusLabel.setVisible(false);
                        ((Timer)evt.getSource()).stop();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}