package Services;

import Models.SearchSettings;
import Utils.StrUtils;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SettingsService {

    private static SearchSettings searchSettings;

    public SettingsService() {
        if (searchSettings == null) {
            this.searchSettings = new SearchSettings();
        }
    }

    public void initSettingsFile(File settingsFile) {
        if (settingsFile == null) {
            Logger.info("Exceptions file path empty");
            return;
        }
        if (StrUtils.isStringContainsExtraSymbols(settingsFile.getAbsolutePath())) {
            Logger.info("Exceptions file has wrong symbols in name or path");
            return;
        }
        SearchSettings searchSettings = new SearchSettings();
        searchSettings.domainExceptions = new ArrayList<>();
        searchSettings.URLExceptions = new ArrayList<>();
        searchSettings.metaTagsExceptions = new ArrayList<>();
        searchSettings.topLevelDomainsExceptions = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(settingsFile.toPath(), StandardCharsets.UTF_8);
            lines.removeIf(l -> l.equals(""));
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).contains("# Exceptions for found domains:")) {
                    searchSettings.domainExceptions = new ArrayList<>(collectTerms(i, lines));
                }

                if (lines.get(i).contains("# Exceptions for words in domain URLs:")) {
                    searchSettings.URLExceptions = new ArrayList<>(collectTerms(i, lines));
                }

                if (lines.get(i).contains("# Exceptions meta titles:")) {
                    searchSettings.metaTagsExceptions = new ArrayList<>(collectTerms(i, lines));
                }

                if (lines.get(i).contains("# Exceptions for top level domains:")) {
                    searchSettings.topLevelDomainsExceptions = new ArrayList<>(collectTerms(i, lines));
                }
            }
        } catch (IOException e) {
            Logger.error(e,"Cannot initialize input exceptions file");
        }
    }

    private ArrayList<String> collectTerms(int index, List<String> lines) {
        ArrayList<String> buffer = new ArrayList<>();
        for (int k = (index+1); k < lines.size(); k++)
        {
            if (lines.get(k).startsWith("#")) {
                break;
            }
            buffer.add(lines.get(k).trim().toLowerCase());
        }
        return buffer;
    }

    public static SearchSettings getSearchSettings() {
        return searchSettings;
    }
}
