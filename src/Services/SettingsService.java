package Services;

import Models.SearchSettings;
import Utils.DirUtils;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SettingsService {

    private static SearchSettings searchSettings;
    private static File settingsDataFile;

    public SettingsService() {
        if (searchSettings == null) {
            this.searchSettings = new SearchSettings();
        }
    }

    public File getSettingsDataFile() {
        return settingsDataFile;
    }

    public void initSettingsFile(File file) {
        if (DirUtils.isFileOk(file, "txt")) {
            settingsDataFile = file;
        }
    }

    public void initSettingsFileData(File settingsFilePath) {
        initSettingsFile(settingsFilePath);
        if(!DirUtils.isFileOk(settingsFilePath, "txt")) {
            return;
        }
        SearchSettings searchSettings = new SearchSettings();
        searchSettings.domainExceptions = new ArrayList<>();
        searchSettings.URLExceptions = new ArrayList<>();
        searchSettings.metaTagsExceptions = new ArrayList<>();
        searchSettings.topLevelDomainsExceptions = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(getSettingsDataFile().toPath(), StandardCharsets.UTF_8);
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
            Logger.tag("SYSTEM").error(e,"Cannot initialize input exceptions file");
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


    public SearchSettings getSearchSettings() {
        return searchSettings;
    }

    public static SearchSettings getSearchSettingsStatic() {
        return searchSettings;
    }

    public void clearSettingsFile() {
        settingsDataFile = null;
    }
}
