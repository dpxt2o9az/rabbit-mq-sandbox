package www;

import java.util.Date;

public class BlogEntry implements Comparable<BlogEntry> {
    private final String blogText;
    private final Date publishDate;
    
    public BlogEntry(String blogText) {
        this.blogText = blogText;
        this.publishDate = new Date();
    }

    @Override
    public int compareTo(BlogEntry o) {
        return o.publishDate.compareTo(this.publishDate);
    }
}
