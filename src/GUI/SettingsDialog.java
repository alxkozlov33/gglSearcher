package GUI;

import Abstract.Models.SearchSettings;
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
    private SearchSettings searchSettings;

    public SettingsDialog(SearchSettings searchSettings) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        if (searchSettings != null) {

        }


        buttonCancel.addActionListener(e -> onCancel());

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

    private void onOK() {
        // add your code here
        SearchSettings searchSettings = new SearchSettings();
        searchSettings.specificWordsInDomainURLs.addAll(separateTextBySemicolon(SpecificWordsInDomainURLS.getText()));
        searchSettings.metaTagsExceptions.addAll(separateTextBySemicolon(ExceptionMetaTitles.getText()));
        searchSettings.domainExceptions.addAll(separateTextBySemicolon(FoundDomainsExceptions.getText()));
        searchSettings.topLevelDomainsExceptions.addAll(separateTextBySemicolon(TopLevelDomainsExceptions.getText()));
        searchSettings.keywordsInSearchResults.addAll(separateTextBySemicolon(LookForKeywordsInSearchResults.getText()));
        searchSettings.URLExceptions.addAll(separateTextBySemicolon(WordsInDomainURLSExceptions.getText()));

        dispose();
    }

    private List separateTextBySemicolon(String text) {
        ArrayList<String> settings = new ArrayList<>();
        if (StringUtils.isEmpty(text)) {
            return settings;
        }
        String [] arrOfStr = text.split(";");
        Collections.addAll(settings, arrOfStr);
        return settings;
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public SearchSettings getSearchSettings() {
        return searchSettings;
    }
}
