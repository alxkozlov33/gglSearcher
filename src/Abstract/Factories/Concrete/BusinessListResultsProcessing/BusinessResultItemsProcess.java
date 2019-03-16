package Abstract.Factories.Concrete.BusinessListResultsProcessing;

import Abstract.Models.SearchResultModels.BusinessListSearchResultItem;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public abstract class BusinessResultItemsProcess {
    public abstract List<BusinessListSearchResultItem> processBody(Element body);

    Elements findBussinessGeoDataByNames(Elements names) {
        Elements addressesElements = new Elements();
        for (Element element : names) {
            if (element.parent().childNodeSize() == 4 || element.parent().childNodeSize() == 5) {
                addressesElements.add(element.parent().child(2).select("span").first());
            } else if (element.parent().childNodeSize() == 2) {
                addressesElements.add(element.parent().child(1).child(1).select("span").first());
            } else if (element.parent().childNodeSize() == 3) {
                if (element.parent().child(2).childNodeSize() == 1) {
                    if (element.parent().child(2).child(0).childNodeSize() == 1) {
                        addressesElements.add(element.parent().child(2).child(0).select("span").first());
                    } else if (element.parent().child(2).child(1).childNodeSize() > 1) {
                        addressesElements.add(element.parent().child(2).child(1).select("span").first());
                    }
                } else if (element.parent().child(2).childNodeSize() == 2) {
                    if (element.parent().child(2).child(1).childNodeSize() == 0) {
                        addressesElements.add(new Element("<div>No data</div>"));
                    } else if (element.parent().child(2).child(1).childNodeSize() > 1) {
                        addressesElements.add(element.parent().child(2).child(1).select("span").first());
                    }
                } else if (element.parent().child(2).childNodeSize() == 3 || element.parent().child(2).childNodeSize() == 4) {
                    addressesElements.add(element.parent().child(2).child(1).select("span").first());
                }
            }
        }
        return addressesElements;
    }
}
