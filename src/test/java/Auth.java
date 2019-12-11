import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.testng.annotations.Test;


public class Auth {
    HttpGet request;
    CloseableHttpClient httpClient;
    String url ="";
    @Test
    private void sendGet() throws Exception {

        url = "http://fbadvisor-release.testingpe.com/";
         request = new HttpGet(url);
         httpClient = HttpClients.createDefault();

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            System.out.println(response.getStatusLine().toString());
            if(response.getStatusLine().getStatusCode()==401){
                System.out.println("PASS");
            }
            else {
                sendEmail(url,response);
                System.out.println("Failed "+response.getStatusLine().getStatusCode());
            }
        }

        url = "https://www.forbes.com/advisor/sitemap.xml";
        request = new HttpGet(url);
        httpClient = HttpClients.createDefault();

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            System.out.println(response.getStatusLine().toString());
            if(response.getStatusLine().getStatusCode()==200){
                System.out.println("PASS");
            }
            else {
                sendEmail(url,response);
                System.out.println("Failed "+response.getStatusLine().getStatusCode());
            }
        }

        url = "https://www.forbes.com/advisor/sitemap-news.xml";
        request = new HttpGet(url);
        httpClient = HttpClients.createDefault();

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            System.out.println(response.getStatusLine().toString());
            if(response.getStatusLine().getStatusCode()==200){
                System.out.println("PASS");
            }
            else {
                sendEmail(url,response);
                System.out.println("Failed "+response.getStatusLine().getStatusCode());
            }
        }
    }

    public void sendEmail(String url ,CloseableHttpResponse response) throws EmailException {

        String to []={"krish.t@media.net","viral.pa@media.net"};

        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("krish.t@media.net", "fstomkzuhqlyvcuv"));
        email.setSSLOnConnect(true);
        email.addTo(to);
        email.setFrom("krish.t@media.net", "Krish");
        email.setSubject("Check failed!");
        email.setMsg("URL = "+url+"\nGot this status code : "+response.getStatusLine().getStatusCode());
        email.send();
    }
}