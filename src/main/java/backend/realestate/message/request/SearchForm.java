package backend.realestate.message.request;

public class SearchForm {
    private String searchString;

    public SearchForm(String searchString) {
        this.searchString = searchString;
    }

    public SearchForm() {
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
