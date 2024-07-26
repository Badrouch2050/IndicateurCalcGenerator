package genindo;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import genindo.tools.GenerateClassDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class GenerateClassAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        if (project != null) {
            // Afficher la boîte de dialogue
            GenerateClassDialog dialog = new GenerateClassDialog();
            if (!dialog.showAndGet()) {
                return; // Utilisateur a annulé
            }
            String className = dialog.getClassName();
            String extraField = dialog.getExtraField();

            // Vérification des entrées utilisateur
            if (className.isEmpty()) {
                Messages.showMessageDialog(project, "Class name cannot be empty.", "Error", Messages.getErrorIcon());
                return;
            }
            //

            // Obtient le répertoire cible où l'utilisateur a cliqué
            VirtualFile virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
            if (virtualFile == null || !virtualFile.isDirectory()) {
                Messages.showMessageDialog(project, "No valid directory selected.", "Error", Messages.getErrorIcon());
                return;
            }

            PsiManager psiManager = PsiManager.getInstance(project);
            PsiDirectory directory = psiManager.findDirectory(virtualFile);

            // Lire le modèle de classe Java à partir des ressources
            String classTemplate = readTemplateFromFile("/templates/MyGeneratedClassTemplate.java");
            if (classTemplate == null) {
                Messages.showMessageDialog(project, "Failed to read class template.", "Error", Messages.getErrorIcon());
                return;
            }

            // Remplacer les placeholders dans le template par les valeurs saisies
            classTemplate = classTemplate.replace("${CLASS_NAME}", className).replace("${CLASS_SERVICE}", extraField);

            // Crée le fichier Java à partir du template
            PsiFileFactory fileFactory = PsiFileFactory.getInstance(project);

            PsiFile javaFile = fileFactory.createFileFromText(className + ".java", classTemplate);


            // Effectuer les opérations de modification du système de fichiers dans une write-action
            ApplicationManager.getApplication().runWriteAction(() -> {
                if (directory != null) {
                    directory.add(javaFile);
                    Messages.showMessageDialog(project, "Class generated successfully!", "Information", Messages.getInformationIcon());
                } else {
                    Messages.showMessageDialog(project, "Failed to create class. Directory not found.", "Error", Messages.getErrorIcon());
                }
            });
        }
    }

    private String readTemplateFromFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = getClass().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return content.toString();
    }
}