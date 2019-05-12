package Services;

import Abstract.Models.SearchSettings;
import Utils.DirUtils;
import org.tinylog.Logger;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SettingsService {

    private File settingsDataFile;
    private SearchSettings searchSettings;

    private File getSettingsDataFile() {
        return settingsDataFile;
    }

    public SearchSettings getSearchSettings() {
        return searchSettings;
    }

    private void writeItemsToFile(List<String> list, String sectionName, BufferedWriter reader) {
        try {
            reader.write(sectionName);
            reader.write("\r\n");
            for (String item : list) {
                reader.write(item);
            }
            reader.write("\r\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void saveSearchSettingsToFile(SearchSettings searchSettings,  File fileToSave) {
        try {
            FileWriter fstream = new FileWriter(fileToSave);
            BufferedWriter out = new BufferedWriter(fstream);

            writeItemsToFile(searchSettings.ExceptionsForFoundDomains, "# Exceptions for found domains:",  out);
            writeItemsToFile(searchSettings.ExceptionsForWordsInDomainURLs, "# Exceptions for words in domain URLs:",  out);
            writeItemsToFile(searchSettings.MetaTagsExceptions, "# Exceptions meta titles:",  out);
            writeItemsToFile(searchSettings.ExceptionsForTopLevelDomains, "# Exceptions for top level domains:",  out);
            writeItemsToFile(searchSettings.KeywordsForLookingInSearchResults, "# Look for keywords in search results (operator “or”):",  out);
            writeItemsToFile(searchSettings.KeywordsForLookingInDomainURLs, "# Specific words in domain URLs (operator “or”):",  out);
            out.close();
        } catch (Exception e) {
            Logger.tag("SYSTEM").error(e);
        }
    }

    public SearchSettings getSearchSettingsFromFile(File file) {
        if (DirUtils.isFileOk(file, "txt")) {
            settingsDataFile = file;
        }

        searchSettings = new SearchSettings();
        try {
            List<String> lines = Files.readAllLines(getSettingsDataFile().toPath(), StandardCharsets.UTF_8);
            lines.removeIf(l -> l.equals(""));
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).contains("# Exceptions for found domains:")) {
                    searchSettings.ExceptionsForFoundDomains = new ArrayList<>(collectTerms(i, lines));
                }

                if (lines.get(i).contains("# Exceptions for words in domain URLs:")) {
                    searchSettings.ExceptionsForWordsInDomainURLs = new ArrayList<>(collectTerms(i, lines));
                }

                if (lines.get(i).contains("# Exceptions meta titles:")) {
                    searchSettings.MetaTagsExceptions = new ArrayList<>(collectTerms(i, lines));
                }

                if (lines.get(i).contains("# Exceptions for top level domains:")) {
                    searchSettings.ExceptionsForTopLevelDomains = new ArrayList<>(collectTerms(i, lines));
                }

                if (lines.get(i).contains("# Look for keywords in search results (operator “or”):")) {
                    searchSettings.KeywordsForLookingInSearchResults = new ArrayList<>(collectTerms(i, lines));
                }

                if (lines.get(i).contains("# Specific words in domain URLs (operator “or”):")) {
                    searchSettings.KeywordsForLookingInDomainURLs = new ArrayList<>(collectTerms(i, lines));
                }
            }
        } catch (IOException e) {
            Logger.tag("SYSTEM").error(e, "Cannot initialize input exceptions file");
        }
        return searchSettings;
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
}
