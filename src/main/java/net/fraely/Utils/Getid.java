package net.fraely.Utils;

import com.google.common.base.Splitter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

public class Getid {
    public static Long GetId(String uid) throws URISyntaxException {
        Long id;
        try {
            id = Long.parseLong(uid);
        } catch (NumberFormatException e) {
            final Splitter.MapSplitter querySplitter = Splitter.on('&').  omitEmptyStrings().trimResults().withKeyValueSeparator('=');
            URI uri = new URI(uid);
            Map<String, String> queries = querySplitter.split(uri.getQuery());
            id = Long.parseLong(Optional.ofNullable(queries.get("id")).orElseThrow(() -> new IllegalStateException("[TogetherMusic.ErrorKey]")));
        }
        return id;
    }
}
