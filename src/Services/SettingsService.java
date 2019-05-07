package Services;

import Abstract.Models.SearchSettings;
import Utils.DirUtils;
import org.tinylog.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SettingsService {

    private SearchSettings searchSettings;

    public SettingsService() {

    }

    public void initSettingsData(SearchSettings searchSettings) {
       this.searchSettings = searchSettings;
    }

    public SearchSettings getSearchSettings() {
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
