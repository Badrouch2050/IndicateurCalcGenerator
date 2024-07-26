package genindo.tools;


import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class GenerateClassDialog extends DialogWrapper {
    private JTextField classNameField;
    private JTextField extraField;

    public GenerateClassDialog() {
        super(true); // use current window as parent
        init();
        setTitle("Generate New Class");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Class Name:"));
        classNameField = new JTextField();
        panel.add(classNameField);

        panel.add(new JLabel("Extra Field:"));
        extraField = new JTextField();
        panel.add(extraField);

        return panel;
    }

    public String getClassName() {
        return classNameField.getText();
    }

    public String getExtraField() {
        return extraField.getText();
    }
}
