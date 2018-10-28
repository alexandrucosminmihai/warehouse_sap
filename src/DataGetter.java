import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataDeltaFeed;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 */
public class DataGetter {
    public static final  String  HTTP_METHOD_PUT          = "PUT";
    public static final  String  HTTP_METHOD_POST         = "POST";
    public static final  String  HTTP_METHOD_GET          = "GET";
    public static final  String  HTTP_HEADER_CONTENT_TYPE = "Content-Type";
    public static final  String  HTTP_HEADER_ACCEPT       = "Accept";
    public static final  String  APPLICATION_JSON         = "application/json";
    public static final  String  APPLICATION_XML          = "application/xml";
    public static final  String  APPLICATION_ATOM_XML     =
            "application/atom+xml";
    public static final  String  APPLICATION_FORM         =
            "application/x-www-form-urlencoded";
    public static final  String  METADATA                 = "$metadata";
    public static final  String  INDEX                    = "/index.jsp";
    public static final  String  SEPARATOR                = "/";
    public static final  boolean PRINT_RAW_CONTENT        = true;
    private static final String  HTTP_METHOD_DELETE       = "DELETE";

    public static ODataFeed getEntriesFeed(
            String entityType, HashMap<String, String> args) throws Exception {
        Authenticator authenticator = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return (new PasswordAuthentication("TEAM04_USER",
                                                   "04TheBestRunSap@2018!"
                                                           .toCharArray()));
            }
        };
        Authenticator.setDefault(authenticator);

        String serviceUrl =
                "https://thebestrunsap2018z3d3pet6df.hana.ondemand.com" +
                "/ro/sap/hackathon/team04/service.xsodata/";

        DataGetter app = new DataGetter();
        Edm edm = app.readEdm(serviceUrl);

        return app.readFeed(
                edm, serviceUrl, APPLICATION_JSON, entityType, args);
    }

    public static ODataFeed getEntriesFeed(
            String entityType, String skip, String top) throws Exception {
        Authenticator authenticator = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return (new PasswordAuthentication("TEAM04_USER",
                                                   "04TheBestRunSap@2018!"
                                                           .toCharArray()));
            }
        };
        Authenticator.setDefault(authenticator);

        String serviceUrl =
                "https://thebestrunsap2018z3d3pet6df.hana.ondemand.com" +
                "/ro/sap/hackathon/team04/service.xsodata/";

        DataGetter app = new DataGetter();
        Edm edm = app.readEdm(serviceUrl);

        return app.readFeed(
                edm, serviceUrl, APPLICATION_JSON, entityType, skip, top);
    }

    public static ODataFeed getEntriesFeed(String entityType) throws Exception {
        Authenticator authenticator = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return (new PasswordAuthentication("TEAM04_USER",
                                                   "04TheBestRunSap@2018!"
                                                           .toCharArray()));
            }
        };
        Authenticator.setDefault(authenticator);

        String serviceUrl =
                "https://thebestrunsap2018z3d3pet6df.hana.ondemand.com" +
                "/ro/sap/hackathon/team04/service.xsodata/";

        DataGetter app = new DataGetter();
        Edm edm = app.readEdm(serviceUrl);

        return app.readFeed(edm, serviceUrl, APPLICATION_JSON, entityType);
    }

    private static String prettyPrint(ODataEntry createdEntry) {
        return prettyPrint(createdEntry.getProperties(), 0);
    }

    private static String prettyPrint(
            Map<String, Object> properties, int level) {
        StringBuilder b = new StringBuilder();
        Set<Entry<String, Object>> entries = properties.entrySet();

        for (Entry<String, Object> entry : entries) {
            intend(b, level);
            b.append(entry.getKey()).append(": ");
            Object value = entry.getValue();
            if (value instanceof Map) {
                value = prettyPrint((Map<String, Object>) value, level + 1);
                b.append(value).append("\n");
            } else if (value instanceof Calendar) {
                Calendar cal = (Calendar) value;
                value = SimpleDateFormat.getInstance().format(cal.getTime());
                b.append(value).append("\n");
            } else if (value instanceof ODataDeltaFeed) {
                ODataDeltaFeed feed = (ODataDeltaFeed) value;
                List<ODataEntry> inlineEntries = feed.getEntries();
                b.append("{");
                for (ODataEntry oDataEntry : inlineEntries) {
                    value = prettyPrint(
                            (Map<String, Object>) oDataEntry.getProperties(),
                            level + 1);
                    b.append("\n[\n").append(value).append("\n],");
                }
                b.deleteCharAt(b.length() - 1);
                intend(b, level);
                b.append("}\n");
            } else {
                b.append(value).append("\n");
            }
        }

        b.deleteCharAt(b.length() - 1);
        return b.toString();
    }

    private static void intend(StringBuilder builder, int intendLevel) {
        for (int i = 0; i < intendLevel; i++) {
            builder.append("  ");
        }
    }

    public void generateSampleData(String serviceUrl)
    throws MalformedURLException, IOException {
        String url = serviceUrl.substring(0, serviceUrl.lastIndexOf(SEPARATOR));
        HttpURLConnection connection = initializeConnection(url + INDEX,
                                                            APPLICATION_FORM,
                                                            HTTP_METHOD_POST);
        String content = "genSampleData=true";
        connection.getOutputStream().write(content.getBytes());
        print("Generate response: " + checkStatus(connection));
        connection.disconnect();
    }

    private HttpURLConnection initializeConnection(
            String absolutUri, String contentType, String httpMethod)
    throws MalformedURLException, IOException {
        URL url = new URL(absolutUri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(httpMethod);
        connection.setRequestProperty(HTTP_HEADER_ACCEPT, contentType);
        if (HTTP_METHOD_POST.equals(httpMethod) || HTTP_METHOD_PUT.equals(
                httpMethod)) {
            connection.setDoOutput(true);
            connection.setRequestProperty(HTTP_HEADER_CONTENT_TYPE,
                                          contentType);
        }

        return connection;
    }

    private static void print(String content) {
        System.out.println(content);
    }

    private HttpStatusCodes checkStatus(HttpURLConnection connection)
    throws IOException {
        HttpStatusCodes httpStatusCode = HttpStatusCodes.fromStatusCode(
                connection.getResponseCode());
        if (400 <= httpStatusCode.getStatusCode() &&
            httpStatusCode.getStatusCode() <= 599) {
            throw new RuntimeException("Http Connection failed with status " +
                                       httpStatusCode.getStatusCode() + " " +
                                       httpStatusCode.toString());
        }
        return httpStatusCode;
    }

    public Edm readEdm(String serviceUrl) throws IOException, ODataException {
        InputStream content = execute(serviceUrl + SEPARATOR + METADATA,
                                      APPLICATION_XML, HTTP_METHOD_GET);
        return EntityProvider.readMetadata(content, false);
    }

    public ODataFeed readFeed(
            Edm edm, String serviceUri, String contentType,
            String entitySetName, HashMap<String, String> args)
    throws IOException, ODataException {
        EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
        String absolutUri = createUri(serviceUri, entitySetName, null, args);

        InputStream content = execute(absolutUri, contentType, HTTP_METHOD_GET);
        return EntityProvider.readFeed(contentType, entityContainer
                                                            .getEntitySet(
                                                                    entitySetName),
                                       content,
                                       EntityProviderReadProperties.init()
                                                                   .build());
    }

    public ODataFeed readFeed(
            Edm edm, String serviceUri, String contentType,
            String entitySetName, String skip, String top)
    throws IOException, ODataException {
        EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
        String absolutUri = createUri(serviceUri, entitySetName, null, skip,
                                      top);

        InputStream content = execute(absolutUri, contentType, HTTP_METHOD_GET);
        return EntityProvider.readFeed(contentType, entityContainer
                                                            .getEntitySet(
                                                                    entitySetName),
                                       content,
                                       EntityProviderReadProperties.init()
                                                                   .build());
    }

    public ODataFeed readFeed(
            Edm edm, String serviceUri, String contentType,
            String entitySetName, String top)
    throws IOException, ODataException {
        EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
        String absolutUri = createUri(serviceUri, entitySetName, null, top);

        InputStream content = execute(absolutUri, contentType, HTTP_METHOD_GET);
        return EntityProvider.readFeed(contentType, entityContainer
                                                            .getEntitySet(
                                                                    entitySetName),
                                       content,
                                       EntityProviderReadProperties.init()
                                                                   .build());
    }

    private String createUri(
            String serviceUri, String entitySetName, String id, String top) {
        final StringBuilder absolutUri = new StringBuilder(serviceUri).append(
                SEPARATOR).append(entitySetName);
        if (id != null) {
            absolutUri.append("(").append(id).append(")");
        }
        if (top != null) {
            absolutUri.append("/?$top=").append(top);
        }
        return absolutUri.toString();
    }

    private InputStream execute(
            String relativeUri, String contentType, String httpMethod)
    throws IOException {
        HttpURLConnection connection = initializeConnection(relativeUri,
                                                            contentType,
                                                            httpMethod);

        connection.connect();
        checkStatus(connection);

        InputStream content = connection.getInputStream();
        content = logRawContent(httpMethod + " request on uri '" + relativeUri +
                                "' with content:\n  ", content, "\n");
        return content;
    }

    private InputStream logRawContent(
            String prefix, InputStream content, String postfix)
    throws IOException {
        if (PRINT_RAW_CONTENT) {
            byte[] buffer = streamToArray(content);

            return new ByteArrayInputStream(buffer);
        }
        return content;
    }

    private byte[] streamToArray(InputStream stream) throws IOException {
        byte[] result = new byte[0];
        byte[] tmp = new byte[8192];
        int readCount = stream.read(tmp);
        while (readCount >= 0) {
            byte[] innerTmp = new byte[result.length + readCount];
            System.arraycopy(result, 0, innerTmp, 0, result.length);
            System.arraycopy(tmp, 0, innerTmp, result.length, readCount);
            result = innerTmp;
            readCount = stream.read(tmp);
        }
        stream.close();
        return result;
    }

    public ODataFeed readFeed(
            Edm edm, String serviceUri, String contentType,
            String entitySetName) throws IOException, ODataException {
        EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
        String absolutUri = createUri(serviceUri, entitySetName, null);

        InputStream content = execute(absolutUri, contentType, HTTP_METHOD_GET);
        return EntityProvider.readFeed(contentType, entityContainer
                                                            .getEntitySet(
                                                                    entitySetName),
                                       content,
                                       EntityProviderReadProperties.init()
                                                                   .build());
    }

    public ODataEntry readEntry(
            Edm edm, String serviceUri, String contentType,
            String entitySetName, String keyValue)
    throws IOException, ODataException {
        return readEntry(edm, serviceUri, contentType, entitySetName, keyValue,
                         null);
    }

    public ODataEntry readEntry(
            Edm edm, String serviceUri, String contentType,
            String entitySetName, String keyValue, String expandRelationName)
    throws IOException, ODataException {
        // working with the default entity container
        EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
        // create absolute uri based on service uri, entity set name with its
        // key property value and optional expanded relation name
        String absolutUri = createUri(serviceUri, entitySetName, keyValue,
                                      expandRelationName);

        InputStream content = execute(absolutUri, contentType, HTTP_METHOD_GET);

        return EntityProvider.readEntry(contentType, entityContainer
                                                             .getEntitySet(
                                                                     entitySetName),
                                        content,
                                        EntityProviderReadProperties.init()
                                                                    .build());
    }

    public ODataEntry createEntry(
            Edm edm, String serviceUri, String contentType,
            String entitySetName, Map<String, Object> data) throws Exception {
        String absolutUri = createUri(serviceUri, entitySetName, null);
        return writeEntity(edm, absolutUri, entitySetName, data, contentType,
                           HTTP_METHOD_POST);
    }

    private String createUri(
            String serviceUri, String entitySetName, String id) {
        return createUri(serviceUri, entitySetName, id, (String) null);
    }

    private ODataEntry writeEntity(
            Edm edm, String absolutUri, String entitySetName,
            Map<String, Object> data, String contentType, String httpMethod)
    throws EdmException, MalformedURLException, IOException,
           EntityProviderException, URISyntaxException {

        HttpURLConnection connection = initializeConnection(absolutUri,
                                                            contentType,
                                                            httpMethod);

        EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
        EdmEntitySet entitySet = entityContainer.getEntitySet(entitySetName);
        URI rootUri = new URI(entitySetName);

        EntityProviderWriteProperties properties =
                EntityProviderWriteProperties.serviceRoot(rootUri).build();
        // serialize data into ODataResponse object
        ODataResponse response = EntityProvider.writeEntry(contentType,
                                                           entitySet, data,
                                                           properties);
        // get (http) entity which is for default Olingo implementation an
        // InputStream
        Object entity = response.getEntity();
        if (entity instanceof InputStream) {
            byte[] buffer = streamToArray((InputStream) entity);
            connection.getOutputStream().write(buffer);
        }

        // if a entity is created (via POST request) the response body
        // contains the new created entity
        ODataEntry entry = null;
        HttpStatusCodes statusCode = HttpStatusCodes.fromStatusCode(
                connection.getResponseCode());
        if (statusCode == HttpStatusCodes.CREATED) {
            // get the content as InputStream and de-serialize it into an
            // ODataEntry object
            InputStream content = connection.getInputStream();
            content = logRawContent(
                    httpMethod + " request on uri '" + absolutUri +
                    "' with content:\n  ", content, "\n");
            entry = EntityProvider.readEntry(contentType, entitySet, content,
                                             EntityProviderReadProperties.init()
                                                                         .build());
        }

        //
        connection.disconnect();

        return entry;
    }

    public void updateEntry(
            Edm edm, String serviceUri, String contentType,
            String entitySetName, String id, Map<String, Object> data)
    throws Exception {
        String absolutUri = createUri(serviceUri, entitySetName, id);
        writeEntity(edm, absolutUri, entitySetName, data, contentType,
                    HTTP_METHOD_PUT);
    }

    public HttpStatusCodes deleteEntry(
            String serviceUri, String entityName, String id)
    throws IOException {
        String absolutUri = createUri(serviceUri, entityName, id);
        HttpURLConnection connection = connect(absolutUri, APPLICATION_XML,
                                               HTTP_METHOD_DELETE);
        return HttpStatusCodes.fromStatusCode(connection.getResponseCode());
    }

    private HttpURLConnection connect(
            String relativeUri, String contentType, String httpMethod)
    throws IOException {
        HttpURLConnection connection = initializeConnection(relativeUri,
                                                            contentType,
                                                            httpMethod);

        connection.connect();
        checkStatus(connection);

        return connection;
    }

    private String createUri(
            String serviceUri, String entitySetName, String id,
            HashMap<String, String> args) {
        final StringBuilder absolutUri = new StringBuilder(serviceUri).append(
                SEPARATOR).append(entitySetName);
        if (id != null) {
            absolutUri.append("(").append(id).append(")");
        }

        for (Entry<String, String> arg : args.entrySet()) {
            absolutUri.append("/?&" + arg.getKey() + "=").append(
                    arg.getValue());
        }
        return absolutUri.toString();
    }

    private String createUri(
            String serviceUri, String entitySetName, String id, String skip,
            String top) {
        final StringBuilder absolutUri = new StringBuilder(serviceUri).append(
                SEPARATOR).append(entitySetName);
        if (id != null) {
            absolutUri.append("(").append(id).append(")");
        }
        if (skip != null) {
            absolutUri.append("/?$skip=").append(skip);
        }
        if (top != null) {
            absolutUri.append("/?$top=").append(top);
        }
        return absolutUri.toString();
    }
}
