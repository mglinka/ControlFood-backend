package pl.lodz.pl.it.config;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleApiConfig {
  @Bean
  public HttpTransport httpTransport() {
    return new NetHttpTransport(); // Use Google's default HTTP transport implementation
  }

  @Bean
  public JsonFactory jsonFactory() {
    return new GsonFactory(); // Use GsonFactory for JSON parsing
  }
}
