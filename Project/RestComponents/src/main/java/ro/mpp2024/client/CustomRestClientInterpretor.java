package ro.mpp2024.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class CustomRestClientInterpretor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        System.out.println("Sending a "+request.getMethod()+ " request to "+request.getURI()+ " and body ["+new String(body)+"]");
        ClientHttpResponse response=null;
        try {
            response = execution.execute(request, body);
            System.out.println("Got response code " + response.getStatusCode());
        }catch(IOException ex){
            System.err.println("Eroare executie "+ex);
        }
        return response;
    }
}
