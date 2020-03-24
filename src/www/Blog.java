package www;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Blog {

    Collection<BlogEntry> blogEntries;

    public Blog() {
        blogEntries = new ArrayList<>();
    }

    public void newBlogEntry(String blogText) {
        BlogEntry newEntry = new BlogEntry(blogText);
        blogEntries.add(newEntry);
    }
    
    public Collection<BlogEntry> getBlogEntries() {
        return Collections.unmodifiableCollection(blogEntries);
    }

}
