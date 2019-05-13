package GUI;

import Abstract.Commands.ExportSettingsActionCommand;
import Abstract.Commands.ImportSettingsActionCommand;
import Abstract.Models.SearchSettings;
import Services.DIResolver;
import org.apache.commons.lang.StringUtils;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SettingsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea SpecificWordsInDomainURLS;
    private JButton ImportSettings;
    private JButton ExportSettings;
    private JTextArea ExceptionMetaTitles;
    private JTextArea FoundDomainsExceptions;
    private JTextArea WordsInDomainURLSExceptions;
    private JTextArea TopLevelDomainsExceptions;
    private JTextArea LookForKeywordsInSearchResults;
    private JScrollPane exceptionsMetaTitles;
    private SearchSettings searchSettings;

    public SettingsDialog(DIResolver diResolver) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setResizable(false);


        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        SearchSettings searchSettings = diResolver.getDbConnectionService().getSearchSettings();
        FillTextBoxesWithSearchSettings(searchSettings);

        ImportSettings.setAction(new ImportSettingsActionCommand(diResolver));
        ExportSettings.setAction(new ExportSettingsActionCommand(diResolver));

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void FillTextBoxesWithSearchSettings(SearchSettings searchSettings) {
        if (searchSettings != null) {
            this.searchSettings = searchSettings;
            SpecificWordsInDomainURLS.setText(String.join(";", searchSettings.KeywordsForLookingInDomainURLs));
            ExceptionMetaTitles.setText(String.join(";", searchSettings.MetaTagsExceptions));
            FoundDomainsExceptions.setText(String.join(";", searchSettings.ExceptionsForFoundDomains));
            WordsInDomainURLSExceptions.setText(String.join(";", searchSettings.ExceptionsForWordsInDomainURLs));
            TopLevelDomainsExceptions.setText(String.join(";", searchSettings.ExceptionsForTopLevelDomains));
            LookForKeywordsInSearchResults.setText(String.join(";", searchSettings.KeywordsForLookingInSearchResults));
        }
    }

    private void onOK() {
        // add your code here
        searchSettings = new SearchSettings();
        searchSettings.KeywordsForLookingInDomainURLs.addAll(separateTextBySemicolon(SpecificWordsInDomainURLS.getText()));
        searchSettings.MetaTagsExceptions.addAll(separateTextBySemicolon(ExceptionMetaTitles.getText()));
        searchSettings.ExceptionsForFoundDomains.addAll(separateTextBySemicolon(FoundDomainsExceptions.getText()));
        searchSettings.ExceptionsForTopLevelDomains.addAll(separateTextBySemicolon(TopLevelDomainsExceptions.getText()));
        searchSettings.KeywordsForLookingInSearchResults.addAll(separateTextBySemicolon(LookForKeywordsInSearchResults.getText()));
        searchSettings.ExceptionsForWordsInDomainURLs.addAll(separateTextBySemicolon(WordsInDomainURLSExceptions.getText()));

        dispose();
    }

    private List separateTextBySemicolon(String text) {
        ArrayList<String> settings = new ArrayList<>();
        if (StringUtils.isEmpty(text)) {
            settings.add("");
            return settings;
        }
        String [] arrOfStr = text.split(";");
        Collections.addAll(settings, arrOfStr);
        return settings;
    }

    public void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public SearchSettings getSearchSettings() {
        return searchSettings;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
